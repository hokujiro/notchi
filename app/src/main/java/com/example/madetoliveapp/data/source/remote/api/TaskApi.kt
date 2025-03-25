package com.example.madetoliveapp.data.source.remote.api

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

    // Endpoint for user login
    @GET("tasks")
    fun getAllTasks(): Response<List<TaskEntity>>

    @GET("tasks/by-date")
    suspend fun getTasksForDay(@Query("date") date: Long): Response<List<TaskEntity>>

    @POST("tasks")
    suspend fun insertTask(@Body task: TaskEntity): Response<Unit>

    @DELETE("tasks/{id}")
    suspend fun deleteTask(@Path("id") id: String): Response<Unit>

    @PUT("tasks/{id}")
    suspend fun updateTask(@Path("id") id: String, @Body task: TaskEntity): Response<Unit>
}