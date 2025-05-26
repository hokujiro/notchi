package com.systems.notchi.presentation.tasks.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.systems.notchi.presentation.tasks.SortMode
import com.systems.notchi.presentation.tasks.TaskFilter
import com.systems.notchi.presentation.theme.DarkText

@Composable
fun FiltersComponent(
    modifier: Modifier,
    selectedFilter: TaskFilter,
    onFilterClick: (TaskFilter) -> Unit,
    onSortClick: () -> Unit,
    sortMode: SortMode,
    dailyPoints: Int
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        val emphasizedPointsStyle = MaterialTheme.typography.bodyLarge.copy(
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = DarkText
        )

        val adjective = if(dailyPoints<0) "lost" else "earned"

        Text(
            text = "⭐ $dailyPoints points $adjective",
            style = emphasizedPointsStyle
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // Replace icon buttons with the segmented button
            TaskFilterSegmentedButton(
                selectedFilter = selectedFilter,
                onFilterClick = onFilterClick
            )

            /*// Sort icon button
            IconButton(
                onClick = onSortClick,
                modifier = Modifier.size(32.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = if (sortMode == SortMode.BY_POINTS) Icons.Default.Star else Icons.Default.Schedule,
                        contentDescription = "Sort base",
                        modifier = Modifier.size(20.dp)
                    )
                    Icon(
                        imageVector = Icons.Default.ArrowUpward, // Sort indicator
                        contentDescription = "Sort indicator",
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .size(10.dp),
                    )
                }
            }*/
        }
    }
}