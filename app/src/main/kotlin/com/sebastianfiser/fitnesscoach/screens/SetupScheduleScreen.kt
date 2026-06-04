package com.seabastianfiser.fitnesscoach.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.sebastianfiser.fitnesscoach.models.AppViewModel
import com.sebastianfiser.fitnesscoach.navigation.Screen
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete

@Composable
fun SetupScheduleScreen(navController: NavController, viewModel: AppViewModel) {
    val days = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets(0.dp)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(top = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(bottom = 90.dp),
        ) {
            items(days) { day ->
                dayCard(day, navController, viewModel)
            }
        }
    }
}

@Composable
fun dayCard(day: String, navController: NavController, viewModel: AppViewModel) {
    var addingExercise by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(Color.DarkGray)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                day,
                color = Color.White,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(16.dp)
            )

            Icon(Icons.Default.Add, contentDescription = "Add exercise", tint = Color.White
                , modifier = Modifier
                    .clickable { 
                        navController.navigate(Screen.ExercisePick.route) 
                        addingExercise = true 
                })
        }
        Divider(color = Color.Gray, thickness = 1.dp)
        if (addingExercise) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "${viewModel.selectedExercise ?: "Selected Exercise"}",
                    color = Color.White,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(16.dp)
                )
                Icon(Icons.Default.Delete, contentDescription = "Remove exercise", tint = Color.White, modifier = Modifier.clickable { addingExercise = false })
            }
        }
    }
}
