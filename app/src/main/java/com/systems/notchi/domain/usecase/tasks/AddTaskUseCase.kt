package com.systems.notchi.domain.usecase.tasks

import com.systems.notchi.data.repository.tasks.TaskRepository
import com.systems.notchi.domain.model.TaskModel

class AddTaskUseCase(private val taskRepository: TaskRepository) {
    suspend fun execute(task: TaskModel) {
       taskRepository.insertTask(task)
    }
}