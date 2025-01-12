package com.example.madetoliveapp.presentation.rewards

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.madetoliveapp.presentation.rewards.screens.RewardsScreen

import com.example.madetoliveapp.presentation.theme.AppTheme

class RewardsActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                RewardsScreen()
            }
        }
    }
}