package com.example.madetoliveapp.presentation.tasks.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.AssignmentTurnedIn
import androidx.compose.material.icons.filled.DisabledByDefault
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.madetoliveapp.presentation.tasks.SortMode
import com.example.madetoliveapp.presentation.tasks.TaskFilter

@Composable
fun FiltersComponent(
    onFilterClick: (TaskFilter) -> Unit,
    onSortClick: () -> Unit,
    sortMode: SortMode
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.End// Align all icons to the end
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            IconButton(
                onClick = { onFilterClick(TaskFilter.POSITIVE) },
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    Icons.Default.AddCircle,
                    contentDescription = "Positive"
                )
            }
            IconButton(
                onClick = { onFilterClick(TaskFilter.NEGATIVE) },
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    Icons.Default.DisabledByDefault,
                    contentDescription = "Negative"
                )
            }
            IconButton(
                onClick = { onFilterClick(TaskFilter.ALL) },
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    Icons.Default.AssignmentTurnedIn,
                    contentDescription = "All"
                )
            }
            IconButton(
                onClick = onSortClick,
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    imageVector = if (sortMode == SortMode.BY_POINTS) Icons.Default.Star else Icons.Default.Schedule,
                    contentDescription = "Sort"
                )
            }
        }
    }
}