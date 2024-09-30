package com.example.madetoliveapp.domain.usecase

import com.example.madetoliveapp.data.entity.TaskEntity
import com.example.madetoliveapp.data.repository.TaskRepository

class GetTasksUseCase (private val taskRepository: TaskRepository) {
    suspend fun execute(): List<TaskEntity> {
        return taskRepository.getAllTasks()
    }
}