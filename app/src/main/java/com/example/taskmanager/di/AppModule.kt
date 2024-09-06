// AppModule.kt
package com.example.taskmanager.di

import org.koin.dsl.module
import org.koin.androidx.viewmodel.dsl.viewModel
import androidx.room.Room
import com.example.taskmanager.data.local.TaskDatabase
import com.example.taskmanager.data.repository.TaskRepositoryImpl
import com.example.taskmanager.domain.repository.TaskRepository
import com.example.taskmanager.domain.usecase.*
import com.example.taskmanager.presentation.viewmodel.TaskViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext

val appModule = module {
    // Database
    single {
        Room.databaseBuilder(get(), TaskDatabase::class.java, "task_database")
            .fallbackToDestructiveMigration()
            .build()
    }

    // DAO
    single { get<TaskDatabase>().taskDao() }

    // Repository
    single<TaskRepository> { TaskRepositoryImpl(get()) }

    // Use Cases
    single { GetTasksUseCase(get()) }
    single { GetTaskByIdUseCase(get()) }
    single { AddTaskUseCase(get()) }
    single { UpdateTaskUseCase(get()) }

    // ViewModel
    viewModel {
        TaskViewModel(
            get(),
            get(),
            get(),
            get(),
            get()
        )
    }
}
