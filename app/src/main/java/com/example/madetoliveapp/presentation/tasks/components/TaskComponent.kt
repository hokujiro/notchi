package com.example.madetoliveapp.presentation.tasks.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.madetoliveapp.domain.model.TaskModel
import com.example.madetoliveapp.presentation.theme.DeepBrown
import com.example.madetoliveapp.presentation.theme.EarthBrown

@Composable
fun TaskComponent(
    tasks: List<TaskModel>,
    onTaskClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .padding(bottom = 56.dp)
            .padding(WindowInsets.systemBars.asPaddingValues()) // Account for system bars
    ) {
        items(tasks) { task ->
            TaskItem(task, onTaskClick)
        }
    }
}

@Composable
fun TaskItem(task: TaskModel, onTaskClick: (String) -> Unit) {
    val backgroundColor = if (task.checked) Color(0xFFECE0D1) else Color(0xFFFFF8E1) // Soft browns/beige
    val pointsCircleColor = if (task.checked) DeepBrown else EarthBrown // Light khaki
    val textColor = MaterialTheme.colorScheme.onSurface
    val pointsTextColor = if (task.checked) Color(0xFF388E3C) else MaterialTheme.colorScheme.onSurface

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 4.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable { onTaskClick(task.uid) },
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = task.checked,
                onCheckedChange = { onTaskClick(task.uid) },
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colorScheme.primary,
                    uncheckedColor = MaterialTheme.colorScheme.primary,
                    checkmarkColor = Color.White
                )
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        textDecoration = if (task.checked) TextDecoration.LineThrough else null,
                        color = textColor
                    )
                )
            }

            // Points Badge
                Text(
                    text = "${task.points} ⭐ ",
                    style = MaterialTheme.typography.labelLarge.copy(
                        color = pointsTextColor
                    )
                )
        }
    }
}