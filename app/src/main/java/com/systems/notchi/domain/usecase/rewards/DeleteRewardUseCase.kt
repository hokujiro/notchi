package com.systems.notchi.domain.usecase.rewards

import com.systems.notchi.data.repository.rewards.RewardRepository
import com.systems.notchi.domain.model.RewardModel

class DeleteRewardUseCase(private val rewardRepository: RewardRepository) {
    suspend fun execute(reward: RewardModel) {
        rewardRepository.redeemReward(reward.id)
    }
}