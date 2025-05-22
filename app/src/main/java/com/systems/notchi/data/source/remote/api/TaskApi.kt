package com.systems.notchi.data.source.remote.api

import com.systems.notchi.data.entity.DailyPointsSummaryEntity
import com.systems.notchi.data.entity.FrameEntity
import retrofit2.http.Query
import com.systems.notchi.data.entity.TaskEntity
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.HTTP
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

    @POST("api/tasks/add-list")
    suspend fun addTaskList(@Body frames: List<TaskEntity>): Response<List<TaskEntity>>

    @HTTP(method = "DELETE", path = "api/tasks/delete-list", hasBody = true)
    suspend fun deleteTaskList(@Body taskIds: List<String>): Response<Unit>

    @GET("api/tasks/points-summary")
    suspend fun getPointsForDay(
        @Query("date") date: Long
    ): Response<DailyPointsSummaryEntity>

    @GET("api/frames/all")
    suspend fun getAllFrames(): Response<List<FrameEntity>>

    @POST("api/frames/add")
    suspend fun addFrame(@Body frame: FrameEntity): Response<Unit>

    @POST("api/frames/add-list")
    suspend fun addFrameList(@Body frames: List<FrameEntity>): Response<List<FrameEntity>>

    @DELETE("api/frames/delete/{id}")
    suspend fun deleteFrame(@Path("id") id: String): Response<Unit>

    @HTTP(method = "DELETE", path = "api/frames/delete-list", hasBody = true)
    suspend fun deleteFrameList(@Body frameIds: List<String>): Response<Unit>

}
