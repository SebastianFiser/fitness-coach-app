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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import com.sebastianfiser.fitnesscoach.models.Appwrite
import androidx.compose.runtime.LaunchedEffect
import com.sebastianfiser.fitnesscoach.models.Exercise

@Composable
fun SetupSchedule(navController: NavController, viewModel: AppViewModel) {
    val days = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")
    val dayMap = mapOf(
        "Monday" to "Mo",
        "Tuesday" to "Tu",
        "Wednesday" to "We",
        "Thursday" to "Th",
        "Friday" to "Fr",
        "Saturday" to "Sa",
        "Sunday" to "Su"
    )
    val scope = rememberCoroutineScope()
    BackHandler(enabled = true) {

    }
    LaunchedEffect(Unit) {
        if (!viewModel.isEditing) {
            viewModel.scheduleSetup.clear()
        }
    }
    LaunchedEffect(viewModel.isEditing) {
        if(viewModel.isEditing) {
            val currentUser = Appwrite.getCurrentUser()
            val userId = currentUser?.id ?: return@LaunchedEffect
            viewModel.loadSchedule(userId)
            viewModel.scheduleSetup.clear()

            viewModel.schedule.groupBy { it.data["day"] as String }.forEach { (day, docs) -> 
                val exercises = docs.map { doc -> 
                    Exercise(
                        name = doc.data["exerciseName"] as? String ?: "",
                        sets = (doc.data["sets"] as? Long)?.toInt() ?: 0,
                        reps = (doc.data["reps"] as? Long)?.toInt() ?: 0,
                        weight = when (val w = doc.data["weight"]) {
                            is Double -> w.toFloat()
                            is Float -> w
                            is Long -> w.toFloat()
                            else -> 0f
                        }
                    )
                }
                viewModel.scheduleSetup[day] = exercises.toMutableList()
            }
        }
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
                onClick = { 
                    scope.launch {
                        val currentUser = Appwrite.getCurrentUser()
                        val userId = currentUser?.id ?: return@launch
                        if (viewModel.isEditing) {
                            viewModel.deleteAllSchedule(userId)
                        }
                        viewModel.saveSetupSchedule(userId)
                        viewModel.loadSchedule(userId)
                        val destination = if (viewModel.isEditing) Screen.Schedule.route else Screen.Overview.route
                        viewModel.isEditing = false
                        navController.navigate(destination)
                    }
                }
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
                ) {
                    Row(
                        modifier = Modifier
                            .clickable { 
                                viewModel.selectedDay = day
                                navController.navigate(Screen.ExercisePick.route)
                            }
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(day, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Icon(Icons.Default.Add, contentDescription = "Add exercise", tint = Color.White)
                    }
                    HorizontalDivider(color = Color.Gray, thickness = 0.5.dp)
                    val exercisesForDay = viewModel.scheduleSetup[dayMap[day]] ?: emptyList()
                    exercisesForDay.forEach { exercise ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                         ) {
                            Text("- ${exercise.name}", fontSize = 16.sp, color = Color.LightGray, modifier = Modifier.padding(start = 32.dp, top = 4.dp))
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = "Remove",
                                tint = Color.LightGray,
                                modifier = Modifier.clickable {
                                    val key = dayMap[day] ?: ""
                                    if(key.isEmpty()) return@clickable
                                    val current = viewModel.scheduleSetup[key] ?: return@clickable
                                    viewModel.scheduleSetup[key] = current.filter { it.name != exercise.name }.toMutableList()
                                }
                            )
                        }
                        HorizontalDivider(color = Color.Gray, thickness = 0.5.dp, modifier = Modifier.padding(horizontal = 32.dp))
                    }                
                }
            }
        }
    }
}
