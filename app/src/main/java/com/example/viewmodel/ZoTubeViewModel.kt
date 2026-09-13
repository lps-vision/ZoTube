package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.BookmarkEntity
import com.example.data.local.WatchHistoryEntity
import com.example.data.local.ZoTubeDatabase
import com.example.data.model.VideoItem
import com.example.data.repository.CuratedCatalog
import com.example.data.repository.YouTubeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class TvNavDestination {
    HOME,
    SEARCH,
    CATEGORIES,
    DIRECT_PLAY,
    LIBRARY,
    SETTINGS
}

data class UiFeedState(
    val isLoading: Boolean = false,
    val videos: List<VideoItem> = emptyList(),
    val error: String? = null
)

data class ByokState(
    val customApiKey: String = "",
    val effectiveApiKey: String = "",
    val hasCustomKey: Boolean = false,
    val source: YouTubeRepository.ApiKeySource = YouTubeRepository.ApiKeySource.NONE
)

class ZoTubeViewModel(application: Application) : AndroidViewModel(application) {

    private val db = ZoTubeDatabase.getDatabase(application)
    private val repository = YouTubeRepository(application, db.zoTubeDao())

    // Navigation State
    private val _currentDestination = MutableStateFlow(TvNavDestination.HOME)
    val currentDestination: StateFlow<TvNavDestination> = _currentDestination.asStateFlow()

    // Active Category
    private val _selectedCategory = MutableStateFlow("Trending")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    // Home / Category Feed State
    private val _feedState = MutableStateFlow(UiFeedState(isLoading = true))
    val feedState: StateFlow<UiFeedState> = _feedState.asStateFlow()

    // Search State
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _searchState = MutableStateFlow(UiFeedState())
    val searchState: StateFlow<UiFeedState> = _searchState.asStateFlow()

    // Active Playing Video (null if not playing)
    private val _activeVideo = MutableStateFlow<VideoItem?>(null)
    val activeVideo: StateFlow<VideoItem?> = _activeVideo.asStateFlow()

    // BYOK & Settings
    private val _byokState = MutableStateFlow(loadByokState())
    val byokState: StateFlow<ByokState> = _byokState.asStateFlow()

    private val _apiKey = MutableStateFlow(repository.getEffectiveApiKey())
    val apiKey: StateFlow<String> = _apiKey.asStateFlow()

    // History and Bookmarks from Room
    val watchHistory: StateFlow<List<WatchHistoryEntity>> = repository.watchHistory
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val bookmarks: StateFlow<List<BookmarkEntity>> = repository.bookmarks
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val recentSearches: StateFlow<List<String>> = repository.recentSearches
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Ad-Shield metrics
    private val _adsBlockedCount = MutableStateFlow(14)
    val adsBlockedCount: StateFlow<Int> = _adsBlockedCount.asStateFlow()

    init {
        loadCategoryVideos("Trending")
    }

    fun navigateTo(destination: TvNavDestination) {
        _currentDestination.value = destination
    }

    fun selectCategory(category: String) {
        _selectedCategory.value = category
        loadCategoryVideos(category)
    }

    fun loadCategoryVideos(category: String) {
        viewModelScope.launch {
            _feedState.value = UiFeedState(isLoading = true)
            repository.getTrendingVideos(category)
                .catch { err ->
                    _feedState.value = UiFeedState(
                        isLoading = false,
                        videos = CuratedCatalog.getCategoryVideos(category),
                        error = err.message
                    )
                }
                .collect { list ->
                    _feedState.value = UiFeedState(
                        isLoading = false,
                        videos = list
                    )
                }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun executeSearch(query: String = _searchQuery.value) {
        val trimmed = query.trim()
        if (trimmed.isEmpty()) return

        _searchQuery.value = trimmed
        viewModelScope.launch {
            _searchState.value = UiFeedState(isLoading = true)
            repository.searchVideos(trimmed)
                .catch { err ->
                    _searchState.value = UiFeedState(isLoading = false, error = err.message)
                }
                .collect { results ->
                    _searchState.value = UiFeedState(
                        isLoading = false,
                        videos = results
                    )
                }
        }
    }

    fun playVideo(video: VideoItem) {
        _activeVideo.value = video
        _adsBlockedCount.value += 1
        viewModelScope.launch {
            repository.recordWatch(video)
        }
    }

    fun closePlayer() {
        _activeVideo.value = null
    }

    fun playDirect(urlOrId: String): Boolean {
        val videoId = VideoItem.extractVideoId(urlOrId) ?: return false
        val directVideo = VideoItem(
            id = videoId,
            title = "Video ($videoId)",
            channelTitle = "YouTube Direct Play",
            thumbnailUrl = "https://img.youtube.com/vi/$videoId/hqdefault.jpg",
            description = "Direct play without ads via ZoTube Ad-Shield.",
            publishedAt = "Ad-Free Streaming",
            duration = "Now Playing",
            viewCount = "Direct",
            category = "Direct Play"
        )
        playVideo(directVideo)
        return true
    }

    fun isBookmarked(videoId: String): Boolean {
        return bookmarks.value.any { it.videoId == videoId }
    }

    fun toggleBookmark(video: VideoItem) {
        val currentlyBookmarked = isBookmarked(video.id)
        viewModelScope.launch {
            repository.toggleBookmark(video, currentlyBookmarked)
        }
    }

    fun clearHistory() {
        viewModelScope.launch {
            repository.clearHistory()
        }
    }

    fun clearSearchHistory() {
        viewModelScope.launch {
            repository.clearSearchHistory()
        }
    }

    private fun loadByokState(): ByokState {
        return ByokState(
            customApiKey = repository.getCustomApiKey(),
            effectiveApiKey = repository.getEffectiveApiKey(),
            hasCustomKey = repository.hasCustomApiKey(),
            source = repository.getApiKeySource()
        )
    }

    private fun refreshApiKeyStates() {
        val newState = loadByokState()
        _byokState.value = newState
        _apiKey.value = newState.effectiveApiKey
    }

    fun saveCustomApiKey(newKey: String) {
        val trimmed = newKey.trim()
        repository.saveCustomApiKey(trimmed)
        refreshApiKeyStates()
        // Refresh feed with new key
        loadCategoryVideos(_selectedCategory.value)
    }

    fun clearCustomApiKey() {
        repository.clearCustomApiKey()
        refreshApiKeyStates()
        // Refresh feed with fallback
        loadCategoryVideos(_selectedCategory.value)
    }

    fun saveApiKey(newKey: String) {
        val trimmed = newKey.trim()
        if (trimmed.isEmpty()) {
            clearCustomApiKey()
        } else {
            saveCustomApiKey(trimmed)
        }
    }
}
