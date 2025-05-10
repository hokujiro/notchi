package com.example.madetoliveapp.data.source.remote.api

import com.example.madetoliveapp.data.entity.ProjectEntity
import com.example.madetoliveapp.data.entity.RewardEntity
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface RewardApi {

     @GET("api/projects/all")
     suspend fun getAllRewards(): Response<List<RewardEntity>>

     @POST("api/projects/add")
     suspend fun addReward(@Body reward: RewardEntity): Response<Unit>
}