package com.example.madetoliveapp

import android.app.Application
import androidx.room.Room
import com.example.madetoliveapp.data.source.local.bbdd.AppDatabase
import io.appwrite.Client
import io.appwrite.services.Account

class MadeToApp : Application() {
    lateinit var database: AppDatabase

    override fun onCreate() {
        super.onCreate()

        // Inicializar la base de datos
        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "database"
        ).build()

        //Inicializar cliente AppWrite.io
        val client = Client(applicationContext)
            .setEndpoint("https://cloud.appwrite.io/v1") // Replace with your Appwrite endpoint
            .setProject("66f2fc83001087da40a2") // Replace with your project ID
            .setSelfSigned(true) // Optional for self-signed certificates

        val account = Account(client)
    }
}