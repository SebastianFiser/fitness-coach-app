package com.sebastianfiser.fitnesscoach.models

import io.appwrite.Client
import android.content.Context
import android.net.Uri
import io.appwrite.ID
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WorkoutRepository(client: Client, private val imageCacheManager: ImageCacheManager) {
    private val db = AppwriteDB(client)
    private val storage = AppwriteStorage(client)

    suspend fun saveWorkout(userId: String, date: String) = db.saveWorkout(userId, date)
    suspend fun saveSet(workoutId: String, userId: String, exerciseName: String, weight: Float, reps: Int) = db.saveSet(workoutId, userId, exerciseName, weight, reps)
    suspend fun getWorkouts(userId: String) = db.getWorkouts(userId)
    suspend fun getSets(workoutId: String) = db.getSets(workoutId)
    suspend fun saveScheduleItem(userId: String, day: String, exerciseName: String, sets: Int, reps: Int, weight: Float) = db.saveScheduleItem(userId, day, exerciseName, sets, reps, weight)
    suspend fun getSchedule(userId: String) = db.getSchedule(userId)
    suspend fun deleteScheduleItem(itemId: String, userId: String) = db.deleteScheduleItem(itemId, userId)
    suspend fun getSetByUser(userId: String) = db.getSetByUser(userId)
    suspend fun uploadVideo(context: android.content.Context, uri: android.net.Uri, userId: String) = storage.uploadVideo(context, uri, userId)
    suspend fun saveSubmission(userId: String, exerciseName: String, weight: Float, reps: Int, videoFileId: String, country: String?, isNatural: Boolean, age: String, gender: String ) = db.saveSubmission(userId, exerciseName, weight, reps, videoFileId, country, isNatural, age, gender)
    suspend fun getPendingSubmissions() = db.getPendingSubmissions()
    suspend fun updateSubmissionStatus(submissionId: String, status: String) = db.updateSubmissionStatus(submissionId, status)
    suspend fun getApprovedSubmissions() = db.getApprovedSubmissions()
    suspend fun getVideoBytes(fileId: String) = storage.getVideoBytes(fileId)
    suspend fun uploadImage(context: Context, uri: Uri, userId: String) = storage.uploadImage(context, uri, userId)
    suspend fun updateUserSettings(userId: String, restTime: Int, unit: String, isDarkTheme: Boolean, profileIconId: String) = db.updateUserSettings(userId, restTime, unit, isDarkTheme, profileIconId)
    suspend fun getUserSettings(userId: String) = db.getUserSettings(userId)
    suspend fun getImage(fileId: String) = storage.getImage(fileId)
    suspend fun createUserSettings(userId: String) = db.createUserSettings(userId)
    suspend fun changeUserName(newName: String) = db.changeUserName(newName)
    suspend fun changeUserEmail(newEmail: String, currentEmail: String) = db.changeUserEmail(newEmail, currentEmail)

    suspend fun getProfileImage(userId: String): Bitmap? = withContext(Dispatchers.IO) {

        val result = getUserSettings(userId)
        if (result == null) {
            return@withContext null
        }
        val document = result.getOrNull() ?: return@withContext null
        val imageFileId = document.data["profileIconId"] as? String ?: return@withContext null

        val ramBitmap = imageCacheManager.getFromMemory(imageFileId)
        if (ramBitmap != null) {
            return@withContext ramBitmap
        }

        val diskBitmap = imageCacheManager.getFromDisk(imageFileId)
        if (diskBitmap != null) {
            imageCacheManager.saveToMemory(imageFileId, diskBitmap)
            return@withContext diskBitmap
        }

        try {

            val result: Result<ByteArray> = getImage(imageFileId)
            val imageBytes: ByteArray? = result.getOrNull()

            if (imageBytes != null) {
                val networkBitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)

                if (networkBitmap != null) {
                    imageCacheManager.saveToMemory(imageFileId, networkBitmap)
                    imageCacheManager.saveToDisk(imageFileId, networkBitmap)
                    return@withContext networkBitmap
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return@withContext null
    }
}
