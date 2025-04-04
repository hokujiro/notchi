package com.example.madetoliveapp.presentation.settings

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.madetoliveapp.presentation.settings.screens.SettingsScreen
import com.example.madetoliveapp.presentation.theme.MadeToLiveTheme

class SettingsActivity  : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MadeToLiveTheme {
                SettingsScreen()
            }
        }
    }
}