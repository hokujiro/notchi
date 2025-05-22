package com.systems.notchi.domain.usecase.tasks

import com.systems.notchi.data.repository.tasks.TaskRepository
import com.systems.notchi.domain.model.TaskModel

class DeleteTaskUseCase(private val taskRepository: TaskRepository) {
    suspend fun execute(task: TaskModel) {
        taskRepository.deleteTask(task.uid)
    }
}