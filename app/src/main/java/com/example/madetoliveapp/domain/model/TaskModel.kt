package com.example.madetoliveapp.domain.model

import java.util.Date

data class TaskModel (
    val uid: String = "",
    val checked: Boolean,
    val title: String = "Title",
    val subTasks: List<SubTaskModel>? = listOf(),
    val project: TaskProjectModel,
    val date: Date?,
    val points: Int?
)