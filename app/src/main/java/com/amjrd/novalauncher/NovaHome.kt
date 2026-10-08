package com.amjrd.novalauncher

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import com.amjrd.novalauncher.core.LauncherViewModel
import com.amjrd.novalauncher.model.*
import com.amjrd.novalauncher.widget.NovaWidgetHost
import java.text.SimpleDateFormat
import java.util.*

@OptIn(androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
fun NovaHome(s:NovaState,vm:LauncherViewModel,host:NovaWidgetHost,requestHome:()->Unit,pickWidget:()->Unit){
    val pager=rememberPagerState(initialPage=s.page.coerceIn(0,2),pageCount={3})
    LaunchedEffect(pager.currentPage){vm.setPage(pager.currentPage)}
    LaunchedEffect(s.page){if(pager.currentPage!=s.page)pager.animateScrollToPage(s.page)}
    Box(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)){
        Column(Modifier.fillMaxSize().padding(horizontal=18.dp)){
            Spacer(Modifier.height(38.dp))
            Text(SimpleDateFormat("EEEE، d MMMM",Locale.getDefault()).format(Date()),fontSize=22.sp)
            Text("NOVA",style=MaterialTheme.typography.labelMedium)
            Spacer(Modifier.height(8.dp))
            HorizontalPager(state=pager,modifier=Modifier.weight(1f)){page->HomePage(s,vm,page,host)}
        }
        Dock(s,vm)
        if(s.editing){
            Row(Modifier.align(Alignment.TopCenter).padding(top=8.dp),horizontalArrangement=Arrangement.spacedBy(6.dp)){
                FilledTonalButton(onClick=pickWidget){Text("إضافة Widget")}
                TextButton(onClick=vm::editing){Text("تم")}
            }
        } else {
            TextButton(onClick=vm::editing,modifier=Modifier.align(Alignment.TopEnd).padding(top=8.dp)){Text("تحرير")}
        }
        if(s.drawer) Drawer(s,vm,requestHome)
    }
}

@Composable
private fun HomePage(s:NovaState,vm:LauncherViewModel,page:Int,host:NovaWidgetHost){
    Column(Modifier.fillMaxSize()){
        s.widgets.filter{it.page==page}.forEach{WidgetBox(it,host)}
        val apps=s.placements.filter{it.page==page&&it.folderId==null}.sortedWith(compareBy<Placement>{it.y}.thenBy{it.x})
        val folders=s.folders.filter{it.page==page}.sortedWith(compareBy<NovaFolder>{it.y}.thenBy{it.x})
        val cells=apps.mapNotNull{p->s.apps.firstOrNull{it.key==p.key}?.let{p to it}}
        LazyVerticalGrid(columns=GridCells.Fixed(4),modifier=Modifier.weight(1f),contentPadding=PaddingValues(top=12.dp,bottom=110.dp),horizontalArrangement=Arrangement.spacedBy(4.dp),verticalArrangement=Arrangement.spacedBy(14.dp)){
            items(cells,key={it.first.key}){(p,a)->DraggableIcon(p,a,s,vm)}
            items(folders,key={it.id}){folder->FolderIcon(folder,s)}
        }
    }
}

