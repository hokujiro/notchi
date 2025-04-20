package com.example.madetoliveapp.presentation.tasks.components

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection

object PositiveTaskShape : Shape {
    override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density): Outline {
        val radius = 28f
        val tabHeight = 16f
        val tabWidth = 60f

        return Outline.Generic(
            Path().apply {
                moveTo(radius, 0f)

                // Top edge and tab
                lineTo(size.width - tabWidth - 12f, 0f)
                lineTo(size.width - tabWidth, tabHeight)
                lineTo(size.width, tabHeight)
                lineTo(size.width, size.height - radius)

                // Bottom-right arc
                arcTo(
                    rect = Rect(size.width - 2 * radius, size.height - 2 * radius, size.width, size.height),
                    startAngleDegrees = 0f,
                    sweepAngleDegrees = 90f,
                    forceMoveTo = false
                )

                // Bottom-left arc
                lineTo(radius, size.height)
                arcTo(
                    rect = Rect(0f, size.height - 2 * radius, 2 * radius, size.height),
                    startAngleDegrees = 90f,
                    sweepAngleDegrees = 90f,
                    forceMoveTo = false
                )

                // Top-left arc
                lineTo(0f, radius)
                arcTo(
                    rect = Rect(0f, 0f, radius * 2, radius * 2),
                    startAngleDegrees = 180f,
                    sweepAngleDegrees = 90f,
                    forceMoveTo = false
                )

                close()
            }
        )
    }
}
object FailTaskShape : Shape {
    override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density): Outline {
        val radius = 24f
        val tipOffset = 18f
        val liftHeight = size.height * 0.25f

        return Outline.Generic(
            Path().apply {
                moveTo(radius, 0f)
                lineTo(size.width - tipOffset, 0f)
                lineTo(size.width, liftHeight)
                lineTo(size.width - tipOffset, size.height)

                // Bottom edge
                lineTo(radius, size.height)

                // Bottom-left arc
                arcTo(
                    rect = Rect(0f, size.height - 2 * radius, 2 * radius, size.height),
                    startAngleDegrees = 90f,
                    sweepAngleDegrees = 90f,
                    forceMoveTo = false
                )

                // Top-left arc
                lineTo(0f, radius)
                arcTo(
                    rect = Rect(0f, 0f, 2 * radius, 2 * radius),
                    startAngleDegrees = 180f,
                    sweepAngleDegrees = 90f,
                    forceMoveTo = false
                )

                close()
            }
        )
    }
}