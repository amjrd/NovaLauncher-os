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

class LauncherViewModel(app: Application): AndroidViewModel(app) {
    private val store=WorkspaceStore(app)
    private val _state=MutableStateFlow(store.load())
    val state=_state.asStateFlow()

    fun refresh(context:Context) {
        val la=context.getSystemService(LauncherApps::class.java)?:return
        val um=context.getSystemService(UserManager::class.java)?:return
        val apps=la.profiles.flatMap{user->runCatching{
            val serial=um.getSerialNumberForUser(user)
            la.getActivityList(null,user).map{info->NovaApp(info.componentName,info.label?.toString().orEmpty(),info.getIcon(context.resources.displayMetrics.densityDpi),serial)}
        }.getOrElse{emptyList()}}.distinctBy{it.key}.sortedBy{it.label.lowercase()}
        val old=_state.value
        val valid=old.placements.filter{p->apps.any{it.key==p.key}}
        val missing=apps.filter{a->valid.none{it.key==a.key}&&old.dock.none{it==a.key}}
        val additions=missing.mapIndexed{i,a->Placement(a.key,0,i%4,(i/4)%6)}
        val dock=old.dock.filter{key->apps.any{it.key==key}}.take(4).ifEmpty{apps.take(4).map{it.key}}
        _state.value=old.copy(apps=apps,placements=(valid+additions).distinctBy{it.key},dock=dock)
        store.save(_state.value)
    }

    fun launch(context:Context,app:NovaApp) {
        val la=context.getSystemService(LauncherApps::class.java)?:return
        val um=context.getSystemService(UserManager::class.java)?:return
        val user=la.profiles.firstOrNull{runCatching{um.getSerialNumberForUser(it)==app.userSerial}.getOrDefault(false)}?:Process.myUserHandle()
        runCatching{la.startMainActivity(app.component,user,null,null)}
    }
    fun query(v:String){_state.value=_state.value.copy(query=v)}
    fun drawer(v:Boolean?=null){_state.value=_state.value.copy(drawer=v?:!_state.value.drawer,query="")}
    fun editing(){_state.value=_state.value.copy(editing=!_state.value.editing)}
    fun setPage(p:Int){_state.value=_state.value.copy(page=p.coerceIn(0,2));store.save(_state.value)}
    fun move(key:String,page:Int,x:Int,y:Int,targetKey:String?=null){
        if(targetKey!=null&&targetKey!=key){createFolder(key,targetKey);return}
        _state.value=_state.value.copy(placements=_state.value.placements.map{if(it.key==key)it.copy(page=page.coerceIn(0,2),x=x.coerceIn(0,3),y=y.coerceIn(0,5),folderId=null)else it});store.save(_state.value)
    }
    fun toggleDock(key:String){val d=_state.value.dock.toMutableList();if(key in d)d.remove(key)else if(d.size<4)d.add(key);_state.value=_state.value.copy(dock=d);store.save(_state.value)}
    fun createFolder(a:String,b:String){
        if(a==b)return
        val s=_state.value
        if(s.folders.any{a in it.members||b in it.members})return
        val p=s.placements.firstOrNull{it.key==a}?:return
        val id=UUID.randomUUID().toString()
        _state.value=s.copy(folders=s.folders+NovaFolder(id,"Folder",listOf(a,b),p.page,p.x,p.y),placements=s.placements.map{if(it.key==a||it.key==b)it.copy(folderId=id)else it})
        store.save(_state.value)
    }
    fun renameFolder(id:String,title:String){_state.value=_state.value.copy(folders=_state.value.folders.map{if(it.id==id)it.copy(title=title)else it});store.save(_state.value)}
    fun addWidget(id:Int,provider:String){_state.value=_state.value.copy(widgets=_state.value.widgets+WidgetPlacement(id,provider,_state.value.page,0,0,2,2));store.save(_state.value)}
    fun removeWidget(id:Int){_state.value=_state.value.copy(widgets=_state.value.widgets.filterNot{it.appWidgetId==id});store.save(_state.value)}
}