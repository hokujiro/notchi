package com.example.madetoliveapp.di

import org.koin.dsl.module
import androidx.room.Room
import com.example.madetoliveapp.data.repository.TaskRepository
import com.example.madetoliveapp.data.repository.TaskRepositoryImpl
import com.example.madetoliveapp.data.source.local.bbdd.AppDatabase

val dataModule = module {
    // Room Database
    single {
        Room.databaseBuilder(get(), AppDatabase::class.java, "app_database")
            .fallbackToDestructiveMigration()
            .build()
    }

    // DAO
    single { get<AppDatabase>().taskDao() }

    // Repository
    single<TaskRepository> { TaskRepositoryImpl(get()) }
}