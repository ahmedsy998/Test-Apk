package com.example.model

import androidx.annotation.DrawableRes
import com.example.R

data class BiText(val ar: String, val en: String) {
    fun get(isArabic: Boolean): String = if (isArabic) ar else en
}

enum class AppScreen {
    SPLASH,
    LOGIN,
    REGISTER,
    ADD_ACCOUNT,
    HOME,
    OFFERS,
    SERVICES,
    SYRIATEL_CASH,
    SMART_USAGE_CENTER,
    OPERATIONS_CENTER,
    PREMIUM_PROFILE,
    NOTIFICATIONS,
    SETTINGS,
    SECURITY_SETTINGS,
    NOTIFICATION_SETTINGS,
    CUSTOMER_SUPPORT,
    PRIVACY_POLICY,
    TERMS_OF_USE,
    RECHARGE_BALANCE,
    REGISTERED_NUMBERS
}

data class UserProfile(
    val firstName: String = "أحمد",
    val lastName: String = "محمد",
    val firstNameEn: String = "Ahmad",
    val lastNameEn: String = "Mohammad",
    val phoneNumber: String = "0933123456",
    val avatarColorIndex: Int = 0,
    val lineType: BiText = BiText("خط مسبق الدفع", "Prepaid Line"),
    val subscriptionType: BiText = BiText("سيريتل بلس الذهبي", "SyriaTel Plus Gold"),
    val lineStatus: BiText = BiText("نشط (حساب تجريبي)", "Active (Demo Account)"),
    val activationDate: String = "15/03/2023",
    val expiryDate: String = "30/12/2026",
    val customerNumber: String = "SYR-9948210",
    val daysUsed: Int = 1332
) {
    fun displayFirstName(isArabic: Boolean): String =
        if (isArabic) firstName else firstNameEn.ifBlank { firstName }

    fun displayFullName(isArabic: Boolean): String =
        if (isArabic) "$firstName $lastName" else "${firstNameEn.ifBlank { firstName }} ${lastNameEn.ifBlank { lastName }}"
}

/**
 * Isolated per-account state for the Multi-Account Management System (up to 2 accounts).
 */
data class AccountSnapshot(
    val accountId: String,
    val userProfile: UserProfile,
    val balanceSyp: Int,
    val balanceExpiryDate: String,
    val internetRemainingGb: Float,
    val internetTotalGb: Float,
    val internetDailyAvgGb: Float,
    val internetEstRemainingDays: Int,
    val minutesRemaining: Int,
    val minutesTotal: Int,
    val minutesDailyAvg: Int,
    val smsRemaining: Int,
    val smsTotal: Int,
    val smsDailyAvg: Int,
    val services: List<ServiceItem>,
    val notifications: List<AppNotification>,
    val transactions: List<OperationRecord>
)

data class PromoBanner(
    val id: String,
    val badge: BiText,
    val title: BiText,
    val description: BiText,
    val ctaText: BiText,
    val targetCategory: OfferCategory,
    val accentHex: Long,
    val secondaryHex: Long,
    @DrawableRes val imageRes: Int
)

enum class OfferCategory(val title: BiText, @DrawableRes val imageRes: Int) {
    INTERNET(BiText("الإنترنت", "Internet"), R.drawable.img_promo_internet),
    SMS(BiText("الرسائل", "SMS"), R.drawable.img_promo_sms),
    MINUTES(BiText("الدقائق", "Minutes"), R.drawable.img_promo_minutes),
    MIX(BiText("ميكس", "Mix"), R.drawable.img_promo_mix),
    SPECIAL(BiText("عروض خاصة", "Special Offers"), R.drawable.img_promo_special),
    ANNIVERSARY(BiText("عرض ذكرى سنوية خطك", "Line Anniversary"), R.drawable.img_promo_anniversary)
}

data class OfferItem(
    val id: String,
    val category: OfferCategory,
    val name: BiText,
    val description: BiText,
    val quotaLabel: BiText,
    val priceSyp: Int,
    val validity: BiText,
    val highlightTag: BiText,
    val internetBonusGb: Float = 0f,
    val minutesBonus: Int = 0,
    val smsBonus: Int = 0
)

