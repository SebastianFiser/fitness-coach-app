package com.sebastianfiser.fitnesscoach.screens

import com.sebastianfiser.fitnesscoach.models.Exercise
import com.sebastianfiser.fitnesscoach.models.Day
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

val weekData = listOf(
    Day("Monday", listOf(
        Exercise("Bench press 12x4",105.0f),
        Exercise("Peck deck 20x3",50.0f),
        Exercise("Dumbell flyes 10x4",35.0f),
        Exercise("Cable cross 15x3",40.0f),
    )),
    Day("Tuesday", listOf(
        Exercise("Squats 20x3",135.0f),
        Exercise("Leg press 15x4",180.0f),
        Exercise("Lunges 12x3",60.0f)
    )),
    Day("Wednesday", listOf(
        Exercise("Deadlift 10x4",185.0f),
        Exercise("Barbell row 12x3",95.0f),
        Exercise("Lat pulldown 15x4",70.0f)
    )),
    Day("Thursday", listOf(
        Exercise("Overhead press 12x4",60.0f),
        Exercise("Lateral raises 15x3",20.0f),
        Exercise("Front raises 10x4",25.0f)
    )),
    Day("Friday", listOf(
        Exercise("Barbell curls 12x4",40.0f),
        Exercise("Hammer curls 15x3",30.0f),
        Exercise("Preacher curls 10x4",35.0f)
    )),
    Day("Saturday", listOf(
        Exercise("Tricep pushdown 12x4",50.0f),
        Exercise("Overhead tricep extension 15x3",40.0f),
        Exercise("Dips 10x4", 0.0f)
    )),
    Day("Sunday", listOf(
        Exercise("Rest day", 0.0f)
    ))
)

@Composable
fun schduleScreen() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(top = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(bottom = 80.dp),
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
        item {
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