package com.example.taskmanager.domain.usecase

import com.example.taskmanager.domain.model.Task
import com.example.taskmanager.domain.repository.TaskRepository

class AddTaskUseCase(private val repository: TaskRepository) {
    suspend operator fun invoke(task: Task) {
        repository.insertTask(task)
    }
}