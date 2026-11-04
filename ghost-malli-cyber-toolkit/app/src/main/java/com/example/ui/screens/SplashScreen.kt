package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.Screen
import com.example.ui.components.CyberBackground
import com.example.ui.components.CyberButton
import com.example.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onEnterApp: () -> Unit
) {
    var glitchState by remember { mutableStateOf(false) }
    var bootStep by remember { mutableStateOf("INITIALIZING SECURE KERNEL...") }

    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.96f,
        targetValue = 1.04f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )

    val neonGlowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.95f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow"
    )

    LaunchedEffect(Unit) {
        delay(600)
        bootStep = "LOADING DEFENSIVE CIPHER PROTOCOLS..."
        delay(600)
        bootStep = "ESTABLISHING ENCRYPTED SANDBOX..."
        delay(600)
        bootStep = "SYSTEM READY: GHOST MALLI ONLINE"
    }

    CyberBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .statusBarsPadding()
                .navigationBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top Terminal Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "[ SYS://DEFENSE.ONLINE ]",
                    fontFamily = JetBrainsMono,
                    fontSize = 11.sp,
                    color = NeonGreen
                )
                Text(
                    text = "v1.0.0 ARMORED",
                    fontFamily = OrbitronFont,
                    fontSize = 10.sp,
                    color = NeonPurple
                )
            }

            // Center Logo & Hero
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(220.dp)
                        .scale(pulseScale)
                ) {
                    // Outer Neon Ring
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                            .border(
                                2.dp,
                                Brush.sweepGradient(listOf(NeonGreen, NeonPurple, NeonBlue, NeonGreen)),
                                CircleShape
                            )
                            .alpha(neonGlowAlpha)
                    )

                    // Logo Image
                    Image(
                        painter = painterResource(id = R.drawable.ghost_malli_logo),
                        contentDescription = "GHOST MALLI Logo",
                        modifier = Modifier
                            .size(190.dp)
                            .clip(CircleShape)
                            .testTag("ghost_logo_image")
                    )
                }

                Spacer(modifier = Modifier.height(28.dp))

                Text(
                    text = "GHOST MALLI",
                    style = MaterialTheme.typography.displayLarge,
                    color = NeonGreen,
                    letterSpacing = 3.sp,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "CYBER SECURITY EDUCATION TOOLKIT",
                    fontFamily = OrbitronFont,
                    fontWeight = FontWeight.Medium,
                    fontSize = 12.sp,
                    color = NeonBlue,
                    letterSpacing = 1.5.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 6.dp)
                )

                Text(
                    text = "Engineered by Ghost Malli",
                    fontFamily = JetBrainsMono,
                    fontSize = 11.sp,
                    color = TextSecondary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 4.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Terminal status box
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(CyberSurface)
                        .border(1.dp, NeonGreen.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 14.dp, vertical = 10.dp)
                ) {
                    Text(
                        text = "> $bootStep",
                        fontFamily = JetBrainsMono,
                        fontSize = 11.sp,
                        color = NeonGreen,
                        textAlign = TextAlign.Start
                    )
                }
            }

            // Bottom CTA
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CyberButton(
                    text = "ACCESS TOOLKIT ❯",
                    onClick = onEnterApp,
                    accentColor = NeonGreen,
                    testTag = "enter_app_button"
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Defensive & Educational Security Protocols Only",
                    fontFamily = JetBrainsMono,
                    fontSize = 10.sp,
                    color = TextMuted,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
