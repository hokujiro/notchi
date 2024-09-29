package com.example.madetoliveapp.data.source.local.bbdd

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.madetoliveapp.data.entity.TaskEntity
import com.example.madetoliveapp.data.source.local.dao.TaskDao

@Database(entities = [TaskEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}