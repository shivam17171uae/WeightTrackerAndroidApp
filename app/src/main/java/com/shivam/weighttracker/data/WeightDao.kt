package com.shivam.weighttracker.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update // Import Update
import kotlinx.coroutines.flow.Flow

@Dao
interface WeightDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(entry: WeightEntry)

    @Update // Add Update annotation
    suspend fun update(entry: WeightEntry) // Add update function

    @Delete
    suspend fun delete(entry: WeightEntry)

    @Query("SELECT * FROM weight_table ORDER BY timestamp DESC")
    fun getAllEntries(): Flow<List<WeightEntry>>
}