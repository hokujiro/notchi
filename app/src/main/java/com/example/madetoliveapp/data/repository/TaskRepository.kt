package com.example.madetoliveapp.data.repository

import com.example.madetoliveapp.data.entity.TaskEntity

interface TaskRepository {
    suspend fun getAllTasks(): List<TaskEntity> // Fetch all tasks
    suspend fun getTasksByIds(taskIds: IntArray): List<TaskEntity> // Fetch tasks by ID array
    suspend fun findTaskByTitle(firstName: String, lastName: String): TaskEntity // Find a task by title
    suspend fun insertTasks(vararg tasks: TaskEntity) // Insert multiple tasks
    suspend fun deleteTask(task: TaskEntity) // Delete a task
}