@Composable
private fun DraggableIcon(p:Placement,a:NovaApp,s:NovaState,vm:LauncherViewModel){
    var dragging by remember{mutableStateOf(false)}
    var dx by remember{mutableFloatStateOf(0f)}
    var dy by remember{mutableFloatStateOf(0f)}
    Column(Modifier.pointerInput(p.key,s.editing){detectDragGesturesAfterLongPress(
        onDragStart={if(s.editing)dragging=true},
        onDrag={change,amount->{change.consume();dx+=amount.x;dy+=amount.y}},
        onDragEnd={
            if(dragging){
                val tx=(p.x+(dx/90f).toInt()).coerceIn(0,3)
                val ty=(p.y+(dy/92f).toInt()).coerceIn(0,5)
                val target=s.placements.firstOrNull{it.page==p.page&&it.folderId==null&&it.key!=p.key&&it.x==tx&&it.y==ty}?.key
                vm.move(p.key,p.page,tx,ty,target)
            }
            dragging=false;dx=0f;dy=0f
        },
        onDragCancel={dragging=false;dx=0f;dy=0f}
    ).padding(2.dp),horizontalAlignment=Alignment.CenterHorizontally){
        Image(a.icon.toBitmap(96,96).asImageBitmap(),a.label,Modifier.size(58.dp).clip(RoundedCornerShape(17.dp)),contentScale=ContentScale.Crop)
        Text(a.label,maxLines=1,style=MaterialTheme.typography.labelSmall)
    }
}

@Composable private fun FolderIcon(folder:NovaFolder,s:NovaState){
    val members=folder.members.mapNotNull{k->s.apps.firstOrNull{it.key==k}}
    Column(horizontalAlignment=Alignment.CenterHorizontally,modifier=Modifier.padding(2.dp)){
        Surface(Modifier.size(58.dp),RoundedCornerShape(17.dp),tonalElevation=3.dp){
            Row(Modifier.fillMaxSize(),horizontalArrangement=Arrangement.Center,verticalAlignment=Alignment.CenterVertically){
                members.take(4).forEach{Image(it.icon.toBitmap(48,48).asImageBitmap(),null,Modifier.size(24.dp))}
            }
        }
        Text(folder.title,maxLines=1,style=MaterialTheme.typography.labelSmall)
    }
}

@Composable private fun WidgetBox(w:WidgetPlacement,host:NovaWidgetHost){
    val context=LocalContext.current
    val view=remember(w.appWidgetId){host.view(context,w.appWidgetId)}
    if(view!=null) androidx.compose.ui.viewinterop.AndroidView(factory={view},modifier=Modifier.fillMaxWidth().height((w.spanY*92).dp).padding(4.dp))
}

@Composable private fun Dock(s:NovaState,vm:LauncherViewModel){
    Box(Modifier.fillMaxWidth().height(86.dp).padding(12.dp)){
        Surface(Modifier.fillMaxSize(),RoundedCornerShape(26.dp),color=MaterialTheme.colorScheme.surface.copy(alpha=.9f),tonalElevation=5.dp){
            Row(Modifier.fillMaxSize().padding(6.dp),horizontalArrangement=Arrangement.SpaceEvenly,verticalAlignment=Alignment.CenterVertically){
                s.dock.mapNotNull{k->s.apps.firstOrNull{it.key==k}}.take(4).forEach{a->
                    IconButton(onClick={vm.launch(LocalContext.current,a)}){
                        Image(a.icon.toBitmap(72,72).asImageBitmap(),a.label,Modifier.size(50.dp).clip(RoundedCornerShape(15.dp)))
                    }
                }
                IconButton(onClick={vm::drawer}){Icon(Icons.Rounded.Apps,"التطبيقات")}
            }
        }
    }
}

@Composable private fun Drawer(s:NovaState,vm:LauncherViewModel,requestHome:()->Unit){
    val list=s.apps.filter{it.label.contains(s.query,true)}
    Surface(Modifier.fillMaxSize().padding(top=70.dp),color=MaterialTheme.colorScheme.surface.copy(alpha=.98f)){
        Column(Modifier.fillMaxSize().padding(16.dp)){
            OutlinedTextField(s.query,vm::query,Modifier.fillMaxWidth(),singleLine=true,placeholder={Text("بحث في التطبيقات")},leadingIcon={Icon(Icons.Rounded.Search,null)})
            Row(Modifier.fillMaxWidth().padding(vertical=8.dp),horizontalArrangement=Arrangement.SpaceBetween){
                Text("كل التطبيقات",style=MaterialTheme.typography.titleMedium)
                TextButton(onClick=requestHome){Text("NOVA كالمشغل الرئيسي")}
            }
            LazyVerticalGrid(columns=GridCells.Adaptive(76.dp),contentPadding=PaddingValues(bottom=20.dp)){
                items(list,key={it.key}){a->
                    Column(Modifier.padding(4.dp),horizontalAlignment=Alignment.CenterHorizontally){
                        IconButton(onClick={vm.launch(LocalContext.current,a);vm.drawer(false)}){
                            Image(a.icon.toBitmap(64,64).asImageBitmap(),a.label,Modifier.size(50.dp).clip(RoundedCornerShape(14.dp)))
                        }
                        Text(a.label,maxLines=1,style=MaterialTheme.typography.labelSmall)
                    }
                }
            }
        }
    }
}