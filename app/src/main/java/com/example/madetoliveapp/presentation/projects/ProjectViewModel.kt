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

class ProjectViewModel(
    private val addProjectUseCase: AddProjectUseCase,
    private val getAllProjectsUseCase: GetProjectsUseCase,
    private val getProjectByIdUseCase: GetProjectByIdUseCase,
    private val deleteProjectUseCase: DeleteProjectUseCase,
) : ViewModel() {

    private val _projects = MutableStateFlow<List<ProjectUiModel>>(emptyList())
    val projects: StateFlow<List<ProjectUiModel>> = _projects.asStateFlow()

    private val _projectById = MutableStateFlow<ProjectUiModel>(ProjectUiModel())
    val projectById: StateFlow<ProjectUiModel> = _projectById.asStateFlow()

    fun addProject(project: ProjectUiModel) {
        viewModelScope.launch {
            addProjectUseCase.execute(
                ProjectModel(
                    uid = project.uid,
                    title = project.title,
                    tasksList = project.tasksList,
                    color = project.color,
                )
            )
        }
        _projects.value += project
    }

    fun getAllProjects() {
        viewModelScope.launch {
            try {
                val result = getAllProjectsUseCase.execute()

                val processedProjects: List<ProjectUiModel> = result.map { project ->
                    val tasks = project.tasksList ?: emptyList()
                    ProjectUiModel(
                        uid = project.uid,
                        title = project.title,
                        tasksList = tasks,
                        color = project.color,
                        icon = project.icon,
                        totalTasks = tasks.size,
                        completedTasks = tasks.count { it.checked }
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
                color = result.color
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
}