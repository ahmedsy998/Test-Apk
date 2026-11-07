package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.R
import com.example.model.AppScreen
import com.example.model.BiText
import com.example.ui.theme.SyriatelGreen
import com.example.ui.theme.SyriatelRed

val AvatarPalette = listOf(
    Color(0xFFD71920),
    Color(0xFF16A765),
    Color(0xFF2B313B),
    Color(0xFF0F6CBD),
    Color(0xFF8E24AA)
)

@Composable
fun SyriaTelBrandLogo(
    modifier: Modifier = Modifier,
    height: Dp = 36.dp,
    isDarkMode: Boolean = false
) {
    if (isDarkMode) {
        Surface(
            color = Color.White.copy(alpha = 0.95f),
            shape = RoundedCornerShape(10.dp),
            modifier = modifier
        ) {
            Image(
                painter = painterResource(id = R.drawable.img_syriatel_logo),
                contentDescription = "SyriaTel Logo",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .padding(horizontal = 10.dp, vertical = 4.dp)
                    .height(height)
            )
        }
    } else {
        Image(
            painter = painterResource(id = R.drawable.img_syriatel_logo),
            contentDescription = "SyriaTel Logo",
            contentScale = ContentScale.Fit,
            modifier = modifier.height(height)
        )
    }
}

@Composable
fun UserAvatarCircle(
    name: String,
    colorIndex: Int,
    size: Dp = 44.dp,
    onClick: (() -> Unit)? = null
) {
    val bgColor = AvatarPalette[colorIndex.coerceIn(0, AvatarPalette.lastIndex)]
    val initial = name.trim().firstOrNull()?.toString() ?: "أ"

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .background(bgColor)
            .then(
                if (onClick != null) {
                    Modifier
                        .clickable { onClick() }
                        .testTag("user_avatar_button")
                } else Modifier
            )
    ) {
        Text(
            text = initial,
            color = Color.White,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = (size.value * 0.42f).sp
            )
        )
    }
}

@Composable
fun SyriaTelSubScreenTopBar(
    title: String,
    subtitle: String? = null,
    onBack: () -> Unit,
    trailingAction: (@Composable () -> Unit)? = null
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 1.dp,
        shadowElevation = 2.dp
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 12.dp, vertical = 10.dp)
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .size(48.dp)
                    .testTag("back_button")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
            Spacer(modifier = Modifier.width(4.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (!subtitle.isNullOrBlank()) {
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            if (trailingAction != null) {
                trailingAction()
            }
        }
    }
}

@Composable
fun SyriaTelBottomNavigationBar(
    currentScreen: AppScreen,
    isArabic: Boolean,
    onSelectScreen: (AppScreen) -> Unit
) {
    val items = listOf(
        Triple(
            AppScreen.HOME,
            BiText("الرئيسية", "Home"),
            Pair(Icons.Filled.Home, Icons.Outlined.Home)
        ),
        Triple(
            AppScreen.OFFERS,
            BiText("العروض", "Offers"),
            Pair(Icons.Filled.CardGiftcard, Icons.Outlined.CardGiftcard)
        ),
        Triple(
            AppScreen.SERVICES,
            BiText("الخدمات", "Services"),
            Pair(Icons.Filled.GridView, Icons.Outlined.GridView)
        ),
        Triple(
            AppScreen.SYRIATEL_CASH,
            BiText("سيريتل كاش", "SyriaTel Cash"),
            Pair(Icons.Filled.AccountBalanceWallet, Icons.Outlined.AccountBalanceWallet)
        )
    )

    Surface(
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 3.dp,
        shadowElevation = 8.dp,
        border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline)
    ) {
        NavigationBar(
            containerColor = Color.Transparent,
            tonalElevation = 0.dp,
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
        ) {
            items.forEach { (screen, label, icons) ->
                val selected = currentScreen == screen
                NavigationBarItem(
                    selected = selected,
                    onClick = { onSelectScreen(screen) },
                    icon = {
                        Icon(
                            imageVector = if (selected) icons.first else icons.second,
                            contentDescription = label.get(isArabic)
                        )
                    },
                    label = {
                        Text(
                            text = label.get(isArabic),
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = SyriatelRed,
                        selectedTextColor = SyriatelRed,
                        indicatorColor = SyriatelRed.copy(alpha = 0.12f),
                        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    modifier = Modifier.testTag("bottom_nav_${screen.name.lowercase()}")
                )
            }
        }
    }
}

@Composable
fun UsageCircularIndicator(
    progress: Float,
    color: Color,
    size: Dp = 64.dp,
    strokeWidth: Dp = 6.dp,
    centerLabel: String
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress.coerceIn(0f, 1f),
        animationSpec = tween(durationMillis = 600),
        label = "circular_usage"
    )
    val trackColor = MaterialTheme.colorScheme.outline

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(size)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokePx = strokeWidth.toPx()
            drawArc(
                color = trackColor,
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(width = strokePx, cap = StrokeCap.Round)
            )
            drawArc(
                color = color,
                startAngle = -90f,
                sweepAngle = 360f * animatedProgress,
                useCenter = false,
                style = Stroke(width = strokePx, cap = StrokeCap.Round)
            )
        }
        Text(
            text = centerLabel,
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun KnowMyNumberDialog(
    phoneNumber: String,
    customerNumber: String,
    isArabic: Boolean,
    onDismiss: () -> Unit,
    onCopied: () -> Unit
) {
    val context = LocalContext.current
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(SyriatelRed.copy(alpha = 0.12f))
                ) {
                    Icon(
                        imageVector = Icons.Filled.SimCard,
                        contentDescription = null,
                        tint = SyriatelRed,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = if (isArabic) "معرفة رقمي" else "Know My Number",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = if (isArabic) "رقم خط سيريتل التجريبي المسجل حالياً" else "Currently registered SyriaTel demo number",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(20.dp))

                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(vertical = 16.dp, horizontal = 12.dp)
                    ) {
                        Text(
                            text = phoneNumber,
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.5.sp
                            ),
                            color = SyriatelRed
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (isArabic) "رمز العميل: $customerNumber" else "Customer ID: $customerNumber",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("close_my_number_modal")
                    ) {
                        Text(text = if (isArabic) "إغلاق" else "Close")
                    }

                    Button(
                        onClick = {
                            copyToClipboard(context, "SyriaTel Number", phoneNumber)
                            onCopied()
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SyriatelRed),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("copy_my_number_button")
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ContentCopy,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = if (isArabic) "نسخ الرقم" else "Copy Number")
                    }
                }
            }
        }
    }
}

fun copyToClipboard(context: Context, label: String, text: String) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
    clipboard?.setPrimaryClip(ClipData.newPlainText(label, text))
}
