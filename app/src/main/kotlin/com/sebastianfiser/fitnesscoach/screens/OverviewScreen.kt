package com.sebastianfiser.fitnesscoach.screens

import androidx.compose.runtime.Composable
import com.sebastianfiser.fitnesscoach.models.Excercise
import androidx.compose.ui.Modifier

@Composable
fun MainWorkoutCard(exercises: List<Exercise>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 80.dp)
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
                color = Color.White,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Text(
                "Here's your workout plan for today",
                color = Color.LightGray,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Canvas(
                modifier = Modifier
                    .size(200.dp)
                    .padding(20.dp)
            ) {
                drawArc(
                    color = Color.DarkGray,
                    startAngle = 135f,
                    sweepAngle = 265f,
                    useCenter = false,
                    style = Stroke(width = 10.dp.toPx(), cap = StrokeCap.Round)
                )
                drawArc(
                    color = Color.White,
                    startAngle = 135f,
                    sweepAngle = 115f,
                    useCenter = false,
                    style = Stroke(width = 10.dp.toPx(), cap = StrokeCap.Round)
                )
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .offset(y = (-10).dp)
                ) {
                Text(
                    "760",
                    color = Color.Gray,
                    style = MaterialTheme.typography.headlineLarge
                )
                Text(
                    "Day Score",
                    color = Color.White,
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1C1C1E)),
            shape = RoundedCornerShape(28.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            "Workout Plan",
                            color = Color.White,
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(
                            "8:00 AM - 9:00 AM",
                            color = Color.Gray,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    ElevatedButton(
                        onClick = { },
                        colors = ButtonDefaults.elevatedButtonColors(containerColor = Color.White, contentColor = Color.Black)
                        ) {
                            Text("Start")
                        }
                }

                Spacer(modifier = Modifier.height(20.dp))

                exercises.forEach { exercise ->
                    ExerciseRow(exercise)
                }
            }
        }
    }
}

@Composable
fun ExerciseRow(exercise: Exercise) {
    HorizontalDivider(color = Color.Gray, thickness = 0.5.dp, modifier = Modifier.padding(vertical = 8.dp))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = exercise.name, color = Color.White)

        Row(verticalAlignment = Alignment.CenterVertically) {
            if(exercise.Weight != null) {
                Text(
                    "${exercise.Weight} kg",
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(end = 8.dp)
                )
            } else {
                Checkbox(
                    checked = exercise.isDone,
                    onCheckedChange = null, // Tady by v budoucnu byla logika kliknutí
                    colors = CheckboxDefaults.colors(
                        checkedColor = Color.White,
                        uncheckedColor = Color.Gray,
                        checkmarkColor = Color.Black
                    )
                )
            }
            
        }
    }
}