package com.example.data

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.model.AppItem
import com.example.model.FolderItem
import com.example.model.IconShape
import com.example.model.LauncherSettings
import com.example.model.OSDesignStyle
import com.example.model.ThemeMode
import com.example.model.WidgetItem
import com.example.model.WidgetType

sealed class ActiveOverlay {
    data object None : ActiveOverlay()
    data object AppDrawer : ActiveOverlay()
    data object UniversalSearch : ActiveOverlay()
    data object ControlCenter : ActiveOverlay()
    data object EditDesktop : ActiveOverlay()
    data object NovaSettings : ActiveOverlay()
    data class FolderView(val folder: FolderItem) : ActiveOverlay()
    data class AppContext(val app: AppItem) : ActiveOverlay()
    data class EditAppShortcut(val app: AppItem) : ActiveOverlay()
    data class SimulatedAppView(val app: AppItem) : ActiveOverlay()
}

class LauncherViewModel : ViewModel() {

    private var store: WorkspaceStore? = null

    var settings by mutableStateOf(LauncherSettings())
        private set

    val allApps = mutableStateListOf<AppItem>()
    val desktopPage0AppIds = mutableStateListOf<String>()
    val desktopPage1AppIds = mutableStateListOf<String>()
    val dockAppIds = mutableStateListOf<String>()
    val folders = mutableStateListOf<FolderItem>()
    val widgets = mutableStateListOf<WidgetItem>()

    var activeOverlay by mutableStateOf<ActiveOverlay>(ActiveOverlay.None)
    var currentDesktopPage by mutableIntStateOf(0)
    var isDeviceLocked by mutableStateOf(false)

    // Quick Note widget content
    var quickNoteText by mutableStateOf("Welcome to Nova Launcher!\nCustomize desktop, widgets, icon shapes and gestures in Nova Settings.")

    // Music widget state
    var isMusicPlaying by mutableStateOf(false)
    var currentSongTitle by mutableStateOf("Midnight City")
    var currentSongArtist by mutableStateOf("M83 • Hurry Up, We're Dreaming")
    var songProgress by mutableFloatStateOf(0.42f)

    // Quick System Toggles state
    var isWifiEnabled by mutableStateOf(true)
    var isBluetoothEnabled by mutableStateOf(true)
    var isFlashlightOn by mutableStateOf(false)
    var isDndEnabled by mutableStateOf(false)
    var brightnessLevel by mutableFloatStateOf(0.75f)
    var batteryPercentage by mutableIntStateOf(88)

    init {
        resetToDefaults()
    }

    fun initDeviceApps(context: Context) {
        val s = WorkspaceStore(context)
        store = s

        // Load saved settings
        settings = s.loadSettings()
        quickNoteText = s.loadQuickNote(quickNoteText)

        val savedPage0 = s.loadDesktopPage(0, desktopPage0AppIds.toList())
        if (savedPage0.isNotEmpty()) {
            desktopPage0AppIds.clear()
            desktopPage0AppIds.addAll(savedPage0)
        }

        val savedPage1 = s.loadDesktopPage(1, desktopPage1AppIds.toList())
        if (savedPage1.isNotEmpty()) {
            desktopPage1AppIds.clear()
            desktopPage1AppIds.addAll(savedPage1)
        }

        val savedDock = s.loadDock(dockAppIds.toList())
        if (savedDock.isNotEmpty()) {
            dockAppIds.clear()
            dockAppIds.addAll(savedDock)
        }

        val extra = AppRepository.loadDeviceInstalledApps(context)
        for (item in extra) {
            if (allApps.none { it.id == item.id || it.packageName == item.packageName }) {
                allApps.add(item)
            }
        }
    }

    fun resetToDefaults() {
        allApps.clear()
        allApps.addAll(AppRepository.getInitialApps())

        desktopPage0AppIds.clear()
        desktopPage0AppIds.addAll(listOf("camera", "photos", "nova_settings", "settings", "calculator", "clock", "weather", "notes"))

        desktopPage1AppIds.clear()
        desktopPage1AppIds.addAll(listOf("calendar", "music", "files", "contacts", "maps", "email", "terminal", "fitness"))

        dockAppIds.clear()
        dockAppIds.addAll(listOf("phone", "messages", "__DRAWER_ICON__", "browser", "playstore"))

        folders.clear()
        folders.add(
            FolderItem(
                id = "folder_essentials",
                name = "Essentials",
                appIds = listOf("calendar", "notes", "calculator", "weather"),
                pageIndex = 0,
                positionIndex = 0
            )
        )

        widgets.clear()
        widgets.add(WidgetItem("w_clock", WidgetType.NOTHING_CLOCK, pageIndex = 0, order = 0))
        widgets.add(WidgetItem("w_search", WidgetType.SEARCH_BAR, pageIndex = 0, order = 1))
        widgets.add(WidgetItem("w_toggles", WidgetType.SYSTEM_TOGGLES, pageIndex = 1, order = 0))
        widgets.add(WidgetItem("w_music", WidgetType.MUSIC_PLAYER, pageIndex = 1, order = 1))
        widgets.add(WidgetItem("w_note", WidgetType.QUICK_NOTE, pageIndex = 1, order = 2))

        settings = LauncherSettings()
        store?.saveSettings(settings)
    }

