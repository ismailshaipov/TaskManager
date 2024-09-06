package com.example.taskmanager.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanager.domain.model.Task
import com.example.taskmanager.domain.usecase.GetTasksUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TaskViewModel(private val getTasksUseCase: GetTasksUseCase) : ViewModel() {
    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks

    fun  loadTasks(){
        viewModelScope.launch {
            val taskList = getTasksUseCase()
            _tasks.value = taskList
        }
    }
}