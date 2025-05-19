package com.example.madetoliveapp.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.madetoliveapp.domain.model.RewardModel


@Entity
data class BundleEntity(
    @PrimaryKey val id: Long? = null,
    @ColumnInfo(name = "title") val title: String = "Title",
    @ColumnInfo(name = "rewards") val rewards: List<RewardEntity>?,
    @ColumnInfo(name = "icon") val icon: String? = null,
    @ColumnInfo(name = "project") val project: TaskProjectEntity? = null,
    @ColumnInfo(name = "photo") val photo: String? = null,
)
