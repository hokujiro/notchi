package com.example.madetoliveapp.presentation.components

import android.app.Activity
import android.content.Intent
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CalendarViewWeek
import androidx.compose.material.icons.filled.Coffee
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.example.madetoliveapp.presentation.projects.ProjectsActivity
import com.example.madetoliveapp.presentation.rewards.RewardsActivity
import com.example.madetoliveapp.presentation.tasks.TasksActivity
import com.example.madetoliveapp.presentation.settings.SettingsActivity

@Composable
fun BottomNavigationBar(selectedRoute: String) {
    val context = LocalContext.current
    val colors = MaterialTheme.colorScheme


    BottomNavigation(
        backgroundColor = colors.surface,            // background
        contentColor = colors.onSurface
    ) {
        BottomNavigationItem(
            icon = { Icon(Icons.Default.BarChart, contentDescription = "Daily") },
            label = { Text("Daily") },
            selected = selectedRoute == "daily",
            selectedContentColor = colors.primary,
            unselectedContentColor = colors.onSurface.copy(alpha = 0.6f),
            onClick = {
                if (selectedRoute != "daily") {
                    context.startActivity(Intent(context, TasksActivity::class.java))
                    (context as Activity).overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                }
            }
        )
        BottomNavigationItem(
            icon = { Icon(Icons.Default.CalendarViewWeek, contentDescription = "Projects") },
            label = { Text("Projects") },
            selected = selectedRoute == "projects",
            selectedContentColor = colors.primary,
            unselectedContentColor = colors.onSurface.copy(alpha = 0.6f),
            onClick = {
                if (selectedRoute != "projects") {
                    context.startActivity(Intent(context, ProjectsActivity::class.java))
                    (context as Activity).overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                }
            }
        )
        BottomNavigationItem(
            icon = { Icon(Icons.Default.Coffee   , contentDescription = "Rewards") },
            label = { Text("Rewards") },
            selected = selectedRoute == "rewards",
            selectedContentColor = colors.primary,
            unselectedContentColor = colors.onSurface.copy(alpha = 0.6f),
            onClick = {
                if (selectedRoute != "rewards") {
                    context.startActivity(Intent(context, RewardsActivity::class.java))
                    (context as Activity).overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                }
            }
        )

        BottomNavigationItem(
            icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
            label = { Text("Settings") },
            selected = selectedRoute == "settings",
            selectedContentColor = colors.primary,
            unselectedContentColor = colors.onSurface.copy(alpha = 0.6f),
            onClick = {
                if (selectedRoute != "settings") {
                    context.startActivity(Intent(context, SettingsActivity::class.java))
                    (context as Activity).overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                }
            }
        )
    }
}