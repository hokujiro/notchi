package com.systems.notchi.domain.usecase.projects

import com.systems.notchi.data.repository.projects.ProjectRepository
import com.systems.notchi.domain.model.ProjectModel

class GetProjectByIdUseCase (private val projectRepository: ProjectRepository) {
    suspend fun execute(projectId: String): ProjectModel {
        return projectRepository.getProjectById(projectId)
    }
}