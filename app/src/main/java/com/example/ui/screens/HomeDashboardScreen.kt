package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.*
import com.example.ui.components.*
import com.example.ui.theme.SyriatelDarkRed
import com.example.ui.theme.SyriatelGreen
import com.example.ui.theme.SyriatelRed
import com.example.viewmodel.SyriatelUiState
import kotlinx.coroutines.delay
import java.text.NumberFormat
import java.util.Locale

@Composable
fun HomeDashboardScreen(
    uiState: SyriatelUiState,
    onOpenProfile: () -> Unit,
    onOpenNotifications: () -> Unit,
    onOpenSettings: () -> Unit,
    onOpenRecharge: () -> Unit,
    onOpenKnowMyNumber: () -> Unit,
    onOpenOffersCategory: (OfferCategory) -> Unit,
    onOpenServices: () -> Unit,
    onOpenSmartUsageCenter: () -> Unit,
    onOpenOperationsCenter: () -> Unit,
    onToggleBalanceVisibility: () -> Unit
) {
    val isArabic = uiState.isArabic
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(scrollState)
            .padding(bottom = 24.dp)
            .testTag("home_dashboard_screen")
    ) {
        // A. TOP HEADER
        HomeTopHeader(
            userProfile = uiState.userProfile,
            isArabic = isArabic,
            isDarkMode = uiState.isDarkMode,
            unreadNotifications = uiState.unreadNotificationsCount,
            onProfileClick = onOpenProfile,
            onNotificationsClick = onOpenNotifications,
            onSettingsClick = onOpenSettings
        )

        Spacer(modifier = Modifier.height(12.dp))

        // B. ADVERTISEMENT BANNER CAROUSEL
        PromoBannerCarousel(
            banners = SyriaTelSampleData.promoBanners,
            isArabic = isArabic,
            onBannerAction = { category -> onOpenOffersCategory(category) }
        )

        Spacer(modifier = Modifier.height(18.dp))

        // C. ACCOUNT BALANCE CARD
        AccountBalanceCard(
            formattedBalance = uiState.formattedBalance(isArabic),
            expiryDate = uiState.balanceExpiryDate,
            isBalanceVisible = uiState.isBalanceVisible,
            isArabic = isArabic,
            onToggleVisibility = onToggleBalanceVisibility,
            onRechargeClick = onOpenRecharge
        )

        Spacer(modifier = Modifier.height(22.dp))

        // D. ACTIVE PACKAGE USAGE
        ActivePackageUsageSection(
            uiState = uiState,
            onViewDetails = onOpenSmartUsageCenter
        )

        Spacer(modifier = Modifier.height(22.dp))

        // E. QUICK ACTIONS — SMART SHORTCUTS
        QuickActionsGridSection(
            isArabic = isArabic,
            onRecharge = onOpenRecharge,
            onKnowMyNumber = onOpenKnowMyNumber,
            onActivatePackages = { onOpenOffersCategory(OfferCategory.INTERNET) },
            onManageServices = onOpenServices
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Operations Center Quick Access Card
        Card(
            onClick = onOpenOperationsCenter,
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .testTag("home_operations_center_card")
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(46.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(SyriatelRed.copy(alpha = 0.1f))
                ) {
                    Icon(
                        imageVector = Icons.Filled.History,
                        contentDescription = null,
                        tint = SyriatelRed
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = if (isArabic) "مركز العمليات وسجل النشاط" else "Operations Center & Activity Log",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = if (isArabic) {
                            "${uiState.transactions.size} عملية مسجلة • تابع جميع عملياتك التجريبية"
                        } else {
                            "${uiState.transactions.size} logged operations • Track your line activity"
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Composable
private fun HomeTopHeader(
    userProfile: UserProfile,
    isArabic: Boolean,
    isDarkMode: Boolean,
    unreadNotifications: Int,
    onProfileClick: () -> Unit,
    onNotificationsClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 1.dp,
        shadowElevation = 2.dp
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            // Start side (Right in Arabic RTL): Profile Avatar + Greeting + Subtitle
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .weight(1.15f)
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { onProfileClick() }
                    .padding(vertical = 2.dp)
                    .testTag("header_profile_action")
            ) {
                UserAvatarCircle(
                    name = userProfile.displayFirstName(isArabic),
                    colorIndex = userProfile.avatarColorIndex,
                    size = 44.dp
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = if (isArabic) {
                            "أهلاً ${userProfile.displayFirstName(true)}"
                        } else {
                            "Hello ${userProfile.displayFirstName(false)}"
                        },
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = if (isArabic) "يسعدنا وجودك معنا" else "Glad to have you with us",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            // Center: Original isolated SyriaTel logo
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.padding(horizontal = 4.dp)
            ) {
                SyriaTelBrandLogo(
                    height = 32.dp,
                    isDarkMode = isDarkMode
                )
            }

            // End side (Left in Arabic RTL): Notification Bell + Settings Gear
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.weight(0.95f)
            ) {
                IconButton(
                    onClick = onNotificationsClick,
                    modifier = Modifier
                        .size(48.dp)
                        .testTag("header_notifications_button")
                ) {
                    BadgedBox(
                        badge = {
                            if (unreadNotifications > 0) {
                                Badge(
                                    containerColor = SyriatelRed,
                                    contentColor = Color.White
                                ) {
                                    Text(unreadNotifications.toString())
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Notifications,
                            contentDescription = if (isArabic) "الإشعارات" else "Notifications",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                IconButton(
                    onClick = onSettingsClick,
                    modifier = Modifier
                        .size(48.dp)
                        .testTag("header_settings_button")
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Settings,
                        contentDescription = if (isArabic) "الإعدادات" else "Settings",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

@Composable
private fun PromoBannerCarousel(
    banners: List<PromoBanner>,
    isArabic: Boolean,
    onBannerAction: (OfferCategory) -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { banners.size })

    LaunchedEffect(pagerState.currentPage) {
        delay(4500L)
        val next = (pagerState.currentPage + 1) % banners.size
        pagerState.animateScrollToPage(next)
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        HorizontalPager(
            state = pagerState,
            contentPadding = PaddingValues(horizontal = 16.dp),
            pageSpacing = 12.dp,
            modifier = Modifier
                .fillMaxWidth()
                .height(176.dp)
        ) { page ->
            val banner = banners[page]
            Card(
                onClick = { onBannerAction(banner.targetCategory) },
                shape = RoundedCornerShape(22.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("promo_banner_$page")
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.linearGradient(
                                colors = listOf(
                                    Color(banner.accentHex),
                                    Color(banner.secondaryHex)
                                )
                            )
                        )
                ) {
                    // Decorative geometric circles
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        drawCircle(
                            color = Color.White.copy(alpha = 0.08f),
                            radius = size.height * 0.65f,
                            center = Offset(size.width * 0.15f, size.height * 0.2f)
                        )
                        drawCircle(
                            color = Color.White.copy(alpha = 0.06f),
                            radius = size.height * 0.45f,
                            center = Offset(size.width * 0.85f, size.height * 0.85f)
                        )
                    }

                    Column(
                        verticalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(18.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Surface(
                                color = Color.White.copy(alpha = 0.22f),
                                shape = RoundedCornerShape(50)
                            ) {
                                Text(
                                    text = banner.badge.get(isArabic),
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                                )
                            }

                            Icon(
                                imageVector = Icons.Filled.CellTower,
                                contentDescription = null,
                                tint = Color.White.copy(alpha = 0.7f),
                                modifier = Modifier.size(22.dp)
                            )
                        }

                        Column {
                            Text(
                                text = banner.title.get(isArabic),
                                style = MaterialTheme.typography.titleLarge.copy(
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                ),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = banner.description.get(isArabic),
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = Color.White.copy(alpha = 0.9f)
                                ),
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                color = Color.White,
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text = banner.ctaText.get(isArabic),
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            color = Color(banner.accentHex),
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Pagination Dots
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            banners.forEachIndexed { index, _ ->
                val isSelected = pagerState.currentPage == index
                Box(
                    modifier = Modifier
                        .padding(horizontal = 3.dp)
                        .height(6.dp)
                        .width(if (isSelected) 22.dp else 6.dp)
                        .clip(CircleShape)
                        .background(
                            if (isSelected) SyriatelRed
                            else MaterialTheme.colorScheme.outlineVariant
                        )
                )
            }
        }
    }
}

@Composable
private fun AccountBalanceCard(
    formattedBalance: String,
    expiryDate: String,
    isBalanceVisible: Boolean,
    isArabic: Boolean,
    onToggleVisibility: () -> Unit,
    onRechargeClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .testTag("account_balance_card")
    ) {
        Column(
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
                            .size(40.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(SyriatelGreen.copy(alpha = 0.12f))
                    ) {
                        Icon(
                            imageVector = Icons.Filled.AccountBalanceWallet,
                            contentDescription = null,
                            tint = SyriatelGreen,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = if (isArabic) "الرصيد المتوفر" else "Available Balance",
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(7.dp)
                                    .clip(CircleShape)
                                    .background(SyriatelGreen)
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = if (isArabic) "الخط نشط" else "Line Active",
                                style = MaterialTheme.typography.labelSmall,
                                color = SyriatelGreen
                            )
                        }
                    }
                }

                IconButton(
                    onClick = onToggleVisibility,
                    modifier = Modifier
                        .size(48.dp)
                        .testTag("balance_visibility_toggle")
                ) {
                    Icon(
                        imageVector = if (isBalanceVisible) Icons.Outlined.Visibility else Icons.Outlined.VisibilityOff,
                        contentDescription = if (isArabic) "إظهار أو إخفاء الرصيد" else "Toggle balance visibility",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Text(
                        text = if (isBalanceVisible) formattedBalance else "•••••• ل.س",
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (isArabic) {
                            "تاريخ انتهاء الصلاحية: $expiryDate"
                        } else {
                            "Expiration Date: $expiryDate"
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Button(
                    onClick = onRechargeClick,
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = SyriatelRed),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
                    modifier = Modifier
                        .height(48.dp)
                        .testTag("balance_card_recharge_button")
                ) {
                    Icon(
                        imageVector = Icons.Filled.AddCard,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isArabic) "شحن الرصيد" else "Recharge",
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

@Composable
private fun ActivePackageUsageSection(
    uiState: SyriatelUiState,
    onViewDetails: () -> Unit
) {
    val isArabic = uiState.isArabic

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = if (isArabic) "استهلاك باقاتك" else "Your Package Usage",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
            )

            TextButton(
                onClick = onViewDetails,
                modifier = Modifier.testTag("open_smart_usage_center_button")
            ) {
                Text(
                    text = if (isArabic) "عرض التفاصيل" else "View Details",
                    style = MaterialTheme.typography.labelLarge.copy(
                        color = SyriatelRed,
                        fontWeight = FontWeight.Bold
                    )
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                    contentDescription = null,
                    tint = SyriatelRed,
                    modifier = Modifier.size(14.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            // 1. INTERNET CARD
            PackageUsageItemCard(
                title = if (isArabic) "باقة الإنترنت" else "Internet Package",
                remainingLabel = if (isArabic) {
                    "${String.format(Locale.US, "%.1f", uiState.internetRemainingGb)} GB متبقي"
                } else {
                    "${String.format(Locale.US, "%.1f", uiState.internetRemainingGb)} GB left"
                },
                usedAndTotalLabel = if (isArabic) {
                    "المستهلك: ${String.format(Locale.US, "%.1f", uiState.internetUsedGb)} GB من أصل ${String.format(Locale.US, "%.0f", uiState.internetTotalGb)} GB"
                } else {
                    "Used: ${String.format(Locale.US, "%.1f", uiState.internetUsedGb)} GB of ${String.format(Locale.US, "%.0f", uiState.internetTotalGb)} GB"
                },
                fractionRemaining = uiState.internetRemainingFraction,
                accentColor = SyriatelRed,
                icon = Icons.Filled.Public,
                onClick = onViewDetails
            )

            // 2. CALL MINUTES CARD
            PackageUsageItemCard(
                title = if (isArabic) "دقائق الاتصال" else "Call Minutes",
                remainingLabel = if (isArabic) {
                    "${uiState.minutesRemaining} دقيقة متبقية"
                } else {
                    "${uiState.minutesRemaining} mins left"
                },
                usedAndTotalLabel = if (isArabic) {
                    "المستهلك: ${uiState.minutesUsed} دقيقة من أصل ${uiState.minutesTotal} دقيقة"
                } else {
                    "Used: ${uiState.minutesUsed} mins of ${uiState.minutesTotal} mins"
                },
                fractionRemaining = uiState.minutesRemainingFraction,
                accentColor = SyriatelGreen,
                icon = Icons.Filled.PhoneInTalk,
                onClick = onViewDetails
            )

            // 3. SMS MESSAGES CARD
            PackageUsageItemCard(
                title = if (isArabic) "الرسائل النصية SMS" else "SMS Messages",
                remainingLabel = if (isArabic) {
                    "${uiState.smsRemaining} رسالة متبقية"
                } else {
                    "${uiState.smsRemaining} SMS left"
                },
                usedAndTotalLabel = if (isArabic) {
                    "المستهلك: ${uiState.smsUsed} رسالة من أصل ${uiState.smsTotal} رسالة"
                } else {
                    "Used: ${uiState.smsUsed} SMS of ${uiState.smsTotal} SMS"
                },
                fractionRemaining = uiState.smsRemainingFraction,
                accentColor = Color(0xFF0F6CBD),
                icon = Icons.Filled.Sms,
                onClick = onViewDetails
            )
        }
    }
}

@Composable
private fun PackageUsageItemCard(
    title: String,
    remainingLabel: String,
    usedAndTotalLabel: String,
    fractionRemaining: Float,
    accentColor: Color,
    icon: ImageVector,
    onClick: () -> Unit
) {
    val pctText = "${(fractionRemaining * 100).toInt()}%"

    Card(
        onClick = onClick,
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            UsageCircularIndicator(
                progress = fractionRemaining,
                color = accentColor,
                size = 58.dp,
                strokeWidth = 6.dp,
                centerLabel = pctText
            )

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = accentColor,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = remainingLabel,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = usedAndTotalLabel,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun QuickActionsGridSection(
    isArabic: Boolean,
    onRecharge: () -> Unit,
    onKnowMyNumber: () -> Unit,
    onActivatePackages: () -> Unit,
    onManageServices: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = if (isArabic) "الوضع السريع" else "Quick Actions",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = if (isArabic) "خدماتك الأساسية بلمسة واحدة" else "Your essential services in one tap",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            QuickActionShortcutCard(
                title = if (isArabic) "شحن الرصيد" else "Recharge Balance",
                subtitle = if (isArabic) "تعبئة فورية" else "Instant top-up",
                icon = Icons.Filled.CreditCard,
                accentColor = SyriatelRed,
                testTag = "quick_action_recharge",
                onClick = onRecharge,
                modifier = Modifier.weight(1f)
            )
            QuickActionShortcutCard(
                title = if (isArabic) "معرفة رقمي" else "Know My Number",
                subtitle = if (isArabic) "عرض ونسخ الرقم" else "View & copy SIM",
                icon = Icons.Filled.SimCard,
                accentColor = SyriatelGreen,
                testTag = "quick_action_know_number",
                onClick = onKnowMyNumber,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            QuickActionShortcutCard(
                title = if (isArabic) "تفعيل الباقات" else "Activate Packages",
                subtitle = if (isArabic) "إنترنت وميكس" else "Data & bundles",
                icon = Icons.Filled.Bolt,
                accentColor = SyriatelDarkRed,
                testTag = "quick_action_packages",
                onClick = onActivatePackages,
                modifier = Modifier.weight(1f)
            )
            QuickActionShortcutCard(
                title = if (isArabic) "إدارة الخدمات" else "Manage Services",
                subtitle = if (isArabic) "تفعيل وإيقاف" else "Toggle services",
                icon = Icons.Filled.Tune,
                accentColor = Color(0xFF0F6CBD),
                testTag = "quick_action_services",
                onClick = onManageServices,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun QuickActionShortcutCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    accentColor: Color,
    testTag: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier.testTag(testTag)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(accentColor.copy(alpha = 0.12f))
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = accentColor,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun RechargeBalanceScreen(
    uiState: SyriatelUiState,
    onBack: () -> Unit,
    onConfirmRecharge: (String, Int) -> Unit
) {
    val isArabic = uiState.isArabic
    var phone by remember { mutableStateOf(uiState.userProfile.phoneNumber) }
    val presetAmounts = listOf(5000, 10000, 25000, 50000)
    var selectedAmount by remember { mutableIntStateOf(10000) }
    var customAmountText by remember { mutableStateOf("") }
    var showConfirmDialog by remember { mutableStateOf(false) }
    var showSuccessBanner by remember { mutableStateOf(false) }
    var errorText by remember { mutableStateOf<String?>(null) }

    val effectiveAmount = customAmountText.toIntOrNull() ?: selectedAmount
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        SyriaTelSubScreenTopBar(
            title = if (isArabic) "شحن الرصيد (تجريبي)" else "Recharge Balance (Demo)",
            subtitle = if (isArabic) "عملية شحن توضيحية فورية" else "Simulated instant balance top-up",
            onBack = onBack
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {
            Surface(
                color = SyriatelRed.copy(alpha = 0.08f),
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.dp, SyriatelRed.copy(alpha = 0.25f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Info,
                        contentDescription = null,
                        tint = SyriatelRed,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = if (isArabic) {
                            "هذه العملية تجريبية (Prototype) لتوضيح تجربة شحن الرصيد وتحديث مركز العمليات تلقائياً."
                        } else {
                            "This is a demonstration operation to showcase balance top-up and automatic Operations Center logging."
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = if (isArabic) "الرصيد الحالي المتوفر" else "Current Available Balance",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = uiState.formattedBalance(isArabic),
                        style = MaterialTheme.typography.headlineMedium.copy(
                            color = SyriatelGreen,
                            fontWeight = FontWeight.Bold
                        )
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    Text(
                        text = if (isArabic) "رقم الهاتف المراد شحنه" else "Target Phone Number",
                        style = MaterialTheme.typography.labelLarge
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = phone,
                        onValueChange = {
                            if (it.length <= 10 && it.all { ch -> ch.isDigit() }) {
                                phone = it
                                errorText = null
                            }
                        },
                        leadingIcon = {
                            Icon(Icons.Filled.PhoneAndroid, contentDescription = null, tint = SyriatelRed)
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("recharge_phone_input")
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    Text(
                        text = if (isArabic) "اختر قيمة الشحن المقترحة (ل.س)" else "Select Suggested Amount (SYP)",
                        style = MaterialTheme.typography.labelLarge
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        presetAmounts.forEach { amt ->
                            val isSelected = customAmountText.isEmpty() && selectedAmount == amt
                            val formattedAmt = NumberFormat.getNumberInstance(Locale.US).format(amt)
                            Surface(
                                onClick = {
                                    selectedAmount = amt
                                    customAmountText = ""
                                    errorText = null
                                },
                                color = if (isSelected) SyriatelRed else MaterialTheme.colorScheme.surfaceVariant,
                                shape = RoundedCornerShape(12.dp),
                                border = BorderStroke(
                                    1.dp,
                                    if (isSelected) SyriatelRed else MaterialTheme.colorScheme.outline
                                ),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = formattedAmt,
                                        style = MaterialTheme.typography.labelLarge.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                                        )
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = if (isArabic) "أو أدخل مبلغاً مخصصاً (ل.س)" else "Or Enter Custom Amount (SYP)",
                        style = MaterialTheme.typography.labelLarge
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = customAmountText,
                        onValueChange = {
                            if (it.length <= 7 && it.all { ch -> ch.isDigit() }) {
                                customAmountText = it
                                errorText = null
                            }
                        },
                        placeholder = { Text(if (isArabic) "مثال: 15000" else "e.g. 15000") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("recharge_custom_amount_input")
                    )

                    if (errorText != null) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = errorText.orEmpty(),
                            style = MaterialTheme.typography.bodySmall,
                            color = SyriatelRed
                        )
                    }

                    Spacer(modifier = Modifier.height(22.dp))

                    Button(
                        onClick = {
                            when {
                                !phone.matches(Regex("^09\\d{8}$")) -> {
                                    errorText = if (isArabic) {
                                        "يرجى إدخال رقم موبايل سوري صحيح يبدأ بـ 09"
                                    } else {
                                        "Please enter a valid Syrian mobile number starting with 09"
                                    }
                                }
                                effectiveAmount < 1000 -> {
                                    errorText = if (isArabic) {
                                        "الحد الأدنى للشحن التجريبي هو 1,000 ل.س"
                                    } else {
                                        "Minimum demo recharge amount is 1,000 SYP"
                                    }
                                }
                                else -> {
                                    showConfirmDialog = true
                                }
                            }
                        },
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SyriatelRed),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("confirm_recharge_button")
                    ) {
                        Icon(Icons.Filled.Bolt, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isArabic) {
                                "متابعة شحن ${NumberFormat.getNumberInstance(Locale.US).format(effectiveAmount)} ل.س"
                            } else {
                                "Proceed with ${NumberFormat.getNumberInstance(Locale.US).format(effectiveAmount)} SYP Recharge"
                            },
                            style = MaterialTheme.typography.titleMedium.copy(
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            }

            AnimatedVisibility(visible = showSuccessBanner) {
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = SyriatelGreen.copy(alpha = 0.12f)),
                    border = BorderStroke(1.dp, SyriatelGreen),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Filled.CheckCircle,
                                contentDescription = null,
                                tint = SyriatelGreen
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isArabic) "تمت عملية الشحن التجريبية بنجاح!" else "Demo Recharge Completed Successfully!",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    color = SyriatelGreen,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = if (isArabic) {
                                "تم تحديث رصيدك المتوفر وإضافة العملية إلى مركز العمليات."
                            } else {
                                "Your available balance has been updated and the record was added to the Operations Center."
                            },
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }

    if (showConfirmDialog) {
        val formattedAmt = NumberFormat.getNumberInstance(Locale.US).format(effectiveAmount)
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            title = {
                Text(
                    text = if (isArabic) "تأكيد شحن الرصيد التجريبي" else "Confirm Demo Recharge",
                    style = MaterialTheme.typography.titleLarge
                )
            },
            text = {
                Text(
                    text = if (isArabic) {
                        "هل ترغب في شحن الرقم $phone بمبلغ $formattedAmt ل.س؟ (عملية محاكاة تجريبية)"
                    } else {
                        "Do you want to recharge $phone with $formattedAmt SYP? (Simulated demo operation)"
                    }
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showConfirmDialog = false
                        onConfirmRecharge(phone, effectiveAmount)
                        showSuccessBanner = true
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SyriatelRed)
                ) {
                    Text(if (isArabic) "تأكيد الشحن" else "Confirm")
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmDialog = false }) {
                    Text(if (isArabic) "إلغاء" else "Cancel")
                }
            }
        )
    }
}
