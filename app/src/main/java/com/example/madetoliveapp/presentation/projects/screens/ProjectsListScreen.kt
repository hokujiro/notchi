package com.example.madetoliveapp.presentation.projects.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.madetoliveapp.presentation.tasks.TaskViewModel
import org.koin.androidx.compose.koinViewModel
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material3.Card
import androidx.compose.material3.CardElevation
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.madetoliveapp.domain.model.ProjectModel
import com.example.madetoliveapp.presentation.components.BottomNavigationBar
import com.example.madetoliveapp.presentation.projects.uimodel.ProjectUiModel


@Composable
fun ProjectsListScreen(
    navController: NavController,
    taskViewModel: TaskViewModel = koinViewModel()
) {
    val projects by taskViewModel.projects.collectAsState()


    LaunchedEffect(Unit) {
        taskViewModel.getAllProjects()
    }

    Scaffold(
        bottomBar = { BottomNavigationBar(selectedRoute = "projects") },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate("create_project")
                },
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add project")
            }
        }
    ) { paddingValues ->
        ProjectsList(
            projects = projects,
            onTaskClick = { projectId ->
                navController.navigate("project_detail/$projectId")
            },
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@Composable
fun ProjectsList(projects: List<ProjectUiModel>, onTaskClick: (String) -> Unit, modifier: Modifier = Modifier) {
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
fun ProjectItem(project: ProjectUiModel, onProjectClick: (String) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onProjectClick(project.uid) },
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Folder, // You can use another icon if you'd like
                    contentDescription = "Project Icon",
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = project.title,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Tasks completed",
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.secondary
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${project.completedTasks}/${project.totalTasks}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        }
    }
}