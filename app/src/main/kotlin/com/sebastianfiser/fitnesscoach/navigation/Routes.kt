package com.sebastianfiser.fitnesscoach.navigation

sealed class Screen(val route: String) {
    object Overview : Screen("overview")
    object Workout : Screen("workout")
    object Schedule : Screen("schedule")
    object Leaderboard : Screen("leaderboard")
    object Profile : Screen("profile")
    object Settings : Screen("settings")
    object Stats : Screen("stats")
}