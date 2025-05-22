package com.systems.notchi.domain.usecase.projects

import com.systems.notchi.data.repository.projects.ProjectRepository
import com.systems.notchi.domain.model.ProjectModel

class UpdateProjectUseCase(private val projectRepository: ProjectRepository) {
    suspend fun execute(project: ProjectModel) {
        projectRepository.updateProject(project)
    }
}