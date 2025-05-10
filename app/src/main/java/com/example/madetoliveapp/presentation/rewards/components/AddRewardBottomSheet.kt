package com.example.madetoliveapp.presentation.rewards.components


import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.madetoliveapp.domain.model.FrameModel
import com.example.madetoliveapp.domain.model.TaskModel
import com.example.madetoliveapp.domain.model.TaskProjectModel
import com.example.madetoliveapp.presentation.projects.uimodel.ProjectUiModel
import com.example.madetoliveapp.presentation.theme.PositiveTaskCheckedAccent
import com.example.madetoliveapp.presentation.theme.PositiveTaskUncheckedAccent
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRewardBottomSheet(
    onDismiss: () -> Unit,
    onAddReward: (TaskModel) -> Unit,
    projects: List<ProjectUiModel> = listOf()
) {
    var title by remember { mutableStateOf("") }
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
                        thumbColor = PositiveTaskCheckedAccent,
                        activeTrackColor = PositiveTaskCheckedAccent,
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
                        val newTask = TaskModel(
                            title = title.ifBlank { "Tarea sin título" },
                            checked = false,
                            subTasks = listOf(),
                            date = parsedDate,
                            points = points.toInt(),
                            project = TaskProjectModel(
                                id = selectedProject?.uid ?: "",
                                title = selectedProject?.title ?: "",
                                icon = selectedProject?.icon ?: ""
                            )
                        )

                        onAddReward(newTask)
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

@Composable
fun HeaderRow(
    title: String,
    isAdded: Boolean,
    onToggleAddFrame: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.9f else 1f,
        label = "iconScale"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge
        )

        IconButton(
            onClick = onToggleAddFrame,
            interactionSource = interactionSource,
            modifier = Modifier.scale(scale)
        ) {
            AnimatedContent(
                targetState = isAdded,
                transitionSpec = {
                    fadeIn(tween(150)) togetherWith fadeOut(tween(150))
                },
                label = "iconToggle"
            ) { added ->
                Icon(
                    imageVector = if (added) Icons.Default.Check else Icons.Default.Add,
                    contentDescription = if (added) "Added" else "Add"
                )
            }
        }
    }
}



