package com.sebastianfiser.fitnesscoach.models

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ScheduleDao {

    @Query("SELECT * FROM schedule_items WHERE userId = :userId ORDER BY day ASC")
    fun getScheduleForUser(userId: String): Flow<List<ScheduleEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateAll(items: List<ScheduleEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(item: ScheduleEntity)

    @Query("DELETE FROM schedule_items WHERE userId = :userId")
    suspend fun deleteScheduleForUser(userId: String)

    @Query("SELECT * FROM schedule_items WHERE isDirty = 1")
    suspend fun getDirtyScheduleItems(): List<ScheduleEntity>
}
