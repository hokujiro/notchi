package com.example.madetoliveapp.presentation.rewards.screens

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.madetoliveapp.presentation.projects.ProjectViewModel
import com.example.madetoliveapp.presentation.rewards.RewardsViewModel
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