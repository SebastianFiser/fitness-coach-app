package com.sebastianfiser.fitnesscoach.screens

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
import androidx.activity.compose.BackHandler
import androidx.navigation.NavController
import com.sebastianfiser.fitnesscoach.models.AppViewModel
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import com.sebastianfiser.fitnesscoach.models.Appwrite

data class ExerciseProgress(
    val isDone: Boolean = false,
    val isSkipped: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutScreen(onFinish: () -> Unit, navController: NavController, viewModel: AppViewModel) {
    val dayMap = mapOf(
        "Monday" to "Mo",
        "Tuesday" to "Tu",
        "Wednesday" to "We",
        "Thursday" to "Th",
        "Friday" to "Fr",
        "Saturday" to "Sa",
        "Sunday" to "Su"
    )
    var day = LocalDate.now().dayOfWeek.toString().lowercase().replaceFirstChar { it.uppercase() }
    val dayKey = dayMap[day] ?: "Mo"
    val exercises = viewModel.scheduleByDayExercises[dayKey] ?: emptyList()
    var currentExerciseIndex by remember { mutableStateOf(0) }
    val currentExercise = exercises.getOrNull(currentExerciseIndex)
    val totalExercises = exercises.size
    var timerRunningForSet by remember { mutableStateOf(-1) }
    var restTimeSeconds by remember { mutableStateOf(viewModel.restTime) }
    var showExitDialog by remember { mutableStateOf(false) }
    val setData = remember(currentExercise) {
        mutableStateListOf(*Array(currentExercise?.sets ?:0) {SetEntry(entryWeight = "", entryReps = "")})
    }
    val focusManager = LocalFocusManager.current
    var scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()
    var workoutId by remember { mutableStateOf("") }
    val exerciseProgress = remember {
        mutableStateListOf(*Array(exercises.size) { ExerciseProgress() })
    }
    val unfinishedIndices: List<Int> = exerciseProgress.indices.filter { !exerciseProgress[it].isDone }
    val allExercisesDone = exerciseProgress.isNotEmpty() && exerciseProgress.all { it.isDone }

    BackHandler { showExitDialog = true }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(bottom = 20.dp, top = 48.dp)
    ) {
        if (showExitDialog) {
            AlertDialog(
                containerColor = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(16.dp),
                onDismissRequest = { showExitDialog = false },
                title = { Text("Exit Workout", color = MaterialTheme.colorScheme.onSurface) },
                text = { Text("Are you sure you want to exit the workout?", color = MaterialTheme.colorScheme.onSurfaceVariant) },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showExitDialog = false
                            navController.popBackStack()
                        }
                    ) {
                        Text("Yes", color = Color.Red)
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { showExitDialog = false }
                    ) {
                        Text("No", color = Color.Green)
                    }
                }
            )
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState),
        ) {
            Card (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .border(2.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(14.dp)),
                colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface),
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
                                progress = { exerciseProgress.count { it.isDone }.toFloat() / totalExercises.toFloat() },
                                modifier = Modifier.fillMaxWidth(),
                                color = MaterialTheme.colorScheme.onBackground,
                                trackColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                "Exercise ${exerciseProgress.count { it.isDone } + 1} of $totalExercises",
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                    Text(
                        currentExercise?.name ?: "No exercise",
                        color = MaterialTheme.colorScheme.onSurface,
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
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            val weight = currentExercise?.weight?.let { viewModel.convertUnit(it, viewModel.unit) } ?: 0f
                            Text(
                                "@ $weight ${viewModel.unit}",
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
            repeat (currentExercise?.sets ?: 0) { setIndex ->
                val (entryWeight, entryReps, isDone) = setData[setIndex]
                val isSetActive = (setIndex == 0 || setData[setIndex - 1].isDone) && timerRunningForSet == -1 && !isDone
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .alpha(if (isSetActive) 1f else 0.4f)
                        .border(2.dp, if (isDone) Color.Green else MaterialTheme.colorScheme.outline, RoundedCornerShape(14.dp)),
                    colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface),
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
                            color = MaterialTheme.colorScheme.onSurface,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        OutlinedTextField(
                            value = entryWeight,
                            readOnly = !isSetActive,
                            onValueChange = { setData[setIndex] = setData[setIndex].copy(entryWeight = it) },
                            label = { Text("Weight (kg)", color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodySmall) },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            shape = RoundedCornerShape(14.dp),
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                                focusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                focusedLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                                unfocusedLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                                cursorColor = MaterialTheme.colorScheme.onSurface
                            ),
                            modifier = Modifier
                                .width(90.dp)
                        )
                        OutlinedTextField(
                            value = entryReps,
                            readOnly = !isSetActive,
                            onValueChange = { setData[setIndex] = setData[setIndex].copy(entryReps = it) },
                            label = { Text("Reps", color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodySmall) },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            shape = RoundedCornerShape(14.dp),
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                                focusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                focusedLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                                unfocusedLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                                cursorColor = MaterialTheme.colorScheme.onSurface
                            ),
                            modifier = Modifier
                                .width(90.dp)
                        )
                        TextButton(
                            enabled = entryWeight.isNotEmpty() && entryReps.isNotEmpty() && !isDone && (setIndex == 0 || setData[setIndex - 1].isDone ) && timerRunningForSet == -1,
                            onClick = {
                                restTimeSeconds = 90
                                timerRunningForSet = setIndex
                                setData[setIndex] = setData[setIndex].copy(isDone = true)

                                if (setData.all {it.isDone}) {
                                    exerciseProgress[currentExerciseIndex] = exerciseProgress[currentExerciseIndex].copy(isDone = true)
                                }

                                focusManager.clearFocus()
                                scope.launch {
                                    val userId = Appwrite.account.get().id
                                    if(workoutId.isEmpty()) {
                                        workoutId = viewModel.createWorkout() ?: ""
                                    }
                                    viewModel.saveSet(
                                        workoutId = workoutId,
                                        userId = userId,
                                        exerciseName = currentExercise?.name ?: "Unknown Exercise",
                                        weight = entryWeight.toFloatOrNull() ?: 0f,
                                        reps = entryReps.toIntOrNull() ?: 0
                                    )
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                contentColor = MaterialTheme.colorScheme.onSurface,
                                containerColor = Color.Transparent,
                                disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
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
            val unfinishedCount = unfinishedIndices.size
            OutlinedButton(
                onClick = {
                    if(unfinishedCount > 1) {
                        exerciseProgress[currentExerciseIndex] = exerciseProgress[currentExerciseIndex].copy(isSkipped = true)
                        val nextUnfinished: Int? = unfinishedIndices.firstOrNull { it > currentExerciseIndex} ?: unfinishedIndices.firstOrNull()
                        if (nextUnfinished != null) {
                            currentExerciseIndex = nextUnfinished
                        }
                        timerRunningForSet = -1
                        restTimeSeconds = viewModel.restTime
                    }
                 },
                colors = ButtonDefaults.outlinedButtonColors(containerColor = MaterialTheme.colorScheme.background, contentColor = MaterialTheme.colorScheme.onSurfaceVariant),
                enabled = unfinishedCount > 1
            ) {
                Text("Skip exercise", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.width(4.dp))
                Icon(Icons.Default.Book, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            ElevatedButton(
                onClick = {
                    if (allExercisesDone) {
                        onFinish()
                    } else {
                        val nextUnfinished: Int?  = unfinishedIndices.firstOrNull { it > currentExerciseIndex } ?: unfinishedIndices.firstOrNull()
                        if (nextUnfinished != null) {
                            currentExerciseIndex = nextUnfinished
                        }
                    }
                    timerRunningForSet = -1
                    restTimeSeconds = viewModel.restTime
                },
                enabled = allExercisesDone || (setData.all { it.isDone } && timerRunningForSet == -1),
                colors = ButtonDefaults.elevatedButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    disabledContainerColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                    )
            ) {
                Text(if (exerciseProgress.all { it.isDone }) "Next Exercise" else "Next Exercise")
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
                .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(16.dp))
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
                    colors =  ButtonDefaults.elevatedButtonColors(containerColor = Color.Transparent, contentColor = MaterialTheme.colorScheme.onSurface )
                ) {
                    Icon(Icons.Default.Close, contentDescription = null)
                }
                Box(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(180.dp))
                        .padding(horizontal = 24.dp, vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        String.format("%02d:%02d", minutes, seconds),
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
                ElevatedButton(
                    onClick = { onAdd30Seconds() },
                    colors = ButtonDefaults.elevatedButtonColors(containerColor = Color.Transparent, contentColor = MaterialTheme.colorScheme.onSurface )
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                }
            }
        }
    }
}
//I hope that people who created jetpack compose get hit by a bus (On accident ofc)
