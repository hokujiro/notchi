package com.example.madetoliveapp.domain.usecase.tasks

import com.example.madetoliveapp.data.repository.tasks.TaskRepository
import com.example.madetoliveapp.domain.model.TaskModel

class DeleteTaskUseCase(private val taskRepository: TaskRepository) {
    suspend fun execute(task: TaskModel) {
        taskRepository.deleteTask(task.uid)
    }
}