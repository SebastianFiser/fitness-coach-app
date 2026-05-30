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
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text("Settings", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.White)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .border(1.dp, Color.DarkGray, RoundedCornerShape(8.dp)),
                    colors = CardDefaults.cardColors(Color.DarkGray),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text(
                        "Rest time selecter placeholder",
                        color = COlor.White,
                    )
                }
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .border(1.dp, Color.DarkGray, RoundedCornerShape(8.dp)),
                    colors = CardDefaults.cardColors(Color.DarkGray),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text(
                        "Unit selecter placeholder",
                        color = COlor.White,
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(4.dp),
                horizontalArrangement = Arrangement.Center,
            ) {
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .border(1.dp, Color.DarkGray, RoundedCornerShape(8.dp)),
                    colors = CardDefaults.cardColors(Color.DarkGray),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text(
                        "Color select placeholder",
                        color = COlor.White,
                    )
                }
            }
        }
    }
}