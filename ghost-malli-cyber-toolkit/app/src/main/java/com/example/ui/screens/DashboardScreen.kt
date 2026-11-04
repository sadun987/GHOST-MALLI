package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Screen
import com.example.ui.components.CyberBackground
import com.example.ui.components.CyberBadge
import com.example.ui.components.CyberCard
import com.example.ui.theme.*

data class ToolCardItem(
    val screen: Screen,
    val title: String,
    val category: String,
    val description: String,
    val accentColor: Color,
    val tag: String,
    val symbol: String
)

@Composable
fun DashboardScreen(
    onNavigate: (Screen) -> Unit
) {
    val tools = remember {
        listOf(
            ToolCardItem(
                screen = Screen.PASSWORD_CHECKER,
                title = "Password Strength",
                category = "AUDIT",
                description = "Entropy analysis, brute-force crack time & character composition scoring.",
                accentColor = NeonGreen,
                tag = "tool_card_pass_checker",
                symbol = "⚿"
            ),
            ToolCardItem(
                screen = Screen.PASSWORD_GENERATOR,
                title = "Cipher Generator",
                category = "CRYPTO",
                description = "CSPRNG cryptographically secure password & key creator with custom rules.",
                accentColor = NeonPurple,
                tag = "tool_card_pass_gen",
                symbol = "◈"
            ),
            ToolCardItem(
                screen = Screen.URL_CHECKER,
                title = "URL Safety Scanner",
                category = "DEFENSE",
                description = "Inspect phishing markers, raw IP hostnames, TLS encryption & deceptive TLDs.",
                accentColor = NeonBlue,
                tag = "tool_card_url_checker",
                symbol = "◎"
            ),
            ToolCardItem(
                screen = Screen.FILE_HASH,
                title = "SHA-256 Checksum",
                category = "INTEGRITY",
                description = "Compute and verify cryptographic digests for local files and raw payloads.",
                accentColor = NeonCyan,
                tag = "tool_card_file_hash",
                symbol = "⌬"
            ),
            ToolCardItem(
                screen = Screen.NETWORK_INFO,
                title = "Network Inspector",
                category = "TELEMETRY",
                description = "Live local IP, gateway, DNS servers, link speeds and subnet security assessment.",
                accentColor = NeonPink,
                tag = "tool_card_network_info",
                symbol = "☵"
            ),
            ToolCardItem(
                screen = Screen.EDUCATION,
                title = "Cyber Academy",
                category = "KNOWLEDGE",
                description = "Interactive defensive security curriculum: Zero-Trust, MITM, OWASP & Hashes.",
                accentColor = NeonYellow,
                tag = "tool_card_education",
                symbol = "☶"
            ),
            ToolCardItem(
                screen = Screen.SETTINGS,
                title = "Security & About",
                category = "SYSTEM",
                description = "Defensive tips, ethical guidelines, compliance info & Ghost Malli credits.",
                accentColor = NeonGreen,
                tag = "tool_card_settings",
                symbol = "⚙"
            )
        )
    }

    // Animated glow pulse
    val infiniteTransition = rememberInfiniteTransition(label = "dashPulse")
    val borderAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "dashBorderPulse"
    )

    CyberBackground {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .statusBarsPadding(),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            // Header
            item {
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "GHOST MALLI",
                            style = MaterialTheme.typography.displayMedium,
                            color = NeonGreen
                        )
                        Text(
                            text = "DEFENSIVE CYBER SECURITY TOOLKIT",
                            fontFamily = OrbitronFont,
                            fontSize = 11.sp,
                            color = NeonBlue
                        )
                    }

                    CyberBadge(
                        text = "SECURE MODE",
                        accentColor = NeonGreen
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Hero Status Banner
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            Brush.linearGradient(
                                listOf(CyberSurfaceElevated, CyberSurface)
                            )
                        )
                        .border(
                            1.dp,
                            NeonGreen.copy(alpha = borderAlpha),
                            RoundedCornerShape(16.dp)
                        )
                        .padding(18.dp)
                ) {
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "SHIELD SYSTEM: ACTIVE",
                                fontFamily = OrbitronFont,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = NeonGreen
                            )
                            Text(
                                text = "7 ARMORED MODULES",
                                fontFamily = JetBrainsMono,
                                fontSize = 11.sp,
                                color = TextSecondary
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "All tools operate purely locally and defensively on-device. No telemetry, no offensive payloads, strictly compliant with ethical cybersecurity guidelines.",
                            fontFamily = JetBrainsMono,
                            fontSize = 12.sp,
                            color = TextSecondary,
                            lineHeight = 17.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "DEFENSIVE UTILITIES",
                    fontFamily = OrbitronFont,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = TextPrimary,
                    letterSpacing = 1.sp,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
            }

            // Animated Tool Cards
            items(tools.size) { index ->
                val tool = tools[index]
                CyberCard(
                    accentColor = tool.accentColor,
                    onClick = { onNavigate(tool.screen) },
                    testTag = tool.tag,
                    modifier = Modifier.padding(bottom = 12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Icon circle
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(tool.accentColor.copy(alpha = 0.15f))
                                .border(1.dp, tool.accentColor.copy(alpha = 0.6f), CircleShape)
                        ) {
                            Text(
                                text = tool.symbol,
                                color = tool.accentColor,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = tool.title,
                                    fontFamily = OrbitronFont,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = TextPrimary
                                )

                                CyberBadge(
                                    text = tool.category,
                                    accentColor = tool.accentColor
                                )
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = tool.description,
                                fontFamily = JetBrainsMono,
                                fontSize = 12.sp,
                                color = TextSecondary,
                                lineHeight = 16.sp
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = "❯",
                            color = tool.accentColor,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                // Developer Watermark
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "GHOST MALLI // DEFENSE CORE",
                        fontFamily = OrbitronFont,
                        fontSize = 11.sp,
                        color = NeonGreen.copy(alpha = 0.7f),
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "Architected & Developed by Ghost Malli",
                        fontFamily = JetBrainsMono,
                        fontSize = 10.sp,
                        color = TextMuted
                    )
                }
            }
        }
    }
}
