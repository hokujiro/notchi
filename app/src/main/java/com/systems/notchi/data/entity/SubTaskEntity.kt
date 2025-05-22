package com.systems.notchi.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SubTaskEntity(
    @PrimaryKey val uid: Int,
    @ColumnInfo(name = "check") val check: Boolean?,
    @ColumnInfo(name = "title") val title: String?,
)