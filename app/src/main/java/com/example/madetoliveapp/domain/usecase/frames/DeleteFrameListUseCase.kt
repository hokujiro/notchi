package com.example.madetoliveapp.domain.usecase.frames

import com.example.madetoliveapp.data.repository.frames.FrameRepository
import com.example.madetoliveapp.domain.model.FrameModel

class DeleteFrameListUseCase(private val frameRepository: FrameRepository) {
    suspend fun execute(frameList: List<FrameModel>) {
        frameRepository.deleteFrameList(frameList)
    }
}