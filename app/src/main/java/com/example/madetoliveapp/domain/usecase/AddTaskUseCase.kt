package com.example.madetoliveapp.domain.usecase

import com.example.madetoliveapp.data.entity.TaskEntity
import com.example.madetoliveapp.data.repository.TaskRepository
import com.example.madetoliveapp.domain.model.TaskModel

class AddTaskUseCase(private val taskRepository: TaskRepository) {
    suspend fun execute(task: TaskModel) {
       taskRepository.insertTask(task)
    }
}