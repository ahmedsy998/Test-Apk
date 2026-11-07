package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.model.*
import com.example.ui.components.AvatarPalette
import com.example.ui.components.SyriaTelSubScreenTopBar
import com.example.ui.components.UserAvatarCircle
import com.example.ui.theme.SyriatelDarkRed
import com.example.ui.theme.SyriatelGreen
import com.example.ui.theme.SyriatelRed
import com.example.viewmodel.SyriatelUiState
import java.util.Locale

// ==========================================
// SCREEN 9 — PREMIUM PROFILE
// ==========================================
@Composable
fun PremiumProfileScreen(
    uiState: SyriatelUiState,
    onBack: () -> Unit,
    onSaveProfile: (String, String, Int) -> Unit,
    onNavigateToServices: () -> Unit,
    onNavigateToUsageCenter: () -> Unit,
    onNavigateToOperationsCenter: () -> Unit,
    onNavigateToSecurity: () -> Unit
) {
    val isArabic = uiState.isArabic
    val profile = uiState.userProfile
    var showEditModal by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("premium_profile_screen")
    ) {
        SyriaTelSubScreenTopBar(
            title = if (isArabic) "الملف الشخصي" else "Personal Profile",
            subtitle = if (isArabic) "معلومات حسابك وخطك وتفاصيل الاشتراك" else "Your account, line, and subscription details",
            onBack = onBack,
            trailingAction = {
                IconButton(
                    onClick = { showEditModal = true },
                    modifier = Modifier.testTag("profile_top_edit_button")
                ) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = if (isArabic) "تعديل" else "Edit",
                        tint = SyriatelRed
                    )
                }
            }
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {
            // A. PROFILE HEADER
            Card(
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(
                                colors = listOf(SyriatelRed, SyriatelDarkRed, Color(0xFF281215))
                            )
                        )
                        .padding(22.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(
                            contentAlignment = Alignment.BottomEnd
                        ) {
                            Surface(
                                shape = CircleShape,
                                border = BorderStroke(3.dp, Color.White),
                                color = Color.Transparent
                            ) {
                                UserAvatarCircle(
                                    name = profile.displayFirstName(isArabic),
                                    colorIndex = profile.avatarColorIndex,
                                    size = 82.dp
                                )
                            }
                            Surface(
                                onClick = { showEditModal = true },
                                shape = CircleShape,
                                color = Color.White,
                                shadowElevation = 4.dp,
                                modifier = Modifier.size(30.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Filled.Edit,
                                        contentDescription = null,
                                        tint = SyriatelRed,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = profile.displayFullName(isArabic),
                            style = MaterialTheme.typography.headlineSmall.copy(
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = profile.phoneNumber,
                            style = MaterialTheme.typography.titleMedium.copy(
                                color = Color.White.copy(alpha = 0.9f)
                            )
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Surface(
                                color = SyriatelGreen,
                                shape = RoundedCornerShape(50)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Verified,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(5.dp))
                                    Text(
                                        text = if (isArabic) "حساب تجريبي" else "Demo Account",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                }
                            }

                            OutlinedButton(
                                onClick = { showEditModal = true },
                                shape = RoundedCornerShape(50),
                                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.7f)),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 4.dp),
                                modifier = Modifier
                                    .height(32.dp)
                                    .testTag("profile_header_edit_button")
                            ) {
                                Text(
                                    text = if (isArabic) "تعديل الملف" else "Edit Profile",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                        }
                    }
                }
            }

            // D. ACCOUNT STATISTICS (4 compact stat cards)
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                ProfileStatBox(
                    value = uiState.activeServicesCount.toString(),
                    label = if (isArabic) "الخدمات المفعّلة" else "Active Services",
                    color = SyriatelGreen,
                    modifier = Modifier.weight(1f)
                )
                ProfileStatBox(
                    value = "3",
                    label = if (isArabic) "الباقات النشطة" else "Active Bundles",
                    color = SyriatelRed,
                    modifier = Modifier.weight(1f)
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                ProfileStatBox(
                    value = uiState.transactions.size.toString(),
                    label = if (isArabic) "إجمالي العمليات التجريبية" else "Total Demo Operations",
                    color = Color(0xFF0F6CBD),
                    modifier = Modifier.weight(1f)
                )
                ProfileStatBox(
                    value = profile.daysUsed.toString(),
                    label = if (isArabic) "أيام استخدام الخط" else "Days Active on Line",
                    color = Color(0xFF8E24AA),
                    modifier = Modifier.weight(1f)
                )
            }

            // B. LINE INFORMATION CARD ("معلومات الخط")
            ProfileSectionCard(
                title = if (isArabic) "معلومات الخط" else "Line Information",
                icon = Icons.Filled.SimCard,
                rows = listOf(
                    Pair(if (isArabic) "رقم الهاتف" else "Phone Number", profile.phoneNumber),
                    Pair(if (isArabic) "نوع الخط" else "Line Type", profile.lineType.get(isArabic)),
                    Pair(if (isArabic) "نوع الاشتراك" else "Subscription Plan", profile.subscriptionType.get(isArabic)),
                    Pair(if (isArabic) "حالة الخط" else "Line Status", profile.lineStatus.get(isArabic)),
                    Pair(if (isArabic) "تاريخ تفعيل الخط" else "Activation Date", profile.activationDate),
                    Pair(if (isArabic) "تاريخ انتهاء الصلاحية" else "Expiration Date", profile.expiryDate),
                    Pair(if (isArabic) "رقم العميل التجريبي" else "Demo Customer ID", profile.customerNumber)
                )
            )

            // C. SUBSCRIPTION INFORMATION ("تفاصيل الاشتراك")
            ProfileSectionCard(
                title = if (isArabic) "تفاصيل الاشتراك" else "Subscription Details",
                icon = Icons.Filled.Inventory2,
                rows = listOf(
                    Pair(
                        if (isArabic) "الباقة الأساسية الحالية" else "Current Plan",
                        profile.subscriptionType.get(isArabic)
                    ),
                    Pair(
                        if (isArabic) "باقة الإنترنت النشطة" else "Active Internet Package",
                        "${String.format(Locale.US, "%.1f", uiState.internetRemainingGb)} GB / ${String.format(Locale.US, "%.0f", uiState.internetTotalGb)} GB"
                    ),
                    Pair(
                        if (isArabic) "باقة الدقائق النشطة" else "Active Minutes Package",
                        if (isArabic) "${uiState.minutesRemaining} / ${uiState.minutesTotal} دقيقة" else "${uiState.minutesRemaining} / ${uiState.minutesTotal} Mins"
                    ),
                    Pair(
                        if (isArabic) "باقة الرسائل النشطة" else "Active SMS Package",
                        if (isArabic) "${uiState.smsRemaining} / ${uiState.smsTotal} رسالة" else "${uiState.smsRemaining} / ${uiState.smsTotal} SMS"
                    ),
                    Pair(
                        if (isArabic) "تاريخ التجديد / الانتهاء" else "Renewal / Expiry Date",
                        uiState.balanceExpiryDate
                    ),
                    Pair(
                        if (isArabic) "عدد الخدمات المفعّلة" else "Active Services Count",
                        if (isArabic) "${uiState.activeServicesCount} خدمات" else "${uiState.activeServicesCount} Services"
                    )
                )
            )

            // E. PROFILE ACTIONS
            Text(
                text = if (isArabic) "إدارة الحساب والوصول السريع" else "Account Management & Quick Access",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground
            )

            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    ProfileActionRow(
                        title = if (isArabic) "تعديل المعلومات الشخصية" else "Edit Personal Information",
                        icon = Icons.Filled.Edit,
                        onClick = { showEditModal = true }
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline)
                    ProfileActionRow(
                        title = if (isArabic) "إدارة الخدمات" else "Manage Services",
                        icon = Icons.Filled.Tune,
                        onClick = onNavigateToServices
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline)
                    ProfileActionRow(
                        title = if (isArabic) "مركز الاستهلاك الذكي" else "Smart Usage Center",
                        icon = Icons.Filled.DonutLarge,
                        onClick = onNavigateToUsageCenter
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline)
                    ProfileActionRow(
                        title = if (isArabic) "مركز العمليات" else "Operations Center",
                        icon = Icons.Filled.History,
                        onClick = onNavigateToOperationsCenter
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline)
                    ProfileActionRow(
                        title = if (isArabic) "إعدادات الأمان" else "Security Settings",
                        icon = Icons.Filled.Security,
                        onClick = onNavigateToSecurity
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    if (showEditModal) {
        EditProfileDialog(
            userProfile = profile,
            isArabic = isArabic,
            onDismiss = { showEditModal = false },
            onSave = { fName, lName, colorIdx ->
                onSaveProfile(fName, lName, colorIdx)
                showEditModal = false
            }
        )
    }
}

