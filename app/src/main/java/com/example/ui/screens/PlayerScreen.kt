package com.example.ui.screens

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.data.model.VideoItem
import com.example.ui.components.AdShieldBadge
import com.example.ui.theme.ZoDarkBackground
import com.example.ui.theme.ZoRed
import com.example.ui.theme.ZoShieldGreen
import com.example.ui.theme.ZoSurfaceBorder
import com.example.ui.theme.ZoTextSecondary
import kotlinx.coroutines.delay
import java.io.ByteArrayInputStream

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun PlayerScreen(
    video: VideoItem,
    isBookmarked: Boolean,
    onToggleBookmark: () -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Handle back button on TV remote or device
    BackHandler {
        onClose()
    }

    var showControls by remember { mutableStateOf(true) }
    var isVideoLoading by remember { mutableStateOf(true) }
    var webViewInstance by remember { mutableStateOf<WebView?>(null) }

    // Auto-hide controls overlay after 5 seconds of inactivity
    LaunchedEffect(showControls) {
        if (showControls) {
            delay(5000)
            showControls = false
        }
    }

    // Clean up WebView on exit
    DisposableEffect(Unit) {
        onDispose {
            webViewInstance?.stopLoading()
            webViewInstance?.loadUrl("about:blank")
            webViewInstance?.destroy()
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                showControls = !showControls
            }
            .testTag("player_screen_container")
    ) {
        // Core Ad-Shielded Video Player (WebView)
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                WebView(context).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    setBackgroundColor(android.graphics.Color.BLACK)

                    settings.apply {
                        javaScriptEnabled = true
                        domStorageEnabled = true
                        mediaPlaybackRequiresUserGesture = false
                        loadWithOverviewMode = true
                        useWideViewPort = true
                        builtInZoomControls = false
                        displayZoomControls = false
                        cacheMode = WebSettings.LOAD_DEFAULT
                        // Set modern desktop / TV user agent for highest quality playback
                        userAgentString = "Mozilla/5.0 (SMART-TV; Linux; Tizen 6.0) AppleWebKit/537.36 (KHTML, like Gecko) SamsungBrowser/4.0 Chrome/76.0.3809.146 TV Safari/537.36"
                    }

                    webChromeClient = object : WebChromeClient() {}

                    webViewClient = object : WebViewClient() {
                        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                            super.onPageStarted(view, url, favicon)
                            isVideoLoading = true
                        }

                        override fun onPageFinished(view: WebView?, url: String?) {
                            super.onPageFinished(view, url)
                            isVideoLoading = false
                            // Inject CSS to wipe out any residual ad containers or overlays
                            view?.evaluateJavascript(
                                """
                                (function() {
                                    var css = '.ytp-ad-module, .video-ads, .ytp-ad-overlay-container, .ytp-ad-player-overlay, .ytp-ad-skip-button-container, .ytp-ad-message-container { display: none !important; opacity: 0 !important; pointer-events: none !important; }';
                                    var head = document.head || document.getElementsByTagName('head')[0];
                                    var style = document.createElement('style');
                                    style.type = 'text/css';
                                    style.appendChild(document.createTextNode(css));
                                    head.appendChild(style);
                                })();
                                """.trimIndent(),
                                null
                            )
                        }

                        // Block all ad network requests at network layer
                        override fun shouldInterceptRequest(
                            view: WebView?,
                            request: WebResourceRequest?
                        ): WebResourceResponse? {
                            val url = request?.url?.toString()?.lowercase() ?: return null
                            val adPatterns = listOf(
                                "doubleclick.net",
                                "googleads",
                                "googlesyndication.com",
                                "/pagead/",
                                "/api/stats/ads",
                                "/ptracking",
                                "adservice.google",
                                "pubads.g.doubleclick",
                                "securepubads",
                                "adsystem.com",
                                "adnxs.com"
                            )

                            for (ad in adPatterns) {
                                if (url.contains(ad)) {
                                    // Return empty blocked response
                                    return WebResourceResponse(
                                        "text/plain",
                                        "UTF-8",
                                        ByteArrayInputStream("".toByteArray())
                                    )
                                }
                            }

                            return super.shouldInterceptRequest(view, request)
                        }
                    }

                    // HTML5 responsive embed targeting youtube-nocookie
                    val html = """
                        <!DOCTYPE html>
                        <html>
                        <head>
                            <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
                            <style>
                                * { box-sizing: border-box; }
                                html, body {
                                    margin: 0;
                                    padding: 0;
                                    width: 100%;
                                    height: 100%;
                                    background: #000000;
                                    overflow: hidden;
                                }
                                #player {
                                    position: absolute;
                                    top: 0;
                                    left: 0;
                                    width: 100%;
                                    height: 100%;
                                    border: 0;
                                }
                                .ytp-ad-module, .video-ads, .ytp-ad-overlay-container, .ytp-ad-player-overlay {
                                    display: none !important;
                                }
                            </style>
                        </head>
                        <body>
                            <iframe id="player"
                                src="https://www.youtube-nocookie.com/embed/${video.id}?autoplay=1&controls=1&rel=0&modestbranding=1&iv_load_policy=3&playsinline=1&enablejsapi=1"
                                frameborder="0"
                                allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share"
                                allowfullscreen>
                            </iframe>
                        </body>
                        </html>
                    """.trimIndent()

                    loadDataWithBaseURL("https://www.youtube-nocookie.com", html, "text/html", "UTF-8", null)
                    webViewInstance = this
                }
            }
        )

        // Loading Spinner
        if (isVideoLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(color = ZoRed, modifier = Modifier.size(52.dp))
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = "Loading ad-free video...",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        // TV Player Controls Overlay
        AnimatedVisibility(
            visible = showControls,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.fillMaxSize()
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                // Top Gradient Bar
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .align(Alignment.TopCenter)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Black.copy(alpha = 0.9f),
                                    Color.Transparent
                                )
                            )
                        )
                )

                // Bottom Gradient Bar
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .align(Alignment.BottomCenter)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.9f)
                                )
                            )
                        )
                )

                // Top Header Controls
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 20.dp)
                        .align(Alignment.TopStart),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        PlayerTvButton(
                            icon = Icons.AutoMirrored.Filled.ArrowBack,
                            description = "Back to list",
                            onClick = onClose,
                            testTag = "player_back_button"
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        Column {
                            Text(
                                text = video.title,
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = video.channelTitle,
                                    color = ZoTextSecondary,
                                    fontSize = 13.sp
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    imageVector = Icons.Default.Verified,
                                    contentDescription = "Verified",
                                    tint = ZoTextSecondary,
                                    modifier = Modifier.size(12.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        AdShieldBadge(compact = false)

                        Spacer(modifier = Modifier.width(12.dp))

                        // Bookmark Toggle
                        PlayerTvButton(
                            icon = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            description = "Bookmark Video",
                            tint = if (isBookmarked) ZoShieldGreen else Color.White,
                            onClick = onToggleBookmark,
                            testTag = "player_bookmark_button"
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        // Close Button
                        PlayerTvButton(
                            icon = Icons.Default.Close,
                            description = "Close Player",
                            onClick = onClose,
                            testTag = "player_close_button"
                        )
                    }
                }

                // Bottom Status Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 18.dp)
                        .align(Alignment.BottomCenter),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(ZoRed)
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = "ZoTube TV",
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Text(
                            text = "Ad-Free Streaming • Zero Commercial Interruptions",
                            color = ZoTextSecondary,
                            fontSize = 12.sp
                        )
                    }

                    Text(
                        text = "Press OK / Tap screen to toggle controls",
                        color = ZoTextSecondary.copy(alpha = 0.7f),
                        fontSize = 11.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun PlayerTvButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    description: String,
    onClick: () -> Unit,
    tint: Color = Color.White,
    testTag: String
) {
    var isFocused by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .size(44.dp)
            .clip(CircleShape)
            .background(if (isFocused) Color.White.copy(alpha = 0.3f) else Color.Black.copy(alpha = 0.6f))
            .border(
                width = if (isFocused) 2.dp else 1.dp,
                color = if (isFocused) Color.White else ZoSurfaceBorder,
                shape = CircleShape
            )
            .onFocusChanged { isFocused = it.isFocused }
            .focusable()
            .onKeyEvent { keyEvent ->
                if (keyEvent.key == Key.DirectionCenter || keyEvent.key == Key.Enter) {
                    onClick()
                    true
                } else {
                    false
                }
            }
            .clickable(onClick = onClick)
            .testTag(testTag),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = description,
            tint = tint,
            modifier = Modifier.size(22.dp)
        )
    }
}
