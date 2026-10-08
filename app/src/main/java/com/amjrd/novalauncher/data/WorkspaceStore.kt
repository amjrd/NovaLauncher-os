package com.amjrd.novalauncher.data

import android.content.Context
import android.content.SharedPreferences
import com.amjrd.novalauncher.model.OSDesignStyle

class WorkspaceStore(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("amjrd_nova_prefs", Context.MODE_PRIVATE)

    fun saveOSStyle(style: OSDesignStyle) {
        prefs.edit().putString("os_style", style.name).apply()
    }

    fun loadOSStyle(): OSDesignStyle {
        val raw = prefs.getString("os_style", OSDesignStyle.FUSION.name) ?: OSDesignStyle.FUSION.name
        return try {
            OSDesignStyle.valueOf(raw)
        } catch (_: Exception) {
            OSDesignStyle.FUSION
        }
    }

    fun saveDesktopApps(page: Int, appIds: List<String>) {
        prefs.edit().putString("desktop_page_$page", appIds.joinToString(",")).apply()
    }

    fun loadDesktopApps(page: Int, defaultList: List<String>): List<String> {
        val raw = prefs.getString("desktop_page_$page", null) ?: return defaultList
        return if (raw.isBlank()) emptyList() else raw.split(",")
    }

    fun saveDockApps(appIds: List<String>) {
        prefs.edit().putString("dock_apps", appIds.joinToString(",")).apply()
    }

    fun loadDockApps(defaultList: List<String>): List<String> {
        val raw = prefs.getString("dock_apps", null) ?: return defaultList
        return if (raw.isBlank()) emptyList() else raw.split(",")
    }
}
