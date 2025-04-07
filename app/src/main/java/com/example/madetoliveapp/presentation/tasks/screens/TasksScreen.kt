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
import com.example.madetoliveapp.presentation.tasks.TaskViewModel
import com.example.madetoliveapp.presentation.tasks.components.AddTaskDialog
import com.example.madetoliveapp.presentation.components.BottomNavigationBar
import com.example.madetoliveapp.presentation.tasks.components.AddTaskBottomSheet
import com.example.madetoliveapp.presentation.tasks.components.CalendarHeader
import com.example.madetoliveapp.presentation.tasks.components.TaskComponent
import com.example.madetoliveapp.presentation.tasks.components.CircularFloatingMenu
import com.example.madetoliveapp.presentation.tasks.components.FiltersComponent
import com.example.madetoliveapp.presentation.tasks.components.HeaderComponent
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDate
import java.time.ZoneId

@Composable
fun TasksScreen(taskViewModel: TaskViewModel = koinViewModel()) {

    var selectedDate by remember {
        mutableLongStateOf(
            LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        )
    }
    // Obtener las tareas desde el ViewModel
    val tasks by taskViewModel.visibleTasks.collectAsState()
    val projects by taskViewModel.projects.collectAsState()
    val totalPoints by taskViewModel.totalPoints.collectAsState() // Make sure this exists!
    val taskFilter by taskViewModel.taskFilter.collectAsState() // Make sure this exists!
    val sortMode by taskViewModel.sortMode.collectAsState() // Make sure this exists!
    val dailyPoints by taskViewModel.dailyPoints.collectAsState()

    var openCreateTaskDialog =
        remember { mutableStateOf(false) } // State to control dialog visibility
    var isFabExpanded by remember { mutableStateOf(false) }


    LaunchedEffect(Unit) {
        taskViewModel.getTasksForDay(selectedDate)
    }
    LaunchedEffect(Unit) {
        taskViewModel.loadUserPoints()
    }

    Scaffold(
        bottomBar = { BottomNavigationBar(selectedRoute = "daily") },
        floatingActionButton = {
            CircularFloatingMenu(
                isExpanded = isFabExpanded,
                onToggle = { isFabExpanded = !isFabExpanded },
                onActionClick = { index ->
                    isFabExpanded = false
                    when (index) {
                        0 -> {
                            openCreateTaskDialog.value = true
                        } // Add Task
                        1 -> {/* handle add project */
                        }

                        2 -> {/* handle add habit */
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        val outerScrollState = rememberScrollState()
        if (openCreateTaskDialog.value) {
            AddTaskBottomSheet(
                onDismiss = { openCreateTaskDialog.value = false },
                onAddTask = { newTask ->
                    taskViewModel.addTask(newTask)
                    openCreateTaskDialog.value = false
                },
                selectedDate = selectedDate,
                projects = projects
            )
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
                onTaskClick = taskViewModel::toggleTaskCompletion,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 500.dp)
            )

        }
    }
}





