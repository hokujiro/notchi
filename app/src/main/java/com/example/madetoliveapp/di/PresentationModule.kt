package com.example.madetoliveapp.di

import com.example.madetoliveapp.presentation.auth.AuthViewModel
import com.example.madetoliveapp.presentation.TaskViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {
    viewModel { TaskViewModel(get(), get(), get(), get(), get()) }
    viewModel { AuthViewModel(get(), get(), get()) }
}