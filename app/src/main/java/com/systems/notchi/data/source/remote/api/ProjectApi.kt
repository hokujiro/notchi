package com.systems.notchi.data.source.remote.api

import com.systems.notchi.data.entity.ProjectEntity
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ProjectApi {

    @GET("api/projects/all")
    suspend fun getAllProjects(): Response<List<ProjectEntity>>

    @POST("api/projects/add")
    suspend fun addProject(@Body project: ProjectEntity): Response<Unit>

    @GET("api/projects/by-id")
    suspend fun getProjectById(
        @Query("id") id: Long
    ): Response<ProjectEntity>

    @DELETE("api/projects/delete/{id}")
    suspend fun deleteProject(@Path("id") id: String): Response<Unit>

    @PUT("api/projects/update/{id}")
    suspend fun updateProject(@Path("id") id: String, @Body task: ProjectEntity): Response<Unit>


}