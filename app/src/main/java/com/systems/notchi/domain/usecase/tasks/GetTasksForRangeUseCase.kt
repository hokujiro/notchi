package com.systems.notchi.domain.usecase.tasks

import com.systems.notchi.data.repository.tasks.TaskRepository
import com.systems.notchi.domain.model.TaskModel

class GetTasksForRangeUseCase(
    private val repository: TaskRepository
) {
    suspend fun execute(startDate: Long, endDate: Long): List<TaskModel> {
        return repository.getTasksBetween(startDate, endDate)
    }
}