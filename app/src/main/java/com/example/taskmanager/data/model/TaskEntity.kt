package com.example.taskmanager.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.taskmanager.domain.model.Task

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String,
    val dueDate: Long,
    val isCompleted: Boolean
){
    fun toDomainModel() = Task(id, title, description, dueDate, isCompleted)
}
// Функция расширения для преобразования Task в TaskEntity
fun Task.toEntity(): TaskEntity {
    return TaskEntity(
        id = this.id,
        title = this.title,
        description = this.description,
        dueDate = this.dueDate,
        isCompleted = this.isCompleted
    )
}
