package com.sebastianfiser.fitnesscoach.models

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import io.appwrite.models.Document
import android.util.Log
import android.content.Context
import android.net.Uri
import java.time.LocalDate
import androidx.compose.material3.SnackbarHostState
import com.sebastianfiser.fitnesscoach.models.LeaderBoardEntry
import kotlinx.coroutines.launch
import androidx.lifecycle.viewModelScope
import com.sebastianfiser.fitnesscoach.models.PersistentData
import kotlinx.serialization.json.Json
import androidx.lifecycle.AndroidViewModel
import android.app.Application

class AppViewModel(application: Application) : AndroidViewModel(application) {
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
    var isReviewer by mutableStateOf(false)
    var pendingSubmissions by mutableStateOf<List<Document<Map<String, Any>>>>(emptyList())
    var approvedSubmissions by mutableStateOf<List<Document<Map<String, Any>>>>(emptyList())
    var userIconId by mutableStateOf<String?>(null)
    var userIconUri by mutableStateOf<android.net.Uri?>(null)
    var userIcon by mutableStateOf<ByteArray?>(null)

    var leaderboardList by mutableStateOf<List<LeaderBoardEntry>>(emptyList())
    var testResult by mutableStateOf<String?>(null)
    var accountDeleted by mutableStateOf(false)


