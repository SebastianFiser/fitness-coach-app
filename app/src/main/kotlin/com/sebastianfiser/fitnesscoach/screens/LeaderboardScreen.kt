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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import com.sebastianfiser.fitnesscoach.models.LeaderBoardEntry
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.shape.CircleShape
import java.time.LocalDate
import com.sebastianfiser.fitnesscoach.models.Exercise
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.foundation.border
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.text.font.FontWeight
import com.sebastianfiser.fitnesscoach.models.FilterData
import com.sebastianfiser.fitnesscoach.models.GenerateFewTimesLeaderBoardData
import androidx.compose.material.icons.filled.Add
import com.sebastianfiser.fitnesscoach.models.AppViewModel
import androidx.navigation.NavController
import com.sebastianfiser.fitnesscoach.navigation.Screen


@Composable
fun LeaderboardScreen(navController: NavController, viewModel: AppViewModel) {
    var selectedParameter by remember { mutableStateOf<String?>(null) }
    val data: List<LeaderBoardEntry> = remember {GenerateFewTimesLeaderBoardData()}
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(Screen.Submission.route) },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("submit PR", color = Color.White)
                    Icon(Icons.Default.Add, contentDescription = "Add PR", tint = Color.White)
                }
            }
        },
        containerColor = Color.Black,
    ) { innerPadding ->
        LazyColumn ( 
            modifier = Modfier
                .fillMaxSize()
                .background(Color.Black)
                .padding(innerPadding)
                .padding(top = 48.dp, bottom = 80.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            item {
                Box (
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.DarkGray)
                ) {
                    Text(
                        "Leaderboard",
                        color = Color.White,
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
            }
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text("Top Users", color = Color.White, style = MaterialTheme.typography.headlineSmall)
            }
            item {
                Card (
                    modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1C1C1E)),
                    shape = RoundedCornerShape(28.dp)
                ) {
                    Box (
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "World Leaderboard",
                            color = Color.White,
                            style = MaterialTheme.typography.headlineMedium
                        )
                    }
                    Box (
                        modifier = Modifier
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        ShowFilterDropdown( onFilterSelected = { selectedParameter = it })
                    }

                    ShowLeaderboard(selectedParameter = selectedParameter, data = data)
                }
            }
        }
    }
}

