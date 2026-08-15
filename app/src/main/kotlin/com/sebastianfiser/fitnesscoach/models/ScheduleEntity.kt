package com.sebastianfiser.fitnesscoach.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "schedule_items")
data class ScheduleEntity(
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
