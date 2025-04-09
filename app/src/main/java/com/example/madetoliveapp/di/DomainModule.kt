package com.example.madetoliveapp.di

import com.example.madetoliveapp.domain.usecase.AddProjectUseCase
import org.koin.dsl.module
import com.example.madetoliveapp.domain.usecase.GetTasksUseCase
import com.example.madetoliveapp.domain.usecase.AddTaskUseCase
import com.example.madetoliveapp.domain.usecase.DeleteTaskUseCase
import com.example.madetoliveapp.domain.usecase.GetProjectByIdUseCase
import com.example.madetoliveapp.domain.usecase.GetProjectsUseCase
import com.example.madetoliveapp.domain.usecase.GetTasksForDayUseCase
import com.example.madetoliveapp.domain.usecase.GetUserPointsUseCase
import com.example.madetoliveapp.domain.usecase.LoginUseCase
import com.example.madetoliveapp.domain.usecase.RegisterUseCase
import com.example.madetoliveapp.domain.usecase.UpdateTaskUseCase
import com.example.madetoliveapp.domain.usecase.GoogleAuthUseCase
import com.example.madetoliveapp.domain.usecase.RefreshTokenUseCase

val domainModule = module {
    // Use cases
    factory { GetTasksUseCase(get()) } // Assuming use case depends on TaskRepository
    factory { GetTasksForDayUseCase(get()) } // Assuming use case depends on TaskRepository
    factory { AddTaskUseCase(get()) } // Assuming use case depends on TaskRepository
    factory { DeleteTaskUseCase(get()) } // Assuming use case depends on TaskRepository
    factory { UpdateTaskUseCase(get()) } // Assuming use case depends on TaskRepository
    factory { LoginUseCase(get()) } // Assuming use case depends on TaskRepository
    factory { RegisterUseCase(get()) } // Assuming use case depends on TaskRepository
    factory { RegisterUseCase(get()) } // Assuming use case depends on TaskRepository
    factory { GoogleAuthUseCase(get()) } // Assuming use case depends on TaskRepository
    factory { GetProjectsUseCase(get()) } // Assuming use case depends on TaskRepository
    factory { AddProjectUseCase(get()) } // Assuming use case depends on TaskRepository
    factory { GetUserPointsUseCase(get()) } // Assuming use case depends on TaskRepository
    factory { RefreshTokenUseCase(get()) } // Assuming use case depends on TaskRepository
    factory { GetProjectByIdUseCase(get()) } // Assuming use case depends on TaskRepository

}