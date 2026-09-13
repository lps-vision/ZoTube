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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.VideoItem
import com.example.ui.components.AdShieldBadge
import com.example.ui.theme.ZoDarkBackground
import com.example.ui.theme.ZoRed
import com.example.ui.theme.ZoShieldGreen
import com.example.ui.theme.ZoSurfaceBorder
import com.example.ui.theme.ZoSurfaceDark
import com.example.ui.theme.ZoSurfaceElevated
import com.example.ui.theme.ZoTextPrimary
import com.example.ui.theme.ZoTextSecondary

@Composable
fun DirectPlayScreen(
    onPlayDirect: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var inputUrlOrId by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val clipboardManager = LocalClipboardManager.current

    val detectedId = remember(inputUrlOrId) {
        VideoItem.extractVideoId(inputUrlOrId)
    }

    val sampleLinks = listOf(
        SampleVideo("Bey4XXJAqS8", "Costa Rica 4K HDR Nature", "4K Ultra HD"),
        SampleVideo("jfKfPfyJRdk", "Lofi Hip Hop Radio 24/7", "Chill Beats"),
        SampleVideo("09R8_2nJtjg", "Maroon 5 - Sugar", "Music Video"),
        SampleVideo("YE7VzlLtp-4", "Big Buck Bunny 1080p", "Animation")
    )

    LazyColumn(
        contentPadding = PaddingValues(horizontal = 28.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        modifier = modifier
            .fillMaxSize()
            .background(ZoDarkBackground)
            .testTag("direct_play_screen")
    ) {
        // Header
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Text(
                        text = "Direct Play: Any YouTube Video",
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Youtube video zawng zawng Ads awm lova en theihna hmun",
                        color = ZoShieldGreen,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                AdShieldBadge(compact = false)
            }
        }

        // Input Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = ZoSurfaceDark),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, ZoSurfaceBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Enter Video URL or 11-Digit Video ID",
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Paste any link (youtube.com/watch?v=..., youtu.be/...) or type the Video ID.",
                        color = ZoTextSecondary,
                        fontSize = 12.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedTextField(
                        value = inputUrlOrId,
                        onValueChange = {
                            inputUrlOrId = it
                            errorMessage = null
                        },
                        placeholder = {
                            Text(
                                text = "e.g. https://www.youtube.com/watch?v=Bey4XXJAqS8 or Bey4XXJAqS8",
                                color = ZoTextSecondary.copy(alpha = 0.6f),
                                fontSize = 13.sp
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Link,
                                contentDescription = "Link",
                                tint = ZoRed
                            )
                        },
                        trailingIcon = {
                            Button(
                                onClick = {
                                    val clip = clipboardManager.getText()?.text
                                    if (!clip.isNullOrBlank()) {
                                        inputUrlOrId = clip.trim()
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = ZoSurfaceElevated),
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                                modifier = Modifier.padding(end = 6.dp)
                            ) {
                                Icon(
                                    Icons.Default.ContentPaste,
                                    contentDescription = "Paste",
                                    tint = Color.White,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Paste", fontSize = 11.sp, color = Color.White)
                            }
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = ZoSurfaceElevated,
                            unfocusedContainerColor = ZoSurfaceDark,
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = ZoSurfaceBorder,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("direct_url_input")
                    )

                    // Detected ID confirmation chip
                    if (detectedId != null) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(ZoShieldGreen.copy(alpha = 0.15f))
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Valid",
                                tint = ZoShieldGreen,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Valid YouTube Video ID: $detectedId (Ready for Ad-Free Play)",
                                color = ZoShieldGreen,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    if (errorMessage != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = errorMessage ?: "",
                            color = ZoRed,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            if (detectedId != null) {
                                onPlayDirect(detectedId)
                            } else {
                                errorMessage = "Please enter a valid YouTube URL or 11-digit Video ID"
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = ZoRed),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("direct_play_submit_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Play",
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Play Video Ad-Free",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }
                }
            }
        }

        // Quick Click Samples
        item {
            Column {
                Text(
                    text = "Quick Sample Videos (Click to test):",
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(10.dp))

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    sampleLinks.forEach { sample ->
                        SampleRow(
                            sample = sample,
                            onClick = {
                                inputUrlOrId = sample.id
                                onPlayDirect(sample.id)
                            }
                        )
                    }
                }
            }
        }

        // Info Banner in Mizo
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = ZoSurfaceElevated),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, ZoSurfaceBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = Icons.Default.Shield,
                        contentDescription = "Info",
                        tint = ZoShieldGreen,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Engtin nge Ads awm lo vin a en theih?",
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "ZoTube hian YouTube video stream direct-in a la chhuak a, video hma leh laia advertisement awm zawng zawng a block vek a ni. Khawi video pawh a ID emaw link i hriat chuan heta tang hian zalen takin i en thei.",
                            color = ZoTextSecondary,
                            fontSize = 12.sp,
                            lineHeight = 17.sp
                        )
                    }
                }
            }
        }
    }
}

private data class SampleVideo(val id: String, val title: String, val tag: String)

@Composable
private fun SampleRow(
    sample: SampleVideo,
    onClick: () -> Unit
) {
    var isFocused by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(if (isFocused) Color.White.copy(alpha = 0.15f) else ZoSurfaceDark)
            .border(
                width = if (isFocused) 2.dp else 1.dp,
                color = if (isFocused) Color.White else ZoSurfaceBorder,
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
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Tv,
                contentDescription = null,
                tint = ZoRed,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = sample.title,
                    color = if (isFocused) Color.White else ZoTextPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "ID: ${sample.id} • ${sample.tag}",
                    color = ZoTextSecondary,
                    fontSize = 11.sp
                )
            }
        }

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(ZoRed.copy(alpha = 0.2f))
                .padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
            Text(
                text = "Play",
                color = ZoRed,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp
            )
        }
    }
}
