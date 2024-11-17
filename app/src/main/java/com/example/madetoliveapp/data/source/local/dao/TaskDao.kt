package com.example.madetoliveapp.data.source.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.madetoliveapp.data.entity.TaskEntity

@Dao
interface TaskDao {

    @Query("SELECT * FROM TaskEntity")
    suspend fun getAll(): List<TaskEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: TaskEntity)

    @Delete
    suspend fun delete(task: TaskEntity)
}