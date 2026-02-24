package com.example.waterreminder

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class ReminderWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        val ctx = applicationContext
        NotificationHelper.ensureChannel(ctx)
        NotificationHelper.show(ctx, "وقت شرب موية 💧", "خذ كم رشفة الآن.")
        return Result.success()
    }
}