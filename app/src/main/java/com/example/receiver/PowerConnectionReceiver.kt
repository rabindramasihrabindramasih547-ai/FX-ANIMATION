package com.example.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import com.example.MainActivity
import com.example.data.BatteryRepository
import com.example.data.SettingsRepository
import com.example.model.ChargingTheme
import com.example.service.ChargingMonitorService
import com.example.util.NotificationHelper
import com.example.util.SoundPlayer
import com.example.util.VibrationHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class PowerConnectionReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        val action = intent?.action ?: return

        when (action) {
            Intent.ACTION_POWER_CONNECTED -> {
                handlePowerConnected(context)
            }
            Intent.ACTION_POWER_DISCONNECTED -> {
                handlePowerDisconnected(context)
            }
        }
    }

    private fun handlePowerConnected(context: Context) {
        val appContext = context.applicationContext
        val settingsRepo = SettingsRepository(appContext)
        val batteryRepo = BatteryRepository(appContext)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val settings = settingsRepo.settingsFlow.first()
                val battery = batteryRepo.getCurrentBatteryState()

                // Sound
                if (settings.soundEnabled) {
                    SoundPlayer.playChargingSound(settings.soundStyle)
                }

                // Vibration
                if (settings.vibrationEnabled) {
                    VibrationHelper.vibrateChargeConnected(appContext)
                }

                // Random theme selection if enabled
                if (settings.randomThemeMode) {
                    val allThemes = ChargingTheme.entries
                    val candidates = allThemes.filter { it.id != settings.selectedThemeId }
                    if (candidates.isNotEmpty()) {
                        val picked = candidates.random()
                        settingsRepo.updateSelectedTheme(picked.id)
                    }
                }

                // Notification & Service
                if (settings.notificationEnabled) {
                    NotificationHelper.updateChargingNotification(appContext, battery)
                    try {
                        val serviceIntent = Intent(appContext, ChargingMonitorService::class.java)
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            appContext.startForegroundService(serviceIntent)
                        } else {
                            appContext.startService(serviceIntent)
                        }
                    } catch (_: Exception) {
                    }
                }

                // Auto start full screen charging animation if enabled
                if (settings.autoStartAnimation) {
                    val activityIntent = Intent(appContext, MainActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                                Intent.FLAG_ACTIVITY_SINGLE_TOP or
                                Intent.FLAG_ACTIVITY_CLEAR_TOP
                        putExtra("EXTRA_START_CHARGING_SCREEN", true)
                    }
                    appContext.startActivity(activityIntent)
                }
            } catch (_: Exception) {
            }
        }
    }

    private fun handlePowerDisconnected(context: Context) {
        val appContext = context.applicationContext
        NotificationHelper.dismissNotification(appContext)
        try {
            val serviceIntent = Intent(appContext, ChargingMonitorService::class.java)
            appContext.stopService(serviceIntent)
        } catch (_: Exception) {
        }
    }
}
