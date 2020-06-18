package com.luisenricke.firstwidget

import android.app.Application
import com.luisenricke.firstwidget.data.AppDatabase

class App: Application() {

    private lateinit var database: AppDatabase

    override fun onCreate() {
        super.onCreate()

        database = AppDatabase.getInstance(this)
        database.timerDAO().count()
    }
}
