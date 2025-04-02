package com.example.madetoliveapp.data.repository

import com.example.madetoliveapp.data.entity.ProjectEntity
import com.example.madetoliveapp.data.entity.TaskEntity
import com.example.madetoliveapp.domain.model.ProjectModel
import com.example.madetoliveapp.domain.model.TaskModel

interface TaskRepository {
    suspend fun getAllTasks(): List<TaskModel> // Fetch all tasks
    suspend fun getTasksForDay(date: Long): List<TaskModel> // Fetch all tasks
    suspend fun insertTask(task: TaskModel) // Insert multiple tasks
    suspend fun deleteTask(id: String) // Delete a task
    suspend fun updateTask(task: TaskModel) // Update a task
    suspend fun addProject(project: ProjectModel) // Add a project
    suspend fun getAllProjects(): List<ProjectModel>
}