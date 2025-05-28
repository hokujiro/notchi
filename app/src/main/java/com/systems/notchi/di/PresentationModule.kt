package com.systems.notchi.di

import com.systems.notchi.presentation.auth.AuthViewModel
import com.systems.notchi.presentation.tasks.TaskViewModel
import com.systems.notchi.presentation.auth.TokenManager
import com.systems.notchi.presentation.profile.ProfileViewModel
import com.systems.notchi.presentation.projects.ProjectViewModel
import com.systems.notchi.presentation.rewards.RewardsViewModel
import com.systems.notchi.presentation.frames.FrameViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {
    single { TokenManager(androidContext()) }
    viewModel { TaskViewModel(get(), get(), get(), get(), get(), get(), get(), get(), get(), get()) }
    viewModel { AuthViewModel(get(), get(), get(), get(), get()) }
    viewModel { ProjectViewModel(get(), get(), get(), get(), get()) }
    viewModel { FrameViewModel(get(), get(), get(), get(), get(), get()) }
    viewModel { RewardsViewModel(get(), get()) }
    viewModel { ProfileViewModel(get(), get(), get()) }
}