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

@Composable
@OptIn(ExperimentalFoundationApi::class)
fun schduleScreen( viewModel: AppViewModel, navController: NavController) {
    val allDays = listOf("Mo", "Tu", "We", "Th", "Fr", "Sa", "Su")
    val todayIndex = LocalDate.now().dayOfWeek.value - 1
    val orderedDays = allDays.drop(todayIndex) + allDays.take(todayIndex)
    val sortedEntries = viewModel.scheduleByDay.entries.toList().sortedBy { orderedDays.indexOf(it.key) }
    val unit = viewModel.unit
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
            displayDaySchedule(day.value, day.key, unit)
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
fun displayDaySchedule(exercise: List<Document<Map<String, Any>>>, day: String, unit: String) {
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
                    scheduleExerciseRow(exercise, unit)
                }
        }
    }
}

@Composable
fun scheduleExerciseRow(exercise: Document<Map<String, Any>>, unit: String) {
    val weight = when (val w = exercise.data["weight"]) {
        is Double -> w.toFloat()
        is Long -> w.toFloat()
        is Float -> w
        else -> 0f
    }
    HorizontalDivider(color = MaterialTheme.colorScheme.outline, thickness = 0.5.dp, modifier = Modifier.padding(vertical = 8.dp))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = exercise.data["exerciseName"] as String, color = MaterialTheme.colorScheme.onSurface)

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                "${convertUnit(weight, unit)} ${if (unit == "kg") "kg" else "lbs"}",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(end = 8.dp)
            )
        }
    }
}

fun convertUnit( weight: Float, unit: String): Float {
    if (unit != "kg") {
        return weight * 2.20462f
    } else {
        return weight
    }
}