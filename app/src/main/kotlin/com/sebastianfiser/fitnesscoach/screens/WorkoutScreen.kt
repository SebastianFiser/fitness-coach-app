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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Add
import kotlinx.coroutines.delay
import androidx.compose.runtime.LaunchedEffect
import com.sebastianfiser.fitnesscoach.models.SetEntry
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.draw.alpha


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutScreen(onFinish: () -> Unit) {
    var day = LocalDate.now().dayOfWeek.toString().lowercase().replaceFirstChar { it.uppercase() }
    val currentDay = weekData.find { it.day == day }
    var currentExerciseIndex by remember { mutableStateOf(0) }
    val currentExercise = currentDay?.exercises?.getOrNull(currentExerciseIndex)
    val totalExercises = currentDay?.exercises?.size ?: 0
    var timerRunningForSet by remember { mutableStateOf(-1) }
    var restTimeSeconds by remember { mutableStateOf(90) } //Seconds
    val setData = remember(currentExercise) {
        mutableStateListOf(*Array(currentExercise?.sets ?:0) {SetEntry(weight = "", reps = "")})
    }
    val focusManager = LocalFocusManager.current
    var scrollState = rememberScrollState()
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
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                ) {
                    Row (
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp),
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
                val (weight, reps, isDone) = setData[setIndex]
                val isSetActive = (setIndex == 0 || setData[setIndex - 1].isDone) && timerRunningForSet == -1
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .alpha(if (isSetActive) 1f else 0.4f)
                        .border(2.dp, if (isDone) Color.Green else Color.DarkGray, RoundedCornerShape(14.dp)),
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
                            readOnly = !isSetActive,
                            onValueChange = { setData[setIndex] = setData[setIndex].copy(weight = it) },
                            label = { Text("Weight (kg)", color = Color.LightGray, style = MaterialTheme.typography.bodySmall) },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            shape = RoundedCornerShape(14.dp),
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = Color.LightGray,
                                unfocusedBorderColor = Color.LightGray,
                                focusedLabelColor = Color.Gray,
                                unfocusedLabelColor = Color.DarkGray,
                                cursorColor = Color.White
                            ),
                            modifier = Modifier
                                .width(90.dp)
                        )
                        OutlinedTextField(
                            value = reps,
                            readOnly = !isSetActive,
                            onValueChange = { setData[setIndex] = setData[setIndex].copy(reps = it) },
                            label = { Text("Reps", color = Color.LightGray, style = MaterialTheme.typography.bodySmall) },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            shape = RoundedCornerShape(14.dp),
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = Color.LightGray,
                                unfocusedBorderColor = Color.LightGray,
                                focusedLabelColor = Color.Gray,
                                unfocusedLabelColor = Color.DarkGray,
                                cursorColor = Color.White
                            ),
                            modifier = Modifier
                                .width(90.dp)
                        )
                        TextButton(
                            enabled = weight.isNotEmpty() && reps.isNotEmpty() && !isDone && (setIndex == 0 || setData[setIndex - 1].isDone ) && timerRunningForSet == -1,
                            onClick = { restTimeSeconds = 90; timerRunningForSet = setIndex; setData[setIndex] = setData[setIndex].copy(isDone = true); focusManager.clearFocus() },
                            colors = ButtonDefaults.buttonColors(
                                contentColor = Color.White,
                                containerColor = Color.Transparent,
                                disabledContentColor = Color.Gray
                                )
                        ) {
                            Icon(Icons.Default.Check, contentDescription = null)
                        }
                    }
                }
                if(timerRunningForSet == setIndex) {
                    TimerCard(
                        restTimeSeconds = restTimeSeconds,
                        onClose = { timerRunningForSet = -1 },
                        onAdd30Seconds = { restTimeSeconds += 30 }
                    )
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            OutlinedButton(
                onClick = { /* TODO: implement selecting different exercise */ },
                colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.Black, contentColor = Color.LightGray),
            ) { 
                Text("Different exerxise", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.width(4.dp))
                Icon(Icons.Default.Book, contentDescription = null, tint = Color.LightGray)
            }
            ElevatedButton(
                onClick = { 
                    if (currentExerciseIndex < totalExercises - 1) {
                        currentExerciseIndex++
                        timerRunningForSet = -1
                        restTimeSeconds = 90
                    } else {
                        onFinish()
                    }
                },
                enabled = (setData.all { it.isDone } && timerRunningForSet == -1),
                colors = ButtonDefaults.elevatedButtonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black,
                    disabledContainerColor = Color.DarkGray,
                    disabledContentColor = Color.Gray
                    )
            ) {
                Text(if (currentExerciseIndex < totalExercises - 1) "Next Exercise" else "Finish Workout")
            }
        }
        LaunchedEffect(timerRunningForSet) {
            if (timerRunningForSet != -1) {
                    while (restTimeSeconds > 0){
                    delay(1000L) //One sec
                    restTimeSeconds--
                }
                timerRunningForSet = -1
            }
        }
    }
}

@Composable
fun TimerCard(
    restTimeSeconds: Int,
    onClose: () -> Unit,
    onAdd30Seconds: () -> Unit
    ) {
    val minutes = restTimeSeconds / 60
    val seconds = restTimeSeconds % 60
    Column() {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF1C1C1E), RoundedCornerShape(16.dp))
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
            ) {
                ElevatedButton(
                    onClick = onClose,
                    colors =  ButtonDefaults.elevatedButtonColors(containerColor = Color.Transparent, contentColor = Color.White )
                ) {
                    Icon(Icons.Default.Close, contentDescription = null)
                }
                Box(
                    modifier = Modifier
                        .background(Color.White, RoundedCornerShape(180.dp))
                        .padding(horizontal = 24.dp, vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        String.format("%02d:%02d", minutes, seconds),
                        color = Color.Black,
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
                ElevatedButton(
                    onClick = { onAdd30Seconds() }, 
                    colors = ButtonDefaults.elevatedButtonColors(containerColor = Color.Transparent, contentColor = Color.White)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                }
            }
        }
    }
}
//I hope that people who created jetpack compose get hit by a bus (On accident ofc)