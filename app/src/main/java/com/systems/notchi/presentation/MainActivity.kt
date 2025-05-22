package com.systems.notchi.presentation

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.systems.notchi.presentation.auth.AuthActivity
import com.systems.notchi.presentation.auth.AuthViewModel
import com.systems.notchi.presentation.tasks.TasksActivity
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity() {

    private val viewModel: AuthViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

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