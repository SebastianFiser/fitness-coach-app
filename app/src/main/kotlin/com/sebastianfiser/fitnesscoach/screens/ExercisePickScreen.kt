package com.sebastianfiser.fitnesscoach.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.sebastianfiser.fitnesscoach.models.AppViewModel
import com.sebastianfiser.fitnesscoach.models.Exercise
import com.sebastianfiser.fitnesscoach.models.exerciseList

@Composable
fun ExercisePickScreen(navController: NavController, viewModel: AppViewModel) {
    var query by remember { mutableStateOf("") }
    val filteredExercises = remember(query) {
        if (query.isEmpty()) {
            exerciseList
        } else {
            exerciseList.filter { it.contains(query, ignoreCase = true) }
        }
    }
    var selectedExerciseDay = viewModel.selectedDay
    val dayMap = mapOf(
        "Monday" to "Mo",
        "Tuesday" to "Tu",
        "Wednesday" to "We",
        "Thursday" to "Th",
        "Friday" to "Fr",
        "Saturday" to "Sa",
        "Sunday" to "Su"
    )
    var sets by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    var clickedExercise by remember { mutableStateOf("") }
    Scaffold(
        topBar = {
            stickyHeader(
                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    placeholder = { Text("Search exercises...") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = Color.White,
                        placeholderColor = Color.LightGray,
                        focusedBorderColor = Color.LightGray,
                        unfocusedBorderColor = Color.LightGray
                    )
                )
            )
        }
    ) { paddingValues -> 
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            items(filteredExercises) { exercise ->
                Card(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, Color.LightGray),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF333333)),
                    onClick = { 
                        showDialog = true 
                        clickedExercise = exercise
                        }
                ) {
                    Text(exercise, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White, modifier = Modifier.padding(16.dp))
                }
            }
        }
    }
    if(showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("How many sets?") },
            text = {
                OutlinedTextField(
                    value = sets,
                    onValueChange = { sets = it },
                    placeholder = { Text("Enter number of sets") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = outlinedTextFieldDefaults.colors(
                        focusedtextColor = Color.White,
                        unfocusedTextColor = Color.LightGray,
                        focusedBorderColor = Color.LightGray,
                        unfocusedBorderColor = Color.LightGray
                    )
                )
            },
            confirmButton = {
                Button(onClick = {
                    showDialog = false
                    val exercise = Exercise(name = clickedExercise, sets = sets.toIntOrNull() ?: 0, reps = 10, weight = 0f)
                    viewModel.scheduleSetup.getOrPut(dayMap[selectedExerciseDay] ?: "Mo") { mutableListOf() }.add(exercise)
                    viewModel.selectedDay = ""
                    navController.popBackStack()
                }) {
                    Text("Add")
                }
            },
            dismissButton = {
                Button(onClick = { showDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}