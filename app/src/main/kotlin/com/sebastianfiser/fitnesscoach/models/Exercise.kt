package com.sebastianfiser.fitnesscoach.models

data class Exercise(val name: String, val weight: Float, var isDone: Boolean = false)

data class Day(val day: String, val exercises: List<Exercise>)