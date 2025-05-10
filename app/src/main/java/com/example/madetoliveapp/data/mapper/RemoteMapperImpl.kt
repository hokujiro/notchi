package com.example.madetoliveapp.data.mapper

import com.example.madetoliveapp.data.entity.DailyPointsSummaryEntity
import com.example.madetoliveapp.data.entity.FrameEntity
import com.example.madetoliveapp.data.entity.ProjectEntity
import com.example.madetoliveapp.data.entity.RewardEntity
import com.example.madetoliveapp.data.entity.SubTaskEntity
import com.example.madetoliveapp.data.entity.TaskEntity
import com.example.madetoliveapp.data.entity.TaskProjectEntity
import com.example.madetoliveapp.domain.model.DailyPointsSummaryModel
import com.example.madetoliveapp.domain.model.FrameModel
import com.example.madetoliveapp.domain.model.ProjectModel
import com.example.madetoliveapp.domain.model.RewardModel
import com.example.madetoliveapp.domain.model.SubTaskModel
import com.example.madetoliveapp.domain.model.TaskModel
import com.example.madetoliveapp.domain.model.TaskProjectModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class RemoteMapperImpl : RemoteMapper {

    override fun toTaskEntity(model: TaskModel): TaskEntity =
        with(model) {
            TaskEntity(
                checked = checked,
                title = title,
                subTasks = toSubTasksEntity(subTasks ?: emptyList()),
                project = toTaskProjectEntity(project),
                date = dateToString(date),
                points = points
            )
        }


    override fun toSubTasksModel(subTaskEntity: List<SubTaskEntity>): List<SubTaskModel> =
        subTaskEntity.map {
            SubTaskModel(
                uid = it.uid,
                check = it.check,
                title = it.title
            )
        }

    override fun toSubTasksEntity(subTaskModel: List<SubTaskModel>): List<SubTaskEntity> =
        subTaskModel.map {
            SubTaskEntity(
                uid = it.uid,
                check = it.check,
                title = it.title
            )
        }


    private fun stringToDate(dateString: String?, format: String = "yyyy-MM-dd"): Date? {
        if (dateString.isNullOrBlank()) return null

        val dateFormat = SimpleDateFormat(format, Locale.getDefault())
        return try {
            dateFormat.parse(dateString)
        } catch (e: Exception) {
            null // or log and return fallback
        }
    }

    private fun dateToString(date: Date?, format: String = "yyyy-MM-dd"): String? {
        if (date == null) return null

        val dateFormatter = SimpleDateFormat(format, Locale.getDefault())
        return try {
            dateFormatter.format(date)
        } catch (e: Exception) {
            null // or log the issue
        }
    }

    override fun toTaskDomainModel(entity: TaskEntity): TaskModel {
        return TaskModel(
            uid = entity.uid.toString(),
            checked = entity.checked,
            title = entity.title,
            project = toTaskProjectDomainModel(entity.project),
            subTasks = toSubTasksModel(entity.subTasks ?: emptyList()),
            date = stringToDate(entity.date),
            points = entity.points
        )
    }

    override fun toFrameDomainModel(entity: FrameEntity): FrameModel {
        return FrameModel(
            uid = entity.uid.toString(),
            title =  entity.title,
            project = toTaskProjectDomainModel(entity.project),
            points = entity.points
        )
    }

    override fun toFrameEntity(model: FrameModel): FrameEntity {
        return FrameEntity(
            title = model.title,
            project = toTaskProjectEntity(model.project),
            points = model.points
        )
    }

    override fun toProjectDomainModel(project: ProjectEntity?): ProjectModel {
        return ProjectModel(
            uid = project?.uid.toString(),
            title = project?.title ?: "",
            subtitle = project?.subtitle ?: "",
            tasksList = project?.tasks?.map { toTaskDomainModel(it) },
            color = project?.color ?: "",
            icon = project?.icon ?: ""
        )
    }

    override fun toTaskProjectDomainModel(entity: TaskProjectEntity?): TaskProjectModel {
        return TaskProjectModel(
            id = entity?.id ?: "",
            title = entity?.title ?: "",
            color = entity?.color ?: "",
            icon = entity?.icon ?: ""
        )
    }

    override fun toTaskProjectEntity(project: TaskProjectModel?): TaskProjectEntity {
        return TaskProjectEntity(
            id = project?.id ?: "",
            title = project?.title ?: "",
            color = project?.color ?: "",
            icon = project?.icon ?: ""
        )
    }

    override fun toProjectEntity(project: ProjectModel): ProjectEntity {
        return ProjectEntity(
            title = project.title,
            subtitle = project.subtitle,
            tasks = project.tasksList?.map {
                TaskEntity(
                    checked = it.checked,
                    title = it.title,
                    subTasks = toSubTasksEntity(it.subTasks ?: emptyList()),
                    date = dateToString(it.date),
                    points = it.points
                )
            },
            color = project.color,
            icon = project.icon
        )
    }

    override fun toDailyPointsSummaryModel(entity: DailyPointsSummaryEntity?): DailyPointsSummaryModel {
        return DailyPointsSummaryModel(
            total = entity?.total ?: 0f,
            positive = entity?.positive?: 0f,
            negative = entity?.negative?: 0f
        )
    }

    override fun toRewardEntity(model: RewardModel): RewardEntity =
        with(model) {
            RewardEntity(
                title = title,
                project = toTaskProjectEntity(project),
                points = points?: 0,
                icon = icon
            )
        }

    override fun toRewardModel(entity: RewardEntity): RewardModel {
        return RewardModel(
            id = entity.id.toString(),
            title = entity.title,
            project = toTaskProjectDomainModel(entity.project),
            points = entity.points,
            icon = entity.icon
        )
    }
}

