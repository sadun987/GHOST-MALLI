package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.NetworkData
import com.example.ui.components.CyberBackground
import com.example.ui.components.CyberBadge
import com.example.ui.components.CyberButton
import com.example.ui.components.CyberCard
import com.example.ui.components.CyberHeader
import com.example.ui.theme.*

@Composable
fun NetworkInfoScreen(
    networkData: NetworkData,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    onBack: () -> Unit
) {
    val statusColor = if (networkData.isConnected) NeonGreen else NeonRed

    CyberBackground {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding(),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            item {
                CyberHeader(
                    title = "NET INSPECTOR",
                    subtitle = "INTERFACE & ROUTING TELEMETRY",
                    accentColor = NeonPink,
                    onBackClick = onBack
                )

                Spacer(modifier = Modifier.height(12.dp))

                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    // Overall Connection Status Card
                    CyberCard(accentColor = statusColor) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "CONNECTION STATE",
                                fontFamily = OrbitronFont,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = statusColor
                            )
                            CyberBadge(
                                text = if (networkData.isConnected) "ONLINE" else "OFFLINE",
                                accentColor = statusColor
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = networkData.connectionType,
                            fontFamily = OrbitronFont,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = TextPrimary
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = networkData.securityAssessment,
                            fontFamily = JetBrainsMono,
                            fontSize = 12.sp,
                            color = TextSecondary,
                            lineHeight = 16.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Interface Diagnostics Card
                    CyberCard(accentColor = NeonBlue) {
                        Text(
                            text = "INTERFACE TELEMETRY",
                            fontFamily = OrbitronFont,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = NeonBlue
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        TelemetryRow("Local IP Address", networkData.localIpAddress, NeonGreen)
                        TelemetryRow("Default Gateway", networkData.gateway, TextPrimary)
                        TelemetryRow("Subnet Mask", networkData.subnetMask, TextPrimary)
                        if (networkData.linkSpeedMbps > 0) {
                            TelemetryRow("Wi-Fi Link Speed", "${networkData.linkSpeedMbps} Mbps", NeonCyan)
                        }
                        if (networkData.frequencyMhz > 0) {
                            val band = if (networkData.frequencyMhz > 4900) "5 GHz" else "2.4 GHz"
                            TelemetryRow("Wi-Fi Frequency", "${networkData.frequencyMhz} MHz ($band)", NeonPurple)
                        }
                        if (networkData.dnsServers.isNotEmpty()) {
                            TelemetryRow("DNS Servers", networkData.dnsServers.joinToString(", "), NeonYellow)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Refresh Button
                    CyberButton(
                        text = if (isRefreshing) "POLLING INTERFACE..." else "REFRESH INTERFACES",
                        onClick = onRefresh,
                        accentColor = NeonPink,
                        testTag = "refresh_network_button"
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Defensive note
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
                                text = "DNS LEAK & MITM PREVENTION",
                                fontFamily = OrbitronFont,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = NeonGreen
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "When connecting to untrusted public networks, an ISP or rogue router can hijack plain UDP DNS requests (Port 53). Always verify DNS-over-HTTPS (DoH) or DNS-over-TLS (DoT) is active in Android Private DNS settings.",
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
private fun TelemetryRow(
    label: String,
    value: String,
    valueColor: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontFamily = JetBrainsMono,
            fontSize = 12.sp,
            color = TextSecondary
        )
        Text(
            text = value,
            fontFamily = JetBrainsMono,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            color = valueColor
        )
    }
}
