package com.sebastianfiser.fitnesscoach.models
import kotlinx.serialization.Serializable

@Serializable
data class PersistentData(
    var isDarkTheme: Boolean? = null,
    var unit: String = "kg",
    var restTimeSeconds: Int = 90
)
