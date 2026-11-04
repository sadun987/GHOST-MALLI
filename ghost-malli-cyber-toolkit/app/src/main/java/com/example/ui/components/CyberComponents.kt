package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun CyberBackground(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(CyberDarkBg)
            .drawBehind {
                // Subtle cyberpunk grid & radial ambient glow
                val canvasWidth = size.width
                val canvasHeight = size.height

                // Radial ambient glow at top right (Neon Purple)
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(NeonPurpleGlow, Color.Transparent),
                        center = Offset(canvasWidth * 0.85f, canvasHeight * 0.15f),
                        radius = canvasWidth * 0.6f
                    )
                )

                // Radial ambient glow at bottom left (Neon Green)
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(NeonGreenGlow, Color.Transparent),
                        center = Offset(canvasWidth * 0.15f, canvasHeight * 0.85f),
                        radius = canvasWidth * 0.6f
                    )
                )

                // Tech grid lines
                val gridSpacing = 40.dp.toPx()
                val gridColor = Color(0x0A00FF88)
                var x = 0f
                while (x < canvasWidth) {
                    drawLine(gridColor, Offset(x, 0f), Offset(x, canvasHeight), strokeWidth = 1f)
                    x += gridSpacing
                }
                var y = 0f
                while (y < canvasHeight) {
                    drawLine(gridColor, Offset(0f, y), Offset(canvasWidth, y), strokeWidth = 1f)
                    y += gridSpacing
                }
            }
    ) {
        content()
    }
}

@Composable
fun CyberCard(
    modifier: Modifier = Modifier,
    accentColor: Color = NeonGreen,
    onClick: (() -> Unit)? = null,
    testTag: String? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    val shape = RoundedCornerShape(16.dp)
    var cardModifier = modifier
        .fillMaxWidth()
        .clip(shape)
        .background(
            brush = Brush.linearGradient(
                colors = listOf(
                    CyberSurfaceElevated.copy(alpha = 0.92f),
                    CyberSurface.copy(alpha = 0.95f)
                )
            )
        )
        .border(
            width = 1.dp,
            brush = Brush.linearGradient(
                colors = listOf(
                    accentColor.copy(alpha = 0.6f),
                    CyberCardBorder.copy(alpha = 0.5f),
                    accentColor.copy(alpha = 0.1f)
                )
            ),
            shape = shape
        )

    if (testTag != null) {
        cardModifier = cardModifier.testTag(testTag)
    }

    if (onClick != null) {
        cardModifier = cardModifier.clickable(onClick = onClick)
    }

    Column(
        modifier = cardModifier.padding(16.dp),
        content = content
    )
}

@Composable
fun CyberBadge(
    text: String,
    accentColor: Color = NeonGreen,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(accentColor.copy(alpha = 0.15f))
            .border(0.8.dp, accentColor.copy(alpha = 0.7f), RoundedCornerShape(6.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = accentColor,
            fontFamily = OrbitronFont,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp
        )
    }
}

@Composable
fun CyberButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    accentColor: Color = NeonGreen,
    testTag: String = "cyber_button"
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
            .testTag(testTag),
        colors = ButtonDefaults.buttonColors(
            containerColor = accentColor,
            contentColor = Color.Black
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = Color.Black
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                text = text,
                fontFamily = OrbitronFont,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                letterSpacing = 0.8.sp,
                color = Color.Black
            )
        }
    }
}

@Composable
fun CyberHeader(
    title: String,
    subtitle: String? = null,
    accentColor: Color = NeonGreen,
    onBackClick: (() -> Unit)? = null
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (onBackClick != null) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier
                        .size(48.dp)
                        .testTag("back_button")
                ) {
                    Text(
                        text = "◀",
                        color = accentColor,
                        fontFamily = OrbitronFont,
                        fontSize = 18.sp
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.displayMedium,
                    color = accentColor
                )
                if (subtitle != null) {
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondary
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(6.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(accentColor, accentColor.copy(alpha = 0.1f), Color.Transparent)
                    )
                )
        )
    }
}
