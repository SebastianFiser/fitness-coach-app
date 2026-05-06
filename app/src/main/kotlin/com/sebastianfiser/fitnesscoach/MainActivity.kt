package com.sebastianfiser.fitnesscoach

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.material3.Text
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.Alignment
import androidx.compose.foundation.border
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
           StartContent()
         }
    }
}

@Composable
fun StartContent() {
    Box (
        modifier = Modifier
        .fillMaxSize()
        .background(Color.Black)
        
    ) {
        BottomNav(modifier = Modifier
        .align(Alignment.BottomCenter)
        .border(1.dp, Color.Gray))
    }
}

@Composable
fun BottomNav(modifier: Modifier = Modifier) {
    NavigationBar(modifier = modifier) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = null) },
            label = { Text("Overview") },
            selected = true,
            onClick = {},
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                unselectedIconColor = Color.Gray,
                selectedTextColor = Color.White,
                unselectedTextColor = Color.Gray,
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.DateRange, contentDescription = null) },
            label = { Text("schedule") },
            selected = false,
            onClick = {},
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                unselectedIconColor = Color.Gray,
                selectedTextColor = Color.White,
                unselectedTextColor = Color.Gray,
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Star, contentDescription = null) },
            label = { Text("leaderboard") },
            selected = false,
            onClick = {},
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                unselectedIconColor = Color.Gray,
                selectedTextColor = Color.White,
                unselectedTextColor = Color.Gray,
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Person, contentDescription = null) },
            label = { Text("profile") },
            selected = false,
            onClick = {},
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                unselectedIconColor = Color.Gray,
                selectedTextColor = Color.White,
                unselectedTextColor = Color.Gray,
            )
        )
    }
}