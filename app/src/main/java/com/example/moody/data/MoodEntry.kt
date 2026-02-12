package com.example.moody.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mood_entries")
data class MoodEntry(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val moodType: String, // "very_happy", "happy", "neutral", "sad", "very_sad"
    val emoji: String, // "😄", "😊", "😐", "😟", "😢"
    val note: String = "", // user can add an optional note
    val timestamp: Long = System.currentTimeMillis()
)