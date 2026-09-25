package com.example.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.MainActivity
import com.example.model.BatteryState

object NotificationHelper {
    const val CHANNEL_ID = "charge_fx_charging_channel"
    private const val CHANNEL_NAME = "Charging Animation & Status"
    const val NOTIFICATION_ID = 1001

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Shows real-time battery status and charging animation controls"
                setShowBadge(false)
                enableVibration(false)
                setSound(null, null)
            }
            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
            manager?.createNotificationChannel(channel)
        }
    }

    fun updateChargingNotification(context: Context, batteryState: BatteryState) {
        if (!batteryState.isCharging) {
            dismissNotification(context)
            return
        }

        try {
            val intent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                putExtra("EXTRA_START_CHARGING_SCREEN", true)
            }
            val pendingIntent = PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val sourceText = batteryState.source.displayName
            val contentTitle = "⚡ Charge FX — ${batteryState.percentage}%"
            val contentText = if (batteryState.isFullCharge) {
                "Battery Fully Charged (100%)"
            } else {
                "Charging via $sourceText"
            }

            val notification = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_lock_idle_charging)
                .setContentTitle(contentTitle)
                .setContentText(contentText)
                .setSubText("${batteryState.percentage}%")
                .setProgress(100, batteryState.percentage, false)
                .setOngoing(true)
                .setOnlyAlertOnce(true)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setCategory(NotificationCompat.CATEGORY_STATUS)
                .build()

            val manager = NotificationManagerCompat.from(context)
            if (manager.areNotificationsEnabled()) {
                manager.notify(NOTIFICATION_ID, notification)
            }
        } catch (_: SecurityException) {
            // Android 13+ permission not granted yet
        } catch (_: Exception) {
        }
    }

    fun dismissNotification(context: Context) {
        try {
            val manager = NotificationManagerCompat.from(context)
            manager.cancel(NOTIFICATION_ID)
        } catch (_: Exception) {
        }
    }
}
