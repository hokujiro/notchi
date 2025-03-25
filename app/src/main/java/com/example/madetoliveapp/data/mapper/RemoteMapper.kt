package com.example.madetoliveapp.data.mapper

import com.example.madetoliveapp.data.entity.SubTaskEntity
import com.example.madetoliveapp.data.entity.TaskEntity
import com.example.madetoliveapp.domain.model.SubTaskModel
import com.example.madetoliveapp.domain.model.TaskModel

interface RemoteMapper {

    fun toModel(
        entity: TaskEntity
    ): TaskModel

    fun toEntity(
        model: TaskModel
    ): TaskEntity


    fun toSubTasksModel(
        subTaskEntity: List<SubTaskEntity>
    ): List<SubTaskModel>

    fun toSubTasksEntity(
        subTaskModel: List<SubTaskModel>
    ): List<SubTaskEntity>

    fun toDomainModel(
        entity: TaskEntity
    ): TaskModel

}
