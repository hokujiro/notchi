package com.systems.notchi.data.source.remote.api

import retrofit2.Response
import retrofit2.http.GET

interface UserApi {

    @GET("api/user/points")
    suspend fun getUserPoints(): Response<Float>
}