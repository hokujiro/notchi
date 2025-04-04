package com.example.madetoliveapp.presentation.rewards.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.madetoliveapp.presentation.tasks.TaskViewModel
import org.koin.androidx.compose.koinViewModel
import androidx.compose.runtime.*
import com.example.madetoliveapp.presentation.components.BottomNavigationBar


@Composable
fun HomeScreen(taskViewModel: TaskViewModel = koinViewModel()) { Scaffold(
    bottomBar = { BottomNavigationBar(selectedRoute = "rewards") },
    content = { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(padding)
        )
        {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "Profile Screen", style = MaterialTheme.typography.h4)
            }
        }
    }
)
}