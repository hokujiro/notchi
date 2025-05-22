package com.systems.notchi.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DayEntity(
    @PrimaryKey val uid: Int,
    @ColumnInfo(name = "tasks") val tasks: List<TaskEntity>?,
    @ColumnInfo(name = "date") val date: String?,
    @ColumnInfo(name = "totalPoints") val totalPoints: String?
)