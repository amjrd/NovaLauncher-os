package com.amjrd.novalauncher.core

import android.app.Application
import android.content.Context
import android.content.pm.LauncherApps
import android.os.Process
import android.os.UserManager
import androidx.lifecycle.AndroidViewModel
import com.amjrd.novalauncher.data.WorkspaceStore
import com.amjrd.novalauncher.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

class LauncherViewModel(app: Application) : AndroidViewModel(app) {
    private val store = WorkspaceStore(app)
    private val _state = MutableStateFlow(store.load())
    val state = _state.asStateFlow()

    fun refresh(context: Context) {
        val la = context.getSystemService(LauncherApps::class.java) ?: return
        val um = context.getSystemService(UserManager::class.java) ?: return

        val apps = runCatching {
            la.profiles.flatMap { user ->
                val serial = um.getSerialNumberForUser(user)
                la.getActivityList(null, user).map { info ->
                    NovaApp(
                        component = info.componentName,
                        label = info.label?.toString().orEmpty(),
                        icon = info.getIcon(context.resources.displayMetrics.densityDpi),
                        userSerial = serial
                    )
                }
            }
        }.getOrElse { emptyList() }
            .distinctBy { it.key }
            .sortedBy { it.label.lowercase() }

        val old = _state.value
        val valid = old.placements.filter { placement ->
            apps.any { app -> app.key == placement.key }
        }
        val missing = apps.filter { app ->
            valid.none { placement -> placement.key == app.key }
        }
        val additions = missing.take(24).mapIndexed { i, app ->
            Placement(app.key, 0, i % 4, i / 4)
        }
        val dock = old.dock
            .filter { key -> apps.any { app -> app.key == key } }
            .take(4)
            .ifEmpty { apps.take(4).map { it.key } }

        _state.value = old.copy(
            apps = apps,
            placements = (valid + additions).distinctBy { it.key },
            dock = dock
        )
        store.save(_state.value)
    }

    fun launch(context: Context, app: NovaApp) {
        val la = context.getSystemService(LauncherApps::class.java) ?: return
        val um = context.getSystemService(UserManager::class.java) ?: return
        val user = la.profiles.firstOrNull {
            um.getSerialNumberForUser(it) == app.userSerial
        } ?: Process.myUserHandle()

        runCatching {
            la.startMainActivity(app.component, user, null, null)
        }
    }

    fun query(value: String) {
        _state.value = _state.value.copy(query = value)
    }

    fun drawer(value: Boolean? = null) {
        _state.value = _state.value.copy(
            drawer = value ?: !_state.value.drawer,
            query = ""
        )
    }

    fun editing() {
        _state.value = _state.value.copy(editing = !_state.value.editing)
    }

    fun setPage(page: Int) {
        val safe = page.coerceIn(0, 2)
        if (_state.value.page != safe) {
            _state.value = _state.value.copy(page = safe)
            store.save(_state.value)
        }
    }

    fun move(key: String, page: Int, x: Int, y: Int) {
        _state.value = _state.value.copy(
            placements = _state.value.placements.map {
                if (it.key == key) {
                    it.copy(
                        page = page.coerceIn(0, 2),
                        x = x.coerceIn(0, 3),
                        y = y.coerceIn(0, 5)
                    )
                } else it
            }
        )
        store.save(_state.value)
    }

    fun toggleDock(key: String) {
        val d = _state.value.dock.toMutableList()
        if (key in d) d.remove(key) else if (d.size < 4) d.add(key)
        _state.value = _state.value.copy(dock = d)
        store.save(_state.value)
    }

    fun folder(a: String, b: String) {
        if (a == b) return
        val current = _state.value
        if (current.placements.any { it.key == a && it.folderId != null }) return
        if (current.placements.any { it.key == b && it.folderId != null }) return

        val id = UUID.randomUUID().toString()
        _state.value = current.copy(
            folders = current.folders + NovaFolder(id, "Folder", listOf(a, b)),
            placements = current.placements.map {
                if (it.key == a || it.key == b) it.copy(folderId = id) else it
            }
        )
        store.save(_state.value)
    }
}
