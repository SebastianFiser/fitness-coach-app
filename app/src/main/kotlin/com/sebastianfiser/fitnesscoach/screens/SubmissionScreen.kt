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
import androidx.compose.ui.platform.LocalContext
import org.kimplify.countries.Countries
import org.kimplify.countries.model.Country
import org.kimplify.countries.extensions.getDisplayName
import com.sebastianfiser.fitnesscoach.models.Appwrite
import androidx.compose.runtime.rememberCoroutineScope
import android.content.Context
import io.appwrite.ID
import kotlinx.coroutines.launch
import androidx.compose.runtime.LaunchedEffect
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.common.MediaItem
import androidx.media3.ui.PlayerView
import androidx.compose.ui.viewinterop.AndroidView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubmissionScreen(navController: NavController, viewModel: AppViewModel) {
    var selectedLift by remember { mutableStateOf<String?>(null) }
    var prWeight by remember { mutableStateOf("") }
    var selectedVideoUri by remember { mutableStateOf<Uri?>(null) }
    var videoUploaded by remember { mutableStateOf(false) }
    val context = LocalContext.current
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
    var genderOpen by remember { mutableStateOf(false) } 
    var selectedGender by remember { mutableStateOf("") } 
    var isSubmitting by remember { mutableStateOf(false) }
    var submitStatus by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()
    val exoPlayer = remember(selectedVideoUri) {
        selectedVideoUri?.let { uri ->
            ExoPlayer.Builder(context).build().apply {
                setMediaItem(MediaItem.fromUri(uri))
                prepare()
            }
        }
    }

    DisposableEffect(exoPlayer) {
        onDispose {
            exoPlayer?.release()
        }
    }

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
                Text("Submit Your PR", style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.onBackground)
            }
            
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(2.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(14.dp))
                        .padding(24.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
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
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        HorizontalDivider(color = MaterialTheme.colorScheme.outline, thickness = 1.dp)
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
                                label = { Text("Lift", color = MaterialTheme.colorScheme.onSurface) },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = liftOpen) },
                                colors = TextFieldDefaults.textFieldColors(
                                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                                    disabledTextColor = MaterialTheme.colorScheme.onSurface,
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
                        .border(2.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(14.dp))
                        .padding(24.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
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
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        HorizontalDivider(color = MaterialTheme.colorScheme.outline, thickness = 1.dp)
                        OutlinedTextField(
                            value = prWeight,
                            shape = RoundedCornerShape(8.dp),
                            onValueChange = { prWeight = it },
                            label = { Text("PR Weight", color = MaterialTheme.colorScheme.onSurface) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            colors = TextFieldDefaults.textFieldColors(
                                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                                disabledTextColor = MaterialTheme.colorScheme.onSurface,
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
                        .border(2.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(14.dp))
                        .padding(24.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(28.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)    
                    ) {
                        Text(
                            "Your gender",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        HorizontalDivider(color = MaterialTheme.colorScheme.outline, thickness = 1.dp)
                        ExposedDropdownMenuBox(
                            expanded = genderOpen,
                            onExpandedChange = { genderOpen = it }
                        ) {
                            OutlinedTextField(
                                modifier = Modifier.menuAnchor(),
                                shape = RoundedCornerShape(8.dp),
                                value = selectedGender,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Gender", color = MaterialTheme.colorScheme.onSurface) },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = genderOpen) },
                                colors = TextFieldDefaults.textFieldColors(
                                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                                    disabledTextColor = MaterialTheme.colorScheme.onSurface,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                    disabledIndicatorColor = Color.Transparent
                                )
                            )
                            ExposedDropdownMenu(
                                expanded = genderOpen,
                                onDismissRequest = { genderOpen = false }
                            ) {
                                listOf("Male", "Female", "Other").forEach { gender ->
                                    DropdownMenuItem(
                                        text = { Text(gender) },
                                        onClick = {
                                            selectedGender = gender
                                            genderOpen = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }

            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(2.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(14.dp))
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(28.dp)
                ) {
                    Text(
                        "Select your country ",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline, thickness = 1.dp)
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
                            label = { Text("Country", color = MaterialTheme.colorScheme.onSurface) },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = countryOpen) },
                            colors = TextFieldDefaults.textFieldColors(
                                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                                disabledTextColor = MaterialTheme.colorScheme.onSurface,
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
                        .border(2.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(14.dp))
                        .padding(24.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
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
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        HorizontalDivider(color = MaterialTheme.colorScheme.outline, thickness = 1.dp)
                        if (exoPlayer != null) {
                            AndroidView(
                                factory = { ctx ->
                                    PlayerView(ctx).apply {
                                        player = exoPlayer
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(400.dp)
                            )
                        }

                        Button(
                            onClick = { videoPicker.launch("video/*") },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant, contentColor = MaterialTheme.colorScheme.onPrimary)
                        ) {
                            Text(if (selectedVideoUri == null) "Select Video" else "Change Video", color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }

                        selectedVideoUri?.let { uri ->
                            Text("Selected Video: ${uri.lastPathSegment}", color = MaterialTheme.colorScheme.onSurface, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
            
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(2.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(14.dp))
                        .padding(24.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
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
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        HorizontalDivider(color = MaterialTheme.colorScheme.outline, thickness = 1.dp)
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
                                label = { Text("Age Group", color = MaterialTheme.colorScheme.onSurface) },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = ageGroupOpen) },
                                colors = TextFieldDefaults.textFieldColors(
                                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                                    disabledTextColor = MaterialTheme.colorScheme.onSurface,
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
                        .border(2.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(14.dp))
                        .padding(24.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
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
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        HorizontalDivider(color = MaterialTheme.colorScheme.outline, thickness = 1.dp)
                        OutlinedTextField(
                            value = bodyweight,
                            shape = RoundedCornerShape(8.dp),
                            onValueChange = { bodyweight = it },
                            label = { Text("Bodyweight", color = MaterialTheme.colorScheme.onSurface) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            colors = TextFieldDefaults.textFieldColors(
                                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                                disabledTextColor = MaterialTheme.colorScheme.onSurface,
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
                        .border(2.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(14.dp))
                        .padding(24.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
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
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        HorizontalDivider(color = MaterialTheme.colorScheme.outline, thickness = 1.dp)
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Button(
                                onClick = { natty = true },
                                colors = ButtonDefaults.buttonColors(containerColor = if (natty == true) Color.Green else MaterialTheme.colorScheme.primary)
                            ) {
                                Text("Yes", color = MaterialTheme.colorScheme.onSurface)
                            }
                            Button(
                                onClick = { natty = false },
                                colors = ButtonDefaults.buttonColors(containerColor = if (natty == false) Color.Red else MaterialTheme.colorScheme.primary)
                            ) {
                                Text("No", color = MaterialTheme.colorScheme.onSurface)
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
                        enabled = !isSubmitting,
                        onClick = {
                            if (selectedLift == null || prWeight.isBlank() || selectedCountry == null || natty == null || selectedAgeGroup == null || bodyweight.isBlank() || selectedVideoUri == null || selectedGender.isBlank()) {
                                submitStatus = "Please fill all fields before submitting."
                                return@Button
                            }
                            val exerciseName = selectedLift ?: return@Button
                            val weight = prWeight.toFloatOrNull() ?: return@Button
                            val country = selectedCountry?.getDisplayName() ?: return@Button
                            val isNatural = natty ?: return@Button
                            val age = selectedAgeGroup ?: return@Button
                            val videoUri = selectedVideoUri ?: return@Button
                            val presGender = selectedGender
                            isSubmitting = true
                            submitStatus = "Submitting submission..."
                            scope.launch {
                                val user = Appwrite.getCurrentUser()
                                val userId = user?.id

                                if (userId == null) {
                                    isSubmitting = false
                                    submitStatus = "Failed to verify user. Please try again."
                                    return@launch
                                }

                                val success = viewModel.submitEntry(
                                    exerciseName = exerciseName,
                                    weight = weight,
                                    reps = 1,
                                    country = country,
                                    isNatural = isNatural,
                                    age = age,
                                    gender = presGender,
                                    context = context,
                                    uri = videoUri,
                                    userId = userId
                                )

                                isSubmitting = false
                                submitStatus = if (success) {
                                    "Submission sent, and waiting for review"
                                } else {
                                    "Submission failed to send."
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary, contentColor = MaterialTheme.colorScheme.onPrimary)
                    ) {
                        if (isSubmitting) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(18.dp),
                                color = MaterialTheme.colorScheme.onPrimary,
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Submitting...", color = MaterialTheme.colorScheme.onPrimary)
                        } else {
                            Text("Submit", color = MaterialTheme.colorScheme.onPrimary)
                        }
                    }
                }
            }
        }  
    }
}