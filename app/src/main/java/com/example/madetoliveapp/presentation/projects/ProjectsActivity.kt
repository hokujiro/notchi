package com.example.madetoliveapp.presentation.projects

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.madetoliveapp.presentation.projects.screens.CreateProjectScreen
import com.example.madetoliveapp.presentation.projects.screens.ProjectDetailScreen
import com.example.madetoliveapp.presentation.projects.screens.ProjectsListScreen
import com.example.madetoliveapp.presentation.tasks.TaskViewModel
import com.example.madetoliveapp.presentation.theme.MadeToLiveTheme
import org.koin.androidx.viewmodel.ext.android.getViewModel

class ProjectsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MadeToLiveTheme {
                val navController = rememberNavController()

                NavHost(navController, startDestination = "projects_list") {
                    composable("projects_list") {
                        ProjectsListScreen(navController = navController)
                    }
                    composable("create_project") {
                        CreateProjectScreen(
                            onCancel = { navController.popBackStack() },
                            onSave = { project ->
                                val projectViewModel: ProjectViewModel = getViewModel()
                                projectViewModel.addProject(project)
                                navController.popBackStack()
                            }
                        )
                    }
                    composable(
                        route = "project_detail/{projectId}",
                        arguments = listOf(navArgument("projectId") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val projectId = backStackEntry.arguments?.getString("projectId")
                        if (projectId != null) {
                            ProjectDetailScreen(projectId)
                        }
                    }
                }
            }
        }
    }
}