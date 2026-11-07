package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.model.*
import com.example.ui.theme.SyriatelDarkRed
import com.example.ui.theme.SyriatelGreen
import com.example.ui.theme.SyriatelRed
import com.example.viewmodel.SyriatelUiState
import java.text.NumberFormat
import java.util.Locale

// ==========================================
// SCREEN 4 — OFFERS
// ==========================================
@Composable
fun OffersScreen(
    uiState: SyriatelUiState,
    onSelectCategory: (OfferCategory) -> Unit,
    onSubscribeOffer: (OfferItem) -> Unit
) {
    val isArabic = uiState.isArabic
    val selectedCat = uiState.selectedOfferCategory
    val filteredOffers = remember(uiState.offers, selectedCat) {
        uiState.offers.filter { it.category == selectedCat }
    }
    var selectedOfferForDetail by remember { mutableStateOf<OfferItem?>(null) }

    val scrollState = rememberScrollState()
    val categoryScrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("offers_screen")
    ) {
        // Top Header
        Surface(
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 1.dp,
            shadowElevation = 2.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(top = 14.dp, bottom = 12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Column {
                        Text(
                            text = if (isArabic) "العروض" else "Offers",
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = if (isArabic) "اختر الباقة الأنسب لاحتياجاتك اليومية" else "Choose the best package for your daily needs",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Surface(
                        color = SyriatelGreen.copy(alpha = 0.12f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = uiState.formattedBalance(isArabic),
                            style = MaterialTheme.typography.labelLarge.copy(
                                color = SyriatelGreen,
                                fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Horizontally scrollable category selector
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(categoryScrollState)
                        .padding(horizontal = 16.dp)
                ) {
                    OfferCategory.entries.forEach { category ->
                        val isSelected = category == selectedCat
                        FilterChip(
                            selected = isSelected,
                            onClick = { onSelectCategory(category) },
                            label = {
                                Text(
                                    text = category.title.get(isArabic),
                                    style = MaterialTheme.typography.labelLarge.copy(
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                    )
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = categoryIcon(category),
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = SyriatelRed,
                                selectedLabelColor = Color.White,
                                selectedLeadingIconColor = Color.White
                            ),
                            shape = RoundedCornerShape(50),
                            modifier = Modifier.testTag("offer_cat_${category.name.lowercase()}")
                        )
                    }
                }
            }
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {
            filteredOffers.forEach { offer ->
                OfferCardItem(
                    offer = offer,
                    isArabic = isArabic,
                    onClickDetails = { selectedOfferForDetail = offer }
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
        }
    }

    selectedOfferForDetail?.let { offer ->
        OfferDetailsDialog(
            offer = offer,
            isArabic = isArabic,
            onDismiss = { selectedOfferForDetail = null },
            onConfirmSubscribe = {
                onSubscribeOffer(offer)
                selectedOfferForDetail = null
            }
        )
    }
}

private fun categoryIcon(category: OfferCategory): ImageVector = when (category) {
    OfferCategory.INTERNET -> Icons.Filled.Public
    OfferCategory.SMS -> Icons.Filled.Sms
    OfferCategory.MINUTES -> Icons.Filled.PhoneInTalk
    OfferCategory.MIX -> Icons.Filled.AutoAwesomeMosaic
    OfferCategory.SPECIAL -> Icons.Filled.Star
    OfferCategory.ANNIVERSARY -> Icons.Filled.Cake
}

@Composable
private fun OfferCardItem(
    offer: OfferItem,
    isArabic: Boolean,
    onClickDetails: () -> Unit
) {
    val formattedPrice = if (offer.priceSyp == 0) {
        if (isArabic) "مجاناً" else "Free"
    } else {
        val num = NumberFormat.getNumberInstance(Locale.US).format(offer.priceSyp)
        if (isArabic) "$num ل.س" else "$num SYP"
    }

    Card(
        onClick = onClickDetails,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("offer_card_${offer.id}")
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Promotional visual header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.horizontalGradient(
                            colors = when (offer.category) {
                                OfferCategory.INTERNET -> listOf(SyriatelRed, SyriatelDarkRed)
                                OfferCategory.MIX -> listOf(SyriatelGreen, Color(0xFF0E7446))
                                OfferCategory.ANNIVERSARY -> listOf(Color(0xFF2B313B), SyriatelRed)
                                else -> listOf(Color(0xFFB81219), Color(0xFF3E4552))
                            }
                        )
                    )
                    .padding(horizontal = 16.dp, vertical = 14.dp)
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
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.2f))
                        ) {
                            Icon(
                                imageVector = categoryIcon(offer.category),
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = offer.name.get(isArabic),
                                style = MaterialTheme.typography.titleMedium.copy(
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Text(
                                text = offer.quotaLabel.get(isArabic),
                                style = MaterialTheme.typography.labelMedium.copy(
                                    color = Color.White.copy(alpha = 0.9f)
                                )
                            )
                        }
                    }

                    Surface(
                        color = Color.White.copy(alpha = 0.22f),
                        shape = RoundedCornerShape(50)
                    ) {
                        Text(
                            text = offer.highlightTag.get(isArabic),
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = offer.description.get(isArabic),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        Text(
                            text = formattedPrice,
                            style = MaterialTheme.typography.titleLarge.copy(
                                color = SyriatelRed,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Text(
                            text = if (isArabic) "الصلاحية: ${offer.validity.ar}" else "Validity: ${offer.validity.en}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedButton(
                            onClick = onClickDetails,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.height(44.dp)
                        ) {
                            Text(text = if (isArabic) "التفاصيل" else "Details")
                        }

                        Button(
                            onClick = onClickDetails,
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = SyriatelRed),
                            modifier = Modifier.height(44.dp)
                        ) {
                            Text(
                                text = if (isArabic) "اشترك الآن" else "Subscribe",
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun OfferDetailsDialog(
    offer: OfferItem,
    isArabic: Boolean,
    onDismiss: () -> Unit,
    onConfirmSubscribe: () -> Unit
) {
    val formattedPrice = if (offer.priceSyp == 0) {
        if (isArabic) "مجاناً" else "Free"
    } else {
        val num = NumberFormat.getNumberInstance(Locale.US).format(offer.priceSyp)
        if (isArabic) "$num ل.س" else "$num SYP"
    }

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
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Surface(
                        color = SyriatelRed.copy(alpha = 0.12f),
                        shape = RoundedCornerShape(50)
                    ) {
                        Text(
                            text = offer.category.title.get(isArabic),
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = SyriatelRed,
                                fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                        )
                    }

                    Text(
                        text = if (isArabic) "عرض تجريبي" else "Demo Offer",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = offer.name.get(isArabic),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = offer.description.get(isArabic),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(16.dp))

                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(14.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = if (isArabic) "مزايا الباقة:" else "Benefits:",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = offer.quotaLabel.get(isArabic),
                                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = if (isArabic) "مدة الصلاحية:" else "Validity:",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = offer.validity.get(isArabic),
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = if (isArabic) "التكلفة التجريبية:" else "Demo Price:",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = formattedPrice,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    color = SyriatelRed,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = if (isArabic) {
                        "ملاحظة: عند الضغط على تأكيد الاشتراك، سيتم تحديث رصيد باقاتك التجريبي وتسجيل العملية في مركز العمليات."
                    } else {
                        "Note: Confirming this subscription updates your demo package balances and logs the operation in the Operations Center."
                    },
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(18.dp))

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
                        onClick = onConfirmSubscribe,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SyriatelRed),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("confirm_subscribe_offer_button")
                    ) {
                        Text(
                            text = if (isArabic) "تأكيد الاشتراك" else "Confirm",
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
// SCREEN 5 — SERVICES
// ==========================================
@Composable
fun ServicesScreen(
    uiState: SyriatelUiState,
    onToggleSection: (ServiceSectionType) -> Unit,
    onToggleService: (String) -> Unit
) {
    val isArabic = uiState.isArabic
    val basicServices = remember(uiState.services) {
        uiState.services.filter { it.section == ServiceSectionType.BASIC }
    }
    val varietyServices = remember(uiState.services) {
        uiState.services.filter { it.section == ServiceSectionType.VARIETY }
    }
    var pendingServiceToggle by remember { mutableStateOf<ServiceItem?>(null) }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("services_screen")
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
                    .padding(horizontal = 16.dp, vertical = 14.dp)
            ) {
                Column {
                    Text(
                        text = if (isArabic) "الخدمات" else "Services",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = if (isArabic) "تحكم بتفعيل أو إيقاف خدمات خطك بسهولة" else "Easily activate or deactivate line services",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Surface(
                    color = SyriatelGreen.copy(alpha = 0.12f),
                    shape = RoundedCornerShape(50)
                ) {
                    Text(
                        text = if (isArabic) {
                            "${uiState.activeServicesCount} مفعّلة"
                        } else {
                            "${uiState.activeServicesCount} Active"
                        },
                        style = MaterialTheme.typography.labelLarge.copy(
                            color = SyriatelGreen,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {
            // SECTION A — الخدمات الأساسية
            ServiceAccordionSection(
                sectionType = ServiceSectionType.BASIC,
                services = basicServices,
                isExpanded = uiState.isBasicServicesExpanded,
                isArabic = isArabic,
                onHeaderClick = { onToggleSection(ServiceSectionType.BASIC) },
                onRequestToggleService = { pendingServiceToggle = it }
            )

            // SECTION B — خدمات منوعة
            ServiceAccordionSection(
                sectionType = ServiceSectionType.VARIETY,
                services = varietyServices,
                isExpanded = uiState.isVarietyServicesExpanded,
                isArabic = isArabic,
                onHeaderClick = { onToggleSection(ServiceSectionType.VARIETY) },
                onRequestToggleService = { pendingServiceToggle = it }
            )

            Spacer(modifier = Modifier.height(12.dp))
        }
    }

    pendingServiceToggle?.let { srv ->
        val nextState = !srv.isActive
        AlertDialog(
            onDismissRequest = { pendingServiceToggle = null },
            title = {
                Text(
                    text = if (nextState) {
                        if (isArabic) "تأكيد تفعيل الخدمة" else "Confirm Service Activation"
                    } else {
                        if (isArabic) "تأكيد إيقاف الخدمة" else "Confirm Service Deactivation"
                    },
                    style = MaterialTheme.typography.titleLarge
                )
            },
            text = {
                Text(
                    text = if (nextState) {
                        if (isArabic) {
                            "هل ترغب في تفعيل '${srv.name.ar}'؟ (${srv.monthlyFee.ar}) — سيتم تسجيل العملية في مركز العمليات."
                        } else {
                            "Do you want to activate '${srv.name.en}'? (${srv.monthlyFee.en}) — This action will be recorded in the Operations Center."
                        }
                    } else {
                        if (isArabic) {
                            "هل أنت متأكد من إيقاف '${srv.name.ar}' على خطك التجريبي؟"
                        } else {
                            "Are you sure you want to deactivate '${srv.name.en}' on your demo line?"
                        }
                    }
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onToggleService(srv.id)
                        pendingServiceToggle = null
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (nextState) SyriatelGreen else SyriatelRed
                    ),
                    modifier = Modifier.testTag("confirm_service_toggle_button")
                ) {
                    Text(
                        text = if (nextState) {
                            if (isArabic) "تفعيل" else "Activate"
                        } else {
                            if (isArabic) "إيقاف" else "Deactivate"
                        },
                        color = Color.White
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { pendingServiceToggle = null }) {
                    Text(if (isArabic) "إلغاء" else "Cancel")
                }
            }
        )
    }
}

@Composable
private fun ServiceAccordionSection(
    sectionType: ServiceSectionType,
    services: List<ServiceItem>,
    isExpanded: Boolean,
    isArabic: Boolean,
    onHeaderClick: () -> Unit,
    onRequestToggleService: (ServiceItem) -> Unit
) {
    val activeCount = services.count { it.isActive }

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Accordion Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onHeaderClick() }
                    .padding(18.dp)
                    .testTag("accordion_header_${sectionType.name.lowercase()}")
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(42.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(SyriatelRed.copy(alpha = 0.1f))
                    ) {
                        Icon(
                            imageVector = if (sectionType == ServiceSectionType.BASIC) {
                                Icons.Filled.SettingsCell
                            } else {
                                Icons.Filled.Widgets
                            },
                            contentDescription = null,
                            tint = SyriatelRed
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = sectionType.title.get(isArabic),
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = sectionType.subtitle.get(isArabic),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(50)
                    ) {
                        Text(
                            text = "$activeCount/${services.size}",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = if (isExpanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 14.dp, end = 14.dp, bottom = 14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline)
                    services.forEach { srv ->
                        ServiceRowCard(
                            service = srv,
                            isArabic = isArabic,
                            onToggle = { onRequestToggleService(srv) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ServiceRowCard(
    service: ServiceItem,
    isArabic: Boolean,
    onToggle: () -> Unit
) {
    val icon = when (service.iconKey) {
        "wifi" -> Icons.Filled.SettingsInputAntenna
        "calendar" -> Icons.Filled.EventAvailable
        "block" -> Icons.Filled.DoNotDisturbOn
        "music" -> Icons.Filled.MusicNote
        "phone_missed" -> Icons.Filled.PhoneMissed
        "newspaper" -> Icons.Filled.Newspaper
        "memory" -> Icons.Filled.Memory
        "sports" -> Icons.Filled.SportsSoccer
        else -> Icons.Filled.AutoAwesome
    }

    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(
            width = 1.dp,
            color = if (service.isActive) SyriatelGreen.copy(alpha = 0.35f) else MaterialTheme.colorScheme.outline
        ),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("service_item_${service.id}")
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
                    .size(42.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        if (service.isActive) SyriatelGreen.copy(alpha = 0.14f)
                        else MaterialTheme.colorScheme.surface
                    )
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = if (service.isActive) SyriatelGreen else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = service.name.get(isArabic),
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Spacer(modifier = Modifier.height(3.dp))

                Text(
                    text = service.description.get(isArabic),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Status Indicator Pill
                    Surface(
                        color = if (service.isActive) SyriatelGreen.copy(alpha = 0.15f)
                        else MaterialTheme.colorScheme.outline,
                        shape = RoundedCornerShape(50)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (service.isActive) SyriatelGreen
                                        else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = if (service.isActive) {
                                    if (isArabic) "مفعّلة" else "Active"
                                } else {
                                    if (isArabic) "غير مفعّلة" else "Inactive"
                                },
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = if (service.isActive) SyriatelGreen
                                    else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            )
                        }
                    }

                    Text(
                        text = service.monthlyFee.get(isArabic),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            Switch(
                checked = service.isActive,
                onCheckedChange = { onToggle() },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = SyriatelGreen,
                    uncheckedThumbColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    uncheckedTrackColor = MaterialTheme.colorScheme.surface
                ),
                modifier = Modifier.testTag("service_switch_${service.id}")
            )
        }
    }
}

// ==========================================
// SCREEN 6 — SYRIATEL CASH
// ==========================================
@Composable
fun SyriatelCashScreen(
    uiState: SyriatelUiState,
    onDemoCashAction: (Boolean, String, String) -> Unit
) {
    val isArabic = uiState.isArabic
    var activeModalMode by remember { mutableStateOf<String?>(null) } // "register" or "login"
    var demoSubmittedMessage by remember { mutableStateOf<String?>(null) }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(scrollState)
            .testTag("syriatel_cash_screen")
    ) {
        // Header
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
                    .padding(horizontal = 16.dp, vertical = 14.dp)
            ) {
                Column {
                    Text(
                        text = if (isArabic) "سيريتل كاش" else "SyriaTel Cash",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = if (isArabic) "خدمات الدفع الإلكتروني والمحفظة الرقمية" else "Electronic payment & digital wallet services",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Surface(
                    color = SyriatelRed.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(50)
                ) {
                    Text(
                        text = if (isArabic) "نموذج تجريبي" else "UI Prototype",
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = SyriatelRed,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            // Hero Wallet Card
            Card(
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(
                                colors = listOf(SyriatelRed, SyriatelDarkRed, Color(0xFF2B1215))
                            )
                        )
                        .padding(22.dp)
                ) {
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(Color.White.copy(alpha = 0.18f))
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.AccountBalanceWallet,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(26.dp)
                                )
                            }

                            Surface(
                                color = Color.White.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(50)
                            ) {
                                Text(
                                    text = "SYRIATEL CASH",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        Text(
                            text = if (isArabic) {
                                "محفظتك الإلكترونية الشاملة في مكان واحد"
                            } else {
                                "Your All-in-One Electronic Wallet"
                            },
                            style = MaterialTheme.typography.headlineSmall.copy(
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = if (isArabic) {
                                "يوفر سيريتل كاش خدمات الدفع الإلكتروني المريحة لتسديد الفواتير وشحن الرصيد والدفع عبر رمز QR بكل أمان وسهولة."
                            } else {
                                "SyriaTel Cash provides convenient electronic payment services for bill payments, top-ups, and QR merchant transactions safely and easily."
                            },
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = Color.White.copy(alpha = 0.9f)
                            )
                        )
                    }
                }
            }

            // Clear Prototype Notice
            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(14.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.VerifiedUser,
                        contentDescription = null,
                        tint = SyriatelRed,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = if (isArabic) {
                            "تنويه هام: هذه الواجهة هي نموذج تصميمي تفاعلي (UI/UX Prototype) فقط. لا تتم معالجة أي دفعات مالية حقيقية أو جمع بيانات مصرفية."
                        } else {
                            "Important Notice: This interface is a UI/UX prototype only. No real payments are processed and no banking credentials are collected."
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Two prominent action cards: 1. إنشاء حساب, 2. تسجيل الدخول
            Text(
                text = if (isArabic) "ابدأ باستخدام سيريتل كاش" else "Get Started with SyriaTel Cash",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                // Action Card 1: إنشاء حساب (User Plus)
                Card(
                    onClick = { activeModalMode = "register" },
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.5.dp, SyriatelRed.copy(alpha = 0.4f)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("cash_create_account_card")
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(SyriatelRed.copy(alpha = 0.12f))
                        ) {
                            Icon(
                                imageVector = Icons.Filled.PersonAdd,
                                contentDescription = null,
                                tint = SyriatelRed,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = if (isArabic) "إنشاء حساب" else "Create Account",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (isArabic) "فتح محفظة تجريبية جديدة" else "Open a new demo wallet",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Action Card 2: تسجيل الدخول (Wallet / Log In)
                Card(
                    onClick = { activeModalMode = "login" },
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.5.dp, SyriatelGreen.copy(alpha = 0.4f)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("cash_login_card")
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(SyriatelGreen.copy(alpha = 0.12f))
                        ) {
                            Icon(
                                imageVector = Icons.Filled.AccountBalanceWallet,
                                contentDescription = null,
                                tint = SyriatelGreen,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = if (isArabic) "تسجيل الدخول" else "Sign In",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (isArabic) "الدخول إلى حسابك التجريبي" else "Access your demo wallet",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            AnimatedVisibility(visible = demoSubmittedMessage != null) {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = SyriatelGreen.copy(alpha = 0.12f)),
                    border = BorderStroke(1.dp, SyriatelGreen),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(14.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.CheckCircle,
                            contentDescription = null,
                            tint = SyriatelGreen
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = demoSubmittedMessage.orEmpty(),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            // Features Showcase
            Text(
                text = if (isArabic) "مزايا خدمة سيريتل كاش" else "SyriaTel Cash Highlights",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground
            )

            val highlights = listOf(
                Triple(
                    Icons.Filled.QrCodeScanner,
                    BiText("الدفع عبر رمز QR", "QR Code Payments"),
                    BiText("ادفع في المتاجر ونقاط البيع المعتمدة بسرعة وأمان.", "Pay at authorized merchants quickly and securely.")
                ),
                Triple(
                    Icons.Filled.ReceiptLong,
                    BiText("تسديد الفواتير والرسوم", "Bill & Utility Payments"),
                    BiText("سدد فواتير الاتصالات والكهرباء والخدمات الحكومية بلمسة واحدة.", "Pay telecom, utility, and service bills in one tap.")
                ),
                Triple(
                    Icons.Filled.PhoneIphone,
                    BiText("شحن الرصيد والباقات", "Balance & Bundle Top-Up"),
                    BiText("اشحن رصيدك أو رصيد أي رقم سيريتل آخر مباشرة من محفظتك.", "Recharge your line or any SyriaTel number directly.")
                )
            )

            highlights.forEach { (icon, title, desc) ->
                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(14.dp)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(42.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(SyriatelRed.copy(alpha = 0.1f))
                        ) {
                            Icon(imageVector = icon, contentDescription = null, tint = SyriatelRed)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = title.get(isArabic),
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = desc.get(isArabic),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }

    if (activeModalMode != null) {
        val isReg = activeModalMode == "register"
        SyriatelCashFormDialog(
            isRegistration = isReg,
            initialPhone = uiState.userProfile.phoneNumber,
            initialName = uiState.userProfile.displayFullName(isArabic),
            isArabic = isArabic,
            onDismiss = { activeModalMode = null },
            onSubmit = { phone, fullName ->
                onDemoCashAction(isReg, phone, fullName)
                demoSubmittedMessage = if (isArabic) {
                    if (isReg) {
                        "تم إرسال طلب إنشاء حساب سيريتل كاش التجريبي للرقم $phone وتسجيله في مركز العمليات."
                    } else {
                        "تمت محاكاة تسجيل الدخول إلى سيريتل كاش للرقم $phone بنجاح (عملية تجريبية)."
                    }
                } else {
                    if (isReg) {
                        "Demo SyriaTel Cash registration submitted for $phone and logged in Operations Center."
                    } else {
                        "Simulated SyriaTel Cash login for $phone completed (Demo operation)."
                    }
                }
                activeModalMode = null
            }
        )
    }
}

@Composable
private fun SyriatelCashFormDialog(
    isRegistration: Boolean,
    initialPhone: String,
    initialName: String,
    isArabic: Boolean,
    onDismiss: () -> Unit,
    onSubmit: (String, String) -> Unit
) {
    var fullName by remember { mutableStateOf(initialName) }
    var phone by remember { mutableStateOf(initialPhone) }
    var pinCode by remember { mutableStateOf("") }
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
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = if (isRegistration) {
                            if (isArabic) "إنشاء حساب سيريتل كاش" else "Create SyriaTel Cash Account"
                        } else {
                            if (isArabic) "تسجيل دخول سيريتل كاش" else "SyriaTel Cash Sign In"
                        },
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = if (isArabic) {
                        "نموذج تجريبي فقط — لا يتم جمع أي بيانات مصرفية حقيقية."
                    } else {
                        "Demonstration form only — no real financial credentials collected."
                    },
                    style = MaterialTheme.typography.labelSmall,
                    color = SyriatelRed
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (isRegistration) {
                    Text(
                        text = if (isArabic) "الاسم الكامل (تجريبي)" else "Full Name (Demo)",
                        style = MaterialTheme.typography.labelLarge
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = fullName,
                        onValueChange = {
                            fullName = it
                            errorText = null
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }

                Text(
                    text = if (isArabic) "رقم الموبايل" else "Mobile Number",
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
                    placeholder = { Text("09xxxxxxxx") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = if (isArabic) "رمز المحفظة التجريبي (4 أرقام)" else "Demo Wallet PIN (4 digits)",
                    style = MaterialTheme.typography.labelLarge
                )
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = pinCode,
                    onValueChange = {
                        if (it.length <= 4 && it.all { ch -> ch.isDigit() }) {
                            pinCode = it
                            errorText = null
                        }
                    },
                    placeholder = { Text("1234") },
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
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
                            when {
                                isRegistration && fullName.isBlank() -> {
                                    errorText = if (isArabic) "يرجى إدخال الاسم" else "Please enter your name"
                                }
                                !phone.matches(Regex("^09\\d{8}$")) -> {
                                    errorText = if (isArabic) "يرجى إدخال رقم يبدأ بـ 09" else "Enter a valid 09xxxxxxxx number"
                                }
                                pinCode.length < 4 -> {
                                    errorText = if (isArabic) "يرجى إدخال رمز تجريبي من 4 أرقام" else "Enter a 4-digit demo PIN"
                                }
                                else -> {
                                    onSubmit(phone, fullName)
                                }
                            }
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SyriatelRed),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("submit_cash_demo_form")
                    ) {
                        Text(
                            text = if (isRegistration) {
                                if (isArabic) "إنشاء تجريبي" else "Create Demo"
                            } else {
                                if (isArabic) "دخول تجريبي" else "Demo Login"
                            },
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
