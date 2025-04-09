package com.example.madetoliveapp.domain.usecase

import com.example.madetoliveapp.data.entity.TaskEntity
import com.example.madetoliveapp.data.repository.TaskRepository
import com.example.madetoliveapp.domain.model.ProjectModel
import com.example.madetoliveapp.domain.model.TaskModel
import java.util.Date

class GetProjectByIdUseCase (private val taskRepository: TaskRepository) {
    suspend fun execute(projectId: String): ProjectModel {
        return taskRepository.getProjectById(projectId)
    }
}