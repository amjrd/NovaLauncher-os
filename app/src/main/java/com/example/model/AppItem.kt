package com.example.model

enum class OSDesignStyle(val title: String, val subtitle: String) {
    FUSION("Fusion OS", "Best of all: Dynamic Island + Nothing Widgets + iOS Glass Dock + Pixel Monet"),
    NOTHING_OS("Nothing OS", "Dot-matrix NDot aesthetic, monochrome glyphs, red accents & glyph rings"),
    IOS("iOS Experience", "Dynamic Island, frosted glass dock, iOS squircle icons & iOS Control Center"),
    PIXEL_17("Pixel Android 17", "Material You Expressive, Monet dynamic colors & At a Glance live pill")
}

enum class IconShape {
    CIRCLE,
    SQUIRCLE,
    ROUNDED_SQUARE,
    TEARDROP,
    HEXAGON
}

enum class AppCategory(val title: String) {
    ALL("All"),
    FAVORITES("Favorites"),
    COMMUNICATION("Social & Talk"),
    PRODUCTIVITY("Productivity"),
    TOOLS("Tools & System"),
    MEDIA("Media"),
    LIFESTYLE("Lifestyle")
}

data class AppItem(
    val id: String,
    val name: String,
    val packageName: String,
    val iconResName: String,
    val category: AppCategory = AppCategory.TOOLS,
    val isHidden: Boolean = false,
    val isFavorite: Boolean = false,
    val customLabel: String? = null,
    val accentColorHex: Long = 0xFF6200EE,
    val launchCount: Int = 0
) {
    val displayLabel: String
        get() = customLabel ?: name
}

enum class WidgetType(val title: String, val description: String) {
    CLOCK_WEATHER("Clock & Weather", "Live digital time, date and local weather conditions"),
    DYNAMIC_ISLAND("Dynamic Island", "Interactive expandable top pill with music and battery"),
    NOTHING_CLOCK("Nothing NDot Clock", "Iconic Nothing OS dot-matrix digital clock & weather"),
    PIXEL_AT_A_GLANCE("Pixel At a Glance", "Smart Material You event, weather and battery pill"),
    SEARCH_BAR("Smart Search Bar", "Universal search for apps, web and instant math solver"),
    SYSTEM_TOGGLES("Control Toggles", "Wi-Fi, Bluetooth, Flashlight, Battery and Brightness"),
    MUSIC_PLAYER("Now Playing", "Interactive music playback controls and track info"),
    QUICK_NOTE("Quick Scratchpad", "Sticky note right on your home screen")
}

data class WidgetItem(
    val id: String,
    val type: WidgetType,
    val pageIndex: Int,
    val order: Int = 0
)

data class FolderItem(
    val id: String,
    val name: String,
    val appIds: List<String>,
    val pageIndex: Int,
    val positionIndex: Int,
    val accentColorHex: Long = 0xFFFF7043
)

enum class ThemeMode {
    SYSTEM,
    DARK,
    AMOLED,
    LIGHT
}

enum class DockBackgroundStyle {
    TRANSPARENT,
    GLASS_BLUR,
    CARD_SHADOW,
    TINTED_ACCENT,
    IOS_FROSTED_GLASS
}

enum class DesktopTransition {
    SIMPLE,
    CUBE,
    ACCORDION,
    CARD_STACK
}

data class LauncherSettings(
    val osStyle: OSDesignStyle = OSDesignStyle.FUSION,
    val gridRows: Int = 5,
    val gridCols: Int = 4,
    val iconScale: Float = 1.0f,
    val showLabels: Boolean = true,
    val iconShape: IconShape = IconShape.SQUIRCLE,
    val dockCount: Int = 5,
    val dockBackgroundStyle: DockBackgroundStyle = DockBackgroundStyle.IOS_FROSTED_GLASS,
    val themeMode: ThemeMode = ThemeMode.DARK,
    val accentColorHex: Long = 0xFFD71921, // Nothing Red default for punchy contrast
    val wallpaperId: String = "nothing_dark",
    val desktopTransition: DesktopTransition = DesktopTransition.SIMPLE,
    val doubleTapAction: String = "search",
    val swipeDownAction: String = "control_center",
    val drawerColumns: Int = 4,
    val isDynamicIslandExpanded: Boolean = false
)

data class WallpaperItem(
    val id: String,
    val name: String,
    val previewGradientColors: List<Long>,
    val subtitle: String
)
