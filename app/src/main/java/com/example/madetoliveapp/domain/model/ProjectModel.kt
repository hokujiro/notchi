package com.example.madetoliveapp.domain.model

import java.util.Date

data class ProjectModel  (
    val uid: String = "",
    val title: String = "Title",
    val tasksList: List<TaskModel>? = listOf(),
    val color: String = "Color"
)