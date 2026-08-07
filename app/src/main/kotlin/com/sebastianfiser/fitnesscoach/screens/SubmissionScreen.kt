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
import androidx.compose.animation.AnimatedContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubmissionScreen(navController: NavController, viewModel: AppViewModel) {
    var selectedLift by remember { mutableStateOf<String?>(null) }
    var liftOpen by remember { mutableStateOf(false) }
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
    var ageGroupOpen by remember { mutableStateOf(false) }
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
    var currentStep by remember { mutableIntStateOf(0) }
    val totalSteps = 8

    DisposableEffect(exoPlayer) {
        onDispose {
            exoPlayer?.release()
        }
    }

    Scaffold (

    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Submit Your PR",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                LinearProgressIndicator(
                    progress = { (currentStep + 1) / totalSteps.toFloat() },
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                )
            }

            AnimatedContent(
                targetState = currentStep,
                label = "StepAnimation",
                modifier = Modifier.weight(1f)
            ) { step ->
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    when (step) {
                        0 -> CardLift(liftOpen = liftOpen, selectedLift = selectedLift, onOpenChange = { liftOpen = it }, onLiftSelect = { selectedLift = it }) //cards
                        1 -> CardWeight(prWeight = prWeight, onWeightChange = { prWeight = it })
                        2 -> GenderCard(selectedGender = selectedGender, genderOpen = genderOpen, onGenderChange = { selectedGender = it }, onOpenChange = { genderOpen = it })
                        3 -> CountryCard(selectedCountry = selectedCountry, countryOpen = countryOpen, onSelectedCountryChange = { selectedCountry = it }, onOpenChange = { countryOpen = it } )
                        //4 -> videoCard()
                        //5 -> ageCard()
                        //6 -> bodyCard()
                        //7 -> nattyCard()
                        else -> Text("Unknown step: $step")
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (currentStep > 0) {
                    OutlinedButton(onClick = { currentStep-- }) {
                        Text("Back")
                    }
                } else {
                    Spacer(modifier = Modifier.width(1.dp))
                }

                Button(
                    onClick = {
                        if (currentStep < totalSteps - 1) {
                            currentStep++
                        } else {
                            //APPWRITE SENDING
                        }
                    }
                ) {
                    Text(if (currentStep < totalSteps - 1) "Next" else "Submit")
                }

            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardLift(liftOpen: Boolean, selectedLift: String?, onOpenChange: (Boolean) -> Unit, onLiftSelect: (String) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(2.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(14.dp))
            .padding(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(28.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Select Lift",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            HorizontalDivider(color = MaterialTheme.colorScheme.outline, thickness = 1.dp)
            ExposedDropdownMenuBox(
                expanded = liftOpen,
                onExpandedChange = { onOpenChange(it) }
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
                        disabledIndicatorColor = Color.Transparent,
                    )
                )
                ExposedDropdownMenu(
                    expanded = liftOpen,
                    onDismissRequest = { onOpenChange(false) }
                ) {
                    listOf("Squat", "Bench press", "Deadlift").forEach { lift ->
                        DropdownMenuItem(
                            text = { Text(lift) },
                            onClick = {
                                onLiftSelect(lift)
                                onOpenChange(false)
                            }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardWeight(prWeight: String, onWeightChange: (String) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(2.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(14.dp))
            .padding(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(14.dp)
    ) {
        Column (
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
                onValueChange = { onWeightChange(it) },
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenderCard(selectedGender: String, genderOpen: Boolean, onGenderChange: (String) -> Unit, onOpenChange: (Boolean) -> Unit) {
    Card (
        modifier = Modifier
            .fillMaxWidth()
            .border(2.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(14.dp))
            .padding(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface,
        ),
        shape = RoundedCornerShape(14.dp)
    ) {
        Column (
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
                onExpandedChange = { onOpenChange(it) }
            ) {
                OutlinedTextField(
                    modifier = Modifier.menuAnchor(),
                    shape = RoundedCornerShape(8.dp),
                    value = selectedGender,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Gender", color = MaterialTheme.colorScheme.onSurface) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = genderOpen) },
                    colors = TextFieldDefaults.colors(
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
                    onDismissRequest = { onOpenChange(false) }
                ) {
                    listOf("Male", "Female", "Other").forEach { gender ->
                        DropdownMenuItem(
                            text = { Text(gender)},
                            onClick = {
                                onOpenChange(false)
                                onGenderChange(gender)
                            }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountryCard(selectedCountry: Country?, countryOpen: Boolean, onSelectedCountryChange: (Country?) -> Unit, onOpenChange: (Boolean) -> Unit) {
    val countries = remember { Countries.repository.getAll() }
    var countryQuery by remember { mutableStateOf("") }
    Card (
        modifier = Modifier
            .fillMaxWidth()
            .border(2.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(14.dp))
            .padding(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(14.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Select your country",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            HorizontalDivider(color = MaterialTheme.colorScheme.outline, thickness = 1.dp)
            ExposedDropdownMenuBox(
                expanded = countryOpen,
                onExpandedChange = { onOpenChange(it) }
            ) {
                OutlinedTextField(
                    modifier = Modifier.menuAnchor(),
                    shape = RoundedCornerShape(14.dp),
                    value = countryQuery,
                    onValueChange = { countryQuery = it; onOpenChange(true) },
                    readOnly = false,
                    label = { Text("Country", color = MaterialTheme.colorScheme.onSurface)},
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
                    onDismissRequest = { onOpenChange(false) }
                ) {
                    countries.filter { it.getDisplayName().contains(countryQuery, ignoreCase = true) }.forEach { country ->
                        DropdownMenuItem(
                            text = { Text(country.getDisplayName()) },
                            onClick = {
                                selectedCountry = onSelectedCountryChange(country)
                                countryQuery = country.getDisplayName()
                                countryOpen = onOpenChange(false)
                            }
                        )
                    }
                }
            }
        }
    }
}
