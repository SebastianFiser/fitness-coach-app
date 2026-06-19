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
import org.kimplify.countries.Countries
import org.kimplify.countries.model.Country
import org.kimplify.countries.extensions.getDisplayName

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
    val countries = remember { Countries.repository.getAll() }
    var countryQuery by remember { mutableStateOf("") }
    var selectedCountry by remember { mutableStateOf<Country?>(null) }
    var countryOpen by remember { mutableStateOf(false) }


    Scaffold (

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
                        .border(2.dp, Color.White, RoundedCornerShape(14.dp))
                        .padding(24.dp),
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
                            OutlinedTextField(
                                modifier = Modifier.menuAnchor(),
                                shape = RoundedCornerShape(8.dp),
                                value = selectedLift ?: "",
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Lift", color = Color.White) },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = liftOpen) },
                                colors = TextFieldDefaults.textFieldColors(
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White,  
                                    disabledTextColor = Color.White,
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
                        .border(2.dp, Color.White, RoundedCornerShape(14.dp))
                        .padding(24.dp),
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
                        OutlinedTextField(
                            value = prWeight,
                            shape = RoundedCornerShape(8.dp),
                            onValueChange = { prWeight = it },
                            label = { Text("PR Weight", color = Color.White) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            colors = TextFieldDefaults.textFieldColors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                disabledTextColor = Color.White,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                disabledIndicatorColor = Color.Transparent
                            )
                        )
                    }
                }
            }

            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(2.dp, Color.White, RoundedCornerShape(14.dp))
                        .padding(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1C1C1E)),
                    shape = RoundedCornerShape(28.dp)
                ) {
                    //Country select- dropdown with search field.
                    ExposedDropdownMenuBox(
                        expanded = countryOpen,
                        onExpandedChange = { countryOpen = it }
                    ) {
                        OutlinedTextField(
                            modifier = Modifier.menuAnchor(),
                            shape = RoundedCornerShape(8.dp),
                            value = countryQuery,
                            onValueChange = { countryQuery = it; countryOpen = true },
                            readOnly = false,
                            label = { Text("Country", color = Color.White) },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = countryOpen) },
                            colors = TextFieldDefaults.textFieldColors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                disabledTextColor = Color.White,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                disabledIndicatorColor = Color.Transparent
    
                            )
                        )
                        ExposedDropdownMenu(
                            expanded = countryOpen,
                            onDismissRequest = { countryOpen = false }
                        ) {
                            countries.filter { it.getDisplayName().contains(countryQuery, ignoreCase = true) }.forEach { country ->
                                DropdownMenuItem(
                                    text = { Text(country.getDisplayName()) },
                                    onClick = {
                                        selectedCountry = country
                                        countryQuery = country.getDisplayName()
                                        countryOpen = false
                                    }
                                )
                            }
                        }
                    }
                }
            }

            item {
                Card (
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(2.dp, Color.White, RoundedCornerShape(14.dp))
                        .padding(24.dp),
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
                            "Upload Video",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White
                        )
                        HorizontalDivider(color = Color.Gray, thickness = 1.dp)
                        Button(
                            onClick = { videoPicker.launch("video/*") },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                        ) {
                            Text("Select Video", color = Color.White)
                        }
                        selectedVideoUri?.let { uri ->
                            Text("Selected Video: ${uri.lastPathSegment}", color = Color.White)
                        }
                    }
                }
            }
            
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(2.dp, Color.White, RoundedCornerShape(14.dp))
                        .padding(24.dp),
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
                            "Select Age Group",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White
                        )
                        HorizontalDivider(color = Color.Gray, thickness = 1.dp)
                        ExposedDropdownMenuBox(
                            expanded = ageGroupOpen,
                            onExpandedChange = { ageGroupOpen = it }
                        ) {
                            OutlinedTextField(
                                modifier = Modifier.menuAnchor(),
                                shape = RoundedCornerShape(8.dp),
                                value = selectedAgeGroup ?: "",
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Age Group", color = Color.White) },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = ageGroupOpen) },
                                colors = TextFieldDefaults.textFieldColors(
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White,
                                    disabledTextColor = Color.White,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                    disabledIndicatorColor = Color.Transparent
        
                                )
                            )
                            ExposedDropdownMenu(
                                expanded = ageGroupOpen,
                                onDismissRequest = { ageGroupOpen = false }
                            ) {
                                listOf("Under 18", "18-25", "26-35", "36-45", "46+").forEach { group ->
                                    DropdownMenuItem(
                                        text = { Text(group) },
                                        onClick = {
                                            selectedAgeGroup = group
                                            ageGroupOpen = false
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
                        .border(2.dp, Color.White, RoundedCornerShape(14.dp))
                        .padding(24.dp),
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
                            "Enter Bodyweight",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White
                        )
                        HorizontalDivider(color = Color.Gray, thickness = 1.dp)
                        OutlinedTextField(
                            value = bodyweight,
                            shape = RoundedCornerShape(8.dp),
                            onValueChange = { bodyweight = it },
                            label = { Text("Bodyweight", color = Color.White) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            colors = TextFieldDefaults.textFieldColors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                disabledTextColor = Color.White,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                disabledIndicatorColor = Color.Transparent
                            )
                        )
                    }
                }
            }

            item {
                Card (
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(2.dp, Color.White, RoundedCornerShape(14.dp))
                        .padding(24.dp),
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
                            "Are you natural?",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White
                        )
                        HorizontalDivider(color = Color.Gray, thickness = 1.dp)
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Button(
                                onClick = { natty = true },
                                colors = ButtonDefaults.buttonColors(containerColor = if (natty == true) Color.Green else Color.Gray)
                            ) {
                                Text("Yes", color = Color.White)
                            }
                            Button(
                                onClick = { natty = false },
                                colors = ButtonDefaults.buttonColors(containerColor = if (natty == false) Color.Red else Color.Gray)
                            ) {
                                Text("No", color = Color.White)
                            }
                        }
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = {
                            // Handle submission logic here
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
                    ) {
                        Text("Submit", color = Color.White)
                    }
                }
            }
        }  
    }
}