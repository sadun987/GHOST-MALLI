package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.CyberBackground
import com.example.ui.components.CyberBadge
import com.example.ui.components.CyberButton
import com.example.ui.components.CyberCard
import com.example.ui.components.CyberHeader
import com.example.ui.theme.*

@Composable
fun FileHashScreen(
    fileHash: String?,
    fileName: String?,
    isHashing: Boolean,
    textToHash: String,
    textHash: String?,
    onHashSelectedFile: (Uri, String?) -> Unit,
    onTextToHashChanged: (String) -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current

    // Zero-permission OpenDocument picker for file hashing
    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        if (uri != null) {
            var name: String? = null
            try {
                context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                    val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    if (cursor.moveToFirst() && nameIndex != -1) {
                        name = cursor.getString(nameIndex)
                    }
                }
            } catch (e: Exception) {
                name = uri.lastPathSegment
            }
            onHashSelectedFile(uri, name)
        }
    }

    CyberBackground {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding(),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            item {
                CyberHeader(
                    title = "SHA-256 HASHER",
                    subtitle = "ONE-WAY CRYPTOGRAPHIC INTEGRITY CHECK",
                    accentColor = NeonCyan,
                    onBackClick = onBack
                )

                Spacer(modifier = Modifier.height(12.dp))

                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    // File Hashing Section
                    CyberCard(accentColor = NeonCyan) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "FILE INTEGRITY AUDIT",
                                fontFamily = OrbitronFont,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = NeonCyan
                            )
                            CyberBadge(text = "256-BIT DIGEST", accentColor = NeonCyan)
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Select any file on your device to compute its SHA-256 hash. Used to verify software binaries haven't been tampered with or modified by malware.",
                            fontFamily = JetBrainsMono,
                            fontSize = 12.sp,
                            color = TextSecondary,
                            lineHeight = 16.sp
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        CyberButton(
                            text = if (isHashing) "COMPUTING HASH..." else "CHOOSE FILE TO HASH",
                            onClick = { filePickerLauncher.launch(arrayOf("*/*")) },
                            accentColor = NeonCyan,
                            testTag = "choose_file_button"
                        )

                        if (fileName != null) {
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "Target: $fileName",
                                fontFamily = JetBrainsMono,
                                fontSize = 12.sp,
                                color = TextPrimary
                            )
                        }

                        if (fileHash != null) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(CyberSurface)
                                    .border(1.dp, NeonCyan.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                                    .padding(12.dp)
                            ) {
                                Column {
                                    Text(
                                        text = fileHash,
                                        fontFamily = JetBrainsMono,
                                        fontSize = 11.sp,
                                        color = NeonCyan,
                                        lineHeight = 16.sp,
                                        modifier = Modifier.testTag("file_hash_result")
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Button(
                                        onClick = {
                                            val clipboard =
                                                context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                            clipboard.setPrimaryClip(ClipData.newPlainText("SHA256", fileHash))
                                            Toast.makeText(context, "SHA-256 hash copied!", Toast.LENGTH_SHORT).show()
                                        },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = CyberSurfaceElevated,
                                            contentColor = NeonCyan
                                        ),
                                        shape = RoundedCornerShape(6.dp),
                                        modifier = Modifier.height(36.dp)
                                    ) {
                                        Text("COPY HASH", fontFamily = OrbitronFont, fontSize = 10.sp)
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Text String Hashing Section
                    CyberCard(accentColor = NeonPurple) {
                        Text(
                            text = "PAYLOAD STRING HASHER",
                            fontFamily = OrbitronFont,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = NeonPurple
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = textToHash,
                            onValueChange = onTextToHashChanged,
                            placeholder = {
                                Text(
                                    "Type text to see real-time SHA-256 avalanche effect...",
                                    color = TextMuted,
                                    fontFamily = JetBrainsMono,
                                    fontSize = 12.sp
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("text_hasher_input"),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = NeonPurple,
                                unfocusedBorderColor = CyberCardBorder,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary,
                                cursorColor = NeonPurple
                            ),
                            textStyle = MaterialTheme.typography.bodyLarge
                        )

                        if (textHash != null) {
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "SHA-256 Digest:",
                                fontFamily = OrbitronFont,
                                fontSize = 11.sp,
                                color = TextSecondary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(CyberSurface)
                                    .border(1.dp, NeonPurple.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                                    .padding(10.dp)
                            ) {
                                Text(
                                    text = textHash,
                                    fontFamily = JetBrainsMono,
                                    fontSize = 11.sp,
                                    color = NeonPurple,
                                    lineHeight = 16.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
