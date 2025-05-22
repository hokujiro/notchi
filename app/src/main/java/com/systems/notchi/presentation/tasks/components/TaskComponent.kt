package com.systems.notchi.presentation.tasks.components


import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.systems.notchi.domain.model.TaskModel
import com.systems.notchi.presentation.theme.FailTaskChecked
import com.systems.notchi.presentation.theme.FailTaskCheckedAccent
import com.systems.notchi.presentation.theme.FailTaskUnchecked
import com.systems.notchi.presentation.theme.FailTaskUncheckedAccent
import com.systems.notchi.presentation.theme.PositiveTaskChecked
import com.systems.notchi.presentation.theme.PositiveTaskCheckedAccent
import com.systems.notchi.presentation.theme.PositiveTaskUnchecked
import com.systems.notchi.presentation.theme.PositiveTaskUncheckedAccent
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
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .padding(WindowInsets.systemBars.asPaddingValues())
    ) {
        items(
            items = tasks,
            key = { task -> task.uid }
        ) { task ->
            val dismissState = rememberDismissState(
                confirmValueChange = {
                    when (it) {
                        DismissValue.DismissedToStart -> {
                            onDeleteTask(task)
                            true
                        }
                        else -> false
                    }
                }
            )

            SwipeToDismiss(
                modifier = Modifier.animateItemPlacement(),
                state = dismissState,
                directions = setOf(DismissDirection.EndToStart),
                background = { /* same background you already have */ },
                dismissContent = {
                    TaskItem(
                        task = task,
                        onCheckClick = onCheckClick,
                        onTaskClick = { clickedTask ->
                            if (selectionMode) {
                                onTaskLongClick(clickedTask) // behave like toggle when already selecting
                            } else {
                                onTaskClick(clickedTask)
                            }
                        },
                        onLongClick = { onTaskLongClick(task) }, // long press
                        isSelected = selectedTasks.contains(task.uid) // mark if selected
                    )
                }
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TaskItem(
    task: TaskModel,
    onCheckClick: (String) -> Unit,
    onTaskClick: (TaskModel) -> Unit,
    onLongClick: (TaskModel) -> Unit, // <-- Added
    isSelected: Boolean,              // <-- Added
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
            isLightsOff ->  FailTaskUnchecked
            isFailureAcknowledged -> FailTaskChecked
            !isChecked -> PositiveTaskUnchecked
            else -> PositiveTaskChecked
        },
        label = "BackgroundColor"
    )

    val borderColor by animateColorAsState(
        targetValue = when {
            isLightsOff ->  FailTaskUncheckedAccent      // Updated: Cool Beige
            isFailureAcknowledged -> FailTaskCheckedAccent
            !isChecked -> PositiveTaskUncheckedAccent
            else -> PositiveTaskCheckedAccent
        },
        label = "BorderColor"
    )

    val selectedBorderColor by animateColorAsState(
        targetValue = if (isSelected) borderColor.copy(alpha = 1f).compositeOver(Color.Black.copy(alpha = 0.2f)) else borderColor,
        label = "SelectedBorderColor"
    )

    val textColor =Color(0xFF212121)

    val pointsColor by animateColorAsState(
        targetValue = if (isChecked) textColor.copy(alpha = 0.7f) else textColor,
        label = "PointsColor"
    )

    val taskShape = PositiveTaskShape


    Card(
        modifier = modifier
            .fillMaxWidth()
            .offset(y = offsetY.value.dp)
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .border(1.dp, if (isSelected) selectedBorderColor else borderColor, if (!isFailure) taskShape else RoundedCornerShape(4.dp))
            .combinedClickable(
                onClick = {
                    coroutineScope.launch {
                        animateBounce()
                        onTaskClick(task)
                    }
                },
                onLongClick = {
                    onLongClick(task)
                }
            ),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color.LightGray else backgroundColor
        ),
        shape = if (!isFailure) taskShape else RoundedCornerShape(4.dp)
    ){
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                CircularCheckbox(
                    checked = isChecked,
                    onCheckedChange = { onCheckClick(task.uid) },
                    borderColor = borderColor,
                    fillColor = borderColor,
                    useCross = isFailure,
                )

                Spacer(modifier = Modifier.width(12.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = task.title,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                color = textColor
                            )
                        )

                        task.project?.icon?.takeIf { it.isNotBlank() }?.let { icon ->
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = icon,
                                    fontSize = 18.sp,
                                    color = textColor.copy(alpha = 0.85f)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = task.project.title,
                                    fontSize = 13.sp,
                                    color = textColor.copy(alpha = 0.65f)
                                )
                            }
                        }
                    }

                    val emphasizedPointsStyle = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = pointsColor
                        )

                    Text(
                        text = "$points ⭐",
                        style = emphasizedPointsStyle
                    )
                }
            }
        }
    }
}