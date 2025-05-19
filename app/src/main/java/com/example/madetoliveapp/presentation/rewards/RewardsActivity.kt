package com.example.madetoliveapp.presentation.rewards

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.madetoliveapp.presentation.rewards.screens.RewardListDetailScreen
import com.example.madetoliveapp.presentation.rewards.screens.RewardsScreen
import com.example.madetoliveapp.presentation.theme.MadeToLiveTheme

class RewardsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MadeToLiveTheme {
                val navController = rememberNavController()

                NavHost(navController, startDestination = "rewards_list") {
                    composable("rewards_list") {
                        RewardsScreen(navController)
                    }
                    composable(
                        route = "reward_list_detail/{rewardId}",
                        arguments = listOf(navArgument("rewardId") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val rewardListId = backStackEntry.arguments?.getString("rewardListId")
                        if (rewardListId != null) {
                            RewardListDetailScreen(rewardListId, navController = navController)
                        }
                    }
                }
            }
        }
    }
}