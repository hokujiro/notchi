package com.systems.notchi.presentation.tasks.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun CircularCheckbox(
    checked: Boolean,
    onCheckedChange: () -> Unit,
    modifier: Modifier = Modifier,
    size: Dp = 20.dp,
    borderColor: Color = MaterialTheme.colorScheme.primary,
    fillColor: Color = MaterialTheme.colorScheme.primary,
    checkmarkColor: Color = Color.White,
    useCross: Boolean = false
) {
    val interactionSource = remember { MutableInteractionSource() }
    Box(
        modifier = modifier
            .size(size)
            .clickable(
                interactionSource = interactionSource,
                indication = null, // 👈 disables ripple and visual "pressed" state
                onClick = { onCheckedChange() }
            ),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            // Outer circle
            drawCircle(
                color = borderColor,
                style = Stroke(width = 1.dp.toPx())
            )

            // Inner fill (if checked)
            if (checked) {
                drawCircle(
                    color = fillColor,
                    radius = size.toPx() / 2f
                )

                if (useCross) {
                    // Draw cross (X)
                    drawLine(
                        color = checkmarkColor,
                        start = Offset(x = size.toPx() * 0.3f, y = size.toPx() * 0.3f),
                        end = Offset(x = size.toPx() * 0.7f, y = size.toPx() * 0.7f),
                        strokeWidth = 2.dp.toPx(),
                        cap = StrokeCap.Round
                    )
                    drawLine(
                        color = checkmarkColor,
                        start = Offset(x = size.toPx() * 0.7f, y = size.toPx() * 0.3f),
                        end = Offset(x = size.toPx() * 0.3f, y = size.toPx() * 0.7f),
                        strokeWidth = 2.dp.toPx(),
                        cap = StrokeCap.Round
                    )
                } else {
                    // Draw checkmark (✓)
                    drawLine(
                        color = checkmarkColor,
                        start = Offset(x = size.toPx() * 0.3f, y = size.toPx() * 0.5f),
                        end = Offset(x = size.toPx() * 0.45f, y = size.toPx() * 0.65f),
                        strokeWidth = 2.dp.toPx(),
                        cap = StrokeCap.Round
                    )
                    drawLine(
                        color = checkmarkColor,
                        start = Offset(x = size.toPx() * 0.45f, y = size.toPx() * 0.65f),
                        end = Offset(x = size.toPx() * 0.75f, y = size.toPx() * 0.35f),
                        strokeWidth = 2.dp.toPx(),
                        cap = StrokeCap.Round
                    )
                }
            }
        }
    }
}