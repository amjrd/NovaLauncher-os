package com.example.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.model.ThemeMode

@Composable
fun NovaLauncherTheme(
    themeMode: ThemeMode = ThemeMode.DARK,
    accentColorHex: Long = 0xFFFF7043,
    content: @Composable () -> Unit
) {
    val accentColor = Color(accentColorHex)
    val isDark = when (themeMode) {
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
        ThemeMode.DARK, ThemeMode.AMOLED -> true
        ThemeMode.LIGHT -> false
    }

    val colorScheme = if (isDark) {
        val bg = if (themeMode == ThemeMode.AMOLED) AmoledBackground else DarkBackground
        val surface = if (themeMode == ThemeMode.AMOLED) AmoledSurface else DarkSurface
        darkColorScheme(
            primary = accentColor,
            onPrimary = Color.Black,
            secondary = NovaCyan,
            onSecondary = Color.Black,
            background = bg,
            onBackground = Color.White,
            surface = surface,
            onSurface = Color.White,
            surfaceVariant = if (themeMode == ThemeMode.AMOLED) Color(0xFF181818) else DarkCard,
            onSurfaceVariant = Color(0xFFCCCCCC)
        )
    } else {
        lightColorScheme(
            primary = accentColor,
            onPrimary = Color.White,
            secondary = NovaBlue,
            onSecondary = Color.White,
            background = LightBackground,
            onBackground = Color(0xFF1E1E2E),
            surface = LightSurface,
            onSurface = Color(0xFF1E1E2E),
            surfaceVariant = LightCard,
            onSurfaceVariant = Color(0xFF4A4A5A)
        )
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
