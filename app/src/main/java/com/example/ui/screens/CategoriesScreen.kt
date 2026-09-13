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
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Gamepad
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.VideoItem
import com.example.data.repository.CuratedCatalog
import com.example.ui.components.AdShieldBadge
import com.example.ui.components.TvVideoCard
import com.example.ui.theme.ZoDarkBackground
import com.example.ui.theme.ZoRed
import com.example.ui.theme.ZoSurfaceBorder
import com.example.ui.theme.ZoSurfaceDark
import com.example.ui.theme.ZoSurfaceElevated
import com.example.ui.theme.ZoTextSecondary

@Composable
fun CategoriesScreen(
    currentCategory: String,
    onCategorySelected: (String) -> Unit,
    categoryVideos: List<VideoItem>,
    onVideoClicked: (VideoItem) -> Unit,
    modifier: Modifier = Modifier
) {
    val categoryMeta = listOf(
        CategoryMeta("Trending", "Global Viral & Top Trending", Icons.AutoMirrored.Filled.TrendingUp, Color(0xFFFF1744)),
        CategoryMeta("Mizo / Hla", "Mizo Lengzem, Gospel & Zoram", Icons.Default.Language, Color(0xFF00E676)),
        CategoryMeta("Music", "Pop, Rock, Hip Hop & Hits", Icons.Default.MusicNote, Color(0xFF2979FF)),
        CategoryMeta("Gaming", "Trailers, Walkthroughs & Esports", Icons.Default.Gamepad, Color(0xFFFF9100)),
        CategoryMeta("News", "Live Global Feeds & Headlines", Icons.Default.Newspaper, Color(0xFFE040FB)),
        CategoryMeta("Tech & Science", "4K OLED Demos & Space Exploration", Icons.Default.Science, Color(0xFF00E5FF)),
        CategoryMeta("Movies & Animation", "Cinema Shorts & Open Movies", Icons.Default.Movie, Color(0xFFFF5252)),
        CategoryMeta("Relaxing / Lofi", "24/7 Lofi Girl & Synthwave Streams", Icons.Default.Spa, Color(0xFF76FF03))
    )

    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 240.dp),
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
            .fillMaxSize()
            .background(ZoDarkBackground)
            .testTag("categories_screen")
    ) {
        // Header
        item(span = { GridItemSpan(maxLineSpan) }) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Text(
                        text = "Explore Categories",
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Browse curated video channels optimized for Android TV",
                        color = ZoTextSecondary,
                        fontSize = 13.sp
                    )
                }
                AdShieldBadge(compact = true)
            }
        }

        // Category Cards Grid
        item(span = { GridItemSpan(maxLineSpan) }) {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 180.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(210.dp)
            ) {
                items(categoryMeta) { meta ->
                    CategoryTile(
                        meta = meta,
                        isSelected = meta.title == currentCategory,
                        onClick = { onCategorySelected(meta.title) }
                    )
                }
            }
        }

        // Current Category Video Header
        item(span = { GridItemSpan(maxLineSpan) }) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .width(4.dp)
                        .height(18.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(ZoRed)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "$currentCategory Videos (${categoryVideos.size})",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Videos in this category
        items(categoryVideos, key = { "${it.id}_cat" }) { video ->
            TvVideoCard(
                video = video,
                onClick = { onVideoClicked(video) },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

private data class CategoryMeta(
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val accentColor: Color
)

@Composable
private fun CategoryTile(
    meta: CategoryMeta,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    var isFocused by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(84.dp)
            .border(
                width = if (isFocused) 2.dp else 1.dp,
                color = if (isFocused) Color.White else if (isSelected) ZoRed else ZoSurfaceBorder,
                shape = RoundedCornerShape(12.dp)
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
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) ZoRed.copy(alpha = 0.25f) else if (isFocused) ZoSurfaceElevated else ZoSurfaceDark
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(meta.accentColor.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = meta.icon,
                    contentDescription = meta.title,
                    tint = meta.accentColor,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = meta.title,
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = meta.subtitle,
                    color = ZoTextSecondary,
                    fontSize = 11.sp,
                    maxLines = 1
                )
            }
        }
    }
}
