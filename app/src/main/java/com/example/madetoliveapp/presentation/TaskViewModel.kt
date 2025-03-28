package com.example.madetoliveapp.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.madetoliveapp.data.entity.TaskEntity
import com.example.madetoliveapp.domain.model.TaskModel
import com.example.madetoliveapp.domain.usecase.AddTaskUseCase
import com.example.madetoliveapp.domain.usecase.DeleteTaskUseCase
import com.example.madetoliveapp.domain.usecase.GetTasksForDayUseCase
import com.example.madetoliveapp.domain.usecase.GetTasksUseCase
import com.example.madetoliveapp.domain.usecase.UpdateTaskUseCase
import com.example.madetoliveapp.presentation.auth.TokenManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TaskViewModel(
    private val getAllTasksUseCase: GetTasksUseCase,
    private val getTasksForDayUseCase: GetTasksForDayUseCase,
    private val addTaskUseCase: AddTaskUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
)  : ViewModel() {

    // Lista de tareas como StateFlow (mejor integración con Compose)
    private val _tasks = MutableStateFlow<List<TaskModel>>(emptyList())
    val tasks: StateFlow<List<TaskModel>> = _tasks.asStateFlow()

    // Método para marcar una tarea como completada
    fun toggleTaskCompletion(taskId: String) {
        viewModelScope.launch {
            val taskToUpdate = _tasks.value.find { it.uid == taskId }
            taskToUpdate?.let { task ->
                val updatedTask = task.copy(checked = !task.checked)
                updateTaskUseCase.execute(updatedTask) // Update task in the database
                // Update the state with only the modified task
                _tasks.value = _tasks.value.map { if (it.uid == taskId) updatedTask else it }
            }
        }
    }
//testing new repo
fun getAllTasks() {
    viewModelScope.launch {
        try {
            val result = getAllTasksUseCase.execute()
            _tasks.value = result
        } catch (e: Exception) {
            // Handle the error gracefully, log it, or update UI state
            Log.e("TaskViewModel", "Error loading tasks", e)
            // Optionally show error state to UI
        }
    }
}

    fun getTasksForDay(date: Long) {
        viewModelScope.launch {
            val result = getTasksForDayUseCase.execute(date)
            _tasks.value = result
        }
    }

    // Add tasks using the use case
    fun addTask(task: TaskModel) {
        viewModelScope.launch {
            addTaskUseCase.execute(task)
        }
        _tasks.value += task
    }

    // Delete tasks using the use case
    fun deleteTask(task: TaskModel) {
        viewModelScope.launch {
            deleteTaskUseCase.execute(task)
        }
    }
}