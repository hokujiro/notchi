package com.systems.notchi.presentation.rewards

import android.util.Log
import androidx.lifecycle.ViewModel
import com.systems.notchi.domain.model.BundleModel
import com.systems.notchi.domain.model.RewardModel
import com.systems.notchi.domain.usecase.rewards.AddRewardUseCase
import com.systems.notchi.domain.usecase.rewards.GetRewardsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class RewardsViewModel(
    private val addRewardUseCase: AddRewardUseCase,
    private val getAllRewardsUseCase: GetRewardsUseCase,
) : ViewModel() {

    private val _rewards = MutableStateFlow<List<RewardModel>>(emptyList())
    val rewards: StateFlow<List<RewardModel>> = _rewards.asStateFlow()

    private val _bundles = MutableStateFlow<List<BundleModel>>(emptyList())
    val bundles: StateFlow<List<BundleModel>> = _bundles.asStateFlow()

    suspend fun getAllRewards() {
        try {
            val result = getAllRewardsUseCase.execute()
            _rewards.value = result
        } catch (e: Exception) {
            Log.e("TaskViewModel", "Error loading rewards", e)
        }
    }

    suspend fun redeemReward(reward: RewardModel) {
        try {
            val result = getAllRewardsUseCase.execute()
            _rewards.value = result
        } catch (e: Exception) {
            Log.e("TaskViewModel", "Error loading rewards", e)
        }
    }

    suspend fun addReward(reward: RewardModel) {
        addRewardUseCase.execute(reward)
        _rewards.value += reward
    }
}