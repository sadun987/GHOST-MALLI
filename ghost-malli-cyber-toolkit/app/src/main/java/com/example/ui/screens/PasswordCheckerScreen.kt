package com.example.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.PasswordAnalysis
import com.example.model.PasswordLevel
import com.example.ui.components.CyberBackground
import com.example.ui.components.CyberBadge
import com.example.ui.components.CyberCard
import com.example.ui.components.CyberHeader
import com.example.ui.theme.*

@Composable
fun PasswordCheckerScreen(
    passwordInput: String,
    analysis: PasswordAnalysis,
    onPasswordChanged: (String) -> Unit,
    onBack: () -> Unit
) {
    var isPasswordVisible by remember { mutableStateOf(false) }

    val animatedScore by animateFloatAsState(
        targetValue = analysis.score / 100f,
        animationSpec = tween(600),
        label = "scoreAnim"
    )

    val scoreColor by animateColorAsState(
        targetValue = when (analysis.level) {
            PasswordLevel.VERY_WEAK -> NeonRed
            PasswordLevel.WEAK -> NeonPink
            PasswordLevel.FAIR -> NeonYellow
            PasswordLevel.STRONG -> NeonBlue
            PasswordLevel.VERY_STRONG -> NeonGreen
        },
        animationSpec = tween(400),
        label = "colorAnim"
    )

    CyberBackground {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding(),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            item {
                CyberHeader(
                    title = "PASS STRENGTH",
                    subtitle = "ENTROPY & BRUTE FORCE RESISTANCE",
                    accentColor = NeonGreen,
                    onBackClick = onBack
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Input Card
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    CyberCard(
                        accentColor = NeonGreen
                    ) {
                        Text(
                            text = "TARGET PASSWORD",
                            fontFamily = OrbitronFont,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = NeonGreen
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = passwordInput,
                            onValueChange = onPasswordChanged,
                            placeholder = {
                                Text(
                                    "Enter password to test...",
                                    color = TextMuted,
                                    fontFamily = JetBrainsMono,
                                    fontSize = 13.sp
                                )
                            },
                            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            trailingIcon = {
                                TextButton(
                                    onClick = { isPasswordVisible = !isPasswordVisible },
                                    modifier = Modifier.testTag("toggle_pass_visibility")
                                ) {
                                    Text(
                                        text = if (isPasswordVisible) "HIDE" else "SHOW",
                                        fontFamily = OrbitronFont,
                                        fontSize = 10.sp,
                                        color = NeonGreen
                                    )
                                }
                            },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("password_checker_input"),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = NeonGreen,
                                unfocusedBorderColor = CyberCardBorder,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary,
                                cursorColor = NeonGreen
                            ),
                            textStyle = MaterialTheme.typography.bodyLarge
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Score Gauge Card
                    CyberCard(
                        accentColor = scoreColor
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "SECURITY RATING",
                                fontFamily = OrbitronFont,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = scoreColor
                            )
                            CyberBadge(
                                text = "${analysis.score}/100",
                                accentColor = scoreColor
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Progress Bar
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(12.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(CyberSurface)
                                .border(1.dp, scoreColor.copy(alpha = 0.3f), RoundedCornerShape(6.dp))
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(animatedScore)
                                    .fillMaxHeight()
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(
                                        Brush.horizontalGradient(
                                            listOf(scoreColor.copy(alpha = 0.6f), scoreColor)
                                        )
                                    )
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = analysis.level.label,
                            fontFamily = OrbitronFont,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = scoreColor
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "Estimated Offline Crack Time: ${analysis.crackTimeEstimate}",
                            fontFamily = JetBrainsMono,
                            fontSize = 12.sp,
                            color = TextPrimary
                        )

                        Text(
                            text = "Shannon Entropy: ${analysis.entropy} bits (Length: ${analysis.length})",
                            fontFamily = JetBrainsMono,
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Character Complexity Matrix
                    CyberCard(accentColor = NeonBlue) {
                        Text(
                            text = "CHARACTER COMPOSITION",
                            fontFamily = OrbitronFont,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = NeonBlue
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            ComplexityItem("LOWERCASE", "a-z", analysis.hasLower)
                            ComplexityItem("UPPERCASE", "A-Z", analysis.hasUpper)
                            ComplexityItem("DIGITS", "0-9", analysis.hasDigit)
                            ComplexityItem("SYMBOLS", "#$%", analysis.hasSpecial)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "DEFENSIVE RECOMMENDATIONS",
                        fontFamily = OrbitronFont,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
            }

            items(analysis.feedback) { tip ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(CyberSurfaceElevated)
                        .border(1.dp, CyberCardBorder, RoundedCornerShape(10.dp))
                        .padding(12.dp)
                ) {
                    Row(verticalAlignment = Alignment.Top) {
                        Text(
                            text = "◆",
                            color = NeonGreen,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(top = 2.dp, end = 8.dp)
                        )
                        Text(
                            text = tip,
                            fontFamily = JetBrainsMono,
                            fontSize = 12.sp,
                            color = TextSecondary,
                            lineHeight = 16.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ComplexityItem(
    label: String,
    sub: String,
    isActive: Boolean
) {
    val activeColor = if (isActive) NeonGreen else TextMuted
    val bgColor = if (isActive) NeonGreen.copy(alpha = 0.15f) else CyberSurface

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(bgColor)
            .border(1.dp, activeColor.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 8.dp)
    ) {
        Text(
            text = if (isActive) "✓" else "✗",
            color = activeColor,
            fontFamily = OrbitronFont,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )
        Text(
            text = label,
            fontFamily = OrbitronFont,
            fontSize = 9.sp,
            color = activeColor,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = sub,
            fontFamily = JetBrainsMono,
            fontSize = 9.sp,
            color = TextSecondary
        )
    }
}
