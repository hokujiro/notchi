package com.example.madetoliveapp.domain.usecase.rewards

import com.example.madetoliveapp.data.repository.rewards.RewardRepository
import com.example.madetoliveapp.data.repository.tasks.TaskRepository
import com.example.madetoliveapp.domain.model.RewardModel
import com.example.madetoliveapp.domain.model.TaskModel

class GetRewardsUseCase (private val rewardRepository: RewardRepository) {
    suspend fun execute(): List<RewardModel> {
        return rewardRepository.getAllRewards()
    }
}