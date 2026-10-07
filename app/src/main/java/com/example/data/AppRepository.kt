package com.example.data

import android.content.Context
import android.content.Intent
import com.example.model.AppCategory
import com.example.model.AppItem
import com.example.model.WallpaperItem

object AppRepository {

    val defaultWallpapers = listOf(
        WallpaperItem(
            id = "nothing_dark",
            name = "Nothing Glyph Carbon",
            previewGradientColors = listOf(0xFF0A0A0A, 0xFF141414, 0xFF1F1F1F, 0xFF000000),
            subtitle = "Iconic Nothing OS carbon black with glyph accents"
        ),
        WallpaperItem(
            id = "ios_aurora",
            name = "iOS 18 Glass Wave",
            previewGradientColors = listOf(0xFF0F172A, 0xFF1E293B, 0xFF0284C7, 0xFF4338CA, 0xFFEC4899),
            subtitle = "Fluid iOS deep indigo and frosted magenta waves"
        ),
        WallpaperItem(
            id = "pixel_monet",
            name = "Pixel 17 Dunes",
            previewGradientColors = listOf(0xFF1B2838, 0xFF2D3748, 0xFF4A5568, 0xFF718096, 0xFFCBD5E0),
            subtitle = "Organic Material You desert horizons"
        ),
        WallpaperItem(
            id = "cosmic_nebula",
            name = "Cosmic Nebula",
            previewGradientColors = listOf(0xFF0D0F18, 0xFF2A164D, 0xFF4A154B, 0xFF0B1936),
            subtitle = "Deep space violet with astral glow"
        ),
        WallpaperItem(
            id = "amoled_void",
            name = "Pure AMOLED Void",
            previewGradientColors = listOf(0xFF000000, 0xFF080808, 0xFF101010),
            subtitle = "Battery saving pitch black minimalist"
        ),
        WallpaperItem(
            id = "cyber_grid",
            name = "Cyberpunk Neo",
            previewGradientColors = listOf(0xFF050510, 0xFF0D1B2A, 0xFF00E5FF, 0xFF7C4DFF),
            subtitle = "Electric cyan & neon violet"
        )
    )

