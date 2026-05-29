package com.sebastianfiser.fitnesscoach.models

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

class AppViewModel : ViewModel() {
    var restTime by mutableStateOf(90)
    var unit by mutableStateOf("kg")
    var isDarkTheme by mutableStateOf(false)
}
