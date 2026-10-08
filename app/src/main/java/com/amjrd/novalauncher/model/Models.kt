package com.amjrd.novalauncher.model

import android.content.ComponentName
import android.graphics.drawable.Drawable

data class NovaApp(val component: ComponentName, val label: String, val icon: Drawable, val userSerial: Long, val isPrivateProfile: Boolean = false) {
    val key: String get() = component.flattenToString() + "#" + userSerial
}
data class Placement(val key: String, val page: Int, val x: Int, val y: Int, val spanX: Int = 1, val spanY: Int = 1, val folderId: String? = null)
data class NovaFolder(val id: String, val title: String, val members: List<String>, val page: Int = 0, val x: Int = 0, val y: Int = 0)
data class WidgetPlacement(val appWidgetId: Int, val provider: String, val page: Int, val x: Int, val y: Int, val spanX: Int = 2, val spanY: Int = 2)
data class NovaState(
    val apps: List<NovaApp> = emptyList(),
    val placements: List<Placement> = emptyList(),
    val folders: List<NovaFolder> = emptyList(),
    val widgets: List<WidgetPlacement> = emptyList(),
    val dock: List<String> = emptyList(),
    val query: String = "",
    val drawer: Boolean = false,
    val editing: Boolean = false,
    val page: Int = 0
)