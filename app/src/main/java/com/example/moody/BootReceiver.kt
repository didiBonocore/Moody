package com.example.moody

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            // Re-schedule the daily reminder after device reboot
            // (AlarmManager alarms are lost when the device restarts)
            if (ReminderScheduler.isReminderEnabled(context)) {
                ReminderScheduler.schedule(context)
            }
        }
    }
}
