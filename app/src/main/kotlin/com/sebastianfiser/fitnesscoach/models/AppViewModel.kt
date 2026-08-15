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
import java.io.File
import android.graphics.Bitmap
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.sebastianfiser.fitnesscoach.models.ProfileImageState
import com.sebastianfiser.fitnesscoach.models.ScheduleDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import com.sebastianfiser.fitnesscoach.models.SyncState
import com.sebastianfiser.fitnesscoach.models.ScheduleEntity

sealed interface ProfileImageState {
    object Loading: ProfileImageState
    data class Success(val bitmap: Bitmap): ProfileImageState
    data class Error(val message: String): ProfileImageState
}

sealed class SyncState {
    object Idle : SyncState()
    object Syncing : SyncState()
    object Synced : SyncState()
    data class Error(val message: String) : SyncState()
}

data class LoginUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
)

class AppViewModel(application: Application, private val scheduleDao: ScheduleDao) : AndroidViewModel(application) {
    private val _syncState = MutableStateFlow<SyncState>(SyncState.Idle)
    val syncState: StateFlow<SyncState> = _syncState.asStateFlow()
    private val imageCacheManager = ImageCacheManager(application.applicationContext)
    private val repository = WorkoutRepository(Appwrite.client, imageCacheManager, scheduleDao)
    private val _loginUiState = MutableStateFlow(LoginUiState())
    val loginUiState: StateFlow<LoginUiState> = _loginUiState.asStateFlow()
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

    fun saveSetupSchedule(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val newEntities = mutableListOf<ScheduleEntity>()
            scheduleSetup.forEach { (dayKey, exercises) ->
                exercises.forEach { ex ->
                    newEntities.add(
                        ScheduleEntity(
                            id = java.util.UUID.randomUUID().toString(),
                            userId = userId,
                            day = dayKey,
                            exerciseName = ex.name,
                            sets = ex.sets,
                            reps = ex.reps,
                            weight = ex.weight,
                            isDirty = true
                        )
                    )
                }
            }

            scheduleDao.replaceUserSchedule(userId, newEntities)

            scheduleSetup.clear()
            scheduleSetupLoaded = false

            repository.syncSchedule(userId)
        }
    }

    fun getScheduleState(userId: String): StateFlow<List<ScheduleEntity>> {
        return repository.getScheduleFlow(userId)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )
    }

    fun syncSchedule(userId: String) {
        viewModelScope.launch {
            _syncState.value = SyncState.Loading
            try {
                repository.syncState(userId)
                _syncState.value = SyncState.Success
            } catch (e: Throwable) {
                _syncState.value = SyncState.Error(e.message ?: "Synchronization failed")
            }
        }
    }

    fun login(email: String, password: String, onSuccess: () -> Unit ) {
        viewModelScope.launch {
            _loginUiState.value = LoginUiState(isLoading = true)

            try {
                Appwrite.onLogin(email, password)
                val currentUser = Appwrite.getCurrentUser()
                val userId: String = currentUser?.id ?: throw Exception("User ID was not found")

                seedSchedule(userId)
                loadSchedule(userId)
                checkReviewerStatus()

                _loginUiState.value = LoginUiState(isLoading = false)
                onSuccess()
            } catch (e: Throwable) {
                val errorMsg = Appwrite.ParseErrorMsg(e.message ?: "")
                _loginUiState.value = LoginUiState(isLoading = false, error = errorMsg)
            }
        }
    }

    fun clearError() {
        _loginUiState.value = _loginUiState.value.copy(error = null)
    }

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

        val prefs = getApplication<Application>().getSharedPreferences("settings_prefs", Context.MODE_PRIVATE)
        if (isDarkTheme != null) {
            if (isDarkTheme == true) {
                prefs.edit().putBoolean("is_dark_theme", true).apply()
            } else {
                prefs.edit().putBoolean("is_dark_theme", false).apply()
            }
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
        _imageState.value = ProfileImageState.Loading
        val result = repository.uploadImage(context, uri, userId)
        if (result.isSuccess) {
            val fileId = result.getOrNull()
            if (fileId != null) {
                userIconId = fileId
                updateUserSettings()
                loadProfileImage(userId)
            }
        } else {
            val e = result.exceptionOrNull()
            Log.d("AppViewModel", "Failed to upload image: ${e?.message}")
            snackbarHostState.showSnackbar("Failed to upload image, check your internet connection DEBUG: ${e?.message}")
            _imageState.value = ProfileImageState.Error("Failed to upload Image")
        }

        return result
    }

    suspend fun getUserSettings(userId: String) {

        var persistentSettings = PersistentData(
            isDarkTheme = false,
            unit = "kg",
            restTimeSeconds = 90
        )

        val contexter = getApplication<Application>()
        val file = File(contexter.filesDir, "settings.json")

        if (file.exists()) {
            val json = file.readText()
            persistentSettings = Json.decodeFromString(PersistentData.serializer(), json)

        } else {
            persistentSettings = PersistentData(
                isDarkTheme = false,
                unit = "kg",
                restTimeSeconds = 90
            )
        }

        isDarkTheme = persistentSettings.isDarkTheme
        unit = persistentSettings.unit
        restTime = persistentSettings.restTimeSeconds

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

    private val _imageState = MutableStateFlow<ProfileImageState>(ProfileImageState.Loading)
    val imageState: StateFlow<ProfileImageState> = _imageState.asStateFlow()

    fun loadProfileImage(userId: String) {
        viewModelScope.launch {
            _imageState.value = ProfileImageState.Loading

            val bitmap = repository.getProfileImage(userId)

            if (bitmap != null) {
                _imageState.value = ProfileImageState.Success(bitmap)
            } else {
                _imageState.value = ProfileImageState.Error("Failed to load image")
            }
        }
    }

    fun updateUserData(name: String, email: String, password: String) {

        viewModelScope.launch {
            try {
                repository.changeUserName(name)
            } catch (e: Exception) {
                snackbarHostState.showSnackbar("Failed to change user name: ${e.message}")
            }

            try {
                repository.changeUserEmail(email, password)
            } catch (e: Exception) {
                snackbarHostState.showSnackbar("Failed to change user email: ${e.message}")
            }
        }
    }

    fun FormatWeight(weight: Float): String {
        return if (weight % 1f == 0f) {
            weight.toInt().toString()
        } else {
            weight.toString()
        }
    }

    fun GetWeightDisplay(weight: Float): String {
        return if (weight <= 0f) {
            "Bw/Ud"
        } else {
            val converted = convertUnit(weight, unit)
            "${FormatWeight(converted)}"
        }
    }

}
