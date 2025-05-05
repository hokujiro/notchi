package com.example.madetoliveapp.domain.usecase.tasks

import com.example.madetoliveapp.data.repository.frames.FrameRepository
import com.example.madetoliveapp.data.repository.tasks.TaskRepository
import com.example.madetoliveapp.domain.model.FrameModel
import com.example.madetoliveapp.domain.model.TaskModel

class DeleteTaskListUseCase (private val taskRepository: TaskRepository) {
    suspend fun execute(taskList: List<TaskModel>) {
        taskRepository.deleteTaskList(taskList)
    }
}