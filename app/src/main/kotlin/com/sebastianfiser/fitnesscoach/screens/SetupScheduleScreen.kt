package com.sebastianfiser.fitnesscoach.screens

import androidx.compose.foundation.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.sebastianfiser.fitnesscoach.models.AppViewModel
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import com.sebastianfiser.fitnesscoach.navigation.Screen

@Composable
fun SetupSchedule(navController: NavController, viewModel: AppViewModel) {
    val days = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")
    val dayMap = mapOf(
        "Mo" to "Monday",
        "Tu" to "Tuesday",
        "We" to "Wednesday",
        "Th" to "Thursday",
        "Fr" to "Friday",
        "Sa" to "Saturday",
        "Su" to "Sunday"
    )
    BackHandler(enabled = true) {

    }
    Scaffold (
        bottomBar = {
            Card(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, Color.LightGray),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF333333)),
                onClick = { /*Implement saving */ }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Save Schedule", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Icon(Icons.Default.Check, contentDescription = "Save", tint = Color.White)
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            items(days) { day ->
                Card(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, Color.LightGray),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF333333)),
                    onClick = { 
                        viewModel.selectedDay = day
                        navController.navigate(Screen.ExercisePick.route)
                    }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(day, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Icon(Icons.Default.Add, contentDescription = "Add exercise", tint = Color.White)
                    }
                    HorizontalDivider(color = Color.Gray, thickness = 0.5.dp)
                }
                val exercisesForDay = viewModel.scheduleSetup[dayMap[day]] ?: emptyList()
                exercisesForDay.forEach { exercise ->
                    Text("- ${exercise.name}", fontSize = 16.sp, color = Color.LightGray, modifier = Modifier.padding(start = 32.dp, top = 4.dp))
                    HorizontalDivider(color = Color.Gray, thickness = 0.5.dp, modifier = Modifier.padding(horizontal = 16.dp))
                }
            }
            item {
                Text("count: ${viewModel.scheduleSetup.size}", color = Color.White, modifier = Modifier.padding(16.dp))
            }
            item{
                Text("${viewModel.scheduleSetup}", color = Color.White, modifier = Modifier.padding(16.dp))
            }
        }
    }
}
