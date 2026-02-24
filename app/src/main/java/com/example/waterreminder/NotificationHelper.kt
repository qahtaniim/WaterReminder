package com.example.waterreminder

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat

object NotificationHelper {

    private const val CHANNEL_ID = "water_reminder"
    private const val CHANNEL_NAME = "Water Reminder"
    private const val CHANNEL_DESC = "Reminds you to drink water"

    fun ensureChannel(context: Context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return

        val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val existing = nm.getNotificationChannel(CHANNEL_ID)
        if (existing != null) return

        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = CHANNEL_DESC
        }

        nm.createNotificationChannel(channel)
    }

    private fun hasPostNotificationsPermission(context: Context): Boolean {
        if (Build.VERSION.SDK_INT < 33) return true
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
    }

    @SuppressLint("MissingPermission")
    fun show(context: Context, title: String, text: String) {
        if (!hasPostNotificationsPermission(context)) return

        ensureChannel(context)

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(text)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(context)
            .notify((System.currentTimeMillis() % 100000).toInt(), notification)
    }
}