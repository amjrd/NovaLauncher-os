package com.amjrd.novalauncher.model

enum class OSDesignStyle(val title: String, val subtitle: String) {
    FUSION("Fusion OS", "Best of all: Dynamic Island + Nothing Widgets + iOS Glass Dock + Pixel Monet"),
    NOTHING_OS("Nothing OS", "Dot-matrix NDot aesthetic, monochrome glyphs, red accents & glyph rings"),
    IOS("iOS Experience", "Dynamic Island, frosted glass dock, iOS squircle icons & iOS Control Center"),
    PIXEL_17("Pixel Android 17", "Material You Expressive, Monet dynamic colors & At a Glance live pill")
}

data class AppModel(
    val id: String,
    val name: String,
    val packageName: String,
    val iconResName: String,
    val category: String = "Tools",
    val isHidden: Boolean = false,
    val isFavorite: Boolean = false,
    val customLabel: String? = null,
    val accentColorHex: Long = 0xFFD71921L
) {
    val displayLabel: String
        get() = customLabel ?: name
}

data class DesktopGridItem(
    val id: String,
    val appId: String,
    val page: Int = 0,
    val row: Int = 0,
    val col: Int = 0
)

data class DockItem(
    val id: String,
    val appId: String,
    val slotIndex: Int
)

data class FolderModel(
    val id: String,
    val name: String,
    val appIds: List<String>,
    val page: Int = 0
)
