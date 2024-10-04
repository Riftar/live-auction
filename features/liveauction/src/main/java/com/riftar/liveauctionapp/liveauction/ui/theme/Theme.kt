package com.riftar.liveauctionapp.liveauction.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat


val AppColorScheme = darkColorScheme(
    primary = AccentGreen,
    onPrimary = DarkBackground,
    primaryContainer = AccentGreenLight,
    onPrimaryContainer = DarkBackground,
    secondary = AccentGreen,
    onSecondary = DarkBackground,
    secondaryContainer = AccentGreenLight,
    onSecondaryContainer = DarkBackground,
    tertiary = AccentGreen,
    onTertiary = DarkBackground,
    tertiaryContainer = AccentGreenLight,
    onTertiaryContainer = DarkBackground,
    background = DarkBackground,
    onBackground = White,
    surface = DarkBackground,
    onSurface = White,
    surfaceVariant = Color(0xFF2C2C2C),
    onSurfaceVariant = White,
    error = Color(0xFFCF6679),
    onError = DarkBackground,
    errorContainer = Color(0xFFB1384E),
    onErrorContainer = White,
    outline = AccentGreen,
    outlineVariant = AccentGreenDark
)

@Composable
fun LiveAuctionAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            val insets = WindowCompat.getInsetsController(window, view)
            insets.isAppearanceLightStatusBars = false
            insets.isAppearanceLightNavigationBars = false
        }
    }
    MaterialTheme(
        colorScheme = AppColorScheme,
        typography = Typography,
        content = content
    )
}