package com.example.madetoliveapp.presentation.projects.screens

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.madetoliveapp.presentation.components.BottomNavigationBar
import com.example.madetoliveapp.presentation.projects.ProjectViewModel
import com.example.madetoliveapp.presentation.projects.uimodel.ProjectUiModel
import com.example.madetoliveapp.presentation.theme.DarkText
import com.example.madetoliveapp.presentation.theme.PositiveTaskCheckedAccent


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectsListScreen(
    navController: NavController,
    projectViewModel: ProjectViewModel = koinViewModel()
) {
    val projects by projectViewModel.projects.collectAsState()

    LaunchedEffect(Unit) {
        projectViewModel.getAllProjects()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                modifier =  Modifier.background(Color(0xFFEBEBEB)),
                title = {
                    Text(
                        text = "Projects",
                        fontSize = 18.sp, // Smaller font
                        modifier = Modifier.padding(vertical = 4.dp) // Less padding
                    )
                },
                windowInsets = WindowInsets.statusBars.only(WindowInsetsSides.Horizontal) // Removes default top padding
            )
        },
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
            onDeleteProject = { project ->
                projectViewModel.deleteProject(project)
            },
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectsList(
    projects: List<ProjectUiModel>,
    onTaskClick: (String) -> Unit,
    onDeleteProject: (ProjectUiModel) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .padding(WindowInsets.systemBars.asPaddingValues()) // Optional, if you want system bar space
    ) {
        items(projects, key = { it.uid }) { project ->
            val dismissState = rememberDismissState(
                confirmValueChange = {
                    if (it == DismissValue.DismissedToStart) {
                        onDeleteProject(project)
                        true
                    } else false
                }
            )

            SwipeToDismiss(
                state = dismissState,
                directions = setOf(DismissDirection.EndToStart), // Swipe right-to-left
                background = {
                    val color = when (dismissState.dismissDirection) {
                        DismissDirection.EndToStart -> MaterialTheme.colorScheme.error
                        else -> MaterialTheme.colorScheme.background
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(color)
                            .padding(16.dp),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Text(
                            text = "Delete",
                            color = MaterialTheme.colorScheme.onError,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                },
                dismissContent = {
                    ProjectItem(project, onTaskClick)
                }
            )
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
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = project.icon,
                        fontSize = 24.sp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = project.title,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Text(
                    text = "${project.points} ⭐",
                    style = MaterialTheme.typography.bodyMedium,
                    color =  MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Tasks completed",
                    modifier = Modifier.size(16.dp),
                    tint = PositiveTaskCheckedAccent
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