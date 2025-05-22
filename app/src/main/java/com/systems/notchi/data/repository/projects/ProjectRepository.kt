package com.systems.notchi.data.repository.projects

import com.systems.notchi.domain.model.ProjectModel

interface ProjectRepository {
    suspend fun addProject(project: ProjectModel) // Add a project
    suspend fun getAllProjects(): List<ProjectModel>
    suspend fun getProjectById(projectId: String): ProjectModel
    suspend fun deleteProject(projectId: String)
    suspend fun updateProject(project: ProjectModel)
}