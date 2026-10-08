package com.example.data

import android.content.Context
import android.content.SharedPreferences
import com.example.model.DockBackgroundStyle
import com.example.model.FolderItem
import com.example.model.IconShape
import com.example.model.LauncherSettings
import com.example.model.OSDesignStyle
import com.example.model.ThemeMode
import com.example.model.WidgetItem
import com.example.model.WidgetType

class WorkspaceStore(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("nova_workspace_prefs", Context.MODE_PRIVATE)

    fun saveSettings(settings: LauncherSettings) {
        prefs.edit().apply {
            putString("os_style", settings.osStyle.name)
            putInt("grid_rows", settings.gridRows)
            putInt("grid_cols", settings.gridCols)
            putFloat("icon_scale", settings.iconScale)
            putBoolean("show_labels", settings.showLabels)
            putString("icon_shape", settings.iconShape.name)
            putString("dock_style", settings.dockBackgroundStyle.name)
            putString("theme_mode", settings.themeMode.name)
            putLong("accent_color", settings.accentColorHex)
            putString("wallpaper_id", settings.wallpaperId)
            putString("double_tap_action", settings.doubleTapAction)
            putString("swipe_down_action", settings.swipeDownAction)
            putInt("drawer_cols", settings.drawerColumns)
            apply()
        }
    }

    fun loadSettings(): LauncherSettings {
        val osStyle = try {
            OSDesignStyle.valueOf(prefs.getString("os_style", OSDesignStyle.FUSION.name) ?: OSDesignStyle.FUSION.name)
        } catch (_: Exception) {
            OSDesignStyle.FUSION
        }

        val iconShape = try {
            IconShape.valueOf(prefs.getString("icon_shape", IconShape.SQUIRCLE.name) ?: IconShape.SQUIRCLE.name)
        } catch (_: Exception) {
            IconShape.SQUIRCLE
        }

        val dockStyle = try {
            DockBackgroundStyle.valueOf(prefs.getString("dock_style", DockBackgroundStyle.IOS_FROSTED_GLASS.name) ?: DockBackgroundStyle.IOS_FROSTED_GLASS.name)
        } catch (_: Exception) {
            DockBackgroundStyle.IOS_FROSTED_GLASS
        }

        val themeMode = try {
            ThemeMode.valueOf(prefs.getString("theme_mode", ThemeMode.DARK.name) ?: ThemeMode.DARK.name)
        } catch (_: Exception) {
            ThemeMode.DARK
        }

        return LauncherSettings(
            osStyle = osStyle,
            gridRows = prefs.getInt("grid_rows", 5),
            gridCols = prefs.getInt("grid_cols", 4),
            iconScale = prefs.getFloat("icon_scale", 1.0f),
            showLabels = prefs.getBoolean("show_labels", true),
            iconShape = iconShape,
            dockBackgroundStyle = dockStyle,
            themeMode = themeMode,
            accentColorHex = prefs.getLong("accent_color", 0xFFD71921L),
            wallpaperId = prefs.getString("wallpaper_id", "nothing_dark") ?: "nothing_dark",
            doubleTapAction = prefs.getString("double_tap_action", "search") ?: "search",
            swipeDownAction = prefs.getString("swipe_down_action", "control_center") ?: "control_center",
            drawerColumns = prefs.getInt("drawer_cols", 4)
        )
    }

    fun saveDesktopPage(pageIndex: Int, appIds: List<String>) {
        prefs.edit().putString("desktop_page_$pageIndex", appIds.joinToString(",")).apply()
    }

    fun loadDesktopPage(pageIndex: Int, defaultList: List<String>): List<String> {
        val raw = prefs.getString("desktop_page_$pageIndex", null) ?: return defaultList
        return if (raw.isBlank()) emptyList() else raw.split(",")
    }

    fun saveDock(dockIds: List<String>) {
        prefs.edit().putString("dock_apps", dockIds.joinToString(",")).apply()
    }

    fun loadDock(defaultList: List<String>): List<String> {
        val raw = prefs.getString("dock_apps", null) ?: return defaultList
        return if (raw.isBlank()) emptyList() else raw.split(",")
    }

    fun saveQuickNote(text: String) {
        prefs.edit().putString("quick_note_text", text).apply()
    }

    fun loadQuickNote(defaultText: String): String {
        return prefs.getString("quick_note_text", defaultText) ?: defaultText
    }
}
