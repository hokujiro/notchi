package com.example.madetoliveapp.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Task(
    @PrimaryKey val uid: Int,
    @ColumnInfo(name = "check") val check: Boolean?,
    @ColumnInfo(name = "title") val title: String?
)