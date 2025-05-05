package com.example.madetoliveapp.domain.usecase.frames

import com.example.madetoliveapp.data.repository.frames.FrameRepository
import com.example.madetoliveapp.domain.model.FrameModel

class DeleteFrameUseCase (private val frameRepository: FrameRepository) {
    suspend fun execute(frame: FrameModel) {
        frameRepository.deleteFrame(frame)
    }
}