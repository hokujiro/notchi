package com.example.madetoliveapp.presentation

import androidx.lifecycle.ViewModel
import com.example.madetoliveapp.data.entity.TaskEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class TaskViewModel : ViewModel() {

    // Lista de tareas como StateFlow (mejor integración con Compose)
    private val _tasks = MutableStateFlow<List<TaskEntity>>(emptyList())
    val tasks: StateFlow<List<TaskEntity>> = _tasks.asStateFlow()

    // Método para agregar una nueva tarea
    fun addTask(task: TaskEntity) {
        _tasks.value = _tasks.value + task
    }

    // Método para marcar una tarea como completada
    fun toggleTaskCompletion(taskId: Int) {
        _tasks.value = _tasks.value.map { task ->
            if (task.uid == taskId) task.copy(checked = !task.checked)
            else task
        }
    }
}