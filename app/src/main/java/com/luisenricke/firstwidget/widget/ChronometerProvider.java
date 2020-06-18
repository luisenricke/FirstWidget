package com.luisenricke.firstwidget.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.luisenricke.firstwidget.R;
import com.luisenricke.firstwidget.activity.WidgetDialog;

/**
 * Implementation of App Widget functionality.
 */
public class ChronometerProvider extends AppWidgetProvider {

    private static String start = "start";
    private static String pause = "pause";
    private static String finalize = "finalize";
    private static boolean pauseClicked = false;
    private static boolean startClicked = false;
    private static long getBase = 0;
    private static long timePause = 0;
    private static RemoteViews remoteViews;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        remoteViews = getRemoteViews(context);
        remoteViews.setOnClickPendingIntent(R.id.btn_start, getPendingIntent(context, start));
        remoteViews.setOnClickPendingIntent(R.id.btn_pause, getPendingIntent(context, pause));
        remoteViews.setOnClickPendingIntent(R.id.btn_finalize, getPendingIntent(context, finalize));

        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
    }

    public static PendingIntent getPendingIntent(Context context, String action) {
        Intent intent = new Intent(context, ChronometerProvider.class);
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        remoteViews = getRemoteViews(context);

        if (start.equals(intent.getAction())) {
            getBase = SystemClock.elapsedRealtime();
            remoteViews.setChronometer(R.id.chronometer3,getBase + timePause,null,true);
            pauseClicked = false;
            startClicked = true;
            updateWidgetNow(context, remoteViews);
        } else if (pause.equals(intent.getAction())) {
            if (!pauseClicked) {
                timePause += (getBase - SystemClock.elapsedRealtime());
                remoteViews.setChronometer(R.id.chronometer3,SystemClock.elapsedRealtime() + timePause,null,false);
                pauseClicked = true;
                updateWidgetNow(context, remoteViews);
            }
        } else if (finalize.equals(intent.getAction())) {
            if (startClicked) {
                if (pauseClicked) {
                    Intent alertDialog = new Intent(context, WidgetDialog.class);
                    alertDialog.putExtra(WidgetDialog.TIME_PAUSE, timePause);
                    alertDialog.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    context.startActivity(alertDialog);
                } else {
                    Toast.makeText(context, context.getString(R.string.pause_clicked), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(context, context.getString(R.string.start_clicked), Toast.LENGTH_SHORT).show();
            }
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

    public static RemoteViews getRemoteViews(Context context) {
        return new RemoteViews(context.getPackageName(), R.layout.chronometer_provider);
    }

    public static void updateWidgetNow(Context context, RemoteViews remoteViews) {
        AppWidgetManager.getInstance(context).updateAppWidget(new ComponentName(context, ChronometerProvider.class), remoteViews);

    }

    public static void restart(){
        pauseClicked = false;
        startClicked = false;
        getBase = 0;
        timePause = 0;
    }
}

