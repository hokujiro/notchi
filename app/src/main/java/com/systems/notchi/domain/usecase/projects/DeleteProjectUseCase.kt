package com.systems.notchi.domain.usecase.projects

import com.systems.notchi.data.repository.projects.ProjectRepository

class DeleteProjectUseCase (private val projectRepository: ProjectRepository) {
    suspend fun execute(projectId: String) {
        projectRepository.deleteProject(projectId)
    }
}