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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AirplanemodeActive
import androidx.compose.material.icons.filled.BatteryChargingFull
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.FlashlightOn
import androidx.compose.material.icons.filled.NotificationsOff
import androidx.compose.material.icons.filled.ScreenRotation
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
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.LauncherViewModel
import com.example.model.ThemeMode
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class MockNotification(
    val id: String,
    val appTitle: String,
    val message: String,
    val time: String
)

@Composable
fun QuickControlCenterSheet(
    viewModel: LauncherViewModel,
    onDismiss: () -> Unit
) {
    BackHandler { onDismiss() }

    val accentColor = Color(viewModel.settings.accentColorHex)
    var isAirplaneMode by remember { mutableStateOf(false) }
    var isAutoRotate by remember { mutableStateOf(true) }
    var volumeLevel by remember { mutableFloatStateOf(0.65f) }

    val notifications = remember {
        mutableStateListOf(
            MockNotification("1", "Messages", "Alex: Hey! Check out this new wallpaper.", "2m ago"),
            MockNotification("2", "Weather", "Sunny and 72°F today with light breeze.", "15m ago"),
            MockNotification("3", "Calendar", "Upcoming: Team Sync at 3:00 PM", "1h ago")
        )
    }

    val timeFormat = remember { SimpleDateFormat("h:mm a", Locale.getDefault()) }
    val currentTime = remember { timeFormat.format(Date()) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .testTag("quick_control_center_sheet")
            .background(Color(0xFF0A0C16).copy(alpha = 0.95f))
            .padding(top = 36.dp, start = 16.dp, end = 16.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = currentTime,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.BatteryChargingFull,
                            contentDescription = null,
                            tint = Color(0xFF00E676),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${viewModel.batteryPercentage}% • Charging",
                            fontSize = 12.sp,
                            color = Color(0xFF8B8FAD)
                        )
                    }
                }

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF1B1E32))
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Toggles Grid
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ControlTile(
                    title = "Wi-Fi",
                    subtitle = if (viewModel.isWifiEnabled) "Home 5G" else "Off",
                    icon = Icons.Default.Wifi,
                    isActive = viewModel.isWifiEnabled,
                    accentColor = accentColor,
                    onClick = { viewModel.isWifiEnabled = !viewModel.isWifiEnabled },
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(10.dp))
                ControlTile(
                    title = "Bluetooth",
                    subtitle = if (viewModel.isBluetoothEnabled) "Pixel Buds" else "Off",
                    icon = Icons.Default.Bluetooth,
                    isActive = viewModel.isBluetoothEnabled,
                    accentColor = Color(0xFF2979FF),
                    onClick = { viewModel.isBluetoothEnabled = !viewModel.isBluetoothEnabled },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ControlTile(
                    title = "Torch",
                    subtitle = if (viewModel.isFlashlightOn) "Active" else "Off",
                    icon = Icons.Default.FlashlightOn,
                    isActive = viewModel.isFlashlightOn,
                    accentColor = Color(0xFFFFD600),
                    onClick = { viewModel.isFlashlightOn = !viewModel.isFlashlightOn },
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(10.dp))
                ControlTile(
                    title = "Airplane",
                    subtitle = if (isAirplaneMode) "On" else "Off",
                    icon = Icons.Default.AirplanemodeActive,
                    isActive = isAirplaneMode,
                    accentColor = Color(0xFFFF9100),
                    onClick = { isAirplaneMode = !isAirplaneMode },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ControlTile(
                    title = "DND",
                    subtitle = if (viewModel.isDndEnabled) "Silent" else "Off",
                    icon = Icons.Default.NotificationsOff,
                    isActive = viewModel.isDndEnabled,
                    accentColor = Color(0xFFFF5252),
                    onClick = { viewModel.isDndEnabled = !viewModel.isDndEnabled },
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(10.dp))
                ControlTile(
                    title = "Auto-Rotate",
                    subtitle = if (isAutoRotate) "Portrait" else "Locked",
                    icon = Icons.Default.ScreenRotation,
                    isActive = isAutoRotate,
                    accentColor = Color(0xFF00E5FF),
                    onClick = { isAutoRotate = !isAutoRotate },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Sliders (Brightness & Volume)
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF16192C)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.WbSunny,
                            contentDescription = null,
                            tint = Color(0xFFFFD54F),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Slider(
                            value = viewModel.brightnessLevel,
                            onValueChange = { viewModel.brightnessLevel = it },
                            modifier = Modifier.fillMaxWidth(),
                            colors = SliderDefaults.colors(
                                thumbColor = accentColor,
                                activeTrackColor = accentColor,
                                inactiveTrackColor = Color(0xFF2E334D)
                            )
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.VolumeUp,
                            contentDescription = null,
                            tint = Color(0xFF00E5FF),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Slider(
                            value = volumeLevel,
                            onValueChange = { volumeLevel = it },
                            modifier = Modifier.fillMaxWidth(),
                            colors = SliderDefaults.colors(
                                thumbColor = Color(0xFF00E5FF),
                                activeTrackColor = Color(0xFF00E5FF),
                                inactiveTrackColor = Color(0xFF2E334D)
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Notifications
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Notifications (${notifications.size})",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF888CA6)
                )
                if (notifications.isNotEmpty()) {
                    TextButton(onClick = { notifications.clear() }) {
                        Text("Clear All", color = accentColor, fontSize = 12.sp)
                    }
                }
            }

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(notifications.size) { index ->
                    val notif = notifications[index]
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF181B2E)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = notif.appTitle,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = notif.message,
                                    fontSize = 12.sp,
                                    color = Color(0xFFB0B4CE)
                                )
                            }
                            Text(
                                text = notif.time,
                                fontSize = 10.sp,
                                color = Color(0xFF6B708D)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ControlTile(
    title: String,
    subtitle: String,
    icon: ImageVector,
    isActive: Boolean,
    accentColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isActive) accentColor.copy(alpha = 0.22f) else Color(0xFF16192C)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(if (isActive) accentColor else Color(0xFF262B42)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = if (isActive) Color.Black else Color.White,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = title,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = subtitle,
                    fontSize = 11.sp,
                    color = if (isActive) accentColor else Color(0xFF7E839E)
                )
            }
        }
    }
}
