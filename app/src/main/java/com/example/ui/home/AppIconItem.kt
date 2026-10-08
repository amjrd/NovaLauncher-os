package com.example.ui.home

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Contacts
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.Widgets
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppItem
import com.example.model.IconShape

fun getShapeForIcon(shape: IconShape): Shape {
    return when (shape) {
        IconShape.CIRCLE -> CircleShape
        IconShape.SQUIRCLE -> RoundedCornerShape(32)
        IconShape.ROUNDED_SQUARE -> RoundedCornerShape(18.dp)
        IconShape.TEARDROP -> RoundedCornerShape(
            topStart = 24.dp,
            topEnd = 24.dp,
            bottomEnd = 4.dp,
            bottomStart = 24.dp
        )
        IconShape.HEXAGON -> CutCornerShape(16.dp)
    }
}

fun getAppIconVector(iconResName: String): ImageVector {
    return when (iconResName) {
        "phone" -> Icons.Default.Phone
        "chat" -> Icons.Default.Chat
        "photo_camera" -> Icons.Default.PhotoCamera
        "photo_library" -> Icons.Default.PhotoLibrary
        "public" -> Icons.Default.Public
        "tune" -> Icons.Default.Tune
        "settings" -> Icons.Default.Settings
        "calculate" -> Icons.Default.Calculate
        "schedule" -> Icons.Default.Schedule
        "calendar_today" -> Icons.Default.CalendarToday
        "wb_sunny" -> Icons.Default.WbSunny
        "music_note" -> Icons.Default.MusicNote
        "edit_note" -> Icons.Default.EditNote
        "folder" -> Icons.Default.Folder
        "contacts" -> Icons.Default.Contacts
        "explore" -> Icons.Default.Explore
        "email" -> Icons.Default.Email
        "shopping_bag" -> Icons.Default.ShoppingBag
        "terminal" -> Icons.Default.Terminal
        "directions_run" -> Icons.Default.DirectionsRun
        "drawer" -> Icons.Default.Apps
        else -> Icons.Default.Widgets
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AppIconItem(
    app: AppItem,
    iconShape: IconShape,
    iconScale: Float = 1.0f,
    showLabel: Boolean = true,
    labelColor: Color = Color.White,
    useThemedIcons: Boolean = false,
    themedAccentColor: Color = Color(0xFF1A73E8),
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    onLongClick: () -> Unit = {}
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val pressScale by animateFloatAsState(
        targetValue = if (isPressed) 0.88f else 1.0f,
        label = "press_scale"
    )

    val shape = remember(iconShape) { getShapeForIcon(iconShape) }
    val vector = remember(app.iconResName) { getAppIconVector(app.iconResName) }
    val baseColor = remember(app.accentColorHex) { Color(app.accentColorHex) }

    Column(
        modifier = modifier
            .testTag("app_item_${app.id}")
            .scale(pressScale)
            .combinedClickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick,
                onLongClick = onLongClick
            )
            .padding(horizontal = 4.dp, vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val iconSize = (56 * iconScale).dp

        val backgroundModifier = if (useThemedIcons) {
            Modifier.background(themedAccentColor.copy(alpha = 0.25f))
        } else {
            Modifier.background(
                Brush.radialGradient(
                    colors = listOf(
                        baseColor.copy(alpha = 0.95f),
                        baseColor.copy(alpha = 0.7f),
                        Color(0xFF1A1C29)
                    )
                )
            )
        }

        Box(
            modifier = Modifier
                .size(iconSize)
                .shadow(
                    elevation = 6.dp,
                    shape = shape,
                    ambientColor = if (useThemedIcons) themedAccentColor.copy(alpha = 0.3f) else baseColor.copy(alpha = 0.4f),
                    spotColor = if (useThemedIcons) themedAccentColor.copy(alpha = 0.5f) else baseColor.copy(alpha = 0.6f)
                )
                .clip(shape)
                .then(backgroundModifier),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = vector,
                contentDescription = app.displayLabel,
                tint = if (useThemedIcons) themedAccentColor else Color.White,
                modifier = Modifier.size((28 * iconScale).dp)
            )
        }

        if (showLabel) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = app.displayLabel,
                color = labelColor,
                fontSize = (11.5 * iconScale).sp,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )
        }
    }
}