@Composable
private fun ProfileStatBox(
    value: String,
    label: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall.copy(
                    color = color,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun ProfileSectionCard(
    title: String,
    icon: ImageVector,
    rows: List<Pair<String, String>>
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = SyriatelRed,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outline)
            Spacer(modifier = Modifier.height(10.dp))

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                rows.forEach { (label, value) ->
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = label,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = value,
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfileActionRow(
    title: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 15.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = SyriatelRed,
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(15.dp)
        )
    }
}

@Composable
private fun EditProfileDialog(
    userProfile: UserProfile,
    isArabic: Boolean,
    onDismiss: () -> Unit,
    onSave: (String, String, Int) -> Unit
) {
    var firstName by remember { mutableStateOf(userProfile.firstName) }
    var lastName by remember { mutableStateOf(userProfile.lastName) }
    var selectedAvatarIdx by remember { mutableIntStateOf(userProfile.avatarColorIndex) }
    var errorText by remember { mutableStateOf<String?>(null) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(22.dp)
            ) {
                Text(
                    text = if (isArabic) "تعديل المعلومات الشخصية" else "Edit Personal Profile",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = if (isArabic) "اختر مظهر الصورة الرمزية" else "Choose Avatar Theme",
                    style = MaterialTheme.typography.labelLarge
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    AvatarPalette.forEachIndexed { idx, color ->
                        val isSelected = idx == selectedAvatarIdx
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(color)
                                .then(
                                    if (isSelected) {
                                        Modifier.border(3.dp, MaterialTheme.colorScheme.onSurface, CircleShape)
                                    } else Modifier
                                )
                                .clickable { selectedAvatarIdx = idx }
                        ) {
                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Filled.Check,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = if (isArabic) "الاسم الأول" else "First Name",
                    style = MaterialTheme.typography.labelLarge
                )
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = firstName,
                    onValueChange = {
                        firstName = it
                        errorText = null
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("edit_profile_first_name")
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = if (isArabic) "الكنية" else "Last Name",
                    style = MaterialTheme.typography.labelLarge
                )
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = lastName,
                    onValueChange = {
                        lastName = it
                        errorText = null
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("edit_profile_last_name")
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = if (isArabic) "رقم الهاتف (للقراءة فقط)" else "Phone Number (Read-only)",
                    style = MaterialTheme.typography.labelLarge
                )
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = userProfile.phoneNumber,
                    onValueChange = {},
                    enabled = false,
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                if (errorText != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = errorText.orEmpty(),
                        style = MaterialTheme.typography.bodySmall,
                        color = SyriatelRed
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                    ) {
                        Text(if (isArabic) "إلغاء" else "Cancel")
                    }

                    Button(
                        onClick = {
                            if (firstName.isBlank() || lastName.isBlank()) {
                                errorText = if (isArabic) "يرجى إدخال الاسم والكنية" else "Please enter first and last name"
                            } else {
                                onSave(firstName.trim(), lastName.trim(), selectedAvatarIdx)
                            }
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SyriatelRed),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("save_profile_changes_button")
                    ) {
                        Text(
                            text = if (isArabic) "حفظ" else "Save",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

// ==========================================
// SCREEN 10 — NOTIFICATIONS
// ==========================================
@Composable
fun NotificationsScreen(
    uiState: SyriatelUiState,
    onBack: () -> Unit,
    onMarkAllRead: () -> Unit,
    onMarkRead: (String) -> Unit,
    onClearAll: () -> Unit
) {
    val isArabic = uiState.isArabic
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("notifications_screen")
    ) {
        SyriaTelSubScreenTopBar(
            title = if (isArabic) "الإشعارات" else "Notifications",
            subtitle = if (isArabic) {
                "لديك ${uiState.unreadNotificationsCount} إشعارات غير مقروءة"
            } else {
                "You have ${uiState.unreadNotificationsCount} unread notifications"
            },
            onBack = onBack
        )

        if (uiState.notifications.isNotEmpty()) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            ) {
                OutlinedButton(
                    onClick = onMarkAllRead,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp)
                        .testTag("notifications_mark_all_read")
                ) {
                    Icon(Icons.Filled.DoneAll, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(if (isArabic) "تحديد الكل كمقروء" else "Mark All Read")
                }

                OutlinedButton(
                    onClick = onClearAll,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = SyriatelRed),
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp)
                        .testTag("notifications_clear_all")
                ) {
                    Icon(Icons.Filled.DeleteSweep, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(if (isArabic) "مسح الإشعارات" else "Clear All")
                }
            }
        }

        if (uiState.notifications.isEmpty()) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(76.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.NotificationsOff,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(38.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = if (isArabic) "لا توجد إشعارات حالياً" else "No Notifications Yet",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = if (isArabic) {
                            "ستظهر هنا تنبيهات استهلاك الباقات، العروض الحصرية، وتحديثات الخدمات."
                        } else {
                            "Package usage alerts, exclusive offers, and service updates will appear here."
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                uiState.notifications.forEach { item ->
                    val icon = when (item.category) {
                        NotificationCategory.OFFERS -> Icons.Filled.CardGiftcard
                        NotificationCategory.SERVICES -> Icons.Filled.SettingsCell
                        NotificationCategory.SECURITY -> Icons.Filled.Shield
                        NotificationCategory.NEW_DEVICE -> Icons.Filled.Smartphone
                        NotificationCategory.USAGE_ALERT -> Icons.Filled.DataUsage
                        NotificationCategory.EXPIRY_ALERT -> Icons.Filled.EventBusy
                    }

                    Card(
                        onClick = { onMarkRead(item.id) },
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (!item.isRead) {
                                MaterialTheme.colorScheme.surface
                            } else {
                                MaterialTheme.colorScheme.surface.copy(alpha = 0.75f)
                            }
                        ),
                        border = BorderStroke(
                            width = if (!item.isRead) 1.5.dp else 1.dp,
                            color = if (!item.isRead) SyriatelRed.copy(alpha = 0.45f)
                            else MaterialTheme.colorScheme.outline
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            verticalAlignment = Alignment.Top,
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(
                                        if (!item.isRead) SyriatelRed.copy(alpha = 0.12f)
                                        else MaterialTheme.colorScheme.surfaceVariant
                                    )
                            ) {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = null,
                                    tint = if (!item.isRead) SyriatelRed else MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(22.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = item.category.title.get(isArabic),
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = SyriatelRed,
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = item.timestampLabel.get(isArabic),
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                        if (!item.isRead) {
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Box(
                                                modifier = Modifier
                                                    .size(8.dp)
                                                    .clip(CircleShape)
                                                    .background(SyriatelRed)
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    text = item.title.get(isArabic),
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.onSurface
                                )

                                Spacer(modifier = Modifier.height(3.dp))

                                Text(
                                    text = item.message.get(isArabic),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

// ==========================================
// SCREEN 11 & 12 — SETTINGS + DARK MODE + SUB-SCREENS
// ==========================================
@Composable
fun SettingsScreen(
    uiState: SyriatelUiState,
    onBack: () -> Unit,
    onToggleDarkMode: () -> Unit,
    onSetLanguage: (Boolean) -> Unit,
    onOpenUserProfile: () -> Unit,
    onOpenSecuritySettings: () -> Unit,
    onOpenNotificationSettings: () -> Unit,
    onOpenCustomerSupport: () -> Unit,
    onOpenPrivacyPolicy: () -> Unit,
    onOpenTermsOfUse: () -> Unit,
    onConfirmLogout: () -> Unit,
    onOpenAddAccount: () -> Unit = {},
    onSwitchAccount: (String) -> Unit = {},
    onRemoveAccount: (String) -> Unit = {}
) {
    val isArabic = uiState.isArabic
    var showLogoutDialog by remember { mutableStateOf(false) }
    var accountPendingRemoval by remember { mutableStateOf<AccountSnapshot?>(null) }
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("settings_screen")
    ) {
        SyriaTelSubScreenTopBar(
            title = if (isArabic) "الإعدادات" else "Settings",
            subtitle = if (isArabic) "تخصيص التطبيق واللغة والأمان وإدارة الحسابات" else "Customize app theme, language, security & accounts",
            onBack = onBack,
            trailingAction = {
                // Day/Night Theme Toggle Icon at top of Settings screen
                IconButton(
                    onClick = onToggleDarkMode,
                    modifier = Modifier
                        .size(48.dp)
                        .testTag("settings_theme_toggle_button")
                ) {
                    Icon(
                        imageVector = if (uiState.isDarkMode) Icons.Filled.LightMode else Icons.Filled.DarkMode,
                        contentDescription = if (isArabic) "تبديل المظهر الليلي/النهاري" else "Toggle Dark/Light Theme",
                        tint = SyriatelRed
                    )
                }
            }
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {
            // ==========================================
            // MULTI-ACCOUNT MANAGEMENT ("إدارة الحسابات")
            // ==========================================
            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("account_management_section")
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(SyriatelRed.copy(alpha = 0.1f))
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.ManageAccounts,
                                    contentDescription = null,
                                    tint = SyriatelRed,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = if (isArabic) "إدارة الحسابات" else "Account Management",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = if (isArabic) {
                                        "التبديل السريع بين خطوطك وحساباتك (${uiState.savedAccounts.size}/2)"
                                    } else {
                                        "Switch seamlessly between your lines (${uiState.savedAccounts.size}/2)"
                                    },
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Surface(
                            color = SyriatelRed.copy(alpha = 0.10f),
                            shape = RoundedCornerShape(50)
                        ) {
                            Text(
                                text = "${uiState.savedAccounts.size} / 2",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    color = SyriatelRed,
                                    fontWeight = FontWeight.Bold
                                ),
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }

                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.7f))

                    // Account Cards (Account 1 & Account 2)
                    uiState.savedAccounts.forEachIndexed { index, account ->
                        val isActiveAccount = account.accountId == uiState.activeAccountId
                        Surface(
                            shape = RoundedCornerShape(18.dp),
                            color = if (isActiveAccount) {
                                SyriatelRed.copy(alpha = if (uiState.isDarkMode) 0.12f else 0.05f)
                            } else {
                                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)
                            },
                            border = BorderStroke(
                                width = if (isActiveAccount) 1.5.dp else 1.dp,
                                color = if (isActiveAccount) SyriatelRed.copy(alpha = 0.65f) else MaterialTheme.colorScheme.outline
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("account_card_${account.accountId}")
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        UserAvatarCircle(
                                            name = account.userProfile.displayFirstName(isArabic),
                                            colorIndex = account.userProfile.avatarColorIndex,
                                            size = 46.dp
                                        )
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Column {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                                            ) {
                                                Text(
                                                    text = account.userProfile.displayFullName(isArabic),
                                                    style = MaterialTheme.typography.titleSmall.copy(
                                                        fontWeight = FontWeight.Bold
                                                    ),
                                                    color = MaterialTheme.colorScheme.onSurface
                                                )
                                                Surface(
                                                    color = SyriatelGreen.copy(alpha = 0.14f),
                                                    shape = RoundedCornerShape(50)
                                                ) {
                                                    Text(
                                                        text = account.userProfile.lineStatus.get(isArabic),
                                                        style = MaterialTheme.typography.labelSmall.copy(
                                                            color = SyriatelGreen,
                                                            fontWeight = FontWeight.Bold
                                                        ),
                                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                                    )
                                                }
                                            }
                                            Spacer(modifier = Modifier.height(2.dp))
                                            Text(
                                                text = account.userProfile.phoneNumber,
                                                style = MaterialTheme.typography.bodyMedium.copy(
                                                    fontWeight = FontWeight.SemiBold
                                                ),
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                            Spacer(modifier = Modifier.height(2.dp))
                                            Text(
                                                text = if (isArabic) {
                                                    "${account.userProfile.lineType.ar} • ${String.format(Locale.US, "%,d", account.balanceSyp)} ل.س"
                                                } else {
                                                    "${account.userProfile.lineType.en} • ${String.format(Locale.US, "%,d", account.balanceSyp)} SYP"
                                                },
                                                style = MaterialTheme.typography.labelSmall,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    }

                                    if (isActiveAccount) {
                                        Surface(
                                            color = SyriatelGreen,
                                            shape = RoundedCornerShape(50),
                                            modifier = Modifier.testTag("active_account_badge_${account.accountId}")
                                        ) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Filled.CheckCircle,
                                                    contentDescription = null,
                                                    tint = Color.White,
                                                    modifier = Modifier.size(14.dp)
                                                )
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text(
                                                    text = if (isArabic) "الحساب الحالي" else "Active Account",
                                                    style = MaterialTheme.typography.labelSmall.copy(
                                                        color = Color.White,
                                                        fontWeight = FontWeight.Bold
                                                    )
                                                )
                                            }
                                        }
                                    } else if (uiState.savedAccounts.size > 1) {
                                        IconButton(
                                            onClick = { accountPendingRemoval = account },
                                            modifier = Modifier
                                                .size(38.dp)
                                                .testTag("remove_account_button_${account.accountId}")
                                        ) {
                                            Icon(
                                                imageVector = Icons.Filled.DeleteOutline,
                                                contentDescription = if (isArabic) "إزالة الحساب" else "Remove Account",
                                                tint = SyriatelRed
                                            )
                                        }
                                    }
                                }

                                if (!isActiveAccount) {
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Button(
                                            onClick = { onSwitchAccount(account.accountId) },
                                            shape = RoundedCornerShape(12.dp),
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = SyriatelRed,
                                                contentColor = Color.White
                                            ),
                                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp),
                                            modifier = Modifier
                                                .weight(1f)
                                                .height(42.dp)
                                                .testTag("switch_account_button_${account.accountId}")
                                        ) {
                                            Icon(
                                                imageVector = Icons.Filled.SwapHoriz,
                                                contentDescription = null,
                                                modifier = Modifier.size(18.dp)
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(
                                                text = if (isArabic) "التبديل إلى هذا الحساب" else "Switch to This Account",
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

                    // "إضافة حساب آخر" Button (when fewer than 2 accounts exist)
                    if (uiState.canAddAnotherAccount) {
                        OutlinedButton(
                            onClick = onOpenAddAccount,
                            shape = RoundedCornerShape(14.dp),
                            border = BorderStroke(1.5.dp, SyriatelRed),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = SyriatelRed),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("add_another_account_button")
                        ) {
                            Icon(
                                imageVector = Icons.Filled.PersonAddAlt1,
                                contentDescription = null,
                                tint = SyriatelRed,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isArabic) "إضافة حساب آخر" else "Add Another Account",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    color = SyriatelRed,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    } else {
                        Surface(
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.65f),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Info,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (isArabic) {
                                        "تم الوصول إلى الحد الأقصى للحسابات المحفوظة (حسابان). يمكنك التبديل بينهما أو إزالة الحساب غير النشط لإضافة حساب آخر."
                                    } else {
                                        "Maximum of 2 saved accounts reached. Switch between them or remove the inactive account to add a different one."
                                    },
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }

            // Theme Card (Screen 12 Dark Mode Switch)
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(42.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(SyriatelRed.copy(alpha = 0.1f))
                        ) {
                            Icon(
                                imageVector = if (uiState.isDarkMode) Icons.Filled.DarkMode else Icons.Filled.LightMode,
                                contentDescription = null,
                                tint = SyriatelRed
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = if (isArabic) "المظهر الداكن (الوضع الليلي)" else "Dark Mode Appearance",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = if (uiState.isDarkMode) {
                                    if (isArabic) "مفعّل حالياً" else "Currently Active"
                                } else {
                                    if (isArabic) "المظهر الفاتح مفعّل" else "Light Theme Active"
                                },
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Switch(
                        checked = uiState.isDarkMode,
                        onCheckedChange = { onToggleDarkMode() },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = SyriatelRed
                        ),
                        modifier = Modifier.testTag("dark_mode_switch")
                    )
                }
            }

            // 1. اللغة (Language Switcher)
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.Language,
                            contentDescription = null,
                            tint = SyriatelRed
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = if (isArabic) "اللغة / Language" else "Language / اللغة",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Surface(
                            onClick = { onSetLanguage(true) },
                            color = if (isArabic) SyriatelRed else MaterialTheme.colorScheme.surfaceVariant,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp)
                                .testTag("lang_arabic_button")
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = "العربية (RTL)",
                                    style = MaterialTheme.typography.labelLarge.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = if (isArabic) Color.White else MaterialTheme.colorScheme.onSurface
                                    )
                                )
                            }
                        }

                        Surface(
                            onClick = { onSetLanguage(false) },
                            color = if (!isArabic) SyriatelRed else MaterialTheme.colorScheme.surfaceVariant,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp)
                                .testTag("lang_english_button")
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = "English (LTR)",
                                    style = MaterialTheme.typography.labelLarge.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = if (!isArabic) Color.White else MaterialTheme.colorScheme.onSurface
                                    )
                                )
                            }
                        }
                    }
                }
            }

            // Settings Navigation List
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    SettingsNavItemRow(
                        title = if (isArabic) "معلومات المستخدم" else "User Information",
                        subtitle = "${uiState.userProfile.displayFullName(isArabic)} • ${uiState.userProfile.phoneNumber}",
                        icon = Icons.Filled.Person,
                        testTag = "settings_user_info",
                        onClick = onOpenUserProfile
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline)
                    SettingsNavItemRow(
                        title = if (isArabic) "إعدادات الأمان" else "Security Settings",
                        subtitle = if (isArabic) "كلمة المرور، الحماية بالبصمة ورمز PIN" else "Password, biometric & PIN lock",
                        icon = Icons.Filled.Lock,
                        testTag = "settings_security",
                        onClick = onOpenSecuritySettings
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline)
                    SettingsNavItemRow(
                        title = if (isArabic) "إعدادات الإشعارات" else "Notification Preferences",
                        subtitle = if (isArabic) "التحكم بإشعارات العروض والخدمات والأمان" else "Control offers, services & security alerts",
                        icon = Icons.Filled.Notifications,
                        testTag = "settings_notifications",
                        onClick = onOpenNotificationSettings
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline)
                    SettingsNavItemRow(
                        title = if (isArabic) "خدمة الزبائن" else "Customer Support",
                        subtitle = if (isArabic) "الأسئلة الشائعة والتواصل والدعم الفني" else "FAQs, contact options & support ticket",
                        icon = Icons.Filled.SupportAgent,
                        testTag = "settings_customer_support",
                        onClick = onOpenCustomerSupport
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline)
                    SettingsNavItemRow(
                        title = if (isArabic) "سياسة الخصوصية" else "Privacy Policy",
                        subtitle = if (isArabic) "قواعد حماية البيانات والخصوصية" else "Data protection & privacy guidelines",
                        icon = Icons.Filled.PrivacyTip,
                        testTag = "settings_privacy_policy",
                        onClick = onOpenPrivacyPolicy
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline)
                    SettingsNavItemRow(
                        title = if (isArabic) "شروط الاستخدام" else "Terms of Use",
                        subtitle = if (isArabic) "الشروط والأحكام الخاصة بالتطبيق" else "Application terms and conditions",
                        icon = Icons.Filled.Description,
                        testTag = "settings_terms_of_use",
                        onClick = onOpenTermsOfUse
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // 7. تسجيل الخروج (Logout Button)
            Button(
                onClick = { showLogoutDialog = true },
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SyriatelRed),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .testTag("settings_logout_button")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                    contentDescription = null,
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isArabic) "تسجيل الخروج" else "Log Out",
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    if (accountPendingRemoval != null) {
        val targetAccount = accountPendingRemoval!!
        AlertDialog(
            onDismissRequest = { accountPendingRemoval = null },
            title = {
                Text(
                    text = if (isArabic) "إزالة الحساب المحفوظ" else "Remove Saved Account",
                    style = MaterialTheme.typography.titleLarge
                )
            },
            text = {
                Text(
                    text = if (isArabic) {
                        "هل أنت متأكد من رغبتك في إزالة حساب (${targetAccount.userProfile.displayFullName(true)} - ${targetAccount.userProfile.phoneNumber}) من قائمة الحسابات المحفوظة؟"
                    } else {
                        "Are you sure you want to remove (${targetAccount.userProfile.displayFullName(false)} - ${targetAccount.userProfile.phoneNumber}) from your saved accounts?"
                    }
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        val idToRemove = targetAccount.accountId
                        accountPendingRemoval = null
                        onRemoveAccount(idToRemove)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SyriatelRed),
                    modifier = Modifier.testTag("confirm_remove_account_dialog_button")
                ) {
                    Text(if (isArabic) "إزالة الحساب" else "Remove", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { accountPendingRemoval = null }) {
                    Text(if (isArabic) "إلغاء" else "Cancel")
                }
            }
        )
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = {
                Text(
                    text = if (isArabic) "تأكيد تسجيل الخروج" else "Confirm Log Out",
                    style = MaterialTheme.typography.titleLarge
                )
            },
            text = {
                Text(
                    text = if (isArabic) {
                        "هل أنت متأكد من رغبتك في تسجيل الخروج من جلسة سيريتل التجريبية الحالية؟"
                    } else {
                        "Are you sure you want to log out of your current SyriaTel demo session?"
                    }
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showLogoutDialog = false
                        onConfirmLogout()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SyriatelRed),
                    modifier = Modifier.testTag("confirm_logout_dialog_button")
                ) {
                    Text(if (isArabic) "تسجيل الخروج" else "Log Out", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text(if (isArabic) "إلغاء" else "Cancel")
                }
            }
        )
    }
}

