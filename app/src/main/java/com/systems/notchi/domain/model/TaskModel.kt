package com.systems.notchi.domain.model

import java.util.Date
import java.util.UUID

data class TaskModel (
    val uid: String = UUID.randomUUID().toString(),
    val checked: Boolean,
    val title: String = "Title",
    val subTasks: List<SubTaskModel>? = listOf(),
    val project: TaskProjectModel?,
    val date: Date?,
    val points: Int?
)