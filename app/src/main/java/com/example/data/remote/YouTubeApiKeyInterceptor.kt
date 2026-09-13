package com.example.data.remote

import okhttp3.Interceptor
import okhttp3.Response

/**
 * OkHttp Interceptor for YouTube Data API v3 calls.
 * Dynamically resolves and appends the YouTube Data API key.
 *
 * Priority order:
 * 1. User-provided custom API key (Bring Your Own Key - BYOK)
 * 2. Default BuildConfig API key
 * 3. If neither is present, request proceeds without query key, triggering repository fallbacks.
 */
class YouTubeApiKeyInterceptor(
    private val apiKeyProvider: () -> String
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val originalUrl = originalRequest.url

        // Only inject API key for Google APIs
        if (!originalUrl.host.contains("googleapis.com")) {
            return chain.proceed(originalRequest)
        }

        val activeKey = apiKeyProvider().trim()

        val newUrl = if (activeKey.isNotEmpty()) {
            // Guarantee that user's custom key or active key replaces any placeholder key
            originalUrl.newBuilder()
                .removeAllQueryParameters("key")
                .addQueryParameter("key", activeKey)
                .build()
        } else {
            originalUrl
        }

        val newRequest = originalRequest.newBuilder()
            .url(newUrl)
            .build()

        return chain.proceed(newRequest)
    }
}
