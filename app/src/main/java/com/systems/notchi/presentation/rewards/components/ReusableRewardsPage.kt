package com.systems.notchi.presentation.rewards.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.systems.notchi.R
import com.systems.notchi.domain.model.RewardModel
import com.systems.notchi.presentation.rewards.RewardsViewModel
import com.systems.notchi.presentation.theme.CharcoalText
import com.systems.notchi.presentation.theme.CoolWhite
import com.systems.notchi.presentation.theme.MistBlueLight
import com.systems.notchi.presentation.theme.MistGray
import com.systems.notchi.presentation.theme.MistGrayDark
import com.systems.notchi.presentation.theme.MistGrayLight
import com.systems.notchi.presentation.theme.SubtleText
import kotlinx.coroutines.launch
import kotlin.collections.set

@Composable
fun ReusableRewardsPage(
    rewards: List<RewardModel>,
    onRedeem: (RewardModel) -> Unit,
    userTotalPoints: Int,
    rewardsViewModel: RewardsViewModel
) {
    val deleteStates = remember { mutableStateMapOf<String, Boolean>() }
    val coroutineScope = rememberCoroutineScope()
    if (rewards.isEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(WindowInsets.systemBars.asPaddingValues()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.no_rewards), // 🔁 Replace with your drawable
                contentDescription = "No tasks",
                modifier = Modifier.size(300.dp)
            )
            Text(
                text = "No rewards yet,\nadd your first one!",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 18.sp, // 👈 increase this as needed
                    color = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    } else {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2), // 👈 Forces two columns, no centering
            contentPadding = PaddingValues(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize() // 👈 Ensures full width
        ) {
            items(rewards) { reward ->
                val isEnabled = userTotalPoints >= (reward.points ?: 0)
                val showDelete = deleteStates[reward.id] == true

                ReusableRewardCard(
                    reward = reward,
                    isEnabled = isEnabled,
                    showDelete = showDelete,
                    onLongPress = {
                        deleteStates[reward.id] = !(deleteStates[reward.id] ?: false)
                    },
                    onDelete = {
                        deleteStates.remove(reward.id)
                        coroutineScope.launch {
                            rewardsViewModel.deleteReward(reward)
                        }
                    },
                    onRedeem = {
                        if (isEnabled) onRedeem(reward)
                    }
                )
            }
        }
    }
}

@Composable
@OptIn(ExperimentalFoundationApi::class)
fun ReusableRewardCard(
    reward: RewardModel,
    isEnabled: Boolean,
    showDelete: Boolean,
    onLongPress: () -> Unit,
    onDelete: () -> Unit,
    onRedeem: () -> Unit
) {
    val cardColor = if (isEnabled) CoolWhite else MistGrayLight
    val contentColor = if (isEnabled) CharcoalText else MistGrayDark
    val borderColor = if (isEnabled) MistGrayLight else MistGray

    Box(
        modifier = Modifier
            .width(140.dp)
            .height(160.dp)
            .padding(6.dp)
            .combinedClickable(
                onClick = {}, // Optional regular click
                onLongClick = onLongPress
            )
    ) {
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = cardColor),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            border = BorderStroke(1.dp, borderColor),
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                reward.icon?.let {
                    Text(it, style = MaterialTheme.typography.titleMedium, color = contentColor)
                }

                Column {
                    Text(
                        reward.title,
                        style = MaterialTheme.typography.bodyMedium,
                        color = contentColor,
                        maxLines = 2
                    )
                    Text(
                        "⭐ ${reward.points} pts",
                        style = MaterialTheme.typography.labelSmall,
                        color = SubtleText
                    )
                }

                Button(
                    onClick = onRedeem,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = isEnabled,
                    contentPadding = PaddingValues(0.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isEnabled) MistBlueLight else MistGray,
                        contentColor = if (isEnabled) Color.Black else Color.DarkGray
                    )
                ) {
                    Text("Canjear", style = MaterialTheme.typography.labelMedium)
                }
            }
        }

        // Show delete button if toggled
        if (showDelete) {
            IconButton(
                onClick = onDelete,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = (-8).dp, y = 8.dp)
                    .background(Color.Red, CircleShape)
                    .size(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Remove,
                    contentDescription = "Delete",
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}
