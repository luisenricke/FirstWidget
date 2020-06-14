package com.luisenricke.firstwidget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.view.View
import android.widget.RemoteViews

/**
 * Implementation of App Widget functionality.
 */
class ImageProvider : AppWidgetProvider() {
    // TODO: Refactor this
    companion object {
        const val onClick = "onClick"
        const val onClick2 = "onClick2"
        lateinit var remoteViews: RemoteViews

        fun updateWidgetNow(context: Context, remoteViews: RemoteViews) {
            AppWidgetManager.getInstance(context)
                .updateAppWidget(ComponentName(context, ImageProvider::class.java), remoteViews)
        }

        fun getRemoteView(context: Context): RemoteViews {
            return RemoteViews(context.packageName, R.layout.image_provider)
        }
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget2(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        remoteViews = getRemoteView(context!!)
        if(onClick.equals(intent?.action)){
            hide(R.id.imgUp)
            hide(R.id.imgDown)
            hide(R.id.imgLeft)
            hide(R.id.imgRight)

            hide(R.id.txtUp)
            hide(R.id.txtDown)
            hide(R.id.txtLeft)
            hide(R.id.txtRight)

            hide(R.id.imgBtn)
            show(R.id.imgBtn2)
            updateWidgetNow(context, ImageProvider.remoteViews)
        }

        if(onClick2.equals(intent?.action)){
            show(R.id.imgUp)
            show(R.id.imgDown)
            show(R.id.imgLeft)
            show(R.id.imgRight)

            show(R.id.txtUp)
            show(R.id.txtDown)
            show(R.id.txtLeft)
            show(R.id.txtRight)

            show(R.id.imgBtn)
            hide(R.id.imgBtn2)
            updateWidgetNow(context, ImageProvider.remoteViews)
        }

    }
}

internal fun show(id: Int){
    ImageProvider.remoteViews.setViewVisibility(id, View.VISIBLE)
}

internal fun hide(id: Int){
    ImageProvider.remoteViews.setViewVisibility(id, View.GONE)
}

internal fun getPEndingIntent(context: Context, action: String): PendingIntent {
    var intent = Intent(context, ImageProvider::class.java)
    intent.setAction(action)
    return PendingIntent.getBroadcast(context,0,intent,0)

}

internal fun updateAppWidget2(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    ImageProvider.remoteViews = ImageProvider.getRemoteView(context)
    ImageProvider.remoteViews.setImageViewBitmap(
        R.id.imgUp,
        BitmapFactory.decodeResource(context.resources, R.drawable.up)
    )
    ImageProvider.remoteViews.setImageViewBitmap(
        R.id.imgDown,
        BitmapFactory.decodeResource(context.resources, R.drawable.down)
    )
    ImageProvider.remoteViews.setImageViewBitmap(
        R.id.imgRight,
        BitmapFactory.decodeResource(context.resources, R.drawable.right)
    )
    ImageProvider.remoteViews.setImageViewBitmap(
        R.id.imgLeft,
        BitmapFactory.decodeResource(context.resources, R.drawable.left)
    )

    ImageProvider.remoteViews.setOnClickPendingIntent(R.id.imgBtn, getPEndingIntent(context,ImageProvider.onClick))
    ImageProvider.remoteViews.setOnClickPendingIntent(R.id.imgBtn2, getPEndingIntent(context,ImageProvider.onClick2))

    appWidgetManager.updateAppWidget(appWidgetId, ImageProvider.remoteViews)
}