@Composable
private fun SettingsNavItemRow(
    title: String,
    subtitle: String,
    icon: ImageVector,
    testTag: String,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 14.dp)
            .testTag(testTag)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(SyriatelRed.copy(alpha = 0.1f))
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = SyriatelRed,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(15.dp)
        )
    }
}

// ==========================================
// SUB-SCREEN: SECURITY SETTINGS (إعدادات الأمان)
// ==========================================
@Composable
fun SecuritySettingsScreen(
    uiState: SyriatelUiState,
    onBack: () -> Unit,
    onSaveSecurity: (Boolean, Boolean, Boolean) -> Unit
) {
    val isArabic = uiState.isArabic
    var currentPass by remember { mutableStateOf("") }
    var newPass by remember { mutableStateOf("") }
    var confirmPass by remember { mutableStateOf("") }
    var showCurrent by remember { mutableStateOf(false) }
    var showNew by remember { mutableStateOf(false) }
    var biometricEnabled by remember { mutableStateOf(uiState.biometricLockEnabled) }
    var pinLockEnabled by remember { mutableStateOf(uiState.appPinLockEnabled) }
    var errorText by remember { mutableStateOf<String?>(null) }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        SyriaTelSubScreenTopBar(
            title = if (isArabic) "إعدادات الأمان" else "Security Settings",
            subtitle = if (isArabic) "تغيير كلمة المرور وخيارات الحماية الإضافية" else "Change password and extra security options",
            onBack = onBack
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = if (isArabic) "تغيير كلمة المرور التجريبية" else "Change Demo Password",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedTextField(
                        value = currentPass,
                        onValueChange = {
                            currentPass = it
                            errorText = null
                        },
                        label = { Text(if (isArabic) "كلمة المرور الحالية" else "Current Password") },
                        visualTransformation = if (showCurrent) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { showCurrent = !showCurrent }) {
                                Icon(
                                    imageVector = if (showCurrent) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                    contentDescription = null
                                )
                            }
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = newPass,
                        onValueChange = {
                            newPass = it
                            errorText = null
                        },
                        label = { Text(if (isArabic) "كلمة المرور الجديدة" else "New Password") },
                        visualTransformation = if (showNew) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { showNew = !showNew }) {
                                Icon(
                                    imageVector = if (showNew) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                    contentDescription = null
                                )
                            }
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = confirmPass,
                        onValueChange = {
                            confirmPass = it
                            errorText = null
                        },
                        label = { Text(if (isArabic) "تأكيد كلمة المرور الجديدة" else "Confirm New Password") },
                        visualTransformation = if (showNew) VisualTransformation.None else PasswordVisualTransformation(),
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    if (errorText != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = errorText.orEmpty(),
                            style = MaterialTheme.typography.bodySmall,
                            color = SyriatelRed
                        )
                    }
                }
            }

            // Additional Security Options
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = if (isArabic) "حماية إضافية للتطبيق (تجريبي)" else "Additional App Protection (Prototype)",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (isArabic) "الحماية بالبصمة البيومترية" else "Biometric Fingerprint Lock",
                                style = MaterialTheme.typography.titleSmall
                            )
                            Text(
                                text = if (isArabic) "فتح التطبيق السريع عبر بصمة الإصبع" else "Quickly unlock app with fingerprint",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Switch(
                            checked = biometricEnabled,
                            onCheckedChange = { biometricEnabled = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = SyriatelGreen
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline)
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (isArabic) "قفل التطبيق برمز PIN" else "Application PIN Lock",
                                style = MaterialTheme.typography.titleSmall
                            )
                            Text(
                                text = if (isArabic) "طلب رمز سري عند فتح التطبيق" else "Require PIN code on app launch",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Switch(
                            checked = pinLockEnabled,
                            onCheckedChange = { pinLockEnabled = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = SyriatelRed
                            )
                        )
                    }
                }
            }

            Button(
                onClick = {
                    val passFilled = currentPass.isNotBlank() || newPass.isNotBlank() || confirmPass.isNotBlank()
                    if (passFilled) {
                        when {
                            currentPass.isBlank() -> {
                                errorText = if (isArabic) "يرجى إدخال كلمة المرور الحالية" else "Enter current password"
                            }
                            newPass.length < 6 -> {
                                errorText = if (isArabic) "يجب أن تكون كلمة المرور الجديدة 6 محارف على الأقل" else "New password must be at least 6 characters"
                            }
                            newPass != confirmPass -> {
                                errorText = if (isArabic) "كلمتا المرور غير متطابقتين" else "Passwords do not match"
                            }
                            else -> {
                                onSaveSecurity(true, biometricEnabled, pinLockEnabled)
                                currentPass = ""
                                newPass = ""
                                confirmPass = ""
                            }
                        }
                    } else {
                        onSaveSecurity(false, biometricEnabled, pinLockEnabled)
                    }
                },
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SyriatelRed),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text(
                    text = if (isArabic) "حفظ إعدادات الأمان" else "Save Security Settings",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

// ==========================================
// SUB-SCREEN: NOTIFICATION SETTINGS (إعدادات الإشعارات)
// ==========================================
@Composable
fun NotificationSettingsScreen(
    uiState: SyriatelUiState,
    onBack: () -> Unit,
    onUpdatePrefs: (NotificationPreferences) -> Unit
) {
    val isArabic = uiState.isArabic
    val prefs = uiState.notificationPrefs

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        SyriaTelSubScreenTopBar(
            title = if (isArabic) "إعدادات الإشعارات" else "Notification Preferences",
            subtitle = if (isArabic) "تحكم بكل فئة من الإشعارات بشكل مستقل" else "Manage each notification category independently",
            onBack = onBack
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    NotifPrefRow(
                        title = if (isArabic) "العروض" else "Offers",
                        subtitle = if (isArabic) "إشعارات العروض الجديدة والحسومات الحصرية" else "New promotional bundles and exclusive discounts",
                        checked = prefs.offersEnabled,
                        onCheckedChange = { onUpdatePrefs(prefs.copy(offersEnabled = it)) }
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline)
                    NotifPrefRow(
                        title = if (isArabic) "الخدمات" else "Services",
                        subtitle = if (isArabic) "تأكيد تفعيل أو إيقاف الخدمات وتجديدها" else "Service activation, deactivation, and renewal alerts",
                        checked = prefs.servicesEnabled,
                        onCheckedChange = { onUpdatePrefs(prefs.copy(servicesEnabled = it)) }
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline)
                    NotifPrefRow(
                        title = if (isArabic) "أمان الحساب" else "Account Security",
                        subtitle = if (isArabic) "تنبيهات تغيير كلمة المرور وإعدادات الحماية" else "Password updates and security alerts",
                        checked = prefs.securityEnabled,
                        onCheckedChange = { onUpdatePrefs(prefs.copy(securityEnabled = it)) }
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline)
                    NotifPrefRow(
                        title = if (isArabic) "تسجيل الدخول إلى جهاز جديد" else "New Device Login",
                        subtitle = if (isArabic) "تنبيه فوري عند الدخول إلى حسابك من جهاز آخر" else "Instant alert when signing in from another device",
                        checked = prefs.newDeviceEnabled,
                        onCheckedChange = { onUpdatePrefs(prefs.copy(newDeviceEnabled = it)) }
                    )
                }
            }
        }
    }
}

