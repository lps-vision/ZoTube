package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.BookmarkEntity
import com.example.data.local.WatchHistoryEntity
import com.example.data.model.VideoItem
import com.example.ui.components.TvVideoCard
import com.example.ui.theme.ZoDarkBackground
import com.example.ui.theme.ZoRed
import com.example.ui.theme.ZoSurfaceBorder
import com.example.ui.theme.ZoSurfaceDark
import com.example.ui.theme.ZoSurfaceElevated
import com.example.ui.theme.ZoTextPrimary
import com.example.ui.theme.ZoTextSecondary

@Composable
fun LibraryScreen(
    watchHistory: List<WatchHistoryEntity>,
    bookmarks: List<BookmarkEntity>,
    onVideoClicked: (VideoItem) -> Unit,
    onClearHistory: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf(0) } // 0: History, 1: Bookmarks

    val displayVideos: List<VideoItem> = if (selectedTab == 0) {
        watchHistory.map {
            VideoItem(
                id = it.videoId,
                title = it.title,
                channelTitle = it.channelTitle,
                thumbnailUrl = it.thumbnailUrl,
                duration = it.duration,
                viewCount = it.viewCount,
                category = "History"
            )
        }
    } else {
        bookmarks.map {
            VideoItem(
                id = it.videoId,
                title = it.title,
                channelTitle = it.channelTitle,
                thumbnailUrl = it.thumbnailUrl,
                duration = it.duration,
                category = "Saved"
            )
        }
    }

    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 260.dp),
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
            .fillMaxSize()
            .background(ZoDarkBackground)
            .testTag("library_screen")
    ) {
        // Header Tabs & Clear Button
        item(span = { GridItemSpan(maxLineSpan) }) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Your Library",
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )

                    if (selectedTab == 0 && watchHistory.isNotEmpty()) {
                        Button(
                            onClick = onClearHistory,
                            colors = ButtonDefaults.buttonColors(containerColor = ZoSurfaceElevated),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.DeleteSweep,
                                contentDescription = "Clear History",
                                tint = ZoRed,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Clear History", color = Color.White, fontSize = 12.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Tab Switcher
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    LibraryTabButton(
                        title = "Watch History (${watchHistory.size})",
                        icon = Icons.Default.History,
                        isSelected = selectedTab == 0,
                        onClick = { selectedTab = 0 }
                    )

                    LibraryTabButton(
                        title = "Saved Bookmarks (${bookmarks.size})",
                        icon = Icons.Default.Bookmark,
                        isSelected = selectedTab == 1,
                        onClick = { selectedTab = 1 }
                    )
                }
            }
        }

        // Empty State or List
        if (displayVideos.isEmpty()) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(ZoSurfaceDark)
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = if (selectedTab == 0) Icons.Default.History else Icons.Default.Bookmark,
                            contentDescription = null,
                            tint = ZoTextSecondary,
                            modifier = Modifier.size(36.dp)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = if (selectedTab == 0) "No watch history yet" else "No saved bookmarks",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (selectedTab == 0)
                                "Videos you watch in ZoTube TV will appear here for fast replay."
                            else
                                "Save your favorite videos using the bookmark icon in the player or hero banner.",
                            color = ZoTextSecondary,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        } else {
            items(displayVideos, key = { "${it.id}_${selectedTab}" }) { video ->
                TvVideoCard(
                    video = video,
                    onClick = { onVideoClicked(video) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun LibraryTabButton(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    var isFocused by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(
                when {
                    isSelected -> ZoRed
                    isFocused -> Color.White.copy(alpha = 0.2f)
                    else -> ZoSurfaceDark
                }
            )
            .border(
                width = if (isFocused) 2.dp else 1.dp,
                color = if (isFocused) Color.White else if (isSelected) ZoRed else ZoSurfaceBorder,
                shape = RoundedCornerShape(20.dp)
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
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = if (isSelected || isFocused) Color.White else ZoTextSecondary,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = title,
            color = if (isSelected || isFocused) Color.White else ZoTextSecondary,
            fontSize = 13.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
        )
    }
}
