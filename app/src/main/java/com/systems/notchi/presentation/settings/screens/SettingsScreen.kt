package com.systems.notchi.presentation.settings.screens

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.systems.notchi.presentation.tasks.TaskViewModel
import com.systems.notchi.presentation.auth.AuthActivity
import com.systems.notchi.presentation.auth.TokenManager
import com.systems.notchi.presentation.components.BottomNavigationBar
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject


@Composable
fun SettingsScreen(
    taskViewModel: TaskViewModel = koinViewModel(),
    tokenManager: TokenManager = koinInject()
    // 👈 injects it via Koin
) {
    val context = LocalContext.current

    Scaffold(
        bottomBar = { BottomNavigationBar(selectedRoute = "settings") },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(padding)
            ) {
                Button(
                    onClick = {
                        tokenManager.clearTokens()
                        context.startActivity(Intent(context, AuthActivity::class.java))
                        if (context is Activity) context.finish()
                    },
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text("Log Out")
                }
            }
        }
    )
}