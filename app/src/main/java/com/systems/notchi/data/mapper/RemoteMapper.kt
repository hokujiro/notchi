package com.systems.notchi.data.mapper

import com.systems.notchi.data.entity.DailyPointsSummaryEntity
import com.systems.notchi.data.entity.FrameEntity
import com.systems.notchi.data.entity.ProjectEntity
import com.systems.notchi.data.entity.RewardEntity
import com.systems.notchi.data.entity.SubTaskEntity
import com.systems.notchi.data.entity.TaskEntity
import com.systems.notchi.data.entity.TaskProjectEntity
import com.systems.notchi.domain.model.DailyPointsSummaryModel
import com.systems.notchi.domain.model.FrameModel
import com.systems.notchi.domain.model.ProjectModel
import com.systems.notchi.domain.model.RewardModel
import com.systems.notchi.domain.model.SubTaskModel
import com.systems.notchi.domain.model.TaskModel
import com.systems.notchi.domain.model.TaskProjectModel

interface RemoteMapper {

    fun toTaskEntity(
        model: TaskModel
    ): TaskEntity

    fun toRewardEntity(
        model: RewardModel
    ): RewardEntity

    fun toRewardModel(
        entity: RewardEntity
    ): RewardModel

    fun toSubTasksModel(
        subTaskEntity: List<SubTaskEntity>
    ): List<SubTaskModel>

    fun toSubTasksEntity(
        subTaskModel: List<SubTaskModel>
    ): List<SubTaskEntity>

    fun toTaskDomainModel(
        entity: TaskEntity
    ): TaskModel

    fun toFrameDomainModel(
        entity: FrameEntity
    ): FrameModel

    fun toFrameEntity(
        model:  FrameModel
    ): FrameEntity

    fun toProjectDomainModel(
        project: ProjectEntity?
    ): ProjectModel

    fun toProjectEntity(
        project: ProjectModel
    ): ProjectEntity

    fun toTaskProjectEntity(project: TaskProjectModel?): TaskProjectEntity

    fun toTaskProjectDomainModel(entity: TaskProjectEntity?): TaskProjectModel

    fun toDailyPointsSummaryModel(entity: DailyPointsSummaryEntity?): DailyPointsSummaryModel

}
