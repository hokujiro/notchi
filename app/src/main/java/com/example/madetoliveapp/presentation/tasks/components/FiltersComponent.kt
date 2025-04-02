package com.example.madetoliveapp.presentation.tasks.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.example.madetoliveapp.presentation.tasks.SortMode
import com.example.madetoliveapp.presentation.tasks.TaskFilter

@Composable
fun FiltersComponent(
    onFilterClick: (TaskFilter) -> Unit,
    onSortClick: () -> Unit,
    sortMode: SortMode
) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row {
            IconButton(onClick = {
                onFilterClick(
                    TaskFilter.POSITIVE
                )
            }) {
                Icon(
                    imageVector = Icons.Default.AddCircle,
                    contentDescription = null
                )
            }
            IconButton(onClick = {
                onFilterClick(
                    TaskFilter.NEGATIVE
                )
            }) {
                Icon(
                    imageVector = Icons.Default.DisabledByDefault,
                    contentDescription = null
                )
            }
            IconButton(onClick = {
                onFilterClick(
                    TaskFilter.ALL
                )
            }) {
                Icon(
                    imageVector = Icons.Default.AssignmentTurnedIn,
                    contentDescription = null
                )
            }
            IconButton(onClick = { onSortClick() }) {
                Icon(
                    imageVector = if (sortMode == SortMode.BY_POINTS) Icons.Default.Star else Icons.Default.Schedule,
                    contentDescription = null
                )
            }
        }

    }
}