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

@Composable
fun LeaderboardScreen() {
    Column ( 
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(top = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
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
        Spacer(modifier = Modifier.height(16.dp))
        Text("Top Users", color = Color.White, style = MaterialTheme.typography.headlineSmall)

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
                ShowFilterDropdown()
            }

            ShowLeaderboard()
        }
    }
}

@Composable
fun ShowFilterDropdown() {
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
                    text = { Text("Male") },
                    onClick = { /* Handle Male filter */ },
                    colors = MenuDefaults.itemColors(textColor = Color.White)
                )
                DropdownMenuItem(
                    text = { Text("Female") },
                    onClick = { /* Handle Female Filter */ },
                    colors = MenuDefaults.itemColors(textColor = Color.White)
                )
                DropdownMenuItem(
                    text = { Text("Other") },
                    onClick = { /* Handle Other Filter */ },
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
                    text = { Text("Under 18") },
                    onClick = { /* Handle Under 18 filter */ },
                    colors = MenuDefaults.itemColors(textColor = Color.White)
                )
                DropdownMenuItem(
                    text = { Text("18-25") },
                    onClick = { /* Handle 18-25 filter */ },
                    colors = MenuDefaults.itemColors(textColor = Color.White)
                )
                DropdownMenuItem(
                    text = { Text("26-35") },
                    onClick = { /* Handle 26-35 Filter */ },
                    colors = MenuDefaults.itemColors(textColor = Color.White)
                )
                DropdownMenuItem(
                    text = { Text("36-45") },
                    onClick = { /* Handle 36-45 Filter */ },
                    colors = MenuDefaults.itemColors(textColor = Color.White)
                )
                DropdownMenuItem(
                    text = { Text("46+") },
                    onClick = { /* Handle 46+ Filter */ },
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
                    text = { Text("Natural") },
                    onClick = { /* Handle Natural Filter */ },
                    colors = MenuDefaults.itemColors(textColor = Color.White)
                )
                DropdownMenuItem(
                    text = { Text("Enhanced") },
                    onClick = { /* Handle Enhanced Filter */ },
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
                    text = { Text("Czech Republic") },
                    onClick = { /* Handle Czech Republic Filter */ },
                    colors = MenuDefaults.itemColors(textColor = Color.White)
                )
                DropdownMenuItem(
                    text = { Text("United States") },
                    onClick = { /* Handle United States Filter */ },
                    colors = MenuDefaults.itemColors(textColor = Color.White)
                )
                DropdownMenuItem(
                    text = { Text("Germany") },
                    onClick = { /* Handle Germany Filter */ },
                    colors = MenuDefaults.itemColors(textColor = Color.White)
                )
                DropdownMenuItem(
                    text = { Text("United Kingdom") },
                    onClick = { /* Handle United Kingdom Filter */ },
                    colors = MenuDefaults.itemColors(textColor = Color.White)
                )
                DropdownMenuItem(
                    text = { Text("Other") },
                    onClick = { /* Handle Other Filter */ },
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
                    text = { Text("Squat") },
                    onClick = { /* Handle Squat Filter */ },
                    colors = MenuDefaults.itemColors(textColor = Color.White)
                )
                DropdownMenuItem(
                    text = { Text("Bench Press") },
                    onClick = { /* Handle Bench Press Filter */ },
                    colors = MenuDefaults.itemColors(textColor = Color.White)
                )
                DropdownMenuItem(
                    text = { Text("Deadlift") },
                    onClick = { /* Handle Deadlift Filter */ },
                    colors = MenuDefaults.itemColors(textColor = Color.White)
                )
            }
        }
    }
}

@Composable
fun ShowLeaderboard() {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            LeaderboardRow(LeaderBoardEntry(1, "User1", "deadlift", 500.5f, 1, true, 25, "CZ"))
        }
        item {
            LeaderboardRow(LeaderBoardEntry(2, "User2", "squat", 450.0f, 2, false, 30, "US"))
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
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                "${entry.rank}. ${entry.username}",
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                .padding(8.dp)
            )

            Text(
                "${entry.lift}. ${entry.weight} kg",
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                .padding(8.dp)
            )
        }
    }
}
