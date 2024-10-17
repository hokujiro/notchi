package com.example.madetoliveapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.madetoliveapp.data.entity.TaskEntity
import com.example.madetoliveapp.data.repository.TaskRepository
import com.example.madetoliveapp.domain.usecase.AddTaskUseCase
import com.example.madetoliveapp.domain.usecase.DeleteTaskUseCase
import com.example.madetoliveapp.domain.usecase.GetTasksUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TaskViewModel(
    private val getAllTasksUseCase: GetTasksUseCase,
    private val addTaskUseCase: AddTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase
)  : ViewModel() {

    // Lista de tareas como StateFlow (mejor integración con Compose)
    private val _tasks = MutableStateFlow<List<TaskEntity>>(emptyList())
    val tasks: StateFlow<List<TaskEntity>> = _tasks.asStateFlow()

    // Método para marcar una tarea como completada
    fun toggleTaskCompletion(taskId: Int) {
        _tasks.value = _tasks.value.map { task ->
            if (task.uid == taskId) task.copy(checked = !task.checked)
            else task
        }
    }

    fun getAllTasks() {
        viewModelScope.launch {
            val result = getAllTasksUseCase.execute()
            _tasks.value = result
        }
    }

    // Add tasks using the use case
    fun addTask(task: TaskEntity) {
        viewModelScope.launch {
            addTaskUseCase.execute(task)
        }
        _tasks.value = _tasks.value + task
    }

    // Delete tasks using the use case
    fun deleteTask(task: TaskEntity) {
        viewModelScope.launch {
            deleteTaskUseCase.execute(task)
        }
    }
}