package com.systems.notchi.data.repository.rewards

import com.systems.notchi.data.mapper.RemoteMapper
import com.systems.notchi.data.source.remote.api.RewardApi
import com.systems.notchi.domain.model.RewardModel

class RewardRepositoryImpl(
    //private val taskDao: TaskDao,
    private val mapper: RemoteMapper,
    private val rewardApi: RewardApi
) : RewardRepository {

/*    // Fetch all tasks from the database
    override suspend fun getAllTasks(): List<TaskModel> {
        return taskDao.getAll().map { mapper.toModel(it) }
    } */

    override suspend fun getAllRewards(): List<RewardModel> {
        val response = rewardApi.getAllRewards()
        if (response.isSuccessful) {
            return response.body()?.map { mapper.toRewardModel(it) } ?: emptyList()
        } else {
            throw Exception("Failed to load tasks: ${response.errorBody()?.string()}")
        }
    }

    override suspend fun addReward(reward: RewardModel) {
        rewardApi.addReward(mapper.toRewardEntity(reward))
    }

    override suspend fun redeemReward(rewardId: String) {
        rewardApi.redeemReward(rewardId)
    }

    override suspend fun getRewardsForDay(date: Long): List<RewardModel> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteReward(id: String) {
        rewardApi.deleteReward(id)
    }

    override suspend fun updateReward(reward: RewardModel) {
        TODO("Not yet implemented")
    }

}