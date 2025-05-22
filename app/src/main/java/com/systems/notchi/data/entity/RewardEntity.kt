package com.systems.notchi.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class RewardEntity(
    @PrimaryKey val id: Long? = null,
    @ColumnInfo(name = "title") val title: String = "Title",
    @ColumnInfo(name = "points") val points: Int = 0,
    @ColumnInfo(name = "icon") val icon: String? = null,
    @ColumnInfo(name = "project") val project: TaskProjectEntity? = null,
    @ColumnInfo(name = "redeemed") val redeemed: Boolean = false,
    @ColumnInfo(name = "reusable") val reusable: Boolean = false,
    @ColumnInfo(name = "photo") val photo: String? = null,
)
