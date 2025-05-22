package com.systems.notchi.presentation.tasks.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.systems.notchi.domain.model.TaskModel
import com.systems.notchi.presentation.projects.uimodel.ProjectUiModel
import com.systems.notchi.presentation.theme.FailTaskCheckedAccent
import com.systems.notchi.presentation.theme.FailTaskUnchecked
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFailBottomSheet(
    onDismiss: () -> Unit,
    onAddTask: (TaskModel) -> Unit,
    selectedDate: Long,
    projects: List<ProjectUiModel> = listOf()
) {
    var title by remember { mutableStateOf("") }
    var subtitle by remember { mutableStateOf("") }
    var selectedProject by remember { mutableStateOf(projects.firstOrNull()) }
    var points by remember { mutableFloatStateOf(0f) }
    var selectedIcon by remember { mutableStateOf("✅") }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Add new fail", style = MaterialTheme.typography.titleLarge)

            val transparentHandle = TextSelectionColors(
                handleColor = Color.Transparent,
                backgroundColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f) // or any selection bg color
            )
            CompositionLocalProvider(LocalTextSelectionColors provides transparentHandle) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Column {
                Slider(
                    value = points,
                    onValueChange = { points = it },
                    valueRange = 0f..100f,
                    steps = 9,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(32.dp), // Increases thickness
                    colors = SliderDefaults.colors(
                        thumbColor = FailTaskCheckedAccent,
                        activeTrackColor = FailTaskCheckedAccent,
                        inactiveTrackColor = FailTaskUnchecked
                    )
                )
                Text(
                    text = "Selected: -${points.toInt()} points",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = {
                        val parsedDate = Date(selectedDate)
                        val newTask = TaskModel(
                            title = title.ifBlank { "Tarea sin título" },
                            checked = false,
                            subTasks = listOf(),
                            date = parsedDate,
                            points = points.toInt().let { -kotlin.math.abs(it) },
                            project = null
                        )
                        onAddTask(newTask)
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Agregar",
                        color =  MaterialTheme.colorScheme.background)
                }
                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Cancel")
                }
            }
        }
    }


}

