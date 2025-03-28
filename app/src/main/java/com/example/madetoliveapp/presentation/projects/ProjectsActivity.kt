package com.example.madetoliveapp.presentation.projects

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.madetoliveapp.presentation.projects.screens.TaskListScreen
import com.example.madetoliveapp.presentation.theme.AppTheme

class ProjectsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                TaskListScreen()
            }
        }
    }
}