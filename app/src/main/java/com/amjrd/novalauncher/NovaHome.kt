package com.amjrd.novalauncher

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.item
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Apps
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import com.amjrd.novalauncher.model.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NovaHome(
    s: NovaState,
    launch: (NovaApp) -> Unit,
    query: (String) -> Unit,
    toggleDrawer: () -> Unit,
    toggleEdit: () -> Unit,
    toggleDock: (String) -> Unit,
    pageChanged: (Int) -> Unit,
    requestHome: () -> Unit
) {
    val pager = rememberPagerState(
        initialPage = s.page.coerceIn(0, 2),
        pageCount = { 3 }
    )

    LaunchedEffect(pager.currentPage) {
        pageChanged(pager.currentPage)
    }

    LaunchedEffect(s.page) {
        val target = s.page.coerceIn(0, 2)
        if (pager.currentPage != target) pager.animateScrollToPage(target)
    }

    Box(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ) {
            Spacer(Modifier.height(42.dp))
            Text(
                SimpleDateFormat("EEEE، d MMMM", Locale.getDefault()).format(Date()),
                fontSize = 22.sp
            )
            Text(
                "NOVA",
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.padding(top = 2.dp)
            )
            Spacer(Modifier.height(12.dp))

            HorizontalPager(
                state = pager,
                modifier = Modifier.weight(1f)
            ) { page ->
                HomeGrid(
                    s = s,
                    page = page,
                    launch = launch,
                    toggleDock = toggleDock,
                    editing = s.editing,
                    toggleEdit = toggleEdit
                )
            }
        }

        Dock(s, launch, toggleDrawer)

        if (s.editing) {
            TextButton(
                onClick = toggleEdit,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 12.dp)
            ) {
                Text("إنهاء التحرير")
            }
        }

        AnimatedVisibility(
            visible = s.drawer,
            modifier = Modifier.fillMaxSize()
        ) {
            Drawer(s, query, launch, toggleDrawer, requestHome)
        }
    }
}

@Composable
private fun HomeGrid(
    s: NovaState,
    page: Int,
    launch: (NovaApp) -> Unit,
    toggleDock: (String) -> Unit,
    editing: Boolean,
    toggleEdit: () -> Unit
) {
    val itemsOnPage = s.placements.filter {
        it.page == page && it.folderId == null
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(top = 18.dp, bottom = 120.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        items(itemsOnPage, key = { it.key }) { placement ->
            s.apps.firstOrNull { it.key == placement.key }?.let { app ->
                NovaIcon(
                    a = app,
                    click = { launch(app) },
                    long = {
                        if (!editing) toggleEdit()
                    },
                    edit = toggleEdit
                )
            }
        }
    }
}

@Composable
private fun NovaIcon(
    a: NovaApp,
    click: () -> Unit,
    long: () -> Unit,
    edit: () -> Unit
) {
    Column(
        modifier = Modifier.pointerInput(Unit) {
            detectTapGestures(
                onLongPress = { long() },
                onTap = { click() }
            )
        },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            bitmap = a.icon.toBitmap(72, 72).asImageBitmap(),
            contentDescription = a.label,
            modifier = Modifier
                .size(58.dp)
                .clip(RoundedCornerShape(17.dp))
        )
        Text(
            text = a.label,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Composable
private fun Dock(
    s: NovaState,
    launch: (NovaApp) -> Unit,
    open: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(14.dp)
            .height(76.dp),
        shape = RoundedCornerShape(28.dp),
        color = MaterialTheme.colorScheme.surface.copy(alpha = .82f),
        tonalElevation = 5.dp
    ) {
        Row(
            Modifier.fillMaxSize().padding(8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            s.dock.take(4)
                .mapNotNull { key -> s.apps.firstOrNull { it.key == key } }
                .forEach { app ->
                    Image(
                        bitmap = app.icon.toBitmap(64, 64).asImageBitmap(),
                        contentDescription = app.label,
                        modifier = Modifier
                            .size(52.dp)
                            .clip(RoundedCornerShape(15.dp))
                            .pointerInput(Unit) {
                                detectTapGestures { launch(app) }
                            }
                    )
                }
            IconButton(onClick = open) {
                Icon(Icons.Rounded.Apps, contentDescription = "التطبيقات")
            }
        }
    }
}

@Composable
private fun Drawer(
    s: NovaState,
    query: (String) -> Unit,
    launch: (NovaApp) -> Unit,
    close: () -> Unit,
    requestHome: () -> Unit
) {
    val list = s.apps.filter { it.label.contains(s.query, true) }

    Surface(
        modifier = Modifier.fillMaxSize().padding(top = 78.dp),
        color = MaterialTheme.colorScheme.surface.copy(alpha = .98f)
    ) {
        Column(
            Modifier.fillMaxSize().padding(18.dp)
        ) {
            OutlinedTextField(
                value = s.query,
                onValueChange = query,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                placeholder = { Text("بحث في التطبيقات") },
                leadingIcon = { Icon(Icons.Rounded.Search, contentDescription = null) }
            )

            Text(
                "كل التطبيقات",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(vertical = 12.dp)
            )

            LazyVerticalGrid(
                columns = GridCells.Adaptive(78.dp),
                contentPadding = PaddingValues(bottom = 12.dp)
            ) {
                items(list, key = { it.key }) { app ->
                    Column(
                        modifier = Modifier.padding(4.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        IconButton(onClick = {
                            launch(app)
                            close()
                        }) {
                            Image(
                                bitmap = app.icon.toBitmap(64, 64).asImageBitmap(),
                                contentDescription = app.label,
                                modifier = Modifier
                                    .size(52.dp)
                                    .clip(RoundedCornerShape(14.dp))
                            )
                        }
                        Text(
                            text = app.label,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Button(
                        onClick = requestHome,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        Text("تعيين NOVA كمشغل رئيسي")
                    }
                }
            }
        }
    }
}
