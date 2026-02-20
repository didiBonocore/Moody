package com.example.moody

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.SharedPreferences
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import io.mockk.verify
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class ReminderSchedulerTest {

    private lateinit var context: Context
    private lateinit var prefs: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var alarmManager: AlarmManager

    @Before
    fun setUp() {
        context = mockk(relaxed = true)
        prefs = mockk(relaxed = true)
        editor = mockk(relaxed = true)
        alarmManager = mockk(relaxed = true)

        every { context.getSharedPreferences("moody_prefs", Context.MODE_PRIVATE) } returns prefs
        every { prefs.edit() } returns editor
        every { editor.putBoolean(any(), any()) } returns editor
        every { context.getSystemService(Context.ALARM_SERVICE) } returns alarmManager

        mockkStatic(PendingIntent::class)
        every { PendingIntent.getService(any(), any(), any(), any()) } returns mockk()
    }

    @After
    fun tearDown() {
        unmockkStatic(PendingIntent::class)
    }

    @Test
    fun `isReminderEnabled returns false by default`() {
        every { prefs.getBoolean("reminder_enabled", false) } returns false

        val result = ReminderScheduler.isReminderEnabled(context)

        assertFalse(result)
    }

    @Test
    fun `isReminderEnabled returns true when enabled`() {
        every { prefs.getBoolean("reminder_enabled", false) } returns true

        val result = ReminderScheduler.isReminderEnabled(context)

        assertTrue(result)
    }

    @Test
    fun `schedule sets reminder enabled to true`() {
        ReminderScheduler.schedule(context)

        verify { editor.putBoolean("reminder_enabled", true) }
        verify { editor.apply() }
    }

    @Test
    fun `cancel sets reminder enabled to false`() {
        ReminderScheduler.cancel(context)

        verify { editor.putBoolean("reminder_enabled", false) }
        verify { editor.apply() }
    }

    @Test
    fun `schedule sets repeating alarm`() {
        ReminderScheduler.schedule(context)

        verify { alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, any(), AlarmManager.INTERVAL_DAY, any()) }
    }

    @Test
    fun `cancel cancels alarm`() {
        ReminderScheduler.cancel(context)

        verify { alarmManager.cancel(any<PendingIntent>()) }
    }
}
