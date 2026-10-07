package com.example.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.LauncherViewModel
import com.example.model.FolderItem

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FolderDialog(
    folder: FolderItem,
    viewModel: LauncherViewModel,
    onAppClick: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var isEditingName by remember { mutableStateOf(false) }
    var folderName by remember { mutableStateOf(folder.name) }
    val accentColor = Color(viewModel.settings.accentColorHex)

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("folder_dialog_${folder.id}"),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF1B1E30)
            )
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (isEditingName) {
                        OutlinedTextField(
                            value = folderName,
                            onValueChange = { folderName = it },
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                        TextButton(onClick = {
                            val idx = viewModel.folders.indexOfFirst { it.id == folder.id }
                            if (idx != -1) {
                                viewModel.folders[idx] = folder.copy(name = folderName)
                            }
                            isEditingName = false
                        }) {
                            Text("Save", color = accentColor)
                        }
                    } else {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable { isEditingName = true }
                        ) {
                            Text(
                                text = folder.name,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit name",
                                tint = Color(0xFF888B9E),
                                modifier = Modifier.size(16.dp)
                            )
                        }

                        Row {
                            IconButton(onClick = {
                                viewModel.removeFolder(folder.id)
                                onDismiss()
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete folder",
                                    tint = Color(0xFFFF5252)
                                )
                            }
                            IconButton(onClick = onDismiss) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Close",
                                    tint = Color.White
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Apps in folder
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalArrangement = Arrangement.Center
                ) {
                    for (appId in folder.appIds) {
                        val app = viewModel.allApps.find { it.id == appId }
                        if (app != null) {
                            AppIconItem(
                                app = app,
                                iconShape = viewModel.settings.iconShape,
                                iconScale = viewModel.settings.iconScale,
                                showLabel = true,
                                onClick = {
                                    onAppClick(app.id)
                                    onDismiss()
                                },
                                modifier = Modifier.padding(4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
