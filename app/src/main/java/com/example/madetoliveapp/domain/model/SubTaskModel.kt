package com.example.madetoliveapp.domain.model

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

class SubTaskModel(
    val uid: Int,
    val check: Boolean?,
    val title: String?,
)