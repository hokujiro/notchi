package com.example.madetoliveapp.presentation.rewards.screens

import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.example.madetoliveapp.domain.model.TaskModel
import com.example.madetoliveapp.presentation.TaskViewModel
import com.example.madetoliveapp.presentation.components.AddTaskDialog
import com.example.madetoliveapp.presentation.components.BottomNavigationBar
import com.example.madetoliveapp.presentation.components.CalendarHeader
import com.example.madetoliveapp.presentation.components.TaskComponent
import com.example.madetoliveapp.presentation.extensions.toLocalDate
import kotlinx.coroutines.coroutineScope
import org.koin.androidx.compose.koinViewModel
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date

@Composable
fun RewardsScreen(taskViewModel: TaskViewModel = koinViewModel()) {

    var selectedDate by remember {
        mutableLongStateOf(
            LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        )
    }
    // Obtener las tareas desde el ViewModel
    val tasks by taskViewModel.tasks.collectAsState()
    val dateFormat = SimpleDateFormat("d-MM-yyyy")
    val openDialog = remember { mutableStateOf(false) } // State to control dialog visibility


    LaunchedEffect(Unit) {
        taskViewModel.getTasksForDay(selectedDate)
    }

    Scaffold(
        bottomBar = { BottomNavigationBar(selectedRoute = "rewards") },
        floatingActionButton = {
            FloatingActionButton(
                onClick =  { openDialog.value = true },
                modifier = Modifier.padding(16.dp) // Adjust padding to prevent overlap
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar tarea")
            }
        }
    ) { paddingValues ->
        val outerScrollState = rememberScrollState()
        if (openDialog.value) {
            AddTaskDialog(
                onDismiss = { openDialog.value = false },
                onAddTask = { newTask ->
                    taskViewModel.addTask(newTask)
                    openDialog.value = false
                },
                selectedDate = selectedDate
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(outerScrollState)
                .pointerInput(Unit) {
                    detectSingleHorizontalSwipe { isSwipeLeft ->
                        val currentDate = LocalDate.ofEpochDay(selectedDate / (24 * 60 * 60 * 1000))
                        val newDate = if (isSwipeLeft) {
                            currentDate.plusDays(1)
                        } else {
                            currentDate.minusDays(1)
                        }
                        selectedDate = newDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
                        taskViewModel.getTasksForDay(selectedDate)
                    }
                }

        ) {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text(text = "Rewards Screen", style = MaterialTheme.typography.h4)
            }
            CalendarHeader(
                modifier = Modifier.fillMaxWidth(),
                selectedDate = selectedDate,
                onDateSelected = { date ->
                    selectedDate = date
                    taskViewModel.getTasksForDay(date) // Fetch tasks for the selected date
                }
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

suspend fun PointerInputScope.detectSingleHorizontalSwipe(onSwipe: (isSwipeLeft: Boolean) -> Unit) {
    var direction = true
    detectHorizontalDragGestures(
        onDragEnd = {
            // Trigger the swipe callback once the drag ends
            onSwipe(direction) // Call the callback (true for left swipe, false for right swipe)
        },
        onHorizontalDrag = { change, dragAmount ->
            change.consume()
            if (dragAmount < 0) {
                direction = true// Swipe left
            } else if (dragAmount > 0) {
                direction = false // Swipe right
            }// Consume gesture changes
        }
    )
}





