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


@Composable
fun SettingsScreen(viewModel: AppViewModel, navController: NavController) {
    val minutes = viewModel.restTime / 60
    val seconds = viewModel.restTime % 60
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(horizontal = 24.dp, vertical = 48.dp),
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
            horizontalArrangement = Arrangement.CenterHorizontally,
        ) {
            Text("Settings", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.White)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                //Rest time
                Card(
                    modifier = Modifier
                        .padding(4.dp)
                        .weight(1f),
                    contentAlignment = Alignment.Center,
                    RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(Color(0xFF1C1C1E))
                ) {
                    Row(
                        modifier = Modifier
                            .fillmaxWidth()
                            .background(Color(0xFF1A1A1A), RoundedCornerShape(16.dp))
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f),
                            contentAlignment = Alignment.center
                        ) {
                            Text("+", color = Color.White, fontSize = 24.sp, modifier = Modifier.clickable { viewModel.restTime += 5 })
                        }
                        Divider(modifier = Modifier.fillMaxHeight().width(1.dp), color = Color.DarkGray)
                        Box(
                            modifier = Modifier.weight(2f),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("${minutes}:${seconds.toString().padStart(2, '0')}", color = Color.White, fontSize = 16.sp)
                        }
                        Divider(modifier = Modifier.fillMaxHeight().width(1.dp), color = Color.DarkGray)
                        Box(
                            modifier = Modifier
                                .weight(1f),
                            contentAlignment = Alignment.center
                        ) {
                            Text("-", color = Color.White, fontSize = 24.sp, modifier = Modifier.clickable { viewModel.restTime -= 5 })
                        }
                    }
                }
                //Unit select
                Card(
                    modifier = Modifier
                        .padding(4.dp)
                        .weight(1f),
                    contentAlignment = Alignment.CenterStart,
                    RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(Color(0xFF1C1C1E))
                ) {

                }
            }
            //Theme select in work
            /* 
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Theme", color = Color.Gray, fontSize = 14.sp)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text("Dark Mode", color = Color.White, fontSize = 16.sp)
                    Switch(
                        checked = viewModel.isDarkTheme,
                        onCheckedChange = { viewModel.isDarkTheme = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            uncheckedThumbColor = Color.Gray,
                            checkedTrackColor = Color.White.copy(alpha = 0.5f),
                            uncheckedTrackColor = Color.Gray.copy(alpha = 0.5f)
                        )
                    )
                }
            }*/
        }
    }
}