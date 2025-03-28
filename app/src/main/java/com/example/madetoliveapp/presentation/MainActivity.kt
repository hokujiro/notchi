package com.example.madetoliveapp.presentation

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.madetoliveapp.presentation.auth.AuthActivity
import com.example.madetoliveapp.presentation.auth.TokenManager
import com.example.madetoliveapp.presentation.rewards.RewardsActivity
import com.example.madetoliveapp.presentation.tasks.TasksActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        val tokenManager = TokenManager(applicationContext)
        val token = tokenManager.getToken()

        if (!token.isNullOrEmpty()) {
            // User is "logged in" – navigate to Home
            startActivity(Intent(this, TasksActivity::class.java))
        } else {
            // No token – navigate to Login/Register
            startActivity(Intent(this, AuthActivity::class.java))
        }

        finish() // Close the launch screen
    }
}