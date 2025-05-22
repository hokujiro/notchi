package com.systems.notchi.presentation.tasks

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import com.systems.notchi.presentation.auth.AuthActivity
import com.systems.notchi.presentation.auth.TokenManager
import com.systems.notchi.presentation.tasks.screens.TasksScreen
import com.systems.notchi.presentation.theme.MadeToLiveTheme


class TasksActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tokenManager = TokenManager(applicationContext)

        setContent {
            MadeToLiveTheme {

                // Listen for token expiration
                LaunchedEffect(Unit) {
                    tokenManager.sessionExpiredFlow.collect {
                        Log.d("TokenDebug", "🔥 SESSION EXPIRED FLOW RECEIVED")
                        // Navigate to login screen and clear task backstack
                        startActivity(Intent(this@TasksActivity, AuthActivity::class.java))
                        finishAffinity() // Finish all previous activities
                    }
                }
                TasksScreen()
            }
        }
    }
}