package com.example.madetoliveapp.presentation.tasks.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
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
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.madetoliveapp.domain.model.ProjectModel
import com.example.madetoliveapp.domain.model.TaskModel
import com.example.madetoliveapp.domain.model.TaskProjectModel
import com.example.madetoliveapp.presentation.projects.uimodel.ProjectUiModel
import com.example.madetoliveapp.presentation.theme.CocoaLight
import com.example.madetoliveapp.presentation.theme.SageLight
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskEditBottomSheet(
    task: TaskModel,
    projects: List<ProjectUiModel>,
    onDismiss: () -> Unit,
    onSave: (TaskModel) -> Unit
) {
    var title by remember {
        mutableStateOf(
            TextFieldValue(
                text = task.title,
                selection = TextRange(task.title.length) // 👈 cursor at end
            )
        )
    }
    var points by remember { mutableFloatStateOf((task.points ?: 0).toFloat()) }
    var selectedProject by remember {
        mutableStateOf(
            projects.find { it.title == task.project?.title }
        )
    }

    val isFail = task.points != null && task.points < 0
    val actualRange = if (isFail) 10f..100f else 0f..100f // Slider goes "forward"
    val visualPoints = if (isFail) -points else points
    val displayPoints = if (isFail) -visualPoints.toInt() else visualPoints.toInt()
    val sliderSteps = if (isFail) 9 else 9

    fun snapToTens(value: Float): Float {
        return (value / 10).roundToInt() * 10f
    }

    val failColor = Color(0xFF6A1B1A)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Text(
                text = if (isFail) "Edit Fail" else "Edit Task",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 16.dp)
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
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = MaterialTheme.typography.bodyLarge
                )
            }

            Spacer(Modifier.height(20.dp))

            Text(
                text = "Points: ${points.toInt()} ⭐",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Slider(
                value = visualPoints,
                onValueChange = {
                    val snapped = snapToTens(it)
                    points = if (isFail) -snapped else snapped
                },
                valueRange = actualRange,
                steps = 9,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(32.dp),
                colors = SliderDefaults.colors(
                    thumbColor = if (isFail) failColor else MaterialTheme.colorScheme.secondary,
                    activeTrackColor = if (isFail) failColor else MaterialTheme.colorScheme.secondary,
                    inactiveTrackColor = CocoaLight
                )
            )

            Spacer(Modifier.height(16.dp))

            Text(
                text = "Project",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 8.dp)
            )

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
                            selectedContainerColor = SageLight,
                            selectedLabelColor = MaterialTheme.colorScheme.onSecondary,
                            containerColor = MaterialTheme.colorScheme.surface,
                            labelColor = MaterialTheme.colorScheme.onSurface
                        )
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = {
                        onSave(
                            task.copy(
                                title = title.text,
                                points = points.toInt(),
                                project = TaskProjectModel(
                                    id = selectedProject?.uid ?: "",
                                    title = selectedProject?.title ?: "",
                                    icon = selectedProject?.icon ?: ""
                                )
                            )
                        )
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Save",
                        color =  MaterialTheme.colorScheme.background)
                }
                Spacer(modifier = Modifier.width(8.dp))
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
