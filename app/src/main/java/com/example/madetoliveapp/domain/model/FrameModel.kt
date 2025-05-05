package com.example.madetoliveapp.domain.model

import java.util.UUID

data class FrameModel (
    val uid: String = UUID.randomUUID().toString(),
    val title: String = "Title",
    val project: TaskProjectModel?,
    val points: Int?
)