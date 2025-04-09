package com.example.madetoliveapp.domain.usecase.projects

import com.example.madetoliveapp.data.repository.projects.ProjectRepository
import com.example.madetoliveapp.data.repository.tasks.TaskRepository
import com.example.madetoliveapp.domain.model.ProjectModel

class GetProjectByIdUseCase (private val projectRepository: ProjectRepository) {
    suspend fun execute(projectId: String): ProjectModel {
        return projectRepository.getProjectById(projectId)
    }
}