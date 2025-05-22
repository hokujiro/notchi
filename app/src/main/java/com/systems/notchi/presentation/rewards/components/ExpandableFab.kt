package com.systems.notchi.presentation.rewards.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.GridOn
import androidx.compose.material.icons.filled.Redeem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ExpandableFab(
    isExpanded: Boolean,
    onActionClick: (Int) -> Unit,
    onToggle: () -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomEnd
    ) {
        Column(
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            if (isExpanded) {
                FloatingActionButton(
                    onClick = { onActionClick(0) },
                    shape = CircleShape,
                    modifier = Modifier
                        .size(48.dp)
                ) {
                    Icon(Icons.Default.Redeem, contentDescription = "Add Reward")
                }

                FloatingActionButton(
                    onClick = { onActionClick(1) },
                    shape = CircleShape,
                    modifier = Modifier
                        .size(48.dp)
                ) {
                    Icon(Icons.Default.GridOn, contentDescription = "Create Reward List")
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
}