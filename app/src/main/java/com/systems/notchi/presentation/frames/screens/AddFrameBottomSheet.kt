package com.systems.notchi.presentation.frames.screens


import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
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
import com.systems.notchi.domain.model.FrameModel
import com.systems.notchi.domain.model.TaskProjectModel
import com.systems.notchi.presentation.projects.uimodel.ProjectUiModel
import com.systems.notchi.presentation.theme.PositiveTaskUncheckedAccent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFrameBottomSheet(
    onDismiss: () -> Unit,
    onAddFrame: (FrameModel) -> Unit,
    projects: List<ProjectUiModel> = listOf()
) {
    var title by remember { mutableStateOf("") }
    var selectedProject by remember { mutableStateOf(projects.firstOrNull()) }
    var points by remember { mutableFloatStateOf(0f) }
    var isAddedAsFrame by remember { mutableStateOf(false) }

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
            Text(
                text = "Add Frame",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
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
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Column {
                Slider(
                    value = points,
                    onValueChange = { points = it },
                    valueRange = 0f..100f,
                    steps = 9, // 10 main stops = 9 in-between steps
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(32.dp),
                    colors = SliderDefaults.colors(
                        thumbColor = MaterialTheme.colorScheme.secondary,
                        activeTrackColor = MaterialTheme.colorScheme.secondary,
                        inactiveTrackColor = PositiveTaskUncheckedAccent
                    )
                )
                Text(
                    text = "Selected: ${points.toInt()} points",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }

            Text("Choose a project", style = MaterialTheme.typography.labelMedium)
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
            ) {
                projects.forEach { project ->
                    val isSelected = project == selectedProject
                    FilterChip(
                        selected = isSelected,
                        onClick = {
                            selectedProject = if (isSelected) null else project
                        },
                        label = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("${project.icon} ")
                                Text(project.title)
                            }
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                            selectedLabelColor = MaterialTheme.colorScheme.onSecondary,
                            containerColor = MaterialTheme.colorScheme.surface,
                            labelColor = MaterialTheme.colorScheme.onSurface
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            borderColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                            selectedBorderColor = MaterialTheme.colorScheme.primary
                        )
                    )
                }
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = {
                        val newFrame = FrameModel(
                            uid = "",
                            title = title.ifBlank { "Tarea sin título" },
                            points = points.toInt(),
                            project = TaskProjectModel(
                                id = selectedProject?.uid ?: "",
                                title = selectedProject?.title ?: "",
                                icon = selectedProject?.icon ?: ""
                            )
                        )

                        onAddFrame(newFrame)
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Agregar", color = MaterialTheme.colorScheme.background)
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


