package com.systems.notchi.presentation.projects.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CompactSegmentedButtonBar(
    selectedIndex: Int,
    onSegmentSelected: (Int) -> Unit
) {
    val height = 28.dp
    val iconSize = 16.dp
    val cornerRadius = 8.dp

    Row(
        modifier = Modifier
            .padding(4.dp)
            .height(height)
            .width(80.dp)
    ) {
        // Calendar segment
        Button(
            onClick = { onSegmentSelected(0) },
            shape = RoundedCornerShape(topStart = cornerRadius, bottomStart = cornerRadius),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (selectedIndex == 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                contentColor = if (selectedIndex == 0) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
            ),
            contentPadding = PaddingValues(6.dp),
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                imageVector = Icons.Default.CalendarToday,
                contentDescription = "Calendar",
                modifier = Modifier.size(iconSize)
            )
        }

        // Info segment
        Button(
            onClick = { onSegmentSelected(1) },
            shape = RoundedCornerShape(topEnd = cornerRadius, bottomEnd = cornerRadius),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (selectedIndex == 1) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                contentColor = if (selectedIndex == 1) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
            ),
            contentPadding = PaddingValues(6.dp),
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = "Info",
                modifier = Modifier.size(iconSize)
            )
        }
    }
}