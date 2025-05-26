package com.systems.notchi.presentation.tasks.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.systems.notchi.R
import com.systems.notchi.domain.model.TaskModel
import com.systems.notchi.presentation.tasks.TaskViewModel
import com.systems.notchi.presentation.components.BottomNavigationBar
import com.systems.notchi.presentation.projects.ProjectViewModel
import com.systems.notchi.presentation.tasks.TaskFilter
import com.systems.notchi.presentation.tasks.components.CalendarHeader
import com.systems.notchi.presentation.tasks.components.CircularFloatingMenu
import com.systems.notchi.presentation.tasks.components.FiltersComponent
import com.systems.notchi.presentation.tasks.components.HeaderComponent
import com.systems.notchi.presentation.tasks.components.TaskComponent
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDate
import java.time.ZoneId

@Composable
fun TasksScreen(
    openDrawer: () -> Unit,
    taskViewModel: TaskViewModel = koinViewModel(),
    projectViewModel: ProjectViewModel = koinViewModel(),
) {
    var selectedDate by remember {
        mutableLongStateOf(
            LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        )
    }
    val tasks by taskViewModel.visibleTasks.collectAsState()
    val projects by projectViewModel.projects.collectAsState()
    val totalPoints by taskViewModel.totalPoints.collectAsState()
    val sortMode by taskViewModel.sortMode.collectAsState()
    val dailyPoints by taskViewModel.dailyPoints.collectAsState()

    var taskToEdit by remember { mutableStateOf<TaskModel?>(null) }
    var currentSheet by remember { mutableStateOf(SheetType.NONE) }
    var isFabExpanded by remember { mutableStateOf(false) }

    var selectedFilter by remember { mutableStateOf(TaskFilter.ALL) }

    val weeklyPointsMap by taskViewModel.weeklyPointsMap.collectAsState()


    val coroutineScope = rememberCoroutineScope()

    // Launch initial loads
    LaunchedEffect(Unit) {
        taskViewModel.getTasksForDay(selectedDate)
        taskViewModel.loadUserPoints()
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
                            2 -> SheetType.ADD_FRAME
                            else -> SheetType.NONE
                        }
                    }
                )
            }
        ) { paddingValues ->

            when (currentSheet) {
                SheetType.ADD_TASK -> {
                    AddTaskBottomSheet(
                        onDismiss = { currentSheet = SheetType.NONE },
                        onAddTask = {
                            coroutineScope.launch {
                                taskViewModel.addTask(it)
                                currentSheet = SheetType.NONE
                            }
                        },
                        selectedDate = selectedDate,
                        projects = projects,
                        onAddFrame = {
                            coroutineScope.launch {
                                taskViewModel.addFrame(it)
                            }
                        }
                    )
                }

                SheetType.ADD_FAIL -> {
                    AddFailBottomSheet(
                        onDismiss = { currentSheet = SheetType.NONE },
                        onAddTask = {
                            coroutineScope.launch {
                                taskViewModel.addTask(it)
                                currentSheet = SheetType.NONE
                            }
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
                                coroutineScope.launch {
                                    taskViewModel.updateTask(it)
                                    currentSheet = SheetType.NONE
                                    taskToEdit = null
                                }
                            }
                        )
                    }
                }

                SheetType.ADD_FRAME -> {
                    FrameListBottomSheet(
                        projects = projects,
                        onDismiss = {
                            currentSheet = SheetType.NONE
                            taskToEdit = null
                        },
                        selectedDate = selectedDate,
                    )
                }

                SheetType.NONE -> {}
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()  // <-- important!!!
                    .padding(paddingValues)
            ) {
                HeaderComponent(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 100.dp),
                    totalPoints = totalPoints,
                    onProfileMenuClick = openDrawer
                )

                CalendarHeader(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 150.dp),
                    selectedDate = selectedDate,
                    onDateSelected = { date ->
                        selectedDate = date
                        coroutineScope.launch {
                            taskViewModel.getTasksForDay(date)
                        }
                    },
                    dailyPointsMap = weeklyPointsMap,
                    onVisibleWeekChange = { start, end ->
                        coroutineScope.launch {
                            taskViewModel.loadTasksForWeek(start, end)
                        }
                    }
                )

                if (taskViewModel.selectionMode.collectAsState().value) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = {
                                coroutineScope.launch {
                                    taskViewModel.deleteSelectedTasks()
                                }
                            },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete Selected",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                        IconButton(
                            onClick = {
                                coroutineScope.launch {
                                    taskViewModel.saveSelectedTasksAsFrames()
                                }
                            },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Add,
                                contentDescription = "Save Selected as Frames",
                                tint = Color(0xFF4CAF50),
                            )
                        }
                    }
                } else {
                    val filteredPoints = if (selectedFilter == TaskFilter.ALL) {
                        tasks.filter { (it.points ?: 0) > 0 && it.checked }.sumOf { it.points ?: 0 }
                    } else {
                        dailyPoints
                    }
                    FiltersComponent(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth()
                            .heightIn(max = 80.dp),
                        selectedFilter,
                        onFilterClick = { filter ->
                            selectedFilter = filter
                            taskViewModel.setFilter(filter)
                        },
                        taskViewModel::toggleSortMode,
                        sortMode,
                        filteredPoints,
                    )
                }

                if (tasks.isEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.no_tasks), // 🔁 Replace with your drawable
                            contentDescription = "No tasks",
                            modifier = Modifier.size(300.dp)
                        )
                        Text(
                            text = "No tasks yet for today,\nadd your first one!",
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontSize = 22.sp, // 👈 increase this as needed
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        )
                    }
                } else {
                    Spacer(modifier = Modifier.height(12.dp))
                    TaskComponent(
                        tasks = tasks,
                        onCheckClick = { taskId ->
                            coroutineScope.launch { taskViewModel.toggleTaskCompletion(taskId) }
                        },
                        onDeleteTask = { task -> coroutineScope.launch { taskViewModel.deleteTask(task) } },
                        onTaskClick = { task ->
                            taskToEdit = task
                            currentSheet = SheetType.EDIT_TASK
                        },
                        onTaskLongClick = { task ->
                            taskViewModel.enableSelectionMode()
                            taskViewModel.toggleTaskSelection(task.uid)
                        },
                        selectedTasks = taskViewModel.selectedTasks.collectAsState().value,
                        selectionMode = taskViewModel.selectionMode.collectAsState().value,
                        selectedFilter = selectedFilter,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    )
                }
            }
        }
    }


enum class SheetType { ADD_TASK, ADD_FAIL, EDIT_TASK, ADD_FRAME, NONE }
