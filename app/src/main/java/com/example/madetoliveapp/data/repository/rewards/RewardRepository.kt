package com.example.madetoliveapp.data.repository.rewards

import com.example.madetoliveapp.domain.model.DailyPointsSummaryModel
import com.example.madetoliveapp.domain.model.FrameModel
import com.example.madetoliveapp.domain.model.RewardModel

interface RewardRepository {
    suspend fun getAllRewards(): List<RewardModel> // Fetch all rewards
    suspend fun getRewardsForDay(date: Long): List<RewardModel> // Fetch all rewards
    suspend fun addReward(reward: RewardModel) // Insert multiple rewards
    suspend fun deleteReward(id: String) // Delete a reward
    suspend fun updateReward(reward: RewardModel) // Update a reward
}