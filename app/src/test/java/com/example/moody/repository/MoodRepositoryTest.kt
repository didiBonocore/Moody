package com.example.moody.repository

import com.example.moody.data.MoodDao
import com.example.moody.data.MoodEntry
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class MoodRepositoryTest {

    private lateinit var dao: MoodDao
    private lateinit var repository: MoodRepository

    @Before
    fun setUp() {
        dao = mockk(relaxed = true)
        repository = MoodRepository(dao)
    }

    @Test
    fun `insert delegates to DAO and returns ID`() = runTest {
        val entry = MoodEntry(moodType = "happy", emoji = "😊", timestamp = 100L)
        coEvery { dao.insert(entry) } returns 42L

        val result = repository.insert(entry)

        assertEquals(42L, result)
        coVerify { dao.insert(entry) }
    }

    @Test
    fun `delete delegates to DAO`() = runTest {
        val entry = MoodEntry(id = 1, moodType = "sad", emoji = "😟", timestamp = 100L)

        repository.delete(entry)

        coVerify { dao.delete(entry) }
    }

    @Test
    fun `update delegates to DAO`() = runTest {
        val entry = MoodEntry(id = 1, moodType = "neutral", emoji = "😐", note = "Updated", timestamp = 100L)

        repository.update(entry)

        coVerify { dao.update(entry) }
    }

    @Test
    fun `getMoodById returns correct entry`() = runTest {
        val entry = MoodEntry(id = 7, moodType = "happy", emoji = "😊", timestamp = 100L)
        coEvery { dao.getMoodById(7L) } returns entry

        val result = repository.getMoodById(7L)

        assertEquals(entry, result)
    }

    @Test
    fun `getMoodById returns null when not found`() = runTest {
        coEvery { dao.getMoodById(99L) } returns null

        val result = repository.getMoodById(99L)

        assertNull(result)
    }

    @Test
    fun `deleteAll delegates to DAO`() = runTest {
        repository.deleteAll()

        coVerify { dao.deleteAll() }
    }
}
