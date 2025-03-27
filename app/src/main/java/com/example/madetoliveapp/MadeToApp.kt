package com.example.madetoliveapp

import android.app.Application
import androidx.room.Room
import com.example.madetoliveapp.data.source.local.bbdd.AppDatabase
import com.example.madetoliveapp.di.dataModule
import com.example.madetoliveapp.di.domainModule
import com.example.madetoliveapp.di.presentationModule

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
    }
}