package com.systems.notchi.presentation.tasks

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.systems.notchi.presentation.auth.AuthActivity
import com.systems.notchi.presentation.auth.TokenManager
import com.systems.notchi.presentation.profile.DrawerContent
import com.systems.notchi.presentation.profile.ProfileScreen
import com.systems.notchi.presentation.profile.ProfileViewModel
import com.systems.notchi.presentation.projects.ProjectViewModel
import com.systems.notchi.presentation.projects.screens.CreateProjectScreen
import com.systems.notchi.presentation.projects.screens.ProjectDetailScreen
import com.systems.notchi.presentation.projects.screens.ProjectsListScreen
import com.systems.notchi.presentation.tasks.screens.TasksScreen
import com.systems.notchi.presentation.theme.MadeToLiveTheme
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.getViewModel


class TasksActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MadeToLiveTheme {
                val navController = rememberNavController()
                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                val scope = rememberCoroutineScope()
                val tokenManager = TokenManager(applicationContext)
                val context = this@TasksActivity

                ModalNavigationDrawer(
                    drawerState = drawerState,
                    gesturesEnabled = true,
                    drawerContent = {
                        DrawerContent(
                            onClose = { scope.launch { drawerState.close() } },
                            onProfileClick = { navController.navigate("profile") },
                            onLogout = {  tokenManager.clearTokens()
                                context.startActivity(Intent(context, AuthActivity::class.java))
                                if (context is Activity) context.finish() }
                        )
                    }
                ) {
                    NavHost(navController, startDestination = "task_list") {
                        composable("task_list") {
                            TasksScreen(
                                openDrawer = { scope.launch { drawerState.open() } }
                            )
                        }
                        composable("profile") {
                            ProfileScreen()
                        }
                    }
                }
            }
        }
    }
}