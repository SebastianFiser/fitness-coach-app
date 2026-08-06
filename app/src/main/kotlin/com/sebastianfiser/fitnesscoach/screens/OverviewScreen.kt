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
import androidx.compose.foundation.Canvas
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip
import java.time.LocalDate
import androidx.compose.ui.draw.alpha
import androidx.compose.foundation.border
import androidx.activity.compose.BackHandler
import com.sebastianfiser.fitnesscoach.models.AppViewModel
import com.sebastianfiser.fitnesscoach.ui.components.ComingSoonOverlay

@Composable
fun MainWorkoutCard(onStartWorkout: () -> Unit, isWorkoutDone: Boolean, viewModel: AppViewModel) {
    var day = LocalDate.now().dayOfWeek.toString().lowercase().replaceFirstChar { it.uppercase() }
    val Locale = java.util.Locale.getDefault()
    val dayMap = mapOf(
        "Monday" to "Mo",
        "Tuesday" to "Tu",
        "Wednesday" to "We",
        "Thursday" to "Th",
        "Friday" to "Fr",
        "Saturday" to "Sa",
        "Sunday" to "Su"
    )
    val todayKey = dayMap[day] ?: "Mo"
    val todayExerercises = viewModel.scheduleByDay[todayKey] ?: emptyList()
    BackHandler(enabled = true) {
        //ignore the action
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 80.dp)
            .background(MaterialTheme.colorScheme.background)
    ) {
        Spacer(modifier = Modifier.height(30.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 15.dp, bottom = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Good Morning, User!",
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Text(
                "Here's your workout plan for today",
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        ComingSoonOverlay (
            modifier = Modifier.padding(horizontal = 30.dp)
        ){
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                val outlineColor =  MaterialTheme.colorScheme.outline
                val primaryColor = MaterialTheme.colorScheme.primary
                Canvas(
                    modifier = Modifier
                        .size(200.dp)
                        .padding(20.dp)
                ) {
                    drawArc(
                        color = outlineColor,
                        startAngle = 135f,
                        sweepAngle = 270f,
                        useCenter = false,
                        style = Stroke(width = 10.dp.toPx(), cap = StrokeCap.Round)
                    )
                    drawArc(
                        color = primaryColor,
                        startAngle = 135f,
                        sweepAngle = 115f,
                        useCenter = false,
                        style = Stroke(width = 10.dp.toPx(), cap = StrokeCap.Round)
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .offset(y = (-7).dp)
                    ) {
                    Text(
                        "760",
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                        style = MaterialTheme.typography.headlineLarge
                    )
                    Text(
                        "Day Score",
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.labelMedium
                    )

                    Text(
                        "Feature coming soon!",
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp)
                    )
                }
            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(500.dp)
                .border(2.dp, if (isWorkoutDone) Color.Green else MaterialTheme.colorScheme.outline, RoundedCornerShape(14.dp))
                .alpha(if (isWorkoutDone) 0.4f else 1f),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(14.dp)
        ) {
            Column(modifier = Modifier
                .padding(20.dp)
                .verticalScroll(rememberScrollState())
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            "Workout Plan",
                            color = MaterialTheme.colorScheme.onSurface,
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(
                            "8:00 AM - 9:00 AM",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    ElevatedButton(
                        enabled = !isWorkoutDone,
                        onClick = { onStartWorkout() },
                        colors = ButtonDefaults.elevatedButtonColors(containerColor = MaterialTheme.colorScheme.primary, contentColor = MaterialTheme.colorScheme.onPrimary, disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f), disabledContentColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f)),
                        ) {
                            Text("Start")
                        }
                }

                Spacer(modifier = Modifier.height(20.dp))

                todayExerercises.forEach { doc ->
                    val exercise = Exercise(
                        name = doc.data["exerciseName"] as? String ?: "Unknown",
                        sets = (doc.data["sets"] as? Long)?.toInt() ?: 0,
                        reps = (doc.data["reps"] as? Long)?.toInt() ?: 0,
                        weight = when (val w = doc.data["weight"]) {
                            is Double -> w.toFloat()
                            is Long -> w.toFloat()
                            else -> 0f
                        }
                    )
                    ExerciseRow(exercise, viewModel)
                }
            }
        }
    }
}

@Composable
fun ExerciseRow(exercise: Exercise, viewModel: AppViewModel) {
    val weight = viewModel.convertUnit(exercise.weight, viewModel.unit)
    HorizontalDivider(color = MaterialTheme.colorScheme.outline, thickness = 0.5.dp, modifier = Modifier.padding(vertical = 8.dp))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = exercise.name, color = MaterialTheme.colorScheme.onSurface)

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                "${weight} ${viewModel.unit}",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(end = 8.dp)
            )
        }
    }
}
