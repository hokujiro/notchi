package com.example.madetoliveapp.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.madetoliveapp.domain.model.TaskModel
import java.util.Date

@Composable
fun AddTaskDialog(
    onDismiss: () -> Unit,
    onAddTask: (TaskModel) -> Unit,
    selectedDate: Long
) {
    // States for user input
    var title by remember { mutableStateOf("") }
    var points by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Agregar nueva tarea") },
        text = {
            Column {
                // Title input
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Título de la tarea") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Points input
                OutlinedTextField(
                    value = points,
                    onValueChange = { points = it },
                    label = { Text("Puntos") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Category input
                OutlinedTextField(
                    value = category,
                    onValueChange = { category = it },
                    label = { Text("Categoría") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    // Create new task
                    val parsedDate = Date(selectedDate)
                    val newTask = TaskModel(
                        title = title.ifBlank { "Tarea sin título" },
                        checked = false,
                        subTasks = listOf(),
                        category = category.ifBlank { null },
                        date = parsedDate,
                        points = points.toIntOrNull()
                    )
                    onAddTask(newTask) // Pass the task back to the parent
                }
            ) {
                Text("Agregar")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}