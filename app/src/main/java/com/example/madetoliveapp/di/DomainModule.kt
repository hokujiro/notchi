package com.example.madetoliveapp.di

import org.koin.dsl.module
import com.example.madetoliveapp.domain.usecase.GetTasksUseCase
import com.example.madetoliveapp.domain.usecase.AddTaskUseCase
import com.example.madetoliveapp.domain.usecase.DeleteTaskUseCase

val domainModule = module {
    // Use cases
    factory { GetTasksUseCase(get()) } // Assuming use case depends on TaskRepository
    factory { AddTaskUseCase(get()) } // Assuming use case depends on TaskRepository
    factory { DeleteTaskUseCase(get()) } // Assuming use case depends on TaskRepository

}