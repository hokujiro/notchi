package com.systems.notchi.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class HabitEntity(
    @PrimaryKey val uid: Int,
    @ColumnInfo(name = "counter") val counter: Int?,
    @ColumnInfo(name = "title") val title: String?,
    @ColumnInfo(name = "points") val points: Int?
)