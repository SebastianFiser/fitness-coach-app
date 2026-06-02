package com.sebastianfiser.fitnesscoach.screens

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

@Composable
fun ProfileScreen(navController : NavController) {
    var selectedTab by remember { mutableStateOf(0) }
    val scope = rememberCoroutineScope()
    var showAlert by remember { mutableStateOf(false) }
    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
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
                TextButton(
                    onClick = {}
                ) {
                    Icon(Icons.Default.Create , contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Edit Profile", color = Color.White)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .border(3.dp, Color(0xFFD4B896), CircleShape)
                    .background(Color.Gray),
                contentAlignment = Alignment.Center
            ) {
                Text("U", color = Color.White, style = MaterialTheme.typography.headlineLarge)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text("User", color = Color.White, style = MaterialTheme.typography.headlineSmall)
            Text("user.email@email.com", color = Color.LightGray, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(16.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text("PERSONAL & APP ACTIVITY", color = Color.LightGray, style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Card (
                    modifier = Modifier
                        .padding(vertical = 4.dp)
                        .border(2.dp, Color.DarkGray, RoundedCornerShape(14.dp))
                        .fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1C1C1E)),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row (
                            modifier = Modifier
                                .clickable { navController.navigate(Screen.Stats.route) }
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.BarChart, contentDescription = null, tint = Color.White)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("My Stats", color = Color.White, style = MaterialTheme.typography.bodyMedium)
                            Spacer(modifier = Modifier.weight(1f))
                            Icon(Icons.Default.KeyboardArrowRight, contentDescription = null, tint = Color.LightGray)
                        }
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = Color.DarkGray)
                        Row (
                            modifier = Modifier
                                .clickable { navController.navigate(Screen.Settings.route) }
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Settings, contentDescription = null, tint = Color.White)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Settings", color = Color.White, style = MaterialTheme.typography.bodyMedium)
                            Spacer(modifier = Modifier.weight(1f))
                            Icon(Icons.Default.KeyboardArrowRight, contentDescription = null, tint = Color.LightGray)
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
                Text("ACCOUNT OPTIONS", color = Color.LightGray, style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Card (
                    modifier = Modifier
                        .padding(vertical = 4.dp)
                        .border(2.dp, Color.DarkGray, RoundedCornerShape(14.dp))
                        .fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1C1C1E)),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row (
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Sync, contentDescription = null, tint = Color.White)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Switch Account", color = Color.White, style = MaterialTheme.typography.bodyMedium)
                            Spacer(modifier = Modifier.weight(1f))
                            Icon(Icons.Default.KeyboardArrowRight, contentDescription = null, tint = Color.LightGray)
                        }
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = Color.DarkGray)
                        Row (
                            modifier = Modifier
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, tint = Color.White)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Add Account", color = Color.White, style = MaterialTheme.typography.bodyMedium)
                            Spacer(modifier = Modifier.weight(1f))
                            Icon(Icons.Default.KeyboardArrowRight, contentDescription = null, tint = Color.LightGray)
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
                Text("DANGER ZONE", color = Color.LightGray, style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Card (
                    modifier = Modifier
                        .padding(vertical = 4.dp)
                        .padding(bottom = 95.dp)
                        .border(2.dp, Color(0xFFFF6B6B), RoundedCornerShape(14.dp))
                        .fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1C1C1E)),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row (
                            modifier = Modifier
                                .clickable { navController.navigate(Screen.Login.route) 
                                    try {
                                        scope.launch {
                                            Appwrite.onLogout()
                                        }
                                    } catch (e: Throwable) {
                                        // Handle logout error if needed
                                    }
                                }
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.ExitToApp, contentDescription = null, tint = Color.White)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Logout", color = Color.White, style = MaterialTheme.typography.bodyMedium)
                            Spacer(modifier = Modifier.weight(1f))
                            Icon(Icons.Default.KeyboardArrowRight, contentDescription = null, tint = Color.LightGray)
                        }
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = Color.DarkGray)
                        Row (
                            modifier = Modifier
                                .clickable { showAlert = true }
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if(showAlert) {
                                 AlertDialog(
                                    containerColor = Color(0xFF1A1A1A),
                                    shape = RoundedCornerShape(16.dp),
                                    onDismissRequest = { showAlert = false },
                                    title = { Text("Delete Account", color = Color.Red) },
                                    text = { Text("Are you sure you want to delete your account?", color = Color.LightGray) },
                                    confirmButton = {
                                        TextButton(
                                            onClick = {
                                                showAlert = false
                                                scope.launch{
                                                    /*Todo */
                                                } 
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
                            Icon(Icons.Default.KeyboardArrowRight, contentDescription = null, tint = Color.LightGray)
                        }
                    }
                }
            }
        }
    }
}
