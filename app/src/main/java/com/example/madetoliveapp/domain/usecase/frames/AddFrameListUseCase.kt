package com.example.madetoliveapp.domain.usecase.frames

import com.example.madetoliveapp.data.repository.frames.FrameRepository
import com.example.madetoliveapp.domain.model.FrameModel

class AddFrameListUseCase(private val frameRepository: FrameRepository) {
    suspend fun execute(frameList: List<FrameModel>) {
        frameRepository.addFrameList(frameList)
    }
}