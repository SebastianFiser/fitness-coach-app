package com.sebastianfiser.fitnesscoach.screens

import com.sebastianfiser.fitnesscoach.models.weekData
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import com.sebastianfiser.fitnesscoach.models.Exercise
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import androidx.compose.foundation.Canvas
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf

@Composable
fun WorkoutScreen(onFinish: () -> Unit) {
    var day = LocalDate.now().dayOfWeek.toString().lowercase().replaceFirstChar { it.uppercase() }
    val currentDay = weekData.find { it.day == day }
    var currentExerciseIndex by remember { mutableStateOf(0) }
    val currentExcercise = currentDay?.exercises?.getOrNull(currentExerciseIndex)
    val totalExercises = currentDay?.excercises?.size ?: 0
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Card (
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(Color.DarkGray),
            shape = RoundedCornerShape(16.dp)
        ) {
            Row (
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                ) {
                    LinearProgressIndicator(
                        progress = { currentExerciseIndex.toFloat() / totalExercises.toFloat() },
                        modifier = Modifier.fillMaxWidth(),
                        color = Color.Green,
                        trackColor = Color.DarkGray
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Excercise ${currentExerciseIndex + 1} of $totalExercises",
                        color = Color.LightGray,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}