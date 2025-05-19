package com.example.madetoliveapp.presentation.rewards.components


import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DoNotDisturb
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.madetoliveapp.domain.model.RewardModel
import com.example.madetoliveapp.domain.model.TaskModel
import com.example.madetoliveapp.domain.model.TaskProjectModel
import com.example.madetoliveapp.presentation.projects.screens.EmojiPicker
import com.example.madetoliveapp.presentation.projects.uimodel.ProjectUiModel
import com.example.madetoliveapp.presentation.theme.PositiveTaskCheckedAccent
import com.example.madetoliveapp.presentation.theme.PositiveTaskUncheckedAccent
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRewardBottomSheet(
    onDismiss: () -> Unit,
    onAddReward: (RewardModel) -> Unit,
    projects: List<ProjectUiModel>? = null
) {
    var title by remember { mutableStateOf("") }
    var photoUrl by remember { mutableStateOf("") }
    var reusable by remember { mutableStateOf(false) }
    var points by remember { mutableFloatStateOf(10f) }

    var selectedIcon by remember { mutableStateOf("🎁") }
    var showEmojiPicker by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    val activeBackground = Color(0xFFD3EEDD)         // verde pastel más oscuro
    val activeBorder = Color(0xFF8BC34A)             // verde más intenso para el borde
    val inactiveBackground = MaterialTheme.colorScheme.surfaceVariant
    val inactiveBorder = Color.Transparent
    val containerColor = if (reusable) activeBackground else inactiveBackground
    val borderColor = if (reusable) activeBorder else inactiveBorder
    val contentColor = if (reusable) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant


    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Nueva recompensa", style = MaterialTheme.typography.titleLarge)

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.size(56.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .clickable { coroutineScope.launch { showEmojiPicker = true } }
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = selectedIcon, fontSize = 24.sp)
                    }
                }

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Título") },
                    modifier = Modifier.weight(1f)
                )
            }

            Column {
                Text("Puntos: ${points.toInt()}", style = MaterialTheme.typography.labelMedium)
                Slider(
                    value = points,
                    onValueChange = { points = it },
                    valueRange = 0f..100f,
                    steps = 9,
                    modifier = Modifier.fillMaxWidth(),
                    colors = SliderDefaults.colors(
                        thumbColor = MaterialTheme.colorScheme.primary,
                        activeTrackColor = MaterialTheme.colorScheme.primary,
                        inactiveTrackColor = MaterialTheme.colorScheme.outline
                    )
                )
            }


            OutlinedButton(
                onClick = { reusable = !reusable },
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = containerColor,
                    contentColor = contentColor
                ),
                border = BorderStroke(1.dp, borderColor),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Repeat,
                    contentDescription = null,
                    tint = Color(0xFF1B1B1B) // un gris oscuro / casi negro
                )
                Spacer(Modifier.width(8.dp))
                Text("Click if you want the reward to be reusable!")
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = {
                        val reward = RewardModel(
                            title = title.ifBlank { "Recompensa sin título" },
                            points = points.toInt(),
                            icon = selectedIcon,
                            reusable = reusable,
                            photo = photoUrl.ifBlank { null },
                            project = null
                        )
                        onAddReward(reward)
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Agregar")
                }

                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Cancelar")
                }
            }
        }

        if (showEmojiPicker) {
            Dialog(onDismissRequest = { showEmojiPicker = false }) {
                Surface(
                    shape = RoundedCornerShape(32.dp),
                    tonalElevation = 10.dp,
                    modifier = Modifier
                        .fillMaxWidth()
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
    }
}