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
    Scaffold (

    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            item {
                Text("Submission Screen", color = Color.White)
            }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .height(48.dp)
                                .weight(1f)
                                .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(8.dp),
                                verticalArrangement = Arrangement.Center,
                            ) {
                                Text("Select your lift")
                                Box(
                                    modifier = Modifier
                                        .height(32.dp)
                                        .fillMaxWidth()
                                        .border(1.dp, Color.Gray, RoundedCornerShape(8.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    ShowFilterDropdownForLift(
                                        selectedLift = selectedLift,
                                        onLiftSelected = { lift ->
                                            selectedLift = lift
                                        }
                                    )
                                    if(selectedLift == null) {
                                        Text("Select Lift")
                                    } else {
                                        Text("${selectedLift}")
                                    }
                                }
                            }
                        }
                        Box(
                            modifier = Modifier
                                .height(48.dp)
                                .weight(1f)
                                .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(8.dp),
                                verticalArrangement = Arrangement.Center,
                            ) {
                                Text("Insert pr weight")
                                OutlinedTextField(
                                    value = prWeight,
                                    onValueChange = { prWeight = it },
                                    label = { Text("weight", color = Color.LightGray, style = MaterialTheme.typography.bodySmall) },
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
                            }
                        }
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .height(48.dp)
                                .weight(1f)
                                .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clickable{ videoPicker.launch("video/*") }
                                    .padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                if(videoUploaded) {
                                    Text("File: ${selectedVideoUri?.lastPathSegment ?: "unknown"}", color = Color.White)
                                } else {
                                    Text("click to select file", color = Color.White)
                                }
                            }
                        }
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .height(48.dp)
                                .weight(1f)
                                .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Country select placeholder")
                        }
                        Box(
                            modifier = Modifier
                                .height(48.dp)
                                .weight(1f)
                                .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(8.dp),
                                verticalArrangement = Arrangement.Center,
                            ) {
                                Text("Input your bodyweight")
                                OutlinedTextField(
                                    value = bodyweight,
                                    onValueChange = { bodyweight = it },
                                    label = { Text("weight", color = Color.LightGray, style = MaterialTheme.typography.bodySmall) },
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
                            }
                        }
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .height(48.dp)
                                .weight(1f)
                                .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(8.dp),
                                verticalArrangement = Arrangement.Center,
                            ) {
                                Text("Select age group")
                                Box(
                                    modifier = Modifier
                                        .height(32.dp)
                                        .fillMaxWidth()
                                        .border(1.dp, Color.Gray, RoundedCornerShape(8.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    ShowFilterDropdownForAgeGroup(
                                        selectedAgeGroup = selectedAgeGroup,
                                        onAgeGroupSelected = { ageGroup ->
                                            selectedAgeGroup = ageGroup
                                        }
                                    )
                                    if(selectedAgeGroup == null) {
                                        Text("Select age Group ")
                                    } else {
                                        Text("${selectedAgeGroup}")
                                    }
                                }
                            }
                        }
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                                .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(8.dp),
                                verticalArrangement = Arrangement.Center
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(vertical = 8.dp)
                                        .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    listOf("Natty", "Juiced").forEach { option ->
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clickable { natty = option == "Natty" }
                                                .padding(8.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            RadioButton(
                                                selected = natty == (option == "Natty"),
                                                onClick = { natty = option == "Natty" },
                                                colors = RadioButtonDefaults.colors(selectedColor = Color.White, unselectedColor = Color.Gray)
                                            )
                                            Text(option, color = Color.White)
                                        }
                                    }
                                }
                            }
                        }
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(onClick = { /* Handle submission */ }) {
                            Text("Submit")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ShowFilterDropdownForLift(selectedLift: String?, onLiftSelected: (String) -> Unit) {
    var liftOpen by remember { mutableStateOf(false) }
    Box{
        Button(
            onClick = { liftOpen = !liftOpen },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black, contentColor = Color.White)
        ) {
            Text("Lift")
        }
        DropdownMenu(
            expanded = liftOpen,
            onDismissRequest = { liftOpen = false },
            modifier = Modifier
                .background(Color(0xFF1C1C1E))
                .border(1.dp, Color.DarkGray, RoundedCornerShape(8.dp))
        ) { 
            DropdownMenuItem(
                text = { Text("Squat") },
                onClick = { 
                    liftOpen = false
                    onLiftSelected("Squat") },
                colors = MenuDefaults.itemColors(textColor = Color.White)
            )
            DropdownMenuItem(
                text = { Text("Bench Press") },
                onClick = { 
                    liftOpen = false
                    onLiftSelected("Bench Press") },
                colors = MenuDefaults.itemColors(textColor = Color.White)
            )
            DropdownMenuItem(
                text = { Text("Deadlift") },
                onClick = { 
                    liftOpen = false
                    onLiftSelected("Deadlift") },
                colors = MenuDefaults.itemColors(textColor = Color.White)
            )
        }
    }
}

@Composable
fun ShowFilterDropdownForAgeGroup(selectedAgeGroup: String?, onAgeGroupSelected: (String) -> Unit) {
    var ageGroupOpen by remember { mutableStateOf(false) }
    Box{
        Button(
            onClick = { ageGroupOpen = !ageGroupOpen },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black, contentColor = Color.White)
        ) {
            Text("Age Group")
        }
        DropdownMenu(
            expanded = ageGroupOpen,
            onDismissRequest = { ageGroupOpen = false },
            modifier = Modifier
                .background(Color(0xFF1C1C1E))
                .border(1.dp, Color.DarkGray, RoundedCornerShape(8.dp))
        ) { 
            DropdownMenuItem(
                text = { Text("Under 18") },
                onClick = { 
                    ageGroupOpen = false
                    onAgeGroupSelected("-18") },
                colors = MenuDefaults.itemColors(textColor = Color.White)
            )
            DropdownMenuItem(
                text = { Text("19-25") },
                onClick = { 
                    ageGroupOpen = false
                    onAgeGroupSelected("19-25") },
                colors = MenuDefaults.itemColors(textColor = Color.White)
            )
            DropdownMenuItem(
                text = { Text("26-35") },
                onClick = { 
                    ageGroupOpen = false
                    onAgeGroupSelected("26-35") },
                colors = MenuDefaults.itemColors(textColor = Color.White)
            )
            DropdownMenuItem(
                text = { Text("36-45") },
                onClick = { 
                    ageGroupOpen = false
                    onAgeGroupSelected("36-45") },
                colors = MenuDefaults.itemColors(textColor = Color.White)
            )
            DropdownMenuItem(
                text = { Text("46+") },
                onClick = { 
                    ageGroupOpen = false
                    onAgeGroupSelected("46+") },
                colors = MenuDefaults.itemColors(textColor = Color.White)
            )
        }
     }
}
