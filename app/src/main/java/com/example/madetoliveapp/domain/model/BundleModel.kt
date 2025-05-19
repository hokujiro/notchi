package com.example.madetoliveapp.domain.model

import java.util.UUID

data class BundleModel (
    val id: String = UUID.randomUUID().toString(),
    val title: String = "Title",
    val rewards: List<RewardModel>?,
    val icon: String?,
    val project: TaskProjectModel?,
    val photo: String?,
)