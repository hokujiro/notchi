package com.example.madetoliveapp.domain.usecase.projects

import com.example.madetoliveapp.data.repository.projects.ProjectRepository
import com.example.madetoliveapp.data.repository.tasks.TaskRepository
import com.example.madetoliveapp.domain.model.ProjectModel
import com.example.madetoliveapp.domain.model.TaskModel

class UpdateProjectUseCase(private val projectRepository: ProjectRepository) {
    suspend fun execute(project: ProjectModel) {
        projectRepository.updateProject(project)
    }
}