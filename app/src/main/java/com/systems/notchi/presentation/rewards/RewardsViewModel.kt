package com.systems.notchi.presentation.rewards

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.util.Log
import android.view.Gravity
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.systems.notchi.domain.model.BundleModel
import com.systems.notchi.domain.model.RewardModel
import com.systems.notchi.domain.usecase.points.GetUserPointsUseCase
import com.systems.notchi.domain.usecase.rewards.AddRewardUseCase
import com.systems.notchi.domain.usecase.rewards.GetRewardsUseCase
import com.systems.notchi.domain.usecase.rewards.RedeemRewardUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.systems.notchi.domain.usecase.rewards.DeleteRewardUseCase

class RewardsViewModel(
    private val addRewardUseCase: AddRewardUseCase,
    private val getAllRewardsUseCase: GetRewardsUseCase,
    private val redeemRewardUseCase: RedeemRewardUseCase,
    private val getUserPointsUseCase: GetUserPointsUseCase,
    private val deleteRewardUseCase: DeleteRewardUseCase,
) : ViewModel() {

    private val _rewards = MutableStateFlow<List<RewardModel>>(emptyList())
    val rewards: StateFlow<List<RewardModel>> = _rewards.asStateFlow()

    private val _bundles = MutableStateFlow<List<BundleModel>>(emptyList())
    val bundles: StateFlow<List<BundleModel>> = _bundles.asStateFlow()

    private val _totalPoints = MutableStateFlow(0)
    val totalPoints: StateFlow<Int> = _totalPoints

    suspend fun getAllRewards() {
        try {
            val result = getAllRewardsUseCase.execute()
            _rewards.value = result
        } catch (e: Exception) {
            Log.e("TaskViewModel", "Error loading rewards", e)
        }
    }

    suspend fun getUserTotalPoints(){
        val points = getUserPointsUseCase.execute()
        _totalPoints.value = points.toInt()
    }

    suspend fun redeemReward(reward: RewardModel) {
        redeemRewardUseCase.execute(reward)
        getUserTotalPoints()
    }

    suspend fun deleteReward(reward: RewardModel) {
        redeemRewardUseCase.execute(reward)
        _rewards.value -= reward
        getAllRewards()
    }

    suspend fun addReward(reward: RewardModel) {
        addRewardUseCase.execute(reward)
        _rewards.value += reward
        getAllRewards()
    }



}