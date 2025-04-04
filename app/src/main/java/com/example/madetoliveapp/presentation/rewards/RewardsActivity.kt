package com.example.madetoliveapp.presentation.rewards

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.madetoliveapp.presentation.rewards.screens.HomeScreen
import com.example.madetoliveapp.presentation.theme.MadeToLiveTheme

class RewardsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MadeToLiveTheme {
                HomeScreen()
            }
        }
    }
}