package com.example.madetoliveapp.domain.usecase

import com.example.madetoliveapp.data.repository.TaskRepository
import com.example.madetoliveapp.domain.model.DailyPointsSummaryModel
import com.example.madetoliveapp.domain.model.TaskModel

class GetDailyPointsUseCase (private val taskRepository: TaskRepository) {
    suspend fun execute(date: Long): DailyPointsSummaryModel {
        return taskRepository.getPointsForDay(date)
    }
}