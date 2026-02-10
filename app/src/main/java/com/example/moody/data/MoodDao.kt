package com.example.moody.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface MoodDao {
    @Insert
    suspend fun insert(mood: MoodEntry): Long

    @Update
    suspend fun update(mood: MoodEntry)

    @Delete
    suspend fun delete(mood: MoodEntry)

    @Query("SELECT * FROM mood_entries ORDER BY timestamp DESC")
    fun getAllMoods(): LiveData<List<MoodEntry>>

    @Query("""
        SELECT * FROM mood_entries
        WHERE DATE(timestamp/1000, 'unixepoch') = DATE('now')
        ORDER BY timestamp DESC
    """)
    fun getTodayMoods(): LiveData<List<MoodEntry>>

    @Query("SELECT * FROM mood_entries WHERE id = :moodId")
    suspend fun getMoodById(moodId: Long): MoodEntry?

    @Query("DELETE FROM mood_entries")
    suspend fun deleteAll()
}