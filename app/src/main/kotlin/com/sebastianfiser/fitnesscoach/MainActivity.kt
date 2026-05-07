package com.sebastianfiser.fitnesscoach

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.Column

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Toto zajistí, že aplikace jde až pod stavový řádek (Edge-to-Edge)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            StartContent()
        }
    }
}

@Composable
fun StartContent() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Voláme navigaci a zarovnáme ji dospod
        BottomNav(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        )

        BuildOverviewScreen(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(bottom = 56.dp) // Přidáme padding, aby obsah nebyl pod navigací
        )
    }
}

@Composable
fun BottomNav(modifier: Modifier = Modifier) {
    NavigationBar(
        modifier = modifier.drawBehind {
            // Vykreslíme šedou linku pouze na horní hranu (Y = 0)
            val strokeWidth = 0.5.dp.toPx()
            drawLine(
                color = Color.Gray,
                start = Offset(0f, 0f),
                end = Offset(size.width, 0f),
                strokeWidth = strokeWidth
            )
        },
        containerColor = Color.Black,
        tonalElevation = 0.dp,
        // windowInsets zajistí, že navigace nebude "nalepená" úplně na spodní hraně displeje
        windowInsets = NavigationBarDefaults.windowInsets 
    ) {
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
                indicatorColor = Color.DarkGray // Barva "kolečka" kolem vybrané ikony
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.DateRange, contentDescription = null) },
            label = { Text("Schedule") },
            selected = false,
            onClick = {},
            colors = navigationBarColors()
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Star, contentDescription = null) },
            label = { Text("Leaderboard") },
            selected = false,
            onClick = {},
            colors = navigationBarColors()
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Person, contentDescription = null) },
            label = { Text("Profile") },
            selected = false,
            onClick = {},
            colors = navigationBarColors()
        )
    }
}

// Pomocná funkce pro barvy, abys nemusel ten dlouhý blok kopírovat u každé položky
@Composable
fun navigationBarColors() = NavigationBarItemDefaults.colors(
    selectedIconColor = Color.White,
    unselectedIconColor = Color.Gray,
    selectedTextColor = Color.White,
    unselectedTextColor = Color.Gray,
    indicatorColor = Color.Transparent
)

@Composable
fun BuildOverviewScreen() {
    Column {
        val Dayscore = 1100
        Box(
            modifier = Modifier
                .size(150.dp)
                .clip(CircleShape)
                .background(Color.Red)
        ) {
           Text("dayscore: ${Dayscore}", color = Color.White, modifier = Modifier.align(Alignment.Center))
        }
        LazyColumn {
            item {
                Text(text = "Bench press 12x4")
            }
            item {
                Text(text = "Squats 20x3")
            }
            item {
                Text(text = "Deadlift 10x4")
            }
        }
    }
}
