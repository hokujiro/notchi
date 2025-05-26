package com.systems.notchi.presentation.tasks.components


import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.systems.notchi.domain.model.TaskModel
import com.systems.notchi.presentation.tasks.TaskFilter
import com.systems.notchi.presentation.theme.BubbleShapeNegative
import com.systems.notchi.presentation.theme.BubbleShapePositive
import com.systems.notchi.presentation.theme.DarkText
import com.systems.notchi.presentation.theme.ErrorTextCool
import com.systems.notchi.presentation.theme.FailTaskChecked
import com.systems.notchi.presentation.theme.FailTaskCheckedAccent
import com.systems.notchi.presentation.theme.FailTaskUnchecked
import com.systems.notchi.presentation.theme.FailTaskUncheckedAccent
import com.systems.notchi.presentation.theme.PositiveTaskChecked
import com.systems.notchi.presentation.theme.PositiveTaskCheckedAccent
import com.systems.notchi.presentation.theme.PositiveTaskUnchecked
import com.systems.notchi.presentation.theme.PositiveTaskUncheckedAccent
import com.systems.notchi.presentation.theme.SoftError
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun TaskComponent(
    tasks: List<TaskModel>,
    onCheckClick: (String) -> Unit,
    onDeleteTask: (TaskModel) -> Unit,
    onTaskClick: (TaskModel) -> Unit,
    onTaskLongClick: (TaskModel) -> Unit,
    selectedTasks: Set<String>,
    selectionMode: Boolean,
    selectedFilter: TaskFilter, // 👈 new param
    modifier: Modifier = Modifier
) {
    val positiveTasks = tasks.filter { (it.points ?: 0) >= 0 }
    val uncheckedNegativeTasks = tasks.filter { (it.points ?: 0) < 0 && !it.checked }

    val checkedNegativeTasks = tasks.filter {
        val points = it.points ?: 0
        points < 0 && it.checked
    }

    val showNegativeSection = checkedNegativeTasks.isNotEmpty()
    val negativePoints = checkedNegativeTasks.sumOf { it.points ?: 0 }

    LazyColumn(
        modifier = modifier
            .padding(WindowInsets.systemBars.asPaddingValues())
    ) {
        // Positive Tasks
        items(
            positiveTasks, key = { it.uid }) { task ->
            val dismissState = rememberDismissState(
                confirmValueChange = {
                    if (it == DismissValue.DismissedToStart) {
                        onDeleteTask(task)
                        true
                    } else false
                }
            )

            SwipeToDismiss(
                modifier = Modifier.animateItemPlacement(),
                state = dismissState,
                directions = setOf(DismissDirection.EndToStart),
                background = {
                    val isSwiping = dismissState.dismissDirection == DismissDirection.EndToStart
                    if (isSwiping) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 4.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(SoftError)
                                .fillMaxHeight(),
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            Text(
                                text = "Delete",
                                color = ErrorTextCool,
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(end = 20.dp)
                            )
                        }
                    }
                },
                dismissContent = {
                    TaskItem(
                        task = task,
                        onCheckClick = onCheckClick,
                        onTaskClick = { clickedTask ->
                            if (selectionMode) {
                                onTaskLongClick(clickedTask)
                            } else {
                                onTaskClick(clickedTask)
                            }
                        },
                        onLongClick = { onTaskLongClick(task) },
                        isSelected = selectedTasks.contains(task.uid)
                    )
                }
            )
        }

        if (uncheckedNegativeTasks.isNotEmpty()) {
            item {
                Spacer(modifier = Modifier.height(12.dp))
            }
        }

        items(uncheckedNegativeTasks, key = { it.uid }) { task ->
            val dismissState = rememberDismissState(
                confirmValueChange = {
                    if (it == DismissValue.DismissedToStart) {
                        onDeleteTask(task)
                        true
                    } else false
                }
            )

            SwipeToDismiss(
                modifier = Modifier.animateItemPlacement(),
                state = dismissState,
                directions = setOf(DismissDirection.EndToStart),
                background = {
                    val isSwiping = dismissState.dismissDirection == DismissDirection.EndToStart
                    if (isSwiping) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 4.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(SoftError)
                                .fillMaxHeight(),
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            Text(
                                text = "Delete",
                                color = ErrorTextCool,
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(end = 20.dp)
                            )
                        }
                    }
                },
                dismissContent = {
                    TaskItem(
                        task = task,
                        onCheckClick = onCheckClick,
                        onTaskClick = { clickedTask ->
                            if (selectionMode) {
                                onTaskLongClick(clickedTask)
                            } else {
                                onTaskClick(clickedTask)
                            }
                        },
                        onLongClick = { onTaskLongClick(task) },
                        isSelected = selectedTasks.contains(task.uid)
                    )
                }
            )
        }

        if (showNegativeSection) {
            if(selectedFilter == TaskFilter.ALL) {

                item {
                    Spacer(modifier = Modifier.height(24.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "⭐ $negativePoints points lost",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = DarkText
                            )
                        )
                    }
                }

                item { Spacer(modifier = Modifier.height(8.dp)) }
            }

            items(
                items = checkedNegativeTasks,
                key = { it.uid }
            ) { task ->
                val dismissState = rememberDismissState(
                    confirmValueChange = {
                        if (it == DismissValue.DismissedToStart) {
                            onDeleteTask(task)
                            true
                        } else false
                    }
                )

                SwipeToDismiss(
                    modifier = Modifier.animateItemPlacement(),
                    state = dismissState,
                    directions = setOf(DismissDirection.EndToStart),
                    background = {
                        val isSwiping = dismissState.dismissDirection == DismissDirection.EndToStart
                        if (isSwiping) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 4.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(SoftError)
                                    .fillMaxHeight(),
                                contentAlignment = Alignment.CenterEnd
                            ) {
                                Text(
                                    text = "Delete",
                                    color = ErrorTextCool,
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier.padding(end = 20.dp)
                                )
                            }
                        }
                    },
                    dismissContent = {
                        TaskItem(
                            task = task,
                            onCheckClick = onCheckClick,
                            onTaskClick = { clickedTask ->
                                if (selectionMode) {
                                    onTaskLongClick(clickedTask)
                                } else {
                                    onTaskClick(clickedTask)
                                }
                            },
                            onLongClick = { onTaskLongClick(task) },
                            isSelected = selectedTasks.contains(task.uid)
                        )
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TaskItem(
    task: TaskModel,
    onCheckClick: (String) -> Unit,
    onTaskClick: (TaskModel) -> Unit,
    onLongClick: (TaskModel) -> Unit,
    isSelected: Boolean,
    modifier: Modifier = Modifier
) {
    val offsetY = remember { androidx.compose.animation.core.Animatable(0f) }
    val coroutineScope = rememberCoroutineScope()

    val points = task.points ?: 0
    val isChecked = task.checked
    val isFailure = points < 0
    val isLightsOff = isFailure && !isChecked
    val isFailureAcknowledged = isFailure && isChecked

    suspend fun animateBounce() {
        offsetY.snapTo(0f)
        offsetY.animateTo(
            targetValue = -6f,
            animationSpec = tween(durationMillis = 80, easing = FastOutLinearInEasing)
        )
        offsetY.animateTo(
            targetValue = 0f,
            animationSpec = tween(durationMillis = 100, easing = LinearOutSlowInEasing)
        )
    }

    val backgroundColor by animateColorAsState(
        targetValue = when {
            isLightsOff -> FailTaskUnchecked
            isFailureAcknowledged -> FailTaskChecked
            !isChecked -> PositiveTaskUnchecked
            else -> PositiveTaskChecked
        },
        label = "BackgroundColor"
    )

    val borderColor by animateColorAsState(
        targetValue = when {
            isLightsOff -> FailTaskUncheckedAccent
            isFailureAcknowledged -> FailTaskCheckedAccent
            !isChecked -> PositiveTaskUncheckedAccent
            else -> PositiveTaskCheckedAccent
        },
        label = "BorderColor"
    )

    val selectedBorderColor by animateColorAsState(
        targetValue = if (isSelected) borderColor.copy(alpha = 1f)
            .compositeOver(Color.Black.copy(alpha = 0.2f)) else borderColor,
        label = "SelectedBorderColor"
    )

    val pointsColor by animateColorAsState(
        targetValue = if (isChecked) Color(0xFF212121).copy(alpha = 0.7f) else Color(0xFF212121),
        label = "PointsColor"
    )
    val alignment = if (isFailure) Arrangement.Start else Arrangement.End

    val horizontalBias = if (isFailure) 0f else 1f // left or right alignment
    val bubbleShape = if (isFailure) {
        RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomEnd = 16.dp, bottomStart = 0.dp)
    } else {
        RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomEnd = 0.dp, bottomStart = 16.dp)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalArrangement = if (isFailure) Arrangement.Start else Arrangement.End
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.97f)
                .offset(y = offsetY.value.dp)
                .border(
                    1.dp,
                    if (isSelected) selectedBorderColor else borderColor,
                    bubbleShape
                )
                .combinedClickable(
                    onClick = {
                        coroutineScope.launch {
                            animateBounce()
                            onTaskClick(task)
                        }
                    },
                    onLongClick = { onLongClick(task) }
                ),
            elevation = CardDefaults.cardElevation(2.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isSelected) Color.LightGray else backgroundColor
            ),
            shape = bubbleShape
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    CircularCheckbox(
                        checked = isChecked,
                        onCheckedChange = { onCheckClick(task.uid) },
                        borderColor = borderColor,
                        fillColor = borderColor,
                        useCross = isFailure,
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = task.title,
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    color = Color(
                                        0xFF212121
                                    )
                                )
                            )

                            task.project?.icon?.takeIf { it.isNotBlank() }?.let { icon ->
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = icon,
                                        fontSize = 18.sp,
                                        color = Color(0xFF212121).copy(alpha = 0.85f)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = task.project.title,
                                        fontSize = 13.sp,
                                        color = Color(0xFF212121).copy(alpha = 0.65f)
                                    )
                                }
                            }
                        }

                        Text(
                            text = "$points ⭐",
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
}