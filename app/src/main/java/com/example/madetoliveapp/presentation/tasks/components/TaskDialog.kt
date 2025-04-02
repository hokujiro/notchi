package com.example.madetoliveapp.presentation.tasks.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.madetoliveapp.domain.model.ProjectModel
import com.example.madetoliveapp.domain.model.TaskModel
import com.example.madetoliveapp.domain.model.TaskProjectModel
import java.util.Date

@Composable
fun AddTaskDialog(
    onDismiss: () -> Unit,
    onAddTask: (TaskModel) -> Unit,
    selectedDate: Long,
    projects: List<ProjectModel> = listOf(),
) {
    // States for user input
    var title by remember { mutableStateOf("") }
    var subtitle by remember { mutableStateOf("") }
    var selectedProject by remember { mutableStateOf(projects.firstOrNull()) }
    var points by remember { mutableStateOf("") }
    var selectedIcon by remember { mutableStateOf("✅") } // Default icon

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add new task") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = subtitle,
                    onValueChange = { subtitle = it },
                    label = { Text("Subtitle") },
                    singleLine = true
                )
                DropdownMenuBox(
                    label = "Project",
                    options = projects.map { it.title },
                    selectedOption = selectedProject?.title?:"Title",
                    onOptionSelected = { option ->
                        selectedProject = projects.find { option == it.title } ?: projects.first()
                    }
                )
                OutlinedTextField(
                    value = points,
                    onValueChange = { points = it.filter { c -> c.isDigit() } },
                    label = { Text("Points") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )
                IconPicker(selectedIcon = selectedIcon, onIconSelected = { selectedIcon = it })
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
                        date = parsedDate,
                        points = points.toIntOrNull(),
                        project = TaskProjectModel()
                    )
                    onAddTask(newTask) // Pass the task back to the parent
                }
            ) {
                Text("Agregar")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        shape = RoundedCornerShape(20.dp),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownMenuBox(
    label: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
        OutlinedTextField(
            value = selectedOption,
            onValueChange = {},
            label = { Text(label) },
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            modifier = Modifier.menuAnchor()
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun IconPicker(selectedIcon: String, onIconSelected: (String) -> Unit) {
    val icons = listOf("✅", "📝", "📌", "🔥", "🚀", "🎯") // Add more as needed
    Column {
        Text("Icon", style = MaterialTheme.typography.labelLarge)
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(icons) { icon ->
                Button(
                    onClick = { onIconSelected(icon) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (icon == selectedIcon) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
                    )
                ) {
                    Text(icon, fontSize = 20.sp)
                }
            }
        }
    }
}