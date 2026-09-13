package com.example.data.remote

import com.example.data.model.InvidiousVideoItem
import com.example.data.model.YouTubeSearchResponse
import com.example.data.model.YouTubeVideosResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface YouTubeApiService {
    @GET("youtube/v3/search")
    suspend fun searchVideos(
        @Query("part") part: String = "snippet",
        @Query("q") query: String,
        @Query("type") type: String = "video",
        @Query("maxResults") maxResults: Int = 30,
        @Query("key") apiKey: String? = null
    ): Response<YouTubeSearchResponse>

    @GET("youtube/v3/videos")
    suspend fun getPopularVideos(
        @Query("part") part: String = "snippet,contentDetails,statistics",
        @Query("chart") chart: String = "mostPopular",
        @Query("maxResults") maxResults: Int = 30,
        @Query("regionCode") regionCode: String = "US",
        @Query("videoCategoryId") categoryId: String? = null,
        @Query("key") apiKey: String? = null
    ): Response<YouTubeVideosResponse>

    @GET("youtube/v3/videos")
    suspend fun getVideoDetails(
        @Query("part") part: String = "snippet,contentDetails,statistics",
        @Query("id") videoId: String,
        @Query("key") apiKey: String? = null
    ): Response<YouTubeVideosResponse>
}

interface InvidiousApiService {
    @GET("api/v1/search")
    suspend fun searchVideos(
        @Query("q") query: String,
        @Query("type") type: String = "video"
    ): Response<List<InvidiousVideoItem>>

    @GET("api/v1/trending")
    suspend fun getTrendingVideos(
        @Query("type") type: String = "music"
    ): Response<List<InvidiousVideoItem>>
}
