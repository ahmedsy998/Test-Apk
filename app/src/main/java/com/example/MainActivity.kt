package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.model.AppScreen
import com.example.model.BiText
import com.example.ui.components.KnowMyNumberDialog
import com.example.ui.components.SyriaTelBottomNavigationBar
import com.example.ui.screens.*
import com.example.ui.theme.SyriatelGreen
import com.example.ui.theme.SyriatelTheme
import com.example.viewmodel.SyriaTelViewModel
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SyriaTelApp()
        }
    }
}

@Composable
fun SyriaTelApp(
    viewModel: SyriaTelViewModel = viewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.initPreferences(context)
    }

    LaunchedEffect(uiState.feedbackMessage) {
        if (uiState.feedbackMessage != null) {
            delay(2800L)
            viewModel.clearFeedback()
        }
    }

    val isBottomBarVisible = uiState.currentScreen in listOf(
        AppScreen.HOME,
        AppScreen.OFFERS,
        AppScreen.SERVICES,
        AppScreen.SYRIATEL_CASH
    )

    BackHandler(enabled = uiState.currentScreen != AppScreen.SPLASH && uiState.currentScreen != AppScreen.LOGIN && uiState.currentScreen != AppScreen.HOME) {
        if (uiState.currentScreen == AppScreen.REGISTER) {
            viewModel.navigateTo(AppScreen.LOGIN, clearStack = true)
        } else {
            viewModel.navigateBack()
        }
    }

    SyriatelTheme(
        darkTheme = uiState.isDarkMode,
        isArabic = uiState.isArabic
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Scaffold(
                contentWindowInsets = WindowInsets(0, 0, 0, 0),
                bottomBar = {
                    if (isBottomBarVisible) {
                        SyriaTelBottomNavigationBar(
                            currentScreen = uiState.currentScreen,
                            isArabic = uiState.isArabic,
                            onSelectScreen = { target ->
                                viewModel.navigateTo(target)
                            }
                        )
                    }
                }
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    AnimatedContent(
                        targetState = uiState.currentScreen,
                        transitionSpec = { fadeIn() togetherWith fadeOut() },
                        label = "screen_transition"
                    ) { screen ->
                        when (screen) {
                            AppScreen.SPLASH -> {
                                SplashScreen(
                                    isArabic = uiState.isArabic,
                                    onSplashCompleted = { viewModel.onSplashFinished() }
                                )
                            }

                            AppScreen.LOGIN -> {
                                LoginScreen(
                                    isArabic = uiState.isArabic,
                                    isDarkMode = uiState.isDarkMode,
                                    initialPhone = uiState.userProfile.phoneNumber,
                                    onLoginSuccess = { phone, remember ->
                                        viewModel.completeLogin(phone, remember, context)
                                    },
                                    onNavigateToRegister = {
                                        viewModel.navigateTo(AppScreen.REGISTER)
                                    },
                                    onToggleLanguage = {
                                        viewModel.setLanguage(!uiState.isArabic, context)
                                    }
                                )
                            }

                            AppScreen.REGISTER -> {
                                RegisterScreen(
                                    isArabic = uiState.isArabic,
                                    isDarkMode = uiState.isDarkMode,
                                    onRegisterSuccess = { fName, lName, phone ->
                                        viewModel.completeRegistration(fName, lName, phone, context)
                                    },
                                    onNavigateToLogin = {
                                        viewModel.navigateTo(AppScreen.LOGIN, clearStack = true)
                                    }
                                )
                            }

                            AppScreen.HOME -> {
                                HomeDashboardScreen(
                                    uiState = uiState,
                                    onOpenProfile = { viewModel.navigateTo(AppScreen.PREMIUM_PROFILE) },
                                    onOpenNotifications = { viewModel.navigateTo(AppScreen.NOTIFICATIONS) },
                                    onOpenSettings = { viewModel.navigateTo(AppScreen.SETTINGS) },
                                    onOpenRecharge = { viewModel.navigateTo(AppScreen.RECHARGE_BALANCE) },
                                    onOpenKnowMyNumber = { viewModel.navigateTo(AppScreen.REGISTERED_NUMBERS) },
                                    onOpenOffersCategory = { cat -> viewModel.navigateToOffersCategory(cat) },
                                    onOpenServices = { viewModel.navigateTo(AppScreen.SERVICES) },
                                    onOpenSmartUsageCenter = { viewModel.navigateTo(AppScreen.SMART_USAGE_CENTER) },
                                    onOpenOperationsCenter = { viewModel.navigateTo(AppScreen.OPERATIONS_CENTER) },
                                    onToggleBalanceVisibility = { viewModel.toggleBalanceVisibility() }
                                )
                            }

                            AppScreen.OFFERS -> {
                                OffersScreen(
                                    uiState = uiState,
                                    onSelectCategory = { cat -> viewModel.selectOfferCategory(cat) },
                                    onSubscribeOffer = { offer -> viewModel.subscribeToOffer(offer, context) }
                                )
                            }

                            AppScreen.SERVICES -> {
                                ServicesScreen(
                                    uiState = uiState,
                                    onToggleSection = { sec -> viewModel.toggleServiceSection(sec) },
                                    onToggleService = { id -> viewModel.toggleServiceStatus(id) }
                                )
                            }

                            AppScreen.SYRIATEL_CASH -> {
                                SyriatelCashScreen(
                                    uiState = uiState,
                                    onDemoCashAction = { isReg, phone, name ->
                                        viewModel.recordSyriatelCashDemoAction(isReg, phone, name)
                                    }
                                )
                            }

                            AppScreen.SMART_USAGE_CENTER -> {
                                SmartUsageCenterScreen(
                                    uiState = uiState,
                                    onBack = { viewModel.navigateBack() },
                                    onSelectPeriod = { p -> viewModel.selectUsagePeriod(p) },
                                    onSelectMetric = { m -> viewModel.selectUsageMetric(m) },
                                    onUpdateAlerts = { cfg -> viewModel.updateUsageAlerts(cfg, context) },
                                    onNavigateToOffers = { viewModel.navigateTo(AppScreen.OFFERS) },
                                    onNavigateToServices = { viewModel.navigateTo(AppScreen.SERVICES) },
                                    onNavigateToHome = { viewModel.navigateTo(AppScreen.HOME, clearStack = true) }
                                )
                            }

                            AppScreen.OPERATIONS_CENTER -> {
                                OperationsCenterScreen(
                                    uiState = uiState,
                                    onBack = { viewModel.navigateBack() },
                                    onSelectCategory = { cat -> viewModel.selectOperationCategory(cat) },
                                    onSelectDateFilter = { df -> viewModel.selectDateFilter(df) },
                                    onCopiedRef = {
                                        viewModel.showFeedback(
                                            BiText("تم نسخ الرقم المرجعي للعملية", "Reference ID copied to clipboard")
                                        )
                                    }
                                )
                            }

                            AppScreen.PREMIUM_PROFILE -> {
                                PremiumProfileScreen(
                                    uiState = uiState,
                                    onBack = { viewModel.navigateBack() },
                                    onSaveProfile = { fName, lName, avatarIdx ->
                                        viewModel.updateProfile(fName, lName, avatarIdx, context)
                                    },
                                    onNavigateToServices = { viewModel.navigateTo(AppScreen.SERVICES) },
                                    onNavigateToUsageCenter = { viewModel.navigateTo(AppScreen.SMART_USAGE_CENTER) },
                                    onNavigateToOperationsCenter = { viewModel.navigateTo(AppScreen.OPERATIONS_CENTER) },
                                    onNavigateToSecurity = { viewModel.navigateTo(AppScreen.SECURITY_SETTINGS) }
                                )
                            }

                            AppScreen.NOTIFICATIONS -> {
                                NotificationsScreen(
                                    uiState = uiState,
                                    onBack = { viewModel.navigateBack() },
                                    onMarkAllRead = { viewModel.markAllNotificationsRead() },
                                    onMarkRead = { id -> viewModel.markNotificationRead(id) },
                                    onClearAll = { viewModel.clearAllNotifications() }
                                )
                            }

                            AppScreen.SETTINGS -> {
                                SettingsScreen(
                                    uiState = uiState,
                                    onBack = { viewModel.navigateBack() },
                                    onToggleDarkMode = { viewModel.toggleDarkMode(context) },
                                    onSetLanguage = { ar -> viewModel.setLanguage(ar, context) },
                                    onOpenUserProfile = { viewModel.navigateTo(AppScreen.PREMIUM_PROFILE) },
                                    onOpenSecuritySettings = { viewModel.navigateTo(AppScreen.SECURITY_SETTINGS) },
                                    onOpenNotificationSettings = { viewModel.navigateTo(AppScreen.NOTIFICATION_SETTINGS) },
                                    onOpenCustomerSupport = { viewModel.navigateTo(AppScreen.CUSTOMER_SUPPORT) },
                                    onOpenPrivacyPolicy = { viewModel.navigateTo(AppScreen.PRIVACY_POLICY) },
                                    onOpenTermsOfUse = { viewModel.navigateTo(AppScreen.TERMS_OF_USE) },
                                    onConfirmLogout = { viewModel.logout() },
                                    onOpenAddAccount = { viewModel.navigateTo(AppScreen.ADD_ACCOUNT) },
                                    onSwitchAccount = { accId -> viewModel.switchAccount(accId, context) },
                                    onRemoveAccount = { accId -> viewModel.removeAccount(accId, context) }
                                )
                            }

                            AppScreen.ADD_ACCOUNT -> {
                                AddAccountScreen(
                                    isArabic = uiState.isArabic,
                                    isDarkMode = uiState.isDarkMode,
                                    existingPhones = uiState.savedAccounts.map { it.userProfile.phoneNumber },
                                    onBack = { viewModel.navigateBack() },
                                    onAddAccountSuccess = { fName, lName, phone ->
                                        viewModel.addSecondAccount(
                                            firstName = fName,
                                            lastName = lName,
                                            phone = phone,
                                            switchImmediately = true,
                                            context = context
                                        )
                                    }
                                )
                            }

                            AppScreen.SECURITY_SETTINGS -> {
                                SecuritySettingsScreen(
                                    uiState = uiState,
                                    onBack = { viewModel.navigateBack() },
                                    onSaveSecurity = { passChanged, bio, pin ->
                                        viewModel.updateSecuritySettings(passChanged, bio, pin)
                                    }
                                )
                            }

                            AppScreen.NOTIFICATION_SETTINGS -> {
                                NotificationSettingsScreen(
                                    uiState = uiState,
                                    onBack = { viewModel.navigateBack() },
                                    onUpdatePrefs = { prefs ->
                                        viewModel.updateNotificationPreferences(prefs, context)
                                    }
                                )
                            }

                            AppScreen.CUSTOMER_SUPPORT -> {
                                CustomerSupportScreen(
                                    isArabic = uiState.isArabic,
                                    onBack = { viewModel.navigateBack() },
                                    onSubmitSupportTicket = {
                                        viewModel.showFeedback(
                                            BiText("تم إرسال طلب الدعم التجريبي بنجاح", "Demo support request submitted")
                                        )
                                    }
                                )
                            }

                            AppScreen.PRIVACY_POLICY -> {
                                LegalDocumentScreen(
                                    isPrivacyPolicy = true,
                                    isArabic = uiState.isArabic,
                                    onBack = { viewModel.navigateBack() }
                                )
                            }

                            AppScreen.TERMS_OF_USE -> {
                                LegalDocumentScreen(
                                    isPrivacyPolicy = false,
                                    isArabic = uiState.isArabic,
                                    onBack = { viewModel.navigateBack() }
                                )
                            }

                            AppScreen.RECHARGE_BALANCE -> {
                                RechargeBalanceScreen(
                                    uiState = uiState,
                                    onBack = { viewModel.navigateBack() },
                                    onConfirmRecharge = { phone, amount ->
                                        viewModel.rechargeBalance(phone, amount, context)
                                    }
                                )
                            }

                            AppScreen.REGISTERED_NUMBERS -> {
                                RegisteredNumbersScreen(
                                    uiState = uiState,
                                    onBack = { viewModel.navigateBack() },
                                    onSwitchAccount = { accId ->
                                        viewModel.switchAccount(accId, context)
                                    },
                                    onViewAccountDetails = { accId ->
                                        if (uiState.activeAccountId != accId) {
                                            viewModel.switchAccount(accId, context)
                                        }
                                        viewModel.navigateTo(AppScreen.PREMIUM_PROFILE)
                                    },
                                    onAddAnotherAccount = {
                                        viewModel.navigateTo(AppScreen.ADD_ACCOUNT)
                                    }
                                )
                            }
                        }
                    }
                }
            }

            // Know My Number Modal
            if (uiState.showKnowMyNumberModal) {
                KnowMyNumberDialog(
                    phoneNumber = uiState.userProfile.phoneNumber,
                    customerNumber = uiState.userProfile.customerNumber,
                    isArabic = uiState.isArabic,
                    onDismiss = { viewModel.setShowKnowMyNumberModal(false) },
                    onCopied = {
                        viewModel.setShowKnowMyNumberModal(false)
                        viewModel.showFeedback(
                            BiText("تم نسخ رقم هاتفك بنجاح", "Phone number copied to clipboard")
                        )
                    }
                )
            }

            // Floating Feedback Toast Banner
            AnimatedVisibility(
                visible = uiState.feedbackMessage != null,
                enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut(),
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .statusBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            ) {
                Surface(
                    color = SyriatelGreen,
                    shape = RoundedCornerShape(16.dp),
                    shadowElevation = 6.dp
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.CheckCircle,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = uiState.feedbackMessage?.get(uiState.isArabic).orEmpty(),
                            style = MaterialTheme.typography.labelLarge.copy(
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            }
        }
    }
}
