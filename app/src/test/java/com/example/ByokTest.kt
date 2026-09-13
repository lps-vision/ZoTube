package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.local.ZoTubeDatabase
import com.example.data.remote.YouTubeApiKeyInterceptor
import com.example.data.repository.YouTubeRepository
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ByokTest {

    private lateinit var context: Context
    private lateinit var repository: YouTubeRepository

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        // Clear preferences before each test
        context.getSharedPreferences("zotube_preferences", Context.MODE_PRIVATE)
            .edit()
            .clear()
            .commit()

        val db = ZoTubeDatabase.getDatabase(context)
        repository = YouTubeRepository(context, db.zoTubeDao())
    }

    @Test
    fun test_initial_state_has_no_custom_key() {
        assertFalse(repository.hasCustomApiKey())
        assertEquals("", repository.getCustomApiKey())
    }

    @Test
    fun test_save_and_retrieve_custom_api_key_persists() {
        val testKey = "AIzaSyCustomKey_ABC_123"
        repository.saveCustomApiKey(testKey)

        assertTrue(repository.hasCustomApiKey())
        assertEquals(testKey, repository.getCustomApiKey())
        assertEquals(testKey, repository.getEffectiveApiKey())
        assertEquals(YouTubeRepository.ApiKeySource.CUSTOM, repository.getApiKeySource())

        // Verify direct SharedPreferences persistence
        val sharedPrefs = context.getSharedPreferences("zotube_preferences", Context.MODE_PRIVATE)
        assertEquals(testKey, sharedPrefs.getString(YouTubeRepository.KEY_CUSTOM_API_KEY, null))
    }

    @Test
    fun test_clear_custom_api_key_removes_from_storage() {
        val testKey = "AIzaSyKeyToBeCleared"
        repository.saveCustomApiKey(testKey)
        assertTrue(repository.hasCustomApiKey())

        repository.clearCustomApiKey()

        assertFalse(repository.hasCustomApiKey())
        assertEquals("", repository.getCustomApiKey())

        // Verify cleared in SharedPreferences
        val sharedPrefs = context.getSharedPreferences("zotube_preferences", Context.MODE_PRIVATE)
        assertNull(sharedPrefs.getString(YouTubeRepository.KEY_CUSTOM_API_KEY, null))
    }

    @Test
    fun test_onboarding_tutorial_persistence() {
        assertFalse(repository.isOnboardingCompleted())

        repository.setOnboardingCompleted(true)
        assertTrue(repository.isOnboardingCompleted())

        val sharedPrefs = context.getSharedPreferences("zotube_preferences", Context.MODE_PRIVATE)
        assertTrue(sharedPrefs.getBoolean(YouTubeRepository.KEY_ONBOARDING_COMPLETED, false))
    }

    @Test
    fun test_interceptor_appends_key_to_google_apis_request() {
        var capturedUrl: String? = null
        val customKey = "AIzaSyCustomKeySecret999"

        val interceptor = YouTubeApiKeyInterceptor { customKey }

        val testClient = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .addInterceptor(Interceptor { chain ->
                capturedUrl = chain.request().url.toString()
                // Return a dummy empty response for the test
                okhttp3.Response.Builder()
                    .request(chain.request())
                    .protocol(okhttp3.Protocol.HTTP_1_1)
                    .code(200)
                    .message("OK")
                    .body("".toResponseBody(null))
                    .build()
            })
            .build()

        val request = Request.Builder()
            .url("https://www.googleapis.com/youtube/v3/search?part=snippet&q=test")
            .build()

        testClient.newCall(request).execute()

        assertTrue(capturedUrl!!.contains("key=AIzaSyCustomKeySecret999"))
        assertTrue(capturedUrl!!.contains("q=test"))
    }

    @Test
    fun test_interceptor_does_not_modify_non_google_urls() {
        var capturedUrl: String? = null
        val customKey = "AIzaSyCustomKeySecret999"

        val interceptor = YouTubeApiKeyInterceptor { customKey }

        val testClient = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .addInterceptor(Interceptor { chain ->
                capturedUrl = chain.request().url.toString()
                okhttp3.Response.Builder()
                    .request(chain.request())
                    .protocol(okhttp3.Protocol.HTTP_1_1)
                    .code(200)
                    .message("OK")
                    .body("".toResponseBody(null))
                    .build()
            })
            .build()

        val request = Request.Builder()
            .url("https://inv.tux.pizza/api/v1/search?q=test")
            .build()

        testClient.newCall(request).execute()

        assertFalse(capturedUrl!!.contains("key="))
        assertEquals("https://inv.tux.pizza/api/v1/search?q=test", capturedUrl)
    }

    @Test
    fun test_interceptor_overrides_existing_key_with_custom_key() {
        var capturedUrl: String? = null
        val customKey = "AIzaSyUserPersonalKey"

        val interceptor = YouTubeApiKeyInterceptor { customKey }

        val testClient = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .addInterceptor(Interceptor { chain ->
                capturedUrl = chain.request().url.toString()
                okhttp3.Response.Builder()
                    .request(chain.request())
                    .protocol(okhttp3.Protocol.HTTP_1_1)
                    .code(200)
                    .message("OK")
                    .body("".toResponseBody(null))
                    .build()
            })
            .build()

        val request = Request.Builder()
            .url("https://www.googleapis.com/youtube/v3/search?part=snippet&key=OldKey")
            .build()

        testClient.newCall(request).execute()

        assertTrue(capturedUrl!!.contains("key=AIzaSyUserPersonalKey"))
        assertFalse(capturedUrl!!.contains("OldKey"))
    }
}
