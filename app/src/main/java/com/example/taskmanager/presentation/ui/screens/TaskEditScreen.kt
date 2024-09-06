package com.example.taskmanager.presentation.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import com.example.taskmanager.domain.model.Task
import com.example.taskmanager.presentation.viewmodel.TaskViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun TaskEditScreen(
    taskId: Int?, // Nullable taskId для создания новой задачи (null) и редактирования (не null)
    onSaveTask: () -> Unit, // Callback при сохранении задачи
    onCancel: () -> Unit, // Callback при отмене
    viewModel: TaskViewModel = koinViewModel()
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var task by remember { mutableStateOf(Task(0, "", "", System.currentTimeMillis(), false)) }
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var dueDate by remember { mutableStateOf(System.currentTimeMillis()) }
    var isCompleted by remember { mutableStateOf(false) }

    // Загружаем задачу при редактировании
    LaunchedEffect(taskId) {
        taskId?.let {
            val loadedTask = viewModel.getTaskById(it)
            loadedTask?.let { taskData ->
                task = taskData
                title = taskData.title
                description = taskData.description
                dueDate = taskData.dueDate
                isCompleted = taskData.isCompleted
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = if (taskId != null) "Edit Task" else "New Task",
            style = MaterialTheme.typography.headlineLarge
        )

        // Поле ввода заголовка
        TextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth()
        )

        // Поле ввода описания
        TextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth()
        )

        // Поле для ввода даты выполнения (в миллисекундах)
        TextField(
            value = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(dueDate)),
            onValueChange = { dateString ->
                try {
                    val parsedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(dateString)
                    parsedDate?.let { dueDate = it.time }
                } catch (e: Exception) {
                    Toast.makeText(context, "Invalid date format", Toast.LENGTH_SHORT).show()
                }
            },
            label = { Text("Due Date (yyyy-MM-dd)") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        // Чекбокс для отметки выполненности задачи
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Completed")
            Checkbox(
                checked = isCompleted,
                onCheckedChange = { isCompleted = it }
            )
        }

        // Кнопки "Сохранить" и "Отмена"
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = onCancel) {
                Text("Cancel")
            }
            Button(onClick = {
                if (title.isNotEmpty() && description.isNotEmpty()) {

                    val newTask = Task(
                        id = task.id,
                        title = title,
                        description = description,
                        dueDate = dueDate,
                        isCompleted = isCompleted
                    )

                    coroutineScope.launch {
                        if (taskId == null) {
                            viewModel.addTask(newTask) // Добавление новой задачи
                        } else {
                            viewModel.updateTask(newTask) // Обновление существующей задачи
                        }

                        onSaveTask()

                        delay(1000L) // Добавляем задержку 1 секунда перед повторным включением кнопки
                    }
                }
            },) {
                Text("Save")
            }
        }
    }
}
