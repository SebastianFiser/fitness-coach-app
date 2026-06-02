package com.sebastianfiser.fitnesscoach.models

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import io.appwrite.models.Document
import android.util.Log

class AppViewModel : ViewModel() {
    private val repository = WorkoutRepository(Appwrite.client)
    var workouts by mutableStateOf<List<Document>>(emptyList())
    var restTime by mutableStateOf(90)
    var unit by mutableStateOf("kg")
    var isDarkTheme by mutableStateOf<Boolean?>(false)

    suspend fun loadWorkouts(userId: String) {
        repository.getWorkouts(userId)
            .onSuccess { workouts -> this.workouts = workouts}
            .onFailure { e -> Log.d("AppViewModel", "Failed to load workouts: ${e.message}") }
    }

    suspend fun saveWorkout(userId: String, date: String) {
        repository.saveWorkout(userId, date)
            .onSuccess { loadWorkouts(userId) }
            .onFailure { e -> Log.d("AppViewModel", "Failed to save workout: ${e.message}") }
    }

    suspend fun saveSet(workoutId: String, userId: String, exerciseName: String, weight: Float, reps: Int) {
        repository.saveSet(workoutId, userId, exerciseName, weight, reps)
            .onFailure { e -> Log.d("AppViewModel", "Failed to save set: ${e.message}") }
    }
}
