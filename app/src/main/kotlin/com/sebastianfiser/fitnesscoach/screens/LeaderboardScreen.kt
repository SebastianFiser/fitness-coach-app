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

    Box{
        Button(
            onClick = { filterOpen = !filterOpen }
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1C1C1E), contentColor = Color.White)
            ) {
            Text("Filter")
        }
        DropdownMenu(
            expanded = filterOpen,
            onDismissRequest = { filterOpen = false },
            modifier = Modifier.background(Color(0xFF1C1C1E))
        ) { 
            DropdownMenuItem(
                text = { Text("Gender") },
                onClick = {/*Handle, showing another dropdown which by selecting THEN closes the main and subsequent dropdown*/},
                colors = MenuDefaults.itemColors(contentColor = Color.White)
            )
            DropdownMenuItem(
                text = { Text("Age Group") },
                onClick = {/*Handle, showing another dropdown which by selecting THEN closes the main and subsequent dropdown*/},
                colors = MenuDefaults.itemColors(contentColor = Color.White)
            )
            DropdownMenuItem(
                text = { Text("Natural/Enhanced") },
                onClick = {/*Handle, showing another dropdown which by selecting THEN closes the main and subsequent dropdown*/},
                colors = MenuDefaults.itemColors(contentColor = Color.White)
            )
            DropdownMenuItem(
                text = { Text("Nationality") },
                onClick = {/*Handle, showing another dropdown which by selecting THEN closes the main and subsequent dropdown*/},
                colors = MenuDefaults.itemColors(contentColor = Color.White)
            )
            DropdownMenuItem(
                text = { Text("Lift") },
                onClick = {/*Handle, showing another dropdown which by selecting THEN closes the main and subsequent dropdown*/},
                colors = MenuDefaults.itemColors(contentColor = Color.White)
            )

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
