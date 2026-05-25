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
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.Icons
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Check
import androidx.

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutScreen(onFinish: () -> Unit) {
    var day = LocalDate.now().dayOfWeek.toString().lowercase().replaceFirstChar { it.uppercase() }
    val currentDay = weekData.find { it.day == day }
    var currentExerciseIndex by remember { mutableStateOf(0) }
    val currentExercise = currentDay?.exercises?.getOrNull(currentExerciseIndex)
    val totalExercises = currentDay?.exercises?.size ?: 0
    val setData = remember(currentExercise) {
        mutableStateListOf(*Array(currentExercise?.sets ?:0) {Pair("", "") })
    }
    var scrollState = rememberScrollState
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(bottom = 80.dp, top = 48.dp)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState),
        ) {
            Card (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .border(2.dp, Color.DarkGray, RoundedCornerShape(14.dp)),
                colors = CardDefaults.cardColors(Color(0xFF1C1C1E)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column {
                    Row (
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Column {
                            LinearProgressIndicator(
                                progress = { currentExerciseIndex.toFloat() / totalExercises.toFloat() },
                                modifier = Modifier.fillMaxWidth(),
                                color = Color.White,
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
                    Text(
                        currentExercise?.name ?: "No exercise",
                        color = Color.White,
                        style = MaterialTheme.typography.headlineLarge,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                "Goal: ${currentExercise?.sets} Sets x ${currentExercise?.reps} Reps ",
                                color = Color.LightGray,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                "@ ${currentExercise?.weight} kg",
                                color = Color.LightGray,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        TextButton(
                            onClick = {},
                            colors = ButtonDefaults.textButtonColors(contentColor = Color.LightGray)
                        ) {
                            Icon(Icons.Default.MenuBook, contentDescription = null, tint = Color.LightGray)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "Show Form Guide", style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
            repeat (currentExercise?.sets ?: 0) { setIndex ->
                val (weight, reps) = setData[setIndex]
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .border(2.dp, Color.DarkGray, RoundedCornerShape(14.dp)),
                    colors = CardDefaults.cardColors(Color(0xFF1C1C1E)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Set ${setIndex + 1}",
                            color = Color.White,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        OutlinedTextField(
                            value = weight,
                            onValueChange = { setData[setIndex] = Pair(it, reps) },
                            placeholder = { Text("Weight (kg)", color = Color.LightGray, style = MaterialTheme.typography.bodySmall) },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = Color.LightGray,
                                unfocusedBorderColor = Color.LightGray,
                                focusedLabelColor = Color.Gray,
                                unfocusedLabelColor = Color.DarkGray,
                                cursorColor = Color.White
                            ),
                            modifier = Modifier.width(125.dp)
                        )
                        OutlinedTextField(
                            value = reps,
                            onValueChange = { setData[setIndex] = Pair(weight, it) },
                            placeholder = { Text("Reps", color = Color.LightGray, style = MaterialTheme.typography.bodySmall) },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = Color.LightGray,
                                unfocusedBorderColor = Color.LightGray,
                                focusedLabelColor = Color.Gray,
                                unfocusedLabelColor = Color.DarkGray,
                                cursorColor = Color.White
                            ),
                            modifier = Modifier.width(125.dp)
                        )
                        Button(
                            onClick = { /* TODO: implement set completion logic */ },
                            colors = ButtonDefaults.buttonColors(contentColor = Color.White)
                        ) {
                            Icon(Icons.Default.Check, contentDescription = null)
                        }
                    }
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextButton(
                onClick = { /* TODO: implement selecting different exercise */ },
                colors = ButtonDefaults.textButtonColors(contentColor = Color.LightGray),
                modifier = Modifier
                    .border(1.dp, Color.White, RoundedCornerShape(8.dp))
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) { 
                Text("Select different exerxise", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.width(4.dp))
                Icon(Icons.Default.Book, contentDescription = null, tint = Color.LightGray)
            }
            ElevatedButton(
                onClick = { 
                    if (currentExerciseIndex < totalExercises - 1) {
                        currentExerciseIndex++
                    } else {
                        onFinish()
                    }
                },
                colors = ButtonDefaults.elevatedButtonColors(containerColor = Color.White, contentColor = Color.Black)
            ) {
                Text(if (currentExerciseIndex < totalExercises - 1) "Next Exercise" else "Finish Workout")
            }
        }
    }
}