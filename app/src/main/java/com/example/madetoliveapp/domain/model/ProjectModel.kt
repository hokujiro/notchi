package com.example.madetoliveapp.domain.model

data class ProjectModel  (
    val uid: String = "",
    val title: String = "Title",
    val subtitle: String = "Subtitle",
    val tasksList: List<TaskModel>? = listOf(),
    val color: String = "Color",
    val icon: String = "Icon"
)