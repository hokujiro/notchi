package com.example.madetoliveapp.domain.usecase.tasks

import com.example.madetoliveapp.data.repository.tasks.TaskRepository
import com.example.madetoliveapp.domain.model.TaskModel

class GetTasksForDayUseCase (private val taskRepository: TaskRepository) {
    suspend fun execute(date: Long): List<TaskModel> {
        return taskRepository.getTasksForDay(date)
    }
}