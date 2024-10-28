package com.example.madetoliveapp.data.mapper

import com.example.madetoliveapp.data.entity.SubTaskEntity
import com.example.madetoliveapp.data.entity.TaskEntity
import com.example.madetoliveapp.domain.model.SubTaskModel
import com.example.madetoliveapp.domain.model.TaskModel

interface RemoteMapper {

    fun toModel(
        taskEntity: TaskEntity
    ): TaskModel

    fun toSubTasksModel(
        subTaskEntity: List<SubTaskEntity>
    ): List<SubTaskModel>

}
