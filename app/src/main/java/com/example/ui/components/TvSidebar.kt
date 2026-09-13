package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PlayCircleOutline
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material.icons.filled.VideoLibrary
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
import com.example.ui.theme.ZoDarkBackground
import com.example.ui.theme.ZoRed
import com.example.ui.theme.ZoShieldGreen
import com.example.ui.theme.ZoSurfaceDark
import com.example.ui.theme.ZoSurfaceElevated
import com.example.ui.theme.ZoTextPrimary
import com.example.ui.theme.ZoTextSecondary
import com.example.viewmodel.TvNavDestination

@Composable
fun TvSidebar(
    selectedDestination: TvNavDestination,
    onDestinationSelected: (TvNavDestination) -> Unit,
    modifier: Modifier = Modifier,
    isExpanded: Boolean = true
) {
    val navItems = listOf(
        NavItem(TvNavDestination.HOME, "Home", Icons.Default.Home, "nav_home"),
        NavItem(TvNavDestination.SEARCH, "Search", Icons.Default.Search, "nav_search"),
        NavItem(TvNavDestination.CATEGORIES, "Categories", Icons.Default.Category, "nav_categories"),
        NavItem(TvNavDestination.DIRECT_PLAY, "Direct Play", Icons.Default.PlayCircleOutline, "nav_direct_play"),
        NavItem(TvNavDestination.LIBRARY, "Library", Icons.Default.VideoLibrary, "nav_library"),
        NavItem(TvNavDestination.SETTINGS, "Settings", Icons.Default.Settings, "nav_settings")
    )

    Column(
        modifier = modifier
            .fillMaxHeight()
            .width(if (isExpanded) 220.dp else 72.dp)
            .background(ZoSurfaceDark)
            .padding(vertical = 20.dp, horizontal = 10.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.fillMaxWidth()
        ) {
            // App Branding (ZoTube TV)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 6.dp)
                    .fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(ZoRed),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Tv,
                        contentDescription = "ZoTube Logo",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }

                if (isExpanded) {
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "ZoTube",
                                color = Color.White,
                                fontWeight = FontWeight.Black,
                                fontSize = 18.sp,
                                letterSpacing = 0.5.sp
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(ZoRed)
                                    .padding(horizontal = 4.dp, vertical = 1.dp)
                            ) {
                                Text(
                                    text = "TV",
                                    color = Color.White,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        Text(
                            text = "Ad-Free Player",
                            color = ZoShieldGreen,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Navigation Items List
            navItems.forEach { item ->
                TvNavItemRow(
                    item = item,
                    isSelected = selectedDestination == item.destination,
                    isExpanded = isExpanded,
                    onClick = { onDestinationSelected(item.destination) }
                )
                Spacer(modifier = Modifier.height(6.dp))
            }
        }

        // Bottom Ad-Free Shield status badge
        if (isExpanded) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(ZoDarkBackground)
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AdShieldBadge(compact = false)
            }
        }
    }
}

private data class NavItem(
    val destination: TvNavDestination,
    val title: String,
    val icon: ImageVector,
    val testTag: String
)

@Composable
private fun TvNavItemRow(
    item: NavItem,
    isSelected: Boolean,
    isExpanded: Boolean,
    onClick: () -> Unit
) {
    var isFocused by remember { mutableStateOf(false) }

    val bgColor by animateColorAsState(
        targetValue = when {
            isFocused -> Color.White.copy(alpha = 0.15f)
            isSelected -> ZoRed.copy(alpha = 0.22f)
            else -> Color.Transparent
        },
        animationSpec = tween(150),
        label = "nav_bg"
    )

    val contentColor by animateColorAsState(
        targetValue = when {
            isFocused -> Color.White
            isSelected -> ZoRed
            else -> ZoTextSecondary
        },
        animationSpec = tween(150),
        label = "nav_content_color"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(bgColor)
            .border(
                width = if (isFocused) 2.dp else 0.dp,
                color = if (isFocused) Color.White else Color.Transparent,
                shape = RoundedCornerShape(10.dp)
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
            .padding(horizontal = 12.dp, vertical = 10.dp)
            .testTag(item.testTag),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = if (isExpanded) Arrangement.Start else Arrangement.Center
    ) {
        Icon(
            imageVector = item.icon,
            contentDescription = item.title,
            tint = contentColor,
            modifier = Modifier.size(22.dp)
        )

        if (isExpanded) {
            Spacer(modifier = Modifier.width(14.dp))
            Text(
                text = item.title,
                color = contentColor,
                fontSize = 14.sp,
                fontWeight = if (isSelected || isFocused) FontWeight.Bold else FontWeight.Normal
            )
        }
    }
}
