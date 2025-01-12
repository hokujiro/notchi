package com.example.madetoliveapp.di

import com.example.madetoliveapp.presentation.TaskViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {
    // ViewModel
    viewModel { TaskViewModel(get(), get(), get(), get()) } // Assuming ViewModel depends on GetTasksUseCase
}