package com.example.moody.data

import org.junit.Assert.*
import org.junit.Test

class MoodEntryTest {

    @Test
    fun `default values are set correctly`() {
        val entry = MoodEntry(moodType = "happy", emoji = "😊")

        assertEquals(0L, entry.id)
        assertEquals("", entry.note)
        assertTrue(entry.timestamp > 0)
    }

    @Test
    fun `custom values are stored correctly`() {
        val entry = MoodEntry(
            id = 5,
            moodType = "sad",
            emoji = "😟",
            note = "Bad day",
            timestamp = 1000L
        )

        assertEquals(5L, entry.id)
        assertEquals("sad", entry.moodType)
        assertEquals("😟", entry.emoji)
        assertEquals("Bad day", entry.note)
        assertEquals(1000L, entry.timestamp)
    }

    @Test
    fun `data class equality works`() {
        val entry1 = MoodEntry(id = 1, moodType = "happy", emoji = "😊", note = "Good", timestamp = 100L)
        val entry2 = MoodEntry(id = 1, moodType = "happy", emoji = "😊", note = "Good", timestamp = 100L)

        assertEquals(entry1, entry2)
    }

    @Test
    fun `data class copy works`() {
        val original = MoodEntry(id = 1, moodType = "happy", emoji = "😊", note = "Good", timestamp = 100L)
        val copied = original.copy(note = "Updated note")

        assertEquals(1L, copied.id)
        assertEquals("happy", copied.moodType)
        assertEquals("Updated note", copied.note)
        assertNotEquals(original, copied)
    }
}
