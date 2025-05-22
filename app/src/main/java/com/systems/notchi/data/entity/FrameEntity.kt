package com.systems.notchi.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FrameEntity(
    @PrimaryKey val uid: Long? = null,
    @ColumnInfo(name = "title") val title: String = "Title",
    @ColumnInfo(name = "project") val project: TaskProjectEntity?,
    @ColumnInfo(name = "points")val points: Int?
)