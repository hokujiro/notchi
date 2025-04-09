package com.example.madetoliveapp.presentation.projects.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.madetoliveapp.domain.model.ProjectModel
import com.example.madetoliveapp.domain.model.TaskModel
import com.example.madetoliveapp.domain.model.TaskProjectModel
import com.example.madetoliveapp.presentation.projects.uimodel.ProjectUiModel
import com.example.madetoliveapp.presentation.tasks.components.IconPicker

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateProjectScreen(
    onCancel: () -> Unit,
    onSave: (ProjectUiModel) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var icon by remember { mutableStateOf("📁") }
    var tasks by remember { mutableStateOf(listOf<TaskModel>()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("New Project") },
                navigationIcon = {
                    IconButton(onClick = onCancel) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    TextButton(onClick = {
                        val newProject =
                            ProjectUiModel(
                                title = title,
                                color = "0xFF3B5F6B",
                                tasksList = tasks,
                                icon = icon
                            )
                        onSave(newProject)
                    }) {
                        Text("Save")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Project Title") },
                modifier = Modifier.fillMaxWidth()
            )

            IconPicker(selectedIcon = icon, onIconSelected = { icon = it })

            Text("Tasks", style = MaterialTheme.typography.titleMedium)

            tasks.forEachIndexed { index, task ->
                TaskInputItem(
                    task = task,
                    onTaskChange = { updated ->
                        tasks = tasks.toMutableList().also { it[index] = updated }
                    },
                    onRemove = {
                        tasks = tasks.toMutableList().also { it.removeAt(index) }
                    }
                )
            }

            Button(
                onClick = {
                    tasks = tasks + TaskModel(
                        title = "",
                        checked = false,
                        date = null,
                        points = 10,
                                project = TaskProjectModel()
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Add Task")
            }
        }
    }
}

@Composable
fun TaskInputItem(
    task: TaskModel,
    onTaskChange: (TaskModel) -> Unit,
    onRemove: () -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        OutlinedTextField(
            value = task.title,
            onValueChange = { onTaskChange(task.copy(title = it)) },
            label = { Text("Task") },
            modifier = Modifier.weight(1f)
        )
        IconButton(onClick = onRemove) {
            Icon(Icons.Default.Delete, contentDescription = "Remove Task")
        }
    }
}
