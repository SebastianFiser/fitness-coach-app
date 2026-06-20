package com.sebastianfiser.fitnesscoach.models

import io.appwrite.Client
import io.appwrite.ID
import io.appwrite.models.Document
import io.appwrite.models.*
import io.appwrite.services.*
import io.appwrite.Permission
import io.appwrite.Role
import io.appwrite.Query


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

    suspend fun saveSubmission(userId: String, exerciseName: String, weight: Float, reps: Int, videoFileId: String, country: String, isNatural: Boolean, age: Int, gender: String ): Result<Document<Map<String, Any>>> {
        val status = "pending" 
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
                    "createdAt" to createdAt,
                    "country" to country,
                    "isNatural" to isNatural,
                    "age" to age,
                    "Gender" to gender
                ),
                permissions = listOf(
                    Permission.read(Role.user(userId)),
                    Permission.update(Role.user(userId)),
                    Permission.delete(Role.user(userId))
                )
            )
        }
    }
}
