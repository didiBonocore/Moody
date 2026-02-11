package com.example.moody.repository

import androidx.lifecycle.LiveData
import com.example.moody.data.MoodDao
import com.example.moody.data.MoodEntry

class MoodRepository(private val moodDao: MoodDao) {

    // LiveData automatically updates the UI when data changes
    val allMoods: LiveData<List<MoodEntry>> = moodDao.getAllMoods()
    val todayMoods: LiveData<List<MoodEntry>> = moodDao.getTodayMoods()

    // Suspend functions run on background threads
    suspend fun insert(mood: MoodEntry): Long {
        return moodDao.insert(mood)
    }

    suspend fun update(mood: MoodEntry) {
        moodDao.update(mood)
    }

    suspend fun delete(mood: MoodEntry) {
        moodDao.delete(mood)
    }

    suspend fun getMoodById(id: Long): MoodEntry? {
        return moodDao.getMoodById(id)
    }

    suspend fun deleteAll() {
        moodDao.deleteAll()
    }
}