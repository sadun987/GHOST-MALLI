package com.example.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.R

val JetBrainsMono = FontFamily(
    Font(R.font.jetbrains_mono, FontWeight.Normal),
    Font(R.font.jetbrains_mono, FontWeight.Bold)
)

val OrbitronFont = FontFamily(
    Font(R.font.orbitron, FontWeight.Normal),
    Font(R.font.orbitron, FontWeight.Bold)
)

val Typography = Typography(
    displayLarge = TextStyle(
        fontFamily = OrbitronFont,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        lineHeight = 38.sp,
        letterSpacing = 1.sp,
        color = TextPrimary
    ),
    displayMedium = TextStyle(
        fontFamily = OrbitronFont,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        lineHeight = 30.sp,
        letterSpacing = 0.5.sp,
        color = TextPrimary
    ),
    headlineMedium = TextStyle(
        fontFamily = OrbitronFont,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        lineHeight = 26.sp,
        letterSpacing = 0.5.sp,
        color = TextPrimary
    ),
    titleLarge = TextStyle(
        fontFamily = OrbitronFont,
        fontWeight = FontWeight.Medium,
        fontSize = 18.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp,
        color = TextPrimary
    ),
    titleMedium = TextStyle(
        fontFamily = JetBrainsMono,
        fontWeight = FontWeight.SemiBold,
        fontSize = 15.sp,
        lineHeight = 22.sp,
        letterSpacing = 0.2.sp,
        color = TextPrimary
    ),
    bodyLarge = TextStyle(
        fontFamily = JetBrainsMono,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp,
        color = TextPrimary
    ),
    bodyMedium = TextStyle(
        fontFamily = JetBrainsMono,
        fontWeight = FontWeight.Normal,
        fontSize = 13.sp,
        lineHeight = 18.sp,
        letterSpacing = 0.2.sp,
        color = TextSecondary
    ),
    labelLarge = TextStyle(
        fontFamily = OrbitronFont,
        fontWeight = FontWeight.Bold,
        fontSize = 13.sp,
        lineHeight = 18.sp,
        letterSpacing = 0.8.sp,
        color = TextPrimary
    ),
    labelSmall = TextStyle(
        fontFamily = JetBrainsMono,
        fontWeight = FontWeight.Medium,
        fontSize = 10.sp,
        lineHeight = 14.sp,
        letterSpacing = 0.5.sp,
        color = TextMuted
    )
)
