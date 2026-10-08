package com.amjrd.novalauncher.data

import android.content.Context
import com.amjrd.novalauncher.model.*
import org.json.JSONArray
import org.json.JSONObject

class WorkspaceStore(context: Context) {
    private val prefs = context.getSharedPreferences("nova_workspace_v2", Context.MODE_PRIVATE)

    fun load(): NovaState {
        return runCatching {
            val placements = JSONArray(prefs.getString("placements", "[]"))
                .mapObjects {
                    Placement(
                        it.getString("k"),
                        it.getInt("p").coerceIn(0, 2),
                        it.getInt("x").coerceIn(0, 3),
                        it.getInt("y").coerceIn(0, 5),
                        it.optInt("sx", 1).coerceAtLeast(1),
                        it.optInt("sy", 1).coerceAtLeast(1),
                        it.optString("f").ifBlank { null }
                    )
                }
            val dock = JSONArray(prefs.getString("dock", "[]")).mapStrings().take(4)
            val folders = JSONArray(prefs.getString("folders", "[]"))
                .mapObjects {
                    NovaFolder(
                        it.getString("id"),
                        it.optString("title", "Folder"),
                        it.getJSONArray("members").mapStrings()
                    )
                }
            NovaState(
                placements = placements,
                dock = dock,
                folders = folders
            )
        }.getOrElse { NovaState() }
    }

    fun save(s: NovaState) {
        runCatching {
            val placements = JSONArray()
            s.placements.forEach { o ->
                placements.put(JSONObject().apply {
                    put("k", o.key)
                    put("p", o.page)
                    put("x", o.x)
                    put("y", o.y)
                    put("sx", o.spanX)
                    put("sy", o.spanY)
                    put("f", o.folderId ?: "")
                })
            }

            val dock = JSONArray()
            s.dock.take(4).forEach(dock::put)

            val folders = JSONArray()
            s.folders.forEach { folder ->
                folders.put(JSONObject().apply {
                    put("id", folder.id)
                    put("title", folder.title)
                    put("members", JSONArray(folder.members))
                })
            }

            prefs.edit()
                .putString("placements", placements.toString())
                .putString("dock", dock.toString())
                .putString("folders", folders.toString())
                .apply()
        }
    }

    private fun JSONArray.mapStrings(): List<String> =
        (0 until length()).map { getString(it) }

    private inline fun <T> JSONArray.mapObjects(
        block: (JSONObject) -> T
    ): List<T> = (0 until length()).map { block(getJSONObject(it)) }
}
