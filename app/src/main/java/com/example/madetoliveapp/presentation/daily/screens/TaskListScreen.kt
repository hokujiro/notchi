package com.example.madetoliveapp.presentation.daily.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
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
fun TaskListScreen(taskViewModel: TaskViewModel = koinViewModel()) {
    // Obtener las tareas desde el ViewModel
    val tasks by taskViewModel.tasks.collectAsState()
    val dateFormat = SimpleDateFormat("d-MM-yyyy")

    LaunchedEffect(Unit) {
       taskViewModel.getAllTasks()
    }
    // Mostrar la lista de tareas
    Scaffold(
        bottomBar = { BottomNavigationBar(selectedRoute = "tasks") },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    val parsedDate: Date = dateFormat.parse("2-10-2013") as Date
                    val newTask = TaskModel(
                        title = "Nueva tarea",
                        checked = false,
                        subTasks = listOf(),
                        category = null,
                        date = parsedDate,
                        points = null
                    )
                    taskViewModel.addTask(newTask)
                },
                modifier = Modifier.padding(16.dp) // Adjust padding to prevent overlap
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar tarea")
            }
        }
    ) { paddingValues ->
        TaskList(
            tasks = tasks,
            onTaskClick = taskViewModel::toggleTaskCompletion,
            modifier = Modifier.padding(paddingValues) // Apply padding from Scaffold
        )
    }
}

@Composable
fun TaskList(tasks: List<TaskModel>, onTaskClick: (String) -> Unit, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier.padding(bottom = 56.dp)
            .padding(WindowInsets.systemBars.asPaddingValues()) // Account for system bars
    ) {
        items(tasks) { task ->
            TaskItem(task, onTaskClick)
        }
    }
}

@Composable
fun TaskItem(task: TaskModel, onTaskClick: (String) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onTaskClick(task.uid) },
        elevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = task.checked,
                onCheckedChange = { onTaskClick(task.uid) }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = task.title,
                style = MaterialTheme.typography.body1,
                textDecoration = if (task.checked) TextDecoration.LineThrough else null
            )
        }
    }
}