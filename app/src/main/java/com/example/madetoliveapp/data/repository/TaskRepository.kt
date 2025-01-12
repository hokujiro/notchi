package com.example.madetoliveapp.data.repository

import com.example.madetoliveapp.data.entity.TaskEntity
import com.example.madetoliveapp.domain.model.TaskModel

interface TaskRepository {
    suspend fun getAllTasks(): List<TaskModel> // Fetch all tasks
    suspend fun getTasksForDay(date: Long): List<TaskModel> // Fetch all tasks
    suspend fun insertTask(task: TaskModel) // Insert multiple tasks
    suspend fun deleteTask(task: TaskModel) // Delete a task
}