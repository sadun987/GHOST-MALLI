package com.example.data

import com.example.model.CyberLesson
import com.example.model.SecurityTip
import com.example.model.TipPriority

object SecurityEducationalData {

    val lessons: List<CyberLesson> = listOf(
        CyberLesson(
            id = "lesson_1",
            title = "Zero Trust Architecture",
            category = "Defense Fundamentals",
            summary = "The paradigm shift from 'trust but verify' to 'never trust, always verify'.",
            keyPoints = listOf(
                "Perimeter defense alone is dead; assume the adversary is already inside the network.",
                "Continuous authentication and explicit authorization for every transaction.",
                "Microsegmentation prevents lateral movement across internal subnets.",
                "Least-privilege access: Grant only the minimal access needed for a role."
            ),
            defensiveTip = "Enforce Multi-Factor Authentication (MFA) with FIDO2 hardware keys and treat every internal service as internet-facing.",
            difficulty = "Beginner"
        ),
        CyberLesson(
            id = "lesson_2",
            title = "Phishing & Social Engineering",
            category = "Threat Vector",
            summary = "How human psychological vulnerabilities are exploited to breach complex cryptographic systems.",
            keyPoints = listOf(
                "Spear phishing targets specific individuals with tailored reconnaissance details.",
                "Lookalike domains (IDN homograph attacks) mimic authentic banking or auth URLs.",
                "Urgency and authority triggers bypass rational human verification routines.",
                "Watering hole attacks compromise trusted third-party websites."
            ),
            defensiveTip = "Never approve spontaneous 2FA prompts (push-bombing). Always inspect actual URL hostnames rather than hyperlinked anchor text.",
            difficulty = "Beginner"
        ),
        CyberLesson(
            id = "lesson_3",
            title = "Cryptographic Hashes & Integrity",
            category = "Applied Cryptography",
            summary = "One-way mathematical functions guaranteeing immutability and tamper detection.",
            keyPoints = listOf(
                "A hash function converts arbitrary data into a fixed-length deterministic digest.",
                "Avalanche effect: Changing even one bit alters >50% of the output hash.",
                "Pre-image resistance: Computationally impossible to reverse the digest back into input.",
                "Collision resistance: No two unique inputs should ever yield the same hash digest."
            ),
            defensiveTip = "Always verify SHA-256 checksums when downloading executable installers or firmwares before execution.",
            difficulty = "Intermediate"
        ),
        CyberLesson(
            id = "lesson_4",
            title = "Man-in-the-Middle (MITM) & TLS",
            category = "Network Security",
            summary = "Interception and alteration of network communications between two trusting endpoints.",
            keyPoints = listOf(
                "Unencrypted HTTP allows any upstream proxy or rogue Wi-Fi AP to read and inject packets.",
                "Rogue access points (Evil Twins) clone SSID names to trick client auto-connections.",
                "TLS 1.3 provides forward secrecy, meaning compromised master keys cannot decrypt past sessions.",
                "Public Key Pinning (HPKP) & Certificate Transparency (CT) prevent fraudulent CA certificates."
            ),
            defensiveTip = "Avoid open public Wi-Fi without an established WireGuard/VPN tunnel, and look for HTTPS indicators.",
            difficulty = "Intermediate"
        ),
        CyberLesson(
            id = "lesson_5",
            title = "SQL Injection & Input Sanitization",
            category = "App Security (OWASP Top 10)",
            summary = "Manipulating backend database queries by injecting unsanitized user inputs.",
            keyPoints = listOf(
                "Occurs when untrusted data is concatenated directly into SQL command strings.",
                "Can lead to complete database dump, auth bypass, and arbitrary data destruction.",
                "Parameterized queries (Prepared Statements) separate executable code from user data.",
                "Defense-in-depth: Least privilege database user accounts reduce breach impact."
            ),
            defensiveTip = "Always use Object-Relational Mappers (Room, Hibernate) or strict parameterized queries instead of string concatenation.",
            difficulty = "Advanced"
        ),
        CyberLesson(
            id = "lesson_6",
            title = "Ransomware Defense & Incident Response",
            category = "Malware Defense",
            summary = "Understanding malicious encryption campaigns and establishing resilient survivability.",
            keyPoints = listOf(
                "Modern ransomware exfiltrates sensitive IP prior to encryption (Double Extortion).",
                "Initial access usually stems from exposed RDP ports or stolen phishing credentials.",
                "The 3-2-1 backup rule: 3 copies, 2 media types, 1 offsite immutable air-gapped copy.",
                "System recovery drills are crucial: An untested backup is not a backup."
            ),
            defensiveTip = "Implement immutable object storage for daily backups and maintain write-once-read-many (WORM) storage.",
            difficulty = "Advanced"
        )
    )

    val securityTips: List<SecurityTip> = listOf(
        SecurityTip(
            id = "tip_1",
            title = "Hardware Multi-Factor Authentication",
            category = "Authentication",
            description = "Upgrade from SMS verification to authenticator apps (TOTP) or FIDO2 hardware security keys (YubiKey) to eliminate SIM-swapping risks.",
            priority = TipPriority.CRITICAL
        ),
        SecurityTip(
            id = "tip_2",
            title = "Enforce Unique Passwords via Vault",
            category = "Identity",
            description = "Never reuse master passwords. If one vendor suffers a credential breach, attackers replay combinations against all major online portals.",
            priority = TipPriority.CRITICAL
        ),
        SecurityTip(
            id = "tip_3",
            title = "DNS-Level Threat Filtering",
            category = "Network",
            description = "Configure Private DNS on Android to an encrypted provider (e.g. NextDNS or Cloudflare 1.1.1.1) to block known telemetry and malware hostnames at packet level.",
            priority = TipPriority.HIGH
        ),
        SecurityTip(
            id = "tip_4",
            title = "Patching & Zero-Day Hygiene",
            category = "System",
            description = "Apply monthly Android Security Updates immediately. Most active in-the-wild exploits target publicly disclosed CVEs patched in preceding updates.",
            priority = TipPriority.HIGH
        ),
        SecurityTip(
            id = "tip_5",
            title = "Disable Automatic Wi-Fi / Bluetooth Pairing",
            category = "Device",
            description = "Turn off auto-connect to open Wi-Fi networks in Android Wi-Fi preferences to defend against pineapple and evil-twin traps in public transit.",
            priority = TipPriority.RECOMMENDED
        )
    )
}
