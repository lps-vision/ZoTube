package com.example.data.model

data class VideoItem(
    val id: String,
    val title: String,
    val channelTitle: String,
    val thumbnailUrl: String,
    val description: String = "",
    val publishedAt: String = "",
    val duration: String = "",
    val viewCount: String = "",
    val category: String = "General"
) {
    val embedUrl: String
        get() = "https://www.youtube-nocookie.com/embed/$id?autoplay=1&controls=1&rel=0&modestbranding=1&iv_load_policy=3&playsinline=1&enablejsapi=1"

    companion object {
        /**
         * Extracts YouTube video ID from various URL formats or raw ID string.
         * Supported formats:
         * - Raw 11-char ID (e.g. dQw4w9WgXcQ)
         * - https://www.youtube.com/watch?v=...
         * - https://youtu.be/...
         * - https://youtube.com/shorts/...
         * - https://www.youtube.com/embed/...
         * - https://music.youtube.com/watch?v=...
         */
        fun extractVideoId(input: String): String? {
            val trimmed = input.trim()
            if (trimmed.isEmpty()) return null

            // If it's already an 11-char alphanumeric ID
            val rawIdRegex = Regex("^[a-zA-Z0-9_-]{11}$")
            if (rawIdRegex.matches(trimmed)) {
                return trimmed
            }

            // Standard URL patterns
            val patterns = listOf(
                Regex("""(?:https?://)?(?:www\.)?youtube\.com/watch\?v=([a-zA-Z0-9_-]{11})"""),
                Regex("""(?:https?://)?youtu\.be/([a-zA-Z0-9_-]{11})"""),
                Regex("""(?:https?://)?(?:www\.)?youtube\.com/shorts/([a-zA-Z0-9_-]{11})"""),
                Regex("""(?:https?://)?(?:www\.)?youtube\.com/embed/([a-zA-Z0-9_-]{11})"""),
                Regex("""(?:https?://)?music\.youtube\.com/watch\?v=([a-zA-Z0-9_-]{11})""")
            )

            for (pattern in patterns) {
                val match = pattern.find(trimmed)
                if (match != null && match.groupValues.size > 1) {
                    return match.groupValues[1]
                }
            }

            // Fallback query parameter check
            if (trimmed.contains("v=")) {
                val sub = trimmed.substringAfter("v=").substringBefore("&")
                if (rawIdRegex.matches(sub)) {
                    return sub
                }
            }

            return null
        }
    }
}
