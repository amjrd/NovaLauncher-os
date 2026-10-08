package com.amjrd.novalauncher

import android.app.role.RoleManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.EdgeToEdge
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.view.WindowCompat
import com.amjrd.novalauncher.core.LauncherViewModel

class MainActivity : ComponentActivity() {
    private val vm by viewModels<LauncherViewModel>()
    private val homeRequest =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EdgeToEdge.enable(this)
        WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars = false
        vm.refresh(this)

        setContent {
            NovaTheme {
                NovaLauncherRoot(
                    vm = vm,
                    activity = this
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        vm.refresh(this)
    }

    private fun requestHome() {
        val rm = getSystemService(RoleManager::class.java)
        if (rm.isRoleAvailable(RoleManager.ROLE_HOME) && !rm.isRoleHeld(RoleManager.ROLE_HOME)) {
            homeRequest.launch(rm.createRequestRoleIntent(RoleManager.ROLE_HOME))
        }
    }

    @androidx.compose.runtime.Composable
    private fun NovaLauncherRoot(
        vm: LauncherViewModel,
        activity: MainActivity
    ) {
        val state = vm.state.collectAsState().value
        NovaHome(
            s = state,
            launch = { vm.launch(activity, it) },
            query = vm::query,
            toggleDrawer = { vm.drawer() },
            toggleEdit = vm::editing,
            toggleDock = vm::toggleDock,
            pageChanged = vm::setPage,
            requestHome = ::requestHome
        )
    }
}
