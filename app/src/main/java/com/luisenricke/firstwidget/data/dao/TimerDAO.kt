package com.luisenricke.firstwidget.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.luisenricke.firstwidget.data.entity.Timer
import com.luisenricke.firstwidget.data.entity.Timer.SCHEMA

@Dao
@Suppress("unused")
abstract class TimerDAO : Base<Timer>,
    Base.UpdateDAO<Timer>,
    Base.DeleteDAO<Timer>,
    Base.PrimaryKeyDAO<Timer> {

    @Query("SELECT COUNT(*) FROM ${SCHEMA.TABLE}")
    abstract override fun count(): Long

    @Query("SELECT * FROM ${SCHEMA.TABLE}")
    abstract override fun get(): List<Timer>

    @Query("DELETE FROM ${SCHEMA.TABLE}")
    abstract override fun drop()

    @Query("SELECT * FROM ${SCHEMA.TABLE} WHERE id = :id")
    abstract override fun get(id: Long): Timer

    @Query("SELECT * FROM ${SCHEMA.TABLE} WHERE id IN(:ids)")
    abstract override fun get(ids: LongArray): List<Timer>

    @Query("DELETE FROM ${SCHEMA.TABLE} WHERE id = :id")
    abstract override fun delete(id: Long): Int

    @Query("DELETE FROM ${SCHEMA.TABLE} WHERE id IN(:ids)")
    abstract override fun deletes(ids: LongArray): Int
}
