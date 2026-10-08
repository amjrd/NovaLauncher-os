package com.amjrd.novalauncher.data
import android.content.Context
import com.amjrd.novalauncher.model.*
import org.json.JSONArray
import org.json.JSONObject
class WorkspaceStore(context:Context){
 private val prefs=context.getSharedPreferences("nova_workspace_v2",Context.MODE_PRIVATE)
 fun load():NovaState{
  val placements=JSONArray(prefs.getString("placements","[]")).mapObjects{Placement(it.getString("k"),it.getInt("p"),it.getInt("x"),it.getInt("y"),it.optInt("sx",1),it.optInt("sy",1),it.optString("f").ifBlank{null})}
  val dock=JSONArray(prefs.getString("dock","[]")).mapStrings()
  val folders=JSONArray(prefs.getString("folders","[]")).mapObjects{NovaFolder(it.getString("id"),it.optString("title","Folder"),it.getJSONArray("members").mapStrings())}
  return NovaState(placements=placements,dock=dock,folders=folders)
 }
 fun save(s:NovaState){val p=JSONArray();s.placements.forEach{o->p.put(JSONObject().apply{put("k",o.key);put("p",o.page);put("x",o.x);put("y",o.y);put("sx",o.spanX);put("sy",o.spanY);put("f",o.folderId?:"")})};val d=JSONArray();s.dock.forEach(d::put);val f=JSONArray();s.folders.forEach{folder->f.put(JSONObject().apply{put("id",folder.id);put("title",folder.title);put("members",JSONArray(folder.members))})};prefs.edit().putString("placements",p.toString()).putString("dock",d.toString()).putString("folders",f.toString()).apply()}
 private fun JSONArray.mapStrings()=(0 until length()).map{getString(it)}
 private inline fun <T> JSONArray.mapObjects(block:(JSONObject)->T)=(0 until length()).map{block(getJSONObject(it))}
}