@Composable
fun ShowFilterDropdown(onFilterSelected: (String?) -> Unit) {
    var filterOpen by remember { mutableStateOf(false) }
    var submenuOpen by remember { mutableStateOf<String?>(null)}
    Box{
        Button(
            onClick = { filterOpen = !filterOpen },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black, contentColor = Color.White)
        ) {
            Icon(
                imageVector = Icons.Default.FilterList,
                contentDescription = null,
                tint = Color.White
            )
            Text("Filter")
        }
        DropdownMenu(
            expanded = filterOpen,
            onDismissRequest = { filterOpen = false },
            modifier = Modifier
                .background(Color(0xFF1C1C1E))
                .border(1.dp, Color.DarkGray, RoundedCornerShape(8.dp))
        ) { 
            DropdownMenuItem(
                text = { Text("Gender") },
                onClick = { submenuOpen = "Gender" },
                colors = MenuDefaults.itemColors(textColor = Color.White)
            )
            if (submenuOpen == "Gender"){
                DropdownMenuItem(
                    text = { Text(" • Male") },
                    onClick = { 
                        filterOpen = false
                        onFilterSelected("Male") },
                    colors = MenuDefaults.itemColors(textColor = Color.White)
                )
                DropdownMenuItem(
                    text = { Text(" • Female") },
                    onClick = { 
                        filterOpen = false
                        onFilterSelected("Female") },
                    colors = MenuDefaults.itemColors(textColor = Color.White)
                )
                DropdownMenuItem(
                    text = { Text(" • Other") },
                    onClick = { 
                        filterOpen = false
                        onFilterSelected("OtherGen") },
                    colors = MenuDefaults.itemColors(textColor = Color.White)
                )
            }
            DropdownMenuItem(
                text = { Text("Age Group") },
                onClick = { submenuOpen = "Age" },
                colors = MenuDefaults.itemColors(textColor = Color.White)
            )
            if (submenuOpen == "Age") {
                DropdownMenuItem(
                    text = { Text(" • Under 18") },
                    onClick = { 
                        filterOpen = false
                        onFilterSelected("Under 18") },
                    colors = MenuDefaults.itemColors(textColor = Color.White)
                )
                DropdownMenuItem(
                    text = { Text(" • 18-25") },
                    onClick = { 
                        filterOpen = false
                        onFilterSelected("18-25") },
                    colors = MenuDefaults.itemColors(textColor = Color.White)
                )
                DropdownMenuItem(
                    text = { Text(" • 26-35") },
                    onClick = { 
                        filterOpen = false
                        onFilterSelected("26-35") },
                    colors = MenuDefaults.itemColors(textColor = Color.White)
                )
                DropdownMenuItem(
                    text = { Text(" • 36-45") },
                    onClick = { 
                        filterOpen = false
                        onFilterSelected("36-45")},
                    colors = MenuDefaults.itemColors(textColor = Color.White)
                )
                DropdownMenuItem(
                    text = { Text(" • 46+") },
                    onClick = { 
                        filterOpen = false
                        onFilterSelected("46+") },
                    colors = MenuDefaults.itemColors(textColor = Color.White)
                )
            }
            DropdownMenuItem(
                text = { Text("Natural/Enhanced") },
                onClick = { submenuOpen = "Natty" },
                colors = MenuDefaults.itemColors(textColor = Color.White)
            )
            if (submenuOpen == "Natty"){
                DropdownMenuItem(
                    text = { Text(" • Natural") },
                    onClick = { 
                        filterOpen = false
                        onFilterSelected("Natural") },
                    colors = MenuDefaults.itemColors(textColor = Color.White)
                )
                DropdownMenuItem(
                    text = { Text(" • Enhanced") },
                    onClick = { 
                        filterOpen = false
                        onFilterSelected("Enhanced") },
                    colors = MenuDefaults.itemColors(textColor = Color.White)
                )
            }
            DropdownMenuItem(
                text = { Text("Nationality") },
                onClick = { submenuOpen = "Nationality" },
                colors = MenuDefaults.itemColors(textColor = Color.White)
            )
            if (submenuOpen == "Nationality") {
                DropdownMenuItem(
                    text = { Text(" • Czech Republic") },
                    onClick = { 
                        filterOpen = false
                        onFilterSelected("CZ") },
                    colors = MenuDefaults.itemColors(textColor = Color.White)
                )
                DropdownMenuItem(
                    text = { Text(" • United States") },
                    onClick = { 
                        filterOpen = false
                        onFilterSelected("USA") },
                    colors = MenuDefaults.itemColors(textColor = Color.White)
                )
                DropdownMenuItem(
                    text = { Text(" • Germany") },
                    onClick = { 
                        filterOpen = false
                        onFilterSelected("GE") },
                    colors = MenuDefaults.itemColors(textColor = Color.White)
                )
                DropdownMenuItem(
                    text = { Text(" • United Kingdom") },
                    onClick = { 
                        filterOpen = false
                        onFilterSelected("UK") },
                    colors = MenuDefaults.itemColors(textColor = Color.White)
                )
                DropdownMenuItem(
                    text = { Text(" • Other") },
                    onClick = { 
                        filterOpen = false
                        onFilterSelected("Other") },
                    colors = MenuDefaults.itemColors(textColor = Color.White)
                )
            }
            DropdownMenuItem(
                text = { Text("Lift") },
                onClick = { submenuOpen = "Lift" },
                colors = MenuDefaults.itemColors(textColor = Color.White)
            )
            if (submenuOpen == "Lift") {
                DropdownMenuItem(
                    text = { Text(" • Squat") },
                    onClick = { 
                        filterOpen = false
                        onFilterSelected("Squat") },
                    colors = MenuDefaults.itemColors(textColor = Color.White)
                )
                DropdownMenuItem(
                    text = { Text(" • Bench Press") },
                    onClick = { 
                        filterOpen = false
                        onFilterSelected("Bench Press") },
                    colors = MenuDefaults.itemColors(textColor = Color.White)
                )
                DropdownMenuItem(
                    text = { Text(" • Deadlift") },
                    onClick = { 
                        filterOpen = false
                        onFilterSelected("Deadlift") },
                    colors = MenuDefaults.itemColors(textColor = Color.White)
                )
            }
        }
    }
}

@Composable
fun ShowLeaderboard(selectedParameter: String?, data: List<LeaderBoardEntry>) {
    val dataFiltered = FilterData(selectedParameter, data)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        dataFiltered.forEach { entry ->
            LeaderboardRow(entry)
        }
    }
}

@Composable
fun LeaderboardRow(entry: LeaderBoardEntry) {
    Column() {
        HorizontalDivider(
            color = Color.Gray,
            thickness = 0.5.dp,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 2.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .clip(CircleShape)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "${entry.rank}",
                    color = Color.Black,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                    .padding(4.dp)
                )
            }

            Text(
                "${entry.username}",
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .padding(2.dp)
            )

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    "${entry.lift}",
                    color = Color.LightGray,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                    .padding(top = 8.dp, bottom = 2.dp)
                )
                Text(
                    "${entry.weight} kg",
                    color = Color.White,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                    .padding(top = 2.dp, bottom = 8.dp)
                )
            }
        }
    }
}
