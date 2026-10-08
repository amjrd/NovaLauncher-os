package com.amjrd.novalauncher.widget

import android.appwidget.AppWidgetHost
import android.appwidget.AppWidgetHostView
import android.appwidget.AppWidgetManager
import android.content.Context

class NovaWidgetHost(context: Context): AppWidgetHost(context, 0x4E4F5641) {
    fun view(context: Context,id: Int): AppWidgetHostView? {
        val info=AppWidgetManager.getInstance(context).getAppWidgetInfo(id) ?: return null
        return runCatching{createView(context,id,info)}.getOrNull()
    }
}