package com.example.madetoliveapp.presentation.rewards

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.madetoliveapp.presentation.projects.ProjectViewModel
import com.example.madetoliveapp.presentation.projects.screens.CreateProjectScreen
import com.example.madetoliveapp.presentation.projects.screens.ProjectDetailScreen
import com.example.madetoliveapp.presentation.projects.screens.ProjectsListScreen
import com.example.madetoliveapp.presentation.rewards.screens.RewardsScreen
import com.example.madetoliveapp.presentation.theme.MadeToLiveTheme
import org.koin.androidx.viewmodel.ext.android.getViewModel

class RewardsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MadeToLiveTheme {
                val navController = rememberNavController()

                NavHost(navController, startDestination = "projects_list") {
                    composable("rewards_list") {
                        RewardsScreen(navController)
                    }
                    composable(
                        route = "reward_detail/{rewardId}",
                        arguments = listOf(navArgument("rewardId") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val projectId = backStackEntry.arguments?.getString("rewardId")
                        if (projectId != null) {
                            RewardDetailScreen(projectId, navController = navController)
                        }
                    }
                }
            }
        }
    }
}