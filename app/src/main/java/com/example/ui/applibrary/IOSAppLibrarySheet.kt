package com.example.ui.applibrary

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
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
import com.example.data.LauncherViewModel
import com.example.model.AppCategory
import com.example.model.AppItem
import com.example.ui.home.AppIconItem

@Composable
fun IOSAppLibrarySheet(
    viewModel: LauncherViewModel,
    onAppClick: (AppItem) -> Unit,
    onDismiss: () -> Unit
) {
    BackHandler { onDismiss() }

    var searchQuery by remember { mutableStateOf("") }
    val categories = remember {
        listOf(
            "Suggestions" to listOf("phone", "messages", "browser", "camera"),
            "Social & Talk" to listOf("messages", "phone", "contacts", "email"),
            "Productivity" to listOf("notes", "calendar", "calculator", "files"),
            "Media & Fun" to listOf("music", "photos", "camera", "browser"),
            "Utilities" to listOf("settings", "clock", "weather", "terminal")
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .testTag("ios_app_library_sheet")
            .background(Color(0xFF000000).copy(alpha = 0.94f))
            .padding(top = 36.dp, start = 16.dp, end = 16.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("App Library", color = Color(0xFF8E8E93)) },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = null, tint = Color(0xFF8E8E93))
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(18.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFF1C1C1E),
                        unfocusedContainerColor = Color(0xFF1C1C1E),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF1C1C1E))
                ) {
                    Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 2-Column iOS App Library Quad Folders
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(categories) { (catTitle, appIds) ->
                    val apps = appIds.mapNotNull { id -> viewModel.allApps.find { it.id == id } }
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1C1C1E).copy(alpha = 0.85f))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(12.dp)
                        ) {
                            Text(
                                text = catTitle,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            // 2x2 grid inside the card
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.SpaceAround
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceAround
                                ) {
                                    val a1 = apps.getOrNull(0)
                                    val a2 = apps.getOrNull(1)
                                    if (a1 != null) {
                                        AppIconItem(
                                            app = a1,
                                            iconShape = viewModel.settings.iconShape,
                                            iconScale = 0.72f,
                                            showLabel = false,
                                            onClick = { onAppClick(a1) }
                                        )
                                    }
                                    if (a2 != null) {
                                        AppIconItem(
                                            app = a2,
                                            iconShape = viewModel.settings.iconShape,
                                            iconScale = 0.72f,
                                            showLabel = false,
                                            onClick = { onAppClick(a2) }
                                        )
                                    }
                                }

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceAround
                                ) {
                                    val a3 = apps.getOrNull(2)
                                    val a4 = apps.getOrNull(3)
                                    if (a3 != null) {
                                        AppIconItem(
                                            app = a3,
                                            iconShape = viewModel.settings.iconShape,
                                            iconScale = 0.72f,
                                            showLabel = false,
                                            onClick = { onAppClick(a3) }
                                        )
                                    }
                                    if (a4 != null) {
                                        AppIconItem(
                                            app = a4,
                                            iconShape = viewModel.settings.iconShape,
                                            iconScale = 0.72f,
                                            showLabel = false,
                                            onClick = { onAppClick(a4) }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
