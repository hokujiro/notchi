package com.example.madetoliveapp.presentation.tasks.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.madetoliveapp.presentation.tasks.TaskViewModel
import com.example.madetoliveapp.presentation.tasks.components.AddTaskDialog
import com.example.madetoliveapp.presentation.components.BottomNavigationBar
import com.example.madetoliveapp.presentation.tasks.components.CalendarHeader
import com.example.madetoliveapp.presentation.tasks.components.TaskComponent
import com.example.madetoliveapp.presentation.tasks.components.CircularFloatingMenu
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDate
import java.time.ZoneId

@Composable
fun TasksScreen(taskViewModel: TaskViewModel = koinViewModel()) {

    var selectedDate by remember {
        mutableLongStateOf(
            LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        )
    }
    // Obtener las tareas desde el ViewModel
    val tasks by taskViewModel.tasks.collectAsState()
    val projects by taskViewModel.projects.collectAsState()
    val totalPoints by taskViewModel.totalPoints.collectAsState() // Make sure this exists!
    var openCreateTaskDialog = remember { mutableStateOf(false) } // State to control dialog visibility
    var isFabExpanded by remember { mutableStateOf(false) }


    LaunchedEffect(Unit) {
        taskViewModel.getTasksForDay(selectedDate)
    }
    LaunchedEffect(Unit) {
        taskViewModel.loadUserPoints()
    }

    Scaffold(
        bottomBar = { BottomNavigationBar(selectedRoute = "daily") },
        floatingActionButton = {
            CircularFloatingMenu(
                isExpanded = isFabExpanded,
                onToggle = { isFabExpanded = !isFabExpanded },
                onActionClick = { index ->
                    isFabExpanded = false
                    when (index) {
                        0 -> { openCreateTaskDialog.value = true} // Add Task
                        1 -> {/* handle add project */ }
                        2 -> {/* handle add habit */ }
                    }
                }
            )
        }
    ) { paddingValues ->
        val outerScrollState = rememberScrollState()
        if (openCreateTaskDialog.value) {
            AddTaskDialog(
                onDismiss = { openCreateTaskDialog.value = false },
                onAddTask = { newTask ->
                    taskViewModel.addTask(newTask)
                    openCreateTaskDialog.value = false
                },
                selectedDate = selectedDate,
                projects = projects
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(outerScrollState)

        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Profile Icon
                IconButton(
                    onClick = { /* TODO: Navigate to profile or show menu */ },
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "Profile",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Text(
                    text = "⭐ $totalPoints points",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 18.sp
                    )
                )
            }

            CalendarHeader(
                modifier = Modifier.fillMaxWidth(),
                selectedDate = selectedDate,
                onDateSelected = { date ->
                    selectedDate = date
                    taskViewModel.getTasksForDay(date) // Fetch tasks for the selected date
                }
            )

            TaskComponent(
                tasks = tasks,
                onTaskClick = taskViewModel::toggleTaskCompletion,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 500.dp)
            )
        }
    }
}





