package com.example.madetoliveapp

import android.app.Application
import androidx.room.Room
import com.example.madetoliveapp.data.source.local.bbdd.AppDatabase
import com.example.madetoliveapp.di.dataModule
import com.example.madetoliveapp.di.domainModule
import com.example.madetoliveapp.di.presentationModule
import io.appwrite.Client
import io.appwrite.services.Account
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class MadeToApp : Application() {
    lateinit var database: AppDatabase

    override fun onCreate() {
        super.onCreate()

        startKoin {
            // Log Koin into Android logger
            androidLogger()
            // Reference Android context
            androidContext(this@MadeToApp)
            // Load modules
            modules(listOf(dataModule, domainModule, presentationModule))
        }

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