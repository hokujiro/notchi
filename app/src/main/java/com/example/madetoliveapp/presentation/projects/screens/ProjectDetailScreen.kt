package com.example.madetoliveapp.presentation.projects.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.madetoliveapp.domain.model.TaskModel
import com.example.madetoliveapp.presentation.projects.ProjectViewModel
import com.example.madetoliveapp.presentation.projects.components.AddProjectTaskBottomSheet
import com.example.madetoliveapp.presentation.tasks.TaskViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProjectDetailScreen(
    projectId: String,
    projectViewModel: ProjectViewModel = koinViewModel(),
    taskViewModel: TaskViewModel = koinViewModel() // or hiltViewModel()
) {
    val project by projectViewModel.projectById.collectAsState()
    val taskList by taskViewModel.tasks.collectAsState()
    val filteredTasks = taskList.filter { it.project?.id == projectId }

    var openCreateTaskDialog =
        remember { mutableStateOf(false) } // State to control dialog visibility

    LaunchedEffect(Unit) {
        projectViewModel.getProjectById(projectId)
    }

    LaunchedEffect(Unit) {
        taskViewModel.getAllTasks()
    }

    if (openCreateTaskDialog.value) {
        AddProjectTaskBottomSheet(
            onDismiss = { openCreateTaskDialog.value = false },
            onAddTask = { newTask ->
                taskViewModel.addTask(newTask)
                openCreateTaskDialog.value = false
            },
            project = projectId
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Row {
            Text(
                text = project.icon,
                fontSize = 24.sp
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = project.title,
                style = MaterialTheme.typography.headlineSmall
            )
        }

        Text(
            text = project.subtitle ?: "",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Tasks",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.weight(1f)) // Pushes the IconButton to the end
            IconButton(onClick = { openCreateTaskDialog.value = true }) {
                Icon(
                    imageVector = Icons.Default.Add, // or any other icon
                    contentDescription = "Add Task"
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            Modifier
                .fillMaxWidth()
                .heightIn(max = 500.dp)
        ) {
            items(filteredTasks) { task ->
                TaskItem(task, onTaskClick = taskViewModel::toggleTaskCompletion)
            }
        }
    }
}

@Composable
fun TaskItem(task: TaskModel, onTaskClick: (String) -> Unit) {
    val backgroundColor =
        if (task.checked) Color(0xFFECE0D1) else Color(0xFFFFF8E1) // Soft browns/beige
    val textColor = MaterialTheme.colorScheme.onSurface
    val pointsTextColor =
        if (task.checked) Color(0xFF388E3C) else MaterialTheme.colorScheme.onSurface

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable {
                onTaskClick(task.uid)
            },
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = task.checked,
                onCheckedChange = { onTaskClick(task.uid) },
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colorScheme.primary,
                    uncheckedColor = MaterialTheme.colorScheme.primary,
                    checkmarkColor = Color.White
                )
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        textDecoration = if (task.checked) TextDecoration.LineThrough else null,
                        color = textColor
                    )
                )
            }

            // Points Badge
            Text(
                text = "${task.points} ⭐ ",
                style = MaterialTheme.typography.labelLarge.copy(
                    color = pointsTextColor
                )
            )
        }
    }
}
