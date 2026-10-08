package com.amjrd.novalauncher

import android.app.Activity
import android.app.role.RoleManager
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProviderInfo
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.core.view.WindowCompat
import com.amjrd.novalauncher.core.LauncherViewModel
import com.amjrd.novalauncher.widget.NovaWidgetHost

class MainActivity: ComponentActivity() {
    private val vm by viewModels<LauncherViewModel>()
    private lateinit var widgetHost: NovaWidgetHost
    private var pendingWidgetId = -1

    private val homeRequest = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {}

    private val widgetPick = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode != Activity.RESULT_OK) return@registerForActivityResult
        val data = result.data ?: return@registerForActivityResult
        val id = data.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, -1)
        val info = data.getParcelableExtra<AppWidgetProviderInfo>(AppWidgetManager.EXTRA_APPWIDGET_PROVIDER) ?: return@registerForActivityResult
        if (id > 0) bindOrConfigure(id, info)
    }

    private val widgetBind = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode != Activity.RESULT_OK) return@registerForActivityResult
        val id = pendingWidgetId
        pendingWidgetId = -1
        val info = AppWidgetManager.getInstance(this).getAppWidgetInfo(id) ?: return@registerForActivityResult
        if (info.configure != null) startWidgetConfig(id, info) else vm.addWidget(id, info.provider.flattenToString())
    }

    private val widgetConfig = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val id = pendingWidgetId
        pendingWidgetId = -1
        if (result.resultCode == Activity.RESULT_OK && id > 0) {
            val info = AppWidgetManager.getInstance(this).getAppWidgetInfo(id)
            if (info != null) vm.addWidget(id, info.provider.flattenToString())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        widgetHost = NovaWidgetHost(this)
        vm.refresh(this)
        setContent {
            NovaTheme {
                val state = vm.state.collectAsState().value
                NovaHome(state, vm, widgetHost, ::requestHome, ::pickWidget)
            }
        }
    }

    override fun onStart() { super.onStart(); widgetHost.startListening() }
    override fun onStop() { widgetHost.stopListening(); super.onStop() }
    override fun onResume() { super.onResume(); vm.refresh(this) }

    private fun requestHome() {
        val role = getSystemService(RoleManager::class.java)
        if (role.isRoleAvailable(RoleManager.ROLE_HOME) && !role.isRoleHeld(RoleManager.ROLE_HOME)) {
            homeRequest.launch(role.createRequestRoleIntent(RoleManager.ROLE_HOME))
        }
    }

    private fun pickWidget() {
        val id = widgetHost.allocateAppWidgetId()
        widgetPick.launch(Intent(AppWidgetManager.ACTION_APPWIDGET_PICK).putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, id))
    }

    private fun bindOrConfigure(id: Int, info: AppWidgetProviderInfo) {
        val manager = AppWidgetManager.getInstance(this)
        if (!manager.bindAppWidgetIdIfAllowed(id, info.provider, null)) {
            pendingWidgetId = id
            widgetBind.launch(Intent(AppWidgetManager.ACTION_APPWIDGET_BIND).apply {
                putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, id)
                putExtra(AppWidgetManager.EXTRA_APPWIDGET_PROVIDER, info.provider)
            })
            return
        }
        if (info.configure != null) startWidgetConfig(id, info) else vm.addWidget(id, info.provider.flattenToString())
    }

    private fun startWidgetConfig(id: Int, info: AppWidgetProviderInfo) {
        pendingWidgetId = id
        widgetConfig.launch(Intent().setComponent(info.configure).putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, id))
    }
}