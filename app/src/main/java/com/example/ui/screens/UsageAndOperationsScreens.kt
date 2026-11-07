package com.example.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.model.*
import com.example.ui.components.SyriaTelSubScreenTopBar
import com.example.ui.components.UsageCircularIndicator
import com.example.ui.components.copyToClipboard
import com.example.ui.theme.SyriatelAmber
import com.example.ui.theme.SyriatelGreen
import com.example.ui.theme.SyriatelRed
import com.example.viewmodel.SyriatelUiState
import java.util.Locale
import kotlin.math.roundToInt

// ==========================================
// SCREEN 7 — SMART USAGE CENTER
// ==========================================
@Composable
fun SmartUsageCenterScreen(
    uiState: SyriatelUiState,
    onBack: () -> Unit,
    onSelectPeriod: (UsagePeriod) -> Unit,
    onSelectMetric: (UsageMetric) -> Unit,
    onUpdateAlerts: (UsageAlertsConfig) -> Unit,
    onNavigateToOffers: () -> Unit,
    onNavigateToServices: () -> Unit,
    onNavigateToHome: () -> Unit
) {
    val isArabic = uiState.isArabic
    val scrollState = rememberScrollState()
    val insights = remember(
        uiState.internetRemainingGb,
        uiState.minutesRemaining,
        uiState.balanceSyp,
        uiState.services
    ) {
        uiState.computeSmartInsights()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("smart_usage_center_screen")
    ) {
        SyriaTelSubScreenTopBar(
            title = if (isArabic) "مركز الاستهلاك الذكي" else "Smart Usage Center",
            subtitle = if (isArabic) "راقب استهلاكك وتحكم بباقاتك بذكاء" else "Monitor your consumption and manage bundles smartly",
            onBack = onBack
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(18.dp),
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {
            // A. USAGE OVERVIEW (3 summary cards)
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                UsageSummaryMiniCard(
                    title = if (isArabic) "الإنترنت" else "Internet",
                    usedText = "${String.format(Locale.US, "%.1f", uiState.internetUsedGb)} GB",
                    remainingText = "${String.format(Locale.US, "%.1f", uiState.internetRemainingGb)} GB",
                    totalText = "${String.format(Locale.US, "%.0f", uiState.internetTotalGb)} GB",
                    usedFraction = uiState.internetUsedFraction,
                    color = SyriatelRed,
                    isArabic = isArabic,
                    modifier = Modifier.weight(1f)
                )
                UsageSummaryMiniCard(
                    title = if (isArabic) "الدقائق" else "Minutes",
                    usedText = "${uiState.minutesUsed}",
                    remainingText = "${uiState.minutesRemaining}",
                    totalText = "${uiState.minutesTotal}",
                    usedFraction = uiState.minutesUsedFraction,
                    color = SyriatelGreen,
                    isArabic = isArabic,
                    modifier = Modifier.weight(1f)
                )
                UsageSummaryMiniCard(
                    title = if (isArabic) "الرسائل" else "SMS",
                    usedText = "${uiState.smsUsed}",
                    remainingText = "${uiState.smsRemaining}",
                    totalText = "${uiState.smsTotal}",
                    usedFraction = uiState.smsUsedFraction,
                    color = Color(0xFF0F6CBD),
                    isArabic = isArabic,
                    modifier = Modifier.weight(1f)
                )
            }

            // C. DAILY AND WEEKLY STATISTICS CHART
            InteractiveUsageChartCard(
                period = uiState.selectedUsagePeriod,
                metric = uiState.selectedUsageMetric,
                isArabic = isArabic,
                onSelectPeriod = onSelectPeriod,
                onSelectMetric = onSelectMetric
            )

            // B. USAGE DETAILS (3 detailed cards)
            Text(
                text = if (isArabic) "تفاصيل الاستهلاك الدقيقة" else "Detailed Usage Breakdown",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
            )

            DetailedMetricSectionCard(
                title = if (isArabic) "تفاصيل استهلاك الإنترنت" else "Internet Usage Details",
                icon = Icons.Filled.Public,
                accentColor = SyriatelRed,
                rows = listOf(
                    Pair(
                        if (isArabic) "إجمالي سعة الباقة" else "Total Allowance",
                        "${String.format(Locale.US, "%.1f", uiState.internetTotalGb)} GB"
                    ),
                    Pair(
                        if (isArabic) "الكمية المستهلكة" else "Used Data",
                        "${String.format(Locale.US, "%.1f", uiState.internetUsedGb)} GB"
                    ),
                    Pair(
                        if (isArabic) "الكمية المتبقية" else "Remaining Data",
                        "${String.format(Locale.US, "%.1f", uiState.internetRemainingGb)} GB"
                    ),
                    Pair(
                        if (isArabic) "متوسط الاستهلاك اليومي" else "Daily Average",
                        "${String.format(Locale.US, "%.2f", uiState.internetDailyAvgGb)} GB"
                    ),
                    Pair(
                        if (isArabic) "المدة التقديرية المتبقية" else "Est. Remaining Duration",
                        if (isArabic) "${uiState.internetEstRemainingDays} يوماً" else "${uiState.internetEstRemainingDays} Days"
                    ),
                    Pair(
                        if (isArabic) "نسبة الاستهلاك" else "Usage Percentage",
                        "${(uiState.internetUsedFraction * 100).roundToInt()}%"
                    )
                )
            )

            DetailedMetricSectionCard(
                title = if (isArabic) "تفاصيل دقائق الاتصال" else "Call Minutes Details",
                icon = Icons.Filled.PhoneInTalk,
                accentColor = SyriatelGreen,
                rows = listOf(
                    Pair(
                        if (isArabic) "إجمالي الدقائق المتاحة" else "Total Available Minutes",
                        if (isArabic) "${uiState.minutesTotal} دقيقة" else "${uiState.minutesTotal} Mins"
                    ),
                    Pair(
                        if (isArabic) "الدقائق المستهلكة" else "Used Minutes",
                        if (isArabic) "${uiState.minutesUsed} دقيقة" else "${uiState.minutesUsed} Mins"
                    ),
                    Pair(
                        if (isArabic) "الدقائق المتبقية" else "Remaining Minutes",
                        if (isArabic) "${uiState.minutesRemaining} دقيقة" else "${uiState.minutesRemaining} Mins"
                    ),
                    Pair(
                        if (isArabic) "متوسط المكالمات اليومي" else "Average Daily Call Duration",
                        if (isArabic) "${uiState.minutesDailyAvg} دقيقة / يوم" else "${uiState.minutesDailyAvg} Mins / Day"
                    ),
                    Pair(
                        if (isArabic) "نسبة الاستهلاك" else "Usage Percentage",
                        "${(uiState.minutesUsedFraction * 100).roundToInt()}%"
                    )
                )
            )

            DetailedMetricSectionCard(
                title = if (isArabic) "تفاصيل الرسائل النصية" else "SMS Usage Details",
                icon = Icons.Filled.Sms,
                accentColor = Color(0xFF0F6CBD),
                rows = listOf(
                    Pair(
                        if (isArabic) "إجمالي الرسائل المتاحة" else "Total Available Messages",
                        if (isArabic) "${uiState.smsTotal} رسالة" else "${uiState.smsTotal} SMS"
                    ),
                    Pair(
                        if (isArabic) "الرسائل المستهلكة" else "Used Messages",
                        if (isArabic) "${uiState.smsUsed} رسالة" else "${uiState.smsUsed} SMS"
                    ),
                    Pair(
                        if (isArabic) "الرسائل المتبقية" else "Remaining Messages",
                        if (isArabic) "${uiState.smsRemaining} رسالة" else "${uiState.smsRemaining} SMS"
                    ),
                    Pair(
                        if (isArabic) "متوسط الاستهلاك اليومي" else "Average Daily SMS",
                        if (isArabic) "${uiState.smsDailyAvg} رسالة / يوم" else "${uiState.smsDailyAvg} SMS / Day"
                    ),
                    Pair(
                        if (isArabic) "نسبة الاستهلاك" else "Usage Percentage",
                        "${(uiState.smsUsedFraction * 100).roundToInt()}%"
                    )
                )
            )

            // D. SMART INSIGHTS
            Text(
                text = if (isArabic) "تحليلات ذكية" else "Smart Insights",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
            )

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                insights.forEach { insight ->
                    SmartInsightItemCard(insight = insight, isArabic = isArabic)
                }
            }

            // E. USAGE ALERTS
            UsageAlertsCard(
                alerts = uiState.usageAlerts,
                isArabic = isArabic,
                onUpdateAlerts = onUpdateAlerts
            )

            // F. QUICK NAVIGATION
            Text(
                text = if (isArabic) "إجراءات سريعة" else "Quick Navigation",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground
            )

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Button(
                    onClick = onNavigateToOffers,
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = SyriatelRed),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("usage_nav_offers_button")
                ) {
                    Icon(Icons.Filled.CardGiftcard, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isArabic) "عرض العروض المناسبة" else "Explore Matching Offers",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedButton(
                        onClick = onNavigateToServices,
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("usage_nav_services_button")
                    ) {
                        Text(if (isArabic) "إدارة الخدمات" else "Manage Services")
                    }

                    OutlinedButton(
                        onClick = onNavigateToHome,
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("usage_nav_home_button")
                    ) {
                        Text(if (isArabic) "العودة إلى الرئيسية" else "Back to Home")
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun UsageSummaryMiniCard(
    title: String,
    usedText: String,
    remainingText: String,
    totalText: String,
    usedFraction: Float,
    color: Color,
    isArabic: Boolean,
    modifier: Modifier = Modifier
) {
    val pctConsumed = "${(usedFraction * 100).roundToInt()}%"

    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        modifier = modifier
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(10.dp))
            UsageCircularIndicator(
                progress = usedFraction,
                color = color,
                size = 56.dp,
                strokeWidth = 5.5.dp,
                centerLabel = pctConsumed
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = if (isArabic) "المتبقي: $remainingText" else "Left: $remainingText",
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                color = color,
                maxLines = 1
            )
            Text(
                text = if (isArabic) "المستهلك: $usedText" else "Used: $usedText",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1
            )
            Text(
                text = if (isArabic) "الإجمالي: $totalText" else "Total: $totalText",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun InteractiveUsageChartCard(
    period: UsagePeriod,
    metric: UsageMetric,
    isArabic: Boolean,
    onSelectPeriod: (UsagePeriod) -> Unit,
    onSelectMetric: (UsageMetric) -> Unit
) {
    val points = if (period == UsagePeriod.DAILY) {
        SyriaTelSampleData.dailyUsageChart
    } else {
        SyriaTelSampleData.weeklyUsageChart
    }

    var selectedBarIndex by remember(period, metric) { mutableIntStateOf(points.lastIndex.coerceAtLeast(0)) }

    val chartColor = when (metric) {
        UsageMetric.INTERNET -> SyriatelRed
        UsageMetric.MINUTES -> SyriatelGreen
        UsageMetric.SMS -> Color(0xFF0F6CBD)
    }

    val values = points.map { pt ->
        when (metric) {
            UsageMetric.INTERNET -> pt.internetGb
            UsageMetric.MINUTES -> pt.minutes.toFloat()
            UsageMetric.SMS -> pt.sms.toFloat()
        }
    }
    val maxVal = (values.maxOrNull() ?: 1f).coerceAtLeast(0.1f)
    val selectedPoint = points.getOrNull(selectedBarIndex)

    Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = if (isArabic) "إحصائيات الاستهلاك" else "Consumption Statistics",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )

                // Segmented Selector: يومي / أسبوعي
                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(modifier = Modifier.padding(3.dp)) {
                        UsagePeriod.entries.forEach { p ->
                            val selected = p == period
                            Surface(
                                onClick = { onSelectPeriod(p) },
                                color = if (selected) SyriatelRed else Color.Transparent,
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.testTag("chart_period_${p.name.lowercase()}")
                            ) {
                                Text(
                                    text = p.title.get(isArabic),
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = if (selected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                                    ),
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Metric Selector: الإنترنت / الدقائق / الرسائل
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                UsageMetric.entries.forEach { m ->
                    val isSelected = m == metric
                    FilterChip(
                        selected = isSelected,
                        onClick = { onSelectMetric(m) },
                        label = {
                            Text(
                                text = m.title.get(isArabic),
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                )
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = chartColor.copy(alpha = 0.15f),
                            selectedLabelColor = chartColor
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("chart_metric_${m.name.lowercase()}")
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Tooltip Banner for selected data point
            if (selectedPoint != null) {
                val formattedValue = when (metric) {
                    UsageMetric.INTERNET -> "${String.format(Locale.US, "%.2f", selectedPoint.internetGb)} GB"
                    UsageMetric.MINUTES -> if (isArabic) "${selectedPoint.minutes} دقيقة" else "${selectedPoint.minutes} Mins"
                    UsageMetric.SMS -> if (isArabic) "${selectedPoint.sms} رسالة" else "${selectedPoint.sms} SMS"
                }

                Surface(
                    color = chartColor.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = if (isArabic) {
                                "الفترة المحددة: ${selectedPoint.label.ar}"
                            } else {
                                "Selected Period: ${selectedPoint.label.en}"
                            },
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = formattedValue,
                            style = MaterialTheme.typography.titleSmall.copy(
                                color = chartColor,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Interactive Bar Chart
            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(155.dp)
            ) {
                points.forEachIndexed { idx, pt ->
                    val rawVal = values[idx]
                    val ratio = (rawVal / maxVal).coerceIn(0.12f, 1f)
                    val animRatio by animateFloatAsState(
                        targetValue = ratio,
                        animationSpec = tween(durationMillis = 450),
                        label = "bar_$idx"
                    )
                    val isSelected = idx == selectedBarIndex
                    val valLabel = when (metric) {
                        UsageMetric.INTERNET -> String.format(Locale.US, "%.1f", pt.internetGb)
                        UsageMetric.MINUTES -> pt.minutes.toString()
                        UsageMetric.SMS -> pt.sms.toString()
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Bottom,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(10.dp))
                            .clickable { selectedBarIndex = idx }
                            .padding(vertical = 2.dp)
                    ) {
                        Text(
                            text = valLabel,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) chartColor else MaterialTheme.colorScheme.onSurfaceVariant
                            ),
                            maxLines = 1
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.62f)
                                .fillMaxHeight(animRatio * 0.72f)
                                .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp, bottomStart = 4.dp, bottomEnd = 4.dp))
                                .background(
                                    if (isSelected) chartColor else chartColor.copy(alpha = 0.35f)
                                )
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = pt.label.get(isArabic),
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            ),
                            color = if (isSelected) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DetailedMetricSectionCard(
    title: String,
    icon: ImageVector,
    accentColor: Color,
    rows: List<Pair<String, String>>
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(accentColor.copy(alpha = 0.12f))
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = accentColor,
                        modifier = Modifier.size(20.dp)
                    )
                }
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

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
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
private fun SmartInsightItemCard(
    insight: SmartInsight,
    isArabic: Boolean
) {
    val (badgeColor, badgeLabel, icon) = when (insight.severity) {
        InsightSeverity.NORMAL -> Triple(
            SyriatelGreen,
            BiText("استهلاك طبيعي", "Normal Usage"),
            Icons.Filled.CheckCircle
        )
        InsightSeverity.HIGH_USAGE -> Triple(
            SyriatelRed,
            BiText("استهلاك مرتفع", "High Usage"),
            Icons.Filled.TrendingUp
        )
        InsightSeverity.LOW_BALANCE -> Triple(
            SyriatelAmber,
            BiText("تنبيه الرصيد", "Low Balance"),
            Icons.Filled.AccountBalanceWallet
        )
        InsightSeverity.EXPIRING_SOON -> Triple(
            Color(0xFF0F6CBD),
            BiText("توصية ذكية", "Smart Tip"),
            Icons.Filled.Lightbulb
        )
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, badgeColor.copy(alpha = 0.35f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.Top,
            modifier = Modifier.padding(14.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(badgeColor.copy(alpha = 0.12f))
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = badgeColor,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Surface(
                        color = badgeColor.copy(alpha = 0.12f),
                        shape = RoundedCornerShape(50)
                    ) {
                        Text(
                            text = badgeLabel.get(isArabic),
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = badgeColor,
                                fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = insight.title.get(isArabic),
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = insight.description.get(isArabic),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun UsageAlertsCard(
    alerts: UsageAlertsConfig,
    isArabic: Boolean,
    onUpdateAlerts: (UsageAlertsConfig) -> Unit
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
                    imageVector = Icons.Filled.NotificationsActive,
                    contentDescription = null,
                    tint = SyriatelRed
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = if (isArabic) "تنبيهات الاستهلاك الذكية" else "Smart Usage Alerts",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = if (isArabic) "اختر متى ترغب باستلام إشعارات استهلاك باقاتك" else "Choose when to receive package threshold alerts",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outline)

            AlertToggleRow(
                title = if (isArabic) "تنبيهي عند استهلاك 50% من الباقة" else "Notify me at 50% package usage",
                checked = alerts.alert50Percent,
                onCheckedChange = { onUpdateAlerts(alerts.copy(alert50Percent = it)) }
            )
            AlertToggleRow(
                title = if (isArabic) "تنبيهي عند استهلاك 80% من الباقة" else "Notify me at 80% package usage",
                checked = alerts.alert80Percent,
                onCheckedChange = { onUpdateAlerts(alerts.copy(alert80Percent = it)) }
            )
            AlertToggleRow(
                title = if (isArabic) "تنبيهي عند استهلاك 90% من الباقة" else "Notify me at 90% package usage",
                checked = alerts.alert90Percent,
                onCheckedChange = { onUpdateAlerts(alerts.copy(alert90Percent = it)) }
            )
            AlertToggleRow(
                title = if (isArabic) "تنبيهي عند اقتراب انتهاء صلاحية الباقة" else "Notify me when package is nearly expired",
                checked = alerts.alertNearExpiry,
                onCheckedChange = { onUpdateAlerts(alerts.copy(alertNearExpiry = it)) }
            )
        }
    }
}

@Composable
private fun AlertToggleRow(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )
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
// SCREEN 8 — OPERATIONS CENTER
// ==========================================
@Composable
fun OperationsCenterScreen(
    uiState: SyriatelUiState,
    onBack: () -> Unit,
    onSelectCategory: (OperationCategory) -> Unit,
    onSelectDateFilter: (DateFilterOption) -> Unit,
    onCopiedRef: () -> Unit
) {
    val isArabic = uiState.isArabic
    val filteredList = remember(
        uiState.transactions,
        uiState.selectedOperationCategory,
        uiState.selectedDateFilter
    ) {
        uiState.transactions.filter { tx ->
            val catMatch = uiState.selectedOperationCategory == OperationCategory.ALL ||
                tx.category == uiState.selectedOperationCategory
            val dateMatch = tx.daysAgo <= uiState.selectedDateFilter.maxDaysAgo
            catMatch && dateMatch
        }
    }

    val totalCount = uiState.transactions.size
    val successCount = uiState.transactions.count { it.status == OperationStatus.SUCCESS }
    val processingCount = uiState.transactions.count { it.status == OperationStatus.PROCESSING }

    var selectedTransaction by remember { mutableStateOf<OperationRecord?>(null) }

    val scrollState = rememberScrollState()
    val filterScrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("operations_center_screen")
    ) {
        SyriaTelSubScreenTopBar(
            title = if (isArabic) "مركز العمليات" else "Operations Center",
            subtitle = if (isArabic) "تابع جميع عملياتك وسجل نشاط خطك" else "Track all your operations and line activity history",
            onBack = onBack
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {
            // Demonstration notice
            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.HistoryEdu,
                        contentDescription = null,
                        tint = SyriatelRed,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isArabic) {
                            "سجل عمليات تجريبي — يتم تسجيل أي عملية تقوم بها في التطبيق تلقائياً هنا."
                        } else {
                            "Demonstration transaction log — actions performed in the app are automatically recorded here."
                        },
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // A. OPERATIONS SUMMARY
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                OperationStatCard(
                    label = if (isArabic) "إجمالي العمليات" else "Total Ops",
                    count = totalCount,
                    color = SyriatelRed,
                    icon = Icons.Filled.ReceiptLong,
                    modifier = Modifier.weight(1f)
                )
                OperationStatCard(
                    label = if (isArabic) "العمليات الناجحة" else "Successful",
                    count = successCount,
                    color = SyriatelGreen,
                    icon = Icons.Filled.CheckCircle,
                    modifier = Modifier.weight(1f)
                )
                OperationStatCard(
                    label = if (isArabic) "قيد المعالجة" else "Processing",
                    count = processingCount,
                    color = SyriatelAmber,
                    icon = Icons.Filled.Schedule,
                    modifier = Modifier.weight(1f)
                )
            }

            // C. FILTERS (Category & Date)
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(filterScrollState)
            ) {
                OperationCategory.entries.forEach { cat ->
                    val selected = cat == uiState.selectedOperationCategory
                    FilterChip(
                        selected = selected,
                        onClick = { onSelectCategory(cat) },
                        label = {
                            Text(
                                text = cat.title.get(isArabic),
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium
                                )
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = SyriatelRed,
                            selectedLabelColor = Color.White
                        ),
                        shape = RoundedCornerShape(50),
                        modifier = Modifier.testTag("op_filter_${cat.name.lowercase()}")
                    )
                }
            }

            // Date Filter Row
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                DateFilterOption.entries.forEach { df ->
                    val selected = df == uiState.selectedDateFilter
                    Surface(
                        onClick = { onSelectDateFilter(df) },
                        color = if (selected) SyriatelRed.copy(alpha = 0.14f) else MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(
                            1.dp,
                            if (selected) SyriatelRed else MaterialTheme.colorScheme.outline
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp)
                            .testTag("date_filter_${df.name.lowercase()}")
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = df.title.get(isArabic),
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (selected) SyriatelRed else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            )
                        }
                    }
                }
            }

            // B. TRANSACTION HISTORY LIST
            if (filteredList.isEmpty()) {
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.SearchOff,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(44.dp)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = if (isArabic) "لا توجد عمليات مطابقة للفلتر المحدد" else "No operations match the selected filter",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    filteredList.forEach { tx ->
                        TransactionRowCard(
                            record = tx,
                            isArabic = isArabic,
                            onClick = { selectedTransaction = tx }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    selectedTransaction?.let { tx ->
        OperationDetailsDialog(
            record = tx,
            isArabic = isArabic,
            onDismiss = { selectedTransaction = null },
            onCopiedRef = onCopiedRef
        )
    }
}

@Composable
private fun OperationStatCard(
    label: String,
    count: Int,
    color: Color,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = count.toString(),
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = color
                    )
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun TransactionRowCard(
    record: OperationRecord,
    isArabic: Boolean,
    onClick: () -> Unit
) {
    val icon = when (record.category) {
        OperationCategory.RECHARGE -> Icons.Filled.AddCard
        OperationCategory.PACKAGES -> Icons.Filled.CardGiftcard
        OperationCategory.SERVICES -> Icons.Filled.Tune
        OperationCategory.ACCOUNT -> Icons.Filled.ManageAccounts
        OperationCategory.SYRIATEL_CASH -> Icons.Filled.AccountBalanceWallet
        OperationCategory.ALL -> Icons.Filled.Receipt
    }
    val statusColor = Color(record.status.colorHex)

    Card(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("tx_item_${record.id}")
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
                    .background(SyriatelRed.copy(alpha = 0.1f))
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = SyriatelRed,
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
                        text = record.title.get(isArabic),
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    if (record.amountLabel != null) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = record.amountLabel.get(isArabic),
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = SyriatelRed
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(3.dp))

                Text(
                    text = record.description.get(isArabic),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "${record.dateTimeLabel.get(isArabic)} • ${record.referenceNumber}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Surface(
                        color = statusColor.copy(alpha = 0.14f),
                        shape = RoundedCornerShape(50)
                    ) {
                        Text(
                            text = record.status.label.get(isArabic),
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = statusColor,
                                fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun OperationDetailsDialog(
    record: OperationRecord,
    isArabic: Boolean,
    onDismiss: () -> Unit,
    onCopiedRef: () -> Unit
) {
    val context = LocalContext.current
    val statusColor = Color(record.status.colorHex)

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
                        text = if (isArabic) "تفاصيل العملية" else "Operation Details",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Surface(
                        color = statusColor.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(50)
                    ) {
                        Text(
                            text = record.status.label.get(isArabic),
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = statusColor,
                                fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = record.title.get(isArabic),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = record.description.get(isArabic),
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
                                text = if (isArabic) "الرقم المرجعي:" else "Reference ID:",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = record.referenceNumber,
                                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = if (isArabic) "التاريخ والوقت:" else "Date & Time:",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = record.dateTimeLabel.get(isArabic),
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        if (record.amountLabel != null) {
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = if (isArabic) "القيمة:" else "Amount:",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = record.amountLabel.get(isArabic),
                                    style = MaterialTheme.typography.labelLarge.copy(
                                        color = SyriatelRed,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = record.extraNotes.get(isArabic),
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
                        Text(if (isArabic) "إغلاق" else "Close")
                    }

                    Button(
                        onClick = {
                            copyToClipboard(context, "Reference ID", record.referenceNumber)
                            onCopiedRef()
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SyriatelRed),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("copy_operation_reference_button")
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ContentCopy,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isArabic) "نسخ المرجع" else "Copy Ref",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
