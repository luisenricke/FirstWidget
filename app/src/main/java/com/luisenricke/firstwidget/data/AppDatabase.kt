package com.luisenricke.firstwidget.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.luisenricke.firstwidget.data.converter.DateConverter
import com.luisenricke.firstwidget.data.dao.TimerDAO
import com.luisenricke.firstwidget.data.entity.Timer


@Database(
    entities = [
        Timer::class
    ],
    version = 1, exportSchema = false
)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun timerDAO(): TimerDAO

    companion object {
        const val NAME = "Database.db"

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: build(context)
                    .also { INSTANCE = it }
            }
        }

        fun close() {
            if (INSTANCE?.isOpen == true) {
                INSTANCE?.close()
            }
            INSTANCE = null
        }

        private fun build(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, NAME)
                .addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) { //When is created do....
                        super.onCreate(db)
                        ioThread {

                        }
                    }

                    override fun onOpen(db: SupportSQLiteDatabase) { //When is open do...
                        super.onOpen(db)

                    }
                })
                .allowMainThreadQueries() // Uncomment if threads available in MainThread
                .build()
        }

    }
}
