package com.example.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.net.wifi.WifiManager
import com.example.model.*
import java.io.InputStream
import java.net.Inet4Address
import java.net.InetAddress
import java.net.NetworkInterface
import java.security.MessageDigest
import java.security.SecureRandom
import kotlin.math.log2
import kotlin.math.pow

object SecurityUtils {

    // --- 1. Password Strength Analyzer ---
    fun analyzePassword(password: String): PasswordAnalysis {
        if (password.isEmpty()) {
            return PasswordAnalysis()
        }

        var poolSize = 0
        val hasLower = password.any { it.isLowerCase() }
        val hasUpper = password.any { it.isUpperCase() }
        val hasDigit = password.any { it.isDigit() }
        val hasSpecial = password.any { !it.isLetterOrDigit() }

        if (hasLower) poolSize += 26
        if (hasUpper) poolSize += 26
        if (hasDigit) poolSize += 10
        if (hasSpecial) poolSize += 33

        val length = password.length
        val entropy = if (poolSize > 0) length * log2(poolSize.toDouble()) else 0.0

        // Score based on length, entropy, and diversity
        var score = 0
        if (length >= 8) score += 20
        if (length >= 12) score += 15
        if (length >= 16) score += 15
        if (hasLower) score += 10
        if (hasUpper) score += 10
        if (hasDigit) score += 15
        if (hasSpecial) score += 15

        // Penalize repeating characters or obvious sequences
        if (password.lowercase().contains("password") ||
            password.contains("1234") ||
            password.contains("qwerty") ||
            password.contains("admin")
        ) {
            score = (score - 40).coerceAtLeast(10)
        }

        score = score.coerceIn(5, 100)

        val level = when {
            score < 30 -> PasswordLevel.VERY_WEAK
            score < 55 -> PasswordLevel.WEAK
            score < 75 -> PasswordLevel.FAIR
            score < 90 -> PasswordLevel.STRONG
            else -> PasswordLevel.VERY_STRONG
        }

        val feedback = mutableListOf<String>()
        if (length < 12) feedback.add("Length is under 12 characters; aim for 16+ for brute-force resistance.")
        if (!hasUpper) feedback.add("Add uppercase characters (A-Z) to widen the search space.")
        if (!hasLower) feedback.add("Add lowercase letters (a-z).")
        if (!hasDigit) feedback.add("Include numeric characters (0-9).")
        if (!hasSpecial) feedback.add("Add cryptographic symbols (!@#$%^&*).")
        if (password.lowercase().contains("admin") || password.contains("123")) {
            feedback.add("Detected common dictionary pattern or sequence!")
        }
        if (feedback.isEmpty()) {
            feedback.add("Excellent entropy! Meets high security defensive standards.")
        }

        val crackTime = calculateCrackTime(entropy)

        return PasswordAnalysis(
            score = score,
            level = level,
            entropy = (entropy * 10).toInt() / 10.0,
            length = length,
            hasUpper = hasUpper,
            hasLower = hasLower,
            hasDigit = hasDigit,
            hasSpecial = hasSpecial,
            feedback = feedback,
            crackTimeEstimate = crackTime
        )
    }

    private fun calculateCrackTime(entropy: Double): String {
        // Assuming an offline GPU cracking cluster testing 100 Billion (10^11) guesses/sec
        val combinations = 2.0.pow(entropy)
        val seconds = combinations / 1e11
        return when {
            seconds < 0.001 -> "Instant (under a millisecond)"
            seconds < 1 -> "Instant (< 1 second)"
            seconds < 60 -> "${seconds.toInt()} seconds"
            seconds < 3600 -> "${(seconds / 60).toInt()} minutes"
            seconds < 86400 -> "${(seconds / 3600).toInt()} hours"
            seconds < 31536000 -> "${(seconds / 86400).toInt()} days"
            seconds < 3153600000.0 -> "${(seconds / 31536000).toInt()} years"
            seconds < 315360000000.0 -> "${(seconds / 3153600000.0).toInt()} centuries"
            else -> "Millions of years (Quantum resistant standard)"
        }
    }

