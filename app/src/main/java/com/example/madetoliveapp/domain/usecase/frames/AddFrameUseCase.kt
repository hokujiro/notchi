package com.example.madetoliveapp.domain.usecase.frames

import com.example.madetoliveapp.data.repository.tasks.TaskRepository
import com.example.madetoliveapp.domain.model.FrameModel

class AddFrameUseCase(private val taskRepository: TaskRepository) {
        suspend fun execute(frame: FrameModel) {
       taskRepository.addFrame(frame)
    }
}