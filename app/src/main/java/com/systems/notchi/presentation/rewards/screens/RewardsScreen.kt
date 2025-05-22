package com.systems.notchi.presentation.rewards.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RewardsScreen(
    navController: NavController,
    rewardsViewModel: RewardsViewModel = koinViewModel(),
    taskViewModel: TaskViewModel = koinViewModel(),
    projectViewModel: ProjectViewModel = koinViewModel()
) {
    var currentSheet by remember { mutableStateOf(SheetType.NONE) }
    var isFabExpanded by remember { mutableStateOf(false) }
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
                modifier = Modifier.background(Color(0xFFEBEBEB)),
                title = {

                },
                windowInsets = WindowInsets.statusBars.only(WindowInsetsSides.Horizontal) // Removes default top padding
            )
        },
        bottomBar = { BottomNavigationBar(selectedRoute = "rewards") },
        floatingActionButton = {
            ExpandableFab(
                onActionClick = { index ->
                    isFabExpanded = false
                    currentSheet = when (index) {
                        0 -> SheetType.ADD_REWARD
                        1 -> SheetType.ADD_BUNDLE
                        else -> SheetType.NONE
                    }
                },
                onToggle = { isFabExpanded = !isFabExpanded },
                isExpanded = isFabExpanded,
            )
        }
    ) { paddingValues ->

        when (currentSheet) {
            SheetType.ADD_REWARD -> {
                AddRewardBottomSheet(
                    onDismiss = { currentSheet = SheetType.NONE },
                    onAddReward = {

                        coroutineScope.launch {
                            rewardsViewModel.addReward(it)
                            currentSheet = SheetType.NONE
                        }
                    },
                    projects = projects,
                )
            }

            SheetType.ADD_BUNDLE -> {}

            SheetType.EDIT_REWARD -> {}

            SheetType.NONE -> {}
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            item {
                Text(
                    "Recompensas disponibles",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(16.dp)
                )
            }

            // Rewards Reusables
            if (rewards.any { it.reusable }) {
                item {
                    Text("Reusables", modifier = Modifier.padding(start = 16.dp))
                    LazyRow(modifier = Modifier.padding(8.dp)) {
                        items(rewards.filter { it.reusable }) { reward ->
                            ReusableRewardCard(
                                reward = reward,
                                onRedeem = {
                                    coroutineScope.launch {
                                        rewardsViewModel.redeemReward(reward)
                                    }
                                }
                            )
                        }
                    }
                }
            }

            // Rewards de un solo uso
            if (rewards.any { !it.reusable }) {
                item {
                    Text("Uso único", modifier = Modifier.padding(start = 16.dp, top = 16.dp))
                }
                items(rewards.filter { !it.reusable && !it.redeemed }) { reward ->
                    SingleUseRewardCard(
                        reward = reward,
                        onRedeem = {
                            coroutineScope.launch {
                                rewardsViewModel.redeemReward(reward)
                            }
                        }
                    )
                }
            }
        }
    }
}


@Composable
fun ReusableRewardCard(reward: RewardModel, onRedeem: () -> Unit) {
    Card(
        modifier = Modifier
            .size(180.dp)
            .padding(8.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            if (reward.icon != null) Text(reward.icon, style = MaterialTheme.typography.titleLarge)
            Text(reward.title, style = MaterialTheme.typography.titleMedium)
            Text("${reward.points} pts", style = MaterialTheme.typography.bodyMedium)
            Button(onClick = onRedeem) {
                Text("Canjear")
            }
        }
    }
}

@Composable
fun SingleUseRewardCard(reward: RewardModel, onRedeem: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            if (reward.icon != null) Text(reward.icon, style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(reward.title, style = MaterialTheme.typography.titleMedium)
                Text("${reward.points} pts", style = MaterialTheme.typography.bodySmall)
            }
            Spacer(modifier = Modifier.weight(1f))
            Button(onClick = onRedeem) {
                Text("Canjear")
            }
        }
    }
}


enum class SheetType { ADD_REWARD, ADD_BUNDLE, EDIT_REWARD, NONE }
