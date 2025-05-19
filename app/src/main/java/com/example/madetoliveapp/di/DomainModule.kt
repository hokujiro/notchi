package com.example.madetoliveapp.di

import com.example.madetoliveapp.domain.usecase.projects.AddProjectUseCase
import org.koin.dsl.module
import com.example.madetoliveapp.domain.usecase.tasks.GetTasksUseCase
import com.example.madetoliveapp.domain.usecase.tasks.AddTaskUseCase
import com.example.madetoliveapp.domain.usecase.projects.DeleteProjectUseCase
import com.example.madetoliveapp.domain.usecase.tasks.DeleteTaskUseCase
import com.example.madetoliveapp.domain.usecase.projects.GetProjectByIdUseCase
import com.example.madetoliveapp.domain.usecase.projects.GetProjectsUseCase
import com.example.madetoliveapp.domain.usecase.tasks.GetTasksForDayUseCase
import com.example.madetoliveapp.domain.usecase.points.GetUserPointsUseCase
import com.example.madetoliveapp.domain.usecase.auth.LoginUseCase
import com.example.madetoliveapp.domain.usecase.auth.RegisterUseCase
import com.example.madetoliveapp.domain.usecase.tasks.UpdateTaskUseCase
import com.example.madetoliveapp.domain.usecase.auth.GoogleAuthUseCase
import com.example.madetoliveapp.domain.usecase.auth.RefreshTokenUseCase
import com.example.madetoliveapp.domain.usecase.frames.AddFrameListUseCase
import com.example.madetoliveapp.domain.usecase.projects.UpdateProjectUseCase
import com.example.madetoliveapp.domain.usecase.frames.AddFrameUseCase
import com.example.madetoliveapp.domain.usecase.frames.DeleteFrameListUseCase
import com.example.madetoliveapp.domain.usecase.frames.DeleteFrameUseCase
import com.example.madetoliveapp.domain.usecase.frames.GetFramesUseCase
import com.example.madetoliveapp.domain.usecase.rewards.AddRewardUseCase
import com.example.madetoliveapp.domain.usecase.rewards.GetRewardsUseCase
import com.example.madetoliveapp.domain.usecase.tasks.AddTaskListUseCase
import com.example.madetoliveapp.domain.usecase.tasks.DeleteTaskListUseCase

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
    factory { DeleteProjectUseCase(get()) } // Assuming use case depends on TaskRepository
    factory { UpdateProjectUseCase(get()) } // Assuming use case depends on TaskRepository
    factory { GetFramesUseCase(get()) } // Assuming use case depends on TaskRepository
    factory { AddFrameUseCase(get()) } // Assuming use case depends on TaskRepository
    factory { DeleteFrameUseCase(get()) } // Assuming use case depends on TaskRepository
    factory { DeleteFrameListUseCase(get()) } // Assuming use case depends on TaskRepository
    factory { AddFrameListUseCase(get()) } // Assuming use case depends on TaskRepository
    factory { AddTaskListUseCase(get()) } // Assuming use case depends on TaskRepository
    factory { DeleteTaskListUseCase(get()) } // Assuming use case depends on TaskRepository
    factory { AddRewardUseCase(get()) } // Assuming use case depends on TaskRepository
    factory { GetRewardsUseCase(get()) } // Assuming use case depends on TaskRepository

}