package com.systems.notchi.data.source.remote.api

import com.systems.notchi.data.entity.RewardEntity
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface RewardApi {

     @GET("api/rewards/all")
     suspend fun getAllRewards(): Response<List<RewardEntity>>

     @POST("api/rewards/add")
     suspend fun addReward(@Body reward: RewardEntity): Response<Unit>

     @POST("api/rewards/redeem/{id}")
     suspend fun redeemReward(@Path("id") id: String): Response<Unit>

     @POST("api/rewards/delete/{id}")
     suspend fun deleteReward(@Path("id") id: String): Response<Unit>

}