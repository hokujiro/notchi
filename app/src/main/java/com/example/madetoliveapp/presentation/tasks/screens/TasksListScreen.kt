package com.example.madetoliveapp.presentation.tasks.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.madetoliveapp.domain.model.TaskModel
import com.example.madetoliveapp.presentation.tasks.TaskViewModel
import com.example.madetoliveapp.presentation.components.BottomNavigationBar
import com.example.madetoliveapp.presentation.projects.ProjectViewModel
import com.example.madetoliveapp.presentation.tasks.components.AddFailBottomSheet
import com.example.madetoliveapp.presentation.tasks.components.AddTaskBottomSheet
import com.example.madetoliveapp.presentation.tasks.components.CalendarHeader
import com.example.madetoliveapp.presentation.tasks.components.CircularFloatingMenu
import com.example.madetoliveapp.presentation.tasks.components.FiltersComponent
import com.example.madetoliveapp.presentation.tasks.components.HeaderComponent
import com.example.madetoliveapp.presentation.tasks.components.TaskComponent
import com.example.madetoliveapp.presentation.tasks.components.TaskEditBottomSheet
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDate
import java.time.ZoneId

@Composable
fun TasksScreen(taskViewModel: TaskViewModel = koinViewModel(), projectViewModel: ProjectViewModel = koinViewModel()) {

    var selectedDate by remember {
        mutableLongStateOf(
            LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        )
    }
    val tasks by taskViewModel.visibleTasks.collectAsState()
    val projects by projectViewModel.projects.collectAsState()
    val totalPoints by taskViewModel.totalPoints.collectAsState() // Make sure this exists!
    val taskFilter by taskViewModel.taskFilter.collectAsState() // Make sure this exists!
    val sortMode by taskViewModel.sortMode.collectAsState() // Make sure this exists!
    val dailyPoints by taskViewModel.dailyPoints.collectAsState()

    var taskToEdit by remember { mutableStateOf<TaskModel?>(null) }


    var currentSheet by remember { mutableStateOf(SheetType.NONE) }
    var isFabExpanded by remember { mutableStateOf(false) }


    LaunchedEffect(Unit) {
        taskViewModel.getTasksForDay(selectedDate)
    }
    LaunchedEffect(Unit) {
        taskViewModel.loadUserPoints()
    }
    LaunchedEffect(Unit) {
        projectViewModel.getAllProjects()
    }

    Scaffold(
        bottomBar = { BottomNavigationBar(selectedRoute = "daily") },
        floatingActionButton = {
            CircularFloatingMenu(
                isExpanded = isFabExpanded,
                onToggle = { isFabExpanded = !isFabExpanded },
                onActionClick = { index ->
                    isFabExpanded = false
                    currentSheet = when (index) {
                        0 -> SheetType.ADD_TASK
                        1 -> SheetType.ADD_FAIL
                        else -> SheetType.NONE
                    }
                }
            )
        }
    ) { paddingValues ->
        val outerScrollState = rememberScrollState()

        when (currentSheet) {
            SheetType.ADD_TASK -> {
                AddTaskBottomSheet(
                    onDismiss = { currentSheet = SheetType.NONE },
                    onAddTask = {
                        taskViewModel.addTask(it)
                        currentSheet = SheetType.NONE
                    },
                    selectedDate = selectedDate,
                    projects = projects
                )
            }

            SheetType.ADD_FAIL -> {
                AddFailBottomSheet(
                    onDismiss = { currentSheet = SheetType.NONE },
                    onAddTask = {
                        taskViewModel.addTask(it)
                        currentSheet = SheetType.NONE
                    },
                    selectedDate = selectedDate,
                    projects = projects
                )
            }

            SheetType.EDIT_TASK -> {
                taskToEdit?.let { task ->
                    TaskEditBottomSheet(
                        task = task,
                        projects = projects,
                        onDismiss = {
                            currentSheet = SheetType.NONE
                            taskToEdit = null
                        },
                        onSave = {
                            taskViewModel.updateTask(it)
                            currentSheet = SheetType.NONE
                            taskToEdit = null
                        }
                    )
                }
            }

            SheetType.NONE -> {}
        }

        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(paddingValues)
        ) {

            HeaderComponent(totalPoints, paddingValues, outerScrollState)

            CalendarHeader(
                modifier = Modifier.fillMaxWidth(),
                selectedDate = selectedDate,
                onDateSelected = { date ->
                    selectedDate = date
                    taskViewModel.getTasksForDay(date) // Fetch tasks for the selected date
                }
            )

            FiltersComponent(
                taskViewModel::setFilter,
                taskViewModel::toggleSortMode,
                sortMode,
                dailyPoints,
            )

            TaskComponent(
                tasks = tasks,
                onCheckClick = taskViewModel::toggleTaskCompletion,
                onDeleteTask = taskViewModel::deleteTask,
                onTaskClick = {
                    taskToEdit = it
                    currentSheet = SheetType.EDIT_TASK
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 500.dp)
            )

        }
    }
}

enum class SheetType { ADD_TASK, ADD_FAIL, EDIT_TASK, NONE }

