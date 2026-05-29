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

@Composable
fun SettingsScreen(viewModel: AppViewModel, navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(horizontal = 24.dp, vertical = 48.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        Row(
            verticalAlignment = Alignment.Start,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Default.KeyboardArrowRight, contentDescription = "Back", tint = Color.White)
            }
        }
        Text("Settings", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.White)

        //Rest time
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Rest Time", color = Color.Gray, fontSize = 14.sp)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                TextButton(
                    onClick = { if (viewModel.restTime > 10) viewModel.restTime -= 5},
                    colors = ButtonDefaults.textButtonColors(contentColor = Color.White, disabledContentColor = Color.DarkGray)
                ) {
                    Text("-5s", color = Color.White, fontSize = 16.sp) }
                
                Text("${viewModel.restTime}s", color = Color.White, fontSize = 20.sp)

                TextButton(
                    onClick = { viewModel.restTime += 5 },
                    colors = ButtonDefaults.textButtonColors(contentColor = Color.White)
                ) {
                    Text("+5s", color = Color.White, fontSize = 16.sp) }
            }
        }
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Units", color = Color.Gray, fontSize = 14.sp)
            Row(
                modifier = Modifier
                    .background(Color(0xFF1A1A1A), RoundedCornerShape(8.dp))
                    .padding(4.dp)
            ) {
                listOf("kg", "lbs").forEach { option ->
                    val selected = viewModel.unit == option
                    TextButton(
                        onClick = { viewModel.unit = option },
                        colors = ButtonDefaults.textButtonColors(
                            containerColor = if (selected) Color.White else Color.Transparent,
                            contentColor = if (selected) Color.Black else Color.White
                        ),
                        shape = RoundedCornerShape(50)
                    ) {
                        Text(option, color = if (selected) Color.Black else Color.Gray)
                    }
                }
            }
        }
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
        }
    }
}