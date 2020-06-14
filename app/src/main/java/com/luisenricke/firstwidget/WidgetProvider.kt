package com.luisenricke.firstwidget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.luisenricke.androidext.Toast

class WidgetProvider : AppWidgetProvider() {
    override fun onUpdate(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetIds: IntArray?
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        Toast.short(context!!, "onUpdate")
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        Toast.short(context!!, "onReceive")
    }

    override fun onEnabled(context: Context?) {
        super.onEnabled(context)
        Toast.short(context!!, "onEnabled")
    }

    override fun onDisabled(context: Context?) {
        super.onDisabled(context)
        Toast.short(context!!, "onDisabled")
    }

    override fun onDeleted(context: Context?, appWidgetIds: IntArray?) {
        super.onDeleted(context, appWidgetIds)
        Toast.short(context!!, "onDeleted")
    }

    override fun onAppWidgetOptionsChanged(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetId: Int,
        newOptions: Bundle?
    ) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions)
        Toast.short(context!!, "onAppWidgetOptionsChanged")
    }
}
