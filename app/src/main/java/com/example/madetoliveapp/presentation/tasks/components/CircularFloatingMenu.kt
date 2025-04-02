package com.example.madetoliveapp.presentation.tasks.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

@Composable
fun CircularFloatingMenu(
    isExpanded: Boolean,
    onToggle: () -> Unit,
    onActionClick: (Int) -> Unit,
    actionIcons: List<ImageVector> = listOf(Icons.Default.Add, Icons.Default.Add, Icons.Default.Add)
) {
    val density = LocalDensity.current
    val radius = 100.dp

    // Angles: distribute across quarter-circle (135° to 225°)
    val angles = listOf(180f, 225f, 270f)

    Box(
        modifier = Modifier
            .wrapContentSize(align = Alignment.BottomEnd)
            .padding(end = 24.dp, bottom = 24.dp)
    ) {
        if (isExpanded) {
            angles.forEachIndexed { index, angle ->
                val rad = Math.toRadians(angle.toDouble())
                with(density) {
                    val x = cos(rad) * radius.toPx()
                    val y = sin(rad) * radius.toPx()

                    FloatingActionButton(
                        onClick = { onActionClick(index) },
                        shape = CircleShape,
                        containerColor = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier
                            .size(48.dp)
                            .offset { IntOffset(x.roundToInt(), y.roundToInt()) }
                    ) {
                        Icon(actionIcons[index], contentDescription = "Action $index")
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = onToggle,
            shape = CircleShape,
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            Icon(
                imageVector = if (isExpanded) Icons.Default.Close else Icons.Default.Add,
                contentDescription = "Toggle Menu"
            )
        }
    }
}