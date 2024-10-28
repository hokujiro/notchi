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
    /*
        // Fetch tasks by a list of task IDs
        override suspend fun getTasksByIds(taskIds: IntArray): List<TaskEntity> {
            return taskDao.loadAllByIds(taskIds)
        }

        // Find a task by first and last name (title)
        override suspend fun findTaskByTitle(firstName: String, lastName: String): TaskEntity {
            return taskDao.findByTitle(firstName, lastName)
        }*/

    override suspend fun insertTasks(vararg tasks: TaskEntity) {
        taskDao.insertAll(*tasks)
    }

    // Delete a task from the database
    override suspend fun deleteTask(task: TaskEntity) {
        taskDao.delete(task)
    }
}