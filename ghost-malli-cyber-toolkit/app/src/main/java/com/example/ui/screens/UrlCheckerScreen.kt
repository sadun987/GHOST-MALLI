package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.UrlAnalysisResult
import com.example.model.UrlVerdict
import com.example.ui.components.CyberBackground
import com.example.ui.components.CyberBadge
import com.example.ui.components.CyberButton
import com.example.ui.components.CyberCard
import com.example.ui.components.CyberHeader
import com.example.ui.theme.*

@Composable
fun UrlCheckerScreen(
    urlInput: String,
    urlAnalysis: UrlAnalysisResult?,
    onUrlInputChanged: (String) -> Unit,
    onCheckUrl: () -> Unit,
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
                    title = "URL SCANNER",
                    subtitle = "DEEP PHISHING & PROTOCOL INSPECTOR",
                    accentColor = NeonBlue,
                    onBackClick = onBack
                )

                Spacer(modifier = Modifier.height(12.dp))

                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    // Input Card
                    CyberCard(accentColor = NeonBlue) {
                        Text(
                            text = "TARGET URL / DOMAIN",
                            fontFamily = OrbitronFont,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = NeonBlue
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = urlInput,
                            onValueChange = onUrlInputChanged,
                            placeholder = {
                                Text(
                                    "e.g. login.secure-update.xyz or bank.com",
                                    color = TextMuted,
                                    fontFamily = JetBrainsMono,
                                    fontSize = 12.sp
                                )
                            },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Uri,
                                imeAction = ImeAction.Go
                            ),
                            keyboardActions = KeyboardActions(onGo = { onCheckUrl() }),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("url_checker_input"),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = NeonBlue,
                                unfocusedBorderColor = CyberCardBorder,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary,
                                cursorColor = NeonBlue
                            ),
                            textStyle = MaterialTheme.typography.bodyLarge
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        CyberButton(
                            text = "SCAN TARGET URL",
                            onClick = onCheckUrl,
                            accentColor = NeonBlue,
                            testTag = "scan_url_button"
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    if (urlAnalysis != null) {
                        val verdictColor = when (urlAnalysis.verdict) {
                            UrlVerdict.SAFE -> NeonGreen
                            UrlVerdict.SUSPICIOUS -> NeonYellow
                            UrlVerdict.POTENTIALLY_DANGEROUS -> NeonRed
                        }

                        CyberCard(accentColor = verdictColor) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "SAFETY VERDICT",
                                    fontFamily = OrbitronFont,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = verdictColor
                                )

                                CyberBadge(
                                    text = "RISK ${urlAnalysis.riskScore}/100",
                                    accentColor = verdictColor
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = urlAnalysis.verdict.label,
                                fontFamily = OrbitronFont,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = verdictColor
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "Host: ${urlAnalysis.host}",
                                fontFamily = JetBrainsMono,
                                fontSize = 12.sp,
                                color = TextPrimary
                            )
                            Text(
                                text = "Scheme: ${urlAnalysis.scheme.uppercase()} (Encrypted: ${urlAnalysis.isSecureScheme})",
                                fontFamily = JetBrainsMono,
                                fontSize = 12.sp,
                                color = if (urlAnalysis.isSecureScheme) NeonGreen else NeonRed
                            )
                            if (urlAnalysis.port != null) {
                                Text(
                                    text = "Port: ${urlAnalysis.port}",
                                    fontFamily = JetBrainsMono,
                                    fontSize = 12.sp,
                                    color = TextSecondary
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "DETECTION TELEMETRY",
                            fontFamily = OrbitronFont,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }
                }
            }

            if (urlAnalysis != null) {
                items(urlAnalysis.findings) { finding ->
                    val itemColor = if (finding.isWarning) NeonRed else NeonGreen
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 5.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(CyberSurfaceElevated)
                            .border(1.dp, itemColor.copy(alpha = 0.4f), RoundedCornerShape(10.dp))
                            .padding(14.dp)
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = if (finding.isWarning) "⚠" else "✓",
                                    color = itemColor,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(end = 8.dp)
                                )
                                Text(
                                    text = finding.title,
                                    fontFamily = OrbitronFont,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = itemColor
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = finding.description,
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
}
