package com.systems.notchi.data.repository.frames

import com.systems.notchi.data.mapper.RemoteMapper
import com.systems.notchi.data.source.remote.api.TaskApi
import com.systems.notchi.domain.model.FrameModel

class FrameRepositoryImpl(
    private val mapper: RemoteMapper,
    private val taskApi: TaskApi
) : FrameRepository {

    override suspend fun getAllFrames(): List<FrameModel> {
        val response = taskApi.getAllFrames()
        if (response.isSuccessful) {
            return response.body()?.map { mapper.toFrameDomainModel(it) } ?: emptyList()
        } else {
            throw Exception("Failed to load frames: ${response.errorBody()?.string()}")
        }
    }

    override suspend fun addFrame(frame: FrameModel) {
        taskApi.addFrame(mapper.toFrameEntity(frame))
    }

    override suspend fun deleteFrame(frame: FrameModel) {
        taskApi.deleteFrame(frame.uid)
    }

    override suspend fun addFrameList(frameList: List<FrameModel>) {
        val frameEntities = frameList.map { mapper.toFrameEntity(it) }
        taskApi.addFrameList(frameEntities)
    }

    override suspend fun deleteFrameList(frameList: List<FrameModel>) {
        val frameIds = frameList.map {it.uid}
        taskApi.deleteFrameList(frameIds)
    }

}