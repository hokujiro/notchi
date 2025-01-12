package com.example.madetoliveapp.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)  val uid: Int = 0,
    @ColumnInfo(name = "checked") val checked: Boolean,
    @ColumnInfo(name = "title") val title: String = "Title",
    @ColumnInfo(name = "subTasks") val subTasks: List<SubTaskEntity>?,
    @ColumnInfo(name = "category") val category: String?,
    @ColumnInfo(name = "finishingDate") val finishingDate: Long,
    @ColumnInfo(name = "points") val points: Int?
)