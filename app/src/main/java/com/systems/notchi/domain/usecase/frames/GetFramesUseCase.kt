package com.systems.notchi.domain.usecase.frames

import com.systems.notchi.data.repository.tasks.TaskRepository
import com.systems.notchi.domain.model.FrameModel

class GetFramesUseCase (private val taskRepository: TaskRepository) {
    suspend fun execute(): List<FrameModel> {
        return taskRepository.getAllFrames()
    }
}