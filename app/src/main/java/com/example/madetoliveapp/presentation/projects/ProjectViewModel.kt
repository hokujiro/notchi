package com.example.madetoliveapp.presentation.projects

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.madetoliveapp.domain.model.ProjectModel
import com.example.madetoliveapp.domain.model.TaskModel
import com.example.madetoliveapp.domain.usecase.projects.AddProjectUseCase
import com.example.madetoliveapp.domain.usecase.tasks.AddTaskUseCase
import com.example.madetoliveapp.domain.usecase.projects.DeleteProjectUseCase
import com.example.madetoliveapp.domain.usecase.tasks.DeleteTaskUseCase
import com.example.madetoliveapp.domain.usecase.projects.GetProjectByIdUseCase
import com.example.madetoliveapp.domain.usecase.projects.GetProjectsUseCase
import com.example.madetoliveapp.domain.usecase.tasks.GetTasksForDayUseCase
import com.example.madetoliveapp.domain.usecase.tasks.GetTasksUseCase
import com.example.madetoliveapp.domain.usecase.points.GetUserPointsUseCase
import com.example.madetoliveapp.domain.usecase.tasks.UpdateTaskUseCase
import com.example.madetoliveapp.presentation.projects.uimodel.ProjectUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId
import java.util.Date

class ProjectViewModel(
    private val addProjectUseCase: AddProjectUseCase,
    private val getAllTasksUseCase: GetTasksUseCase,
    private val getAllProjectsUseCase: GetProjectsUseCase,
    private val getProjectByIdUseCase: GetProjectByIdUseCase,
    private val deleteProjectUseCase: DeleteProjectUseCase,
) : ViewModel() {

    private val _tasks = MutableStateFlow<List<TaskModel>>(emptyList())
    val tasks: StateFlow<List<TaskModel>> = _tasks.asStateFlow()

    private val _projects = MutableStateFlow<List<ProjectUiModel>>(emptyList())
    val projects: StateFlow<List<ProjectUiModel>> = _projects.asStateFlow()

    private val _projectById = MutableStateFlow<ProjectUiModel>(ProjectUiModel())
    val projectById: StateFlow<ProjectUiModel> = _projectById.asStateFlow()

    private val _groupedTasksByDate = MutableStateFlow<Map<LocalDate, List<TaskModel>>>(emptyMap())
    val groupedTasksByDate: StateFlow<Map<LocalDate, List<TaskModel>>> = _groupedTasksByDate.asStateFlow()

    private val _completedProjectDates = MutableStateFlow<Map<Date, List<TaskModel>>>(emptyMap())
    val completedProjectDates: StateFlow<Map<Date, List<TaskModel>>> = _completedProjectDates.asStateFlow()

    private val _totalPoints = MutableStateFlow(0)
    val totalPoints: StateFlow<Int> = _totalPoints.asStateFlow()

    private val _monthlyPoints = MutableStateFlow<Map<YearMonth, Int>>(emptyMap())
    val monthlyPoints: StateFlow<Map<YearMonth, Int>> = _monthlyPoints.asStateFlow()

    suspend fun getAllTasks() {
        try {
            val result = getAllTasksUseCase.execute()
            _tasks.value = result
        } catch (e: Exception) {
            Log.e("TaskViewModel", "Error loading tasks", e)
        }
    }

    fun addProject(project: ProjectUiModel) {
        viewModelScope.launch {
            addProjectUseCase.execute(
                ProjectModel(
                    uid = project.uid,
                    title = project.title,
                    tasksList = project.tasksList,
                    color = project.color,
                    icon = project.icon
                )
            )
        }
        _projects.value += project
    }

    fun getAllProjects() {
        viewModelScope.launch {
            getAllTasks()
            try {
                val result = getAllProjectsUseCase.execute()

                val processedProjects: List<ProjectUiModel> = result.map { project ->
                    val tasks = project.tasksList ?: emptyList()
                    val completedTasks = tasks.filter { it.checked }

                    ProjectUiModel(
                        uid = project.uid,
                        title = project.title,
                        tasksList = tasks,
                        color = project.color,
                        icon = project.icon,
                        totalTasks = tasks.size,
                        completedTasks = completedTasks.size,
                        points = completedTasks.sumOf { it.points ?: 0 }
                    )
                }

                _projects.value = processedProjects
            } catch (e: Exception) {
                // Handle the error gracefully, log it, or update UI state
                Log.e("TaskViewModel", "Error loading projects", e)
                // Optionally show error state to UI
            }
        }
    }

    fun getProjectById(projectId: String) {
        viewModelScope.launch {
           val result = getProjectByIdUseCase.execute(projectId)
            _projectById.value = ProjectUiModel(
                uid = result.uid,
                title = result.title,
                subtitle = result.subtitle,
                tasksList = result.tasksList,
                color = result.color,
                icon = result.icon
            )
        }
    }

    fun deleteProject(project: ProjectUiModel) {
        viewModelScope.launch {
            deleteProjectUseCase.execute(
                project.uid
            )
            _projects.value = _projects.value.filterNot { it.uid == project.uid }
        }
    }

    fun updateCompletedDatesForProject(projectId: String) {
        viewModelScope.launch {
            getAllTasks()
            val completed = _tasks.value.filter { task ->
                task.checked && task.project?.id == projectId && task.date != null
            }

            val grouped = completed.groupBy { it.date!! }
            _completedProjectDates.value = grouped
        }
    }

    fun updateGroupedTasksByDate(projectId: String) {
        viewModelScope.launch {
            getAllTasks() // Ensure _tasks is updated
            val grouped = _tasks.value
                .filter { it.project?.id == projectId && it.date != null }
                .groupBy { task ->
                    task.date!!.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                }
                .toSortedMap(reverseOrder())

            _groupedTasksByDate.value = grouped
        }
    }

    fun updatePointsForProject(projectId: String) {
        viewModelScope.launch {
            getAllTasks()

            val completed = _tasks.value.filter {
                it.checked && it.project?.id == projectId && it.points != null && it.date != null
            }

            // 🔢 Total points
            _totalPoints.value = completed.sumOf { it.points ?: 0 }

            // 📅 Monthly points
            val groupedByMonth = completed.groupBy { task ->
                task.date!!.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()
                    .let { YearMonth.from(it) }
            }

            _monthlyPoints.value = groupedByMonth.mapValues { (_, tasks) ->
                tasks.sumOf { it.points ?: 0 }
            }
        }
    }
}