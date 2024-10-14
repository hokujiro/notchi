package com.example.madetoliveapp.data.mapper

import androidx.room.TypeConverter
import com.example.madetoliveapp.data.entity.SubTaskEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TypeConverters {
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

}