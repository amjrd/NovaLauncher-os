package com.example.ui.simulatedapps

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CallEnd
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppItem
import kotlinx.coroutines.delay

@Composable
fun SimulatedAppContainer(
    app: AppItem,
    accentColor: Color,
    onBack: () -> Unit
) {
    BackHandler { onBack() }

    when (app.id) {
        "phone" -> SimulatedPhoneApp(accentColor, onBack)
        "messages" -> SimulatedMessagesApp(accentColor, onBack)
        "camera" -> SimulatedCameraApp(accentColor, onBack)
        "calculator" -> SimulatedCalculatorApp(accentColor, onBack)
        "weather" -> SimulatedWeatherApp(accentColor, onBack)
        "clock" -> SimulatedClockApp(accentColor, onBack)
        "notes" -> SimulatedNotesApp(accentColor, onBack)
        else -> GenericAppView(app, accentColor, onBack)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimulatedPhoneApp(accentColor: Color, onBack: () -> Unit) {
    var dialedNumber by remember { mutableStateOf("") }
    var inCall by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag("simulated_phone_app")
            .background(Color(0xFF10121D))
    ) {
        TopAppBar(
            title = { Text("Phone", color = Color.White, fontWeight = FontWeight.Bold) },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF161928))
        )

        if (inCall) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF00E676)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Call, contentDescription = null, tint = Color.Black, modifier = Modifier.size(48.dp))
                }
                Spacer(modifier = Modifier.height(20.dp))
                Text(text = dialedNumber.ifEmpty { "Voicemail" }, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Text(text = "Calling… 00:04", fontSize = 14.sp, color = Color(0xFF00E676))
                Spacer(modifier = Modifier.height(40.dp))
                IconButton(
                    onClick = { inCall = false },
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFF5252))
                ) {
                    Icon(Icons.Default.CallEnd, contentDescription = "End Call", tint = Color.White, modifier = Modifier.size(32.dp))
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Dialed Display
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = dialedNumber,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        letterSpacing = 2.sp
                    )
                }

                // Keypad
                val keys = listOf(
                    listOf("1", "2", "3"),
                    listOf("4", "5", "6"),
                    listOf("7", "8", "9"),
                    listOf("*", "0", "#")
                )

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    for (row in keys) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            for (key in row) {
                                Box(
                                    modifier = Modifier
                                        .size(64.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFF1E2238))
                                        .clickable { dialedNumber += key },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = key, fontSize = 24.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
                                }
                            }
                        }
                    }
                }

                // Call and Backspace Actions
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.size(56.dp))
                    IconButton(
                        onClick = { if (dialedNumber.isNotEmpty()) inCall = true },
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF00E676))
                    ) {
                        Icon(Icons.Default.Call, contentDescription = "Call", tint = Color.Black, modifier = Modifier.size(30.dp))
                    }
                    IconButton(
                        onClick = { if (dialedNumber.isNotEmpty()) dialedNumber = dialedNumber.dropLast(1) },
                        modifier = Modifier.size(56.dp)
                    ) {
                        Text("⌫", color = Color(0xFF9EA3C0), fontSize = 24.sp)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimulatedMessagesApp(accentColor: Color, onBack: () -> Unit) {
    val messages = remember {
        mutableStateListOf(
            "Alex" to "Hey, are we still meeting today at 4?",
            "Sarah" to "Check out Nova Launcher, it's super fast!",
            "David" to "Got the package. Thanks!",
            "Emily" to "Let's grab coffee this weekend."
        )
    }

    var selectedThread by remember { mutableStateOf<String?>(null) }
    var replyText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag("simulated_messages_app")
            .background(Color(0xFF10121D))
    ) {
        TopAppBar(
            title = { Text(selectedThread ?: "Messages", color = Color.White, fontWeight = FontWeight.Bold) },
            navigationIcon = {
                IconButton(onClick = {
                    if (selectedThread != null) selectedThread = null else onBack()
                }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF161928))
        )

        if (selectedThread == null) {
            LazyColumn(modifier = Modifier.fillMaxSize().padding(12.dp)) {
                items(messages) { (sender, text) ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable { selectedThread = sender },
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1B1E32))
                    ) {
                        Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(accentColor),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(sender.take(1), fontWeight = FontWeight.Bold, color = Color.Black)
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(sender, fontWeight = FontWeight.Bold, color = Color.White, fontSize = 14.sp)
                                Text(text, color = Color(0xFF8E93B0), fontSize = 12.sp, maxLines = 1)
                            }
                        }
                    }
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E2238)),
                        modifier = Modifier.padding(end = 48.dp)
                    ) {
                        Text(
                            text = messages.find { it.first == selectedThread }?.second ?: "Hello!",
                            modifier = Modifier.padding(12.dp),
                            color = Color.White,
                            fontSize = 14.sp
                        )
                    }
                }

                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(
                        value = replyText,
                        onValueChange = { replyText = it },
                        placeholder = { Text("Type a message…") },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFF1E2238),
                            unfocusedContainerColor = Color(0xFF181B2C),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(
                        onClick = { replyText = "" },
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(accentColor)
                    ) {
                        Icon(Icons.Default.Send, contentDescription = "Send", tint = Color.Black)
                    }
                }
            }
        }
    }
}

