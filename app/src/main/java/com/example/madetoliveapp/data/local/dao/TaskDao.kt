package com.example.madetoliveapp.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.madetoliveapp.data.entity.Task

@Dao
interface TaskDao {
    @Query("SELECT * FROM user")
    fun getAll(): List<Task>

    @Query("SELECT * FROM user WHERE uid IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<Task>

    @Query("SELECT * FROM user WHERE first_name LIKE :first AND " +
            "last_name LIKE :last LIMIT 1")
    fun findByTitle(first: String, last: String): Task

    @Insert
    fun insertAll(vararg users: Task)

    @Delete
    fun delete(user: Task)
}