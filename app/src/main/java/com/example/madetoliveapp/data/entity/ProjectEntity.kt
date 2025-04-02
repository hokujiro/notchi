package com.example.madetoliveapp.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class ProjectEntity (
    @PrimaryKey val uid: String,
    @ColumnInfo(name = "title") val title: String = "Title",
    @ColumnInfo(name = "tasksList") val tasksList: List<TaskEntity>?,
    @ColumnInfo(name = "color") val color: String?
)
