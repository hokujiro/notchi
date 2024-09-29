package com.example.madetoliveapp.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TaskEntity(
    @PrimaryKey val uid: Int,
    @ColumnInfo(name = "checked") val checked: Boolean,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "subTasks") val subTasks: List<SubTaskEntity>?,
    @ColumnInfo(name = "category") val category: String?,
    @ColumnInfo(name = "finishingDate") val finishingDate: String?,
    @ColumnInfo(name = "points") val points: Int?
)