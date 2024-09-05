package com.example.taskmanager.domain.repository

import com.example.taskmanager.domain.model.Task

interface TaskRepository {
    suspend fun getAllTasks(): List<Task>
    suspend fun getTaskById(id: Int): Task?
    suspend fun insertTask(task: Task)
    suspend fun deleteTask(task: Task)
}