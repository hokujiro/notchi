package com.example.madetoliveapp.presentation.daily

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.madetoliveapp.presentation.daily.screens.TaskListScreen
import com.example.madetoliveapp.presentation.theme.AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                TaskListScreen()
            }
        }
    }
}