package com.amjrd.novalauncher

import android.os.Build
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
fun NovaTheme(content: @Composable () -> Unit) {
    val context = LocalContext.current
    val scheme = if (Build.VERSION.SDK_INT >= 31) {
        dynamicLightColorScheme(context)
    } else {
        lightColorScheme()
    }
    MaterialTheme(colorScheme = scheme, typography = Typography(), content = content)
}
