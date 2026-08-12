package com.sebastianfiser.fitnesscoach.models

import io.appwrite.Client
import io.appwrite.ID
import io.appwrite.models.Document
import io.appwrite.models.*
import io.appwrite.services.*
import io.appwrite.Permission
import io.appwrite.Role
import io.appwrite.Query
import com.sebastianfiser.fitnesscoach.models.Appwrite
import com.sebastianfiser.fitnesscoach.models.AppwriteClient
import io.appwrite.services.Account
import io.appwrite.exceptions.AppwriteException


class AppwriteDB(private val client: Client) {
    private val databases = Databases(client)

    companion object {
        private const val DB_ID = "fitness-coach-db"
        private const val USER_SETTINGS_COL_ID = "user_settings"
        private const val WORKOUT_COL_ID = "workouts"
        private const val SETS_COL_ID = "sets"
        private const val SCHEDULE_COL_ID = "schedule"
        private const val SUBMISSION_COL_ID = "submission"
    }

    suspend fun saveWorkout(userId: String, date: String): Result<Document<Map<String, Any>>> {
        return runCatching {
            databases.createDocument(
                databaseId = DB_ID,
                collectionId = WORKOUT_COL_ID,
                documentId = ID.unique(),
                data = mapOf(
                    "userId" to userId,
                    "date" to date
                ),
                permissions = listOf(
                    Permission.read(Role.user(userId)),
                    Permission.update(Role.user(userId)),
                    Permission.delete(Role.user(userId))
                )
            )
        }
    }

    suspend fun saveSet(workoutId: String, userId: String, exerciseName: String, weight: Float, reps: Int) : Result<Document<Map<String, Any>>> {
        return runCatching {
            databases.createDocument(
                databaseId = DB_ID,
                collectionId = SETS_COL_ID,
                documentId = ID.unique(),
                data = mapOf(
                    "workoutId" to workoutId,
                    "userId" to userId,
                    "exerciseName" to exerciseName,
                    "weight" to weight,
                    "reps" to reps
                ),
                permissions = listOf(
                    Permission.read(Role.user(userId)),
                    Permission.update(Role.user(userId)),
                    Permission.delete(Role.user(userId))
                )
            )
        }
    }

    suspend fun getSets(workoutId: String): Result<List<Document<Map<String, Any>>>> {
        return runCatching {
            val response = databases.listDocuments(
                databaseId = DB_ID,
                collectionId = SETS_COL_ID,
                queries = listOf(
                    Query.equal("workoutId", workoutId)
                )
            )
            response.documents
        }
    }

    suspend fun getWorkouts(userId: String): Result<List<Document<Map<String, Any>>>> {
        return runCatching {
            val response = databases.listDocuments(
                databaseId = DB_ID,
                collectionId = WORKOUT_COL_ID,
                queries = listOf(
                    Query.equal("userId", userId),
                    Query.orderDesc("date")
                )
            )
            response.documents
        }
    }

    suspend fun saveScheduleItem(userId: String, day: String, exerciseName: String, sets: Int, reps: Int, weight: Float): Result<Document<Map<String, Any>>> {
        return runCatching {
            databases.createDocument(
                databaseId = DB_ID,
                collectionId = SCHEDULE_COL_ID,
                documentId = ID.unique(),
                data = mapOf(
                    "userId" to userId,
                    "day" to day,
                    "exerciseName" to exerciseName,
                    "sets" to sets,
                    "reps" to reps,
                    "weight" to weight
                ),
                permissions = listOf(
                    Permission.read(Role.user(userId)),
                    Permission.update(Role.user(userId)),
                    Permission.delete(Role.user(userId))
                )
            )
        }
    }

    suspend fun getSchedule(userId: String): Result<List<Document<Map<String, Any>>>> {
        return runCatching {
            val response = databases.listDocuments(
                databaseId = DB_ID,
                collectionId = SCHEDULE_COL_ID,
                queries = listOf(
                    Query.equal("userId", userId)
                )
            )
            response.documents
        }
    }

    suspend fun deleteScheduleItem(itemId: String, userId: String): Result<Unit> {
        return runCatching {
            databases.deleteDocument(
                databaseId = DB_ID,
                collectionId = SCHEDULE_COL_ID,
                documentId = itemId
            )
        }
    }