enum class ServiceSectionType(val title: BiText, val subtitle: BiText) {
    BASIC(
        BiText("الخدمات الأساسية", "Basic Services"),
        BiText("خدمات الاتصال والشبكة الأساسية لخطك", "Essential network and call management services")
    ),
    VARIETY(
        BiText("خدمات منوعة", "Variety Services"),
        BiText("باقات المحتوى الإخباري والرياضي والثقافي", "News, tech, sports, and cultural content services")
    )
}

data class ServiceItem(
    val id: String,
    val section: ServiceSectionType,
    val name: BiText,
    val description: BiText,
    val monthlyFee: BiText,
    val isActive: Boolean,
    val iconKey: String
)

enum class UsageMetric(val title: BiText, val unit: BiText) {
    INTERNET(BiText("الإنترنت", "Internet"), BiText("GB", "GB")),
    MINUTES(BiText("الدقائق", "Minutes"), BiText("دقيقة", "Min")),
    SMS(BiText("الرسائل", "SMS"), BiText("رسالة", "SMS"))
}

enum class UsagePeriod(val title: BiText) {
    DAILY(BiText("يومي", "Daily")),
    WEEKLY(BiText("أسبوعي", "Weekly"))
}

data class ChartDataPoint(
    val label: BiText,
    val internetGb: Float,
    val minutes: Int,
    val sms: Int
)

enum class InsightSeverity {
    NORMAL, HIGH_USAGE, LOW_BALANCE, EXPIRING_SOON
}

data class SmartInsight(
    val id: String,
    val title: BiText,
    val description: BiText,
    val severity: InsightSeverity
)

data class UsageAlertsConfig(
    val alert50Percent: Boolean = true,
    val alert80Percent: Boolean = true,
    val alert90Percent: Boolean = false,
    val alertNearExpiry: Boolean = true
)

enum class OperationCategory(val title: BiText) {
    ALL(BiText("الكل", "All")),
    RECHARGE(BiText("الشحن", "Recharge")),
    PACKAGES(BiText("الباقات", "Packages")),
    SERVICES(BiText("الخدمات", "Services")),
    ACCOUNT(BiText("الحساب", "Account")),
    SYRIATEL_CASH(BiText("سيريتل كاش", "SyriaTel Cash"))
}

enum class DateFilterOption(val title: BiText, val maxDaysAgo: Int) {
    TODAY(BiText("اليوم", "Today"), 0),
    LAST_7_DAYS(BiText("آخر 7 أيام", "Last 7 Days"), 7),
    LAST_30_DAYS(BiText("آخر 30 يوماً", "Last 30 Days"), 30)
}

enum class OperationStatus(val label: BiText, val colorHex: Long) {
    SUCCESS(BiText("ناجحة", "Successful"), 0xFF16A765),
    PROCESSING(BiText("قيد المعالجة", "Processing"), 0xFFE68A00),
    CANCELLED(BiText("ملغاة", "Cancelled"), 0xFF777777),
    FAILED(BiText("فاشلة", "Failed"), 0xFFD71920)
}

data class OperationRecord(
    val id: String,
    val referenceNumber: String,
    val title: BiText,
    val description: BiText,
    val category: OperationCategory,
    val dateTimeLabel: BiText,
    val daysAgo: Int,
    val amountLabel: BiText?,
    val status: OperationStatus,
    val extraNotes: BiText
)

enum class NotificationCategory(val title: BiText) {
    OFFERS(BiText("العروض", "Offers")),
    SERVICES(BiText("الخدمات", "Services")),
    SECURITY(BiText("أمان الحساب", "Account Security")),
    NEW_DEVICE(BiText("تسجيل الدخول إلى جهاز جديد", "New Device Login")),
    USAGE_ALERT(BiText("استهلاك الباقات", "Package Usage")),
    EXPIRY_ALERT(BiText("انتهاء صلاحية الباقة", "Package Expiration"))
}

data class AppNotification(
    val id: String,
    val category: NotificationCategory,
    val title: BiText,
    val message: BiText,
    val timestampLabel: BiText,
    val isRead: Boolean = false
)

data class NotificationPreferences(
    val offersEnabled: Boolean = true,
    val servicesEnabled: Boolean = true,
    val securityEnabled: Boolean = true,
    val newDeviceEnabled: Boolean = true
)
