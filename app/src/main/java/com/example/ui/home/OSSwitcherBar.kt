package com.example.ui.home

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.LauncherViewModel
import com.example.model.DockBackgroundStyle
import com.example.model.IconShape
import com.example.model.OSDesignStyle

@Composable
fun OSSwitcherBar(
    viewModel: LauncherViewModel,
    modifier: Modifier = Modifier
) {
    val currentStyle = viewModel.settings.osStyle

    val options = listOf(
        Triple(OSDesignStyle.LAWNCHAIR_16, "🌱 Lawnchair", 0xFF1A73E8L),
        Triple(OSDesignStyle.FUSION, "⚡ Fusion", 0xFFD71921L),
        Triple(OSDesignStyle.NOTHING_OS, "🔴 Nothing", 0xFFD71921L),
        Triple(OSDesignStyle.IOS, "🍏 iOS 18", 0xFF007AFFL)
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("os_switcher_bar")
            .padding(horizontal = 14.dp, vertical = 2.dp),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF0F111B).copy(alpha = 0.85f)),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.12f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            options.forEach { (style, label, accentHex) ->
                val isSelected = currentStyle == style
                val bgColor by animateColorAsState(
                    targetValue = if (isSelected) Color(accentHex).copy(alpha = 0.28f) else Color.Transparent,
                    label = "os_switch_bg"
                )

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(18.dp))
                        .background(bgColor)
                        .then(
                            if (isSelected) Modifier.border(1.5.dp, Color(accentHex), RoundedCornerShape(18.dp))
                            else Modifier
                        )
                        .clickable {
                            val newDock = when (style) {
                                OSDesignStyle.IOS, OSDesignStyle.FUSION -> DockBackgroundStyle.IOS_FROSTED_GLASS
                                OSDesignStyle.NOTHING_OS -> DockBackgroundStyle.GLASS_BLUR
                                OSDesignStyle.LAWNCHAIR_16 -> DockBackgroundStyle.CARD_SHADOW
                            }
                            val newShape = when (style) {
                                OSDesignStyle.IOS, OSDesignStyle.FUSION -> IconShape.SQUIRCLE
                                OSDesignStyle.NOTHING_OS -> IconShape.CIRCLE
                                OSDesignStyle.LAWNCHAIR_16 -> IconShape.ROUNDED_SQUARE
                            }
                            val newWallpaper = when (style) {
                                OSDesignStyle.NOTHING_OS -> "nothing_dark"
                                OSDesignStyle.IOS -> "ios_aurora"
                                OSDesignStyle.LAWNCHAIR_16 -> "pixel_monet"
                                OSDesignStyle.FUSION -> "nothing_dark"
                            }
                            viewModel.updateSettings(
                                viewModel.settings.copy(
                                    osStyle = style,
                                    accentColorHex = accentHex,
                                    dockBackgroundStyle = newDock,
                                    iconShape = newShape,
                                    wallpaperId = newWallpaper,
                                    useThemedIcons = (style == OSDesignStyle.LAWNCHAIR_16)
                                )
                            )
                        }
                        .padding(vertical = 7.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = label,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) Color.White else Color(0xFF8E92A8),
                        maxLines = 1
                    )
                }
            }
        }
    }
}
