package com.example.ui.home

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppItem
import com.example.model.FolderItem
import com.example.model.IconShape

@Composable
fun FolderIconItem(
    folder: FolderItem,
    apps: List<AppItem>,
    iconShape: IconShape,
    iconScale: Float = 1.0f,
    showLabel: Boolean = true,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val pressScale by animateFloatAsState(if (isPressed) 0.88f else 1.0f, label = "folder_scale")

    val shape = remember(iconShape) { getShapeForIcon(iconShape) }
    val folderApps = remember(folder.appIds, apps) {
        folder.appIds.mapNotNull { id -> apps.find { it.id == id } }.take(4)
    }

    Column(
        modifier = modifier
            .testTag("folder_icon_${folder.id}")
            .scale(pressScale)
            .clickable(interactionSource = interactionSource, indication = null, onClick = onClick)
            .padding(horizontal = 4.dp, vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val iconSize = (56 * iconScale).dp

        Box(
            modifier = Modifier
                .size(iconSize)
                .shadow(6.dp, shape)
                .clip(shape)
                .background(Color(0xFF24273E).copy(alpha = 0.85f))
                .padding(6.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceAround,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(horizontalArrangement = Arrangement.SpaceAround) {
                    val app1 = folderApps.getOrNull(0)
                    val app2 = folderApps.getOrNull(1)
                    if (app1 != null) {
                        MiniIcon(app1)
                    }
                    if (app2 != null) {
                        MiniIcon(app2)
                    }
                }
                Row(horizontalArrangement = Arrangement.SpaceAround) {
                    val app3 = folderApps.getOrNull(2)
                    val app4 = folderApps.getOrNull(3)
                    if (app3 != null) {
                        MiniIcon(app3)
                    }
                    if (app4 != null) {
                        MiniIcon(app4)
                    }
                }
            }
        }

        if (showLabel) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = folder.name,
                color = Color.White,
                fontSize = (11.5 * iconScale).sp,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun MiniIcon(app: AppItem) {
    val vector = remember(app.iconResName) { getAppIconVector(app.iconResName) }
    Box(
        modifier = Modifier
            .padding(2.dp)
            .size(16.dp)
            .clip(androidx.compose.foundation.shape.CircleShape)
            .background(Color(app.accentColorHex)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = vector,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(10.dp)
        )
    }
}
