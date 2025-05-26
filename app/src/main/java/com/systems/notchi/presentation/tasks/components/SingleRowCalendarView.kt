package com.systems.notchi.presentation.tasks.components


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.systems.notchi.presentation.extensions.CalendarDataSource
import com.systems.notchi.presentation.extensions.toLocalDate
import com.systems.notchi.presentation.model.CalendarUiModel
import com.systems.notchi.presentation.tasks.TaskViewModel
import com.systems.notchi.presentation.theme.MistBlueLight
import com.systems.notchi.presentation.theme.MistGrayLight
import com.systems.notchi.presentation.theme.PositiveTaskChecked
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Composable
fun CalendarHeader(
    modifier: Modifier = Modifier,
    selectedDate: Long,
    onDateSelected: (Long) -> Unit,
    dailyPointsMap: Map<LocalDate, Int>,
    onVisibleWeekChange: (startDate: LocalDate, endDate: LocalDate) -> Unit// <-- add this
) {

    val dataSource = CalendarDataSource()
    var calendarUiModel by remember { mutableStateOf(dataSource.getData(lastSelectedDate = selectedDate.toLocalDate())) }
    val listState = rememberLazyListState()
    LaunchedEffect(calendarUiModel.startDate.date, calendarUiModel.endDate.date) {
        onVisibleWeekChange(
            calendarUiModel.startDate.date,
            calendarUiModel.endDate.date
        )
    }

    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
        Header(
            data = calendarUiModel,
            onPrevClickListener = { startDate ->
                val finalStartDate = startDate.minusDays(1)
                calendarUiModel = dataSource.getData(
                    startDate = finalStartDate,
                    lastSelectedDate = calendarUiModel.selectedDate.date
                )
                onVisibleWeekChange(finalStartDate, finalStartDate.plusDays(6))
            },
            onNextClickListener = { endDate ->
                val finalStartDate = endDate.plusDays(2)
                calendarUiModel = dataSource.getData(
                    startDate = finalStartDate,
                    lastSelectedDate = calendarUiModel.selectedDate.date
                )
                onVisibleWeekChange(finalStartDate, finalStartDate.plusDays(6))
            }
        )

        Content(
            data = calendarUiModel,
            listState = listState,
            onDateClickListener = { date ->
                calendarUiModel = calendarUiModel.copy(
                    selectedDate = date,
                    visibleDates = calendarUiModel.visibleDates.map {
                        it.copy(isSelected = it.date.isEqual(date.date))
                    }
                )
                onDateSelected(date.date.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli())
            },
            dailyPointsMap = dailyPointsMap // <-- pass it along
        )
    }
}

@Composable
fun Content(
    data: CalendarUiModel,
    listState: LazyListState,
    onDateClickListener: (CalendarUiModel.Date) -> Unit,
    dailyPointsMap: Map<LocalDate, Int>
) {
    LazyRow(
        state = listState,
        modifier = Modifier .fillMaxWidth()
            .height(200.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterHorizontally)
    ) {
        items(items = data.visibleDates) { day ->
            ContentItem(
                date = day,
                onClickListener = onDateClickListener,
                points = dailyPointsMap[day.date] ?: 0 // provide points for this day
            )
        }
    }
}


@Composable
fun ContentItem(
    date: CalendarUiModel.Date,
    onClickListener: (CalendarUiModel.Date) -> Unit,
    points: Int
) {
    val isSelected = date.isSelected

    val containerColor = when {
        isSelected -> MistBlueLight
        points > 0 -> PositiveTaskChecked
        points < 0 -> MistGrayLight
        else -> MaterialTheme.colorScheme.background
    }

    Card(
        modifier = Modifier
            .clickable { onClickListener(date) },
        colors = CardDefaults.cardColors(containerColor = containerColor),
        border = if (!isSelected) BorderStroke(1.dp, MaterialTheme.colorScheme.secondary) else null
    ) {
        Column(
            modifier = Modifier
                .width(60.dp)
                .height(100.dp)
                .padding(6.dp)
        ) {
            Text(
                text = date.day,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = date.date.dayOfMonth.toString(),
                modifier = Modifier.align(Alignment.CenterHorizontally),
                style = MaterialTheme.typography.bodyLarge,
            )
        }
    }
}

@Composable
fun Header(
    data: CalendarUiModel,
    onPrevClickListener: (LocalDate) -> Unit,
    onNextClickListener: (LocalDate) -> Unit,
) {
    Row {
        Text(
            text = data.selectedDate.date.format(
                DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)
            ),
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically),
            style = MaterialTheme.typography.headlineSmall
        )
        IconButton(onClick = {
            onPrevClickListener(data.startDate.date)
        }) {
            Icon(
                imageVector = Icons.Filled.ChevronLeft,
                contentDescription = "Back"
            )
        }
        IconButton(onClick = {
            onNextClickListener(data.endDate.date)
        }) {
            Icon(
                imageVector = Icons.Filled.ChevronRight,
                contentDescription = "Next"
            )
        }
    }
}

