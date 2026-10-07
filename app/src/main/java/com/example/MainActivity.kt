package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.LauncherViewModel
import com.example.theme.NovaLauncherTheme
import com.example.ui.home.HomeScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val launcherViewModel: LauncherViewModel = viewModel()

            // Initialize any real installed packages on device
            launcherViewModel.initDeviceApps(this)

            NovaLauncherTheme(
                themeMode = launcherViewModel.settings.themeMode,
                accentColorHex = launcherViewModel.settings.accentColorHex
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    HomeScreen(viewModel = launcherViewModel)
                }
            }
        }
    }
}
