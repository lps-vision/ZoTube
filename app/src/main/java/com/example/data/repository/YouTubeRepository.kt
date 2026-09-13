package com.example.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.BuildConfig
import com.example.data.local.BookmarkEntity
import com.example.data.local.SearchHistoryEntity
import com.example.data.local.WatchHistoryEntity
import com.example.data.local.ZoTubeDao
import com.example.data.model.VideoItem
import com.example.data.remote.InvidiousApiService
import com.example.data.remote.YouTubeApiKeyInterceptor
import com.example.data.remote.YouTubeApiService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

class YouTubeRepository(
    private val context: Context,
    private val dao: ZoTubeDao
) {
    companion object {
        private const val PREFS_NAME = "zotube_preferences"
        const val KEY_CUSTOM_API_KEY = "custom_youtube_api_key"
    }

    enum class ApiKeySource {
        CUSTOM,            // User entered custom key (BYOK)
        DEFAULT_CONFIG,    // Sourced from BuildConfig.YOUTUBE_API_KEY
        NONE               // No key configured, running on standalone fallback
    }

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    private val moshi: Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(YouTubeApiKeyInterceptor { getEffectiveApiKey() })
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .build()

    private val youtubeApi: YouTubeApiService = Retrofit.Builder()
        .baseUrl("https://www.googleapis.com/")
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
        .create(YouTubeApiService::class.java)

    private val invidiousApi: InvidiousApiService = Retrofit.Builder()
        .baseUrl("https://inv.tux.pizza/")
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
        .create(InvidiousApiService::class.java)

    /**
     * Retrieves the user-configured custom YouTube Data API v3 key from persistent storage.
     */
    fun getCustomApiKey(): String {
        return prefs.getString(KEY_CUSTOM_API_KEY, "")?.trim() ?: ""
    }

    /**
     * Checks if a user has entered and saved a custom API key.
     */
    fun hasCustomApiKey(): Boolean {
        return getCustomApiKey().isNotEmpty()
    }

    /**
     * Persistently saves the user's custom YouTube API key in SharedPreferences.
     */
    fun saveCustomApiKey(apiKey: String) {
        prefs.edit().putString(KEY_CUSTOM_API_KEY, apiKey.trim()).apply()
    }

    /**
     * Removes the custom API key from persistent storage.
     */
    fun clearCustomApiKey() {
        prefs.edit().remove(KEY_CUSTOM_API_KEY).apply()
    }

    /**
     * Resolves the effective API key:
     * 1. User's custom API key (BYOK) takes highest precedence.
     * 2. Default BuildConfig API key (if available).
     * 3. Empty string if no key is configured.
     */
    fun getEffectiveApiKey(): String {
        val customKey = getCustomApiKey()
        if (customKey.isNotEmpty()) {
            return customKey
        }
        return getBuildConfigApiKey()
    }

    /**
     * Determines where the current active API key comes from.
     */
    fun getApiKeySource(): ApiKeySource {
        return when {
            hasCustomApiKey() -> ApiKeySource.CUSTOM
            getBuildConfigApiKey().isNotEmpty() -> ApiKeySource.DEFAULT_CONFIG
            else -> ApiKeySource.NONE
        }
    }

    /**
     * Backward-compatible getter for active API key.
     */
    fun getApiKey(): String = getEffectiveApiKey()

    /**
     * Backward-compatible setter for API key.
     */
    fun setApiKey(apiKey: String) {
        if (apiKey.isBlank()) {
            clearCustomApiKey()
        } else {
            saveCustomApiKey(apiKey)
        }
    }

    private fun getBuildConfigApiKey(): String {
        return try {
            val field = BuildConfig::class.java.getField("YOUTUBE_API_KEY")
            val key = field.get(null) as? String
            if (key != null && key.isNotBlank() && !key.contains("YOUR_")) key.trim() else ""
        } catch (e: Throwable) {
            ""
        }
    }

    fun getTrendingVideos(category: String = "Trending"): Flow<List<VideoItem>> = flow {
        val apiKey = getEffectiveApiKey()
        if (apiKey.isNotEmpty()) {
            try {
                val categoryId = when (category) {
                    "Music" -> "10"
                    "Gaming" -> "20"
                    "News" -> "25"
                    "Movies & Animation" -> "1"
                    "Tech & Science" -> "28"
                    else -> null
                }
                val response = youtubeApi.getPopularVideos(categoryId = categoryId)
                if (response.isSuccessful && response.body()?.items != null) {
                    val list = response.body()!!.items!!.mapNotNull { item ->
                        val id = item.id ?: return@mapNotNull null
                        val snippet = item.snippet ?: return@mapNotNull null
                        VideoItem(
                            id = id,
                            title = snippet.title ?: "Untitled",
                            channelTitle = snippet.channelTitle ?: "YouTube Creator",
                            thumbnailUrl = snippet.thumbnails?.highThumb?.url
                                ?: snippet.thumbnails?.mediumThumb?.url
                                ?: "https://img.youtube.com/vi/$id/hqdefault.jpg",
                            description = snippet.description ?: "",
                            publishedAt = snippet.publishedAt ?: "",
                            duration = item.contentDetails?.duration ?: "",
                            viewCount = item.statistics?.viewCount?.let { "$it views" } ?: "",
                            category = category
                        )
                    }
                    if (list.isNotEmpty()) {
                        emit(list)
                        return@flow
                    }
                }
            } catch (_: Throwable) {
                // Fallback to curated category on quota error or failure
            }
        }

        // Curated fallback
        emit(CuratedCatalog.getCategoryVideos(category))
    }.flowOn(Dispatchers.IO)

    fun searchVideos(query: String): Flow<List<VideoItem>> = flow {
        val cleanQuery = query.trim()
        if (cleanQuery.isEmpty()) {
            emit(emptyList())
            return@flow
        }

        // Save to search history
        dao.insertSearch(SearchHistoryEntity(query = cleanQuery))

        // Direct URL or direct Video ID check
        val extractedId = VideoItem.extractVideoId(cleanQuery)
        if (extractedId != null) {
            emit(listOf(
                VideoItem(
                    id = extractedId,
                    title = "Direct Video ($extractedId)",
                    channelTitle = "YouTube Video",
                    thumbnailUrl = "https://img.youtube.com/vi/$extractedId/hqdefault.jpg",
                    description = "Direct YouTube video loaded from URL or Video ID.",
                    publishedAt = "Ready to play",
                    duration = "Full Video",
                    viewCount = "Direct Play",
                    category = "Direct"
                )
            ))
            return@flow
        }

        val apiKey = getEffectiveApiKey()
        if (apiKey.isNotEmpty()) {
            try {
                val response = youtubeApi.searchVideos(query = cleanQuery)
                if (response.isSuccessful && response.body()?.items != null) {
                    val list = response.body()!!.items!!.mapNotNull { item ->
                        val videoId = item.id?.videoId ?: return@mapNotNull null
                        val snippet = item.snippet ?: return@mapNotNull null
                        VideoItem(
                            id = videoId,
                            title = snippet.title ?: "YouTube Video",
                            channelTitle = snippet.channelTitle ?: "Creator",
                            thumbnailUrl = snippet.thumbnails?.highThumb?.url
                                ?: snippet.thumbnails?.mediumThumb?.url
                                ?: "https://img.youtube.com/vi/$videoId/hqdefault.jpg",
                            description = snippet.description ?: "",
                            publishedAt = snippet.publishedAt ?: "",
                            category = "Search"
                        )
                    }
                    if (list.isNotEmpty()) {
                        emit(list)
                        return@flow
                    }
                }
            } catch (_: Throwable) {
                // Fallback to Invidious public instance or catalog search
            }
        }

        // Invidious API Fallback
        try {
            val invResponse = invidiousApi.searchVideos(query = cleanQuery)
            if (invResponse.isSuccessful && !invResponse.body().isNullOrEmpty()) {
                val invList = invResponse.body()!!.mapNotNull { item ->
                    val vid = item.videoId ?: return@mapNotNull null
                    val thumb = item.videoThumbnails?.firstOrNull()?.url
                        ?: "https://img.youtube.com/vi/$vid/hqdefault.jpg"
                    VideoItem(
                        id = vid,
                        title = item.title ?: "Video",
                        channelTitle = item.author ?: "YouTube Creator",
                        thumbnailUrl = thumb,
                        description = item.description ?: "",
                        publishedAt = item.publishedText ?: "",
                        viewCount = item.viewCount?.let { "$it views" } ?: "",
                        category = "Search"
                    )
                }
                if (invList.isNotEmpty()) {
                    emit(invList)
                    return@flow
                }
            }
        } catch (_: Throwable) {
            // Local curated search match
        }

        // Fallback: search among curated items matching query terms
        val allCurated = CuratedCatalog.CATEGORIES.flatMap { CuratedCatalog.getCategoryVideos(it) }
        val matches = allCurated.filter {
            it.title.contains(cleanQuery, ignoreCase = true) ||
            it.channelTitle.contains(cleanQuery, ignoreCase = true) ||
            it.category.contains(cleanQuery, ignoreCase = true)
        }.distinctBy { it.id }

        if (matches.isNotEmpty()) {
            emit(matches)
        } else {
            // If no exact match, return general trending so user never sees empty dead-end
            emit(CuratedCatalog.TRENDING_VIDEOS)
        }
    }.flowOn(Dispatchers.IO)

    // History and Bookmarks
    val watchHistory: Flow<List<WatchHistoryEntity>> = dao.getAllHistory()
    val bookmarks: Flow<List<BookmarkEntity>> = dao.getAllBookmarks()
    val recentSearches: Flow<List<String>> = dao.getRecentSearches().map { list ->
        list.map { it.query }.distinct()
    }

    suspend fun recordWatch(video: VideoItem) {
        dao.insertHistory(
            WatchHistoryEntity(
                videoId = video.id,
                title = video.title,
                channelTitle = video.channelTitle,
                thumbnailUrl = video.thumbnailUrl,
                duration = video.duration,
                viewCount = video.viewCount
            )
        )
    }

    fun isBookmarked(videoId: String): Flow<Boolean> = dao.isBookmarked(videoId)

    suspend fun toggleBookmark(video: VideoItem, isCurrentlyBookmarked: Boolean) {
        if (isCurrentlyBookmarked) {
            dao.deleteBookmark(video.id)
        } else {
            dao.insertBookmark(
                BookmarkEntity(
                    videoId = video.id,
                    title = video.title,
                    channelTitle = video.channelTitle,
                    thumbnailUrl = video.thumbnailUrl,
                    duration = video.duration
                )
            )
        }
    }

    suspend fun clearHistory() {
        dao.clearHistory()
    }

    suspend fun clearSearchHistory() {
        dao.clearSearchHistory()
    }
}
