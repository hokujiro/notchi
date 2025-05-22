package com.systems.notchi.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity
data class DailyPointsSummaryEntity(
    @ColumnInfo(name = "total") val total: Float,
    @ColumnInfo(name = "positive") val positive: Float,
    @ColumnInfo(name = "negative") val negative: Float
)