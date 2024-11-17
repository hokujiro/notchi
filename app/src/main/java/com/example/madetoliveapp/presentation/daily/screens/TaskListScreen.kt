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
import com.example.madetoliveapp.data.entity.TaskEntity
import com.example.madetoliveapp.presentation.TaskViewModel
import org.koin.androidx.compose.koinViewModel
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import com.example.madetoliveapp.domain.model.TaskModel
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
        topBar = {
            TopAppBar(title = { Text("Lista de Tareas") })
        },
        floatingActionButton = {
            val parsedDate: Date = dateFormat.parse("2-10-2013") as Date
            FloatingActionButton(onClick = {
                val newTask = TaskModel(
                    title = "Nueva tarea",
                    checked = false,
                    subTasks = listOf(),
                    category = null,
                    finishingDate = parsedDate,
                    points = null
                )
                taskViewModel.addTask(newTask)
            }) {
                Icon(Icons.Default.Add, contentDescription = "Agregar tarea")
            }
        }
    ) { paddingValues ->
        TaskList(tasks, taskViewModel::toggleTaskCompletion, Modifier.padding(paddingValues))
    }
}

@Composable
fun TaskList(tasks: List<TaskModel>, onTaskClick: (Int) -> Unit, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier) {
        items(tasks) { task ->
            TaskItem(task, onTaskClick)
        }
    }
}

@Composable
fun TaskItem(task: TaskModel, onTaskClick: (Int) -> Unit) {
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