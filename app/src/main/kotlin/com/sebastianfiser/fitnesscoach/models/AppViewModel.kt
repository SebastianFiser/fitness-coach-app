package com.sebastianfiser.fitnesscoach.models

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import io.appwrite.models.Document
import android.util.Log
import java.time.LocalDate
import androidx.compose.material3.SnackbarHostState

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
    val scheduleByDayExercises: Map<String, List<Exercise>>
        get() = schedule.groupBy { it.data["day"] as String }.mapValues { entry ->
            entry.value.map { doc ->
                Exercise(
                    name = doc.data["exerciseName"] as String,
                    sets = (doc.data["sets"] as Number).toInt(),
                    reps = (doc.data["reps"] as Number).toInt(),
                    weight = when (val w = doc.data["weight"]) {
                        is Double -> w.toFloat()
                        is Float -> w
                        is Long -> w.toFloat()
                        else -> 0f
                    }
                )
            }
        }
    var selectedDay by mutableStateOf<String?>(null)
    var scheduleSetup = mutableStateMapOf<String, MutableList<Exercise>>()
    var isEditing by mutableStateOf(false)
    var scheduleSetupLoaded by mutableStateOf(false)
    var prData by mutableStateOf<Map<String, Float>>(emptyMap())
    val snackbarHostState = SnackbarHostState()

    suspend fun loadWorkouts(userId: String) {
        repository.getWorkouts(userId)
            .onSuccess { workouts -> this.workouts = workouts}
            .onFailure { e -> 
                Log.d("AppViewModel", "Failed to load workouts: ${e.message}")
                snackbarHostState.showSnackbar("Failed to load workouts, check your internet connection")
            }
    }

    suspend fun saveWorkout(userId: String, date: String) {
        repository.saveWorkout(userId, date)
            .onSuccess { loadWorkouts(userId) }
            .onFailure { e -> 
                Log.d("AppViewModel", "Failed to save workout: ${e.message}")
                snackbarHostState.showSnackbar("Failed to save workout, check your internet connection")
            }
    }

    suspend fun saveSet(workoutId: String, userId: String, exerciseName: String, weight: Float, reps: Int) {
        repository.saveSet(workoutId, userId, exerciseName, weight, reps)
            .onFailure { e -> 
                Log.d("AppViewModel", "Failed to save set: ${e.message}")
                snackbarHostState.showSnackbar("Failed to save set, check your internet connection")
            }
    }

    suspend fun createWorkout(): String? {
        val userId = Appwrite.account.get().id
        val date = LocalDate.now().toString()
        return repository.saveWorkout(userId, date)
            .onSuccess { loadWorkouts(userId) }
            .onFailure { e -> 
                Log.d("AppViewModel", "Failed to create workout: ${e.message}")
                snackbarHostState.showSnackbar("Failed to create workout, check your internet connection")
            }
            .getOrNull()?.id
    }

    suspend fun loadSchedule(userId: String) {
        repository.getSchedule(userId)
            .onSuccess { schedule -> this.schedule = schedule }
            .onFailure { e -> 
                Log.d("AppViewModel", "Failed to load schedule: ${e.message}")
                snackbarHostState.showSnackbar("Failed to load schedule, check your internet connection")
            }
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
            loadSchedule(userId)
        }
    }

    suspend fun saveSetupSchedule(userId: String) {
        scheduleSetup.forEach { (day, exercises) ->
            exercises.forEach { exercise ->
                repository.saveScheduleItem(
                    userId = userId,
                    day = day,
                    exerciseName = exercise.name,
                    sets = exercise.sets,
                    reps = exercise.reps,
                    weight = exercise.weight
                )
            }
        }
    }
    
    suspend fun deleteAllSchedule(userId: String) {
        schedule.forEach { doc ->
            repository.deleteScheduleItem(doc.id, userId)
                .onFailure { e -> 
                    Log.d("AppViewModel", "Failed to delete schedule item: ${e.message}")
                    snackbarHostState.showSnackbar("Failed to delete schedule item, check your internet connection")
                }
        }
        schedule = emptyList()
    }

    suspend fun loadPrData(userId: String) {
        val result = repository.getSetByUser(userId)
        result.onSuccess { docs ->
            prData = docs
                .groupBy { it.data["exerciseName"] as String }
                .mapValues { (_, sets) ->
                    sets.maxOf { set ->
                        when (val w = set.data["weight"]) {
                            is Double -> w.toFloat()
                            is Float -> w
                            is Long -> w.toFloat()
                            else -> 0f
                        }
                    }
                }
        }
    }

    fun clearUserState() {
        workouts = emptyList()
        schedule = emptyList()
        restTime = 90
        unit = "kg"
        isDarkTheme = false
        selectedDay = null
        scheduleSetup = mutableStateMapOf()
        isEditing = false
        scheduleSetupLoaded = false
        prData = emptyMap()
    }

    suspend fun submitEntry(exerciseName: String, weight: Float, reps: Int,createdAt: String, country: String, isNatural: Boolean, age: Int, gender: String, context: android.content.Context, uri: android.net.Uri, userId: String) {
        repository.uploadVideo(context, uri, userId)
            .onSuccess { fileId ->
            repository.saveSubmission(userId, exerciseName, weight, reps, fileId, createdAt, country, isNatural, age, gender)
                .onFailure { e ->
                    Log.d("AppViewModel", "Failed to save submission: ${e.message}")
                    snackbarHostState.showSnackbar("Failed to save submission, check your internet connection")
                }
            }
            .onFailure { e ->
                Log.d("AppViewModel", "Failed to upload video: ${e.message}")
                snackbarHostState.showSnackbar("Failed to upload video, check your internet connection")
            }
    }
}
