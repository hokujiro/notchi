package com.systems.notchi.presentation.rewards.screens

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.systems.notchi.presentation.projects.ProjectViewModel
import com.systems.notchi.presentation.rewards.RewardsViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RewardListDetailScreen(
    rewardListId: String,
    navController: NavController,
    taskViewModel: RewardsViewModel = koinViewModel(),
    projectViewModel: ProjectViewModel = koinViewModel()
) {


}