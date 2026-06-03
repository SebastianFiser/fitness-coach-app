package com.sebastianfiser.fitnesscoach.models

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import io.appwrite.models.Document
import android.util.Log
import java.time.LocalDate

class AppViewModel : ViewModel() {
    private val repository = WorkoutRepository(Appwrite.client)
    var workouts by mutableStateOf<List<Document<Map<String, Any>>>>(
        emptyList()
    )
    var schedule by mutableStateOf<List<Document<Map<String, Any>>>>(
        emptyList()
    )
    var restTime by mutableStateOf(90)
    var unit by mutableStateOf("kg")
    var isDarkTheme by mutableStateOf<Boolean?>(false)
    val scheduleByDay: Map<String, List<Document<Map<String, Any>>>>
        get() = schedule.groupBy { it.data["day"] as String }

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

    suspend fun createWorkout(): String? {
        val userId = Appwrite.account.get().id
        val date = LocalDate.now().toString()
        return repository.saveWorkout(userId, date)
            .onSuccess { loadWorkouts(userId) }
            .onFailure { e -> Log.d("AppViewModel", "Failed to create workout: ${e.message}") }
            .getOrNull()?.id
    }

    suspend fun loadSchedule(userId: String) {
        repository.getSchedule(userId)
            .onSuccess { schedule -> this.schedule = schedule }
            .onFailure { e -> Log.d("AppViewModel", "Failed to load schedule: ${e.message}") }
    }

    suspend fun seedSchedule(userId: String) {
        val dayAbbreviations = mapOf(
            "Monday" to "Mo",
            "Tuesday" to "Tu",
            "Wednesday" to "We",
            "Thursday" to "Th",
            "Friday" to "Fr",
            "Saturday" to "Sa",
            "Sunday" to "Su"
        )
        loadSchedule(userId)
        if (schedule.isEmpty()) {
            weekData.forEach { day ->
                day.exercises.forEach { exercise ->
                    repository.saveScheduleItem(
                        userId = userId,
                        day = dayAbbreviations[day.day] ?: day.day,
                        exerciseName = exercise.name,
                        sets = exercise.sets,
                        reps = exercise.reps,
                        weight = exercise.weight
                    )
                }
            }
        }
    }


}
