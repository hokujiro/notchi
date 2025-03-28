package com.example.madetoliveapp.data.mapper

import com.example.madetoliveapp.data.entity.SubTaskEntity
import com.example.madetoliveapp.data.entity.TaskEntity
import com.example.madetoliveapp.domain.model.SubTaskModel
import com.example.madetoliveapp.domain.model.TaskModel
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
                category = category,
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
                category = category,
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

    override fun toDomainModel(entity: TaskEntity): TaskModel {
        return TaskModel(
            uid = entity.uid,
            checked = entity.checked,
            title = entity.title,
            category = entity.category,
            subTasks = toSubTasksModel(entity.subTasks ?: emptyList()),
            date = stringToDate(entity.date),
            points = entity.points
        )
    }
}

