package com.systems.notchi.data.source.remote.api

import com.systems.notchi.data.entity.UserEntity
import com.systems.notchi.data.repository.user.UpdateUserRequest
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part

interface UserApi {

    @GET("api/user/points")
    suspend fun getUserPoints(): Response<Float>

    @GET("api/user/me")
    suspend fun getCurrentUser(): Response<UserEntity>

    @PUT("api/user/update")
    suspend fun updateUserProfile(@Body request: UpdateUserRequest): Response<Unit>

    @Multipart
    @POST("api/user/upload-photo")
    suspend fun uploadProfilePhoto(
        @Part file: MultipartBody.Part
    ): Response<String>
}