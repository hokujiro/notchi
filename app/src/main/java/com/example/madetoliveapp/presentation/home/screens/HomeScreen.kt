package com.example.madetoliveapp.presentation.home.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.madetoliveapp.data.entity.TaskEntity
import com.example.madetoliveapp.presentation.TaskViewModel
import org.koin.androidx.compose.koinViewModel
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import com.example.madetoliveapp.domain.model.TaskModel
import com.example.madetoliveapp.presentation.components.BottomNavigationBar
import java.text.SimpleDateFormat
import java.util.Date



@Composable
fun HomeScreen(taskViewModel: TaskViewModel = koinViewModel()) { Scaffold(
    bottomBar = { BottomNavigationBar(selectedRoute = "home") },
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