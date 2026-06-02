package com.sebastianfiser.fitnesscoach.models

import android.content.Context
import io.appwrite.Client
import io.appwrite.ID
import io.appwrite.services.Database
import io.appwrite.models.Document
import io.appwrite.models.*
import io.appwrite.services.*

class AppwriteDB(private valclient: Client) {
    private val database = Databases(client)

    companion object {
        private const val DB_ID = "fitness-coach-db"
        private const val USER_SETTINGS_COL_ID = "user_settings"
        private const val WORKOUT_COL_ID = "workouts"
        private const val SETS_COL_ID = "sets"
    }

    suspend fun saveWorkout(userId: String, date: String): Result<Document> {
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

    suspend fun saveSet(workoutId: String, userId: String, exerciseName: String, weight: Float, reps: Int) : Result<Document> {
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

    suspend fun getSets(workoutId: String): Result<List<Document>> {
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

    suspend fun getWorkouts(userId: String): Result<List<Document>> {
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
}
