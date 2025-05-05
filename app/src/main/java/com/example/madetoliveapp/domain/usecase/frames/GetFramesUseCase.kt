package com.example.madetoliveapp.domain.usecase.frames

import com.example.madetoliveapp.data.repository.tasks.TaskRepository
import com.example.madetoliveapp.domain.model.FrameModel

class GetFramesUseCase (private val taskRepository: TaskRepository) {
    suspend fun execute(): List<FrameModel> {
        return taskRepository.getAllFrames()
    }
}