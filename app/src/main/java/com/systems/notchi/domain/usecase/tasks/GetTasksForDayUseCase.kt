package com.systems.notchi.domain.usecase.tasks

import com.systems.notchi.data.repository.tasks.TaskRepository
import com.systems.notchi.domain.model.TaskModel

class GetTasksForDayUseCase (private val taskRepository: TaskRepository) {
    suspend fun execute(date: Long): List<TaskModel> {
        return taskRepository.getTasksForDay(date)
    }
}