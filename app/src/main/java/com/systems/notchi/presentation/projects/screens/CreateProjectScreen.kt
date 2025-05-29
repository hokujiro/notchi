package com.systems.notchi.presentation.projects.screens


import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import com.systems.notchi.domain.model.TaskModel
import com.systems.notchi.domain.model.TaskProjectModel
import com.systems.notchi.presentation.projects.uimodel.ProjectUiModel
import androidx.emoji2.emojipicker.RecentEmojiProviderAdapter
import com.systems.notchi.presentation.extensions.CustomRecentEmojiProvider
import kotlinx.coroutines.launch
import androidx.compose.material3.TextFieldDefaults


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateProjectScreen(
    onCancel: () -> Unit,
    onSave: (ProjectUiModel) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var tasks by remember { mutableStateOf(listOf<TaskModel>()) }
    var selectedIcon by remember { mutableStateOf("✅") }
    var showEmojiPicker by remember { mutableStateOf(false) }

    val sharedShape = RoundedCornerShape(20.dp)
    val coroutineScope = rememberCoroutineScope()
    val colorScheme = MaterialTheme.colorScheme

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("New Project") },
                navigationIcon = {
                    IconButton(onClick = onCancel) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    TextButton(onClick = {
                        val newProject = ProjectUiModel(
                            title = title,
                            color = "0xFF3B5F6B",
                            tasksList = tasks,
                            icon = selectedIcon
                        )
                        onSave(newProject)
                    }) {
                        Text("Save", color = colorScheme.primary)
                    }
                }
            )
        },
        containerColor = colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Surface(
                    shape = CircleShape,
                    color = colorScheme.surfaceVariant,
                    modifier = Modifier
                        .size(56.dp)
                        .clickable { showEmojiPicker = true }
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(text = selectedIcon, fontSize = 24.sp)
                    }
                }

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Project Title") },
                    shape = sharedShape,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        containerColor = colorScheme.surfaceVariant
                    ),
                    modifier = Modifier.weight(1f)
                )
            }

            if (showEmojiPicker) {
                Dialog(onDismissRequest = { showEmojiPicker = false }) {
                    Surface(
                        shape = RoundedCornerShape(24.dp),
                        tonalElevation = 10.dp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 500.dp)
                            .padding(8.dp)
                    ) {
                        EmojiPicker(
                            onDismiss = { showEmojiPicker = false },
                            onConfirm = { emoji ->
                                selectedIcon = emoji
                                showEmojiPicker = false
                            }
                        )
                    }
                }
            }

            Text("Tasks", style = MaterialTheme.typography.titleMedium)

            tasks.forEachIndexed { index, task ->
                TaskInputItem(
                    task = task,
                    onTaskChange = { updated ->
                        tasks = tasks.toMutableList().also { it[index] = updated }
                    },
                    onRemove = {
                        tasks = tasks.toMutableList().also { it.removeAt(index) }
                    }
                )
            }

            Button(
                onClick = {
                    tasks = tasks + TaskModel(
                        title = "",
                        checked = false,
                        date = null,
                        points = 10,
                        project = TaskProjectModel()
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = colorScheme.primary)
            ) {
                Text("Add Task")
            }
        }
    }
}

@Composable
fun TaskInputItem(
    task: TaskModel,
    onTaskChange: (TaskModel) -> Unit,
    onRemove: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        tonalElevation = 2.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ) {
            OutlinedTextField(
                value = task.title,
                onValueChange = { onTaskChange(task.copy(title = it)) },
                label = { Text("Task") },
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = onRemove) {
                Icon(Icons.Default.Delete, contentDescription = "Remove Task")
            }
        }
    }
}

@Composable
fun EmojiPicker(
    onDismiss: () -> Unit = {},
    onConfirm: (String) -> Unit
) {
    BackHandler { onDismiss() }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            tonalElevation = 12.dp,
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier
                .fillMaxWidth() // ⬅️ Full width
                .heightIn(min = 500.dp, max = 700.dp)
                .padding(horizontal = 0.dp, vertical = 12.dp) // ⬅️ Minimal outer padding
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp) // ⬅️ Inner breathing space
            ) {
                AndroidView(
                    factory = { context ->
                        androidx.emoji2.emojipicker.EmojiPickerView(context).apply {
                            setRecentEmojiProvider(
                                RecentEmojiProviderAdapter(CustomRecentEmojiProvider(context))
                            )
                            emojiGridRows = 6.5f // More height
                            emojiGridColumns = 7 // Fewer columns = larger emojis

                            setOnEmojiPickedListener { emoji ->
                                onDismiss()
                                onConfirm(emoji.emoji)
                            }
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

