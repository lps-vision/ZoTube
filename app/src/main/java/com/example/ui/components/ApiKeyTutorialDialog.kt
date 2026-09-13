package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Api
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.theme.ZoDarkBackground
import com.example.ui.theme.ZoRed
import com.example.ui.theme.ZoShieldGreen
import com.example.ui.theme.ZoSurfaceBorder
import com.example.ui.theme.ZoSurfaceDark
import com.example.ui.theme.ZoSurfaceElevated
import com.example.ui.theme.ZoTextPrimary
import com.example.ui.theme.ZoTextSecondary

data class TutorialStep(
    val stepNumber: Int,
    val title: String,
    val icon: ImageVector,
    val mainText: String,
    val proTip: String,
    val accentColor: Color
)

@Composable
fun ApiKeyTutorialDialog(
    initialApiKey: String = "",
    onDismiss: (dontShowAgain: Boolean) -> Unit,
    onSaveKey: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var keyInput by remember { mutableStateOf(initialApiKey) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val steps = remember {
        listOf(
            TutorialStep(
                stepNumber = 1,
                title = "Bul Ṭanna: Phone-ah Google Cloud hawng la, Project thlang rawh",
                icon = Icons.Default.PhoneAndroid,
                mainText = "I smartphone (Android emaw iPhone) browser (Chrome/Safari)-ah console.cloud.google.com ah lut la, i Gmail account pangngaiin log in rawh. A chung veilam-ah khan 'Select a project' emaw 'New Project' hmet la, project thar siam rawh (e.g. ZoTube TV).",
                proTip = "Phone atanga siam hi TV remote hmang aia a awlsam zawk em em vang a ni e.",
                accentColor = Color(0xFF4285F4)
            ),
            TutorialStep(
                stepNumber = 2,
                title = "API Enable: Library-ah lut la, 'YouTube Data API v3' Enable rawh",
                icon = Icons.Default.Api,
                mainText = "Veilam Menu (☰) hmet la, 'APIs & Services' > 'Library' ah lut rawh. Zawnnaah khan 'YouTube Data API v3' tiin zawng la, a phek a lo lan hunah button pawl 'Enable' tih kha hmet rawh.",
                proTip = "Hei hian YouTube video search leh channel zawng zawng hawng theihna a pe ang che.",
                accentColor = ZoRed
            ),
            TutorialStep(
                stepNumber = 3,
                title = "Key Siam: Credentials-ah lut la, API Key thar siam rawh",
                icon = Icons.Default.Key,
                mainText = "Veilam menu-ah bawk 'APIs & Services' > 'Credentials' tihah lut leh rawh. A chung lamah '+ Create credentials' hmet la, a lo lang atang khan 'API key' thlang rawh. Google-in i API Key thar chu a siam nghal zung zung ang.",
                proTip = "A tlangpuiin 'AIzaSy...' atanga inṭan key a lo chhuak ang.",
                accentColor = Color(0xFFFFB300)
            ),
            TutorialStep(
                stepNumber = 4,
                title = "Himna (Restrictions) Dah: Security fel fai takin siam rawh",
                icon = Icons.Default.Security,
                mainText = "API key a lo siam zawhah 'Edit API key' ah lut rawh.\n" +
                        "• API restrictions: 'Restrict key' thlangin 'YouTube Data API v3' chauh kha tick rawh.\n" +
                        "• Application restrictions: Android TV tan chuan 'None (Don't restrict)' thlang rawh! (Android restriction i thlan chuan package name leh SHA-1 fingerprint buaithlak a ngai a, 'None' hian TV-ah awlsam takin a kal theih phah a ni).",
                proTip = "A hnuai berah 'Save' hmet theihnghilh suh ang che!",
                accentColor = ZoShieldGreen
            ),
            TutorialStep(
                stepNumber = 5,
                title = "Hmang Ṭan Rawh: Copy la, he TV Apps-ah hian dah lût rawh",
                icon = Icons.Default.Tv,
                mainText = "I API Key (AIzaSy...) siam thar chu copy la, he popup hnuai emaw Settings > BYOK-ah hian chhu lut (type) la, 'Save & Hmang Rawh' tih hmet rawh.",
                proTip = "Ni tin 10,000 free quota units i nei dawn a, ZoTube TV-ah ads awm lova 4K video zalen takin i en thei tawh ang!",
                accentColor = Color(0xFFAB47BC)
            )
        )
    }

    Dialog(
        onDismissRequest = { onDismiss(true) },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = modifier
                .fillMaxWidth(0.92f)
                .fillMaxHeight(0.92f)
                .testTag("api_key_tutorial_dialog"),
            shape = RoundedCornerShape(20.dp),
            color = ZoDarkBackground,
            border = androidx.compose.foundation.BorderStroke(1.5.dp, ZoSurfaceBorder)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Dialog Top Header Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(ZoSurfaceDark)
                        .padding(horizontal = 24.dp, vertical = 18.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(
                                    Brush.linearGradient(
                                        colors = listOf(ZoRed, Color(0xFFFF5252))
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Key,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "YouTube Data API v3 Key Siam Dan",
                                    color = Color.White,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(ZoShieldGreen.copy(alpha = 0.2f))
                                        .padding(horizontal = 8.dp, vertical = 3.dp)
                                ) {
                                    Text(
                                        text = "PHONE GUIDE",
                                        color = ZoShieldGreen,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Black
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(3.dp))
                            Text(
                                text = "Phone hmangin awlsam takin siam la, he TV Apps-ah hian dah lût rawh",
                                color = ZoTextSecondary,
                                fontSize = 12.sp
                            )
                        }
                    }

                    IconButton(
                        onClick = { onDismiss(true) },
                        modifier = Modifier
                            .size(36.dp)
                            .testTag("close_tutorial_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close tutorial",
                            tint = ZoTextSecondary
                        )
                    }
                }

                // Scrollable Content
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentPadding = PaddingValues(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Why Phone Hero Banner
                    item {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = ZoSurfaceElevated),
                            shape = RoundedCornerShape(14.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF4285F4).copy(alpha = 0.4f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(18.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(48.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFF4285F4).copy(alpha = 0.2f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.PhoneAndroid,
                                        contentDescription = "Phone",
                                        tint = Color(0xFF4285F4),
                                        modifier = Modifier.size(28.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.width(16.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Engvangin nge Phone hman a ṭhat zawk?",
                                        color = Color.White,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "TV Remote hmanga Google Cloud khawih leh chhut chu a buaithlak a. I smartphone (Chrome/Safari) atangin a hnuaia URL ah hian lut la, API Key chu awlsam takin i siam thei a, i siam zawhah he TV-ah hian i chhu lut (type) dawn nia:",
                                        color = ZoTextSecondary,
                                        fontSize = 12.sp,
                                        lineHeight = 17.sp
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(ZoSurfaceDark)
                                            .border(1.dp, ZoSurfaceBorder, RoundedCornerShape(8.dp))
                                            .padding(horizontal = 12.dp, vertical = 6.dp)
                                    ) {
                                        Text(
                                            text = "console.cloud.google.com",
                                            color = Color(0xFFFFD54F),
                                            fontFamily = FontFamily.Monospace,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Steps 1 to 5
                    itemsIndexed(steps) { _, step ->
                        StepItemCard(step = step)
                    }
                }

                // Bottom Action & Input Bar
                Surface(
                    color = ZoSurfaceDark,
                    tonalElevation = 6.dp,
                    border = androidx.compose.foundation.BorderStroke(1.dp, ZoSurfaceBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 18.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(14.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            OutlinedTextField(
                                value = keyInput,
                                onValueChange = {
                                    keyInput = it
                                    errorMessage = null
                                },
                                label = { Text("API Key Dah Lût Rawh (I siam zawhah)") },
                                placeholder = {
                                    Text(
                                        text = "AIzaSy...",
                                        color = ZoTextSecondary.copy(alpha = 0.5f)
                                    )
                                },
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = ZoSurfaceElevated,
                                    unfocusedContainerColor = ZoDarkBackground,
                                    focusedBorderColor = ZoShieldGreen,
                                    unfocusedBorderColor = ZoSurfaceBorder,
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White
                                ),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("tutorial_api_key_input")
                            )

                            // Save & Use Button
                            Button(
                                onClick = {
                                    val trimmed = keyInput.trim()
                                    if (trimmed.isEmpty()) {
                                        errorMessage = "API Key i la chhu lut lo. I neih loh chuan 'A hnuah ka ti ang' tih hmet rawh."
                                    } else {
                                        onSaveKey(trimmed)
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = ZoRed),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .height(52.dp)
                                    .testTag("tutorial_save_key_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Save & Hmang Rawh",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                            }

                            // Skip Button
                            OutlinedButton(
                                onClick = { onDismiss(true) },
                                shape = RoundedCornerShape(10.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, ZoSurfaceBorder),
                                modifier = Modifier
                                    .height(52.dp)
                                    .testTag("tutorial_skip_button")
                            ) {
                                Text(
                                    text = "A hnuah ka ti ang (Skip)",
                                    color = ZoTextSecondary,
                                    fontSize = 13.sp
                                )
                            }
                        }

                        if (errorMessage != null) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = errorMessage!!,
                                color = Color(0xFFFF5252),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                        } else {
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Hriattirna: Settings atangin he tutorial leh API Key hi engtik lai pawhin i en leh thei e.",
                                color = ZoTextSecondary,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StepItemCard(
    step: TutorialStep,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = ZoSurfaceDark),
        shape = RoundedCornerShape(14.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, ZoSurfaceBorder),
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(18.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Number / Icon Badge
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(step.accentColor.copy(alpha = 0.2f))
                    .border(1.5.dp, step.accentColor.copy(alpha = 0.6f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${step.stepNumber}",
                    color = step.accentColor,
                    fontWeight = FontWeight.Black,
                    fontSize = 17.sp
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = step.title,
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Icon(
                        imageVector = step.icon,
                        contentDescription = null,
                        tint = step.accentColor,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = step.mainText,
                    color = ZoTextPrimary,
                    fontSize = 13.sp,
                    lineHeight = 19.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                // ProTip / Kim lo belhna Box
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(ZoSurfaceElevated)
                        .border(1.dp, step.accentColor.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 12.dp, vertical = 7.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "💡 Tip: ",
                            color = Color(0xFFFFB300),
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                        Text(
                            text = step.proTip,
                            color = ZoTextSecondary,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }
    }
}