    suspend fun getSetByUser(userId: String): Result<List<Document<Map<String, Any>>>> {
        return runCatching {
            val response = databases.listDocuments(
                databaseId = DB_ID,
                collectionId = SETS_COL_ID,
                queries = listOf(
                    Query.equal("userId", userId)
                )
            )
            response.documents
        }
    }

    suspend fun saveSubmission(userId: String, exerciseName: String, weight: Float, reps: Int, videoFileId: String, country: String?, isNatural: Boolean, age: String, gender: String ): Result<Document<Map<String, Any>>> {
        val status = "pending"
        val user = Appwrite.getCurrentUser()
        val userName = user?.name ?: "Unknown"
        return runCatching {
            databases.createDocument(
                databaseId = DB_ID,
                collectionId = SUBMISSION_COL_ID,
                documentId = ID.unique(),
                data = mapOf(
                    "userId" to userId,
                    "exerciseName" to exerciseName,
                    "weight" to weight,
                    "reps" to reps,
                    "videoFileId" to videoFileId,
                    "status" to status,
                    "country" to country,
                    "isNatural" to isNatural,
                    "ageGroup" to age,
                    "gender" to gender,
                    "userName" to userName
                ),
                permissions = listOf(
                    Permission.read(Role.user(userId)),
                    Permission.update(Role.user(userId)),
                    Permission.delete(Role.user(userId))
                )
            )
        }
    }

    suspend fun getPendingSubmissions(): Result<List<Document<Map<String, Any>>>> {
        return runCatching {
            val response = databases.listDocuments(
                databaseId = DB_ID,
                collectionId = SUBMISSION_COL_ID,
                queries = listOf(
                    Query.equal("status", "pending")
                )
            )
            response.documents
        }
    }

    suspend fun updateSubmissionStatus(submissionId: String, newStatus: String): Result<Document<Map<String, Any>>> {
        return runCatching {
            databases.updateDocument(
                databaseId = DB_ID,
                collectionId = SUBMISSION_COL_ID,
                documentId = submissionId,
                data = mapOf(
                    "status" to newStatus
                )
            )
        }
    }

    suspend fun getApprovedSubmissions(): Result<List<Document<Map<String, Any>>>> {
        return runCatching {
            val response = databases.listDocuments(
                databaseId = DB_ID,
                collectionId = SUBMISSION_COL_ID,
                queries = listOf(
                    Query.equal("status", "approved"),
                    Query.orderDesc("weight"),
                    Query.limit(20)
                )
            )
            response.documents
        }
    }

    suspend fun updateUserSettings(userId: String, restTime: Int, unit: String, isDarkTheme: Boolean, profileIconId: String): Result<Document<Map<String, Any>>> {
        return runCatching {
            val idToSend: String? = if (profileIconId == "") null else profileIconId
            databases.updateDocument(
                databaseId = DB_ID,
                collectionId = USER_SETTINGS_COL_ID,
                documentId = userId,
                data = mapOf(
                    "restTime" to restTime,
                    "unit" to unit,
                    "isDarkTheme" to isDarkTheme,
                    "profileIconId" to idToSend
                )
            )
        }
    }

    suspend fun getUserSettings(userId: String): Result<Document<Map<String, Any>>> {
        return runCatching {
            databases.getDocument(
                databaseId = DB_ID,
                collectionId = USER_SETTINGS_COL_ID,
                documentId = userId
            )
        }
    }

    suspend fun createUserSettings(userId: String): Result<Document<Map<String, Any>>> {
        return runCatching {
            databases.createDocument(
                databaseId = DB_ID,
                collectionId = USER_SETTINGS_COL_ID,
                documentId = userId,
                data = mapOf(
                    "restTime" to 60,
                    "unit" to "kg",
                    "isDarkTheme" to false,
                    "profileIconId" to ""
                ),
                permissions = listOf(
                    Permission.read(Role.user(userId)),
                    Permission.update(Role.user(userId)),
                    Permission.delete(Role.user(userId))
                )
            )
        }
    }

    suspend fun changeUserName(newName: String): Result<Account> {
        return runCatching {
            AppwriteClient.account.updateName(name = newName)
        }
    }

    suspend fun changeUserEmail(newEmail: String, currentEmail: String): Result<Account> {
        return runCatching {
            AppwriteClient.account.updateEmail(email = newEmail, currentEmail = currentEmail)
        }
    }


}
