package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = NeonGreen,
    onPrimary = Color.Black,
    primaryContainer = NeonGreenDim,
    onPrimaryContainer = Color.White,
    secondary = NeonPurple,
    onSecondary = Color.White,
    secondaryContainer = NeonPurpleDim,
    onSecondaryContainer = Color.White,
    tertiary = NeonBlue,
    onTertiary = Color.Black,
    background = CyberDarkBg,
    onBackground = TextPrimary,
    surface = CyberSurface,
    onSurface = TextPrimary,
    surfaceVariant = CyberSurfaceElevated,
    onSurfaceVariant = TextSecondary,
    outline = CyberCardBorder
)

@Composable
fun GhostMalliTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
