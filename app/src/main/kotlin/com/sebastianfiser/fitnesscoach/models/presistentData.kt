package com.sebastianfiser.fitnesscoach.models
import kotlinx.serialization.Serializable

@Serializable
data class PresistentData(
    var isDarkTheme: Boolean,
    var unit: String,
    var restTimeSeconds: Int
)
