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


// 1. DATA MODEL (Co je to za data)
data class Exercise(val name: String, val isDone: Boolean)

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
    var selectedTab by remember { mutableStateOf(0)}
    // Testovací data pro tvůj plán
    val exercises = listOf(
        Exercise("Bench press 12x4", false),
        Exercise("Squats 20x3", true),
        Exercise("Deadlift 10x4", false)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Hlavní obsah obrazovky
        when(selectedTab) {
            0 -> MainWorkoutCard(exercises)
            1 -> schduleScreen()
            2 -> LeaderboardScreen()
            3 -> ProfileScreen()
        }
        // SPODNÍ NAVIGACE
        BottomNav(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
            selectedTab = selectedTab,
            onTabSelected = { selectedTab = it }
        )
    }
}

@Composable
fun MainWorkoutCard(exercises: List<Exercise>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 80.dp)
    ) {
        Spacer(modifier = Modifier.height(30.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 15.dp, bottom = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Good Morning, User!",
                color = Color.White,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Text(
                "Here's your workout plan for today",
                color = Color.LightGray,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Canvas(
                modifier = Modifier
                    .size(200.dp)
                    .padding(20.dp)
            ) {
                drawArc(
                    color = Color.DarkGray,
                    startAngle = 135f,
                    sweepAngle = 265f,
                    useCenter = false,
                    style = Stroke(width = 10.dp.toPx(), cap = StrokeCap.Round)
                )
                drawArc(
                    color = Color.White,
                    startAngle = 135f,
                    sweepAngle = 113f,
                    useCenter = false,
                    style = Stroke(width = 10.dp.toPx(), cap = StrokeCap.Round)
                )
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .offset(y = (-10).dp)
                ) {
                Text(
                    "760",
                    color = Color.Gray,
                    style = MaterialTheme.typography.headlineLarge
                )
                Text(
                    "Day Score",
                    color = Color.White,
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1C1C1E)),
            shape = RoundedCornerShape(28.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            "Workout Plan",
                            color = Color.White,
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(
                            "8:00 AM - 9:00 AM",
                            color = Color.Gray,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    ElevatedButton(onClick = { }) {
                            Text("Start")
                        }
                }

                Spacer(modifier = Modifier.height(20.dp))

                exercises.forEach { exercise ->
                    ExerciseRow(exercise)
                }
            }
        }
    }
}

@Composable
fun ExerciseRow(exercise: Exercise) {
    HorizontalDivider(color = Color.Gray, thickness = 0.5.dp, modifier = Modifier.padding(vertical = 8.dp))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = exercise.name, color = Color.White)
        
        Checkbox(
            checked = exercise.isDone,
            onCheckedChange = null, // Tady by v budoucnu byla logika kliknutí
            colors = CheckboxDefaults.colors(
                checkedColor = Color.White,
                uncheckedColor = Color.Gray,
                checkmarkColor = Color.Black
            )
        )
    }
}

@Composable
fun schduleScreen() {
    Column(
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
                "Your Schedule",
                color = Color.White,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }

        DrawDayrow()
        displayDaySchedule()

    }
}

@Composable
fun DrawDayrow() {
    val dayToday = LocalDate.now().dayOfWeek
    val Locale = java.util.Locale.getDefault()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val days = mapOf(
            "Mon" to "Monday",
            "Tue" to "Tuesday",
            "Wed" to "Wednesday",
            "Thu" to "Thursday",
            "Fri" to "Friday",
            "Sat" to "Saturday",
            "Sun" to "Sunday")

            days.forEach { (shortName, fullName) ->
                val isToday = fullName.uppercase(Locale) == dayToday.name
                val color = if (isToday) Color.White else Color.DarkGray
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        shortName,
                        color = color,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(color)
                    )
                }
            }
    }
}

@Composable
fun displayDaySchedule(exercise: List<Exercise> = listOf(
    Exercise("Bench press 12x4", false),
    Exercise("Squats 20x3", true),
    Exercise("Deadlift 10x4", false)
)) {
    var day = LocalDate.now().dayOfWeek
    var dayNuminMonth = LocalDate.now().dayOfMonth
    var month = LocalDate.now().monthValue 
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1C1C1E)),
        shape = RoundedCornerShape(28.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text (
                "$day, $dayNuminMonth.$month.",
                color = Color.White,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            exercise.forEach { exercise ->
                    ExerciseRow(exercise)
                }
        }
    }
}

@Composable
fun BottomNav(modifier: Modifier = Modifier, selectedTab : Int, onTabSelected: (Int) -> Unit) {
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
            selected = selectedTab == 0,
            onClick = { onTabSelected(0) },
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
            selected = selectedTab == 1,
            onClick = { onTabSelected(1) },
            colors = colors
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Star, contentDescription = null) },
            label = { Text("Leaderboard") },
            selected = selectedTab == 2,
            onClick = { onTabSelected(2) },
            colors = colors
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Person, contentDescription = null) },
            label = { Text("Profile") },
            selected = selectedTab == 3,
            onClick = { onTabSelected(3) },
            colors = colors
        )
    }
}

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
            Spacer(modifier = Modifier.height(16.dp))

            ShowLeaderboard()
        }
    }
}

data class LeaderBoardEntry(
    var rank: Int,
    var username: String,
    var Lift: String,
    var Weight: Float,
)

@Composable
fun ShowLeaderboard() {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            LeaderboardRow(LeaderBoardEntry(1, "User1", "deadlift", 500.5f))
        }
        item {
            LeaderboardRow(LeaderBoardEntry(2, "User2", "squat", 450.0f))
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
                "${entry.Lift}. ${entry.Weight} kg",
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                .padding(8.dp)
            )
        }
    }
}

@Composable
fun navItemColors() = NavigationBarItemDefaults.colors(
    selectedIconColor = Color.White,
    unselectedIconColor = Color.Gray,
    selectedTextColor = Color.White,
    unselectedTextColor = Color.Gray,
    indicatorColor = Color.DarkGray
)

@Composable
fun ProfileScreen() {
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
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Settings, contentDescription = null, tint = Color.White)
                Text(
                    "Settings",
                    color = Color.White,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(8.dp)
                )
            }
            HorizontalDivider(color = Color.Gray, thickness = 0.5.dp, modifier = Modifier.padding(vertical = 16.dp))
             Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Person, contentDescription = null, tint = Color.White)
                Text(
                    "Profile",
                    color = Color.White,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(8.dp)
                )
            }
            HorizontalDivider(color = Color.Gray, thickness = 0.5.dp, modifier = Modifier.padding(vertical = 16.dp))
             Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.ExitToApp, contentDescription = null, tint = Color.White)
                Text(
                    "Logout",
                    color = Color.White,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}
