package com.example.madetoliveapp.data.repository

import com.example.madetoliveapp.data.entity.TaskEntity
import com.example.madetoliveapp.data.mapper.RemoteMapper
import com.example.madetoliveapp.data.source.local.dao.TaskDao
import com.example.madetoliveapp.domain.model.TaskModel

class TaskRepositoryImpl(
    private val taskDao: TaskDao,
    private val mapper: RemoteMapper
) : TaskRepository {

    // Fetch all tasks from the database
    override suspend fun getAllTasks(): List<TaskModel> {
        return taskDao.getAll().map { mapper.toModel(it) }
    }

    // Fetch all tasks from the database
    override suspend fun getTasksForDay(date: Long): List<TaskModel> {
        return taskDao.getTasksForDay(date).map { mapper.toModel(it) }
    }

    override suspend fun insertTask(task: TaskModel) {
        taskDao.insert(mapper.toEntity(task))
    }

    // Delete a task from the database
    override suspend fun deleteTask(task: TaskModel) {
        taskDao.delete(mapper.toEntity(task))
    }

    override suspend fun updateTask(task: TaskModel) {
        taskDao.update(mapper.toEntity(task))
    }
}