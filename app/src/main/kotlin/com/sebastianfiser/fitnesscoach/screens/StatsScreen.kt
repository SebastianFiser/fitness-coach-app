package com.sebastianfiser.fitnesscoach.screens

import com.sebastianfiser.fitnesscoach.models.AppViewModel
import com.sebastianfiser.fitnesscoach.models.ExerciseStat
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.Icons
import androidx.navigation.NavController
import com.sebastianfiser.fitnesscoach.models.Appwrite
import androidx.compose.runtime.LaunchedEffect

@Composable
fun StatsScreen(viewModel: AppViewModel, navController: NavController) {
    val userId = Appwrite.account.get().id
    LaunchedEffect(Unit) {
        viewModel.prData = viewModel.getPRData(userId)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .padding(top = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
           IconButton (onClick = { navController.popBackStack() }) {     
                Icon(
                    imageVector = Icons.Default.KeyboardArrowLeft,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
        }
        Text("Your Progress", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
        Spacer(modifier = Modifier.height(16.dp))

        stats.forEach { stat ->
        Card(
            modifier = Modifier
                .background(Color(0xFF1C1C1E))
                .fillMaxWidth()
                .padding(top = 4.dp, bottom = 4.dp),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(Color(0xFF1C1C1E)),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
                    .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                    .padding(16.dp)
            ) {
                Text(stat.name, fontSize = 20.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
                Spacer(modifier = Modifier.height(8.dp))
                Text("PR: ${stat.pr} ${viewModel.unit}", fontSize = 16.sp, color = Color.White)
                Text("Weekly Gain: ${stat.weeklyGain} ${viewModel.unit}", fontSize = 16.sp, color = Color.White)
            }
            Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}