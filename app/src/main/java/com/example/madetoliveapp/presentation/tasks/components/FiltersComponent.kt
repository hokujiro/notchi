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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.madetoliveapp.presentation.tasks.SortMode
import com.example.madetoliveapp.presentation.tasks.TaskFilter
import com.example.madetoliveapp.presentation.theme.DarkText

@Composable
fun FiltersComponent(
    onFilterClick: (TaskFilter) -> Unit,
    onSortClick: () -> Unit,
    sortMode: SortMode,
    dailyPoints: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        val emphasizedPointsStyle = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = DarkText // warm fail red
            )

        Text(
            text = "$dailyPoints ⭐",
            style = emphasizedPointsStyle
        )

        // RIGHT: Filter buttons
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { onFilterClick(TaskFilter.POSITIVE) },
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    Icons.Default.AddCircle,
                    contentDescription = "Positive",
                    modifier = Modifier.size(20.dp)
                )
            }
            IconButton(
                onClick = { onFilterClick(TaskFilter.NEGATIVE) },
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    Icons.Default.DisabledByDefault,
                    contentDescription = "Negative",
                    modifier = Modifier.size(20.dp)
                )
            }
            IconButton(
                onClick = { onFilterClick(TaskFilter.ALL) },
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    Icons.Default.AssignmentTurnedIn,
                    contentDescription = "All",
                    modifier = Modifier.size(20.dp)
                )
            }
            IconButton(
                onClick = onSortClick,
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = if (sortMode == SortMode.BY_POINTS) Icons.Default.Star else Icons.Default.Schedule,
                    contentDescription = "Sort",
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}