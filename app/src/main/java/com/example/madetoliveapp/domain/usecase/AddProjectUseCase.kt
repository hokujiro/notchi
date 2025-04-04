package com.example.madetoliveapp.domain.usecase

import com.example.madetoliveapp.data.entity.TaskEntity
import com.example.madetoliveapp.data.repository.TaskRepository
import com.example.madetoliveapp.domain.model.ProjectModel
import com.example.madetoliveapp.domain.model.TaskModel

class AddProjectUseCase(private val taskRepository: TaskRepository) {
    suspend fun execute(project: ProjectModel) {
       taskRepository.addProject(project)
    }
}