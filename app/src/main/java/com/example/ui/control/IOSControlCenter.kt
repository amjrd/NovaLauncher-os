package com.example.ui.control

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.AirplanemodeActive
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FlashlightOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SignalCellularAlt
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.LauncherViewModel
import com.example.theme.IosBlue
import com.example.theme.IosGreen

@Composable
fun IOSControlCenter(
    viewModel: LauncherViewModel,
    onOpenCalculator: () -> Unit,
    onOpenCamera: () -> Unit,
    onDismiss: () -> Unit
) {
    BackHandler { onDismiss() }

    var isAirplaneMode by remember { mutableStateOf(false) }
    var isCellularOn by remember { mutableStateOf(true) }
    var isRotationLocked by remember { mutableStateOf(false) }
    var volumeLevel by remember { mutableFloatStateOf(0.7f) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .testTag("ios_control_center")
            .background(Color(0xFF000000).copy(alpha = 0.88f))
            .padding(top = 40.dp, start = 20.dp, end = 20.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header with Close
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Control Center",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF2C2C2E))
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = Color.White
                    )
                }
            }

            // Top Modules Row: Connectivity (2x2) + Now Playing (Square)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // 4-Toggle Connectivity Plate
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .height(150.dp),
                    shape = RoundedCornerShape(26.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1C1C1E).copy(alpha = 0.9f))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(14.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            IosRoundToggle(
                                icon = Icons.Default.AirplanemodeActive,
                                isActive = isAirplaneMode,
                                activeColor = Color(0xFFFF9500),
                                onClick = { isAirplaneMode = !isAirplaneMode }
                            )
                            IosRoundToggle(
                                icon = Icons.Default.SignalCellularAlt,
                                isActive = isCellularOn,
                                activeColor = IosGreen,
                                onClick = { isCellularOn = !isCellularOn }
                            )
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            IosRoundToggle(
                                icon = Icons.Default.Wifi,
                                isActive = viewModel.isWifiEnabled,
                                activeColor = IosBlue,
                                onClick = { viewModel.isWifiEnabled = !viewModel.isWifiEnabled }
                            )
                            IosRoundToggle(
                                icon = Icons.Default.Bluetooth,
                                isActive = viewModel.isBluetoothEnabled,
                                activeColor = IosBlue,
                                onClick = { viewModel.isBluetoothEnabled = !viewModel.isBluetoothEnabled }
                            )
                        }
                    }
                }

                // Now Playing Square
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .height(150.dp),
                    shape = RoundedCornerShape(26.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1C1C1E).copy(alpha = 0.9f))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(14.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFFFC3C44)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.MusicNote, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(viewModel.currentSongTitle, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                Text(viewModel.currentSongArtist, color = Color(0xFF8E8E93), fontSize = 10.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(onClick = { viewModel.isMusicPlaying = !viewModel.isMusicPlaying }) {
                                Icon(
                                    imageVector = if (viewModel.isMusicPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                            IconButton(onClick = {
                                viewModel.currentSongTitle = "Blinding Lights"
                                viewModel.currentSongArtist = "The Weeknd"
                            }) {
                                Icon(Icons.Default.SkipNext, contentDescription = null, tint = Color.White, modifier = Modifier.size(28.dp))
                            }
                        }
                    }
                }
            }

            // Middle Modules: Rotation Lock, Screen Mirroring, Sliders
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Rotation Lock & Flashlight Square
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(68.dp)
                            .clickable { isRotationLocked = !isRotationLocked },
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isRotationLocked) Color(0xFFFF453A).copy(alpha = 0.3f) else Color(0xFF1C1C1E)
                        )
                    ) {
                        Row(
                            modifier = Modifier.fillMaxSize().padding(horizontal = 14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Lock, contentDescription = null, tint = if (isRotationLocked) Color(0xFFFF453A) else Color.White)
                            Spacer(modifier = Modifier.width(10.dp))
                            Text("Orientation", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(68.dp)
                            .clickable { viewModel.isFlashlightOn = !viewModel.isFlashlightOn },
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (viewModel.isFlashlightOn) Color(0xFFFFD60A).copy(alpha = 0.3f) else Color(0xFF1C1C1E)
                        )
                    ) {
                        Row(
                            modifier = Modifier.fillMaxSize().padding(horizontal = 14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.FlashlightOn, contentDescription = null, tint = if (viewModel.isFlashlightOn) Color(0xFFFFD60A) else Color.White)
                            Spacer(modifier = Modifier.width(10.dp))
                            Text("Flashlight", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }

                // Brightness & Volume Sliders
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .height(150.dp),
                    shape = RoundedCornerShape(26.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1C1C1E))
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize().padding(14.dp),
                        verticalArrangement = Arrangement.SpaceAround
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.WbSunny, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Slider(
                                value = viewModel.brightnessLevel,
                                onValueChange = { viewModel.brightnessLevel = it },
                                colors = SliderDefaults.colors(thumbColor = Color.White, activeTrackColor = Color.White, inactiveTrackColor = Color(0xFF3A3A3C)),
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.VolumeUp, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Slider(
                                value = volumeLevel,
                                onValueChange = { volumeLevel = it },
                                colors = SliderDefaults.colors(thumbColor = Color.White, activeTrackColor = Color.White, inactiveTrackColor = Color(0xFF3A3A3C)),
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }

            // Bottom Quick Utility Shortcuts
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                IosBottomActionCircle(icon = Icons.Default.Calculate, label = "Calc", onClick = onOpenCalculator)
                IosBottomActionCircle(icon = Icons.Default.PhotoCamera, label = "Camera", onClick = onOpenCamera)
            }
        }
    }
}

@Composable
private fun IosRoundToggle(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isActive: Boolean,
    activeColor: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(52.dp)
            .clip(CircleShape)
            .background(if (isActive) activeColor else Color(0xFF2C2C2E))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (isActive) Color.White else Color(0xFF8E8E93),
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
private fun IosBottomActionCircle(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(60.dp)
            .clip(CircleShape)
            .background(Color(0xFF1C1C1E))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = Color.White,
            modifier = Modifier.size(26.dp)
        )
    }
}
