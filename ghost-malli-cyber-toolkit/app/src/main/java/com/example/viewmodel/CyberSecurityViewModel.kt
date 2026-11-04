package com.example.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.SecurityEducationalData
import com.example.model.*
import com.example.util.SecurityUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CyberSecurityViewModel(application: Application) : AndroidViewModel(application) {

    private val _currentScreen = MutableStateFlow(Screen.SPLASH)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

    // 1. Password Strength State
    private val _passwordInput = MutableStateFlow("")
    val passwordInput: StateFlow<String> = _passwordInput.asStateFlow()

    private val _passwordAnalysis = MutableStateFlow(PasswordAnalysis())
    val passwordAnalysis: StateFlow<PasswordAnalysis> = _passwordAnalysis.asStateFlow()

    // 2. Password Generator State
    private val _genLength = MutableStateFlow(16)
    val genLength: StateFlow<Int> = _genLength.asStateFlow()

    private val _genUpper = MutableStateFlow(true)
    val genUpper: StateFlow<Boolean> = _genUpper.asStateFlow()

    private val _genLower = MutableStateFlow(true)
    val genLower: StateFlow<Boolean> = _genLower.asStateFlow()

    private val _genDigits = MutableStateFlow(true)
    val genDigits: StateFlow<Boolean> = _genDigits.asStateFlow()

    private val _genSymbols = MutableStateFlow(true)
    val genSymbols: StateFlow<Boolean> = _genSymbols.asStateFlow()

    private val _generatedPassword = MutableStateFlow("")
    val generatedPassword: StateFlow<String> = _generatedPassword.asStateFlow()

    // 3. URL Safety Checker State
    private val _urlInput = MutableStateFlow("")
    val urlInput: StateFlow<String> = _urlInput.asStateFlow()

    private val _urlAnalysis = MutableStateFlow<UrlAnalysisResult?>(null)
    val urlAnalysis: StateFlow<UrlAnalysisResult?> = _urlAnalysis.asStateFlow()

    // 4. File Hash (SHA-256) State
    private val _fileHashResult = MutableStateFlow<String?>(null)
    val fileHashResult: StateFlow<String?> = _fileHashResult.asStateFlow()

    private val _fileName = MutableStateFlow<String?>(null)
    val fileName: StateFlow<String?> = _fileName.asStateFlow()

    private val _isHashing = MutableStateFlow(false)
    val isHashing: StateFlow<Boolean> = _isHashing.asStateFlow()

    private val _textToHash = MutableStateFlow("")
    val textToHash: StateFlow<String> = _textToHash.asStateFlow()

    private val _textHashResult = MutableStateFlow<String?>(null)
    val textHashResult: StateFlow<String?> = _textHashResult.asStateFlow()

    // 5. Network Information State
    private val _networkData = MutableStateFlow(NetworkData())
    val networkData: StateFlow<NetworkData> = _networkData.asStateFlow()

    private val _isRefreshingNetwork = MutableStateFlow(false)
    val isRefreshingNetwork: StateFlow<Boolean> = _isRefreshingNetwork.asStateFlow()

    // 6. Cyber Academy & Lessons State
    val lessons: List<CyberLesson> = SecurityEducationalData.lessons
    val securityTips: List<SecurityTip> = SecurityEducationalData.securityTips

    private val _selectedLesson = MutableStateFlow<CyberLesson?>(null)
    val selectedLesson: StateFlow<CyberLesson?> = _selectedLesson.asStateFlow()

    init {
        generateNewPassword()
        refreshNetworkInfo()
    }

    fun navigateTo(screen: Screen) {
        _currentScreen.value = screen
    }

    // Password Analyzer Methods
    fun onPasswordChanged(newPassword: String) {
        _passwordInput.value = newPassword
        _passwordAnalysis.value = SecurityUtils.analyzePassword(newPassword)
    }

    // Password Generator Methods
    fun updateGenLength(length: Int) {
        _genLength.value = length
        generateNewPassword()
    }

    fun toggleGenUpper() {
        if (_genUpper.value && !_genLower.value && !_genDigits.value && !_genSymbols.value) return
        _genUpper.value = !_genUpper.value
        generateNewPassword()
    }

    fun toggleGenLower() {
        if (!_genUpper.value && _genLower.value && !_genDigits.value && !_genSymbols.value) return
        _genLower.value = !_genLower.value
        generateNewPassword()
    }

    fun toggleGenDigits() {
        if (!_genUpper.value && !_genLower.value && _genDigits.value && !_genSymbols.value) return
        _genDigits.value = !_genDigits.value
        generateNewPassword()
    }

    fun toggleGenSymbols() {
        if (!_genUpper.value && !_genLower.value && !_genDigits.value && _genSymbols.value) return
        _genSymbols.value = !_genSymbols.value
        generateNewPassword()
    }

    fun generateNewPassword() {
        _generatedPassword.value = SecurityUtils.generateSecurePassword(
            length = _genLength.value,
            includeUpper = _genUpper.value,
            includeLower = _genLower.value,
            includeDigits = _genDigits.value,
            includeSymbols = _genSymbols.value
        )
    }

    // URL Checker Methods
    fun onUrlInputChanged(newUrl: String) {
        _urlInput.value = newUrl
    }

    fun checkUrlSafety() {
        if (_urlInput.value.isBlank()) return
        _urlAnalysis.value = SecurityUtils.analyzeUrl(_urlInput.value)
    }

    // File / Text Hash Methods
    fun hashSelectedFile(uri: Uri, displayName: String?) {
        viewModelScope.launch {
            _isHashing.value = true
            _fileName.value = displayName ?: "Selected File"
            _fileHashResult.value = null

            val result = withContext(Dispatchers.IO) {
                try {
                    val inputStream = getApplication<Application>().contentResolver.openInputStream(uri)
                    if (inputStream != null) {
                        SecurityUtils.calculateSha256(inputStream).also {
                            inputStream.close()
                        }
                    } else {
                        "Error opening file stream"
                    }
                } catch (e: Exception) {
                    "Error computing hash: ${e.localizedMessage}"
                }
            }

            _fileHashResult.value = result
            _isHashing.value = false
        }
    }

    fun onTextToHashChanged(text: String) {
        _textToHash.value = text
        if (text.isNotEmpty()) {
            _textHashResult.value = SecurityUtils.calculateSha256ForText(text)
        } else {
            _textHashResult.value = null
        }
    }

    // Network Info Methods
    fun refreshNetworkInfo() {
        viewModelScope.launch {
            _isRefreshingNetwork.value = true
            val data = withContext(Dispatchers.IO) {
                SecurityUtils.fetchNetworkData(getApplication())
            }
            _networkData.value = data
            _isRefreshingNetwork.value = false
        }
    }

    // Academy
    fun selectLesson(lesson: CyberLesson?) {
        _selectedLesson.value = lesson
    }
}
