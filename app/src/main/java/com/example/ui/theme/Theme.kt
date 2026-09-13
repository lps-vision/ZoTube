package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val ZoTubeColorScheme = darkColorScheme(
    primary = ZoRed,
    onPrimary = Color.White,
    primaryContainer = ZoRedDark,
    onPrimaryContainer = Color.White,
    secondary = ZoShieldGreen,
    onSecondary = Color.Black,
    secondaryContainer = ZoShieldGreenDim,
    onSecondaryContainer = ZoShieldGreen,
    background = ZoDarkBackground,
    onBackground = ZoTextPrimary,
    surface = ZoSurfaceDark,
    onSurface = ZoTextPrimary,
    surfaceVariant = ZoSurfaceElevated,
    onSurfaceVariant = ZoTextSecondary,
    outline = ZoSurfaceBorder
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true, // TV apps are always best in rich cinematic dark theme
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = ZoTubeColorScheme,
        typography = Typography,
        content = content
    )
}
