package com.systems.notchi.presentation.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val CottageShapes = Shapes(
    small = RoundedCornerShape(16.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(16.dp)
)

val BubbleShapePositive = RoundedCornerShape(
    topStart = 12.dp,
    topEnd = 0.dp,
    bottomEnd = 12.dp,
    bottomStart = 12.dp
)

val BubbleShapeNegative = RoundedCornerShape(
    topStart = 0.dp,
    topEnd = 12.dp,
    bottomEnd = 12.dp,
    bottomStart = 12.dp
)