package com.amjrd.novalauncher.data

import android.content.Context
import com.amjrd.novalauncher.model.*
import org.json.JSONArray
import org.json.JSONObject

class WorkspaceStore(context: Context) {
    private val prefs = context.getSharedPreferences("nova_workspace_v3", Context.MODE_PRIVATE)
    fun load(): NovaState = runCatching {
        val placements = JSONArray(prefs.getString("placements","[]")).mapObjects {
            Placement(it.getString("k"),it.getInt("p"),it.getInt("x"),it.getInt("y"),it.optInt("sx",1),it.optInt("sy",1),it.optString("f").ifBlank{null})
        }
        val folders = JSONArray(prefs.getString("folders","[]")).mapObjects {
            NovaFolder(it.getString("id"),it.optString("title","Folder"),it.getJSONArray("members").mapStrings(),it.optInt("p"),it.optInt("x"),it.optInt("y"))
        }
        val widgets = JSONArray(prefs.getString("widgets","[]")).mapObjects {
            WidgetPlacement(it.getInt("id"),it.getString("provider"),it.getInt("p"),it.getInt("x"),it.getInt("y"),it.optInt("sx",2),it.optInt("sy",2))
        }
        NovaState(placements=placements,folders=folders,widgets=widgets,dock=JSONArray(prefs.getString("dock","[]")).mapStrings().take(4))
    }.getOrElse { NovaState() }

    fun save(s: NovaState) {
        runCatching {
            val p=JSONArray();s.placements.forEach{p.put(JSONObject().apply{put("k",it.key);put("p",it.page);put("x",it.x);put("y",it.y);put("sx",it.spanX);put("sy",it.spanY);put("f",it.folderId?:"")})}
            val f=JSONArray();s.folders.forEach{f.put(JSONObject().apply{put("id",it.id);put("title",it.title);put("members",JSONArray(it.members));put("p",it.page);put("x",it.x);put("y",it.y)})}
            val w=JSONArray();s.widgets.forEach{w.put(JSONObject().apply{put("id",it.appWidgetId);put("provider",it.provider);put("p",it.page);put("x",it.x);put("y",it.y);put("sx",it.spanX);put("sy",it.spanY)})}
            prefs.edit().putString("placements",p.toString()).putString("folders",f.toString()).putString("widgets",w.toString()).putString("dock",JSONArray(s.dock.take(4)).toString()).apply()
        }
    }
    private fun JSONArray.mapStrings(): List<String>=(0 until length()).map{getString(it)}
    private inline fun <T> JSONArray.mapObjects(block:(JSONObject)->T):List<T>=(0 until length()).map{block(getJSONObject(it))}
}