@Composable
private fun NotifPrefRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = SyriatelRed
            )
        )
    }
}

// ==========================================
// SUB-SCREEN: CUSTOMER SUPPORT (خدمة الزبائن)
// ==========================================
@Composable
fun CustomerSupportScreen(
    isArabic: Boolean,
    onBack: () -> Unit,
    onSubmitSupportTicket: () -> Unit
) {
    var subject by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var expandedFaqIndex by remember { mutableIntStateOf(0) }
    var ticketSent by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        SyriaTelSubScreenTopBar(
            title = if (isArabic) "خدمة الزبائن" else "Customer Support",
            subtitle = if (isArabic) "نحن هنا لمساعدتك على مدار الساعة" else "We are here to assist you 24/7",
            onBack = onBack
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {
            // Contact Channels
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp)
                    ) {
                        Icon(Icons.Filled.Call, contentDescription = null, tint = SyriatelRed)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = if (isArabic) "مركز النداء: 111" else "Call Center: 111",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = if (isArabic) "متاح 24/7" else "Available 24/7",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp)
                    ) {
                        Icon(Icons.Filled.Email, contentDescription = null, tint = SyriatelGreen)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "care@syriatel.sy",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = if (isArabic) "دعم تجريبي" else "Demo Support",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // FAQs
            Text(
                text = if (isArabic) "الأسئلة الشائعة" else "Frequently Asked Questions",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
            )

            SyriaTelSampleData.faqs.forEachIndexed { idx, (q, a) ->
                val isExp = expandedFaqIndex == idx
                Card(
                    onClick = { expandedFaqIndex = if (isExp) -1 else idx },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = q.get(isArabic),
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.weight(1f)
                            )
                            Icon(
                                imageVector = if (isExp) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                                contentDescription = null,
                                tint = SyriatelRed
                            )
                        }
                        AnimatedVisibility(visible = isExp) {
                            Text(
                                text = a.get(isArabic),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                    }
                }
            }

            // Support Request Form
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = if (isArabic) "إرسال طلب دعم فني (تجريبي)" else "Submit Support Request (Demo)",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = subject,
                        onValueChange = { subject = it },
                        label = { Text(if (isArabic) "موضوع الاستفسار" else "Subject") },
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = message,
                        onValueChange = { message = it },
                        label = { Text(if (isArabic) "تفاصيل الرسالة" else "Message Details") },
                        minLines = 3,
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    Button(
                        onClick = {
                            if (subject.isNotBlank() && message.isNotBlank()) {
                                ticketSent = true
                                subject = ""
                                message = ""
                                onSubmitSupportTicket()
                            }
                        },
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SyriatelRed),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                    ) {
                        Text(
                            text = if (isArabic) "إرسال الطلب التجريبي" else "Submit Demo Request",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    if (ticketSent) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = if (isArabic) {
                                "تم استلام رسالتك التجريبية بنجاح! شكراً لتواصلك."
                            } else {
                                "Your demo support request has been received!"
                            },
                            style = MaterialTheme.typography.bodySmall,
                            color = SyriatelGreen
                        )
                    }
                }
            }
        }
    }
}

