package com.sebastianfiser.fitnesscoach.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Overview : Screen("overview")
    object Workout : Screen("workout")
    object Schedule : Screen("schedule")
    object Leaderboard : Screen("leaderboard")
    object Profile : Screen("profile")
    object Settings : Screen("settings")
    object Stats : Screen("stats")
    object SetupSchedule : Screen("setup_schedule")
    object ExercisePick : Screen("exercise_pick")
    object Submission : Screen("submission")
    object Reviewer : Screen("reviewer")
    object Edit : Screen("edit")
}