    suspend fun checkReviewerStatus() {
        isReviewer = Appwrite.isReviewer()
    }

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
            var failCount = 0
            weekData.forEach { day ->
                day.exercises.forEach { exercise ->
                    repository.saveScheduleItem(
                        userId = userId,
                        day = dayAbbreviations[day.day] ?: day.day,
                        exerciseName = exercise.name,
                        sets = exercise.sets,
                        reps = exercise.reps,
                        weight = exercise.weight
                    ).onFailure { e ->
                        Log.d("AppViewModel", "Failed to seed schedule item: ${e.message}")
                        failCount++
                    }
                }
            }
            if (failCount > 0) {
                snackbarHostState.showSnackbar("Failed to seed $failCount schedule items, check your internet connection")
            }
            loadSchedule(userId)
        }
    }

    suspend fun saveSetupSchedule(userId: String) {
        var failSetupCount = 0
        scheduleSetup.forEach { (day, exercises) ->
            exercises.forEach { exercise ->
                repository.saveScheduleItem(
                    userId = userId,
                    day = day,
                    exerciseName = exercise.name,
                    sets = exercise.sets,
                    reps = exercise.reps,
                    weight = exercise.weight
                ).onFailure { e ->
                    failSetupCount++
                    Log.d("AppViewModel", "Failed to save setup schedule item: ${e.message}")
                }
            }
        }
        if (failSetupCount > 0) {
            snackbarHostState.showSnackbar("Failed to save $failSetupCount schedule items, check your internet connection")
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
        }.onFailure { e ->
            Log.d("AppViewModel", "Failed to load PR data: ${e.message}")
            snackbarHostState.showSnackbar("Failed to load PR data, check your internet connection")
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
        isReviewer = false
        userIcon = null
        userIconId = null
        userIconUri = null
    }

    suspend fun submitEntry(exerciseName: String, weight: Float, reps: Int, country: String?, isNatural: Boolean, age: String, gender: String, context: android.content.Context, uri: android.net.Uri, userId: String): Boolean {
        val uploadResult = repository.uploadVideo(context, uri, userId)
        val fileId = uploadResult.getOrNull()

        if (fileId == null) {
            val errorMessage = uploadResult.exceptionOrNull()?.message ?: "Unknown upload error"
            Log.d("AppViewModel", "Failed to upload video: $errorMessage")
            snackbarHostState.showSnackbar("Upload failed: $errorMessage")
            return false
        }

        val saveResult = repository.saveSubmission(userId, exerciseName, weight, reps, fileId, country, isNatural, age, gender)
        if (saveResult.isFailure) {
            val errorMessage = saveResult.exceptionOrNull()?.message ?: "Unknown save error"
            Log.d("AppViewModel", "Failed to save submission: $errorMessage")
            snackbarHostState.showSnackbar("Submission not saved: $errorMessage")
            return false
        }

        snackbarHostState.showSnackbar("Submission sent and waiting for review")
        return true
    }

    suspend fun loadPendingSubmissions() {
        repository.getPendingSubmissions()
            .onSuccess { submissions -> pendingSubmissions = submissions }
            .onFailure { e ->
                Log.d("AppViewModel", "Failed to load pending submissions: ${e.message}")
                snackbarHostState.showSnackbar("Failed to load pending submissions, check your internet connection")
            }
    }

    suspend fun updateSubmissionStatus(submissionId: String, newStatus: String) {
        repository.updateSubmissionStatus(submissionId, newStatus)
            .onSuccess { loadPendingSubmissions() }
            .onFailure { e ->
                Log.d("AppViewModel", "Failed to update submission status: ${e.message}")
                snackbarHostState.showSnackbar("Failed to update submission status, check your internet connection")
            }
    }

    suspend fun getApprovedSubmissions() {
        repository.getApprovedSubmissions()
            .onSuccess { approvedSubmissions = it }
            .onFailure { e ->
                Log.d("AppViewModel", "Failed to load approved submissions: ${e.message}")
                snackbarHostState.showSnackbar("Failed to load leaderboard, check your internet connection")
            }
    }

    val leaderboardEntries: List<LeaderBoardEntry>
        get() = approvedSubmissions.mapIndexed { index, document ->
            val data = document.data

            val rank = index + 1
            val username = data["userName"] as? String ?: "Unknown"
            val lift = data["exerciseName"] as? String ?: "Unknown"
            val weight = (data["weight"] as? Number)?.toFloat() ?: 0f
            val gender = when (data["gender"] as? String) {
                "Male" -> 1
                "Female" -> 2
                "Other" -> 3
                else -> 3
            }

            val ageInt = when (val ageString = data["ageGroup"] as? String) {
                "Under 18" -> 17
                "18-25" -> 21
                "26-35" -> 30
                "36-45" -> 40
                "46+" -> 50
                else -> 0
            }

            val natural = data["isNatural"] as? Boolean ?: false

            val nationality = data["country"] as? String ?: "Unknown"

            LeaderBoardEntry(
                rank = rank,
                username = username,
                lift = lift,
                weight = weight,
                gender = gender,
                natural = natural,
                age = ageInt,
                nationality = nationality
            )

        }

    suspend fun getVideoBytes(fileId: String): ByteArray? {
        return repository.getVideoBytes(fileId)
            .onFailure {
                snackbarHostState.showSnackbar("Failed to get video bytes, check your internet connection")
            }
            .getOrNull()
    }

    fun convertUnit( weight: Float, unit: String): Float {
        return if (unit == "kg") {
            weight
        } else {
            weight * 2.20462f
        }
    }

    fun updateUserSettingsAsync() {
        viewModelScope.launch {
            updateUserSettings()
        }
    }

    suspend fun updateUserSettings() {

        val persistentData = PersistentData(
            isDarkTheme = isDarkTheme,
            unit = unit,
            restTimeSeconds = restTime
        )

        val json = Json.encodeToString(PersistentData.serializer(), persistentData)

        getApplication<Application>().openFileOutput("settings.json", Context.MODE_PRIVATE).use { outputStream ->
            outputStream.write(json.toByteArray())
        }

        val userId = Appwrite.account.get().id
        repository.updateUserSettings(
            userId = userId,
            restTime = restTime,
            unit = unit,
            isDarkTheme = isDarkTheme ?: false,
            profileIconId = userIconId ?: ""
        )
        .onFailure { e ->
            Log.d("AppViewModel", "Failed to update user settings: ${e.message}")
            snackbarHostState.showSnackbar("Failed to update user settings, check your internet connection DEBUG: ${e.message}")
        }



    }

    suspend fun uploadImage(context: Context, uri: Uri, userId: String): Result<String> {
        val result = repository.uploadImage(context, uri, userId)
        if (result.isSuccess) {
            val fileId = result.getOrNull()
            if (fileId != null) {
                userIconId = fileId
                updateUserSettings()
            }
        } else {
            val e = result.exceptionOrNull()
            Log.d("AppViewModel", "Failed to upload image: ${e?.message}")
            snackbarHostState.showSnackbar("Failed to upload image, check your internet connection DEBUG: ${e?.message}")
        }

        return result
    }

    suspend fun getUserSettings(userId: String) {



        repository.getUserSettings(userId)
            .onSuccess { document ->
                restTime = (document.data["restTime"] as? Number)?.toInt() ?: 90
                unit = document.data["unit"] as? String ?: "kg"
                isDarkTheme = document.data["isDarkTheme"] as? Boolean ?: false
                userIconId = document.data["profileIconId"] as? String
                if (!userIconId.isNullOrEmpty()) {
                    getImage(userIconId!!)
                }
            }
            .onFailure { e ->
                Log.d("AppViewModel", "Failed to get user settings: ${e.message}")
                snackbarHostState.showSnackbar("Failed to get user settings, check your internet connection DEBUG: ${e.message}")
            }
    }

    suspend fun getImage(fileId: String) {
        repository.getImage(fileId)
            .onSuccess { bytes ->
                userIcon = bytes
            }
            .onFailure { e ->
                Log.d("AppViewModel", "Failed to get image: ${e.message}")
                snackbarHostState.showSnackbar("Failed to get image, check your internet connection: DEBUG: ${e.message}")
            }
    }

    suspend fun createUserSettings(userId: String) {
        repository.createUserSettings(userId)
            .onFailure { e ->
                Log.d("AppViewModel", "Failed to create user settings: ${e.message}")
                snackbarHostState.showSnackbar("Failed to create user settings, check your internet connection DEBUG: ${e.message}")
            }
    }

    fun deleteAccount() {
        viewModelScope.launch {
            try {
                val execution = Appwrite.deleteAccount()
                if(execution.responseStatusCode == 200L) {
                    clearUserState()
                    snackbarHostState.showSnackbar("Account deleted successfully")
                    accountDeleted = true
                } else {
                    Log.d("AppViewModel", "Failed to delete account, status code: ${execution.responseStatusCode}")
                    snackbarHostState.showSnackbar("Failed to delete account, error code ${execution.responseStatusCode}")
                }
            } catch (e: Exception) {
                Log.d("AppViewModel", "Failed to execute delete account function: ${e.message}")
                snackbarHostState.showSnackbar("Failed to delete account error code ${e.message}")
            }
        }
    }

}
