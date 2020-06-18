package com.luisenricke.firstwidget.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.Glide;
import com.luisenricke.firstwidget.R;
import com.luisenricke.firstwidget.bd.Animal;

import java.util.ArrayList;

class WidgetViewFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context context;
    private int idWidget = 0;
    private ArrayList<Animal> animals = new ArrayList<>();

    public WidgetViewFactory(Context context, Intent intent) {
        this.context = context;
        idWidget = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        animals = new Animal().getAnimals();
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return animals.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        // TODO: implements glide or picasso
        Bitmap bitmap = null;
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.page_animal);
        Animal animal = animals.get(position);
        remoteViews.setTextViewText(R.id.txt_animal, animal.getName());
        try {
            remoteViews.setImageViewResource(R.id.img_animal, animal.getImage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
