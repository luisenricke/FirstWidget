package com.luisenricke.firstwidget.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.ContextThemeWrapper;
import android.widget.RemoteViews;

import com.luisenricke.firstwidget.R;
import com.luisenricke.firstwidget.widget.ChronometerProvider;

public class WidgetDialog extends Activity {

    public static final String TIME_PAUSE = "time_pause";
    private long timePause = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Bundle data = getIntent().getExtras();
        if(data != null){
            timePause = getIntent().getExtras().getLong(TIME_PAUSE);
        }

        showDialog();
    }

    private void showDialog() {
        new AlertDialog.Builder(getDialogContext())
                .setTitle(getString(R.string.title))
                .setMessage(getString(R.string.message))
                .setPositiveButton(getString(R.string.save), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        RemoteViews remoteViews = ChronometerProvider.getRemoteViews(getApplicationContext());
                        remoteViews.setChronometer(R.id.chronometer3, SystemClock.elapsedRealtime(), null, false);
                        ChronometerProvider.updateWidgetNow(getApplicationContext(),remoteViews);
                        Intent main = new Intent(getApplicationContext(), MainActivity2.class);
                        main.putExtra(WidgetDialog.TIME_PAUSE, timePause);
                        main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(main);
                        dialog.dismiss();
                        finish();
                        ChronometerProvider.restart();
                    }
                })
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                })
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        finish();
                    }
                })
                .show();
    }

    private Context getDialogContext() {
        return new ContextThemeWrapper(this, android.R.style.Theme_Dialog);
    }
}
