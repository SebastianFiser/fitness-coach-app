package com.sebastianfiser.fitnesscoach.screens

import com.sebastianfiser.fitnesscoach.models.AppViewModel
import com.sebastianfiser.fitnesscoach.models.exerciseList
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.sebastianfiser.fitnesscoach.navigation.Screen
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.material.icons.filled.Add

@Composable
fun workoutSelectScreen(onExerciseSelected: (String) -> Unit, navController: NavController) {
    var query by remember { mutableStateOf("") }
    val filtedExercises = remember(query) {
        if (query.isEmpty()) {
            exerciseList
        } else {
            exerciseList.filter { it.contains(query, ignoreCase = true) }
        }
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets(0.dp)
    ) {
        LazyColumn{
            stickyHeader {
                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    label = { Text("Search exercises") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Black)
                        .padding(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.Gray,
                        focusedLabelColor = Color.White,
                        unfocusedLabelColor = Color.Gray
                    )
                )
            }

            item {
                HorizontalDivider(thickness = 2.dp)
                Card (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    colors = CardDefaults.cardColors(Color.DarkGray)
                ) {
                    Text(
                        "Select an exercise to add to your schedule",
                        color = Color.White,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(16.dp)
                    )

                    filtedExercises.forEach { exercise ->
                        filterItem(exercise, onSelected = {
                            onExerciseSelected(exercise)
                            navController.popBackStack()
                        })
                    }
                }
            }

        }
    }
}

@Composable
fun filterItem(exercise: String, onSelected: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelected() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = exercise, color = Color.White)
        Icon(Icons.Default.Add, contentDescription = "Add", tint = Color.White)
    }
}