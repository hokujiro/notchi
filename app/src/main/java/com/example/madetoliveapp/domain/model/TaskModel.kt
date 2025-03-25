package com.example.madetoliveapp.domain.model

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import com.example.madetoliveapp.data.entity.SubTaskEntity
import java.util.Date

data class TaskModel (
    val uid: String = "",
    val checked: Boolean,
    val title: String = "Title",
    val subTasks: List<SubTaskModel>?,
    val category: String?,
    val finishingDate: Date,
    val points: Int?
)