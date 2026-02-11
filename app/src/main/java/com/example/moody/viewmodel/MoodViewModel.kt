package com.example.moody.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.moody.data.MoodDatabase
import com.example.moody.data.MoodEntry
import com.example.moody.repository.MoodRepository
import kotlinx.coroutines.launch

class MoodViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: MoodRepository
    val allMoods: LiveData<List<MoodEntry>>
    val todayMoods: LiveData<List<MoodEntry>>

    init {
        // Initialize database and repository
        val moodDao = MoodDatabase.getDatabase(application).moodDao()
        repository = MoodRepository(moodDao)
        allMoods = repository.allMoods
        todayMoods = repository.todayMoods
    }

    // Insert a new mood entry
    fun insert(mood: MoodEntry) = viewModelScope.launch {
        repository.insert(mood)
    }

    // Update an existing mood
    fun update(mood: MoodEntry) = viewModelScope.launch {
        repository.update(mood)
    }

    // Delete a mood
    fun delete(mood: MoodEntry) = viewModelScope.launch {
        repository.delete(mood)
    }

    // Delete all moods (for testing)
    fun deleteAll() = viewModelScope.launch {
        repository.deleteAll()
    }
}