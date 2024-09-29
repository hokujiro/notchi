package com.example.madetoliveapp.data.source.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.madetoliveapp.data.entity.TaskEntity

@Dao
interface TaskDao {
    @Query("SELECT * FROM user")
    fun getAll(): List<TaskEntity>

    @Query("SELECT * FROM user WHERE uid IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<TaskEntity>

    @Query("SELECT * FROM user WHERE first_name LIKE :first AND " +
            "last_name LIKE :last LIMIT 1")
    fun findByTitle(first: String, last: String): TaskEntity

    @Insert
    fun insertAll(vararg users: TaskEntity)

    @Delete
    fun delete(user: TaskEntity)
}