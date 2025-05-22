package com.systems.notchi.domain.usecase.frames

import com.systems.notchi.data.repository.frames.FrameRepository
import com.systems.notchi.domain.model.FrameModel

class DeleteFrameUseCase (private val frameRepository: FrameRepository) {
    suspend fun execute(frame: FrameModel) {
        frameRepository.deleteFrame(frame)
    }
}