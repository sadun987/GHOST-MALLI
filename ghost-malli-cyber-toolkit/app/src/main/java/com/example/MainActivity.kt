package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.model.Screen
import com.example.ui.screens.*
import com.example.ui.theme.GhostMalliTheme
import com.example.viewmodel.CyberSecurityViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: CyberSecurityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            GhostMalliTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CyberAppContent(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun CyberAppContent(viewModel: CyberSecurityViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val passwordInput by viewModel.passwordInput.collectAsStateWithLifecycle()
    val passwordAnalysis by viewModel.passwordAnalysis.collectAsStateWithLifecycle()

    val genLength by viewModel.genLength.collectAsStateWithLifecycle()
    val genUpper by viewModel.genUpper.collectAsStateWithLifecycle()
    val genLower by viewModel.genLower.collectAsStateWithLifecycle()
    val genDigits by viewModel.genDigits.collectAsStateWithLifecycle()
    val genSymbols by viewModel.genSymbols.collectAsStateWithLifecycle()
    val generatedPassword by viewModel.generatedPassword.collectAsStateWithLifecycle()

    val urlInput by viewModel.urlInput.collectAsStateWithLifecycle()
    val urlAnalysis by viewModel.urlAnalysis.collectAsStateWithLifecycle()

    val fileHash by viewModel.fileHashResult.collectAsStateWithLifecycle()
    val fileName by viewModel.fileName.collectAsStateWithLifecycle()
    val isHashing by viewModel.isHashing.collectAsStateWithLifecycle()
    val textToHash by viewModel.textToHash.collectAsStateWithLifecycle()
    val textHash by viewModel.textHashResult.collectAsStateWithLifecycle()

    val networkData by viewModel.networkData.collectAsStateWithLifecycle()
    val isRefreshingNetwork by viewModel.isRefreshingNetwork.collectAsStateWithLifecycle()

    val selectedLesson by viewModel.selectedLesson.collectAsStateWithLifecycle()

    // BackHandler: Handle subscreen navigation back to Dashboard
    if (currentScreen != Screen.SPLASH && currentScreen != Screen.DASHBOARD) {
        BackHandler {
            viewModel.navigateTo(Screen.DASHBOARD)
        }
    }

    AnimatedContent(
        targetState = currentScreen,
        transitionSpec = {
            if (targetState == Screen.SPLASH || initialState == Screen.SPLASH) {
                fadeIn() togetherWith fadeOut()
            } else {
                (slideInHorizontally { width -> width / 3 } + fadeIn()) togetherWith
                        (slideOutHorizontally { width -> -width / 3 } + fadeOut())
            }
        },
        label = "screenTransition"
    ) { screen ->
        when (screen) {
            Screen.SPLASH -> {
                SplashScreen(
                    onEnterApp = { viewModel.navigateTo(Screen.DASHBOARD) }
                )
            }
            Screen.DASHBOARD -> {
                DashboardScreen(
                    onNavigate = { target -> viewModel.navigateTo(target) }
                )
            }
            Screen.PASSWORD_CHECKER -> {
                PasswordCheckerScreen(
                    passwordInput = passwordInput,
                    analysis = passwordAnalysis,
                    onPasswordChanged = { viewModel.onPasswordChanged(it) },
                    onBack = { viewModel.navigateTo(Screen.DASHBOARD) }
                )
            }
            Screen.PASSWORD_GENERATOR -> {
                PasswordGeneratorScreen(
                    length = genLength,
                    includeUpper = genUpper,
                    includeLower = genLower,
                    includeDigits = genDigits,
                    includeSymbols = genSymbols,
                    generatedPassword = generatedPassword,
                    onLengthChanged = { viewModel.updateGenLength(it) },
                    onToggleUpper = { viewModel.toggleGenUpper() },
                    onToggleLower = { viewModel.toggleGenLower() },
                    onToggleDigits = { viewModel.toggleGenDigits() },
                    onToggleSymbols = { viewModel.toggleGenSymbols() },
                    onRegenerate = { viewModel.generateNewPassword() },
                    onBack = { viewModel.navigateTo(Screen.DASHBOARD) }
                )
            }
            Screen.URL_CHECKER -> {
                UrlCheckerScreen(
                    urlInput = urlInput,
                    urlAnalysis = urlAnalysis,
                    onUrlInputChanged = { viewModel.onUrlInputChanged(it) },
                    onCheckUrl = { viewModel.checkUrlSafety() },
                    onBack = { viewModel.navigateTo(Screen.DASHBOARD) }
                )
            }
            Screen.FILE_HASH -> {
                FileHashScreen(
                    fileHash = fileHash,
                    fileName = fileName,
                    isHashing = isHashing,
                    textToHash = textToHash,
                    textHash = textHash,
                    onHashSelectedFile = { uri, name -> viewModel.hashSelectedFile(uri, name) },
                    onTextToHashChanged = { viewModel.onTextToHashChanged(it) },
                    onBack = { viewModel.navigateTo(Screen.DASHBOARD) }
                )
            }
            Screen.NETWORK_INFO -> {
                NetworkInfoScreen(
                    networkData = networkData,
                    isRefreshing = isRefreshingNetwork,
                    onRefresh = { viewModel.refreshNetworkInfo() },
                    onBack = { viewModel.navigateTo(Screen.DASHBOARD) }
                )
            }
            Screen.EDUCATION -> {
                EducationScreen(
                    lessons = viewModel.lessons,
                    selectedLesson = selectedLesson,
                    onSelectLesson = { viewModel.selectLesson(it) },
                    onBack = { viewModel.navigateTo(Screen.DASHBOARD) }
                )
            }
            Screen.SETTINGS -> {
                SettingsScreen(
                    securityTips = viewModel.securityTips,
                    onBack = { viewModel.navigateTo(Screen.DASHBOARD) }
                )
            }
        }
    }
}
