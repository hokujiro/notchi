package com.example.madetoliveapp.di

import com.example.madetoliveapp.presentation.auth.AuthViewModel
import com.example.madetoliveapp.presentation.tasks.TaskViewModel
import com.example.madetoliveapp.presentation.auth.TokenManager
import com.example.madetoliveapp.presentation.projects.ProjectViewModel
import com.example.madetoliveapp.presentation.tasks.FrameViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {
    single { TokenManager(androidContext()) }
    viewModel { TaskViewModel(get(), get(), get(), get(), get(), get(), get(), get(), get()) }
    viewModel { AuthViewModel(get(), get(), get(), get(), get()) }
    viewModel { ProjectViewModel(get(), get(), get(), get(), get()) }
    viewModel { FrameViewModel(get(), get(), get(), get(), get(), get()) }
}