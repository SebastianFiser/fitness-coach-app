package com.sebastianfiser.fitnesscoach.models

import io.appwrite.Client

class WorkoutRepository(client: Client) {
    private val db = AppwriteDB(client)

    suspend fun saveWorkout(userId: String, date: String) = db.saveWorkout(userId, date)
    suspend fun saveSet(workoutId: String, userId: String, exerciseName: String, weight: Float, reps: Int) = db.saveSet(workoutId, userId, exerciseName, weight, reps)
    suspend fun getWorkouts(userId: String) = db.getWorkouts(userId)
    suspend fun getSets(workoutId: String) = db.getSets(workoutId)
    suspend fun saveScheduleItem(userId: String, day: String, exerciseName: String, sets: Int, reps: Int, weight: Float) = db.saveScheduleItem(userId, day, exerciseName, sets, reps, weight)
    suspend fun getSchedule(userId: String) = db.getSchedule(userId)
    suspend fun deleteScheduleItem(itemId: String, userId: String) = db.deleteScheduleItem(itemId, userId)
}