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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
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
import com.systems.notchi.presentation.theme.CoolWhite
import com.systems.notchi.presentation.theme.MistBlueLight
import com.systems.notchi.presentation.theme.MistGray
import com.systems.notchi.presentation.theme.MistGrayLight
import kotlinx.coroutines.launch

@Composable
fun SingleUseRewardsPage(
    rewards: List<RewardModel>,
    onRedeem: (RewardModel) -> Unit,
    userTotalPoints: Int,
    rewardsViewModel: RewardsViewModel
) {
    val coroutineScope = rememberCoroutineScope()
    val deleteStates = remember { mutableStateMapOf<String, Boolean>() }

    val availableRewards = rewards.filter { !it.redeemed }
    val redeemedRewards = rewards.filter { it.redeemed }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (availableRewards.isNotEmpty()) {
            item {
                Text("Available", style = MaterialTheme.typography.titleMedium)
            }
            items(availableRewards) { reward ->
                val showDelete = deleteStates[reward.id] == true
                TicketRewardItem(
                    reward = reward,
                    isEnabled = userTotalPoints >= (reward.points ?: 0),
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
                    onRedeem = { onRedeem(reward) }
                )
            }
        }

        if (redeemedRewards.isNotEmpty()) {
            item {
                Spacer(modifier = Modifier.height(12.dp))
                Text("Redeemed", style = MaterialTheme.typography.titleMedium)
            }
            items(redeemedRewards) { reward ->
                val showDelete = deleteStates[reward.id] == true
                TicketRewardItem(
                    reward = reward,
                    isEnabled = false,
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
                    onRedeem = {}
                )
            }
        }

        if (availableRewards.isEmpty() && redeemedRewards.isEmpty()) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 64.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.no_rewards),
                        contentDescription = "No rewards",
                        modifier = Modifier.size(300.dp)
                    )
                    Text(
                        text = "No rewards yet,\nadd your first one!",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TicketRewardItem(
    reward: RewardModel,
    isEnabled: Boolean = true,
    showDelete: Boolean = false,
    onLongPress: () -> Unit = {},
    onDelete: () -> Unit = {},
    onRedeem: () -> Unit = {}
) {
    val shape = if (reward.redeemed) {
        CutCornerShape(topEnd = 12.dp, bottomEnd = 12.dp)
    } else {
        RoundedCornerShape(12.dp)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .combinedClickable(
                onClick = {}, // optional normal click
                onLongClick = onLongPress
            )
    ) {
        Card(
            shape = shape,
            colors = CardDefaults.cardColors(containerColor = if (isEnabled) CoolWhite else MistGrayLight),
            border = BorderStroke(1.dp, MistGray),
            modifier = Modifier.fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                reward.icon?.let {
                    Text(it, style = MaterialTheme.typography.titleMedium)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(reward.title, style = MaterialTheme.typography.bodyMedium)
                    Text("⭐ ${reward.points} pts", style = MaterialTheme.typography.labelSmall)
                }

                if (!reward.redeemed) {
                    Button(
                        onClick = onRedeem,
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isEnabled) MistBlueLight else MistGray,
                            contentColor = if (isEnabled) Color.Black else Color.DarkGray
                        ),
                        enabled = isEnabled
                    ) {
                        Text("Canjear", style = MaterialTheme.typography.labelMedium)
                    }
                } else {
                    Icon(Icons.Default.Done, contentDescription = "Redeemed", tint = Color.Gray)
                }
            }
        }

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