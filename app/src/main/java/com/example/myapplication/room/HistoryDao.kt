package com.example.myapplication.room

import android.database.Cursor
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface HistoryDao {
    @Query("SELECT * FROM HistoryEntity")
    fun all(): List<HistoryEntity>

    @Query("SELECT * FROM HistoryEntity WHERE city LIKE :city")
    fun getDataByWord(city: String): List<HistoryEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(entity: HistoryEntity)

    @Update
    fun update(entity: HistoryEntity)

    @Delete
    fun delete(entity: HistoryEntity)

    @Query("DELETE FROM HistoryEntity WHERE id = :id")
    fun deleteById(id: Long)

    @Query("SELECT id, city, temperature FROM HistoryEntity")
    fun getHistoryCursor(): Cursor

    @Query("SELECT id, city, temperature FROM HistoryEntity WHERE id = :id")
    fun getHistoryCursor(id: Long): Cursor
}