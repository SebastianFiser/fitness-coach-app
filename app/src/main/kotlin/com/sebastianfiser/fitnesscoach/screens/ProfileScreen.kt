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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.ui.draw.clip
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

@Composable
fun ProfileScreen() {
    var selectedTab by remember { mutableStateOf(0) }
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
                "Profile",
                color = Color.White,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }
        Column {
            HorizontalDivider(color = Color.Gray, thickness = 0.5.dp, modifier = Modifier.padding(vertical = 16.dp))
            TabRow(selectedTabIndex = selectedTab, backgroundColor = Color.Transparent, contentColor = Color.White) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("Settings") },
                    icon = { Icon(Icons.Default.Settings, contentDescription = null, tint = Color.White) }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("Profile") },
                    icon = { Icon(Icons.Default.Person, contentDescription = null, tint = Color.White)  }
                )
                Tab(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    text = { Text("Logout") },
                    icon = { Icon(Icons.Default.ExitToApp, contentDescription = null, tint = Color.White) }
                )
            }
            when(selectedTab) {
                0 -> SettingsScreen()
                1 -> ProfileDetailsScreen()
                2 -> LogoutScreen()
            }            
        }
    }
}

@Composable
fun LogoutScreen() {
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
                    "Logout Options",
                    color = Color.White,
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        Column {
            HorizontalDivider(color = Color.Gray, thickness = 0.5.dp, modifier = Modifier.padding(vertical = 16.dp))
            Row (
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Logout", color = Color.White)
            }
            HorizontalDivider(color = Color.Gray, thickness = 0.5.dp, modifier = Modifier.padding(vertical = 16.dp))
            Row (
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Switch Account", color = Color.White)
            }
            HorizontalDivider(color = Color.Gray, thickness = 0.5.dp, modifier = Modifier.padding(vertical = 16.dp))
            Row (
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Add Account", color = Color.White)
            }
            HorizontalDivider(color = Color.Gray, thickness = 0.5.dp, modifier = Modifier.padding(vertical = 16.dp))
            Row (
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Add Account", color = Color.White)
            }
            HorizontalDivider(color = Color.Gray, thickness = 0.5.dp, modifier = Modifier.padding(vertical = 16.dp))
            Row (
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Delete Account", color = Color.White)
            }
        }
    }
}