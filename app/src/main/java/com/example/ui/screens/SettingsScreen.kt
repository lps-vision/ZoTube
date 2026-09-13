package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.AdShieldBadge
import com.example.ui.theme.ZoDarkBackground
import com.example.ui.theme.ZoRed
import com.example.ui.theme.ZoShieldGreen
import com.example.ui.theme.ZoSurfaceBorder
import com.example.ui.theme.ZoSurfaceDark
import com.example.ui.theme.ZoSurfaceElevated
import com.example.ui.theme.ZoTextPrimary
import com.example.ui.theme.ZoTextSecondary
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(
    currentApiKey: String = "",
    customApiKey: String = currentApiKey,
    effectiveApiKey: String = currentApiKey,
    hasCustomKey: Boolean = customApiKey.isNotBlank(),
    adsBlockedCount: Int = 0,
    onSaveApiKey: (String) -> Unit = {},
    onClearApiKey: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var keyInput by remember(customApiKey) { mutableStateOf(customApiKey) }
    var feedbackMessage by remember { mutableStateOf<Pair<String, Boolean>?>(null) }

    fun showFeedback(message: String, isSuccess: Boolean) {
        feedbackMessage = Pair(message, isSuccess)
        coroutineScope.launch {
            snackbarHostState.currentSnackbarData?.dismiss()
            snackbarHostState.showSnackbar(message)
        }
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(ZoDarkBackground)
            .testTag("settings_screen")
    ) {
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 28.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            modifier = Modifier.fillMaxSize()
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
                            text = "ZoTube TV Settings",
                            color = Color.White,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Configure Bring Your Own Key (BYOK), Ad-Shield, and Leanback options",
                            color = ZoTextSecondary,
                            fontSize = 13.sp
                        )
                    }

                    AdShieldBadge(compact = false)
                }
            }

            // Bring Your Own Key (BYOK) Section Card
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = ZoSurfaceDark),
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, ZoSurfaceBorder),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("byok_settings_card")
                ) {
                    Column(modifier = Modifier.padding(22.dp)) {
                        // Section Header
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(ZoRed.copy(alpha = 0.2f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Key,
                                        contentDescription = "BYOK API Key",
                                        tint = ZoRed,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(14.dp))
                                Column {
                                    Text(
                                        text = "Bring Your Own Key (BYOK)",
                                        color = Color.White,
                                        fontSize = 17.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "Personal YouTube Data API v3 Key",
                                        color = ZoTextSecondary,
                                        fontSize = 12.sp
                                    )
                                }
                            }

                            // Dynamic Status Chip
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(
                                        when {
                                            hasCustomKey -> ZoShieldGreen.copy(alpha = 0.2f)
                                            effectiveApiKey.isNotBlank() -> Color(0xFFFFB300).copy(alpha = 0.18f)
                                            else -> ZoSurfaceElevated
                                        }
                                    )
                                    .border(
                                        1.dp,
                                        when {
                                            hasCustomKey -> ZoShieldGreen.copy(alpha = 0.6f)
                                            effectiveApiKey.isNotBlank() -> Color(0xFFFFB300).copy(alpha = 0.5f)
                                            else -> ZoSurfaceBorder
                                        },
                                        RoundedCornerShape(8.dp)
                                    )
                                    .padding(horizontal = 10.dp, vertical = 5.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = if (hasCustomKey) Icons.Default.Verified else Icons.Default.Info,
                                        contentDescription = null,
                                        tint = when {
                                            hasCustomKey -> ZoShieldGreen
                                            effectiveApiKey.isNotBlank() -> Color(0xFFFFB300)
                                            else -> ZoTextSecondary
                                        },
                                        modifier = Modifier.size(13.dp)
                                    )
                                    Spacer(modifier = Modifier.width(5.dp))
                                    Text(
                                        text = when {
                                            hasCustomKey -> "CUSTOM KEY ACTIVE • 10K QUOTA"
                                            effectiveApiKey.isNotBlank() -> "DEFAULT KEY ACTIVE"
                                            else -> "STANDALONE / INVIDIOUS"
                                        },
                                        color = when {
                                            hasCustomKey -> ZoShieldGreen
                                            effectiveApiKey.isNotBlank() -> Color(0xFFFFB300)
                                            else -> ZoTextSecondary
                                        },
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Explanatory Details
                        Text(
                            text = "To prevent Google Cloud quota limits (10,000 units/day) from throttling your searches, you can enter your own free YouTube Data API key. Your personal key is stored securely in persistent local storage on this TV and is automatically attached to all API requests.",
                            color = ZoTextSecondary,
                            fontSize = 12.sp,
                            lineHeight = 18.sp
                        )

                        // Active Custom Key Masked Preview
                        if (hasCustomKey && customApiKey.isNotBlank()) {
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(ZoSurfaceElevated)
                                    .border(1.dp, ZoSurfaceBorder, RoundedCornerShape(8.dp))
                                    .padding(horizontal = 12.dp, vertical = 8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = ZoShieldGreen,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Active Key: ",
                                    color = ZoTextSecondary,
                                    fontSize = 12.sp
                                )
                                Text(
                                    text = if (customApiKey.length > 8) {
                                        "${customApiKey.take(4)}••••••••••••${customApiKey.takeLast(4)}"
                                    } else {
                                        "••••••••"
                                    },
                                    color = Color.White,
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                                Spacer(modifier = Modifier.weight(1f))
                                Text(
                                    text = "Saved in Persistent Storage",
                                    color = ZoShieldGreen,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // TextField for Key Input
                        OutlinedTextField(
                            value = keyInput,
                            onValueChange = {
                                keyInput = it
                                feedbackMessage = null
                            },
                            label = {
                                Text(
                                    text = "YouTube Data API v3 Key",
                                    fontSize = 12.sp
                                )
                            },
                            placeholder = {
                                Text(
                                    text = "Paste AIzaSy... key here",
                                    color = ZoTextSecondary.copy(alpha = 0.5f),
                                    fontSize = 13.sp
                                )
                            },
                            trailingIcon = {
                                if (keyInput.isNotEmpty()) {
                                    IconButton(
                                        onClick = {
                                            keyInput = ""
                                            feedbackMessage = null
                                        }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Clear,
                                            contentDescription = "Clear text",
                                            tint = ZoTextSecondary,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = ZoSurfaceElevated,
                                unfocusedContainerColor = ZoSurfaceDark,
                                focusedBorderColor = Color.White,
                                unfocusedBorderColor = ZoSurfaceBorder,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedLabelColor = Color.White,
                                unfocusedLabelColor = ZoTextSecondary
                            ),
                            shape = RoundedCornerShape(10.dp),
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("byok_api_key_input")
                        )

                        // In-card confirmation / error feedback banner
                        if (feedbackMessage != null) {
                            val (msg, isSuccess) = feedbackMessage!!
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(
                                        if (isSuccess) ZoShieldGreen.copy(alpha = 0.15f)
                                        else Color(0xFFFF5252).copy(alpha = 0.15f)
                                    )
                                    .border(
                                        1.dp,
                                        if (isSuccess) ZoShieldGreen.copy(alpha = 0.4f)
                                        else Color(0xFFFF5252).copy(alpha = 0.4f),
                                        RoundedCornerShape(8.dp)
                                    )
                                    .padding(horizontal = 12.dp, vertical = 8.dp)
                            ) {
                                Icon(
                                    imageVector = if (isSuccess) Icons.Default.CheckCircle else Icons.Default.Info,
                                    contentDescription = null,
                                    tint = if (isSuccess) ZoShieldGreen else Color(0xFFFF5252),
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = msg,
                                    color = if (isSuccess) ZoShieldGreen else Color(0xFFFF5252),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Action Buttons: Save & Clear/Remove
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            // Save Button
                            Button(
                                onClick = {
                                    val trimmed = keyInput.trim()
                                    if (trimmed.isEmpty()) {
                                        showFeedback("Please enter a valid YouTube API key first.", false)
                                    } else {
                                        onSaveApiKey(trimmed)
                                        showFeedback("API key saved persistently! Personal quota active.", true)
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = ZoRed),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp)
                                    .testTag("save_api_key_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Save,
                                    contentDescription = "Save",
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Save Key",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                            }

                            // Clear / Remove Button
                            OutlinedButton(
                                onClick = {
                                    keyInput = ""
                                    onClearApiKey()
                                    showFeedback("Custom API key removed. Falling back to default engine.", true)
                                },
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = if (hasCustomKey || keyInput.isNotEmpty()) Color(0xFFFF5252) else ZoTextSecondary
                                ),
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (hasCustomKey || keyInput.isNotEmpty()) Color(0xFFFF5252).copy(alpha = 0.5f) else ZoSurfaceBorder
                                ),
                                shape = RoundedCornerShape(8.dp),
                                enabled = hasCustomKey || keyInput.isNotEmpty(),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp)
                                    .testTag("clear_api_key_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.DeleteOutline,
                                    contentDescription = "Clear",
                                    tint = if (hasCustomKey || keyInput.isNotEmpty()) Color(0xFFFF5252) else ZoTextSecondary,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Clear / Remove",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        // Step-by-step Guide Card
                        Card(
                            colors = CardDefaults.cardColors(containerColor = ZoSurfaceElevated),
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, ZoSurfaceBorder),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.HelpOutline,
                                        contentDescription = "Help",
                                        tint = Color(0xFFFFB300),
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "How to get a Free YouTube Data API v3 Key:",
                                        color = Color.White,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "1. Visit console.cloud.google.com on your computer or phone.\n" +
                                            "2. Create a free project and search for 'YouTube Data API v3', then click Enable.\n" +
                                            "3. Go to 'APIs & Services' > 'Credentials' > 'Create Credentials' > 'API Key'.\n" +
                                            "4. Copy your key, paste it in the box above, and press 'Save Key'.",
                                    color = ZoTextSecondary,
                                    fontSize = 11.sp,
                                    lineHeight = 16.sp
                                )
                            }
                        }
                    }
                }
            }

            // Ad-Shield Statistics Card
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = ZoSurfaceDark),
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, ZoSurfaceBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(ZoShieldGreen.copy(alpha = 0.2f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Shield,
                                        contentDescription = "Shield",
                                        tint = ZoShieldGreen,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = "ZoTube Ad-Shield Engine",
                                        color = Color.White,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "Network-level Ad Interception & CSS Blocker",
                                        color = ZoTextSecondary,
                                        fontSize = 12.sp
                                    )
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(ZoShieldGreen.copy(alpha = 0.15f))
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = "100% AD-FREE",
                                    color = ZoShieldGreen,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Black
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            MetricBox(
                                title = "Video Ads Blocked",
                                value = "$adsBlockedCount",
                                modifier = Modifier.weight(1f)
                            )
                            MetricBox(
                                title = "Popups & Overlays",
                                value = "0 (Zero)",
                                modifier = Modifier.weight(1f)
                            )
                            MetricBox(
                                title = "Shield Status",
                                value = "Protected",
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }

            // TV Leanback Info
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = ZoSurfaceElevated),
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, ZoSurfaceBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(ZoRed),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Tv,
                                contentDescription = "TV",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column {
                            Text(
                                text = "ZoTube Android TV Edition v1.0",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Designed for 10-foot TV experience with D-pad remote support, full 4K playback, and ad-free YouTube streaming.",
                                color = ZoTextSecondary,
                                fontSize = 12.sp,
                                lineHeight = 16.sp
                            )
                        }
                    }
                }
            }
        }

        // Snackbar Host for on-screen notification feedback
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        )
    }
}

@Composable
private fun MetricBox(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(ZoSurfaceElevated)
            .border(1.dp, ZoSurfaceBorder, RoundedCornerShape(10.dp))
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            color = ZoShieldGreen,
            fontSize = 18.sp,
            fontWeight = FontWeight.Black
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = title,
            color = ZoTextSecondary,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium
        )
    }
}
