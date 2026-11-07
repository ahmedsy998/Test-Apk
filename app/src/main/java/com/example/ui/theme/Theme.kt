package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection

private val LightColorScheme = lightColorScheme(
    primary = SyriatelRed,
    onPrimary = Color.White,
    primaryContainer = SyriatelSoftRed,
    onPrimaryContainer = SyriatelDarkRed,
    secondary = SyriatelGreen,
    onSecondary = Color.White,
    secondaryContainer = SyriatelSoftGreen,
    onSecondaryContainer = SyriatelGreen,
    tertiary = CharcoalGray,
    background = LightBackground,
    onBackground = LightTextPrimary,
    surface = LightSurface,
    onSurface = LightTextPrimary,
    surfaceVariant = Color(0xFFF0F2F5),
    onSurfaceVariant = LightTextSecondary,
    outline = LightBorder,
    outlineVariant = Color(0xFFD8DCE2),
    error = Color(0xFFD32F2F)
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFFF4D54),
    onPrimary = Color.White,
    primaryContainer = SyriatelDarkSoftRed,
    onPrimaryContainer = Color(0xFFFFB3B6),
    secondary = Color(0xFF28C77F),
    onSecondary = Color.Black,
    secondaryContainer = SyriatelDarkSoftGreen,
    onSecondaryContainer = Color(0xFF75F0B6),
    tertiary = Color(0xFFB0B7C3),
    background = DarkBackground,
    onBackground = DarkTextPrimary,
    surface = DarkSurface,
    onSurface = DarkTextPrimary,
    surfaceVariant = DarkSurfaceElevated,
    onSurfaceVariant = DarkTextSecondary,
    outline = DarkBorder,
    outlineVariant = Color(0xFF3B4252),
    error = Color(0xFFEF5350)
)

@Composable
fun SyriatelTheme(
    darkTheme: Boolean = false,
    isArabic: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val layoutDirection = if (isArabic) LayoutDirection.Rtl else LayoutDirection.Ltr

    CompositionLocalProvider(LocalLayoutDirection provides layoutDirection) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}
