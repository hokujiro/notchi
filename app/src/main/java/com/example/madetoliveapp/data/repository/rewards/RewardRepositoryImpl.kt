package com.example.madetoliveapp.data.repository.rewards

import com.example.madetoliveapp.data.mapper.RemoteMapper
import com.example.madetoliveapp.data.source.remote.api.RewardApi
import com.example.madetoliveapp.data.source.remote.api.TaskApi
import com.example.madetoliveapp.domain.model.DailyPointsSummaryModel
import com.example.madetoliveapp.domain.model.FrameModel
import com.example.madetoliveapp.domain.model.RewardModel
import com.example.madetoliveapp.domain.model.TaskModel

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

    override suspend fun getRewardsForDay(date: Long): List<RewardModel> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteReward(id: String) {
        TODO("Not yet implemented")
    }

    override suspend fun updateReward(reward: RewardModel) {
        TODO("Not yet implemented")
    }

}