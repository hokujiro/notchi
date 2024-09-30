package com.example.madetoliveapp.domain.usecase

import com.example.madetoliveapp.data.entity.TaskEntity
import com.example.madetoliveapp.data.repository.TaskRepository

class DeleteTaskUseCase(private val taskRepository: TaskRepository) {
    suspend fun execute(task: TaskEntity) {
        taskRepository.deleteTask(task)
    }
}