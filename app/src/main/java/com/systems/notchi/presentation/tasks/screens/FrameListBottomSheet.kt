package com.systems.notchi.presentation.tasks.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.systems.notchi.domain.model.FrameModel
import com.systems.notchi.domain.model.TaskModel
import com.systems.notchi.presentation.projects.uimodel.ProjectUiModel
import com.systems.notchi.presentation.tasks.FrameViewModel
import com.systems.notchi.presentation.tasks.TaskViewModel
import com.systems.notchi.presentation.tasks.components.FailTaskShape
import com.systems.notchi.presentation.tasks.components.PositiveTaskShape
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FrameListBottomSheet(
    taskViewModel: TaskViewModel = koinViewModel(),
    frameViewModel: FrameViewModel = koinViewModel(),
    onDismiss: () -> Unit,
    selectedDate: Long,
    projects: List<ProjectUiModel> = listOf()
) {
    val frames by frameViewModel.frames.collectAsState()
    val selectionMode by frameViewModel.selectionMode.collectAsState()
    val selectedFrames by frameViewModel.selectedFrames.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    // 🧠 NEW: Local nested sheet state
    var isAddingFrame by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        frameViewModel.getAllFrames()
    }
    if (isAddingFrame) {
        AddFrameBottomSheet(
            onDismiss = {
                isAddingFrame = false
                frameViewModel.getAllFrames() // Reload frames after adding
            },
            onAddFrame = { frameModel ->
                coroutineScope.launch {
                    taskViewModel.addFrame(frameModel)
                    isAddingFrame = false
                    frameViewModel.getAllFrames()
                }
            },
            projects = projects,
        )
    } else {
        ModalBottomSheet(
            onDismissRequest = {
                frameViewModel.clearSelection()
                onDismiss()
            }
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                AnimatedVisibility(visible = selectionMode) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.Top
                    ) {
                        IconButton(
                            onClick = {
                                coroutineScope.launch {
                                    frameViewModel.deleteSelectedFrames()
                                    if (frameViewModel.frames.value.isEmpty()) {
                                        onDismiss()
                                    }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }

                        IconButton(
                            onClick = {
                                coroutineScope.launch {
                                    frameViewModel.addSelectedTasks(selectedDate)
                                    taskViewModel.getTasksForDay(selectedDate)
                                    onDismiss()
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Add,
                                contentDescription = "Add",
                                tint = Color(0xFF4CAF50)
                            )
                        }
                    }
                }

                Column {
                    TaskFrameHeader {
                        isAddingFrame = true // Open Add Frame mini sheet!
                    }

                    if (frames.isEmpty()) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "No frames yet.",
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            Text(
                                text = "Add a frame?",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                                modifier = Modifier.padding(bottom = 16.dp)
                            )
                            IconButton(
                                onClick = {
                                    isAddingFrame = true // Open Add Frame mini sheet!
                                },
                                modifier = Modifier
                                    .size(56.dp)
                                    .background(Color(0xFF4CAF50), CircleShape)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Add Frame",
                                    tint = Color.White,
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                        }
                    } else {
                        LazyColumn(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            items(frames, key = { it.uid }) { frame ->
                                val isSelected = selectedFrames.contains(frame.uid)

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center,
                                    modifier = Modifier
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    TaskFrameItem(
                                        frame = frame,
                                        isSelected = isSelected,
                                        onClick = {
                                            if (selectionMode) {
                                                frameViewModel.toggleSelection(frame.uid)
                                            } else {
                                                coroutineScope.launch {
                                                    val parsedDate = Date(selectedDate)
                                                    val task = TaskModel(
                                                        title = frame.title,
                                                        checked = false,
                                                        subTasks = emptyList(),
                                                        date = parsedDate,
                                                        points = frame.points,
                                                        project = frame.project
                                                    )
                                                    taskViewModel.addTask(task)
                                                    taskViewModel.getTasksForDay(selectedDate)
                                                    onDismiss()
                                                }
                                            }
                                        },
                                        onLongClick = {
                                            frameViewModel.enableSelectionMode()
                                            frameViewModel.toggleSelection(frame.uid)
                                        },
                                        selectionMode = selectionMode
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TaskFrameItem(
    frame: FrameModel,
    isSelected: Boolean,
    selectionMode: Boolean,
    onClick: () -> Unit,
    onLongClick: () -> Unit
) {
    val points = frame.points ?: 0
    val isFailure = points < 0

    val backgroundColor by animateColorAsState(
        targetValue = when {
            isFailure -> Color(0xFFE7DED6)
            else -> Color(0xFFF8F9FA)
        },
        label = "BackgroundColor"
    )

    val borderColor by animateColorAsState(
        targetValue = when {
            isFailure -> Color(0xFFC9B8A7)
            else -> Color(0xFFE0E0E0)
        },
        label = "BorderColor"
    )

    val textColor by animateColorAsState(
        targetValue = when {
            isFailure -> Color(0xFF4B3A34)
            else -> Color(0xFF212121)
        },
        label = "TextColor"
    )

    val pointsColor by animateColorAsState(
        targetValue = textColor,
        label = "PointsColor"
    )

    val taskShape = if (isFailure) FailTaskShape else PositiveTaskShape

    Card(
        modifier = Modifier
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            )
            .border(1.dp, borderColor, taskShape),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = if (isSelected) Color.LightGray else backgroundColor),
        shape = taskShape
    ){
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = frame.title,
                        style = MaterialTheme.typography.bodyLarge.copy(color = textColor)
                    )

                    frame.project?.icon?.takeIf { it.isNotBlank() }?.let { icon ->
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = icon,
                                fontSize = 18.sp,
                                color = textColor.copy(alpha = 0.85f)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = frame.project.title,
                                fontSize = 13.sp,
                                color = textColor.copy(alpha = 0.65f)
                            )
                        }
                    }
                }

                Column(horizontalAlignment = Alignment.End) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Task",
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = "${frame.points} ⭐",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = pointsColor
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun TaskFrameHeader(onAddClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Choose a Task Frame",
            style = MaterialTheme.typography.titleLarge
        )
        IconButton(onClick = onAddClick) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add Frame",
                tint = Color(0xFF4CAF50) // Material green 500
            )
        }
    }
}
