package com.systems.notchi.presentation.tasks

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.systems.notchi.domain.model.FrameModel
import com.systems.notchi.domain.model.TaskModel
import com.systems.notchi.domain.usecase.tasks.AddTaskUseCase
import com.systems.notchi.domain.usecase.tasks.DeleteTaskUseCase
import com.systems.notchi.domain.usecase.tasks.GetTasksForDayUseCase
import com.systems.notchi.domain.usecase.tasks.GetTasksUseCase
import com.systems.notchi.domain.usecase.points.GetUserPointsUseCase
import com.systems.notchi.domain.usecase.frames.AddFrameUseCase
import com.systems.notchi.domain.usecase.frames.GetFramesUseCase
import com.systems.notchi.domain.usecase.tasks.DeleteTaskListUseCase
import com.systems.notchi.domain.usecase.tasks.GetTasksForRangeUseCase
import com.systems.notchi.domain.usecase.tasks.UpdateTaskUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate
import java.time.ZoneId

class TaskViewModel(
    private val getAllTasksUseCase: GetTasksUseCase,
    private val getTasksForDayUseCase: GetTasksForDayUseCase,
    private val addTaskUseCase: AddTaskUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val getUserPointsUseCase: GetUserPointsUseCase,
    private val getAllFramesUseCase: GetFramesUseCase,
    private val addFrameUseCase: AddFrameUseCase,
    private val deleteTaskListUseCase: DeleteTaskListUseCase,
    private val getTasksForRangeUseCase: GetTasksForRangeUseCase
) : ViewModel() {

    private val _tasks = MutableStateFlow<List<TaskModel>>(emptyList())
    val tasks: StateFlow<List<TaskModel>> = _tasks.asStateFlow()

    private val _frames = MutableStateFlow<List<FrameModel>>(emptyList())
    val frames: StateFlow<List<FrameModel>> = _frames.asStateFlow()

    private val _totalPoints = MutableStateFlow(0)
    val totalPoints: StateFlow<Int> = _totalPoints

    private val _taskFilter = MutableStateFlow(TaskFilter.ALL)
    val taskFilter = _taskFilter.asStateFlow()

    private val _sortMode = MutableStateFlow(SortMode.BY_POINTS)
    val sortMode = _sortMode.asStateFlow()

    private val _selectionMode = MutableStateFlow(false)
    val selectionMode = _selectionMode.asStateFlow()

    private val _selectedTasks = MutableStateFlow<Set<String>>(emptySet())
    val selectedTasks = _selectedTasks.asStateFlow()

    fun enableSelectionMode() {
        _selectionMode.value = true
    }

    fun disableSelectionMode() {
        _selectionMode.value = false
        _selectedTasks.value = emptySet()
    }

    fun toggleTaskSelection(taskId: String) {
        val updatedSet = _selectedTasks.value.toMutableSet()
        if (updatedSet.contains(taskId)) {
            updatedSet.remove(taskId)
        } else {
            updatedSet.add(taskId)
        }
        _selectedTasks.value = updatedSet

        // Automatically exit selection mode if nothing selected
        if (updatedSet.isEmpty()) {
            disableSelectionMode()
        }
    }

    val visibleTasks = combine(_tasks, _taskFilter, _sortMode) { allTasks, filter, sort ->
        val filtered = when (filter) {
            TaskFilter.ALL -> allTasks
            TaskFilter.POSITIVE -> allTasks.filter { (it.points ?: 0) > 0 }
            TaskFilter.NEGATIVE -> allTasks.filter { (it.points ?: 0) < 0 }
        }

        val sorted = when (sort) {
            SortMode.BY_POINTS -> filtered.sortedWith(
                compareByDescending<TaskModel> { (it.points ?: 0) >= 0 }
                    .thenBy { it.checked }
                    .thenByDescending { it.points ?: 0 }
            )

            SortMode.BY_CREATION -> filtered.sortedWith(
                compareByDescending<TaskModel> { (it.points ?: 0) >= 0 }
                    .thenBy { it.checked }
                    .thenBy { it.date }
            )
        }
        sorted
    }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val dailyPoints = combine(tasks, taskFilter) { allTasks, filter ->
        val filtered = when (filter) {
            TaskFilter.ALL -> allTasks
            TaskFilter.POSITIVE -> allTasks.filter { (it.points ?: 0) > 0 }
            TaskFilter.NEGATIVE -> allTasks.filter { (it.points ?: 0) < 0 }
        }

        filtered.filter { it.checked }.sumOf { it.points ?: 0 }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, 0)


    private val _weeklyTasks = MutableStateFlow<List<TaskModel>>(emptyList())
    val weeklyPointsMap: StateFlow<Map<LocalDate, Int>> = _weeklyTasks.map { allTasks ->
        allTasks
            .filter { it.checked && (it.points ?: 0) != 0 }
            .groupBy { task ->
                task.date?.toInstant()?.atZone(ZoneId.systemDefault())?.toLocalDate()
            }
            .filterKeys { it != null }
            .mapKeys { it.key!! }
            .mapValues { entry -> entry.value.sumOf { it.points ?: 0 } }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyMap())

    suspend fun loadTasksForWeek(start: LocalDate, end: LocalDate) {
        val startEpoch = start.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        val endEpoch = end.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        _weeklyTasks.value = getTasksForRangeUseCase.execute(startEpoch, endEpoch)
    }

    fun setFilter(filter: TaskFilter) {
        _taskFilter.value = filter
    }

    fun toggleSortMode() {
        _sortMode.value =
            if (_sortMode.value == SortMode.BY_POINTS) SortMode.BY_CREATION else SortMode.BY_POINTS
    }

    suspend fun toggleTaskCompletion(taskId: String) {
        val taskToUpdate = _tasks.value.find { it.uid == taskId }
        taskToUpdate?.let { task ->
            val updatedTask = task.copy(checked = !task.checked)
            updateTaskUseCase.execute(updatedTask)
            _tasks.value = _tasks.value.map { if (it.uid == taskId) updatedTask else it }
            loadUserPoints()
        }
    }

    suspend fun updateTask(task: TaskModel) {
        updateTaskUseCase.execute(task)
        _tasks.value = _tasks.value.map { if (it.uid == task.uid) task else it }
        loadUserPoints()
    }

    suspend fun getAllTasks() {
        try {
            val result = getAllTasksUseCase.execute()
            _tasks.value = result
        } catch (e: Exception) {
            Log.e("TaskViewModel", "Error loading tasks", e)
        }
    }

    suspend fun getTasksForDay(date: Long) {
        try {
            val result = getTasksForDayUseCase.execute(date)
            _tasks.value = result
        } catch (e: Exception) {
            Log.e("TaskViewModel", "Error loading tasks for day", e)
        }
    }

    suspend fun addTask(task: TaskModel) {
        addTaskUseCase.execute(task)
        _tasks.value += task
    }

    suspend fun deleteTask(task: TaskModel) {
        deleteTaskUseCase.execute(task)
        _tasks.value = _tasks.value.filterNot { it.uid == task.uid }
    }

    suspend fun loadUserPoints() {
        try {
            val points = getUserPointsUseCase.execute()
            _totalPoints.value = points.toInt()
        } catch (e: Exception) {
            Log.e("TaskViewModel", "Error loading user points", e)
        }
    }

    suspend fun getAllFrames() {
        try {
            val result = getAllFramesUseCase.execute()
            _frames.value = result
        } catch (e: Exception) {
            Log.e("TaskViewModel", "Error loading frames", e)
        }
    }

    suspend fun addFrame(frame: FrameModel) {
        addFrameUseCase.execute(frame)
        _frames.value += frame
    }

    suspend fun deleteSelectedTasks() {
       val tasksToDelete = _selectedTasks.value.mapNotNull { id ->
           tasks.value.find { it.uid == id }
       }

        if(tasksToDelete.isNotEmpty()){
            deleteTaskListUseCase.execute(tasksToDelete)
            getAllTasks()
        }
        disableSelectionMode()
    }

    suspend fun saveSelectedTasksAsFrames() {
        _selectedTasks.value.forEach { id ->
            tasks.value.find { it.uid == id }?.let { task ->
                addFrame(
                    FrameModel(
                        uid = task.uid,
                        title = task.title,
                        points = task.points,
                        project = task.project
                    )
                )
            }
        }
        disableSelectionMode()
    }
}

enum class TaskFilter { ALL, POSITIVE, NEGATIVE }
enum class SortMode { BY_POINTS, BY_CREATION }