package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.SecurityTip
import com.example.model.TipPriority
import com.example.ui.components.CyberBackground
import com.example.ui.components.CyberBadge
import com.example.ui.components.CyberCard
import com.example.ui.components.CyberHeader
import com.example.ui.theme.*

@Composable
fun SettingsScreen(
    securityTips: List<SecurityTip>,
    onBack: () -> Unit
) {
    CyberBackground {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding(),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            item {
                CyberHeader(
                    title = "SYSTEM & ABOUT",
                    subtitle = "HARDENING TIPS & CREDITS",
                    accentColor = NeonGreen,
                    onBackClick = onBack
                )

                Spacer(modifier = Modifier.height(12.dp))

                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    // Developer Credits Card
                    CyberCard(
                        accentColor = NeonGreen,
                        testTag = "developer_credits_card"
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ghost_malli_logo),
                                contentDescription = "Ghost Malli Avatar",
                                modifier = Modifier
                                    .size(60.dp)
                                    .clip(CircleShape)
                                    .border(1.5.dp, NeonGreen, CircleShape)
                            )

                            Spacer(modifier = Modifier.width(14.dp))

                            Column {
                                Text(
                                    text = "GHOST MALLI",
                                    fontFamily = OrbitronFont,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 17.sp,
                                    color = NeonGreen
                                )
                                Text(
                                    text = "Lead Security Architect & Developer",
                                    fontFamily = JetBrainsMono,
                                    fontSize = 11.sp,
                                    color = NeonBlue
                                )
                                Text(
                                    text = "Cyber Security Toolkit v1.0",
                                    fontFamily = JetBrainsMono,
                                    fontSize = 10.sp,
                                    color = TextMuted
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Crafted with passion for ethical cybersecurity, defensive hygiene, and open cryptographic standards. No tracking, zero third-party telemetry, 100% on-device.",
                            fontFamily = JetBrainsMono,
                            fontSize = 11.sp,
                            color = TextSecondary,
                            lineHeight = 16.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Ethics & Compliance Card
                    CyberCard(accentColor = NeonBlue) {
                        Text(
                            text = "COMPLIANCE & ETHICAL CHARTER",
                            fontFamily = OrbitronFont,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = NeonBlue
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "• Purely Defensive: All tools are designed for user-directed safety audits, self-defense and learning.\n• No Hacking or Exploits: Contains zero unauthorized access payloads, network scanners, or malicious tools.\n• Google Play Compliant: Complies strictly with Google Play Developer Policies and Android Least Privilege guidelines.",
                            fontFamily = JetBrainsMono,
                            fontSize = 11.sp,
                            color = TextSecondary,
                            lineHeight = 16.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "CORE DEFENSIVE HARDENING TIPS",
                        fontFamily = OrbitronFont,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
            }

            items(securityTips) { tip ->
                val priorityColor = when (tip.priority) {
                    TipPriority.CRITICAL -> NeonRed
                    TipPriority.HIGH -> NeonYellow
                    TipPriority.RECOMMENDED -> NeonGreen
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 5.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(CyberSurfaceElevated)
                        .border(1.dp, priorityColor.copy(alpha = 0.3f), RoundedCornerShape(10.dp))
                        .padding(14.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = tip.title,
                                fontFamily = OrbitronFont,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = TextPrimary
                            )
                            CyberBadge(
                                text = tip.priority.name,
                                accentColor = priorityColor
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = tip.category,
                            fontFamily = JetBrainsMono,
                            fontSize = 10.sp,
                            color = priorityColor
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = tip.description,
                            fontFamily = JetBrainsMono,
                            fontSize = 11.sp,
                            color = TextSecondary,
                            lineHeight = 16.sp
                        )
                    }
                }
            }
        }
    }
}
