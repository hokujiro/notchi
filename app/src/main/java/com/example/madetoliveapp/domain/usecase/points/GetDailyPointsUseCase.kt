package com.example.madetoliveapp.domain.usecase.points

import com.example.madetoliveapp.data.repository.tasks.TaskRepository
import com.example.madetoliveapp.domain.model.DailyPointsSummaryModel

class GetDailyPointsUseCase (private val taskRepository: TaskRepository) {
    suspend fun execute(date: Long): DailyPointsSummaryModel {
        return taskRepository.getPointsForDay(date)
    }
}