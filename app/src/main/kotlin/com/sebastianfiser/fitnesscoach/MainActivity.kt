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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 80.dp) // Prostor pro BottomNav
        ) {
            Spacer(modifier = Modifier.height(60.dp)) // Mezera shora (pod status barem)
            
            // VOLÁME HLAVNÍ KARTU
            MainWorkoutCard(exercises = exercises)
        }

        // SPODNÍ NAVIGACE
        BottomNav(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        )
    }
}

@Composable
fun MainWorkoutCard(exercises: List<Exercise>) {
    Column {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 15.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            
            Box(
                modifier = Modifier,
                contentAlignment = Alignment.Center
            ) {
                Canvas(
                    modifier = Modifier
                        .size(200.dp)
                        .padding(20.dp)
                ) {
                    drawArc(
                        color = Color.DarkGray,
                        startAngle = 180f,
                        sweepAngle = 180f,
                        useCenter = false,
                        style = Stroke(width = 10.dp.toPx(), cap = StrokeCap.Round)
                    )

                    drawArc(
                        color = Color.White,
                        startAngle = 180f,
                        sweepAngle = 90f, // 50% dokončeno
                        useCenter = false,
                        style = Stroke(width = 10.dp.toPx(), cap = StrokeCap.Round)
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
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
        }
        Column{
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1C1C1E)),
                shape = RoundedCornerShape(28.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp),) {
                    // HLAVIČKA KARTY (Nadpis a Score)
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
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // SEZNAM CVIKŮ UVNITŘ KARTY
                    exercises.forEach { exercise ->
                        ExerciseRow(exercise)
                    }
                }
            }
        }
    }
}

@Composable
fun ExerciseRow(exercise: Exercise) {
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
fun BottomNav(modifier: Modifier = Modifier) {
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
            selected = true,
            onClick = {},
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
            selected = false,
            onClick = {},
            colors = colors
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Star, contentDescription = null) },
            label = { Text("Leaderboard") },
            selected = false,
            onClick = {},
            colors = colors
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Person, contentDescription = null) },
            label = { Text("Profile") },
            selected = false,
            onClick = {},
            colors = colors
        )
    }
}

@Composable
fun navItemColors() = NavigationBarItemDefaults.colors(
    selectedIconColor = Color.White,
    unselectedIconColor = Color.Gray,
    selectedTextColor = Color.White,
    unselectedTextColor = Color.Gray,
    indicatorColor = Color.Transparent
)
