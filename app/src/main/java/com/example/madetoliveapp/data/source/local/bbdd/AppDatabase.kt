package com.example.madetoliveapp.data.source.local.bbdd

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.madetoliveapp.data.entity.SubTaskEntity
import com.example.madetoliveapp.data.entity.TaskEntity
import com.example.madetoliveapp.data.mapper.Converters
import com.example.madetoliveapp.data.source.local.dao.TaskDao

@Database(entities = [TaskEntity::class, SubTaskEntity::class], version = 4, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}