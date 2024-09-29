package com.example.madetoliveapp.presentation.daily.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.example.madetoliveapp.data.entity.TaskEntity
import com.example.madetoliveapp.presentation.TaskViewModel
import okhttp3.internal.concurrent.Task

@Composable
fun TaskListScreen(taskViewModel: TaskViewModel = viewModel()) {
    // Obtener las tareas desde el ViewModel
    val taskList by taskViewModel.tasks.collectAsState()

    // Mostrar la lista de tareas
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Lista de Tareas") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                // Agregar una nueva tarea de ejemplo
                val newTask = TaskEntity(uid = taskList.size + 1, title = "Nueva tarea", checked = false)
                taskViewModel.addTask(newTask)
            }) {
                Icon(Icons.Default.Add, contentDescription = "Agregar tarea")
            }
        }
    ) { paddingValues ->
        TaskList(taskList, taskViewModel::toggleTaskCompletion, Modifier.padding(paddingValues))
    }
}

@Composable
fun TaskList(tasks: List<TaskEntity>, onTaskClick: (Int) -> Unit, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier) {
        items(tasks) { task ->
            TaskItem(task, onTaskClick)
        }
    }
}

@Composable
fun TaskItem(task: Task, onTaskClick: (Int) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onTaskClick(task.id) },
        elevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = task.isCompleted,
                onCheckedChange = { onTaskClick(task.id) }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = task.title,
                style = MaterialTheme.typography.body1,
                textDecoration = if (task.isCompleted) TextDecoration.LineThrough else null
            )
        }
    }
}