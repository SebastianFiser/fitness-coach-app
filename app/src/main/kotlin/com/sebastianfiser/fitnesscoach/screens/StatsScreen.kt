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
    LaunchedEffect(Unit) {
        val currentUser = Appwrite.getCurrentUser()
        val userId = currentUser?.id ?: return@LaunchedEffect
        viewModel.loadPrData(userId)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.background)
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
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        }
        Text("Your Progress", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
        Spacer(modifier = Modifier.height(16.dp))

        viewModel.prData.entries.forEach { (exerciseName, pr) ->
        Card(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .fillMaxWidth()
                .padding(top = 4.dp, bottom = 4.dp),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface),
        ) {
                val convPr = viewModel.convertUnit(pr, viewModel.unit)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
                    .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(8.dp))
                    .padding(16.dp)
            ) {
                Text(exerciseName, fontSize = 20.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
                Spacer(modifier = Modifier.height(8.dp))
                Text("PR: ${convPr} ${viewModel.unit}", fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface)
                Text("Weekly Gain: 'Placeholder' ${viewModel.unit}", fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface)
            }
            Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}