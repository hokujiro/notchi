package com.example.madetoliveapp.domain.usecase.tasks

import com.example.madetoliveapp.data.repository.tasks.TaskRepository
import com.example.madetoliveapp.domain.model.TaskModel

class AddTaskListUseCase(private val taskRepository: TaskRepository) {
    suspend fun execute(taskList: List<TaskModel>) {
        taskRepository.addTaskList(taskList)
    }
}