package com.example.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.roundToInt
import kotlin.random.Random

data class SyriatelUiState(
    val currentScreen: AppScreen = AppScreen.SPLASH,
    val backStack: List<AppScreen> = emptyList(),
    val isArabic: Boolean = true,
    val isDarkMode: Boolean = false,
    val isAuthenticated: Boolean = false,
    val rememberMe: Boolean = true,

    // User Profile
    val userProfile: UserProfile = UserProfile(),

    // Account Balance
    val balanceSyp: Int = 25000,
    val isBalanceVisible: Boolean = true,
    val balanceExpiryDate: String = "30/12/2026",

    // Active Package Usage (Shared across Home & Smart Usage Center & Profile)
    val internetRemainingGb: Float = 8.0f,
    val internetTotalGb: Float = 12.0f,
    val internetDailyAvgGb: Float = 0.58f,
    val internetEstRemainingDays: Int = 14,

    val minutesRemaining: Int = 250,
    val minutesTotal: Int = 500,
    val minutesDailyAvg: Int = 35,

    val smsRemaining: Int = 120,
    val smsTotal: Int = 200,
    val smsDailyAvg: Int = 11,

    // Offers
    val selectedOfferCategory: OfferCategory = OfferCategory.INTERNET,
    val offers: List<OfferItem> = SyriaTelSampleData.allOffers,

    // Services
    val services: List<ServiceItem> = SyriaTelSampleData.initialServices,
    val isBasicServicesExpanded: Boolean = true,
    val isVarietyServicesExpanded: Boolean = true,

    // Smart Usage Center state
    val selectedUsagePeriod: UsagePeriod = UsagePeriod.WEEKLY,
    val selectedUsageMetric: UsageMetric = UsageMetric.INTERNET,
    val usageAlerts: UsageAlertsConfig = UsageAlertsConfig(),

    // Operations Center state
    val transactions: List<OperationRecord> = SyriaTelSampleData.initialTransactions,
    val selectedOperationCategory: OperationCategory = OperationCategory.ALL,
    val selectedDateFilter: DateFilterOption = DateFilterOption.LAST_30_DAYS,

    // Notifications
    val notifications: List<AppNotification> = SyriaTelSampleData.initialNotifications,
    val notificationPrefs: NotificationPreferences = NotificationPreferences(),

    // Security settings
    val biometricLockEnabled: Boolean = true,
    val appPinLockEnabled: Boolean = false,

    // Global Toast / Banner feedback
    val feedbackMessage: BiText? = null,

    // Know My Number modal state
    val showKnowMyNumberModal: Boolean = false
) {
    val internetUsedGb: Float
        get() = (internetTotalGb - internetRemainingGb).coerceAtLeast(0f)

    val internetUsedFraction: Float
        get() = if (internetTotalGb > 0f) (internetUsedGb / internetTotalGb).coerceIn(0f, 1f) else 0f

    val internetRemainingFraction: Float
        get() = if (internetTotalGb > 0f) (internetRemainingGb / internetTotalGb).coerceIn(0f, 1f) else 0f

    val minutesUsed: Int
        get() = (minutesTotal - minutesRemaining).coerceAtLeast(0)

    val minutesUsedFraction: Float
        get() = if (minutesTotal > 0) (minutesUsed.toFloat() / minutesTotal).coerceIn(0f, 1f) else 0f

    val minutesRemainingFraction: Float
        get() = if (minutesTotal > 0) (minutesRemaining.toFloat() / minutesTotal).coerceIn(0f, 1f) else 0f

    val smsUsed: Int
        get() = (smsTotal - smsRemaining).coerceAtLeast(0)

    val smsUsedFraction: Float
        get() = if (smsTotal > 0) (smsUsed.toFloat() / smsTotal).coerceIn(0f, 1f) else 0f

    val smsRemainingFraction: Float
        get() = if (smsTotal > 0) (smsRemaining.toFloat() / smsTotal).coerceIn(0f, 1f) else 0f

    val activeServicesCount: Int
        get() = services.count { it.isActive }

    val unreadNotificationsCount: Int
        get() = notifications.count { !it.isRead }

    fun formattedBalance(isArabic: Boolean): String {
        val formatted = NumberFormat.getNumberInstance(Locale.US).format(balanceSyp)
        return if (isArabic) "$formatted ل.س" else "$formatted SYP"
    }

    fun computeSmartInsights(): List<SmartInsight> {
        val netUsedPct = (internetUsedFraction * 100).roundToInt()
        val minUsedPct = (minutesUsedFraction * 100).roundToInt()
        val list = mutableListOf<SmartInsight>()

        list.add(
            SmartInsight(
                id = "ins_1",
                title = BiText(
                    "استهلاك الإنترنت أعلى بنسبة 15% من متوسط الأسبوع الماضي",
                    "Internet usage is 15% higher than last week's average"
                ),
                description = BiText(
                    "تم استهلاك ${String.format(Locale.US, "%.1f", internetUsedGb)} GB (${netUsedPct}%) ومتبقي لديك ${String.format(Locale.US, "%.1f", internetRemainingGb)} GB تكفي لحوالي $internetEstRemainingDays يوماً.",
                    "Used ${String.format(Locale.US, "%.1f", internetUsedGb)} GB ($netUsedPct%) with ${String.format(Locale.US, "%.1f", internetRemainingGb)} GB remaining (~$internetEstRemainingDays days left)."
                ),
                severity = if (netUsedPct >= 75) InsightSeverity.HIGH_USAGE else InsightSeverity.NORMAL
            )
        )

        list.add(
            SmartInsight(
                id = "ins_2",
                title = BiText(
                    "استهلاكك اليومي من الدقائق ضمن المعدل الطبيعي",
                    "Your daily call minutes usage is within normal range"
                ),
                description = BiText(
                    "معدل مكالماتك اليومي هو $minutesDailyAvg دقيقة. يتبقى لديك $minutesRemaining دقيقة من أصل $minutesTotal دقيقة ($minUsedPct% مستهلك).",
                    "Your daily average is $minutesDailyAvg mins. You have $minutesRemaining mins left out of $minutesTotal mins ($minUsedPct% used)."
                ),
                severity = InsightSeverity.NORMAL
            )
        )

        list.add(
            SmartInsight(
                id = "ins_3",
                title = if (balanceSyp < 15000) {
                    BiText("الرصيد المتوفر منخفض نسبياً للتجديد التلقائي", "Available balance is relatively low for auto-renewal")
                } else {
                    BiText("رصيدك كافٍ لتجديد الباقات والخدمات النشطة", "Your balance is sufficient for active package renewals")
                },
                description = BiText(
                    "الرصيد الحالي ${formattedBalance(true)} ولديك $activeServicesCount خدمات مفعّلة حالياً.",
                    "Current balance is ${formattedBalance(false)} with $activeServicesCount active services."
                ),
                severity = if (balanceSyp < 15000) InsightSeverity.LOW_BALANCE else InsightSeverity.NORMAL
            )
        )

        list.add(
            SmartInsight(
                id = "ins_4",
                title = BiText(
                    "يمكنك الاستفادة من باقة ميكس إضافية لتوفير 25%",
                    "You can save 25% by bundling with a Mix package"
                ),
                description = BiText(
                    "بناءً على استخدامك للإنترنت والدقائق معاً، باقة ميكس بلس تمنحك قيمة أعلى قبل انتهاء الباقة الحالية.",
                    "Based on your combined data and voice usage, Mix Plus offers higher value before your current bundle expires."
                ),
                severity = InsightSeverity.EXPIRING_SOON
            )
        )

        return list
    }
}

class SyriaTelViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(SyriatelUiState())
    val uiState: StateFlow<SyriatelUiState> = _uiState.asStateFlow()

    private var prefsInitialized = false

    fun initPreferences(context: Context) {
        if (prefsInitialized) return
        prefsInitialized = true
        val prefs = context.getSharedPreferences("syriatel_demo_prefs", Context.MODE_PRIVATE)
        val isArabic = prefs.getBoolean("is_arabic", true)
        val isDark = prefs.getBoolean("is_dark_mode", false)
        val firstName = prefs.getString("first_name", "أحمد") ?: "أحمد"
        val lastName = prefs.getString("last_name", "محمد") ?: "محمد"
        val firstNameEn = prefs.getString("first_name_en", "Ahmad") ?: "Ahmad"
        val lastNameEn = prefs.getString("last_name_en", "Mohammad") ?: "Mohammad"
        val phone = prefs.getString("phone_number", "0933123456") ?: "0933123456"
        val avatarIdx = prefs.getInt("avatar_idx", 0)
        val balance = prefs.getInt("balance_syp", 25000)

        val alert50 = prefs.getBoolean("alert_50", true)
        val alert80 = prefs.getBoolean("alert_80", true)
        val alert90 = prefs.getBoolean("alert_90", false)
        val alertExp = prefs.getBoolean("alert_exp", true)

        val notifOffers = prefs.getBoolean("notif_offers", true)
        val notifServices = prefs.getBoolean("notif_services", true)
        val notifSecurity = prefs.getBoolean("notif_security", true)
        val notifDevice = prefs.getBoolean("notif_device", true)

        _uiState.update { state ->
            state.copy(
                isArabic = isArabic,
                isDarkMode = isDark,
                balanceSyp = balance,
                userProfile = state.userProfile.copy(
                    firstName = firstName,
                    lastName = lastName,
                    firstNameEn = firstNameEn,
                    lastNameEn = lastNameEn,
                    phoneNumber = phone,
                    avatarColorIndex = avatarIdx
                ),
                usageAlerts = UsageAlertsConfig(
                    alert50Percent = alert50,
                    alert80Percent = alert80,
                    alert90Percent = alert90,
                    alertNearExpiry = alertExp
                ),
                notificationPrefs = NotificationPreferences(
                    offersEnabled = notifOffers,
                    servicesEnabled = notifServices,
                    securityEnabled = notifSecurity,
                    newDeviceEnabled = notifDevice
                )
            )
        }
    }

    private fun savePrefs(context: Context?) {
        context ?: return
        val state = _uiState.value
        context.getSharedPreferences("syriatel_demo_prefs", Context.MODE_PRIVATE)
            .edit()
            .putBoolean("is_arabic", state.isArabic)
            .putBoolean("is_dark_mode", state.isDarkMode)
            .putString("first_name", state.userProfile.firstName)
            .putString("last_name", state.userProfile.lastName)
            .putString("first_name_en", state.userProfile.firstNameEn)
            .putString("last_name_en", state.userProfile.lastNameEn)
            .putString("phone_number", state.userProfile.phoneNumber)
            .putInt("avatar_idx", state.userProfile.avatarColorIndex)
            .putInt("balance_syp", state.balanceSyp)
            .putBoolean("alert_50", state.usageAlerts.alert50Percent)
            .putBoolean("alert_80", state.usageAlerts.alert80Percent)
            .putBoolean("alert_90", state.usageAlerts.alert90Percent)
            .putBoolean("alert_exp", state.usageAlerts.alertNearExpiry)
            .putBoolean("notif_offers", state.notificationPrefs.offersEnabled)
            .putBoolean("notif_services", state.notificationPrefs.servicesEnabled)
            .putBoolean("notif_security", state.notificationPrefs.securityEnabled)
            .putBoolean("notif_device", state.notificationPrefs.newDeviceEnabled)
            .apply()
    }

    fun navigateTo(screen: AppScreen, clearStack: Boolean = false) {
        _uiState.update { state ->
            if (state.currentScreen == screen) return@update state
            val newStack = if (clearStack) {
                emptyList()
            } else {
                (state.backStack + state.currentScreen).takeLast(12)
            }
            state.copy(
                currentScreen = screen,
                backStack = newStack
            )
        }
    }

    fun navigateToOffersCategory(category: OfferCategory) {
        _uiState.update { state ->
            val newStack = (state.backStack + state.currentScreen).takeLast(12)
            state.copy(
                selectedOfferCategory = category,
                currentScreen = AppScreen.OFFERS,
                backStack = newStack
            )
        }
    }

    fun navigateBack(): Boolean {
        val current = _uiState.value
        if (current.backStack.isNotEmpty()) {
            val prev = current.backStack.last()
            _uiState.update {
                it.copy(
                    currentScreen = prev,
                    backStack = it.backStack.dropLast(1)
                )
            }
            return true
        }
        if (current.currentScreen != AppScreen.HOME && current.isAuthenticated) {
            _uiState.update { it.copy(currentScreen = AppScreen.HOME, backStack = emptyList()) }
            return true
        }
        return false
    }

    fun onSplashFinished() {
        _uiState.update { state ->
            if (state.currentScreen == AppScreen.SPLASH) {
                state.copy(currentScreen = AppScreen.LOGIN, backStack = emptyList())
            } else state
        }
    }

    fun completeLogin(phone: String, rememberMe: Boolean, context: Context? = null) {
        _uiState.update { state ->
            val newTx = createOperationRecord(
                title = BiText("تسجيل الدخول", "Account Sign In"),
                description = BiText("تم تسجيل الدخول بنجاح للرقم $phone (جلسة تجريبية)", "Signed in successfully with $phone (Demo session)"),
                category = OperationCategory.ACCOUNT,
                amountLabel = null,
                status = OperationStatus.SUCCESS
            )
            state.copy(
                isAuthenticated = true,
                rememberMe = rememberMe,
                userProfile = state.userProfile.copy(phoneNumber = phone),
                currentScreen = AppScreen.HOME,
                backStack = emptyList(),
                transactions = listOf(newTx) + state.transactions,
                feedbackMessage = BiText("مرحباً بك في تطبيق سيريتل!", "Welcome to SyriaTel!")
            )
        }
        savePrefs(context)
    }

    fun completeRegistration(
        firstName: String,
        lastName: String,
        phone: String,
        context: Context? = null
    ) {
        _uiState.update { state ->
            val newTx = createOperationRecord(
                title = BiText("إنشاء حساب جديد", "New Account Registration"),
                description = BiText("تم إنشاء حساب تجريبي باسم $firstName $lastName للرقم $phone", "Created demo account for $firstName $lastName ($phone)"),
                category = OperationCategory.ACCOUNT,
                amountLabel = null,
                status = OperationStatus.SUCCESS
            )
            state.copy(
                isAuthenticated = true,
                userProfile = state.userProfile.copy(
                    firstName = firstName,
                    lastName = lastName,
                    firstNameEn = firstName,
                    lastNameEn = lastName,
                    phoneNumber = phone
                ),
                currentScreen = AppScreen.HOME,
                backStack = emptyList(),
                transactions = listOf(newTx) + state.transactions,
                feedbackMessage = BiText("تم إنشاء حسابك التجريبي بنجاح!", "Your demo account has been created!")
            )
        }
        savePrefs(context)
    }

    fun logout() {
        _uiState.update { state ->
            state.copy(
                isAuthenticated = false,
                currentScreen = AppScreen.LOGIN,
                backStack = emptyList(),
                feedbackMessage = BiText("تم تسجيل الخروج بنجاح", "Logged out successfully")
            )
        }
    }

    fun setLanguage(isArabic: Boolean, context: Context? = null) {
        _uiState.update { state ->
            state.copy(
                isArabic = isArabic,
                feedbackMessage = if (isArabic) {
                    BiText("تم تغيير اللغة إلى العربية", "Language changed to Arabic")
                } else {
                    BiText("Language changed to English", "Language changed to English")
                }
            )
        }
        savePrefs(context)
    }

    fun toggleDarkMode(context: Context? = null) {
        _uiState.update { state ->
            val nextDark = !state.isDarkMode
            state.copy(
                isDarkMode = nextDark,
                feedbackMessage = if (nextDark) {
                    BiText("تم تفعيل الوضع الليلي الداكن", "Dark Mode enabled")
                } else {
                    BiText("تم تفعيل الوضع النهاري الفاتح", "Light Mode enabled")
                }
            )
        }
        savePrefs(context)
    }

    fun toggleBalanceVisibility() {
        _uiState.update { it.copy(isBalanceVisible = !it.isBalanceVisible) }
    }

    fun setShowKnowMyNumberModal(show: Boolean) {
        _uiState.update { it.copy(showKnowMyNumberModal = show) }
    }

    fun selectOfferCategory(category: OfferCategory) {
        _uiState.update { it.copy(selectedOfferCategory = category) }
    }

    fun subscribeToOffer(offer: OfferItem, context: Context? = null) {
        _uiState.update { state ->
            val formattedPrice = NumberFormat.getNumberInstance(Locale.US).format(offer.priceSyp)
            val newBalance = (state.balanceSyp - offer.priceSyp).coerceAtLeast(0)
            val newTx = createOperationRecord(
                title = BiText("تفعيل ${offer.name.ar}", "Activate ${offer.name.en}"),
                description = BiText(
                    "اشتراك تجريبي في ${offer.name.ar} (${offer.quotaLabel.ar})",
                    "Simulated subscription to ${offer.name.en} (${offer.quotaLabel.en})"
                ),
                category = OperationCategory.PACKAGES,
                amountLabel = if (offer.priceSyp > 0) {
                    BiText("$formattedPrice ل.س", "$formattedPrice SYP")
                } else {
                    BiText("مجاناً", "Free")
                },
                status = OperationStatus.SUCCESS
            )

            val updatedNotifs = if (state.notificationPrefs.offersEnabled) {
                val notif = AppNotification(
                    id = "notif_${System.currentTimeMillis()}",
                    category = NotificationCategory.OFFERS,
                    title = BiText("تم تفعيل ${offer.name.ar}", "${offer.name.en} Activated"),
                    message = BiText(
                        "تمت إضافة مزايا الباقة (${offer.quotaLabel.ar}) إلى رصيد باقاتك التجريبية بنجاح.",
                        "Package benefits (${offer.quotaLabel.en}) have been added to your demo account."
                    ),
                    timestampLabel = BiText("الآن", "Just now"),
                    isRead = false
                )
                listOf(notif) + state.notifications
            } else state.notifications

            state.copy(
                balanceSyp = newBalance,
                internetRemainingGb = state.internetRemainingGb + offer.internetBonusGb,
                internetTotalGb = state.internetTotalGb + offer.internetBonusGb,
                minutesRemaining = state.minutesRemaining + offer.minutesBonus,
                minutesTotal = state.minutesTotal + offer.minutesBonus,
                smsRemaining = state.smsRemaining + offer.smsBonus,
                smsTotal = state.smsTotal + offer.smsBonus,
                transactions = listOf(newTx) + state.transactions,
                notifications = updatedNotifs,
                feedbackMessage = BiText(
                    "تم تفعيل ${offer.name.ar} بنجاح (محاكاة تجريبية)",
                    "${offer.name.en} activated successfully (Demo simulation)"
                )
            )
        }
        savePrefs(context)
    }

    fun rechargeBalance(targetPhone: String, amountSyp: Int, context: Context? = null) {
        _uiState.update { state ->
            val formattedAmount = NumberFormat.getNumberInstance(Locale.US).format(amountSyp)
            val newBalance = state.balanceSyp + amountSyp
            val newTx = createOperationRecord(
                title = BiText("شحن الرصيد", "Balance Recharge"),
                description = BiText(
                    "شحن رصيد تجريبي للرقم $targetPhone بقيمة $formattedAmount ل.س",
                    "Simulated balance recharge for $targetPhone of $formattedAmount SYP"
                ),
                category = OperationCategory.RECHARGE,
                amountLabel = BiText("+$formattedAmount ل.س", "+$formattedAmount SYP"),
                status = OperationStatus.SUCCESS
            )
            val notif = AppNotification(
                id = "notif_${System.currentTimeMillis()}",
                category = NotificationCategory.SERVICES,
                title = BiText("تم شحن رصيدك التجريبي", "Demo Balance Recharged"),
                message = BiText(
                    "تمت إضافة $formattedAmount ل.س إلى رصيدك المتوفر. الرصيد الحالي ${NumberFormat.getNumberInstance(Locale.US).format(newBalance)} ل.س.",
                    "Added $formattedAmount SYP to your available balance. Current balance: ${NumberFormat.getNumberInstance(Locale.US).format(newBalance)} SYP."
                ),
                timestampLabel = BiText("الآن", "Just now"),
                isRead = false
            )
            state.copy(
                balanceSyp = newBalance,
                transactions = listOf(newTx) + state.transactions,
                notifications = listOf(notif) + state.notifications,
                feedbackMessage = BiText(
                    "تم شحن الرصيد بقيمة $formattedAmount ل.س (عملية تجريبية)",
                    "Recharged $formattedAmount SYP successfully (Demo operation)"
                )
            )
        }
        savePrefs(context)
    }

    fun toggleServiceSection(section: ServiceSectionType) {
        _uiState.update { state ->
            when (section) {
                ServiceSectionType.BASIC -> state.copy(isBasicServicesExpanded = !state.isBasicServicesExpanded)
                ServiceSectionType.VARIETY -> state.copy(isVarietyServicesExpanded = !state.isVarietyServicesExpanded)
            }
        }
    }

    fun toggleServiceStatus(serviceId: String) {
        _uiState.update { state ->
            val target = state.services.find { it.id == serviceId } ?: return@update state
            val newActive = !target.isActive
            val updatedServices = state.services.map {
                if (it.id == serviceId) it.copy(isActive = newActive) else it
            }

            val opTitle = if (newActive) {
                BiText("تفعيل ${target.name.ar}", "Activate ${target.name.en}")
            } else {
                BiText("إيقاف ${target.name.ar}", "Deactivate ${target.name.en}")
            }
            val opDesc = if (newActive) {
                BiText("تم تفعيل ${target.name.ar} على خطك التجريبي", "Activated ${target.name.en} on your demo line")
            } else {
                BiText("تم إيقاف ${target.name.ar} على خطك التجريبي", "Deactivated ${target.name.en} on your demo line")
            }

            val newTx = createOperationRecord(
                title = opTitle,
                description = opDesc,
                category = OperationCategory.SERVICES,
                amountLabel = if (newActive) target.monthlyFee else null,
                status = OperationStatus.SUCCESS
            )

            val updatedNotifs = if (state.notificationPrefs.servicesEnabled) {
                val notif = AppNotification(
                    id = "notif_${System.currentTimeMillis()}",
                    category = NotificationCategory.SERVICES,
                    title = opTitle,
                    message = opDesc,
                    timestampLabel = BiText("الآن", "Just now"),
                    isRead = false
                )
                listOf(notif) + state.notifications
            } else state.notifications

            state.copy(
                services = updatedServices,
                transactions = listOf(newTx) + state.transactions,
                notifications = updatedNotifs,
                feedbackMessage = if (newActive) {
                    BiText("تم تفعيل ${target.name.ar}", "${target.name.en} activated")
                } else {
                    BiText("تم إيقاف ${target.name.ar}", "${target.name.en} deactivated")
                }
            )
        }
    }

    fun recordSyriatelCashDemoAction(isRegistration: Boolean, phone: String, fullName: String = "") {
        _uiState.update { state ->
            val title = if (isRegistration) {
                BiText("إنشاء حساب سيريتل كاش تجريبي", "SyriaTel Cash Demo Registration")
            } else {
                BiText("تسجيل دخول سيريتل كاش تجريبي", "SyriaTel Cash Demo Login")
            }
            val desc = if (isRegistration) {
                BiText("طلب فتح محفظة إلكترونية تجريبية للرقم $phone ($fullName)", "Simulated e-wallet registration request for $phone ($fullName)")
            } else {
                BiText("تسجيل دخول تجريبي إلى واجهة سيريتل كاش للرقم $phone", "Simulated login to SyriaTel Cash interface for $phone")
            }
            val newTx = createOperationRecord(
                title = title,
                description = desc,
                category = OperationCategory.SYRIATEL_CASH,
                amountLabel = BiText("تجريبي", "Demo Only"),
                status = OperationStatus.SUCCESS
            )
            state.copy(
                transactions = listOf(newTx) + state.transactions,
                feedbackMessage = BiText(
                    "تم تسجيل عملية سيريتل كاش التجريبية في مركز العمليات",
                    "SyriaTel Cash demo operation logged in Operations Center"
                )
            )
        }
    }

    fun selectUsagePeriod(period: UsagePeriod) {
        _uiState.update { it.copy(selectedUsagePeriod = period) }
    }

    fun selectUsageMetric(metric: UsageMetric) {
        _uiState.update { it.copy(selectedUsageMetric = metric) }
    }

    fun updateUsageAlerts(newConfig: UsageAlertsConfig, context: Context? = null) {
        _uiState.update { state ->
            val newTx = createOperationRecord(
                title = BiText("تحديث تنبيهات الاستهلاك الذكي", "Update Smart Usage Alerts"),
                description = BiText("تم تعديل إعدادات التنبيهات التلقائية لباقات الإنترنت والدقائق", "Updated automatic usage threshold alerts for packages"),
                category = OperationCategory.ACCOUNT,
                amountLabel = null,
                status = OperationStatus.SUCCESS
            )

            // Generate a smart usage notification if 50% alert is enabled and minutes are at 50%
            val shouldAddAlertNotif = newConfig.alert50Percent && !state.usageAlerts.alert50Percent
            val updatedNotifs = if (shouldAddAlertNotif) {
                val notif = AppNotification(
                    id = "notif_${System.currentTimeMillis()}",
                    category = NotificationCategory.USAGE_ALERT,
                    title = BiText("تنبيه استهلاك 50% مفعّل", "50% Usage Threshold Alert Active"),
                    message = BiText(
                        "استهلاك الدقائق الحالي وصل إلى 50% (متبقي ${state.minutesRemaining} دقيقة). سيصلك إشعار عند اقتراب باقي الباقات من الحد المحدد.",
                        "Current voice minutes usage reached 50% (${state.minutesRemaining} mins left). You will be notified as packages approach your thresholds."
                    ),
                    timestampLabel = BiText("الآن", "Just now"),
                    isRead = false
                )
                listOf(notif) + state.notifications
            } else state.notifications

            state.copy(
                usageAlerts = newConfig,
                transactions = listOf(newTx) + state.transactions,
                notifications = updatedNotifs,
                feedbackMessage = BiText("تم حفظ إعدادات تنبيهات الاستهلاك", "Usage alert preferences saved")
            )
        }
        savePrefs(context)
    }

    fun selectOperationCategory(category: OperationCategory) {
        _uiState.update { it.copy(selectedOperationCategory = category) }
    }

    fun selectDateFilter(filter: DateFilterOption) {
        _uiState.update { it.copy(selectedDateFilter = filter) }
    }

    fun updateProfile(
        firstName: String,
        lastName: String,
        avatarColorIndex: Int,
        context: Context? = null
    ) {
        _uiState.update { state ->
            val newTx = createOperationRecord(
                title = BiText("تعديل المعلومات الشخصية", "Update Personal Profile"),
                description = BiText(
                    "تم تحديث الاسم في الملف الشخصي إلى $firstName $lastName",
                    "Updated profile name to $firstName $lastName"
                ),
                category = OperationCategory.ACCOUNT,
                amountLabel = null,
                status = OperationStatus.SUCCESS
            )
            state.copy(
                userProfile = state.userProfile.copy(
                    firstName = firstName,
                    lastName = lastName,
                    firstNameEn = firstName,
                    lastNameEn = lastName,
                    avatarColorIndex = avatarColorIndex
                ),
                transactions = listOf(newTx) + state.transactions,
                feedbackMessage = BiText("تم حفظ التعديلات في ملفك الشخصي بنجاح", "Profile changes saved successfully")
            )
        }
        savePrefs(context)
    }

    fun markAllNotificationsRead() {
        _uiState.update { state ->
            state.copy(
                notifications = state.notifications.map { it.copy(isRead = true) },
                feedbackMessage = BiText("تم تحديد جميع الإشعارات كمقروءة", "All notifications marked as read")
            )
        }
    }

    fun markNotificationRead(id: String) {
        _uiState.update { state ->
            state.copy(
                notifications = state.notifications.map {
                    if (it.id == id) it.copy(isRead = true) else it
                }
            )
        }
    }

    fun clearAllNotifications() {
        _uiState.update { state ->
            state.copy(
                notifications = emptyList(),
                feedbackMessage = BiText("تم مسح جميع الإشعارات", "All notifications cleared")
            )
        }
    }

    fun updateNotificationPreferences(newPrefs: NotificationPreferences, context: Context? = null) {
        _uiState.update { state ->
            state.copy(
                notificationPrefs = newPrefs,
                feedbackMessage = BiText("تم تحديث إعدادات الإشعارات", "Notification settings updated")
            )
        }
        savePrefs(context)
    }

    fun updateSecuritySettings(
        passwordChanged: Boolean,
        biometricEnabled: Boolean,
        pinEnabled: Boolean
    ) {
        _uiState.update { state ->
            val newTx = createOperationRecord(
                title = BiText("تعديل إعدادات الأمان", "Security Settings Updated"),
                description = if (passwordChanged) {
                    BiText("تم تغيير كلمة المرور التجريبية وتحديث خيارات الحماية", "Updated demo password and security protection options")
                } else {
                    BiText("تم تحديث خيارات القفل البيومتري ورمز الحماية", "Updated biometric and PIN protection preferences")
                },
                category = OperationCategory.ACCOUNT,
                amountLabel = null,
                status = OperationStatus.SUCCESS
            )
            state.copy(
                biometricLockEnabled = biometricEnabled,
                appPinLockEnabled = pinEnabled,
                transactions = listOf(newTx) + state.transactions,
                feedbackMessage = BiText("تم حفظ إعدادات الأمان بنجاح", "Security settings saved successfully")
            )
        }
    }

    fun showFeedback(message: BiText) {
        _uiState.update { it.copy(feedbackMessage = message) }
    }

    fun clearFeedback() {
        _uiState.update { it.copy(feedbackMessage = null) }
    }

    private fun createOperationRecord(
        title: BiText,
        description: BiText,
        category: OperationCategory,
        amountLabel: BiText?,
        status: OperationStatus
    ): OperationRecord {
        val randomDigits = Random.nextInt(100000, 999999)
        return OperationRecord(
            id = "op_${System.currentTimeMillis()}_$randomDigits",
            referenceNumber = "SYR-OP-$randomDigits",
            title = title,
            description = description,
            category = category,
            dateTimeLabel = BiText("اليوم • الآن", "Today • Just now"),
            daysAgo = 0,
            amountLabel = amountLabel,
            status = status,
            extraNotes = BiText(
                "عملية تجريبية مسجلة تلقائياً في مركز العمليات — لأغراض العرض فقط.",
                "Demonstration operation automatically recorded in Operations Center — for prototype display only."
            )
        )
    }
}
