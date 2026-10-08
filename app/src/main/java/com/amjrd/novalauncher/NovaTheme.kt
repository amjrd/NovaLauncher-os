package com.amjrd.novalauncher

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.amjrd.novalauncher.model.OSDesignStyle

val NothingRedColor = Color(0xFFD71921)
val IosBlueColor = Color(0xFF007AFF)
val PixelMintColor = Color(0xFF7CE4B5)

@Composable
fun NovaLauncherTheme(
    osStyle: OSDesignStyle = OSDesignStyle.FUSION,
    accentColorHex: Long = 0xFFD71921L,
    content: @Composable () -> Unit
) {
    val accent = Color(accentColorHex)
    val colorScheme = darkColorScheme(
        primary = accent,
        onPrimary = Color.Black,
        secondary = when (osStyle) {
            OSDesignStyle.NOTHING_OS -> Color.White
            OSDesignStyle.IOS -> IosBlueColor
            OSDesignStyle.PIXEL_17 -> PixelMintColor
            OSDesignStyle.FUSION -> NothingRedColor
        },
        background = Color(0xFF0B0D13),
        surface = Color(0xFF141722),
        onSurface = Color.White
    )

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
