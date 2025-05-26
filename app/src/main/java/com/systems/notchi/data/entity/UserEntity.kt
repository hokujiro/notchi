package com.systems.notchi.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity
data class UserEntity(
    @ColumnInfo(name = "id") val id: Long,
    @ColumnInfo(name = "username") val username: String,
    @ColumnInfo(name = "email") val email: String,
    @ColumnInfo(name = "photo") val photo: String?,
    @ColumnInfo(name = "totalPoints") val totalPoints: Float
)
