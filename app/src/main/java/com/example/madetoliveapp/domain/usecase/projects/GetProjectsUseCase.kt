package com.example.madetoliveapp.domain.usecase.projects

import com.example.madetoliveapp.data.repository.projects.ProjectRepository
import com.example.madetoliveapp.data.repository.tasks.TaskRepository
import com.example.madetoliveapp.domain.model.ProjectModel

class GetProjectsUseCase (private val projectRepository: ProjectRepository) {
    suspend fun execute(): List<ProjectModel> {
        return projectRepository.getAllProjects()
    }
}