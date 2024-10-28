package com.example.madetoliveapp.data.repository

import com.example.madetoliveapp.data.entity.TaskEntity
import com.example.madetoliveapp.domain.model.TaskModel

interface TaskRepository {
    suspend fun getAllTasks(): List<TaskModel> // Fetch all tasks
  //  suspend fun getTasksByIds(taskIds: IntArray): List<TaskEntity> // Fetch tasks by ID array
  //  suspend fun findTaskByTitle(firstName: String, lastName: String): TaskEntity // Find a task by title
    suspend fun insertTasks(vararg tasks: TaskEntity) // Insert multiple tasks
    suspend fun deleteTask(task: TaskEntity) // Delete a task
}