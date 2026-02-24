package com.example.waterreminder

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

object ReminderScheduler {

    fun schedule(context: Context, repeatMinutes: Long) {
        val request = PeriodicWorkRequestBuilder<ReminderWorker>(
            repeatMinutes, TimeUnit.MINUTES
        ).build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "water_reminder_work",
            ExistingPeriodicWorkPolicy.UPDATE,
            request
        )
    }

    fun cancel(context: Context) {
        WorkManager.getInstance(context).cancelUniqueWork("water_reminder_work")
    }
}