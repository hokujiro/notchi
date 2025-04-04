package com.example.madetoliveapp.presentation

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.example.madetoliveapp.presentation.auth.AuthActivity
import com.example.madetoliveapp.presentation.auth.AuthViewModel
import com.example.madetoliveapp.presentation.auth.TokenManager
import com.example.madetoliveapp.presentation.tasks.TasksActivity
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val viewModel: AuthViewModel by viewModels() // Or by koinViewModel() if using Koin

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        // Optional content view (if you're not using Compose)

        lifecycleScope.launch {
            viewModel.navigationTarget.collect { target ->
                when (target) {
                    AuthViewModel.NavigationTarget.TASKS -> {
                        startActivity(Intent(this@MainActivity, TasksActivity::class.java))
                        finish()
                    }

                    AuthViewModel.NavigationTarget.AUTH -> {
                        startActivity(Intent(this@MainActivity, AuthActivity::class.java))
                        finish()
                    }

                    null -> {
                        // Do nothing — still checking session
                    }
                }
            }
        }
    }
}