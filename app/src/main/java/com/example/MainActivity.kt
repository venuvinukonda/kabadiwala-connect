package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.model.UserRole
import com.example.service.localization.LocalizationManager
import com.example.ui.components.*
import com.example.ui.navigation.AppScreen
import com.example.ui.screens.admin.*
import com.example.ui.screens.auth.*
import com.example.ui.screens.collector.*
import com.example.ui.screens.common.*
import com.example.ui.screens.recycler.*
import com.example.ui.theme.KabadiwalaConnectTheme
import com.example.ui.viewmodel.KabadiwalaViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LocalizationManager.initialize(applicationContext)
        enableEdgeToEdge()
        setContent {
            KabadiwalaConnectTheme {
                KabadiwalaApp()
            }
        }
    }
}

@Composable
fun KabadiwalaApp(viewModel: KabadiwalaViewModel = viewModel()) {
    val userMessage by viewModel.userMessage.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    // Handle user messages in Snackbar
    LaunchedEffect(userMessage) {
        userMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearUserMessage()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background,
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        NavigationHost(
            viewModel = viewModel,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

/**
 * NavigationHost that handles role-based routing to show the Collector, Recycler,
 * or Admin dashboards based on authentication state (currentUser).
 */
@Composable
fun NavigationHost(
    viewModel: KabadiwalaViewModel,
    modifier: Modifier = Modifier
) {
    val currentUser by viewModel.currentUser.collectAsState()
    val currentLang by viewModel.currentLanguage.collectAsState()
    var showLanguageDialog by remember { mutableStateOf(false) }

    // Navigation stack for hierarchical back navigation
    var screenStack by remember { mutableStateOf(listOf<AppScreen>(AppScreen.RoleSelection)) }
    val currentScreen = screenStack.lastOrNull() ?: AppScreen.RoleSelection

    fun navigateTo(screen: AppScreen) {
        viewModel.stopAudio()
        screenStack = screenStack + screen
    }

    fun navigateBack() {
        viewModel.stopAudio()
        if (screenStack.size > 1) {
            screenStack = screenStack.dropLast(1)
        }
    }

    fun navigateRoot(screen: AppScreen) {
        viewModel.stopAudio()
        screenStack = listOf(screen)
    }

    // Role-based routing based on authentication state (currentUser)
    LaunchedEffect(currentUser) {
        when (val user = currentUser) {
            null -> {
                // Unauthenticated state: If on a protected role-specific screen, route to RoleSelection
                val isAuthScreen = currentScreen is AppScreen.RoleSelection ||
                        currentScreen is AppScreen.Login ||
                        currentScreen is AppScreen.CollectorRegister ||
                        currentScreen is AppScreen.RecyclerRegister

                if (!isAuthScreen) {
                    navigateRoot(AppScreen.RoleSelection)
                }
            }
            else -> {
                // Authenticated state: Route to the respective role dashboard
                val targetRoleDashboard = when (user.role.uppercase()) {
                    "COLLECTOR" -> AppScreen.CollectorHome
                    "RECYCLER" -> AppScreen.RecyclerHome
                    "ADMIN" -> AppScreen.AdminHome
                    else -> AppScreen.CollectorHome
                }

                // If currently on an auth screen or unauthenticated screen, route immediately to role dashboard
                val isAuthScreen = currentScreen is AppScreen.RoleSelection ||
                        currentScreen is AppScreen.Login ||
                        currentScreen is AppScreen.CollectorRegister ||
                        currentScreen is AppScreen.RecyclerRegister

                if (isAuthScreen) {
                    navigateRoot(targetRoleDashboard)
                }
            }
        }
    }

    // Handle system back button with BackHandler
    BackHandler(enabled = screenStack.size > 1) {
        navigateBack()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        when (val screen = currentScreen) {
            is AppScreen.RoleSelection -> {
                RoleSelectionScreen(
                    viewModel = viewModel,
                    onRoleSelected = { role ->
                        navigateTo(AppScreen.Login(role))
                    },
                    onLanguageClick = { showLanguageDialog = true }
                )
            }

            is AppScreen.Login -> {
                LoginScreen(
                    role = screen.selectedRole,
                    viewModel = viewModel,
                    onLoginSuccess = {
                        val target = when (screen.selectedRole) {
                            UserRole.COLLECTOR -> AppScreen.CollectorHome
                            UserRole.RECYCLER -> AppScreen.RecyclerHome
                            UserRole.ADMIN -> AppScreen.AdminHome
                        }
                        navigateRoot(target)
                    },
                    onNavigateToRegister = {
                        if (screen.selectedRole == UserRole.COLLECTOR) {
                            navigateTo(AppScreen.CollectorRegister)
                        } else {
                            navigateTo(AppScreen.RecyclerRegister)
                        }
                    },
                    onBack = { navigateBack() },
                    onLanguageClick = { showLanguageDialog = true }
                )
            }

                is AppScreen.CollectorRegister -> {
                    CollectorRegisterScreen(
                        viewModel = viewModel,
                        onRegistered = { navigateRoot(AppScreen.RoleSelection) },
                        onBack = { navigateBack() },
                        onLanguageClick = { showLanguageDialog = true }
                    )
                }

                is AppScreen.RecyclerRegister -> {
                    RecyclerRegisterScreen(
                        viewModel = viewModel,
                        onRegistered = { navigateRoot(AppScreen.RoleSelection) },
                        onBack = { navigateBack() },
                        onLanguageClick = { showLanguageDialog = true }
                    )
                }

                is AppScreen.CollectorHome -> {
                    CollectorDashboardScreen(
                        viewModel = viewModel,
                        onCollectClick = { navigateTo(AppScreen.CollectEWasteWizard) },
                        onMyLotsClick = { navigateTo(AppScreen.MyLotsList) },
                        onPickupsClick = { navigateTo(AppScreen.CollectorPickups) },
                        onPickupsMapClick = { navigateTo(AppScreen.CollectorPickupsMap) },
                        onEarningsClick = { navigateTo(AppScreen.CollectorEarnings) },
                        onHistoryClick = { navigateTo(AppScreen.TransactionHistory) },
                        onPriceHistoryClick = { navigateTo(AppScreen.PriceHistory) },
                        onSafetyClick = { navigateTo(AppScreen.SafetyInstructions) },
                        onLanguageClick = { showLanguageDialog = true },
                        onProfileClick = { navigateTo(AppScreen.ProfileView) },
                        onLogoutClick = {
                            viewModel.logout { navigateRoot(AppScreen.RoleSelection) }
                        }
                    )
                }

                is AppScreen.CollectEWasteWizard -> {
                    CollectEWasteWizardScreen(
                        viewModel = viewModel,
                        onBack = { navigateBack() },
                        onFinish = {
                            navigateBack()
                            navigateTo(AppScreen.CollectorPickups)
                        },
                        onLanguageClick = { showLanguageDialog = true }
                    )
                }

                is AppScreen.MyLotsList -> {
                    MyLotsScreen(
                        viewModel = viewModel,
                        onBack = { navigateBack() },
                        onLanguageClick = { showLanguageDialog = true }
                    )
                }

                is AppScreen.CollectorPickups -> {
                    CollectorPickupsScreen(
                        viewModel = viewModel,
                        onStartHandover = { pickupId ->
                            navigateTo(AppScreen.HandoverScreen(pickupId))
                        },
                        onViewMapClick = { navigateTo(AppScreen.CollectorPickupsMap) },
                        onBack = { navigateBack() },
                        onLanguageClick = { showLanguageDialog = true }
                    )
                }

                is AppScreen.CollectorPickupsMap -> {
                    CollectorMapScreen(
                        viewModel = viewModel,
                        onBack = { navigateBack() },
                        onStartHandover = { pickupId ->
                            navigateTo(AppScreen.HandoverScreen(pickupId))
                        },
                        onLanguageClick = { showLanguageDialog = true }
                    )
                }

                is AppScreen.CollectorEarnings -> {
                    CollectorEarningsScreen(
                        viewModel = viewModel,
                        onBack = { navigateBack() },
                        onLanguageClick = { showLanguageDialog = true }
                    )
                }

                is AppScreen.TransactionHistory -> {
                    TransactionHistoryScreen(
                        viewModel = viewModel,
                        onBack = { navigateBack() },
                        onLanguageClick = { showLanguageDialog = true }
                    )
                }

                is AppScreen.PriceHistory -> {
                    PriceHistoryScreen(
                        viewModel = viewModel,
                        onBack = { navigateBack() },
                        onLanguageClick = { showLanguageDialog = true }
                    )
                }

                is AppScreen.SafetyInstructions -> {
                    SafetyInstructionsScreen(
                        viewModel = viewModel,
                        onBack = { navigateBack() },
                        onLanguageClick = { showLanguageDialog = true }
                    )
                }

                is AppScreen.RecyclerHome -> {
                    RecyclerDashboardScreen(
                        viewModel = viewModel,
                        onLotRequestsClick = { navigateTo(AppScreen.RecyclerLotRequests) },
                        onRatesConfigClick = { navigateTo(AppScreen.RecyclerRatesConfig) },
                        onCertificateClick = { navigateTo(AppScreen.RecyclerCertificate) },
                        onTransactionsClick = { navigateTo(AppScreen.TransactionHistory) },
                        onProfileClick = { navigateTo(AppScreen.ProfileView) },
                        onLanguageClick = { showLanguageDialog = true },
                        onLogoutClick = {
                            viewModel.logout { navigateRoot(AppScreen.RoleSelection) }
                        }
                    )
                }

                is AppScreen.RecyclerLotRequests -> {
                    RecyclerLotRequestsScreen(
                        viewModel = viewModel,
                        onBack = { navigateBack() },
                        onLanguageClick = { showLanguageDialog = true }
                    )
                }

                is AppScreen.RecyclerRatesConfig -> {
                    RecyclerRatesConfigScreen(
                        viewModel = viewModel,
                        onBack = { navigateBack() },
                        onLanguageClick = { showLanguageDialog = true }
                    )
                }

                is AppScreen.RecyclerCertificate -> {
                    RecyclerCertificateScreen(
                        viewModel = viewModel,
                        onBack = { navigateBack() },
                        onLanguageClick = { showLanguageDialog = true }
                    )
                }

                is AppScreen.AdminHome -> {
                    AdminDashboardScreen(
                        viewModel = viewModel,
                        onUserApprovalsClick = { navigateTo(AppScreen.AdminUserApprovals) },
                        onTransactionsClick = { navigateTo(AppScreen.AdminTransactions) },
                        onAnalyticsClick = { navigateTo(AppScreen.AdminAnalytics) },
                        onLanguageClick = { showLanguageDialog = true },
                        onLogoutClick = {
                            viewModel.logout { navigateRoot(AppScreen.RoleSelection) }
                        }
                    )
                }

                is AppScreen.AdminUserApprovals -> {
                    AdminUserApprovalsScreen(
                        viewModel = viewModel,
                        onBack = { navigateBack() },
                        onLanguageClick = { showLanguageDialog = true }
                    )
                }

                is AppScreen.AdminTransactions -> {
                    AdminTransactionsScreen(
                        viewModel = viewModel,
                        onBack = { navigateBack() },
                        onLanguageClick = { showLanguageDialog = true }
                    )
                }

                is AppScreen.AdminAnalytics -> {
                    AdminAnalyticsScreen(
                        viewModel = viewModel,
                        onBack = { navigateBack() },
                        onLanguageClick = { showLanguageDialog = true }
                    )
                }

                is AppScreen.HandoverScreen -> {
                    DigitalHandoverScreen(
                        pickupRequestId = screen.pickupRequestId,
                        viewModel = viewModel,
                        onBack = { navigateBack() },
                        onComplete = { _ -> },
                        onLanguageClick = { showLanguageDialog = true }
                    )
                }

                is AppScreen.ProfileView -> {
                    UserProfileScreen(
                        viewModel = viewModel,
                        onBack = { navigateBack() },
                        onLogout = {
                            viewModel.logout { navigateRoot(AppScreen.RoleSelection) }
                        },
                        onLanguageClick = { showLanguageDialog = true }
                    )
                }
            }

            val isSpeaking by viewModel.isSpeaking.collectAsState()
            val isPaused by viewModel.isPaused.collectAsState()
            val currentSpokenText by viewModel.currentSpokenText.collectAsState()
            val speechRate by viewModel.speechRate.collectAsState()
            val voiceWarning by viewModel.voiceUnavailableMessage.collectAsState()

            val isLoginOrAuthScreen = currentScreen is AppScreen.RoleSelection ||
                    currentScreen is AppScreen.Login ||
                    currentScreen is AppScreen.CollectorRegister ||
                    currentScreen is AppScreen.RecyclerRegister

            if (showLanguageDialog) {
                LanguageDialog(
                    currentLang = currentLang,
                    onSelectLanguage = { lang ->
                        viewModel.setLanguage(lang, speakGreeting = !isLoginOrAuthScreen)
                        showLanguageDialog = false
                    },
                    onDismiss = { showLanguageDialog = false }
                )
            }

            if ((isSpeaking || isPaused) && !currentSpokenText.isNullOrBlank() && !isLoginOrAuthScreen) {
                FloatingAudioPlayerBar(
                    currentText = currentSpokenText ?: "",
                    isSpeaking = isSpeaking,
                    isPaused = isPaused,
                    speechRate = speechRate,
                    currentLang = currentLang,
                    onPlayPause = {
                        if (isSpeaking) viewModel.pauseAudio() else viewModel.resumeAudio()
                    },
                    onReplay = { viewModel.replayAudio() },
                    onStop = { viewModel.stopAudio() },
                    onSpeedChange = { viewModel.setSpeechRate(it) },
                    onLanguageClick = { showLanguageDialog = true },
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(horizontal = 16.dp, vertical = 16.dp)
                )
            }

            if (voiceWarning != null) {
                VoiceUnavailableDialog(
                    message = voiceWarning!!,
                    onRetry = { viewModel.retryAudio() },
                    onDismiss = { viewModel.dismissVoiceWarning() }
                )
            }
        }
    }
