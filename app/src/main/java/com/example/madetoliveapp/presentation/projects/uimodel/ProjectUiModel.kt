package com.example.madetoliveapp.presentation.projects.uimodel

import com.example.madetoliveapp.domain.model.TaskModel

data class ProjectUiModel(
    val uid: String = "uid",
    val title: String = "Title",
    val subtitle: String = "Subtitle",
    val tasksList: List<TaskModel>? = listOf(),
    val color: String = "Color",
    val icon: String = "Icon",
    val totalTasks: Int = 0,
    val completedTasks: Int = 0
)