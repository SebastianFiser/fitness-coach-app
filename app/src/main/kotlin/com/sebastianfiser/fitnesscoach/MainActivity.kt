package com.sebastianfiser.fitnesscoach

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.compose.foundation.Canvas
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import java.time.LocalDate
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Settings

import com.sebastianfiser.fitnesscoach.screens.MainWorkoutCard
import com.sebastianfiser.fitnesscoach.screens.schduleScreen
import com.sebastianfiser.fitnesscoach.screens.LeaderboardScreen
import com.sebastianfiser.fitnesscoach.screens.ProfileScreen
import com.sebastianfiser.fitnesscoach.models.Exercise
import com.sebastianfiser.fitnesscoach.screens.WorkoutScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sebastianfiser.fitnesscoach.navigation.Screen
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Edge-to-edge režim (pod hodiny a navigaci)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            StartContent()
        }
    }
}

@Composable
fun StartContent() {
    var isWorkoutDone by remember {mutableStateOf(false)}
    val navController = rememberNavController()
    val currentRoute= navController.currentBackStackEntryAsState().value?.destination?.route
    Scaffold(
        containerColor = Color.Black,
        bottomBar = {
            if (currentRoute != Screen.Workout.route) {
                BottomNav(navController = navController)
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screen.Overview.route,
        ) {
            composable(Screen.Overview.route) { MainWorkoutCard(onStartWorkout = { navController.navigate(Screen.Workout.route) }, isWorkoutDone = isWorkoutDone) }
            composable(Screen.Workout.route) { WorkoutScreen(onFinish = { isWorkoutDone = true; navController.popBackStack() }), navController = navController }
            composable(Screen.Schedule.route) { schduleScreen() }
            composable(Screen.Leaderboard.route) { LeaderboardScreen() }
            composable(Screen.Profile.route) { ProfileScreen() }
        }

    }
}

@Composable
fun BottomNav(modifier: Modifier = Modifier, navController: NavController) {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    NavigationBar(
        modifier = modifier.drawBehind {
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
        windowInsets = NavigationBarDefaults.windowInsets 
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = null) },
            label = { Text("Overview") },
            selected = currentRoute == Screen.Overview.route,
            onClick = { navController.navigate(Screen.Overview.route) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                unselectedIconColor = Color.Gray,
                selectedTextColor = Color.White,
                unselectedTextColor = Color.Gray,
                indicatorColor = Color.DarkGray
            )
        )
        // Pomocná funkce pro barvy položek (vytvořená dole)
        val colors = navItemColors()
        
        NavigationBarItem(
            icon = { Icon(Icons.Default.DateRange, contentDescription = null) },
            label = { Text("Schedule") },
            selected = currentRoute == Screen.Schedule.route,
            onClick = { navController.navigate(Screen.Schedule.route) },
            colors = colors
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Star, contentDescription = null) },
            label = { Text("Leaderboard") },
            selected = currentRoute == Screen.Leaderboard.route,
            onClick = { navController.navigate(Screen.Leaderboard.route) },
            colors = colors
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Person, contentDescription = null) },
            label = { Text("Profile") },
            selected = currentRoute == Screen.Profile.route,
            onClick = { navController.navigate(Screen.Profile.route) },
            colors = colors
        )
    }
}

data class LeaderBoardEntry(
    var rank: Int,
    var username: String,
    var Lift: String,
    var Weight: Float,
)

@Composable
fun navItemColors() = NavigationBarItemDefaults.colors(
    selectedIconColor = Color.White,
    unselectedIconColor = Color.Gray,
    selectedTextColor = Color.White,
    unselectedTextColor = Color.Gray,
    indicatorColor = Color.DarkGray
)
