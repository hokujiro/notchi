package com.systems.notchi.domain.usecase.frames

import com.systems.notchi.data.repository.frames.FrameRepository
import com.systems.notchi.domain.model.FrameModel

class DeleteFrameListUseCase(private val frameRepository: FrameRepository) {
    suspend fun execute(frameList: List<FrameModel>) {
        frameRepository.deleteFrameList(frameList)
    }
}