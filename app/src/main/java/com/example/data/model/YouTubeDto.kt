package com.example.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class YouTubeSearchResponse(
    @Json(name = "items") val items: List<YouTubeSearchItem>? = null,
    @Json(name = "nextPageToken") val nextPageToken: String? = null
)

@JsonClass(generateAdapter = true)
data class YouTubeSearchItem(
    @Json(name = "id") val id: YouTubeSearchId? = null,
    @Json(name = "snippet") val snippet: YouTubeSnippet? = null
)

@JsonClass(generateAdapter = true)
data class YouTubeSearchId(
    @Json(name = "kind") val kind: String? = null,
    @Json(name = "videoId") val videoId: String? = null
)

@JsonClass(generateAdapter = true)
data class YouTubeVideosResponse(
    @Json(name = "items") val items: List<YouTubeVideoResultItem>? = null,
    @Json(name = "nextPageToken") val nextPageToken: String? = null
)

@JsonClass(generateAdapter = true)
data class YouTubeVideoResultItem(
    @Json(name = "id") val id: String? = null,
    @Json(name = "snippet") val snippet: YouTubeSnippet? = null,
    @Json(name = "contentDetails") val contentDetails: YouTubeContentDetails? = null,
    @Json(name = "statistics") val statistics: YouTubeStatistics? = null
)

@JsonClass(generateAdapter = true)
data class YouTubeSnippet(
    @Json(name = "title") val title: String? = null,
    @Json(name = "description") val description: String? = null,
    @Json(name = "channelTitle") val channelTitle: String? = null,
    @Json(name = "publishedAt") val publishedAt: String? = null,
    @Json(name = "thumbnails") val thumbnails: YouTubeThumbnails? = null
)

@JsonClass(generateAdapter = true)
data class YouTubeThumbnails(
    @Json(name = "default") val defaultThumb: YouTubeThumbnailInfo? = null,
    @Json(name = "medium") val mediumThumb: YouTubeThumbnailInfo? = null,
    @Json(name = "high") val highThumb: YouTubeThumbnailInfo? = null,
    @Json(name = "maxres") val maxresThumb: YouTubeThumbnailInfo? = null
)

@JsonClass(generateAdapter = true)
data class YouTubeThumbnailInfo(
    @Json(name = "url") val url: String? = null,
    @Json(name = "width") val width: Int? = null,
    @Json(name = "height") val height: Int? = null
)

@JsonClass(generateAdapter = true)
data class YouTubeContentDetails(
    @Json(name = "duration") val duration: String? = null
)

@JsonClass(generateAdapter = true)
data class YouTubeStatistics(
    @Json(name = "viewCount") val viewCount: String? = null,
    @Json(name = "likeCount") val likeCount: String? = null
)

// Invidious / Public Search Item DTO for zero-config fallback
@JsonClass(generateAdapter = true)
data class InvidiousVideoItem(
    @Json(name = "videoId") val videoId: String? = null,
    @Json(name = "title") val title: String? = null,
    @Json(name = "author") val author: String? = null,
    @Json(name = "videoThumbnails") val videoThumbnails: List<InvidiousThumbnail>? = null,
    @Json(name = "description") val description: String? = null,
    @Json(name = "publishedText") val publishedText: String? = null,
    @Json(name = "viewCount") val viewCount: Long? = null,
    @Json(name = "lengthSeconds") val lengthSeconds: Int? = null
)

@JsonClass(generateAdapter = true)
data class InvidiousThumbnail(
    @Json(name = "quality") val quality: String? = null,
    @Json(name = "url") val url: String? = null,
    @Json(name = "width") val width: Int? = null,
    @Json(name = "height") val height: Int? = null
)
