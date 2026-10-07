package com.example.ui.search

import android.content.Intent
import android.net.Uri
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.LauncherViewModel
import com.example.model.AppItem
import com.example.ui.home.AppIconItem

fun tryEvaluateMath(expr: String): Double? {
    val clean = expr.trim().replace(" ", "").replace("x", "*").replace("X", "*")
    if (clean.isEmpty()) return null
    if (!clean.matches(Regex("^[0-9+\\-*/.()]+$"))) return null
    return try {
        // Simple 2-operand or basic arithmetic evaluation
        val parts = clean.split(Regex("(?<=[+\\-*/])|(?=[+\\-*/])"))
        if (parts.size >= 3) {
            var result = parts[0].toDouble()
            var i = 1
            while (i < parts.size - 1) {
                val op = parts[i]
                val nextVal = parts[i + 1].toDouble()
                result = when (op) {
                    "+" -> result + nextVal
                    "-" -> result - nextVal
                    "*" -> result * nextVal
                    "/" -> if (nextVal != 0.0) result / nextVal else return null
                    else -> result
                }
                i += 2
            }
            result
        } else {
            null
        }
    } catch (_: Exception) {
        null
    }
}

@Composable
fun UniversalSearchOverlay(
    viewModel: LauncherViewModel,
    onAppClick: (AppItem) -> Unit,
    onDismiss: () -> Unit
) {
    BackHandler { onDismiss() }

    val context = LocalContext.current
    var query by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }
    val accentColor = Color(viewModel.settings.accentColorHex)

    val recentSearches = remember {
        mutableStateListOf("Nova Settings", "Weather", "Chrome", "Camera")
    }

    LaunchedEffect(Unit) {
        try {
            focusRequester.requestFocus()
        } catch (_: Exception) {}
    }

    val matchedApps = remember(query, viewModel.allApps) {
        if (query.isBlank()) emptyList()
        else viewModel.allApps.filter {
            it.displayLabel.contains(query, ignoreCase = true) ||
                    it.name.contains(query, ignoreCase = true) ||
                    it.category.title.contains(query, ignoreCase = true)
        }
    }

    val mathResult = remember(query) { tryEvaluateMath(query) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .testTag("universal_search_overlay")
            .background(Color(0xFF0C0E1A).copy(alpha = 0.98f))
            .padding(top = 36.dp, start = 16.dp, end = 16.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Search Input Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    placeholder = { Text("Search apps, web, math (e.g. 24 * 5)…", color = Color(0xFF747892)) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = accentColor
                        )
                    },
                    trailingIcon = {
                        if (query.isNotEmpty()) {
                            IconButton(onClick = { query = "" }) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Clear",
                                    tint = Color.White
                                )
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(26.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFF1B1E32),
                        unfocusedContainerColor = Color(0xFF17192A),
                        focusedBorderColor = accentColor,
                        unfocusedBorderColor = Color(0xFF282C46),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .focusRequester(focusRequester)
                )

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .size(44.dp)
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

            // Results List
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // If math result is available
                if (mathResult != null) {
                    item {
                        Card(
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF242017)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Calculate,
                                        contentDescription = "Math Solver",
                                        tint = Color(0xFFFFB300),
                                        modifier = Modifier.size(28.dp)
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(
                                            text = query,
                                            fontSize = 12.sp,
                                            color = Color(0xFFB5A47C)
                                        )
                                        Text(
                                            text = "= ${if (mathResult % 1.0 == 0.0) mathResult.toLong() else mathResult}",
                                            fontSize = 24.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFFFFD54F),
                                            fontFamily = FontFamily.Monospace
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // If query is empty, show Quick Launch & Recent searches
                if (query.isBlank()) {
                    item {
                        Text(
                            text = "Suggested Apps",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF888CA4),
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }

                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            val suggested = viewModel.allApps.filter { it.isFavorite }.take(4)
                            for (app in suggested) {
                                AppIconItem(
                                    app = app,
                                    iconShape = viewModel.settings.iconShape,
                                    iconScale = 0.95f,
                                    showLabel = true,
                                    onClick = { onAppClick(app) }
                                )
                            }
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Recent Searches",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF888CA4),
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }

                    items(recentSearches) { term ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { query = term }
                                .padding(horizontal = 12.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.History,
                                contentDescription = null,
                                tint = Color(0xFF6B708B),
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = term,
                                fontSize = 14.sp,
                                color = Color.White
                            )
                        }
                    }
                } else {
                    // Filtered app results
                    if (matchedApps.isNotEmpty()) {
                        item {
                            Text(
                                text = "Applications (${matchedApps.size})",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF888CA4),
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                        }

                        items(matchedApps) { app ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onAppClick(app) },
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFF1B1E32))
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    AppIconItem(
                                        app = app,
                                        iconShape = viewModel.settings.iconShape,
                                        iconScale = 0.8f,
                                        showLabel = false,
                                        onClick = { onAppClick(app) }
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(
                                            text = app.displayLabel,
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                        Text(
                                            text = "${app.category.title} • ${app.packageName}",
                                            fontSize = 11.sp,
                                            color = Color(0xFF7E839E)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Web Search option
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    try {
                                        val intent = Intent(
                                            Intent.ACTION_VIEW,
                                            Uri.parse("https://www.google.com/search?q=${Uri.encode(query)}")
                                        )
                                        context.startActivity(intent)
                                    } catch (_: Exception) {}
                                },
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF16233B))
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.OpenInBrowser,
                                    contentDescription = "Search Web",
                                    tint = Color(0xFF00E5FF),
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = "Search the web for \"$query\"",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color.White
                                    )
                                    Text(
                                        text = "Open in browser with Google Search",
                                        fontSize = 11.sp,
                                        color = Color(0xFF8AA3C7)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
