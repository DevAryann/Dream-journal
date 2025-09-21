package com.example.dreamjournal.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface DreamDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(dream: Dream): Long

    @Update
    suspend fun update(dream: Dream)

    @Delete
    suspend fun delete(dream: Dream)

    @Query("SELECT * FROM dreams ORDER BY timestamp DESC")
    fun observeAll(): Flow<List<Dream>>

    @Query("SELECT * FROM dreams WHERE id = :id")
    fun observeById(id: Long): Flow<Dream?>

    @Query("SELECT * FROM dreams WHERE timestamp BETWEEN :from AND :to ORDER BY timestamp ASC")
    fun observeInRange(from: Long, to: Long): Flow<List<Dream>>
}
