package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.CyberBackground
import com.example.ui.components.CyberBadge
import com.example.ui.components.CyberButton
import com.example.ui.components.CyberCard
import com.example.ui.components.CyberHeader
import com.example.ui.theme.*

@Composable
fun PasswordGeneratorScreen(
    length: Int,
    includeUpper: Boolean,
    includeLower: Boolean,
    includeDigits: Boolean,
    includeSymbols: Boolean,
    generatedPassword: String,
    onLengthChanged: (Int) -> Unit,
    onToggleUpper: () -> Unit,
    onToggleLower: () -> Unit,
    onToggleDigits: () -> Unit,
    onToggleSymbols: () -> Unit,
    onRegenerate: () -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current

    CyberBackground {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding(),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            item {
                CyberHeader(
                    title = "CIPHER GEN",
                    subtitle = "CRYPTOGRAPHIC PASSWORD CREATOR",
                    accentColor = NeonPurple,
                    onBackClick = onBack
                )

                Spacer(modifier = Modifier.height(12.dp))

                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    // Generated Output Box
                    CyberCard(
                        accentColor = NeonPurple,
                        testTag = "generated_password_card"
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "GENERATED CIPHER",
                                fontFamily = OrbitronFont,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = NeonPurple
                            )
                            CyberBadge(
                                text = "$length CHARS",
                                accentColor = NeonPurple
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(CyberSurface)
                                .border(1.dp, NeonPurple.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                                .padding(14.dp)
                        ) {
                            Text(
                                text = generatedPassword.ifEmpty { "Generating..." },
                                fontFamily = JetBrainsMono,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = NeonPurple,
                                letterSpacing = 1.sp,
                                modifier = Modifier.testTag("generated_password_text")
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Button(
                                onClick = {
                                    val clipboard =
                                        context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                    clipboard.setPrimaryClip(
                                        ClipData.newPlainText("Generated Password", generatedPassword)
                                    )
                                    Toast.makeText(context, "Password copied to clipboard!", Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp)
                                    .testTag("copy_password_button"),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = CyberSurfaceElevated,
                                    contentColor = NeonGreen
                                ),
                                shape = RoundedCornerShape(10.dp),
                                border = ButtonDefaults.outlinedButtonBorder.copy(
                                    brush = androidx.compose.ui.graphics.SolidColor(NeonGreen.copy(alpha = 0.6f))
                                )
                            ) {
                                Text(
                                    text = "COPY CIPHER",
                                    fontFamily = OrbitronFont,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Button(
                                onClick = onRegenerate,
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp)
                                    .testTag("regenerate_password_button"),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = NeonPurple,
                                    contentColor = Color.White
                                ),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text(
                                    text = "RE-GENERATE",
                                    fontFamily = OrbitronFont,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Configuration Options
                    CyberCard(accentColor = NeonBlue) {
                        Text(
                            text = "ENTROPY PARAMETERS",
                            fontFamily = OrbitronFont,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = NeonBlue
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Length Slider
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Password Length",
                                fontFamily = JetBrainsMono,
                                fontSize = 13.sp,
                                color = TextPrimary
                            )
                            Text(
                                text = "$length",
                                fontFamily = OrbitronFont,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = NeonBlue
                            )
                        }

                        Slider(
                            value = length.toFloat(),
                            onValueChange = { onLengthChanged(it.toInt()) },
                            valueRange = 8f..32f,
                            steps = 23,
                            modifier = Modifier.testTag("password_length_slider"),
                            colors = SliderDefaults.colors(
                                thumbColor = NeonBlue,
                                activeTrackColor = NeonBlue,
                                inactiveTrackColor = CyberCardBorder
                            )
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Toggles
                        ToggleRuleRow(
                            title = "Uppercase Letters (A-Z)",
                            checked = includeUpper,
                            onToggle = onToggleUpper,
                            testTag = "toggle_upper"
                        )
                        ToggleRuleRow(
                            title = "Lowercase Letters (a-z)",
                            checked = includeLower,
                            onToggle = onToggleLower,
                            testTag = "toggle_lower"
                        )
                        ToggleRuleRow(
                            title = "Numeric Digits (0-9)",
                            checked = includeDigits,
                            onToggle = onToggleDigits,
                            testTag = "toggle_digits"
                        )
                        ToggleRuleRow(
                            title = "Cryptographic Symbols (!@#$)",
                            checked = includeSymbols,
                            onToggle = onToggleSymbols,
                            testTag = "toggle_symbols"
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Educational note
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(CyberSurfaceElevated)
                            .border(1.dp, CyberCardBorder, RoundedCornerShape(10.dp))
                            .padding(14.dp)
                    ) {
                        Column {
                            Text(
                                text = "CSPRNG STANDARD",
                                fontFamily = OrbitronFont,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = NeonGreen
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Generated keys utilize Java SecureRandom (Linux /dev/urandom), providing cryptographically strong pseudo-random numbers immune to predictable seed replay attacks.",
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
}

@Composable
private fun ToggleRuleRow(
    title: String,
    checked: Boolean,
    onToggle: () -> Unit,
    testTag: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onToggle)
            .padding(vertical = 8.dp, horizontal = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontFamily = JetBrainsMono,
            fontSize = 12.sp,
            color = if (checked) TextPrimary else TextMuted
        )

        Switch(
            checked = checked,
            onCheckedChange = { onToggle() },
            modifier = Modifier.testTag(testTag),
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.Black,
                checkedTrackColor = NeonBlue,
                uncheckedThumbColor = TextMuted,
                uncheckedTrackColor = CyberSurface
            )
        )
    }
}