@Composable
fun SimulatedCameraApp(accentColor: Color, onBack: () -> Unit) {
    var shutterTriggered by remember { mutableStateOf(false) }

    LaunchedEffect(shutterTriggered) {
        if (shutterTriggered) {
            delay(200)
            shutterTriggered = false
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .testTag("simulated_camera_app")
            .background(Color.Black)
    ) {
        // Viewfinder
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 120.dp, top = 40.dp)
                .background(
                    Brush.radialGradient(
                        listOf(Color(0xFF283593), Color(0xFF121422), Color.Black)
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Viewfinder 4K 60fps",
                color = Color.White.copy(alpha = 0.5f),
                fontSize = 14.sp
            )
        }

        // Shutter flash effect
        if (shutterTriggered) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
            )
        }

        // Top Controls
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp, start = 20.dp, end = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
            IconButton(onClick = {}) {
                Icon(Icons.Default.FlashOn, contentDescription = "Flash", tint = Color.White)
            }
        }

        // Bottom Controls
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 40.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF22263C)),
                contentAlignment = Alignment.Center
            ) {
                Text("HDR", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }

            // Big Shutter Button
            Box(
                modifier = Modifier
                    .size(76.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .clickable { shutterTriggered = true }
                    .padding(4.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                )
            }

            IconButton(
                onClick = {},
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF22263C))
            ) {
                Icon(Icons.Default.Cameraswitch, contentDescription = "Flip", tint = Color.White)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimulatedCalculatorApp(accentColor: Color, onBack: () -> Unit) {
    var display by remember { mutableStateOf("0") }
    var operand1 by remember { mutableStateOf<Double?>(null) }
    var operator by remember { mutableStateOf<String?>(null) }
    var newNumber by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag("simulated_calculator_app")
            .background(Color(0xFF0F111D))
    ) {
        TopAppBar(
            title = { Text("Calculator", color = Color.White, fontWeight = FontWeight.Bold) },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF161928))
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Display
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                contentAlignment = Alignment.BottomEnd
            ) {
                Text(
                    text = display,
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontFamily = FontFamily.Monospace,
                    maxLines = 1
                )
            }

            // Keypad
            val buttons = listOf(
                listOf("C", "±", "%", "÷"),
                listOf("7", "8", "9", "×"),
                listOf("4", "5", "6", "-"),
                listOf("1", "2", "3", "+"),
                listOf("0", ".", "=")
            )

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                for (row in buttons) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        for (btn in row) {
                            val isOp = btn in listOf("÷", "×", "-", "+", "=")
                            val isZero = btn == "0"
                            val weight = if (isZero && row.size == 3) 2f else 1f

                            Box(
                                modifier = Modifier
                                    .weight(weight)
                                    .height(64.dp)
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(if (isOp) accentColor else Color(0xFF1F2338))
                                    .clickable {
                                        when (btn) {
                                            "C" -> {
                                                display = "0"
                                                operand1 = null
                                                operator = null
                                                newNumber = true
                                            }
                                            "+", "-", "×", "÷" -> {
                                                operand1 = display.toDoubleOrNull()
                                                operator = btn
                                                newNumber = true
                                            }
                                            "=" -> {
                                                val op2 = display.toDoubleOrNull()
                                                if (operand1 != null && op2 != null && operator != null) {
                                                    val res = when (operator) {
                                                        "+" -> operand1!! + op2
                                                        "-" -> operand1!! - op2
                                                        "×" -> operand1!! * op2
                                                        "÷" -> if (op2 != 0.0) operand1!! / op2 else 0.0
                                                        else -> op2
                                                    }
                                                    display = if (res % 1.0 == 0.0) res.toLong().toString() else res.toString()
                                                    operand1 = null
                                                    operator = null
                                                    newNumber = true
                                                }
                                            }
                                            else -> {
                                                if (newNumber || display == "0") {
                                                    display = btn
                                                    newNumber = false
                                                } else {
                                                    display += btn
                                                }
                                            }
                                        }
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = btn,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isOp) Color.Black else Color.White
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimulatedWeatherApp(accentColor: Color, onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag("simulated_weather_app")
            .background(Color(0xFF0D1426))
    ) {
        TopAppBar(
            title = { Text("Weather", color = Color.White, fontWeight = FontWeight.Bold) },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF131E38))
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Card(
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E2D52)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("San Francisco", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Spacer(modifier = Modifier.height(10.dp))
                        Icon(Icons.Default.WbSunny, contentDescription = null, tint = Color(0xFFFFD54F), modifier = Modifier.size(64.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("72°F", fontSize = 52.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Text("Clear Skies • High 76° / Low 55°", fontSize = 13.sp, color = Color(0xFF9FB2DC))
                    }
                }
            }

            item {
                Text("Hourly Forecast", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    val hours = listOf("Now" to "72°", "12 PM" to "74°", "1 PM" to "76°", "2 PM" to "75°", "3 PM" to "73°", "4 PM" to "70°")
                    items(hours) { (hr, temp) ->
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF192442)),
                            modifier = Modifier.width(72.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(hr, fontSize = 12.sp, color = Color(0xFF9FB2DC))
                                Spacer(modifier = Modifier.height(6.dp))
                                Icon(Icons.Default.WbSunny, contentDescription = null, tint = Color(0xFFFFD54F), modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(temp, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimulatedClockApp(accentColor: Color, onBack: () -> Unit) {
    var isRunning by remember { mutableStateOf(false) }
    var seconds by remember { mutableIntStateOf(0) }

    LaunchedEffect(isRunning) {
        while (isRunning) {
            delay(1000)
            seconds++
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag("simulated_clock_app")
            .background(Color(0xFF10121F))
    ) {
        TopAppBar(
            title = { Text("Stopwatch", color = Color.White, fontWeight = FontWeight.Bold) },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF161928))
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val mins = seconds / 60
            val secs = seconds % 60
            Text(
                text = String.format("%02d:%02d", mins, secs),
                fontSize = 64.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                fontFamily = FontFamily.Monospace
            )

            Spacer(modifier = Modifier.height(36.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                Button(
                    onClick = { isRunning = !isRunning },
                    colors = ButtonDefaults.buttonColors(containerColor = accentColor)
                ) {
                    Text(if (isRunning) "Pause" else "Start", color = Color.Black, fontWeight = FontWeight.Bold)
                }
                Button(
                    onClick = {
                        isRunning = false
                        seconds = 0
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2B2E48))
                ) {
                    Text("Reset", color = Color.White)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimulatedNotesApp(accentColor: Color, onBack: () -> Unit) {
    val notes = remember {
        mutableStateListOf(
            "Grocery list" to "Apples, almond milk, coffee beans, bread.",
            "Nova Launcher Ideas" to "Add custom gesture actions and support icon packs."
        )
    }

    var isAdding by remember { mutableStateOf(false) }
    var newTitle by remember { mutableStateOf("") }
    var newBody by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag("simulated_notes_app")
            .background(Color(0xFF121422))
    ) {
        TopAppBar(
            title = { Text("Notes", color = Color.White, fontWeight = FontWeight.Bold) },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF181B2E))
        )

        if (isAdding) {
            Column(modifier = Modifier.padding(16.dp)) {
                OutlinedTextField(
                    value = newTitle,
                    onValueChange = { newTitle = it },
                    label = { Text("Title") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White)
                )
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = newBody,
                    onValueChange = { newBody = it },
                    label = { Text("Note content") },
                    modifier = Modifier.fillMaxWidth().height(160.dp),
                    colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White)
                )
                Spacer(modifier = Modifier.height(14.dp))
                Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                    Button(onClick = { isAdding = false }) { Text("Cancel") }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (newTitle.isNotEmpty()) {
                                notes.add(newTitle to newBody)
                                newTitle = ""
                                newBody = ""
                                isAdding = false
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = accentColor)
                    ) {
                        Text("Save", color = Color.Black)
                    }
                }
            }
        } else {
            Box(modifier = Modifier.fillMaxSize()) {
                LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                    items(notes) { (title, body) ->
                        Card(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF1F233A))
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(title, fontWeight = FontWeight.Bold, color = Color.White, fontSize = 15.sp)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(body, color = Color(0xFF9BA0BE), fontSize = 12.sp)
                            }
                        }
                    }
                }

                FloatingActionButton(
                    onClick = { isAdding = true },
                    modifier = Modifier.align(Alignment.BottomEnd).padding(20.dp),
                    containerColor = accentColor
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Note", tint = Color.Black)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenericAppView(app: AppItem, accentColor: Color, onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag("simulated_generic_app_${app.id}")
            .background(Color(0xFF10121E))
    ) {
        TopAppBar(
            title = { Text(app.displayLabel, color = Color.White, fontWeight = FontWeight.Bold) },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF161928))
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(accentColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Info, contentDescription = null, tint = Color.Black, modifier = Modifier.size(40.dp))
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(app.displayLabel, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Text(app.packageName, fontSize = 12.sp, color = Color(0xFF888CA6))
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                "Running simulated native container within Nova Launcher OS.",
                color = Color(0xFFB4B9D8),
                fontSize = 13.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}