// ==========================================
// SUB-SCREENS: PRIVACY POLICY & TERMS OF USE
// ==========================================
@Composable
fun LegalDocumentScreen(
    isPrivacyPolicy: Boolean,
    isArabic: Boolean,
    onBack: () -> Unit
) {
    val title = if (isPrivacyPolicy) {
        if (isArabic) "سياسة الخصوصية" else "Privacy Policy"
    } else {
        if (isArabic) "شروط الاستخدام" else "Terms of Use"
    }

    val sections = if (isPrivacyPolicy) {
        listOf(
            Pair(
                BiText("1. طبيعة التطبيق التجريبية", "1. Prototype Nature of the Application"),
                BiText(
                    "هذا التطبيق هو نموذج أولي تفاعلي (UI/UX Prototype) لتطبيق سيريتل. جميع البيانات المدخلة (الاسم، رقم الهاتف، تفضيلات اللغة والمظهر) تُحفظ محلياً على جهازك فقط لأغراض العرض ولا يتم إرسالها لأي خادم خارجي.",
                    "This application is an interactive UI/UX prototype for SyriaTel. All entered data (name, phone number, language, and theme preferences) is stored strictly locally on your device for demonstration purposes and is never transmitted to any external server."
                )
            ),
            Pair(
                BiText("2. حماية المعلومات المحلية", "2. Local Information Protection"),
                BiText(
                    "نحترم خصوصيتك بالكامل؛ لا يطلب التطبيق أي أذونات حساسة ولا يجمع أي بيانات مالية أو مصرفية حقيقية في قسم سيريتل كاش أو شحن الرصيد.",
                    "We respect your privacy completely; the app does not request sensitive permissions or collect any real financial or banking credentials in the SyriaTel Cash or Recharge sections."
                )
            ),
            Pair(
                BiText("3. التحكم بالإشعارات والتنبيهات", "3. Notifications & Alerts Control"),
                BiText(
                    "يمكنك في أي وقت تخصيص أو إيقاف تنبيهات الاستهلاك الذكية وإشعارات العروض والخدمات من خلال صفحة إعدادات الإشعارات ومركز الاستهلاك الذكي.",
                    "You can customize or disable smart usage alerts and promotional notifications at any time via Notification Preferences and the Smart Usage Center."
                )
            )
        )
    } else {
        listOf(
            Pair(
                BiText("1. شروط استخدام النموذج التفاعلي", "1. Interactive Prototype Terms"),
                BiText(
                    "تم تصميم هذا التطبيق لاستعراض الجيل الجديد من خدمات المشتركين الرقمية، بما في ذلك مركز الاستهلاك الذكي، ومركز العمليات، وإدارة الباقات والخدمات.",
                    "This application is designed to showcase next-generation digital subscriber services, including the Smart Usage Center, Operations Center, and bundle/service management."
                )
            ),
            Pair(
                BiText("2. العمليات التجريبية والأرصدة", "2. Demonstration Operations & Balances"),
                BiText(
                    "جميع الأرصدة المعروضة بالليرة السورية، وعمليات شحن الرصيد، والاشتراك بالباقات، وتفعيل الخدمات هي عمليات محاكاة توضيحية فقط ولا يترتب عليها أي التزام مالي أو تعاقدي فعلي.",
                    "All displayed SYP balances, recharges, package subscriptions, and service activations are illustrative simulations only and carry no real financial or contractual obligation."
                )
            ),
            Pair(
                BiText("3. الهوية البصرية وحقوق الملكية", "3. Visual Identity & Trademarks"),
                BiText(
                    "تم استخدام شعار سيريتل وألوان الهوية البصرية الرسمية ضمن هذا النموذج التفاعلي لأغراض التصميم والعرض التقني فقط.",
                    "The SyriaTel logo and official brand color palette are used within this interactive prototype strictly for design and technical demonstration purposes."
                )
            )
        )
    }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        SyriaTelSubScreenTopBar(
            title = title,
            subtitle = if (isArabic) "مستند تنظيمي وتوضيحي" else "Regulatory & informational document",
            onBack = onBack
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {
            sections.forEach { (heading, body) ->
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text(
                            text = heading.get(isArabic),
                            style = MaterialTheme.typography.titleMedium.copy(
                                color = SyriatelRed,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = body.get(isArabic),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}
