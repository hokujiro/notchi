package com.systems.notchi

import android.app.Application
import androidx.room.Room
import com.google.firebase.FirebaseApp
import com.systems.notchi.data.source.local.bbdd.AppDatabase
import com.systems.notchi.di.dataModule
import com.systems.notchi.di.domainModule
import com.systems.notchi.di.presentationModule

import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class MadeToApp : Application() {
    lateinit var database: AppDatabase

    override fun onCreate() {
        super.onCreate()

        FirebaseApp.initializeApp(this)

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