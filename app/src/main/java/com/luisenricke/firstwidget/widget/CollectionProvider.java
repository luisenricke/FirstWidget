package com.luisenricke.firstwidget.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.luisenricke.firstwidget.R;

/**
 * Implementation of App Widget functionality.
 */
public class CollectionProvider extends AppWidgetProvider {

    public static String PREVIOUS_ACTION = "previous";
    public static String NEXT_ACTION = "next";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        RemoteViews views = getRemoteViews(context);
        views.setOnClickPendingIntent(R.id.btn_next, getPendingIntent(context, NEXT_ACTION));
        views.setOnClickPendingIntent(R.id.btn_previous, getPendingIntent(context, PREVIOUS_ACTION));

        Intent intent = new Intent(context, WidgetService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);

        views.setRemoteAdapter(R.id.flipper_images, intent);
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    public static PendingIntent getPendingIntent(Context context, String action) {
        Intent intent = new Intent(context, CollectionProvider.class);
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        RemoteViews remoteViews = getRemoteViews(context);

        if (action.equals(PREVIOUS_ACTION)) {
            remoteViews.showPrevious(R.id.flipper_images);
            updateWidgetNow(context, remoteViews);
        } else if (action.equals(NEXT_ACTION)) {
            remoteViews.showNext(R.id.flipper_images);
            updateWidgetNow(context, remoteViews);
        }

        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    public static void updateWidgetNow(Context context, RemoteViews remoteViews) {
        AppWidgetManager.getInstance(context).updateAppWidget(new ComponentName(context, CollectionProvider.class), remoteViews);
    }

    public static RemoteViews getRemoteViews(Context context) {
        return new RemoteViews(context.getPackageName(), R.layout.adapter_view_flipper);
    }
}

