package com.example.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.RemoveCircleOutline
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.AppItem

@Composable
fun AppContextMenu(
    app: AppItem,
    onEdit: () -> Unit,
    onRemoveFromDesktop: () -> Unit,
    onToggleHide: () -> Unit,
    onAppInfo: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("app_context_menu_${app.id}"),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E2135))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = app.displayLabel,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                )

                Divider(color = Color(0xFF2E324E), modifier = Modifier.padding(vertical = 8.dp))

                ContextMenuItem(
                    icon = Icons.Default.Edit,
                    label = "Edit Label & Icon",
                    onClick = onEdit
                )

                ContextMenuItem(
                    icon = Icons.Default.RemoveCircleOutline,
                    label = "Remove from Desktop",
                    onClick = onRemoveFromDesktop
                )

                ContextMenuItem(
                    icon = Icons.Default.VisibilityOff,
                    label = if (app.isHidden) "Unhide in Drawer" else "Hide in Drawer",
                    onClick = onToggleHide
                )

                ContextMenuItem(
                    icon = Icons.Default.Info,
                    label = "App Info",
                    onClick = onAppInfo
                )
            }
        }
    }
}

@Composable
private fun ContextMenuItem(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = Color(0xFF9FA4C4),
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(14.dp))
        Text(
            text = label,
            fontSize = 15.sp,
            color = Color.White,
            fontWeight = FontWeight.Normal
        )
    }
}
