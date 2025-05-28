package com.systems.notchi.presentation.rewards.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.systems.notchi.presentation.tasks.TaskViewModel
import org.koin.androidx.compose.koinViewModel
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.systems.notchi.domain.model.RewardModel
import com.systems.notchi.presentation.components.BottomNavigationBar
import com.systems.notchi.presentation.projects.ProjectViewModel
import com.systems.notchi.presentation.rewards.RewardsViewModel
import com.systems.notchi.presentation.rewards.components.AddRewardBottomSheet
import com.systems.notchi.presentation.rewards.components.ExpandableFab
import kotlinx.coroutines.launch
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.ui.unit.sp
import com.systems.notchi.presentation.theme.CharcoalText
import com.systems.notchi.presentation.theme.CoolWhite
import com.systems.notchi.presentation.theme.LightGray
import com.systems.notchi.presentation.theme.MistBlueLight
import com.systems.notchi.presentation.theme.MistGrayLight
import com.systems.notchi.presentation.theme.SubtleText


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun RewardsScreen(
    navController: NavController,
    rewardsViewModel: RewardsViewModel = koinViewModel(),
    taskViewModel: TaskViewModel = koinViewModel(),
    projectViewModel: ProjectViewModel = koinViewModel()
) {
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { 2 })
    val coroutineScope = rememberCoroutineScope()

    val projects by projectViewModel.projects.collectAsState()
    val totalPoints by taskViewModel.totalPoints.collectAsState()
    val rewards by rewardsViewModel.rewards.collectAsState()


    LaunchedEffect(Unit) {
        taskViewModel.loadUserPoints()
        projectViewModel.getAllProjects()
        rewardsViewModel.getAllRewards()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("⭐ $totalPoints puntos", style = MaterialTheme.typography.bodyLarge)
                }
            )
        },
        bottomBar = {
            BottomNavigationBar(selectedRoute = "rewards")
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                when (page) {
                    0 -> ReusableRewardsPage(
                        rewards = rewards.filter { it.reusable },
                        onRedeem = {
                            coroutineScope.launch {
                                rewardsViewModel.redeemReward(it)
                            }
                        }
                    )
                    1 -> SingleUseRewardsPage(
                        rewards = rewards.filter { !it.reusable },
                        onRedeem = {
                            coroutineScope.launch {
                                rewardsViewModel.redeemReward(it)
                            }
                        }
                    )
                }
            }
        }
    }
}


@Composable
fun ReusableRewardsPage(
    rewards: List<RewardModel>,
    onRedeem: (RewardModel) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 160.dp),
        contentPadding = PaddingValues(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(rewards) { reward ->
            ReusableRewardCard(
                reward = reward,
                onRedeem = { onRedeem(reward) }
            )
        }
    }
}

@Composable
fun ReusableRewardCard(reward: RewardModel, onRedeem: () -> Unit) {
    Card(
        modifier = Modifier
            .width(140.dp)
            .height(160.dp)
            .padding(6.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = CoolWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        border = BorderStroke(1.dp, MistGrayLight)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            reward.icon?.let {
                Text(it, style = MaterialTheme.typography.titleMedium)
            }

            Column {
                Text(
                    reward.title,
                    style = MaterialTheme.typography.bodyMedium,
                    color = CharcoalText,
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
                contentPadding = PaddingValues(0.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MistBlueLight)
            ) {
                Text("Canjear", style = MaterialTheme.typography.labelMedium)
            }
        }
    }
}

@Composable
fun SingleUseRewardsPage(
    rewards: List<RewardModel>,
    onRedeem: (RewardModel) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(rewards) { reward ->
            TicketRewardItem(
                reward = reward,
                onRedeem = { onRedeem(reward) }
            )
        }
    }
}

@Composable
fun TicketRewardItem(
    reward: RewardModel,
    onRedeem: () -> Unit
) {
    val isRedeemed = reward.redeemed
    val shape = if (isRedeemed) {
        // Simulate "torn" ticket by altering shape
        CutCornerShape(topEnd = 12.dp, bottomEnd = 12.dp)
    } else {
        RoundedCornerShape(12.dp)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
        shape = shape,
        colors = CardDefaults.cardColors(containerColor = CoolWhite),
        border = BorderStroke(1.dp, MistGrayLight)
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

            if (!isRedeemed) {
                Button(
                    onClick = onRedeem,
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MistBlueLight)
                ) {
                    Text("Canjear", style = MaterialTheme.typography.labelMedium)
                }
            } else {
                Icon(Icons.Default.Done, contentDescription = "Redeemed", tint = Color.Gray)
            }
        }
    }
}


enum class SheetType { ADD_REWARD, ADD_BUNDLE, EDIT_REWARD, NONE }
