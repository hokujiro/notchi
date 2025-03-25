package com.example.madetoliveapp.presentation.components

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
import androidx.compose.material.Card
import androidx.compose.material.Checkbox
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.madetoliveapp.domain.model.TaskModel

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
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onTaskClick(task.uid) },
        elevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min) // Asegura que ambas secciones tengan la misma altura
        ) {
            // Contenido principal de la tarjeta
            Row(
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = task.checked,
                    onCheckedChange = { onTaskClick(task.uid) }
                )

                Spacer(modifier = Modifier.width(8.dp))

                Column {
                    Text(
                        text = task.title,
                        style = MaterialTheme.typography.body1,
                        textDecoration = if (task.checked) TextDecoration.LineThrough else null
                    )
                    if (task.points != null) {
                        Text(
                            text = "${task.points} points",
                            style = MaterialTheme.typography.body2,
                            color = if (task.checked) Color(0xFF388E3C) else Color.Unspecified// Verde más oscuro
                        )
                    }

                }
            }
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .padding(start = 8.dp)
                    .wrapContentWidth()
                    .fillMaxHeight()
                    .width(50.dp)
            ) {
                if (task.checked) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(50.dp) // Puedes ajustar el ancho del indicador
                            .background(
                                Color(0xFFA5D6A7)
                            )
                    )
                }
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            color = Color.White, // Color del círculo de puntos
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${task.points} pt",
                        style = MaterialTheme.typography.body2,
                        color = Color.Black
                    )
                }
            }

        }
    }
}