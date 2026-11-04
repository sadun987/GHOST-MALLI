package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.CyberLesson
import com.example.ui.components.CyberBackground
import com.example.ui.components.CyberBadge
import com.example.ui.components.CyberCard
import com.example.ui.components.CyberHeader
import com.example.ui.theme.*

@Composable
fun EducationScreen(
    lessons: List<CyberLesson>,
    selectedLesson: CyberLesson?,
    onSelectLesson: (CyberLesson?) -> Unit,
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
                    title = "CYBER ACADEMY",
                    subtitle = "DEFENSIVE CYBERSECURITY CURRICULUM",
                    accentColor = NeonYellow,
                    onBackClick = onBack
                )

                Spacer(modifier = Modifier.height(12.dp))

                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    // Academy banner
                    CyberCard(accentColor = NeonYellow) {
                        Text(
                            text = "KNOWLEDGE IS ARMOR",
                            fontFamily = OrbitronFont,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = NeonYellow
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Explore the operational mechanics of real-world threats and master modern defensive protocols like Zero-Trust, Forward Secrecy, and Input Sanitization.",
                            fontFamily = JetBrainsMono,
                            fontSize = 12.sp,
                            color = TextSecondary,
                            lineHeight = 16.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "CORE MODULES",
                        fontFamily = OrbitronFont,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
            }

            items(lessons) { lesson ->
                val isExpanded = selectedLesson?.id == lesson.id

                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                    CyberCard(
                        accentColor = if (isExpanded) NeonYellow else CyberCardBorder,
                        onClick = {
                            if (isExpanded) onSelectLesson(null) else onSelectLesson(lesson)
                        },
                        testTag = "lesson_card_${lesson.id}"
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = lesson.title,
                                fontFamily = OrbitronFont,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = if (isExpanded) NeonYellow else TextPrimary,
                                modifier = Modifier.weight(1f)
                            )
                            CyberBadge(
                                text = lesson.difficulty.uppercase(),
                                accentColor = when (lesson.difficulty) {
                                    "Beginner" -> NeonGreen
                                    "Intermediate" -> NeonBlue
                                    else -> NeonPurple
                                }
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = lesson.category,
                            fontFamily = JetBrainsMono,
                            fontSize = 11.sp,
                            color = NeonYellow.copy(alpha = 0.8f)
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = lesson.summary,
                            fontFamily = JetBrainsMono,
                            fontSize = 12.sp,
                            color = TextSecondary,
                            lineHeight = 16.sp
                        )

                        // Expanded Lesson Details
                        AnimatedVisibility(visible = isExpanded) {
                            Column(modifier = Modifier.padding(top = 12.dp)) {
                                HorizontalDivider(color = CyberCardBorder)
                                Spacer(modifier = Modifier.height(10.dp))

                                Text(
                                    text = "KEY THREAT MECHANICS",
                                    fontFamily = OrbitronFont,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = NeonYellow
                                )

                                Spacer(modifier = Modifier.height(6.dp))

                                lesson.keyPoints.forEach { point ->
                                    Row(
                                        verticalAlignment = Alignment.Top,
                                        modifier = Modifier.padding(vertical = 3.dp)
                                    ) {
                                        Text(
                                            text = "•",
                                            color = NeonYellow,
                                            fontSize = 14.sp,
                                            modifier = Modifier.padding(end = 8.dp)
                                        )
                                        Text(
                                            text = point,
                                            fontFamily = JetBrainsMono,
                                            fontSize = 12.sp,
                                            color = TextPrimary,
                                            lineHeight = 16.sp
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                // Defensive mitigation card
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(NeonGreen.copy(alpha = 0.1f))
                                        .border(1.dp, NeonGreen.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                                        .padding(10.dp)
                                ) {
                                    Column {
                                        Text(
                                            text = "DEFENSIVE COUNTERMEASURE",
                                            fontFamily = OrbitronFont,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = NeonGreen
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = lesson.defensiveTip,
                                            fontFamily = JetBrainsMono,
                                            fontSize = 11.sp,
                                            color = TextPrimary,
                                            lineHeight = 15.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
