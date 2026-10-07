package com.example.ui.drawer

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import kotlinx.coroutines.launch

@Composable
fun AppDrawerSheet(
    viewModel: LauncherViewModel,
    onAppClick: (AppItem) -> Unit,
    onAppLongClick: (AppItem) -> Unit,
    onDismiss: () -> Unit
) {
    BackHandler { onDismiss() }

    var searchQuery by remember { mutableStateOf("") }
    var selectedCategoryIndex by remember { mutableIntStateOf(0) }
    var showHiddenApps by remember { mutableStateOf(false) }

    val categories = remember { AppCategory.values() }
    val accentColor = Color(viewModel.settings.accentColorHex)
    val gridState = rememberLazyGridState()
    val coroutineScope = rememberCoroutineScope()

    val filteredApps by remember(viewModel.allApps, searchQuery, selectedCategoryIndex, showHiddenApps) {
        derivedStateOf {
            val selectedCategory = categories[selectedCategoryIndex]
            viewModel.allApps
                .filter { app ->
                    val matchesHidden = if (showHiddenApps) true else !app.isHidden
                    val matchesCategory = if (selectedCategory == AppCategory.ALL) {
                        true
                    } else if (selectedCategory == AppCategory.FAVORITES) {
                        app.isFavorite
                    } else {
                        app.category == selectedCategory
                    }
                    val matchesQuery = if (searchQuery.isBlank()) {
                        true
                    } else {
                        app.displayLabel.contains(searchQuery, ignoreCase = true) ||
                                app.name.contains(searchQuery, ignoreCase = true)
                    }
                    matchesHidden && matchesCategory && matchesQuery
                }
                .sortedBy { it.displayLabel.lowercase() }
        }
    }

    val alphabet = remember { ('A'..'Z').toList() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .testTag("app_drawer_sheet")
            .background(Color(0xFF0F121F).copy(alpha = 0.96f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 36.dp)
        ) {
            // Header / Search Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search all apps…", color = Color(0xFF7E8299)) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = accentColor
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Clear",
                                    tint = Color.White
                                )
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(26.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFF1B1E30),
                        unfocusedContainerColor = Color(0xFF181B2B),
                        focusedBorderColor = accentColor,
                        unfocusedBorderColor = Color(0xFF282C44),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                    onClick = { showHiddenApps = !showHiddenApps },
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(if (showHiddenApps) accentColor.copy(alpha = 0.3f) else Color(0xFF1B1E30))
                ) {
                    Icon(
                        imageVector = if (showHiddenApps) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = "Toggle Hidden",
                        tint = if (showHiddenApps) accentColor else Color(0xFF888B9E)
                    )
                }

                Spacer(modifier = Modifier.width(6.dp))

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF1B1E30))
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close Drawer",
                        tint = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Categories Tab Row
            ScrollableTabRow(
                selectedTabIndex = selectedCategoryIndex,
                containerColor = Color.Transparent,
                contentColor = accentColor,
                edgePadding = 16.dp,
                divider = {}
            ) {
                categories.forEachIndexed { index, cat ->
                    Tab(
                        selected = selectedCategoryIndex == index,
                        onClick = { selectedCategoryIndex = index },
                        text = {
                            Text(
                                text = cat.title,
                                color = if (selectedCategoryIndex == index) accentColor else Color(0xFF8B8FA8),
                                fontWeight = if (selectedCategoryIndex == index) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 13.sp
                            )
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Main Apps Grid + Alphabet Index Scrubber
            Row(modifier = Modifier.fillMaxSize()) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(viewModel.settings.drawerColumns),
                    state = gridState,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(filteredApps, key = { it.id }) { app ->
                        AppIconItem(
                            app = app,
                            iconShape = viewModel.settings.iconShape,
                            iconScale = viewModel.settings.iconScale * 0.95f,
                            showLabel = true,
                            onClick = { onAppClick(app) },
                            onLongClick = { onAppLongClick(app) }
                        )
                    }
                }

                // Alphabet Index Scrubber
                Column(
                    modifier = Modifier
                        .width(24.dp)
                        .fillMaxHeight()
                        .padding(vertical = 4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceAround
                ) {
                    alphabet.forEach { letter ->
                        Text(
                            text = letter.toString(),
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF6B7088),
                            modifier = Modifier
                                .clickable {
                                    val index = filteredApps.indexOfFirst {
                                        it.displayLabel.startsWith(letter, ignoreCase = true)
                                    }
                                    if (index != -1) {
                                        coroutineScope.launch {
                                            gridState.animateScrollToItem(index)
                                        }
                                    }
                                }
                                .padding(vertical = 1.dp)
                        )
                    }
                }
            }
        }
    }
}
