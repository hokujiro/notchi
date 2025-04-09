package com.example.madetoliveapp.data.source.remote.api

import com.example.madetoliveapp.data.entity.DailyPointsSummaryEntity
import com.example.madetoliveapp.data.entity.ProjectEntity
import retrofit2.http.Query
import com.example.madetoliveapp.data.entity.TaskEntity
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface TaskApi {
    // Endpoint for tasks
    @GET("api/tasks/all")
    suspend fun getAllTasks(): Response<List<TaskEntity>>

    @GET("api/tasks/by-date")
    suspend fun getTasksForDay(
        @Query("date") date: Long
    ): Response<List<TaskEntity>>

    @POST("api/tasks/add")
    suspend fun insertTask(@Body task: TaskEntity): Response<Unit>

    @PUT("api/tasks/update/{id}")
    suspend fun updateTask(@Path("id") id: String, @Body task: TaskEntity): Response<Unit>

    @DELETE("api/tasks/delete/{id}")
    suspend fun deleteTask(@Path("id") id: String): Response<Unit>

    @GET("api/tasks/points-summary")
    suspend fun getPointsForDay(
        @Query("date") date: Long
    ): Response<DailyPointsSummaryEntity>
}
