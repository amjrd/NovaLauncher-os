package com.amjrd.novalauncher
import android.app.role.RoleManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.amjrd.novalauncher.core.LauncherViewModel
class MainActivity:ComponentActivity(){
 private val vm by viewModels<LauncherViewModel>();private val homeRequest=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){}
 override fun onCreate(savedInstanceState:Bundle?){super.onCreate(savedInstanceState);vm.refresh(this);setContent{val state by vm.state.collectAsState();NovaTheme{NovaHome(state,{vm.launch(this,it)},vm::query,{vm.drawer()},vm::editing,vm::toggleDock,{requestHome()})}}}
 private fun requestHome(){val rm=getSystemService(RoleManager::class.java);if(rm.isRoleAvailable(RoleManager.ROLE_HOME)&&!rm.isRoleHeld(RoleManager.ROLE_HOME))homeRequest.launch(rm.createRequestRoleIntent(RoleManager.ROLE_HOME))}
}