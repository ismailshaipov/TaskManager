package com.example.taskmanager.presentation.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.taskmanager.domain.model.Task
import com.example.taskmanager.presentation.viewmodel.TaskViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListScreen(
    onTaskClick: (Int) -> Unit,       // Переход к детальной информации
    onAddTaskClick: () -> Unit,       // Переход на экран создания новой задачи
    onEditTaskClick: (Int) -> Unit,   // Переход на экран редактирования задачи
    viewModel: TaskViewModel = koinViewModel()
) {
    val tasks by viewModel.tasks.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadTasks()
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Task Manager") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddTaskClick) {
                Icon(Icons.Default.Add, contentDescription = "Add Task")
            }
        }
    ) { paddingValues ->
        TaskList(
            tasks = tasks,
            onTaskClick = onTaskClick,
            onEditTaskClick = onEditTaskClick,
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@Composable
fun TaskList(
    tasks: List<Task>,
    onTaskClick: (Int) -> Unit,
    onEditTaskClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var expandedTaskId by remember { mutableStateOf<Int?>(null) }  // Храним ID раскрытой задачи

    LazyColumn(modifier = modifier.fillMaxSize()) {
        items(tasks) { task ->
            TaskItem(
                task = task,
                isExpanded = task.id == expandedTaskId, // Проверяем, раскрыта ли эта задача
                onTaskClick = { taskId ->
                    expandedTaskId = if (expandedTaskId == taskId) null else taskId // Сворачиваем, если уже раскрыта
                },
                onEditTaskClick = onEditTaskClick
            )
            HorizontalDivider()
        }
    }
}

@Composable
fun TaskItem(
    task: Task,
    isExpanded: Boolean, // Флаг для отображения полного описания
    onTaskClick: (Int) -> Unit,
    onEditTaskClick: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onTaskClick(task.id) }
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = task.title, style = MaterialTheme.typography.titleSmall)
                if (isExpanded) {
                    Text(text = task.description, style = MaterialTheme.typography.bodyLarge)
                }
            }
            IconButton(onClick = { onEditTaskClick(task.id) }) { // Кнопка для редактирования
                Icon(Icons.Default.Edit, contentDescription = "Edit Task")
            }
        }
        if (isExpanded) {
            // Здесь можно добавить дополнительную информацию при раскрытии
            Checkbox(
                checked = task.isCompleted,
                onCheckedChange = { /* Обработка изменения состояния */ }
            )
        }
    }
}
