package com.systems.notchi.presentation.rewards.screens

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.unit.sp
import com.systems.notchi.presentation.theme.CharcoalText
import com.systems.notchi.presentation.theme.CoolWhite
import com.systems.notchi.presentation.theme.LightGray
import com.systems.notchi.presentation.theme.MistBlueLight
import com.systems.notchi.presentation.theme.MistGrayLight
import com.systems.notchi.presentation.theme.SubtleText


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
                modifier = Modifier
                    .background(Color(0xFFEBEBEB)),
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Spacer(modifier = Modifier.weight(1f)) // Empty space to center the title
                        Text(
                            text = "⭐ $totalPoints points",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                color = MaterialTheme.colorScheme.onSurface,
                                fontSize = 18.sp
                            ),
                            modifier = Modifier.padding(end = 16.dp)
                        )
                    }
                },
                windowInsets = WindowInsets.statusBars.only(WindowInsetsSides.Horizontal)
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
fun SingleUseRewardCard(reward: RewardModel, onRedeem: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 6.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        colors = CardDefaults.cardColors(containerColor = CoolWhite),
        border = BorderStroke(1.dp, MistGrayLight)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            reward.icon?.let {
                Text(it, style = MaterialTheme.typography.titleMedium)
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    reward.title,
                    style = MaterialTheme.typography.bodyMedium,
                    color = CharcoalText
                )
                Text(
                    "⭐ ${reward.points} pts",
                    style = MaterialTheme.typography.labelSmall,
                    color = SubtleText
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = onRedeem,
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MistBlueLight)
            ) {
                Text("Canjear", style = MaterialTheme.typography.labelMedium)
            }
        }
    }
}


enum class SheetType { ADD_REWARD, ADD_BUNDLE, EDIT_REWARD, NONE }
