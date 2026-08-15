package com.sebastianfiser.fitnesscoach.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.ExperimentalFoundationApi
import io.appwrite.models.Document
import androidx.navigation.NavController
import com.sebastianfiser.fitnesscoach.models.AppViewModel
import com.sebastianfiser.fitnesscoach.navigation.Screen
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.foundation.border
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.LaunchedEffect
import com.sebastianfiser.fitnesscoach.models.ScheduleEntity
import com.sebastianfiser.fitnesscoach.models.SyncState
import com.sebastianfiser.fitnesscoach.models.Appwrite

@Composable
@OptIn(ExperimentalFoundationApi::class)
fun schduleScreen( viewModel: AppViewModel, navController: NavController) {
    var userId: String = ""
    val allDays = listOf("Mo", "Tu", "We", "Th", "Fr", "Sa", "Su")
    val todayIndex = LocalDate.now().dayOfWeek.value - 1
    val orderedDays = allDays.drop(todayIndex) + allDays.take(todayIndex)
    val sortedEntries = viewModel.scheduleByDay.entries.toList().sortedBy { orderedDays.indexOf(it.key) }
    val unit = viewModel.unit

    val scheduleItems by viewModel.getScheduleState(userId).collectAsState(initial = emptyList())
    val syncState by viewModel.syncState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    val scheduleByDay = scheduleItems.groupBy { it.day }

    LaunchedEffect(Unit) {
        val user = Appwrite.getCurrentUser()
        userId = user?.id ?: ""
    }

    LaunchedEffect(userId) {
        viewModel.syncSchedule(userId)
    }

    LaunchedEffect(syncState) {
        when (val state = syncState) {
            is SyncState.Syncing -> snackbarHostState.showSnackbar("Syncing schedule...")
            is SyncState.Synced -> snackbarHostState.showSnackbar("Schedule synced successfully")
            is SyncState.Error -> snackbarHostState.showSnackbar("Failed to sync schedule")
            else -> {}
        }
    }

    Scaffold (
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(top = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(bottom = 90.dp),
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Box (
                        modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.surface)
                    ) {
                        Text(
                            "Your Schedule",
                            color = MaterialTheme.colorScheme.onSurface,
                            style = MaterialTheme.typography.headlineMedium,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }

                    TextButton(
                        onClick = {
                            viewModel.isEditing = true
                            navController.navigate(Screen.SetupSchedule.route)
                        },
                        colors = ButtonDefaults.textButtonColors(contentColor = Color.White),
                        modifier = Modifier.padding(start = 16.dp)
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit Schedule", tint = MaterialTheme.colorScheme.onBackground)
                        Text("Edit", color = MaterialTheme.colorScheme.onBackground)
                    }

                }
            }
            stickyHeader {
                DrawDayrow()
            }
            items(sortedEntries) { day ->
                displayDaySchedule(
                    exercise = day.value.map { it.toEntity() },
                    day = day.key,
                    unit = unit,
                    viewModel = viewModel
                )
            }
        }
    }
}

@Composable
fun DrawDayrow() {
    val dayToday = LocalDate.now().dayOfWeek
    val Locale = java.util.Locale.getDefault()
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val days = mapOf(
                "Mon" to "Monday",
                "Tue" to "Tuesday",
                "Wed" to "Wednesday",
                "Thu" to "Thursday",
                "Fri" to "Friday",
                "Sat" to "Saturday",
                "Sun" to "Sunday")

                days.forEach { (shortName, fullName) ->
                    val isToday = fullName.uppercase(Locale) == dayToday.name
                    val color = if (isToday) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            shortName,
                            color = color,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(color)
                        )
                    }
                }
        }
    }
}

@Composable
fun displayDaySchedule(exercise: List<ScheduleEntity>, day: String, unit: String, viewModel: AppViewModel) {
    //var day = LocalDate.now().dayOfWeek
    //var dayNuminMonth = LocalDate.now().dayOfMonth
    var month = LocalDate.now().monthValue
    val dayLabels = mapOf(
        "Mo" to "Monday",
        "Tu" to "Tuesday",
        "We" to "Wednesday",
        "Th" to "Thursday",
        "Fr" to "Friday",
        "Sa" to "Saturday",
        "Su" to "Sunday"
    )
    val dayName = dayLabels[day] ?: day
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(28.dp)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(28.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text (
                "$dayName",
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            exercise.forEach { exercise ->
                    scheduleExerciseRow(exercise, unit, viewModel)
                }
        }
    }
}

@Composable
fun scheduleExerciseRow(exercise: ScheduleEntity, unit: String, viewModel: AppViewModel) {
    HorizontalDivider(color = MaterialTheme.colorScheme.outline, thickness = 0.5.dp, modifier = Modifier.padding(vertical = 8.dp))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = exercise.exerciseName as String,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface
        )

        Text(
            "${viewModel.GetWeightDisplay(exercise.weight)} ${if (unit == "kg") "kg" else "lbs"}",
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = if (exercise.weight > 0f) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
            },
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(end = 8.dp)
        )

    }
}

@Composable
fun DayItem(
    dayName: String,
    isSelected: Boolean = false,
    hasWorkout: Boolean = false,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
        modifier = Modifier
            .padding(horizontal = 2.dp)
            .width(44.dp)
            .height(64.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.padding(vertical = 6.dp)
        ) {
            Text(
                text = dayName,
                fontSize = 13.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
            )

            Box (
                modifier = Modifier
                    .size(6.dp)
                    .background(
                        color = when {
                            isSelected -> MaterialTheme.colorScheme.onPrimary
                            hasWorkout -> MaterialTheme.colorScheme.primary
                            else -> Color.Transparent
                        },
                        shape = CircleShape
                    )
            )
        }
    }
}
