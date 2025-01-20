package com.example.madetoliveapp.domain.usecase

import com.example.madetoliveapp.data.entity.TaskEntity
import com.example.madetoliveapp.data.repository.TaskRepository
import com.example.madetoliveapp.domain.model.TaskModel
import java.util.Date

class GetTasksForDayUseCase (private val taskRepository: TaskRepository) {
    suspend fun execute(date: Long): List<TaskModel> {
        return taskRepository.getTasksForDay(date)
    }
}