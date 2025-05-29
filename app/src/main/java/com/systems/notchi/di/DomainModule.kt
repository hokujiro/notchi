package com.systems.notchi.di

import com.systems.notchi.domain.usecase.projects.AddProjectUseCase
import org.koin.dsl.module
import com.systems.notchi.domain.usecase.tasks.GetTasksUseCase
import com.systems.notchi.domain.usecase.tasks.AddTaskUseCase
import com.systems.notchi.domain.usecase.projects.DeleteProjectUseCase
import com.systems.notchi.domain.usecase.tasks.DeleteTaskUseCase
import com.systems.notchi.domain.usecase.projects.GetProjectByIdUseCase
import com.systems.notchi.domain.usecase.projects.GetProjectsUseCase
import com.systems.notchi.domain.usecase.tasks.GetTasksForDayUseCase
import com.systems.notchi.domain.usecase.points.GetUserPointsUseCase
import com.systems.notchi.domain.usecase.auth.LoginUseCase
import com.systems.notchi.domain.usecase.auth.RegisterUseCase
import com.systems.notchi.domain.usecase.tasks.UpdateTaskUseCase
import com.systems.notchi.domain.usecase.auth.GoogleAuthUseCase
import com.systems.notchi.domain.usecase.auth.RefreshTokenUseCase
import com.systems.notchi.domain.usecase.frames.AddFrameListUseCase
import com.systems.notchi.domain.usecase.projects.UpdateProjectUseCase
import com.systems.notchi.domain.usecase.frames.AddFrameUseCase
import com.systems.notchi.domain.usecase.frames.DeleteFrameListUseCase
import com.systems.notchi.domain.usecase.frames.DeleteFrameUseCase
import com.systems.notchi.domain.usecase.frames.GetFramesUseCase
import com.systems.notchi.domain.usecase.profile.GetCurrentUserUseCase
import com.systems.notchi.domain.usecase.profile.UpdateUserProfileUseCase
import com.systems.notchi.domain.usecase.profile.UploadProfilePhotoUseCase
import com.systems.notchi.domain.usecase.rewards.AddRewardUseCase
import com.systems.notchi.domain.usecase.rewards.DeleteRewardUseCase
import com.systems.notchi.domain.usecase.rewards.GetRewardsUseCase
import com.systems.notchi.domain.usecase.rewards.RedeemRewardUseCase
import com.systems.notchi.domain.usecase.tasks.AddTaskListUseCase
import com.systems.notchi.domain.usecase.tasks.DeleteTaskListUseCase
import com.systems.notchi.domain.usecase.tasks.GetTasksForRangeUseCase

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
    factory { GetTasksForRangeUseCase(get()) } // Assuming use case depends on TaskRepository
    factory { GetCurrentUserUseCase(get()) } // Assuming use case depends on TaskRepository
    factory { UpdateUserProfileUseCase(get()) } // Assuming use case depends on TaskRepository
    factory { UploadProfilePhotoUseCase(get()) } // Assuming use case depends on TaskRepository
    factory { RedeemRewardUseCase(get()) } // Assuming use case depends on TaskRepository
    factory { DeleteRewardUseCase(get()) } // Assuming use case depends on TaskRepository

}