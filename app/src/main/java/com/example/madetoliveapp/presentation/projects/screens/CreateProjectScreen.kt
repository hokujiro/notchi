package com.example.madetoliveapp.presentation.projects.screens


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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
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
import com.example.madetoliveapp.domain.model.TaskModel
import com.example.madetoliveapp.domain.model.TaskProjectModel
import com.example.madetoliveapp.presentation.projects.uimodel.ProjectUiModel
import androidx.emoji2.emojipicker.RecentEmojiProviderAdapter
import com.example.madetoliveapp.presentation.extensions.CustomRecentEmojiProvider
import kotlinx.coroutines.launch
import androidx.compose.material3.TextFieldDefaults


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateProjectScreen(
    onCancel: () -> Unit,
    onSave: (ProjectUiModel) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var icon by remember { mutableStateOf("📁") }
    var tasks by remember { mutableStateOf(listOf<TaskModel>()) }
    var selectedIcon by remember { mutableStateOf("✅") } // Default emoji
    val coroutineScope = rememberCoroutineScope()
    var showEmojiPicker by remember { mutableStateOf(false) }
    val sharedShape = RoundedCornerShape(20.dp)
    val backgroundColor = MaterialTheme.colorScheme.surfaceVariant


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
                        val newProject =
                            ProjectUiModel(
                                title = title,
                                color = "0xFF3B5F6B",
                                tasksList = tasks,
                                icon = selectedIcon
                            )
                        onSave(newProject)
                    }) {
                        Text("Save")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                // Emoji Button styled like a field
                Surface(
                    shape = sharedShape,
                    color = backgroundColor,
                    modifier = Modifier.size(56.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .clickable {
                                coroutineScope.launch { showEmojiPicker = true }
                            }
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = selectedIcon, fontSize = 24.sp)
                    }
                }

                // Styled TextField with matching shape and background
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Project Title") },
                    shape = sharedShape,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        containerColor = backgroundColor,
                    ),
                    modifier = Modifier.weight(1f)
                )
            }

            if (showEmojiPicker) {
                Dialog(onDismissRequest = {
                    showEmojiPicker = false
                }) {
                    Surface(
                        shape = RoundedCornerShape(32.dp),
                        tonalElevation = 10.dp,
                        modifier = Modifier
                            .fillMaxWidth() // 👉 wider (95% of screen width)
                            .heightIn(max = 500.dp)
                            .padding(horizontal = 4.dp, vertical = 16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
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
            }


            //IconPicker(selectedIcon = icon, onIconSelected = { icon = it })

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
                modifier = Modifier.fillMaxWidth()
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
    Row(verticalAlignment = Alignment.CenterVertically) {
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

@Composable
fun EmojiPicker(
    onDismiss: () -> Unit = {},
    onConfirm: (String) -> Unit
) {
    BackHandler { onDismiss() }

    AndroidView(
        factory = { context ->
            androidx.emoji2.emojipicker.EmojiPickerView(context).apply {
                clipToOutline = true
                isVerticalScrollBarEnabled = true
                setRecentEmojiProvider(
                    RecentEmojiProviderAdapter(CustomRecentEmojiProvider(context))
                )
                setOnEmojiPickedListener { emoji ->
                    onDismiss()
                    onConfirm(emoji.emoji)
                }
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 500.dp) // 👈 This is the key
    )
}


