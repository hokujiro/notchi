package com.example.madetoliveapp.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class ProjectEntity (
    @PrimaryKey val uid: Long? = null,
    @ColumnInfo(name = "title") val title: String = "Title",
    @ColumnInfo(name = "subtitle") val subtitle: String = "Subtitle",
    @ColumnInfo(name = "tasks") val tasks: List<TaskEntity>?,
    @ColumnInfo(name = "color") val color: String?,
    @ColumnInfo(name = "icon") val icon: String?,
)
