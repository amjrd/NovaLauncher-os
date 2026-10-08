package com.example.ui.home

import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BatteryChargingFull
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.SignalCellularAlt
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ActiveOverlay
import com.example.data.AppRepository
import com.example.data.LauncherViewModel
import com.example.model.OSDesignStyle
import com.example.ui.applibrary.IOSAppLibrarySheet
import com.example.ui.control.IOSControlCenter
import com.example.ui.control.QuickControlCenterSheet
import com.example.ui.drawer.AppDrawerSheet
import com.example.ui.dynamicisland.DynamicIsland
import com.example.ui.lockscreen.LockScreenView
import com.example.ui.search.UniversalSearchOverlay
import com.example.ui.settings.NovaSettingsScreen
import com.example.ui.simulatedapps.SimulatedAppContainer
import com.example.ui.widgets.ClockWeatherWidget
import com.example.ui.widgets.MusicPlayerWidget
import com.example.ui.widgets.NothingClockWidget
import com.example.ui.widgets.NovaSearchBarWidget
import com.example.ui.widgets.PixelAtAGlanceWidget
import com.example.ui.widgets.QuickNoteWidget
import com.example.ui.widgets.SystemTogglesWidget
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HomeScreen(
    viewModel: LauncherViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val settings = viewModel.settings
    val accentColor = Color(settings.accentColorHex)

    // If device is in lock screen mode
    if (viewModel.isDeviceLocked) {
        LockScreenView(
            viewModel = viewModel,
            onUnlock = { viewModel.isDeviceLocked = false }
        )
        return
    }

    // Hardware Back press handling
    BackHandler(enabled = viewModel.activeOverlay !is ActiveOverlay.None) {
        viewModel.activeOverlay = ActiveOverlay.None
    }

    // Wallpaper Gradient calculation
    val currentWallpaper = remember(settings.wallpaperId) {
        AppRepository.defaultWallpapers.find { it.id == settings.wallpaperId }
            ?: AppRepository.defaultWallpapers.first()
    }
    val backgroundBrush = remember(currentWallpaper) {
        Brush.verticalGradient(
            colors = currentWallpaper.previewGradientColors.map { Color(it) }
        )
    }

    val pagerState = rememberPagerState(initialPage = 0, pageCount = { 2 })

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            viewModel.currentDesktopPage = page
        }
    }

    // Status bar clock
    var statusTime by remember { mutableStateOf("") }
    LaunchedEffect(Unit) {
        val format = SimpleDateFormat("h:mm", Locale.getDefault())
        while (true) {
            statusTime = format.format(Date())
            delay(2000)
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .testTag("home_screen_container")
            .background(backgroundBrush)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .pointerInput(Unit) {
                    detectTapGestures(
                        onDoubleTap = { viewModel.isDeviceLocked = true },
                        onLongPress = { viewModel.activeOverlay = ActiveOverlay.EditDesktop }
                    )
                }
        ) {
            // Apple Dynamic Island (shown in iOS and Fusion mode)
            if (settings.osStyle == OSDesignStyle.IOS || settings.osStyle == OSDesignStyle.FUSION) {
                DynamicIsland(viewModel = viewModel)
            }

            // Top Status Bar Area
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp, start = 18.dp, end = 18.dp, bottom = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = statusTime.ifEmpty { "10:24" },
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                // Quick Lock Button & Status Indicators
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.SignalCellularAlt,
                        contentDescription = "Cellular",
                        tint = Color.White,
                        modifier = Modifier.size(13.dp)
                    )
                    Icon(
                        imageVector = Icons.Default.Wifi,
                        contentDescription = "Wi-Fi",
                        tint = if (viewModel.isWifiEnabled) Color.White else Color(0xFF6B7088),
                        modifier = Modifier.size(13.dp)
                    )
                    Icon(
                        imageVector = Icons.Default.BatteryChargingFull,
                        contentDescription = "Battery",
                        tint = Color(0xFF34C759),
                        modifier = Modifier.size(15.dp)
                    )
                    IconButton(
                        onClick = { viewModel.isDeviceLocked = true },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Lock",
                            tint = Color.White.copy(alpha = 0.85f),
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }

            // 1-Tap Flagship OS Switcher Toolbar
            OSSwitcherBar(viewModel = viewModel)

            // Desktop Horizontal Pager
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) { page ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 14.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.Top
                ) {
                    if (page == 0) {
                        // Page 0 Widgets according to active OS Style
                        when (settings.osStyle) {
                            OSDesignStyle.NOTHING_OS -> {
                                NothingClockWidget(
                                    onClockClick = {
                                        val clockApp = viewModel.allApps.find { it.id == "clock" }
                                        if (clockApp != null) viewModel.openApp(clockApp, context)
                                    },
                                    onWeatherClick = {
                                        val weatherApp = viewModel.allApps.find { it.id == "weather" }
                                        if (weatherApp != null) viewModel.openApp(weatherApp, context)
                                    },
                                    modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
                                )
                            }
                            OSDesignStyle.LAWNCHAIR_16 -> {
                                PixelAtAGlanceWidget(
                                    onCalendarClick = {
                                        val cal = viewModel.allApps.find { it.id == "calendar" }
                                        if (cal != null) viewModel.openApp(cal, context)
                                    },
                                    onWeatherClick = {
                                        val wth = viewModel.allApps.find { it.id == "weather" }
                                        if (wth != null) viewModel.openApp(wth, context)
                                    },
                                    modifier = Modifier.padding(top = 8.dp, bottom = 6.dp)
                                )
                                NovaSearchBarWidget(
                                    onSearchClick = { viewModel.activeOverlay = ActiveOverlay.UniversalSearch },
                                    accentColor = accentColor,
                                    modifier = Modifier.padding(bottom = 10.dp)
                                )
                            }
                            OSDesignStyle.IOS -> {
                                ClockWeatherWidget(
                                    onClockClick = {
                                        val clockApp = viewModel.allApps.find { it.id == "clock" }
                                        if (clockApp != null) viewModel.openApp(clockApp, context)
                                    },
                                    onWeatherClick = {
                                        val weatherApp = viewModel.allApps.find { it.id == "weather" }
                                        if (weatherApp != null) viewModel.openApp(weatherApp, context)
                                    },
                                    accentColor = accentColor,
                                    modifier = Modifier.padding(top = 6.dp, bottom = 10.dp)
                                )
                            }
                            OSDesignStyle.FUSION -> {
                                NothingClockWidget(
                                    onClockClick = {
                                        val clockApp = viewModel.allApps.find { it.id == "clock" }
                                        if (clockApp != null) viewModel.openApp(clockApp, context)
                                    },
                                    onWeatherClick = {
                                        val weatherApp = viewModel.allApps.find { it.id == "weather" }
                                        if (weatherApp != null) viewModel.openApp(weatherApp, context)
                                    },
                                    modifier = Modifier.padding(top = 6.dp, bottom = 8.dp)
                                )
                                NovaSearchBarWidget(
                                    onSearchClick = { viewModel.activeOverlay = ActiveOverlay.UniversalSearch },
                                    accentColor = accentColor,
                                    modifier = Modifier.padding(bottom = 10.dp)
                                )
                            }
                        }

                        // Desktop Icons on Page 0
                        FlowRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("desktop_page_0_grid"),
                            horizontalArrangement = Arrangement.SpaceAround,
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            // Folders on page 0
                            viewModel.folders.filter { it.pageIndex == 0 }.forEach { folder ->
                                FolderIconItem(
                                    folder = folder,
                                    apps = viewModel.allApps,
                                    iconShape = settings.iconShape,
                                    iconScale = settings.iconScale,
                                    showLabel = settings.showLabels,
                                    onClick = { viewModel.activeOverlay = ActiveOverlay.FolderView(folder) }
                                )
                            }

                            // Apps on page 0
                            viewModel.desktopPage0AppIds.forEach { appId ->
                                val app = viewModel.allApps.find { it.id == appId }
                                if (app != null) {
                                    AppIconItem(
                                        app = app,
                                        iconShape = settings.iconShape,
                                        iconScale = settings.iconScale,
                                        showLabel = settings.showLabels,
                                        useThemedIcons = settings.useThemedIcons,
                                        themedAccentColor = accentColor,
                                        onClick = { viewModel.openApp(app, context) },
                                        onLongClick = { viewModel.activeOverlay = ActiveOverlay.AppContext(app) }
                                    )
                                }
                            }
                        }
                    } else {
                        // Page 1: Control Center Widget & Media Player & Quick Note
                        Spacer(modifier = Modifier.height(10.dp))

                        SystemTogglesWidget(
                            viewModel = viewModel,
                            modifier = Modifier.padding(bottom = 10.dp)
                        )

                        MusicPlayerWidget(
                            viewModel = viewModel,
                            modifier = Modifier.padding(bottom = 10.dp)
                        )

                        QuickNoteWidget(
                            viewModel = viewModel,
                            modifier = Modifier.padding(bottom = 10.dp)
                        )

                        // Desktop Icons on Page 1
                        FlowRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("desktop_page_1_grid"),
                            horizontalArrangement = Arrangement.SpaceAround,
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            viewModel.desktopPage1AppIds.forEach { appId ->
                                val app = viewModel.allApps.find { it.id == appId }
                                if (app != null) {
                                    AppIconItem(
                                        app = app,
                                        iconShape = settings.iconShape,
                                        iconScale = settings.iconScale,
                                        showLabel = settings.showLabels,
                                        useThemedIcons = settings.useThemedIcons,
                                        themedAccentColor = accentColor,
                                        onClick = { viewModel.openApp(app, context) },
                                        onLongClick = { viewModel.activeOverlay = ActiveOverlay.AppContext(app) }
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Desktop Page Indicator Dots
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(2) { index ->
                    val isSelected = pagerState.currentPage == index
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .size(if (isSelected) 8.dp else 6.dp)
                            .clip(CircleShape)
                            .background(if (isSelected) accentColor else Color.White.copy(alpha = 0.4f))
                    )
                }
            }

            // Bottom Dock Bar (iOS Frosted Glass / Nothing Minimal)
            DockBar(
                viewModel = viewModel,
                onAppClick = { appId ->
                    val app = viewModel.allApps.find { it.id == appId }
                    if (app != null) viewModel.openApp(app, context)
                },
                onAppLongClick = { appId ->
                    val app = viewModel.allApps.find { it.id == appId }
                    if (app != null) viewModel.activeOverlay = ActiveOverlay.AppContext(app)
                },
                onDrawerClick = {
                    viewModel.activeOverlay = ActiveOverlay.AppDrawer
                }
            )
        }

        // Active Overlays Layer
        when (val overlay = viewModel.activeOverlay) {
            is ActiveOverlay.AppDrawer -> {
                if (settings.osStyle == OSDesignStyle.IOS) {
                    IOSAppLibrarySheet(
                        viewModel = viewModel,
                        onAppClick = { app ->
                            viewModel.activeOverlay = ActiveOverlay.None
                            viewModel.openApp(app, context)
                        },
                        onDismiss = { viewModel.activeOverlay = ActiveOverlay.None }
                    )
                } else {
                    AppDrawerSheet(
                        viewModel = viewModel,
                        onAppClick = { app ->
                            viewModel.activeOverlay = ActiveOverlay.None
                            viewModel.openApp(app, context)
                        },
                        onAppLongClick = { app ->
                            viewModel.activeOverlay = ActiveOverlay.AppContext(app)
                        },
                        onDismiss = { viewModel.activeOverlay = ActiveOverlay.None }
                    )
                }
            }
            is ActiveOverlay.UniversalSearch -> {
                UniversalSearchOverlay(
                    viewModel = viewModel,
                    onAppClick = { app ->
                        viewModel.activeOverlay = ActiveOverlay.None
                        viewModel.openApp(app, context)
                    },
                    onDismiss = { viewModel.activeOverlay = ActiveOverlay.None }
                )
            }
            is ActiveOverlay.ControlCenter -> {
                if (settings.osStyle == OSDesignStyle.IOS || settings.osStyle == OSDesignStyle.FUSION) {
                    IOSControlCenter(
                        viewModel = viewModel,
                        onOpenCalculator = {
                            viewModel.activeOverlay = ActiveOverlay.None
                            val calc = viewModel.allApps.find { it.id == "calculator" }
                            if (calc != null) viewModel.openApp(calc, context)
                        },
                        onOpenCamera = {
                            viewModel.activeOverlay = ActiveOverlay.None
                            val cam = viewModel.allApps.find { it.id == "camera" }
                            if (cam != null) viewModel.openApp(cam, context)
                        },
                        onDismiss = { viewModel.activeOverlay = ActiveOverlay.None }
                    )
                } else {
                    QuickControlCenterSheet(
                        viewModel = viewModel,
                        onDismiss = { viewModel.activeOverlay = ActiveOverlay.None }
                    )
                }
            }
            is ActiveOverlay.EditDesktop -> {
                EditDesktopBottomSheet(
                    viewModel = viewModel,
                    onOpenSettings = { viewModel.activeOverlay = ActiveOverlay.NovaSettings },
                    onDismiss = { viewModel.activeOverlay = ActiveOverlay.None }
                )
            }
            is ActiveOverlay.NovaSettings -> {
                NovaSettingsScreen(
                    viewModel = viewModel,
                    onBack = { viewModel.activeOverlay = ActiveOverlay.None }
                )
            }
            is ActiveOverlay.FolderView -> {
                FolderDialog(
                    folder = overlay.folder,
                    viewModel = viewModel,
                    onAppClick = { appId ->
                        val app = viewModel.allApps.find { it.id == appId }
                        if (app != null) {
                            viewModel.activeOverlay = ActiveOverlay.None
                            viewModel.openApp(app, context)
                        }
                    },
                    onDismiss = { viewModel.activeOverlay = ActiveOverlay.None }
                )
            }
            is ActiveOverlay.AppContext -> {
                AppContextMenu(
                    app = overlay.app,
                    onEdit = {
                        viewModel.activeOverlay = ActiveOverlay.EditAppShortcut(overlay.app)
                    },
                    onRemoveFromDesktop = {
                        viewModel.removeAppFromDesktop(overlay.app.id)
                        viewModel.activeOverlay = ActiveOverlay.None
                    },
                    onToggleHide = {
                        viewModel.toggleAppHidden(overlay.app.id)
                        viewModel.activeOverlay = ActiveOverlay.None
                    },
                    onAppInfo = {
                        viewModel.activeOverlay = ActiveOverlay.SimulatedAppView(overlay.app)
                    },
                    onDismiss = { viewModel.activeOverlay = ActiveOverlay.None }
                )
            }
            is ActiveOverlay.EditAppShortcut -> {
                EditShortcutDialog(
                    app = overlay.app,
                    accentColor = accentColor,
                    onSave = { newName ->
                        viewModel.updateAppCustomLabel(overlay.app.id, newName)
                        viewModel.activeOverlay = ActiveOverlay.None
                    },
                    onDismiss = { viewModel.activeOverlay = ActiveOverlay.None }
                )
            }
            is ActiveOverlay.SimulatedAppView -> {
                SimulatedAppContainer(
                    app = overlay.app,
                    accentColor = accentColor,
                    onBack = { viewModel.activeOverlay = ActiveOverlay.None }
                )
            }
            ActiveOverlay.None -> {}
        }
    }
}
