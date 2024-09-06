package com.example.taskmanager.presentation.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.taskmanager.presentation.ui.screens.TaskEditScreen
import com.example.taskmanager.presentation.ui.screens.TaskListScreen

@Composable
fun TaskManagerNavHost(){
    val navController = rememberNavController()
    NavHost(navController, startDestination = "taskList"){

        // Экран списка задач
        composable("taskList") {
            TaskListScreen(
                onTaskClick = { /* Можем раскрывать задачу на месте */ },
                onAddTaskClick = {
                    navController.navigate("taskEdit") // Переход на экран создания новой задачи
                },
                onEditTaskClick = { taskId ->
                    navController.navigate("taskEdit/$taskId") // Переход к редактированию задачи
                }
            )
        }

        // Экран создания новой задачи (без taskId)
        composable("taskEdit") {
            TaskEditScreen(
                taskId = null, // Здесь taskId отсутствует, так как создаётся новая задача
                onSaveTask = {
                    navController.popBackStack() // Возврат на экран списка задач после сохранения
                },
                onCancel = {
                    navController.popBackStack() // Возврат на экран списка задач при отмене
                }
            )
        }

        // Экран редактирования задачи (с taskId)
        composable(
            "taskEdit/{taskId}", // Маршрут для редактирования задачи
            arguments = listOf(navArgument("taskId") { type = NavType.IntType }) // taskId обязательный
        ) { backStackEntry ->
            val taskId = backStackEntry.arguments?.getInt("taskId") // Получаем taskId
            TaskEditScreen(
                taskId = taskId, // Передаём taskId для редактирования
                onSaveTask = {
                    navController.popBackStack() // Возврат на экран списка задач после сохранения
                },
                onCancel = {
                    navController.popBackStack() // Возврат на экран списка задач при отмене
                }
            )
        }
    }
}