    fun getInitialApps(): List<AppItem> = listOf(
        AppItem(
            id = "phone",
            name = "Phone",
            packageName = "com.android.dialer",
            iconResName = "phone",
            category = AppCategory.COMMUNICATION,
            isFavorite = true,
            accentColorHex = 0xFF34C759
        ),
        AppItem(
            id = "messages",
            name = "Messages",
            packageName = "com.android.mms",
            iconResName = "chat",
            category = AppCategory.COMMUNICATION,
            isFavorite = true,
            accentColorHex = 0xFF007AFF
        ),
        AppItem(
            id = "camera",
            name = "Camera",
            packageName = "com.android.camera",
            iconResName = "photo_camera",
            category = AppCategory.MEDIA,
            isFavorite = true,
            accentColorHex = 0xFFD71921
        ),
        AppItem(
            id = "photos",
            name = "Photos",
            packageName = "com.google.android.apps.photos",
            iconResName = "photo_library",
            category = AppCategory.MEDIA,
            accentColorHex = 0xFFFF2D55
        ),
        AppItem(
            id = "browser",
            name = "Safari / Chrome",
            packageName = "com.android.chrome",
            iconResName = "public",
            category = AppCategory.PRODUCTIVITY,
            isFavorite = true,
            accentColorHex = 0xFF007AFF
        ),
        AppItem(
            id = "nova_settings",
            name = "OS Settings",
            packageName = "com.example.novasettings",
            iconResName = "tune",
            category = AppCategory.TOOLS,
            isFavorite = true,
            accentColorHex = 0xFFD71921
        ),
        AppItem(
            id = "settings",
            name = "Settings",
            packageName = "com.android.settings",
            iconResName = "settings",
            category = AppCategory.TOOLS,
            accentColorHex = 0xFF8E8E93
        ),
        AppItem(
            id = "calculator",
            name = "Calculator",
            packageName = "com.android.calculator2",
            iconResName = "calculate",
            category = AppCategory.TOOLS,
            accentColorHex = 0xFFFF9500
        ),
        AppItem(
            id = "clock",
            name = "Clock",
            packageName = "com.android.deskclock",
            iconResName = "schedule",
            category = AppCategory.TOOLS,
            accentColorHex = 0xFFFF3B30
        ),
        AppItem(
            id = "calendar",
            name = "Calendar",
            packageName = "com.android.calendar",
            iconResName = "calendar_today",
            category = AppCategory.PRODUCTIVITY,
            accentColorHex = 0xFFFF2D55
        ),
        AppItem(
            id = "weather",
            name = "Weather",
            packageName = "com.example.weather",
            iconResName = "wb_sunny",
            category = AppCategory.LIFESTYLE,
            accentColorHex = 0xFF007AFF
        ),
        AppItem(
            id = "music",
            name = "Music",
            packageName = "com.example.music",
            iconResName = "music_note",
            category = AppCategory.MEDIA,
            accentColorHex = 0xFFFC3C44
        ),
        AppItem(
            id = "notes",
            name = "Notes",
            packageName = "com.example.notes",
            iconResName = "edit_note",
            category = AppCategory.PRODUCTIVITY,
            accentColorHex = 0xFFFFCC00
        ),
        AppItem(
            id = "files",
            name = "Files",
            packageName = "com.android.documentsui",
            iconResName = "folder",
            category = AppCategory.TOOLS,
            accentColorHex = 0xFF5856D6
        ),
        AppItem(
            id = "contacts",
            name = "Contacts",
            packageName = "com.android.contacts",
            iconResName = "contacts",
            category = AppCategory.COMMUNICATION,
            accentColorHex = 0xFF30D158
        ),
        AppItem(
            id = "maps",
            name = "Maps",
            packageName = "com.google.android.apps.maps",
            iconResName = "explore",
            category = AppCategory.LIFESTYLE,
            accentColorHex = 0xFF34C759
        ),
        AppItem(
            id = "email",
            name = "Mail",
            packageName = "com.google.android.gm",
            iconResName = "email",
            category = AppCategory.COMMUNICATION,
            accentColorHex = 0xFF007AFF
        ),
        AppItem(
            id = "playstore",
            name = "App Store",
            packageName = "com.android.vending",
            iconResName = "shopping_bag",
            category = AppCategory.TOOLS,
            accentColorHex = 0xFF007AFF
        ),
        AppItem(
            id = "terminal",
            name = "Terminal",
            packageName = "com.example.terminal",
            iconResName = "terminal",
            category = AppCategory.TOOLS,
            accentColorHex = 0xFFD71921
        ),
        AppItem(
            id = "fitness",
            name = "Fitness",
            packageName = "com.example.fit",
            iconResName = "directions_run",
            category = AppCategory.LIFESTYLE,
            accentColorHex = 0xFF34C759
        )
    )

    fun loadDeviceInstalledApps(context: Context): List<AppItem> {
        val list = mutableListOf<AppItem>()
        try {
            val pm = context.packageManager
            val mainIntent = Intent(Intent.ACTION_MAIN, null).apply {
                addCategory(Intent.CATEGORY_LAUNCHER)
            }
            val resolved = pm.queryIntentActivities(mainIntent, 0)
            for (resolveInfo in resolved) {
                val pkg = resolveInfo.activityInfo.packageName
                if (pkg == context.packageName) continue
                val label = resolveInfo.loadLabel(pm).toString()
                list.add(
                    AppItem(
                        id = "pkg_$pkg",
                        name = label,
                        packageName = pkg,
                        iconResName = "android_installed",
                        category = AppCategory.TOOLS,
                        accentColorHex = 0xFF007AFF
                    )
                )
            }
        } catch (_: Exception) {}
        return list
    }
}
