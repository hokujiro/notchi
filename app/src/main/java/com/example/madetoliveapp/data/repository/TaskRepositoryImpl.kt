package com.example.madetoliveapp.data.repository

import com.example.madetoliveapp.data.entity.TaskEntity
import com.example.madetoliveapp.data.source.local.dao.TaskDao

class TaskRepositoryImpl(private val taskDao: TaskDao) : TaskRepository {

    // Fetch all tasks from the database
    override suspend fun getAllTasks(): List<TaskEntity> {
        return taskDao.getAll() // Calls the TaskDao's getAll() function
    }

    // Fetch tasks by a list of task IDs
    override suspend fun getTasksByIds(taskIds: IntArray): List<TaskEntity> {
        return taskDao.loadAllByIds(taskIds)
    }

    // Find a task by first and last name (title)
    override suspend fun findTaskByTitle(firstName: String, lastName: String): TaskEntity {
        return taskDao.findByTitle(firstName, lastName)
    }

    override suspend fun insertTasks(vararg tasks: TaskEntity) {
        taskDao.insertAll(*tasks)
    }

    // Delete a task from the database
    override suspend fun deleteTask(task: TaskEntity) {
        taskDao.delete(task)
    }
}