package com.systems.notchi.data.repository.rewards

import com.systems.notchi.domain.model.RewardModel

interface RewardRepository {
    suspend fun getAllRewards(): List<RewardModel> // Fetch all rewards
    suspend fun getRewardsForDay(date: Long): List<RewardModel> // Fetch all rewards
    suspend fun addReward(reward: RewardModel) // Insert multiple rewards
    suspend fun redeemReward(rewardId: String) // Redeem rewards
    suspend fun deleteReward(id: String) // Delete a reward
    suspend fun updateReward(reward: RewardModel) // Update a reward
}