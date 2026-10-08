package com.example.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.data.LauncherViewModel
import com.example.model.DockBackgroundStyle
import com.example.model.OSDesignStyle

@Composable
fun DockBar(
    viewModel: LauncherViewModel,
    onAppClick: (String) -> Unit,
    onAppLongClick: (String) -> Unit,
    onDrawerClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val settings = viewModel.settings
    val accentColor = Color(settings.accentColorHex)

    val dockShape = if (settings.osStyle == OSDesignStyle.IOS || settings.dockBackgroundStyle == DockBackgroundStyle.IOS_FROSTED_GLASS) {
        RoundedCornerShape(32.dp)
    } else {
        RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
    }

    val dockBackgroundModifier = when (settings.dockBackgroundStyle) {
        DockBackgroundStyle.TRANSPARENT -> Modifier
        DockBackgroundStyle.GLASS_BLUR -> Modifier
            .clip(dockShape)
            .background(Color(0xFF101322).copy(alpha = 0.72f))
        DockBackgroundStyle.CARD_SHADOW -> Modifier
            .shadow(12.dp, dockShape)
            .clip(dockShape)
            .background(Color(0xFF181B2A))
        DockBackgroundStyle.TINTED_ACCENT -> Modifier
            .clip(dockShape)
            .background(accentColor.copy(alpha = 0.18f))
        DockBackgroundStyle.IOS_FROSTED_GLASS -> Modifier
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .shadow(16.dp, RoundedCornerShape(32.dp), ambientColor = Color.White.copy(alpha = 0.1f))
            .clip(RoundedCornerShape(32.dp))
            .background(Color(0xFF2C2C2E).copy(alpha = 0.55f))
            .border(1.dp, Color.White.copy(alpha = 0.18f), RoundedCornerShape(32.dp))
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .testTag("dock_bar")
            .then(dockBackgroundModifier)
            .padding(horizontal = 14.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val dockIds = viewModel.dockAppIds
            for (id in dockIds) {
                if (id == "__DRAWER_ICON__") {
                    // Dedicated Drawer Launcher Button
                    Box(
                        modifier = Modifier
                            .testTag("dock_app_drawer_button")
                            .size((52 * settings.iconScale).dp)
                            .shadow(8.dp, CircleShape, ambientColor = accentColor, spotColor = accentColor)
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    colors = listOf(
                                        accentColor,
                                        Color(0xFF00E5FF)
                                    )
                                )
                            )
                            .clickable { onDrawerClick() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Apps,
                            contentDescription = "App Drawer",
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                } else {
                    val app = viewModel.allApps.find { it.id == id }
                    if (app != null) {
                        AppIconItem(
                            app = app,
                            iconShape = settings.iconShape,
                            iconScale = settings.iconScale * 0.95f,
                            showLabel = false,
                            useThemedIcons = settings.useThemedIcons,
                            themedAccentColor = accentColor,
                            onClick = { onAppClick(app.id) },
                            onLongClick = { onAppLongClick(app.id) }
                        )
                    }
                }
            }
        }
    }
}