    // --- 2. Cryptographic Password Generator ---
    fun generateSecurePassword(
        length: Int = 16,
        includeUpper: Boolean = true,
        includeLower: Boolean = true,
        includeDigits: Boolean = true,
        includeSymbols: Boolean = true
    ): String {
        val upperChars = "ABCDEFGHJKLMNPQRSTUVWXYZ" // Exclude confusing O, I
        val lowerChars = "abcdefghijkmnopqrstuvwxyz" // Exclude l
        val digitChars = "23456789" // Exclude 0, 1
        val symbolChars = "!@#$%^&*()-_=+[]{}<>?~"

        val charPool = StringBuilder()
        val guaranteedChars = mutableListOf<Char>()
        val random = SecureRandom()

        if (includeUpper) {
            charPool.append(upperChars)
            guaranteedChars.add(upperChars[random.nextInt(upperChars.length)])
        }
        if (includeLower) {
            charPool.append(lowerChars)
            guaranteedChars.add(lowerChars[random.nextInt(lowerChars.length)])
        }
        if (includeDigits) {
            charPool.append(digitChars)
            guaranteedChars.add(digitChars[random.nextInt(digitChars.length)])
        }
        if (includeSymbols) {
            charPool.append(symbolChars)
            guaranteedChars.add(symbolChars[random.nextInt(symbolChars.length)])
        }

        if (charPool.isEmpty()) {
            return ""
        }

        val passwordChars = ArrayList<Char>(guaranteedChars)
        val remainingLength = (length - guaranteedChars.size).coerceAtLeast(0)

        for (i in 0 until remainingLength) {
            val idx = random.nextInt(charPool.length)
            passwordChars.add(charPool[idx])
        }

        // Shuffle securely
        for (i in passwordChars.indices.reversed()) {
            val j = random.nextInt(i + 1)
            val tmp = passwordChars[i]
            passwordChars[i] = passwordChars[j]
            passwordChars[j] = tmp
        }

        return passwordChars.joinToString("")
    }

    // --- 3. URL Safety Analyzer ---
    fun analyzeUrl(rawUrl: String): UrlAnalysisResult {
        var clean = rawUrl.trim()
        if (!clean.startsWith("http://") && !clean.startsWith("https://")) {
            clean = "https://$clean"
        }

        val findings = mutableListOf<UrlFinding>()
        var riskScore = 0

        val uriParsed = try {
            java.net.URI(clean)
        } catch (e: Exception) {
            null
        }

        val scheme = uriParsed?.scheme ?: if (clean.startsWith("https://", ignoreCase = true)) "https" else "http"
        val host = uriParsed?.host ?: clean.removePrefix("https://").removePrefix("http://").substringBefore("/").substringBefore(":")
        val port = if (uriParsed?.port != null && uriParsed.port != -1) uriParsed.port else null

        // Scheme Check
        val isHttps = scheme.equals("https", ignoreCase = true)
        if (!isHttps) {
            riskScore += 35
            findings.add(
                UrlFinding(
                    "Insecure HTTP Protocol",
                    "Traffic is unencrypted and vulnerable to Man-In-The-Middle (MITM) eavesdropping.",
                    true
                )
            )
        } else {
            findings.add(
                UrlFinding(
                    "Encrypted Protocol (HTTPS)",
                    "Data in transit is protected via TLS/SSL encryption.",
                    false
                )
            )
        }

        // Check if Host is raw IP address (common in phishing & malware command centers)
        val isIp = host.matches(Regex("""^(\d{1,3}\.){3}\d{1,3}$"""))
        if (isIp) {
            riskScore += 40
            findings.add(
                UrlFinding(
                    "Direct IP Address in URL",
                    "Using a raw IP instead of a registered domain name is a common phishing and C2 server tactic.",
                    true
                )
            )
        }

        // Check suspicious TLDs
        val suspiciousTlds = listOf(".zip", ".mov", ".top", ".xyz", ".country", ".kim", ".work")
        if (suspiciousTlds.any { host.endsWith(it, ignoreCase = true) }) {
            riskScore += 25
            findings.add(
                UrlFinding(
                    "High-Risk Top-Level Domain",
                    "The domain uses a TLD frequently leveraged for throwaway phishing campaigns.",
                    true
                )
            )
        }

        // Check for homograph / excessive subdomains
        val subdomains = host.split(".")
        if (subdomains.size > 4) {
            riskScore += 20
            findings.add(
                UrlFinding(
                    "Deep Subdomain Nesting",
                    "Overly complex domain hierarchy often masks phishing destinations (e.g. login.paypal.com.evil.xyz).",
                    true
                )
            )
        }

        // Check for suspicious keywords in hostname
        val suspiciousKeywords = listOf("login-verify", "bank-update", "account-alert", "free-crypto", "claim-reward", "metamask-recover")
        if (suspiciousKeywords.any { clean.contains(it, ignoreCase = true) }) {
            riskScore += 30
            findings.add(
                UrlFinding(
                    "Social Engineering Keywords Detected",
                    "Targeted keywords like 'login-verify' or 'claim-reward' strongly indicate credential theft.",
                    true
                )
            )
        }

        // Check for non-standard ports
        if (port != null && port != 80 && port != 443 && port != 8080) {
            riskScore += 15
            findings.add(
                UrlFinding(
                    "Non-Standard Port ($port)",
                    "Web traffic on uncommon ports may evade standard enterprise boundary firewalls.",
                    true
                )
            )
        }

        if (findings.none { it.isWarning }) {
            findings.add(
                UrlFinding(
                    "Clean Structure",
                    "Standard domain structure, secure scheme, and no obvious evasion patterns detected.",
                    false
                )
            )
        }

        riskScore = riskScore.coerceIn(0, 100)
        val verdict = when {
            riskScore < 25 -> UrlVerdict.SAFE
            riskScore < 60 -> UrlVerdict.SUSPICIOUS
            else -> UrlVerdict.POTENTIALLY_DANGEROUS
        }

        return UrlAnalysisResult(
            url = clean,
            isSecureScheme = isHttps,
            scheme = scheme,
            host = host,
            port = port,
            isIpAddress = isIp,
            riskScore = riskScore,
            verdict = verdict,
            findings = findings
        )
    }

