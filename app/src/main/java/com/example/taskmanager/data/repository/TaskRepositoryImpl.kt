package com.example.taskmanager.data.repository

import com.example.taskmanager.data.local.TaskDao
import com.example.taskmanager.data.model.TaskEntity
import com.example.taskmanager.data.model.toEntity
import com.example.taskmanager.domain.model.Task
import com.example.taskmanager.domain.repository.TaskRepository

class TaskRepositoryImpl(private val taskDao: TaskDao) : TaskRepository {
    override suspend fun getAllTasks(): List<Task> {
        return taskDao.getAllTasks().map { it.toDomainModel() }
    }

    override suspend fun getTaskById(id: Int): Task? {
        return taskDao.getTaskById(id)?.toDomainModel()
    }

    override suspend fun updateTask(task: Task) {
        taskDao.updateTask(task.toEntity()) // Преобразуем Task в TaskEntity и обновляем
    }

    override suspend fun insertTask(task: Task) {
        taskDao.insertTask(TaskEntity(
            id = task.id,
            title = task.title,
            description = task.description,
            dueDate = task.dueDate,
            isCompleted = task.isCompleted
        ))
    }

    override suspend fun deleteTask(task: Task) {
        taskDao.deleteTask(TaskEntity(
            id = task.id,
            title = task.title,
            description = task.description,
            dueDate = task.dueDate,
            isCompleted = task.isCompleted
        ))
    }
}