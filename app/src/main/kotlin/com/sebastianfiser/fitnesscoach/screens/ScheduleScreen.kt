package com.sebastianfiser.fitnesscoach.screens

import com.sebastianfiser.fitnesscoach.models.Exercise
import com.sebastianfiser.fitnesscoach.models.Day
import com.sebastianfiser.fitnesscoach.models.weekData
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

@Composable
@OptIn(ExperimentalFoundationApi::class)
fun schduleScreen() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(top = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(bottom = 90.dp),
    ) {
        item {
            Box (
                modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(Color.DarkGray)
            ) {
                Text(
                    "Your Schedule",
                    color = Color.White,
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        }
        stickyHeader {
            DrawDayrow()
        }
        items(weekData) { day ->
            displayDaySchedule(day.exercises, day.day)
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
            .background(Color.Black)
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
                    val color = if (isToday) Color.White else Color.DarkGray
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
fun displayDaySchedule(exercise: List<Exercise>, day: String) {
    //var day = LocalDate.now().dayOfWeek
    //var dayNuminMonth = LocalDate.now().dayOfMonth
    var month = LocalDate.now().monthValue 
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1C1C1E)),
        shape = RoundedCornerShape(28.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text (
                "$day",
                color = Color.White,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            exercise.forEach { exercise ->
                    ExerciseRow(exercise)
                }
        }
    }
}