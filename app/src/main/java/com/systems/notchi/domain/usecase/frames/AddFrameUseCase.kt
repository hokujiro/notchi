package com.systems.notchi.domain.usecase.frames

import com.systems.notchi.data.repository.tasks.TaskRepository
import com.systems.notchi.domain.model.FrameModel

class AddFrameUseCase(private val taskRepository: TaskRepository) {
        suspend fun execute(frame: FrameModel) {
       taskRepository.addFrame(frame)
    }
}