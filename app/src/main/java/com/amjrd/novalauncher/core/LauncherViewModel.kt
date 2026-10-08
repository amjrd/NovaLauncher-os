package com.amjrd.novalauncher.core

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.amjrd.novalauncher.data.WorkspaceStore
import com.amjrd.novalauncher.model.AppModel
import com.amjrd.novalauncher.model.OSDesignStyle

class LauncherViewModel : ViewModel() {

    private var store: WorkspaceStore? = null

    var osStyle by mutableStateOf(OSDesignStyle.FUSION)
        private set

    var accentColorHex by mutableStateOf(0xFFD71921L)
        private set

    val allApps = mutableStateListOf<AppModel>()
    val desktopApps = mutableStateListOf<String>()
    val dockApps = mutableStateListOf<String>()

    var isDynamicIslandExpanded by mutableStateOf(false)
    var isDeviceLocked by mutableStateOf(false)

    fun init(context: Context) {
        val s = WorkspaceStore(context)
        store = s
        osStyle = s.loadOSStyle()
        updateAccentForStyle(osStyle)

        desktopApps.clear()
        desktopApps.addAll(s.loadDesktopApps(0, listOf("camera", "photos", "settings", "calculator", "clock", "weather", "notes", "browser")))

        dockApps.clear()
        dockApps.addAll(s.loadDockApps(listOf("phone", "messages", "__DRAWER__", "browser", "camera")))
    }

    fun setStyle(style: OSDesignStyle) {
        osStyle = style
        updateAccentForStyle(style)
        store?.saveOSStyle(style)
    }

    private fun updateAccentForStyle(style: OSDesignStyle) {
        accentColorHex = when (style) {
            OSDesignStyle.NOTHING_OS -> 0xFFD71921L
            OSDesignStyle.IOS -> 0xFF007AFFL
            OSDesignStyle.PIXEL_17 -> 0xFF7CE4B5L
            OSDesignStyle.FUSION -> 0xFFD71921L
        }
    }
}
