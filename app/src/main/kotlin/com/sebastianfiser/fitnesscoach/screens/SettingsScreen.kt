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


@Composable
fun SettingsScreen(viewModel: AppViewModel, navController: NavController) {
    val minutes = viewModel.restTime / 60
    val seconds = viewModel.restTime % 60
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(horizontal = 12.dp)
            .padding(top = 24.dp, bottom = 24.dp)
            .padding(bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.Start
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Default.KeyboardArrowLeft, contentDescription = "Back", tint = Color.White)
            }
        }
        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text("Settings", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.White)

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
                    colors = CardDefaults.cardColors(Color(0xFF1C1C1E)),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text("Rest Time", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
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
                                        .background(Color.DarkGray)
                                        .weight(1f),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.KeyboardArrowUp, contentDescription = "Increase time", tint = Color.White)
                                }
                                Divider(color = Color.Gray, thickness = 1.dp)
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(Color.Black)
                                        .weight(3f)
                                        .padding(vertical = 8.dp),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(String.format("%02d:%02d", minutes, seconds), color = Color.White, fontSize = 32.sp)
                                }
                                Divider(color = Color.Gray, thickness = 1.dp)
                                Row(
                                    modifier = Modifier                                        
                                        .fillMaxWidth()
                                        .clickable {viewModel.restTime -= 5}
                                        .background(Color.DarkGray)
                                        .weight(1f),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Decrease Rest Time", tint = Color.White)
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
                        .border(1.dp, Color.Gray, RoundedCornerShape(8.dp)),
                    colors = CardDefaults.cardColors(Color(0xFF1C1C1E)),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text("Unit System", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        
                    //Unit sider
                        Card(
                            modifier = Modifier
                                .padding(horizontal = 12.dp, vertical = 24.dp)
                                .fillMaxSize()
                                .border(1.dp, Color.Gray, RoundedCornerShape(8.dp)),
                            shape = RoundedCornerShape(8.dp),
                            colors = CardDefaults.cardColors(Color.DarkGray)
                        ) {
                            listOf("kg", "lbs").forEach { unit ->
                                val selected = viewModel.unit == unit
                                TextButton(
                                    onClick = { viewModel.unit = unit },
                                    colors = ButtonDefaults.textButtonColors(
                                        contentColor = if (selected) Color.Black else Color.Gray,
                                        containerColor = if (selected) Color.White else Color.DarkGray
                                    ),
                                    modifier = Modifier.fillMaxSize().weight(1f),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(unit, fontSize = 18.sp, color = if (selected) Color.White else Color.Gray)
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
                        .border(1.dp, Color.DarkGray, RoundedCornerShape(8.dp)),
                    colors = CardDefaults.cardColors(Color(0xFF1C1C1E)),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text(
                        "Color select placeholder",
                        color = Color.White,
                    )
                }
            }
        }
    }
}