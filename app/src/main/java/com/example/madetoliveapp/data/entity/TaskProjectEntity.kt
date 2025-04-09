package com.example.madetoliveapp.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity
data class TaskProjectEntity (
    @ColumnInfo(name = "title") val title: String = "Title",
    @ColumnInfo(name = "color") val color: String? = "Color",
    @ColumnInfo(name = "icon") val icon: String? = "Icon",
)