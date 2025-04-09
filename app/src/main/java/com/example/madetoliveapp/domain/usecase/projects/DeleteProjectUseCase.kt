package com.example.madetoliveapp.domain.usecase.projects

import com.example.madetoliveapp.data.repository.projects.ProjectRepository
import com.example.madetoliveapp.data.repository.tasks.TaskRepository

class DeleteProjectUseCase (private val projectRepository: ProjectRepository) {
    suspend fun execute(projectId: String) {
        projectRepository.deleteProject(projectId)
    }
}