    // --- 4. SHA-256 Checksum Calculator ---
    fun calculateSha256(inputStream: InputStream): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val buffer = ByteArray(8192)
        var bytesRead: Int
        while (inputStream.read(buffer).also { bytesRead = it } != -1) {
            digest.update(buffer, 0, bytesRead)
        }
        val hashBytes = digest.digest()
        return hashBytes.joinToString("") { "%02x".format(it) }
    }

    fun calculateSha256ForText(text: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hashBytes = digest.digest(text.toByteArray(Charsets.UTF_8))
        return hashBytes.joinToString("") { "%02x".format(it) }
    }

    // --- 5. Network Information Fetcher ---
    fun fetchNetworkData(context: Context): NetworkData {
        try {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork = connectivityManager.activeNetwork ?: return NetworkData()
            val caps = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return NetworkData()

            val isConnected = caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            val isWifi = caps.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
            val isCellular = caps.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
            val isVpn = caps.hasTransport(NetworkCapabilities.TRANSPORT_VPN)

            val typeStr = when {
                isVpn -> "VPN / Encrypted Tunnel"
                isWifi -> "Wi-Fi (802.11)"
                isCellular -> "Cellular (Mobile Data)"
                else -> "Active Network"
            }

            var localIp = "127.0.0.1"
            try {
                val interfaces = NetworkInterface.getNetworkInterfaces()
                while (interfaces.hasMoreElements()) {
                    val networkInterface = interfaces.nextElement()
                    val addresses = networkInterface.inetAddresses
                    while (addresses.hasMoreElements()) {
                        val addr = addresses.nextElement()
                        if (!addr.isLoopbackAddress && addr is Inet4Address) {
                            localIp = addr.hostAddress ?: localIp
                            break
                        }
                    }
                }
            } catch (e: Exception) {
                localIp = "Detection Restricted"
            }

            var linkSpeed = -1
            var freq = -1
            if (isWifi) {
                try {
                    val wifiManager =
                        context.applicationContext.getSystemService(Context.WIFI_SERVICE) as? WifiManager
                    wifiManager?.connectionInfo?.let { info ->
                        linkSpeed = info.linkSpeed
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                            freq = info.frequency
                        }
                    }
                } catch (e: Exception) {
                    // Safe fallback
                }
            }

            val linkProperties = connectivityManager.getLinkProperties(activeNetwork)
            val dnsList = linkProperties?.dnsServers?.mapNotNull { it.hostAddress } ?: emptyList()
            val gateway = linkProperties?.routes?.firstOrNull { it.isDefaultRoute }?.gateway?.hostAddress ?: "Default"

            val assessment = when {
                isVpn -> "Encrypted tunnel active. Network packets protected from local ISP/LAN sniffing."
                isWifi && localIp.startsWith("192.168.") -> "Private Wi-Fi Subnet. Ensure WPA3/WPA2-AES is enforced on access point."
                isCellular -> "Cellular Carrier Network. Direct carrier NAT."
                else -> "Standard network connection."
            }

            return NetworkData(
                isConnected = isConnected,
                connectionType = typeStr,
                localIpAddress = localIp,
                linkSpeedMbps = linkSpeed,
                frequencyMhz = freq,
                dnsServers = dnsList,
                gateway = gateway,
                subnetMask = "255.255.255.0",
                securityAssessment = assessment
            )
        } catch (e: Exception) {
            return NetworkData(
                isConnected = false,
                connectionType = "Unavailable",
                localIpAddress = "N/A",
                securityAssessment = "Unable to read interface details: ${e.localizedMessage}"
            )
        }
    }
}
