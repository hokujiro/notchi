package com.example.madetoliveapp.presentation.rewards.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.madetoliveapp.presentation.tasks.TaskViewModel
import org.koin.androidx.compose.koinViewModel
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.madetoliveapp.domain.model.RewardModel
import com.example.madetoliveapp.presentation.components.BottomNavigationBar
import com.example.madetoliveapp.presentation.projects.ProjectViewModel
import com.example.madetoliveapp.presentation.rewards.components.AddRewardBottomSheet
import com.example.madetoliveapp.presentation.rewards.components.ExpandableFab
import com.example.madetoliveapp.presentation.tasks.components.CalendarHeader
import com.example.madetoliveapp.presentation.tasks.components.CircularFloatingMenu
import com.example.madetoliveapp.presentation.tasks.components.HeaderComponent
import com.example.madetoliveapp.presentation.tasks.screens.AddFailBottomSheet
import com.example.madetoliveapp.presentation.tasks.screens.AddTaskBottomSheet
import com.example.madetoliveapp.presentation.tasks.screens.FrameListBottomSheet
import com.example.madetoliveapp.presentation.tasks.screens.TaskEditBottomSheet
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RewardsScreen(
    navController: NavController,
    taskViewModel: TaskViewModel = koinViewModel(),
    projectViewModel: ProjectViewModel = koinViewModel()
) {
    var currentSheet by remember { mutableStateOf(SheetType.NONE) }
    var isFabExpanded by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    val projects by projectViewModel.projects.collectAsState()
    val totalPoints by taskViewModel.totalPoints.collectAsState()

    LaunchedEffect(Unit) {
        taskViewModel.loadUserPoints()
        projectViewModel.getAllProjects()
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
        bottomBar = { BottomNavigationBar(selectedRoute = "projects") },
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
                            taskViewModel.addTask(it)
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

        Column(
            modifier = Modifier
                .fillMaxSize()  // <-- important!!!
                .padding(paddingValues)
        ) {
            HeaderComponent(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 100.dp),
                totalPoints = totalPoints
            )
        }

    }

    @Composable
    fun CardGridScreen(items: List<RewardModel>) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(items) { item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Text(
                            text = item.title,
                            style = MaterialTheme.typography.titleMedium,
                            maxLines = 1
                        )

                    }
                }
            }
        }
    }
}

enum class SheetType { ADD_REWARD, ADD_BUNDLE, EDIT_REWARD, NONE }
