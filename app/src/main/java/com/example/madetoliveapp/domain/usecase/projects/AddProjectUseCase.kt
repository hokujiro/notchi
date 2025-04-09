package com.example.madetoliveapp.domain.usecase.projects

import com.example.madetoliveapp.data.repository.projects.ProjectRepository
import com.example.madetoliveapp.domain.model.ProjectModel

class AddProjectUseCase(private val projectRepository: ProjectRepository) {
    suspend fun execute(project: ProjectModel) {
       projectRepository.addProject(project)
    }
}