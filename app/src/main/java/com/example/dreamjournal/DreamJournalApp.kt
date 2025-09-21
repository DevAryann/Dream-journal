package com.example.dreamjournal

import android.app.Application
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.dreamjournal.reminders.ReminderWorker
import java.util.concurrent.TimeUnit

class DreamJournalApp : Application() {
    override fun onCreate() {
        super.onCreate()
        scheduleReminders()
    }

    private fun scheduleReminders() {
        val work = PeriodicWorkRequestBuilder<ReminderWorker>(24, TimeUnit.HOURS)
            .addTag(ReminderWorker.WORK_NAME)
            .build()
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            ReminderWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            work
        )
    }
}
