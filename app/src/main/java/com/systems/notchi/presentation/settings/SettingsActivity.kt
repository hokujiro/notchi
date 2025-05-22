package com.systems.notchi.presentation.settings

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.systems.notchi.presentation.settings.screens.SettingsScreen
import com.systems.notchi.presentation.theme.MadeToLiveTheme

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