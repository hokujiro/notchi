package com.example.madetoliveapp.presentation.projects.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.madetoliveapp.presentation.tasks.TaskViewModel
import org.koin.androidx.compose.koinViewModel
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import com.example.madetoliveapp.domain.model.ProjectModel
import com.example.madetoliveapp.presentation.components.BottomNavigationBar


@Composable
fun ProjectsListScreen(taskViewModel: TaskViewModel = koinViewModel()) {
    // Obtener los proyectos desde el ViewModel
    val projects by taskViewModel.projects.collectAsState()

    LaunchedEffect(Unit) {
       taskViewModel.getAllProjects()
    }
    // Mostrar la lista de proyectos
    Scaffold(
        bottomBar = { BottomNavigationBar(selectedRoute = "projects") },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    val newProject = ProjectModel(
                        title = "Nuevo project",
                        tasksList = listOf(),
                        color = "Color"
                    )
                    taskViewModel.addProject(newProject)
                },
                modifier = Modifier.padding(16.dp) // Adjust padding to prevent overlap
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add project")
            }
        }
    ) { paddingValues ->
        ProjectsList(
            projects = projects,
            onTaskClick = taskViewModel::toggleTaskCompletion,
            modifier = Modifier.padding(paddingValues) // Apply padding from Scaffold
        )
    }
}

@Composable
fun ProjectsList(projects: List<ProjectModel>, onTaskClick: (String) -> Unit, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier.padding(bottom = 56.dp)
            .padding(WindowInsets.systemBars.asPaddingValues()) // Account for system bars
    ) {
        items(projects) { project ->
            ProjectItem(project, onTaskClick)
        }
    }
}

@Composable
fun ProjectItem(project: ProjectModel, onProjectClick: (String) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onProjectClick(project.uid) },
        elevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = project.title,
                style = MaterialTheme.typography.body1,
            )
        }
    }
}