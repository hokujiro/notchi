package com.systems.notchi.presentation.projects.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.systems.notchi.domain.model.TaskModel
import com.systems.notchi.presentation.projects.ProjectViewModel
import com.systems.notchi.presentation.projects.components.AddProjectTaskBottomSheet
import com.systems.notchi.presentation.projects.components.ProjectCalendar
import com.systems.notchi.presentation.tasks.TaskViewModel
import com.systems.notchi.presentation.projects.components.CompactSegmentedButtonBar
import com.systems.notchi.presentation.projects.components.ProjectTaskEditBottomSheet
import com.systems.notchi.presentation.projects.components.ProjectTaskItem
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import java.time.YearMonth
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ProjectDetailScreen(
    projectId: String,
    navController: NavController, // Add this parameter
    projectViewModel: ProjectViewModel = koinViewModel(),
    taskViewModel: TaskViewModel = koinViewModel() // or hiltViewModel()
) {
    val project by projectViewModel.projectById.collectAsState()
    val taskList by taskViewModel.tasks.collectAsState()
    val filteredTasks = taskList.filter { it.project?.id == projectId }
    val selectedSegment = remember { mutableIntStateOf(0) }
    var currentSheet by remember { mutableStateOf(TaskSheetType.NONE) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        projectViewModel.getProjectById(projectId)
    }

    LaunchedEffect(Unit) {
        taskViewModel.getAllTasks()
    }

    LaunchedEffect(projectId) {
        projectViewModel.updateCompletedDatesForProject(projectId)
    }

    LaunchedEffect(projectId) {
        projectViewModel.updateGroupedTasksByDate(projectId)
    }

    LaunchedEffect(projectId) {
        projectViewModel.updatePointsForProject(projectId)
    }

    val groupedTasks by projectViewModel.groupedTasksByDate.collectAsState()
    val dateFormatter = remember {
        DateTimeFormatter.ofPattern("EEEE, dd MMMM", Locale.getDefault())
    }
    var taskToEdit by remember { mutableStateOf<TaskModel?>(null) }

    val totalPoints by projectViewModel.totalPoints.collectAsState()

    val completedDatesMap by projectViewModel.completedProjectDates.collectAsState()
    val completedDates = completedDatesMap.keys.map {
        it.toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDate() // or use DateTimeFormatter if needed
    }.toSet()
    val currentMonth = remember { mutableStateOf(YearMonth.now()) }

    when (currentSheet) {
        TaskSheetType.ADD_TASK -> {
            AddProjectTaskBottomSheet(
                onDismiss = { currentSheet = TaskSheetType.NONE },
                onAddTask = { newTask ->
                    coroutineScope.launch {
                        taskViewModel.addTask(newTask)
                        currentSheet = TaskSheetType.NONE
                    }
                },
                project = projectId
            )
        }

        TaskSheetType.EDIT_TASK -> {
            taskToEdit?.let { task ->
                ProjectTaskEditBottomSheet(
                    task = task,
                    project = project,
                    onDismiss = {
                        currentSheet = TaskSheetType.NONE
                        taskToEdit = null
                    },
                    onSave = {
                        coroutineScope.launch {
                            taskViewModel.updateTask(it)
                            currentSheet = TaskSheetType.NONE
                            taskToEdit = null
                        }
                    }
                )
            }
        }

        TaskSheetType.NONE -> {}
        TaskSheetType.ADD_FAIL -> {}
        TaskSheetType.ADD_FRAME -> {}
    }


    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = project.icon,
                            fontSize = 24.sp
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = project.title,
                            style = MaterialTheme.typography.headlineSmall
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )

        }
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "$totalPoints ⭐",
                        style = MaterialTheme.typography.headlineSmall
                    )

                    CompactSegmentedButtonBar(
                        selectedIndex = selectedSegment.intValue,
                        onSegmentSelected = { selectedSegment.intValue = it }
                    )
                }
            }

            when (selectedSegment.intValue) {
                0 -> {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(445.dp)
                        ) {
                            ProjectCalendar(
                                yearMonth = currentMonth.value,
                                completedDates = completedDates,
                                icon = project.icon,
                                onMonthChange = { newMonth -> currentMonth.value = newMonth }
                            )
                        }
                    }
                }

                1 -> {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = project.icon,
                                    fontSize = 24.sp
                                )
                                Spacer(modifier = Modifier.width(16.dp))
                                Text(
                                    text = project.title,
                                    style = MaterialTheme.typography.headlineSmall
                                )
                            }
                            Text(
                                text = project.subtitle,
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }
                }
            }

            item {

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Tasks",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(onClick = { currentSheet = TaskSheetType.ADD_TASK }) {
                        Icon(Icons.Default.Add, contentDescription = "Add Task")
                    }
                }
            }

            if (filteredTasks.isEmpty()) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "No tasks yet!",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(vertical = 16.dp)
                        )
                    }
                }
            }

            groupedTasks.forEach { (date, tasksForDate) ->
                item {
                    Text(
                        text = date.format(dateFormatter),
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier.padding(top = 16.dp, bottom = 4.dp)
                    )
                }

                items(tasksForDate) { task ->
                    ProjectTaskItem(
                        task = task,
                        onCheckClick = { taskId ->
                            coroutineScope.launch {
                                taskViewModel.toggleTaskCompletion(taskId)
                                projectViewModel.updateGroupedTasksByDate(projectId)
                            }
                        },
                        onTaskClick = { taskModel ->
                            taskToEdit = taskModel
                            currentSheet = TaskSheetType.EDIT_TASK
                        },
                        onLongClick = { taskModel ->
                            taskViewModel.enableSelectionMode()
                            taskViewModel.toggleTaskSelection(taskModel.uid)
                        },
                        isSelected = taskViewModel.selectedTasks.collectAsState().value.contains(
                            task.uid
                        )
                    )
                }
            }
        }
    }
}

enum class TaskSheetType { ADD_TASK, ADD_FAIL, EDIT_TASK, ADD_FRAME, NONE }


