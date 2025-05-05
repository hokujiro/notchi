package com.example.madetoliveapp.data.repository.tasks

import com.example.madetoliveapp.domain.model.DailyPointsSummaryModel
import com.example.madetoliveapp.domain.model.FrameModel
import com.example.madetoliveapp.domain.model.ProjectModel
import com.example.madetoliveapp.domain.model.TaskModel

interface TaskRepository {
    suspend fun getAllTasks(): List<TaskModel> // Fetch all tasks
    suspend fun getTasksForDay(date: Long): List<TaskModel> // Fetch all tasks
    suspend fun insertTask(task: TaskModel) // Insert multiple tasks
    suspend fun deleteTask(id: String) // Delete a task
    suspend fun updateTask(task: TaskModel) // Update a task
    suspend fun getPointsForDay(date: Long): DailyPointsSummaryModel
    suspend fun getAllFrames(): List<FrameModel>
    suspend fun addFrame(frame: FrameModel)
    suspend fun addTaskList(taskList: List<TaskModel>)
    suspend fun deleteTaskList(taskList: List<TaskModel>)

}