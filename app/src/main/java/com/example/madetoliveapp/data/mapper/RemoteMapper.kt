package com.example.madetoliveapp.data.mapper

import com.example.madetoliveapp.data.entity.DailyPointsSummaryEntity
import com.example.madetoliveapp.data.entity.ProjectEntity
import com.example.madetoliveapp.data.entity.SubTaskEntity
import com.example.madetoliveapp.data.entity.TaskEntity
import com.example.madetoliveapp.data.entity.TaskProjectEntity
import com.example.madetoliveapp.domain.model.DailyPointsSummaryModel
import com.example.madetoliveapp.domain.model.ProjectModel
import com.example.madetoliveapp.domain.model.SubTaskModel
import com.example.madetoliveapp.domain.model.TaskModel
import com.example.madetoliveapp.domain.model.TaskProjectModel

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

    fun toTaskDomainModel(
        entity: TaskEntity
    ): TaskModel

    fun toProjectDomainModel(
        entity: ProjectEntity?
    ): ProjectModel

    fun toProjectEntity(
        project: ProjectModel
    ): ProjectEntity

    fun toTaskProjectEntity(project: TaskProjectModel?): TaskProjectEntity

    fun toTaskProjectDomainModel(entity: TaskProjectEntity?): TaskProjectModel

    fun toModel(entity: DailyPointsSummaryEntity?): DailyPointsSummaryModel

}
