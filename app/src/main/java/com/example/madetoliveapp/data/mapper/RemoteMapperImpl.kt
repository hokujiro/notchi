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
                finishingDate = stringToDate(finishingDate)?: Date(),
                points = points

            )
        }

    override fun toEntity(model: TaskModel): TaskEntity =
        with(model){
            TaskEntity(
                checked = checked,
                title = title,
                subTasks =  toSubTasksEntity(subTasks ?: emptyList()),
                category = category,
                finishingDate = dateToString(finishingDate)?: "Date",
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


    private fun stringToDate(dateString: String, format: String = "yyyy-MM-dd"): Date? {
        val dateFormat = SimpleDateFormat(format, Locale.getDefault())
        return dateFormat.parse(dateString)
    }

    private fun dateToString(date: Date, format: String = "yyyy-MM-dd"): String? {
        val dateFormatter = SimpleDateFormat(format)
           return dateFormatter.format(date)
        }
    }

