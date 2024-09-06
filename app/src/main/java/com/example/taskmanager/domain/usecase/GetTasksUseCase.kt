package com.example.taskmanager.domain.usecase

import com.example.taskmanager.domain.model.Task
import com.example.taskmanager.domain.repository.TaskRepository

class GetTasksUseCase(private val repository: TaskRepository) {
    suspend operator fun invoke(): List<Task> {
        return repository.getAllTasks()
    }
}