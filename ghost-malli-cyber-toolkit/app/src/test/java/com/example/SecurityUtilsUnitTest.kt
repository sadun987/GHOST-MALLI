package com.example

import com.example.model.PasswordLevel
import com.example.model.UrlVerdict
import com.example.util.SecurityUtils
import org.junit.Assert.*
import org.junit.Test

class SecurityUtilsUnitTest {

    @Test
    fun passwordStrength_detectsWeakPassword() {
        val analysis = SecurityUtils.analyzePassword("123456")
        assertTrue(analysis.level == PasswordLevel.VERY_WEAK || analysis.level == PasswordLevel.WEAK)
        assertTrue(analysis.score < 50)
    }

    @Test
    fun passwordStrength_detectsComplexPassword() {
        val analysis = SecurityUtils.analyzePassword("Cyber#Ghost_9921!Malli")
        assertTrue(analysis.hasUpper)
        assertTrue(analysis.hasLower)
        assertTrue(analysis.hasDigit)
        assertTrue(analysis.hasSpecial)
        assertTrue(analysis.score >= 80)
        assertTrue(analysis.entropy > 50.0)
    }

    @Test
    fun passwordGenerator_generatesExactLengthAndConstraints() {
        val length = 20
        val pass = SecurityUtils.generateSecurePassword(
            length = length,
            includeUpper = true,
            includeLower = true,
            includeDigits = true,
            includeSymbols = true
        )
        assertEquals(length, pass.length)
        assertTrue(pass.any { it.isUpperCase() })
        assertTrue(pass.any { it.isLowerCase() })
        assertTrue(pass.any { it.isDigit() })
    }

    @Test
    fun sha256_computesCorrectKnownDigest() {
        // Known SHA-256 for "hello world"
        val expected = "b94d27b9934d3e08a52e52d7da7dabfac484efe37a5380ee9088f7ace2efcde9"
        val actual = SecurityUtils.calculateSha256ForText("hello world")
        assertEquals(expected, actual)
    }

    @Test
    fun urlChecker_flagsInsecureHttpAndRawIp() {
        val analysis = SecurityUtils.analyzeUrl("http://192.168.1.1/login-verify")
        assertFalse(analysis.isSecureScheme)
        assertTrue(analysis.isIpAddress)
        assertTrue(analysis.riskScore > 50)
        assertTrue(analysis.verdict == UrlVerdict.POTENTIALLY_DANGEROUS || analysis.verdict == UrlVerdict.SUSPICIOUS)
    }

    @Test
    fun urlChecker_approvesCleanHttpsDomain() {
        val analysis = SecurityUtils.analyzeUrl("https://example.com")
        assertTrue(analysis.isSecureScheme)
        assertFalse(analysis.isIpAddress)
        assertTrue(analysis.riskScore < 30)
        assertEquals(UrlVerdict.SAFE, analysis.verdict)
    }
}
