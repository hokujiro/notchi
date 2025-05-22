package com.systems.notchi.domain.model

import java.util.UUID

data class RewardModel (
    val id: String = UUID.randomUUID().toString(),
    val title: String = "Title",
    val project: TaskProjectModel?,
    val points: Int?,
    val icon: String?,
    val redeemed: Boolean = false,
    val reusable: Boolean = false,
    val photo: String? = null,
)