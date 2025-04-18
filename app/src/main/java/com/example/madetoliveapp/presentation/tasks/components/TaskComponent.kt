package com.example.madetoliveapp.presentation.tasks.components


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SentimentVeryDissatisfied
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.madetoliveapp.domain.model.TaskModel

@Composable
fun TaskComponent(
    tasks: List<TaskModel>,
    onTaskClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .padding(WindowInsets.systemBars.asPaddingValues()) // Account for system bars
    ) {
        items(
            items = tasks,
            key = { it.uid } // stable identity to prevent ghost animations
        ) { task ->
            TaskItem(
                task = task,
                onTaskClick = onTaskClick,
                modifier = Modifier.animateItem(fadeInSpec = null, fadeOutSpec = null)
            )
        }
    }
}

object PositiveTaskShape : Shape {
    override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density): Outline {
        val radius = 28f
        val tabHeight = 16f
        val tabWidth = 60f

        return Outline.Generic(
            Path().apply {
                moveTo(radius, 0f)

                // Top edge and tab
                lineTo(size.width - tabWidth - 12f, 0f)
                lineTo(size.width - tabWidth, tabHeight)
                lineTo(size.width, tabHeight)
                lineTo(size.width, size.height - radius)

                // Bottom-right arc
                arcTo(
                    rect = Rect(size.width - 2 * radius, size.height - 2 * radius, size.width, size.height),
                    startAngleDegrees = 0f,
                    sweepAngleDegrees = 90f,
                    forceMoveTo = false
                )

                // Bottom-left arc
                lineTo(radius, size.height)
                arcTo(
                    rect = Rect(0f, size.height - 2 * radius, 2 * radius, size.height),
                    startAngleDegrees = 90f,
                    sweepAngleDegrees = 90f,
                    forceMoveTo = false
                )

                // Top-left arc
                lineTo(0f, radius)
                arcTo(
                    rect = Rect(0f, 0f, radius * 2, radius * 2),
                    startAngleDegrees = 180f,
                    sweepAngleDegrees = 90f,
                    forceMoveTo = false
                )

                close()
            }
        )
    }
}
object FailTaskShape : Shape {
    override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density): Outline {
        val radius = 24f
        val tipOffset = 18f
        val liftHeight = size.height * 0.25f

        return Outline.Generic(
            Path().apply {
                moveTo(radius, 0f)
                lineTo(size.width - tipOffset, 0f)
                lineTo(size.width, liftHeight)
                lineTo(size.width - tipOffset, size.height)

                // Bottom edge
                lineTo(radius, size.height)

                // Bottom-left arc
                arcTo(
                    rect = Rect(0f, size.height - 2 * radius, 2 * radius, size.height),
                    startAngleDegrees = 90f,
                    sweepAngleDegrees = 90f,
                    forceMoveTo = false
                )

                // Top-left arc
                lineTo(0f, radius)
                arcTo(
                    rect = Rect(0f, 0f, 2 * radius, 2 * radius),
                    startAngleDegrees = 180f,
                    sweepAngleDegrees = 90f,
                    forceMoveTo = false
                )

                close()
            }
        )
    }
}

@Composable
fun TaskItem(
    task: TaskModel,
    onTaskClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val points = task.points ?: 0
    val isChecked = task.checked
    val isFailure = points < 0

    val isLightsOff = isFailure && !isChecked
    val isFailureAcknowledged = isFailure && isChecked

    val backgroundColor by animateColorAsState(
        targetValue = when {
            isLightsOff -> Color(0xFFE7DED6)
            isFailureAcknowledged -> Color(0xFFFCE8E8)
            !isChecked -> Color(0xFFF8F9FA)
            else -> Color(0xFFE9F5EC)
        },
        label = "BackgroundColor"
    )

    val borderColor by animateColorAsState(
        targetValue = when {
            isLightsOff -> Color(0xFFC9B8A7)          // Updated: Cool Beige
            isFailureAcknowledged -> Color(0xFFDB6A6A)
            !isChecked -> Color(0xFFE0E0E0)
            else -> Color(0xFFAED9C5)
        },
        label = "BorderColor"
    )

    val textColor by animateColorAsState(
        targetValue = when {
            isLightsOff -> Color(0xFF4B3A34)          // Updated: Cocoa
            isFailureAcknowledged -> Color(0xFF7A1F1F)
            else -> Color(0xFF212121)
        },
        label = "TextColor"
    )

    val pointsColor by animateColorAsState(
        targetValue = if (isChecked) textColor.copy(alpha = 0.7f) else textColor,
        label = "PointsColor"
    )

    val taskShape = when {
        isFailure -> FailTaskShape
        else -> PositiveTaskShape
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clickable { onTaskClick(task.uid) }
            .border(1.dp, borderColor, taskShape),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
                shape = taskShape
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                CircularCheckbox(
                    checked = isChecked,
                    onCheckedChange = { onTaskClick(task.uid) },
                    borderColor = borderColor,
                    fillColor = borderColor
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
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

                val emphasizedPointsStyle = when {
                    isLightsOff -> MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color(0xFF933A3A) // warm fail red
                    )
                    else -> MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = pointsColor
                    )
                }

                Text(
                    text = "${points} ⭐",
                    style = emphasizedPointsStyle
                )
            }
        }
    }
}