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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class RemoteMapperImpl : RemoteMapper {

    override fun toModel(
        entity: TaskEntity
    ): TaskModel =
        with(entity) {
            TaskModel(
                uid = uid,
                checked = checked,
                title = title,
                subTasks = toSubTasksModel(subTasks ?: emptyList()),
                project = toTaskProjectDomainModel(project),
                date = stringToDate(date),
                points = points

            )
        }

    override fun toEntity(model: TaskModel): TaskEntity =
        with(model) {
            TaskEntity(
                uid = uid,
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
            uid = entity.uid,
            checked = entity.checked,
            title = entity.title,
            project = toTaskProjectDomainModel(entity.project),
            subTasks = toSubTasksModel(entity.subTasks ?: emptyList()),
            date = stringToDate(entity.date),
            points = entity.points
        )
    }

    override fun toProjectDomainModel(entity: ProjectEntity?): ProjectModel {
        return ProjectModel(
            title = entity?.title ?: "",
            color = entity?.color ?: ""
        )
    }

    override fun toTaskProjectDomainModel(entity: TaskProjectEntity?): TaskProjectModel {
        return TaskProjectModel(
            title = entity?.title ?: "",
            color = entity?.color ?: ""
        )
    }

    override fun toTaskProjectEntity(project: TaskProjectModel?): TaskProjectEntity {
        return TaskProjectEntity(
            title = project?.title ?: "",
            color = project?.color ?: ""
        )
    }

    override fun toProjectEntity(project: ProjectModel): ProjectEntity {
        return ProjectEntity(
            uid = project.uid,
            title = project.title,
            tasksList = project.tasksList?.map {
                TaskEntity(
                    uid = it.uid,
                    checked = it.checked,
                    title = it.title,
                    subTasks = toSubTasksEntity(it.subTasks ?: emptyList()),
                    date = dateToString(it.date),
                    points = it.points
                )
            },
            color = project.color
        )
    }

    override fun toModel(entity: DailyPointsSummaryEntity?): DailyPointsSummaryModel {
        return DailyPointsSummaryModel(
            total = entity?.total ?: 0f,
            positive = entity?.positive?: 0f,
            negative = entity?.negative?: 0f
        )
    }
}

