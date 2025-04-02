package com.example.madetoliveapp.presentation.tasks

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.madetoliveapp.presentation.tasks.screens.TasksScreen
import com.example.madetoliveapp.presentation.theme.MadeToLiveTheme

class TasksActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MadeToLiveTheme {
                TasksScreen()
            }
        }
    }
}