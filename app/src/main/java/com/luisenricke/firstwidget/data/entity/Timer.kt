package com.luisenricke.firstwidget.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.luisenricke.firstwidget.data.entity.Timer.SCHEMA.DATE
import com.luisenricke.firstwidget.data.entity.Timer.SCHEMA.DESCRIPTION
import com.luisenricke.firstwidget.data.entity.Timer.SCHEMA.DISTANCE
import com.luisenricke.firstwidget.data.entity.Timer.SCHEMA.ID
import com.luisenricke.firstwidget.data.entity.Timer.SCHEMA.TABLE
import com.luisenricke.firstwidget.data.entity.Timer.SCHEMA.TIME

@Entity(tableName = TABLE)
data class Timer(
    @ColumnInfo(name = TIME)
    var time: String,
    @ColumnInfo(name = DATE)
    var date: String,
    @ColumnInfo(name = DISTANCE)
    var distance: String,
    @ColumnInfo(name = DESCRIPTION)
    var description: String,
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ID)
    val id: Long = 0
) {
    object SCHEMA {
        const val TABLE = "Timer"
        const val ID = "id"
        const val TIME = "time"
        const val DATE = "date"
        const val DISTANCE = "distance"
        const val DESCRIPTION = "description"
    }
}
