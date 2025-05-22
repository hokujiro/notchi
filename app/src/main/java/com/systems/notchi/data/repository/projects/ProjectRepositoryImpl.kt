package com.systems.notchi.data.repository.projects

import com.systems.notchi.data.mapper.RemoteMapper
import com.systems.notchi.data.source.remote.api.ProjectApi
import com.systems.notchi.domain.model.ProjectModel

class ProjectRepositoryImpl(
    private val mapper: RemoteMapper,
    private val projectApi: ProjectApi
) : ProjectRepository {

    override suspend fun addProject(project: ProjectModel) {
        projectApi.addProject(mapper.toProjectEntity(project))
    }

    override suspend fun getAllProjects(): List<ProjectModel> {
        val response = projectApi.getAllProjects()
        if (response.isSuccessful) {
            return response.body()?.map { mapper.toProjectDomainModel(it) } ?: emptyList()
        } else {
            throw Exception("Failed to load projects: ${response.errorBody()?.string()}")
        }
    }

    override suspend fun getProjectById(projectId: String): ProjectModel {
        val response = projectApi.getProjectById(projectId.toLong())
        if (response.isSuccessful) {
            return mapper.toProjectDomainModel(response.body())
        } else {
            throw Exception("Failed to load project by id: ${response.errorBody()?.string()}")
        }
    }

    override suspend fun deleteProject(projectId: String) {
        projectApi.deleteProject(projectId)
    }

    override suspend fun updateProject(project: ProjectModel) {
        projectApi.updateProject(project.uid, mapper.toProjectEntity(project))
    }


}