package com.sebastianfiser.fitnesscoach.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import io.appwrite.models.Document

@Entity(tableName = "schedule_items")
data class ScheduleItemEntity(
    @PrimaryKey
    val id: String,
    val userId: String,
    val day: String,
    val exerciseName: String,
    val sets: Int,
    val reps: Int,
    val weight: Float,

    val lastSyncedAt: Long = System.currentTimeMillis(),
    val isDirty: Boolean = false
)

fun Document<Map<String, Any>>.toEntity(): ScheduleEntity {
    return ScheduleEntity(
        id = id,
        userId = data["userId"] as? String ?: "",
        day = data["day"] as? String ?: "",
        exerciseName = data["exerciseName"] as? String ?: "",
        sets = (data["sets"] as? Number)?.toInt() ?: 0,
        reps = (data["reps"] as? Number)?.toInt() ?: 0,
        weight = (data["weight"] as? Number)?.toFloat() ?: 0f,
        lastSyncedAt = System.currentTimeMillis(),
        isDirty = false
    )
}

fun ScheduleEntity.toDocument(): Map<String, Any> {
    return mapOf(
        "userId" to userId,
        "day" to day,
        "exerciseName" to exerciseName,
        "sets" to sets,
        "reps" to reps,
        "weight" to weight
    )
}
