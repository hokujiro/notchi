package com.systems.notchi.data.source.local.bbdd

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.systems.notchi.data.entity.SubTaskEntity
import com.systems.notchi.data.entity.TaskEntity
import com.systems.notchi.data.mapper.Converters
import com.systems.notchi.data.source.local.dao.TaskDao

@Database(entities = [TaskEntity::class, SubTaskEntity::class], version = 5, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}