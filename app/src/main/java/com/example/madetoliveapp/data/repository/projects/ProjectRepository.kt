package com.example.madetoliveapp.data.repository.projects

import com.example.madetoliveapp.domain.model.DailyPointsSummaryModel
import com.example.madetoliveapp.domain.model.ProjectModel
import com.example.madetoliveapp.domain.model.TaskModel

interface ProjectRepository {
    suspend fun addProject(project: ProjectModel) // Add a project
    suspend fun getAllProjects(): List<ProjectModel>
    suspend fun getProjectById(projectId: String): ProjectModel
    suspend fun deleteProject(projectId: String)
    suspend fun updateProject(project: ProjectModel)
}