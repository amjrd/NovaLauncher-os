package com.example.ui.lockscreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.FlashlightOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AppRepository
import com.example.data.LauncherViewModel
import com.example.model.OSDesignStyle
import com.example.theme.NothingRed
import com.example.ui.dynamicisland.DynamicIsland
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun LockScreenView(
    viewModel: LauncherViewModel,
    onUnlock: () -> Unit
) {
    val settings = viewModel.settings
    val accentColor = Color(settings.accentColorHex)

    var currentTime by remember { mutableStateOf("") }
    var currentDate by remember { mutableStateOf("") }
    var hourOnly by remember { mutableStateOf("10") }
    var minOnly by remember { mutableStateOf("24") }

    LaunchedEffect(Unit) {
        val tf = SimpleDateFormat("h:mm", Locale.getDefault())
        val df = SimpleDateFormat("EEEE, MMMM d", Locale.getDefault())
        val hf = SimpleDateFormat("HH", Locale.getDefault())
        val mf = SimpleDateFormat("mm", Locale.getDefault())
        while (true) {
            val now = Date()
            currentTime = tf.format(now)
            currentDate = df.format(now)
            hourOnly = hf.format(now)
            minOnly = mf.format(now)
            delay(1000)
        }
    }

    val currentWallpaper = remember(settings.wallpaperId) {
        AppRepository.defaultWallpapers.find { it.id == settings.wallpaperId }
            ?: AppRepository.defaultWallpapers.first()
    }
    val wallpaperBrush = remember(currentWallpaper) {
        Brush.verticalGradient(currentWallpaper.previewGradientColors.map { Color(it) })
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .testTag("lock_screen_view")
            .background(wallpaperBrush)
            .pointerInput(Unit) {
                detectVerticalDragGestures { _, dragAmount ->
                    if (dragAmount < -40) {
                        onUnlock()
                    }
                }
            }
            .clickable { onUnlock() }
    ) {
        // Dark Dimmer Overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.35f))
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 28.dp, bottom = 32.dp, start = 24.dp, end = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top Section (Dynamic Island if iOS/Fusion, Lock Icon)
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                if (settings.osStyle == OSDesignStyle.IOS || settings.osStyle == OSDesignStyle.FUSION) {
                    DynamicIsland(viewModel = viewModel)
                    Spacer(modifier = Modifier.height(16.dp))
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Locked",
                        tint = Color.White.copy(alpha = 0.8f),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = settings.osStyle.title,
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Middle Clock based on active OS Style
                when (settings.osStyle) {
                    OSDesignStyle.NOTHING_OS -> {
                        // Nothing OS Dot-Matrix Clock
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = currentTime.ifEmpty { "10:24" },
                                fontSize = 72.sp,
                                fontWeight = FontWeight.Black,
                                fontFamily = FontFamily.Monospace,
                                color = Color.White,
                                letterSpacing = 3.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = currentDate.uppercase(),
                                fontSize = 13.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold,
                                color = NothingRed,
                                letterSpacing = 2.sp
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            // Glyph Battery Ring
                            Box(
                                modifier = Modifier
                                    .size(54.dp)
                                    .clip(CircleShape)
                                    .border(2.dp, Color.White, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${viewModel.batteryPercentage}%",
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                    }

                    OSDesignStyle.PIXEL_17 -> {
                        // Pixel 17 Massive Dual-Line Material You Clock
                        Column(horizontalAlignment = Alignment.Start, modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = hourOnly,
                                fontSize = 92.sp,
                                fontWeight = FontWeight.Black,
                                color = accentColor,
                                lineHeight = 80.sp
                            )
                            Text(
                                text = minOnly,
                                fontSize = 92.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White,
                                lineHeight = 80.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = currentDate,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFFCFD8DC)
                            )
                        }
                    }

                    OSDesignStyle.IOS, OSDesignStyle.FUSION -> {
                        // iOS 18 Bold Depth Clock
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = currentDate,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White.copy(alpha = 0.9f)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = currentTime.ifEmpty { "10:24" },
                                fontSize = 84.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                letterSpacing = (-2).sp
                            )
                        }
                    }
                }
            }

            // Bottom Section (Flashlight, Unlock Action, Camera)
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Bottom Shortcut buttons (Flashlight & Camera)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                            .background(Color.Black.copy(alpha = 0.5f))
                            .clickable { viewModel.isFlashlightOn = !viewModel.isFlashlightOn },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.FlashlightOn,
                            contentDescription = "Torch",
                            tint = if (viewModel.isFlashlightOn) Color(0xFFFFD600) else Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    // Unlock Indicator Bar
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickable { onUnlock() }
                    ) {
                        if (settings.osStyle == OSDesignStyle.PIXEL_17) {
                            Icon(
                                imageVector = Icons.Default.Fingerprint,
                                contentDescription = "Fingerprint",
                                tint = accentColor,
                                modifier = Modifier.size(46.dp)
                            )
                        } else {
                            Text(
                                text = "Swipe up or tap to unlock",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.White.copy(alpha = 0.8f)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Box(
                                modifier = Modifier
                                    .width(135.dp)
                                    .height(4.5.dp)
                                    .clip(CircleShape)
                                    .background(Color.White)
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                            .background(Color.Black.copy(alpha = 0.5f))
                            .clickable {
                                onUnlock()
                                val cam = viewModel.allApps.find { it.id == "camera" }
                                if (cam != null) viewModel.activeOverlay = com.example.data.ActiveOverlay.SimulatedAppView(cam)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = "Camera",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
    }
}
