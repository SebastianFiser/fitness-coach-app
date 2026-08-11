package com.sebastianfiser.fitnesscoach.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.ui.draw.clip
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.border
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.foundation.clickable
import androidx.navigation.NavController
import com.sebastianfiser.fitnesscoach.models.AppViewModel
import com.sebastianfiser.fitnesscoach.navigation.Screen
import com.sebastianfiser.fitnesscoach.models.Appwrite
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.LaunchedEffect
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.PickVisualMediaRequest
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.Image
import androidx.compose.ui.graphics.asImageBitmap
import android.graphics.BitmapFactory
import androidx.compose.ui.layout.ContentScale
import com.sebastianfiser.fitnesscoach.ui.components.ComingSoonOverlay
import androidx.compose.runtime.collectAsState
import com.sebastianfiser.fitnesscoach.models.ProfileImageState

@Composable
fun ProfileScreen(navController: NavController, viewModel: AppViewModel) {
    var userName by remember { mutableStateOf("") }
    var userEmail by remember { mutableStateOf("") }
    var selectedTab by remember { mutableStateOf(0) }
    val scope = rememberCoroutineScope()
    var showAlert by remember { mutableStateOf(false) }
    var userIcUri by remember { mutableStateOf(Uri.EMPTY) }
    val context = LocalContext.current

    val imageState by viewModel.imageState.collectAsState()

    val pickMedia = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        userIcUri = uri ?: Uri.EMPTY
    }

    LaunchedEffect(Unit) {
        scope.launch {
            val currentUser = Appwrite.getCurrentUser()
            userName = currentUser?.name ?: "User"
            userEmail = currentUser?.email ?: "user@email.com"
            val userId = currentUser?.id ?: return@LaunchedEffect

            viewModel.loadProfileImage(userId)
        }
    }

    LaunchedEffect(userIcUri) {
        if (userIcUri != Uri.EMPTY) {
            val currentUser = Appwrite.getCurrentUser()
            val userId = currentUser?.id ?: return@LaunchedEffect
            scope.launch {
                val result = viewModel.uploadImage(context, userIcUri, userId)
                result.onSuccess { fileId ->
                    viewModel.userIconId = fileId
                }.onFailure { error ->
                    // Handle the error, e.g., show a message to the user
                }
            }
        }
    }

    LaunchedEffect(viewModel.accountDeleted) {
        if (viewModel.accountDeleted) {
            navController.navigate(Screen.Login.route) {
                popUpTo(0) { inclusive = true }
            }
            viewModel.accountDeleted = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(top = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                ComingSoonOverlay {
                    TextButton(onClick = {}) {
                        Icon(Icons.Default.Create, contentDescription = null, tint = MaterialTheme.colorScheme.onBackground)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Edit Profile", color = MaterialTheme.colorScheme.onBackground)
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .size(85.dp)
                    .clip(CircleShape)
                    .background(Color.Transparent)
                    .clickable {
                        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                    },
                contentAlignment = Alignment.Center
            ) {
                val capedFirLetUname = userName.firstOrNull()?.uppercase() ?: "U"

                when (val state = imageState) {
                    is ProfileImageState.Loading -> {
                        CircularProgressIndicator(
                            modifier = Modifier.size(40.dp),
                            color = Color(0xFFD4B896)
                        )
                    }
                    is ProfileImageState.Sucess -> {
                        Image(
                            bitmap = state.bitmap.asImageBitmap(),
                            contentDescription = "User Icon",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(80.dp)
                                .clip(CircleShape)
                                .border(3.dp, Color(0xFFD4B896), CircleShape)
                        )
                    }
                    is ProfileImageState.Error -> {
                        Box (
                            modifier = Modifier
                                .size(80.dp)
                                .clip(CircleShape)
                                .border(3.dp, Color(0xFFD4B896), CircleShape)
                                .background(Color.Gray),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text (
                                text = capedFirLetUname,
                                color = Color.White,
                                style = MaterialTheme.typography.headlineLarge
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(userName, color = MaterialTheme.colorScheme.onBackground, style = MaterialTheme.typography.headlineSmall)
            Text(userEmail, color = MaterialTheme.colorScheme.onBackground, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(16.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text("PERSONAL & APP ACTIVITY", color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Card(
                    modifier = Modifier
                        .padding(vertical = 4.dp)
                        .border(2.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(14.dp))
                        .fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .clickable { navController.navigate(Screen.Stats.route) }
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.BarChart, contentDescription = null, tint = MaterialTheme.colorScheme.onSurface)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("My Stats", color = MaterialTheme.colorScheme.onSurface, style = MaterialTheme.typography.bodyMedium)
                            Spacer(modifier = Modifier.weight(1f))
                            Icon(Icons.Default.KeyboardArrowRight, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.outline)
                        Row(
                            modifier = Modifier
                                .clickable { navController.navigate(Screen.Settings.route) }
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Settings, contentDescription = null, tint = MaterialTheme.colorScheme.onSurface)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Settings", color = MaterialTheme.colorScheme.onSurface, style = MaterialTheme.typography.bodyMedium)
                            Spacer(modifier = Modifier.weight(1f))
                            Icon(Icons.Default.KeyboardArrowRight, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text("DANGER ZONE", color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Card(
                    modifier = Modifier
                        .padding(vertical = 4.dp)
                        .padding(bottom = 95.dp)
                        .border(2.dp, Color(0xFFFF6B6B), RoundedCornerShape(14.dp))
                        .fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .clickable {
                                    try {
                                        scope.launch {
                                            Appwrite.onLogout()
                                        }
                                    } catch (e: Throwable) {
                                        // Handle logout error if needed
                                    }
                                    viewModel.clearUserState()
                                    navController.navigate(Screen.Login.route) {
                                        popUpTo(0) { inclusive = true }
                                    }
                                }
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.ExitToApp, contentDescription = null, tint = MaterialTheme.colorScheme.onSurface)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Logout", color = MaterialTheme.colorScheme.onSurface, style = MaterialTheme.typography.bodyMedium)
                            Spacer(modifier = Modifier.weight(1f))
                            Icon(Icons.Default.KeyboardArrowRight, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.outline)
                        Row(
                            modifier = Modifier
                                .clickable { showAlert = true }
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (showAlert) {
                                AlertDialog(
                                    containerColor = MaterialTheme.colorScheme.surface,
                                    shape = RoundedCornerShape(16.dp),
                                    onDismissRequest = { showAlert = false },
                                    title = { Text("Delete Account", color = Color.Red) },
                                    text = { Text("Are you sure you want to delete your account?", color = MaterialTheme.colorScheme.onSurface) },
                                    confirmButton = {
                                        TextButton(
                                            onClick = {
                                                showAlert = false
                                                viewModel.deleteAccount()
                                            }
                                        ) {
                                            Text("Yes", color = Color.Red)
                                        }
                                    },
                                    dismissButton = {
                                        TextButton(
                                            onClick = { showAlert = false }
                                        ) {
                                            Text("No", color = Color.Green)
                                        }
                                    }
                                )
                            }
                            Icon(Icons.Default.Delete, contentDescription = null, tint = Color(0xFFFF6B6B))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Delete Account", color = Color(0xFFFF6B6B), style = MaterialTheme.typography.bodyMedium)
                            Spacer(modifier = Modifier.weight(1f))
                            Icon(Icons.Default.KeyboardArrowRight, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }
        }
    }
}
