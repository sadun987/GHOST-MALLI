package com.example.model

enum class Screen(val title: String, val iconName: String) {
    SPLASH("Splash", "security"),
    DASHBOARD("Dashboard", "dashboard"),
    PASSWORD_CHECKER("Strength Checker", "lock"),
    PASSWORD_GENERATOR("Pass Generator", "key"),
    URL_CHECKER("URL Scanner", "shield"),
    FILE_HASH("File Hash (SHA-256)", "fingerprint"),
    NETWORK_INFO("Network Info", "wifi"),
    EDUCATION("Cyber Academy", "school"),
    SETTINGS("Security & About", "settings")
}

data class PasswordAnalysis(
    val score: Int = 0, // 0 to 100
    val level: PasswordLevel = PasswordLevel.VERY_WEAK,
    val entropy: Double = 0.0,
    val length: Int = 0,
    val hasUpper: Boolean = false,
    val hasLower: Boolean = false,
    val hasDigit: Boolean = false,
    val hasSpecial: Boolean = false,
    val feedback: List<String> = emptyList(),
    val crackTimeEstimate: String = "Instant"
)

enum class PasswordLevel(val label: String) {
    VERY_WEAK("CRITICAL / VERY WEAK"),
    WEAK("WEAK"),
    FAIR("MODERATE"),
    STRONG("STRONG"),
    VERY_STRONG("ARMORED / VERY STRONG")
}

data class UrlAnalysisResult(
    val url: String,
    val isSecureScheme: Boolean,
    val scheme: String,
    val host: String,
    val port: Int?,
    val isIpAddress: Boolean,
    val riskScore: Int, // 0 (Safe) to 100 (High Risk)
    val verdict: UrlVerdict,
    val findings: List<UrlFinding>
)

enum class UrlVerdict(val label: String) {
    SAFE("TRUSTED / SAFE"),
    SUSPICIOUS("SUSPICIOUS DETECTED"),
    POTENTIALLY_DANGEROUS("HIGH RISK / MALICIOUS PATTERN")
}

data class UrlFinding(
    val title: String,
    val description: String,
    val isWarning: Boolean
)

data class NetworkData(
    val isConnected: Boolean = false,
    val connectionType: String = "Disconnected",
    val localIpAddress: String = "Unavailable",
    val linkSpeedMbps: Int = -1,
    val frequencyMhz: Int = -1,
    val dnsServers: List<String> = emptyList(),
    val gateway: String = "Unavailable",
    val subnetMask: String = "Unavailable",
    val securityAssessment: String = "No active connection"
)

data class CyberLesson(
    val id: String,
    val title: String,
    val category: String,
    val summary: String,
    val keyPoints: List<String>,
    val defensiveTip: String,
    val difficulty: String = "Beginner"
)

data class SecurityTip(
    val id: String,
    val title: String,
    val category: String,
    val description: String,
    val priority: TipPriority
)

enum class TipPriority {
    CRITICAL, HIGH, RECOMMENDED
}
