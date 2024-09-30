package com.example.madetoliveapp.domain.usecase

import com.example.madetoliveapp.data.entity.TaskEntity
import com.example.madetoliveapp.data.repository.TaskRepository

class AddTaskUseCase(private val taskRepository: TaskRepository) {
    suspend fun execute(vararg tasks: TaskEntity) {
        taskRepository.insertTasks(*tasks)
    }
}