    fun updateSettings(newSettings: LauncherSettings) {
        settings = newSettings
        store?.saveSettings(newSettings)
    }

    fun setWallpaper(wallpaperId: String) {
        updateSettings(settings.copy(wallpaperId = wallpaperId))
    }

    fun setIconShape(shape: IconShape) {
        updateSettings(settings.copy(iconShape = shape))
    }

    fun setGrid(rows: Int, cols: Int) {
        updateSettings(settings.copy(gridRows = rows, gridCols = cols))
    }

    fun setAccentColor(colorHex: Long) {
        updateSettings(settings.copy(accentColorHex = colorHex))
    }

    fun setThemeMode(mode: ThemeMode) {
        updateSettings(settings.copy(themeMode = mode))
    }

    fun toggleAppHidden(appId: String) {
        val index = allApps.indexOfFirst { it.id == appId }
        if (index != -1) {
            val item = allApps[index]
            allApps[index] = item.copy(isHidden = !item.isHidden)
        }
    }

    fun updateAppCustomLabel(appId: String, newLabel: String) {
        val index = allApps.indexOfFirst { it.id == appId }
        if (index != -1) {
            val item = allApps[index]
            allApps[index] = item.copy(customLabel = newLabel.ifBlank { null })
        }
    }

    fun addAppToDesktop(appId: String, page: Int = currentDesktopPage) {
        if (page == 0) {
            if (!desktopPage0AppIds.contains(appId)) {
                desktopPage0AppIds.add(appId)
                store?.saveDesktopPage(0, desktopPage0AppIds)
            }
        } else {
            if (!desktopPage1AppIds.contains(appId)) {
                desktopPage1AppIds.add(appId)
                store?.saveDesktopPage(1, desktopPage1AppIds)
            }
        }
    }

    fun removeAppFromDesktop(appId: String, page: Int = currentDesktopPage) {
        if (page == 0) {
            desktopPage0AppIds.remove(appId)
            store?.saveDesktopPage(0, desktopPage0AppIds)
        } else {
            desktopPage1AppIds.remove(appId)
            store?.saveDesktopPage(1, desktopPage1AppIds)
        }
    }

    fun addWidget(type: WidgetType, page: Int = currentDesktopPage) {
        val newId = "widget_${System.currentTimeMillis()}"
        widgets.add(WidgetItem(newId, type, pageIndex = page, order = widgets.size))
    }

    fun removeWidget(widgetId: String) {
        widgets.removeAll { it.id == widgetId }
    }

    fun createFolder(name: String, appIds: List<String>, page: Int = currentDesktopPage) {
        val newFolder = FolderItem(
            id = "folder_${System.currentTimeMillis()}",
            name = name,
            appIds = appIds,
            pageIndex = page,
            positionIndex = folders.size
        )
        folders.add(newFolder)
    }

    fun removeFolder(folderId: String) {
        folders.removeAll { it.id == folderId }
    }

    fun openApp(app: AppItem, context: Context) {
        if (app.id.startsWith("pkg_")) {
            try {
                val launchIntent = context.packageManager.getLaunchIntentForPackage(app.packageName)
                if (launchIntent != null) {
                    context.startActivity(launchIntent)
                    return
                }
            } catch (_: Exception) {}
        }
        activeOverlay = ActiveOverlay.SimulatedAppView(app)
    }

    fun handleGesture(gestureType: String) {
        when (gestureType) {
            "double_tap" -> {
                when (settings.doubleTapAction) {
                    "search" -> activeOverlay = ActiveOverlay.UniversalSearch
                    "settings" -> activeOverlay = ActiveOverlay.NovaSettings
                    "control_center" -> activeOverlay = ActiveOverlay.ControlCenter
                }
            }
            "swipe_down" -> {
                when (settings.swipeDownAction) {
                    "control_center" -> activeOverlay = ActiveOverlay.ControlCenter
                    "search" -> activeOverlay = ActiveOverlay.UniversalSearch
                }
            }
            "swipe_up" -> {
                activeOverlay = ActiveOverlay.AppDrawer
            }
        }
    }
}
