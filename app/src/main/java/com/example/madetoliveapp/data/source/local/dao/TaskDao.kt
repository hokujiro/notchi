package com.example.madetoliveapp.data.source.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.madetoliveapp.data.entity.TaskEntity

@Dao
interface TaskDao {

    @Insert
    fun insertAll(vararg users: TaskEntity)

    @Delete
    fun delete(user: TaskEntity)
}