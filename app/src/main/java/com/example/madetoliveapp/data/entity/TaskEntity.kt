package com.example.madetoliveapp.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TaskEntity(
    @PrimaryKey val uid: String,
    @ColumnInfo(name = "checked") val checked: Boolean,
    @ColumnInfo(name = "title") val title: String = "Title",
    @ColumnInfo(name = "subTasks") val subTasks: List<SubTaskEntity>?,
    @ColumnInfo(name = "project") val project: TaskProjectEntity? = null,
    @ColumnInfo(name = "date") val date: String?,
    @ColumnInfo(name = "points") val points: Int?
)