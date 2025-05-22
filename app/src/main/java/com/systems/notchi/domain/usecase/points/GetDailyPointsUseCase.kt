package com.systems.notchi.domain.usecase.points

import com.systems.notchi.data.repository.tasks.TaskRepository
import com.systems.notchi.domain.model.DailyPointsSummaryModel

class GetDailyPointsUseCase (private val taskRepository: TaskRepository) {
    suspend fun execute(date: Long): DailyPointsSummaryModel {
        return taskRepository.getPointsForDay(date)
    }
}