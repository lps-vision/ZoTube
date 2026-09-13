package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.model.VideoItem
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun read_app_name_from_context() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("ZoTube", appName)
  }

  @Test
  fun extract_video_id_handles_various_url_formats() {
    // Standard watch URL
    assertEquals("Bey4XXJAqS8", VideoItem.extractVideoId("https://www.youtube.com/watch?v=Bey4XXJAqS8"))
    
    // Short URL
    assertEquals("Bey4XXJAqS8", VideoItem.extractVideoId("https://youtu.be/Bey4XXJAqS8"))
    
    // Shorts URL
    assertEquals("Bey4XXJAqS8", VideoItem.extractVideoId("https://youtube.com/shorts/Bey4XXJAqS8"))
    
    // Raw ID
    assertEquals("Bey4XXJAqS8", VideoItem.extractVideoId("Bey4XXJAqS8"))
    
    // Embed URL
    assertEquals("Bey4XXJAqS8", VideoItem.extractVideoId("https://www.youtube.com/embed/Bey4XXJAqS8"))
  }
}
