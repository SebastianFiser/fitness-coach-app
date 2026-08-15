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
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import com.sebastianfiser.fitnesscoach.models.ScheduleEntity
import com.sebastianfiser.fitnesscoach.models.SyncState
import com.sebastianfiser.fitnesscoach.models.toEntity

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
    var isSaving by remember { mutableStateOf(false) }
    BackHandler(enabled = true) {

    }
    LaunchedEffect(Unit) {
        if (viewModel.isEditing && !viewModel.scheduleSetupLoaded) {
            viewModel.scheduleSetupLoaded = true
            val currentUser = Appwrite.getCurrentUser()
            val userId = currentUser?.id ?: return@LaunchedEffect
            viewModel.loadSchedule(userId)
            viewModel.scheduleSetup.clear()
            viewModel.schedule.map { it.toEntity() }.groupBy { it.day }.forEach { (day, entities) ->
                val exercises = entities.map { entity ->
                    Exercise(
                        name = entity.exerciseName,
                        sets = entity.sets,
                        reps = entity.reps,
                        weight = entity.weight
                    )
                }
                viewModel.scheduleSetup[day] = exercises.toMutableList()
            }
        } else if (!viewModel.isEditing && !viewModel.scheduleSetupLoaded) {
            viewModel.scheduleSetupLoaded = true
            viewModel.scheduleSetup.clear()

        }
    }
    Scaffold (
        bottomBar = {
            Card(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                colors = CardDefaults.cardColors(if (isSaving) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.surface.copy(alpha = 0.8f)),
                enabled = if(isSaving) false else true,
                onClick = {
                    scope.launch {
                        if (isSaving) return@launch
                        isSaving = true
                        try {
                            val currentUser = Appwrite.getCurrentUser()
                            val userId = currentUser?.id ?: return@launch

                            viewModel.saveSetupSchedule(userId)

                            val destination = if (viewModel.isEditing) Screen.Schedule.route else Screen.Overview.route
                            viewModel.isEditing = false
                            viewModel.scheduleSetupLoaded = false

                            navController.navigate(destination) {
                                popUpTo(Screen.SetupSchedule.route) {
                                    inclusive = true
                                }
                            }
                        } finally {
                            isSaving = false
                        }
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
                    Text("Save Schedule", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                    if(isSaving) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onSurface, strokeWidth = 2.dp)
                    } else {
                        Icon(Icons.Default.Check, contentDescription = "Save", tint = MaterialTheme.colorScheme.onSurface)
                    }
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
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
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
                        Text(day, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                        Icon(Icons.Default.Add, contentDescription = "Add exercise", tint = MaterialTheme.colorScheme.onSurface)
                    }
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline, thickness = 0.5.dp)
                    val exercisesForDay = viewModel.scheduleSetup[dayMap[day]] ?: emptyList()
                    exercisesForDay.forEach { exercise ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                         ) {
                            Text("- ${exercise.name}", fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(start = 32.dp, top = 4.dp))
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = "Remove",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.clickable {
                                    val key = dayMap[day] ?: ""
                                    if(key.isEmpty()) return@clickable
                                    val current = viewModel.scheduleSetup[key] ?: return@clickable
                                    viewModel.scheduleSetup[key] = current.filter { it.name != exercise.name }.toMutableList()
                                }
                            )
                        }
                        HorizontalDivider(color = MaterialTheme.colorScheme.outline, thickness = 0.5.dp, modifier = Modifier.padding(horizontal = 32.dp))
                    }
                }
            }
        }
    }
}
