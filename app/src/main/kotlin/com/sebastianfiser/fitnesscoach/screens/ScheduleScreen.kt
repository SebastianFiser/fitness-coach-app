package com.sebastianfiser.fitnesscoach.screens

import com.sebastianfiser.fitnesscoach.models.Exercise
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

@Composable
fun schduleScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(top = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
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

        DrawDayrow()
        displayDaySchedule()

    }
}

@Composable
fun DrawDayrow() {
    val dayToday = LocalDate.now().dayOfWeek
    val Locale = java.util.Locale.getDefault()
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

@Composable
fun displayDaySchedule(exercise: List<Exercise> = listOf(
    Exercise("Bench press 12x4",105.0f),
    Exercise("Squats 20x3",135.0f),
    Exercise("Deadlift 10x4",185.0f)
)) {
    var day = LocalDate.now().dayOfWeek
    var dayNuminMonth = LocalDate.now().dayOfMonth
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
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text (
                "$day, $dayNuminMonth.$month.",
                color = Color.White,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            exercise.forEach { exercise ->
                    ExerciseRow(exercise)
                }
        }
    }
}