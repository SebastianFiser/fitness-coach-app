package com.sebastianfiser.fitnesscoach.models

data class Exercise(
    val name: String,
    val weight: Float,
    val sets: Int,
    val reps: Int,
    var isDone: Boolean = false,
    var isSkipped: Boolean = false
)

data class Day(val day: String, val exercises: List<Exercise>)

data class SetEntry (
    val weight: String,
    val reps: String,
    var isDone: Boolean = false
)

data class ExerciseStat(
    val name: String,
    val pr: Float,
    val weeklyGain: Float
)
