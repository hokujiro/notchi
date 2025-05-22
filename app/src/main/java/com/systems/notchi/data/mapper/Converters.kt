package com.systems.notchi.data.mapper

import androidx.room.TypeConverter
import com.systems.notchi.data.entity.SubTaskEntity
import com.systems.notchi.data.entity.TaskProjectEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    private val gson = Gson()

    // Convertir List<SubTaskEntity> a String
    @TypeConverter
    fun fromSubTaskList(value: List<SubTaskEntity>?): String? {
        return value?.let { gson.toJson(it) }
    }

    // Convertir String a List<SubTaskEntity>
    @TypeConverter
    fun toSubTaskList(value: String?): List<SubTaskEntity>? {
        return value?.let {
            val listType = object : TypeToken<List<SubTaskEntity>>() {}.type
            gson.fromJson(it, listType)
        }
    }


    @TypeConverter
    fun fromProject(value: TaskProjectEntity?): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toProject(value: String): TaskProjectEntity? {
        val type = object : TypeToken<TaskProjectEntity>() {}.type
        return gson.fromJson(value, type)
    }

}