package com.example.madetoliveapp.presentation.tasks.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.DisabledByDefault
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.madetoliveapp.presentation.tasks.TaskFilter
import com.example.madetoliveapp.presentation.theme.FailTaskChecked
import com.example.madetoliveapp.presentation.theme.FailTaskCheckedAccent
import com.example.madetoliveapp.presentation.theme.FailTaskUncheckedAccent
import com.example.madetoliveapp.presentation.theme.PositiveTaskChecked
import com.example.madetoliveapp.presentation.theme.PositiveTaskCheckedAccent
import com.example.madetoliveapp.presentation.theme.PositiveTaskUnchecked

@Composable
fun TaskFilterSegmentedButton(
    selectedFilter: TaskFilter,
    onFilterClick: (TaskFilter) -> Unit
) {
    val height = 28.dp
    val iconSize = 16.dp
    val cornerRadius = 8.dp

    val isPositiveSelected = selectedFilter == TaskFilter.POSITIVE
    val isNegativeSelected = selectedFilter == TaskFilter.NEGATIVE

    fun toggleFilter(target: TaskFilter) {
        val newFilter = if (selectedFilter == target) TaskFilter.ALL else target
        onFilterClick(newFilter)
    }

    Row(
        modifier = Modifier
            .padding(4.dp)
            .height(height)
            .width(80.dp)
    ) {
        // Positive Button
        Button(
            onClick = { toggleFilter(TaskFilter.POSITIVE) },
            shape = RoundedCornerShape(topStart = cornerRadius, bottomStart = cornerRadius),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isPositiveSelected) PositiveTaskChecked else PositiveTaskUnchecked,
                contentColor = if (isPositiveSelected) PositiveTaskCheckedAccent else FailTaskUncheckedAccent
            ),
            contentPadding = PaddingValues(6.dp),
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                imageVector = Icons.Default.AddCircle,
                contentDescription = "Positive",
                modifier = Modifier.size(iconSize)
            )
        }

        // Negative Button
        Button(
            onClick = { toggleFilter(TaskFilter.NEGATIVE) },
            shape = RoundedCornerShape(topEnd = cornerRadius, bottomEnd = cornerRadius),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isNegativeSelected) FailTaskChecked else PositiveTaskUnchecked,
                contentColor = if (isNegativeSelected) FailTaskCheckedAccent else FailTaskUncheckedAccent
            ),
            contentPadding = PaddingValues(6.dp),
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                imageVector = Icons.Default.DisabledByDefault,
                contentDescription = "Negative",
                modifier = Modifier.size(iconSize)
            )
        }
    }
}