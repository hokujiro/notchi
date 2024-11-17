package com.example.madetoliveapp.domain.model

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

data class SubTaskModel(
    val uid: Int,
    val check: Boolean?,
    val title: String?,
)