package com.systems.notchi.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class BundleEntity(
    @PrimaryKey val id: Long? = null,
    @ColumnInfo(name = "title") val title: String = "Title",
    @ColumnInfo(name = "rewards") val rewards: List<RewardEntity>?,
    @ColumnInfo(name = "icon") val icon: String? = null,
    @ColumnInfo(name = "project") val project: TaskProjectEntity? = null,
    @ColumnInfo(name = "photo") val photo: String? = null,
)
