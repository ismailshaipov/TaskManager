package com.example.taskmanager.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.taskmanager.data.worker.TaskReminderWorker
import com.example.taskmanager.domain.model.Task
import com.example.taskmanager.domain.usecase.AddTaskUseCase
import com.example.taskmanager.domain.usecase.GetTaskByIdUseCase
import com.example.taskmanager.domain.usecase.GetTasksUseCase
import com.example.taskmanager.domain.usecase.UpdateTaskUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class TaskViewModel(
    application: Application, // Наследуем от AndroidViewModel для доступа к контексту
    private val getTasksUseCase: GetTasksUseCase,
    private val getTaskByIdUseCase: GetTaskByIdUseCase,
    private val addTaskUseCase: AddTaskUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase
) : AndroidViewModel(application) {

    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks

    fun loadTasks() {
        viewModelScope.launch {
            val taskList = getTasksUseCase()
            _tasks.value = taskList
        }
    }

    suspend fun getTaskById(id: Int): Task? {
        return getTaskByIdUseCase(id)
    }

    fun addTask(task: Task) {
        viewModelScope.launch {
            addTaskUseCase(task)
            loadTasks()
            scheduleTaskReminder(task)
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch {
            updateTaskUseCase(task) // Вызываем use case для обновления задачи
            loadTasks() // Перезагружаем список задач
        }
    }

    private fun scheduleTaskReminder(task: Task) {
        val workManager = WorkManager.getInstance(getApplication<Application>()) // Используем getApplication() для доступа к контексту

        val data = workDataOf(TaskReminderWorker.TASK_TITLE to task.title)

        val delay = task.dueDate - System.currentTimeMillis()
        if (delay > 0) {
            val reminderRequest = OneTimeWorkRequestBuilder<TaskReminderWorker>()
                .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                .setInputData(data)
                .build()

            workManager.enqueue(reminderRequest)
        }
    }
}
