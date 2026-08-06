package com.sebastianfiser.fitnesscoach.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sebastianfiser.fitnesscoach.models.AppViewModel
import androidx.navigation.NavController
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.foundation.clickable
import androidx.compose.foundation.border
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.NightlightRound
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.rememberCoroutineScope



@Composable
fun SettingsScreen(viewModel: AppViewModel, navController: NavController) {
    val minutes = viewModel.restTime / 60
    val seconds = viewModel.restTime % 60
    val themes = listOf(
        Triple("Light", Icons.Default.WbSunny, false),
        Triple("Dark", Icons.Default.NightlightRound, true),
        Triple("System", Icons.Default.Settings, null)
    )
    val scope = rememberCoroutineScope()

    DisposableEffect(Unit) {
        onDispose {
            viewModel.updateUserSettingsAsync()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 12.dp)
            .padding(top = 24.dp, bottom = 114.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp, top = 12.dp),
            horizontalArrangement = Arrangement.Start
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Default.KeyboardArrowLeft, contentDescription = "Back", tint = MaterialTheme.colorScheme.onBackground)
            }
        }
        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text("Settings", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .weight(1f)
                    .padding(4.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                        .padding(end = 2.dp)
                        .border(1.dp, Color.Gray, RoundedCornerShape(8.dp)),
                    colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 28.dp, vertical = 40.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text("Rest Time", color = MaterialTheme.colorScheme.onSurface, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        Card(
                            modifier = Modifier
                                .fillMaxSize()
                                .border(1.dp, Color.Gray, RoundedCornerShape(8.dp)),
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {viewModel.restTime += 5}
                                        .background(MaterialTheme.colorScheme.onSurfaceVariant)
                                        .weight(1f),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.KeyboardArrowUp, contentDescription = "Increase time", tint = MaterialTheme.colorScheme.onSurface)
                                }
                                Divider(color = MaterialTheme.colorScheme.outline, thickness = 1.dp)
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(MaterialTheme.colorScheme.background)
                                        .weight(3f),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(String.format("%02d:%02d", minutes, seconds), color = MaterialTheme.colorScheme.onBackground, fontSize = 32.sp)
                                }
                                Divider(color = MaterialTheme.colorScheme.outline, thickness = 1.dp)
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {viewModel.restTime -= 5}
                                        .background(MaterialTheme.colorScheme.onSurfaceVariant)
                                        .weight(1f),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Decrease Rest Time", tint = MaterialTheme.colorScheme.onSurface)
                                }
                            }
                        }
                    }
                }
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                        .padding(start = 2.dp)
                        .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(8.dp)),
                    colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text("Unit System", color = MaterialTheme.colorScheme.onSurface, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))

                    //Unit sider
                        Card(
                            modifier = Modifier
                                .padding(horizontal = 12.dp, vertical = 24.dp)
                                .fillMaxSize()
                                .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(8.dp)),
                            shape = RoundedCornerShape(8.dp),
                            colors = CardDefaults.cardColors(MaterialTheme.colorScheme.onSurfaceVariant)
                        ) {
                            listOf("kg", "lbs").forEach { unit ->
                                val selected = viewModel.unit == unit
                                TextButton(
                                    onClick = { viewModel.unit = unit },
                                    colors = ButtonDefaults.textButtonColors(
                                        contentColor = if (selected) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onSurface,
                                        containerColor = if (selected) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.onSurfaceVariant
                                    ),
                                    modifier = Modifier.fillMaxSize().weight(1f),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(unit, fontSize = 18.sp, color = if (selected) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onSurface)
                                }
                            }
                        }
                    }
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .weight(1f),
                horizontalArrangement = Arrangement.Center,
            ) {
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                        .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(8.dp)),
                    colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                            .background(MaterialTheme.colorScheme.surface),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                        ) {
                            Text("Color Themes", color = MaterialTheme.colorScheme.onSurface, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 12.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            themes.forEach { (name, icon, isDark) ->
                                val selected = viewModel.isDarkTheme == isDark
                                Card(
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxHeight()
                                        .clickable { viewModel.isDarkTheme = isDark }
                                        .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(8.dp)),
                                    colors = CardDefaults.cardColors(
                                        containerColor = if (selected) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.onSurfaceVariant,
                                        contentColor = if (selected) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onSurface
                                    ),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Column(
                                        modifier = Modifier.fillMaxSize(),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        Icon(icon, contentDescription = name, tint = if (selected) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onSurface)
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(name, fontSize = 14.sp, color = if (selected) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onSurface)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
