package com.example.taskmanager.domain.model

data class Task(
    val id: Int,
    val title: String,
    val description: String,
    val dueDate: Long,
    val isCompleted: Boolean
)