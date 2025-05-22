package com.systems.notchi.data.repository.frames

import com.systems.notchi.domain.model.FrameModel

interface FrameRepository {
    suspend fun getAllFrames(): List<FrameModel>
    suspend fun addFrame(frame: FrameModel)
    suspend fun deleteFrame(frame: FrameModel)
    suspend fun addFrameList(frameList: List<FrameModel>)
    suspend fun deleteFrameList(frameList: List<FrameModel>)
}