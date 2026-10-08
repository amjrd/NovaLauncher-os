package com.example.ui.settings

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Gesture
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material.icons.filled.ViewList
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import com.example.model.DockBackgroundStyle
import com.example.model.IconShape
import com.example.model.OSDesignStyle
import com.example.model.ThemeMode
import com.example.theme.AccentColors
import com.example.ui.home.getShapeForIcon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NovaSettingsScreen(
    viewModel: LauncherViewModel,
    onBack: () -> Unit
) {
    BackHandler { onBack() }

    val settings = viewModel.settings
    val accentColor = Color(settings.accentColorHex)
    var showHiddenAppsManager by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag("nova_settings_screen")
            .background(Color(0xFF0C0E18))
    ) {
        TopAppBar(
            title = {
                Text(
                    text = "Lawnchair Preferences",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color.White
                )
            },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF141727))
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Section 0: OS Design Engine (Lawnchair 16, Nothing OS, iOS 18, Fusion)
            item {
                SettingsSectionHeader(title = "Flagship OS Design Engine", icon = Icons.Default.AutoAwesome, accentColor = accentColor)
                Spacer(modifier = Modifier.height(8.dp))
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OSDesignStyle.values().forEach { os ->
                        val isSelected = settings.osStyle == os
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    val newAccent = when (os) {
                                        OSDesignStyle.LAWNCHAIR_16 -> 0xFF1A73E8L
                                        OSDesignStyle.NOTHING_OS -> 0xFFD71921L
                                        OSDesignStyle.IOS -> 0xFF007AFFL
                                        OSDesignStyle.FUSION -> 0xFFD71921L
                                    }
                                    val newDock = when (os) {
                                        OSDesignStyle.IOS, OSDesignStyle.FUSION -> DockBackgroundStyle.IOS_FROSTED_GLASS
                                        OSDesignStyle.NOTHING_OS -> DockBackgroundStyle.GLASS_BLUR
                                        OSDesignStyle.LAWNCHAIR_16 -> DockBackgroundStyle.CARD_SHADOW
                                    }
                                    val newShape = when (os) {
                                        OSDesignStyle.IOS, OSDesignStyle.FUSION -> IconShape.SQUIRCLE
                                        OSDesignStyle.NOTHING_OS -> IconShape.CIRCLE
                                        OSDesignStyle.LAWNCHAIR_16 -> IconShape.ROUNDED_SQUARE
                                    }
                                    viewModel.updateSettings(
                                        settings.copy(
                                            osStyle = os,
                                            accentColorHex = newAccent,
                                            dockBackgroundStyle = newDock,
                                            iconShape = newShape,
                                            useThemedIcons = (os == OSDesignStyle.LAWNCHAIR_16)
                                        )
                                    )
                                },
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected) accentColor.copy(alpha = 0.22f) else Color(0xFF1A1D30)
                            ),
                            border = if (isSelected) androidx.compose.foundation.BorderStroke(2.dp, accentColor) else null
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = isSelected,
                                    onClick = null,
                                    colors = RadioButtonDefaults.colors(selectedColor = accentColor)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = os.title,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                    Text(
                                        text = os.subtitle,
                                        fontSize = 11.sp,
                                        color = if (isSelected) accentColor else Color(0xFF8B8FA8)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Section 1: Look & Feel
            item {
                SettingsSectionHeader(title = "Look & Feel", icon = Icons.Default.ColorLens, accentColor = accentColor)
                Spacer(modifier = Modifier.height(8.dp))
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1D30))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        // Icon Shape Selector
                        Text(
                            text = "Icon Shape",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            IconShape.values().forEach { shape ->
                                val isSelected = settings.iconShape == shape
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.clickable { viewModel.setIconShape(shape) }
                                ) {
                                    val composeShape = getShapeForIcon(shape)
                                    Box(
                                        modifier = Modifier
                                            .size(46.dp)
                                            .clip(composeShape)
                                            .background(if (isSelected) accentColor else Color(0xFF2C3048))
                                            .then(
                                                if (isSelected) Modifier.border(2.dp, Color.White, composeShape)
                                                else Modifier
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        if (isSelected) {
                                            Icon(
                                                imageVector = Icons.Default.Check,
                                                contentDescription = null,
                                                tint = Color.Black,
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = shape.name.lowercase().replace("_", " ").capitalizeWords(),
                                        fontSize = 10.sp,
                                        color = if (isSelected) Color.White else Color(0xFF7D829C)
                                    )
                                }
                            }
                        }

                        Divider(color = Color(0xFF292C45), modifier = Modifier.padding(vertical = 14.dp))

                        // Themed Icons Toggle (Material You)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Themed Icons",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = "Tint app icons with Material You colors",
                                    fontSize = 11.sp,
                                    color = Color(0xFF7E839E)
                                )
                            }
                            Switch(
                                checked = settings.useThemedIcons,
                                onCheckedChange = {
                                    viewModel.updateSettings(settings.copy(useThemedIcons = it))
                                },
                                colors = SwitchDefaults.colors(checkedThumbColor = accentColor, checkedTrackColor = accentColor.copy(alpha = 0.5f))
                            )
                        }

                        Divider(color = Color(0xFF292C45), modifier = Modifier.padding(vertical = 14.dp))

                        // Theme Mode
                        Text(
                            text = "Theme Mode",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            ThemeMode.values().forEach { mode ->
                                val selected = settings.themeMode == mode
                                Card(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(horizontal = 3.dp)
                                        .clickable { viewModel.setThemeMode(mode) },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = if (selected) accentColor.copy(alpha = 0.25f) else Color(0xFF25283D)
                                    )
                                ) {
                                    Text(
                                        text = mode.name,
                                        fontSize = 11.sp,
                                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (selected) accentColor else Color.White,
                                        modifier = Modifier.padding(vertical = 10.dp, horizontal = 4.dp)
                                    )
                                }
                            }
                        }

                        Divider(color = Color(0xFF292C45), modifier = Modifier.padding(vertical = 14.dp))

                        // Accent Color Palette
                        Text(
                            text = "Accent Color",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            items(AccentColors) { (hex, name) ->
                                val selected = settings.accentColorHex == hex
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(Color(hex))
                                        .clickable { viewModel.setAccentColor(hex) }
                                        .then(
                                            if (selected) Modifier.border(3.dp, Color.White, CircleShape)
                                            else Modifier
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (selected) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = name,
                                            tint = if (hex == 0xFFFFFFFF) Color.Black else Color.White,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Section 2: Desktop Layout
            item {
                SettingsSectionHeader(title = "Desktop Layout", icon = Icons.Default.Dashboard, accentColor = accentColor)
                Spacer(modifier = Modifier.height(8.dp))
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1D30))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        // Desktop Grid Size
                        Text(
                            text = "Grid Size: ${settings.gridRows} Rows × ${settings.gridCols} Columns",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            val gridOptions = listOf(4 to 4, 5 to 4, 5 to 5, 6 to 5)
                            gridOptions.forEach { (r, c) ->
                                val isSelected = settings.gridRows == r && settings.gridCols == c
                                Card(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(horizontal = 3.dp)
                                        .clickable { viewModel.setGrid(r, c) },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = if (isSelected) accentColor else Color(0xFF25283D)
                                    )
                                ) {
                                    Text(
                                        text = "${r}×${c}",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) Color.Black else Color.White,
                                        modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp)
                                    )
                                }
                            }
                        }

                        Divider(color = Color(0xFF292C45), modifier = Modifier.padding(vertical = 14.dp))

                        // Icon Size Scale
                        Text(
                            text = "Icon Size: ${(settings.iconScale * 100).toInt()}%",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Slider(
                            value = settings.iconScale,
                            onValueChange = { viewModel.updateSettings(settings.copy(iconScale = it)) },
                            valueRange = 0.8f..1.3f,
                            colors = SliderDefaults.colors(thumbColor = accentColor, activeTrackColor = accentColor)
                        )

                        Divider(color = Color(0xFF292C45), modifier = Modifier.padding(vertical = 10.dp))

                        // Show Labels Switch
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "App Icon Labels",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = "Show names under icons on desktop",
                                    fontSize = 11.sp,
                                    color = Color(0xFF7E839E)
                                )
                            }
                            Switch(
                                checked = settings.showLabels,
                                onCheckedChange = { viewModel.updateSettings(settings.copy(showLabels = it)) },
                                colors = SwitchDefaults.colors(checkedThumbColor = accentColor, checkedTrackColor = accentColor.copy(alpha = 0.5f))
                            )
                        }
                    }
                }
            }

            // Section 3: App Drawer & Dock
            item {
                SettingsSectionHeader(title = "App Drawer & Dock", icon = Icons.Default.ViewList, accentColor = accentColor)
                Spacer(modifier = Modifier.height(8.dp))
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1D30))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        // Drawer Columns
                        Text(
                            text = "Drawer Grid Columns: ${settings.drawerColumns}",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            listOf(3, 4, 5).forEach { cols ->
                                val isSelected = settings.drawerColumns == cols
                                Card(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable { viewModel.updateSettings(settings.copy(drawerColumns = cols)) },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = if (isSelected) accentColor else Color(0xFF25283D)
                                    )
                                ) {
                                    Text(
                                        text = "$cols Columns",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) Color.Black else Color.White,
                                        modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp)
                                    )
                                }
                            }
                        }

                        Divider(color = Color(0xFF292C45), modifier = Modifier.padding(vertical = 14.dp))

                        // Dock Background Style
                        Text(
                            text = "Dock Background Style",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        DockBackgroundStyle.values().forEach { style ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { viewModel.updateSettings(settings.copy(dockBackgroundStyle = style)) }
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = settings.dockBackgroundStyle == style,
                                    onClick = { viewModel.updateSettings(settings.copy(dockBackgroundStyle = style)) },
                                    colors = RadioButtonDefaults.colors(selectedColor = accentColor)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = style.name.lowercase().replace("_", " ").capitalizeWords(),
                                    fontSize = 13.sp,
                                    color = Color.White
                                )
                            }
                        }

                        Divider(color = Color(0xFF292C45), modifier = Modifier.padding(vertical = 10.dp))

                        // Manage Hidden Apps Row
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { showHiddenAppsManager = !showHiddenAppsManager }
                                .padding(vertical = 6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Manage Hidden Apps",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = "${viewModel.allApps.count { it.isHidden }} apps currently hidden",
                                    fontSize = 11.sp,
                                    color = Color(0xFF7E839E)
                                )
                            }
                            Icon(
                                imageVector = if (showHiddenAppsManager) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = null,
                                tint = accentColor
                            )
                        }

                        if (showHiddenAppsManager) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color(0xFF131522), RoundedCornerShape(12.dp))
                                    .padding(8.dp)
                            ) {
                                viewModel.allApps.forEach { app ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 6.dp, horizontal = 4.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(text = app.displayLabel, color = Color.White, fontSize = 13.sp)
                                        Switch(
                                            checked = app.isHidden,
                                            onCheckedChange = { viewModel.toggleAppHidden(app.id) },
                                            colors = SwitchDefaults.colors(checkedThumbColor = accentColor)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Section 4: Gestures & Actions
            item {
                SettingsSectionHeader(title = "Gestures", icon = Icons.Default.Gesture, accentColor = accentColor)
                Spacer(modifier = Modifier.height(8.dp))
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1D30))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        GestureOptionRow(
                            title = "Double Tap Desktop",
                            currentValue = settings.doubleTapAction.capitalizeWords(),
                            options = listOf("search" to "Universal Search", "control_center" to "Control Center", "settings" to "OS Settings"),
                            accentColor = accentColor,
                            onSelect = { viewModel.updateSettings(settings.copy(doubleTapAction = it)) }
                        )

                        Divider(color = Color(0xFF292C45), modifier = Modifier.padding(vertical = 12.dp))

                        GestureOptionRow(
                            title = "Swipe Down",
                            currentValue = settings.swipeDownAction.capitalizeWords(),
                            options = listOf("control_center" to "Control Center", "search" to "Universal Search"),
                            accentColor = accentColor,
                            onSelect = { viewModel.updateSettings(settings.copy(swipeDownAction = it)) }
                        )
                    }
                }
            }

            // Section 5: Reset & About
            item {
                SettingsSectionHeader(title = "System & Reset", icon = Icons.Default.Restore, accentColor = accentColor)
                Spacer(modifier = Modifier.height(8.dp))
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1D30))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { viewModel.resetToDefaults() }
                                .padding(vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Restore,
                                contentDescription = null,
                                tint = Color(0xFFFF5252),
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Reset Layout & Settings",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFFF5252)
                                )
                                Text(
                                    text = "Restores default desktop icons, dock and widgets",
                                    fontSize = 11.sp,
                                    color = Color(0xFF888B9E)
                                )
                            }
                        }

                        Divider(color = Color(0xFF292C45), modifier = Modifier.padding(vertical = 12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null,
                                tint = accentColor,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Lawnchair 16 Dev (Android 16 Quickstep)",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = "Smartspacer At a Glance, Material You Themed Icons & Monet engine",
                                    fontSize = 11.sp,
                                    color = Color(0xFF888B9E)
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun SettingsSectionHeader(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    accentColor: Color
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = accentColor,
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = title,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}

@Composable
private fun GestureOptionRow(
    title: String,
    currentValue: String,
    options: List<Pair<String, String>>,
    accentColor: Color,
    onSelect: (String) -> Unit
) {
    Column {
        Text(
            text = title,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(6.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            options.forEach { (key, label) ->
                val isSelected = currentValue.equals(key, ignoreCase = true) || currentValue.equals(label, ignoreCase = true)
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onSelect(key) },
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) accentColor else Color(0xFF25283D)
                    )
                ) {
                    Text(
                        text = label,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = if (isSelected) Color.Black else Color.White,
                        modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp)
                    )
                }
            }
        }
    }
}

private fun String.capitalizeWords(): String =
    split(" ").joinToString(" ") { it.replaceFirstChar { char -> char.uppercase() } }
