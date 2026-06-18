package com.sebastianfiser.fitnesscoach.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.sebastianfiser.fitnesscoach.models.AppViewModel
import androidx.compose.runtime.*
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.background
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.material3.ExperimentalMaterial3Api

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubmissionScreen(navController: NavController, viewModel: AppViewModel) {
    var selectedLift by remember { mutableStateOf<String?>(null) }
    var prWeight by remember { mutableStateOf("") }
    var selectedVideoUri by remember { mutableStateOf<Uri?>(null) }
    var videoUploaded by remember { mutableStateOf(false) }
    var videoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        selectedVideoUri = uri
    }
    var bodyweight by remember { mutableStateOf("") }
    var selectedAgeGroup by remember { mutableStateOf<String?>(null) }
    var natty by remember { mutableStateOf<Boolean?>(null) }
    var liftOpen by remember { mutableStateOf(false) }
    var ageGroupOpen by remember { mutableStateOf(false) }
    Scaffold (
        containerColor = color.Black,
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .padding(bottom = 80.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Text("Submit Your PR", style = MaterialTheme.typography.headlineMedium, color = Color.White)
            }
            
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1C1C1E)),
                    shape = RoundedCornerShape(28.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)    
                    ) {
                        Text(
                            "Select Lift",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White
                        )
                        HorizontalDivider(color = Color.Gray, thickness = 1.dp)
                        ExposedDropdownMenuBox(
                            expanded = liftOpen,
                            onExpandedChange = { liftOpen = it }
                        ) {
                            TextField(
                                value = selectedLift ?: "",
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Lift", color = Color.White) },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = liftOpen) },
                                colors = TextFieldDefaults.textFieldColors(
                                    textColor = Color.White,
                                    disabledTextColor = Color.White,
                                    backgroundColor = Color.Transparent,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                    disabledIndicatorColor = Color.Transparent
                                )
                            )
                            ExposedDropdownMenu(
                                expanded = liftOpen,
                                onDismissRequest = { liftOpen = false }
                            ) {
                                listOf("Squat", "Bench Press", "Deadlift").forEach { lift ->
                                    DropdownMenuItem(
                                        text = { Text(lift) },
                                        onClick = {
                                            selectedLift = lift
                                            liftOpen = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }

            item {
                Card (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1C1C1E)),
                    shape = RoundedCornerShape(28.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)    
                    ) {
                        Text(
                            "Enter PR Weight",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White
                        )
                        HorizontalDivider(color = Color.Gray, thickness = 1.dp)
                        TextField(
                            value = prWeight,
                            onValueChange = { prWeight = it },
                            label = { Text("PR Weight", color = Color.White) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            colors = TextFieldDefaults.textFieldColors(
                                textColor = Color.White,
                                disabledTextColor = Color.White,
                                backgroundColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                disabledIndicatorColor = Color.Transparent
                            )
                        )
                    }
                }
            }

        }  
    }
}