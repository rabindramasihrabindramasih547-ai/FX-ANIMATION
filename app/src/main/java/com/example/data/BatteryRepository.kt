package com.example.data

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import com.example.model.BatteryHealthStatus
import com.example.model.BatteryState
import com.example.model.ChargingSource
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged

class BatteryRepository(private val context: Context) {

    fun getCurrentBatteryState(): BatteryState {
        val filter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        val intent = context.registerReceiver(null, filter)
        return mapIntentToBatteryState(intent)
    }

    fun observeBatteryState(): Flow<BatteryState> = callbackFlow {
        // Send initial state immediately
        trySend(getCurrentBatteryState())

        val receiver = object : BroadcastReceiver() {
            override fun onReceive(ctx: Context?, intent: Intent?) {
                if (intent != null) {
                    val state = mapIntentToBatteryState(intent)
                    trySend(state)
                }
            }
        }

        val filter = IntentFilter().apply {
            addAction(Intent.ACTION_BATTERY_CHANGED)
            addAction(Intent.ACTION_POWER_CONNECTED)
            addAction(Intent.ACTION_POWER_DISCONNECTED)
        }

        context.registerReceiver(receiver, filter)

        awaitClose {
            try {
                context.unregisterReceiver(receiver)
            } catch (_: Exception) {
            }
        }
    }.distinctUntilChanged()

    private fun mapIntentToBatteryState(intent: Intent?): BatteryState {
        if (intent == null) {
            return BatteryState(
                level = 100,
                scale = 100,
                isCharging = false,
                source = ChargingSource.NONE
            )
        }

        val rawLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 100)
        val rawScale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 100)
        val status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
        val isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL
        val isFull = status == BatteryManager.BATTERY_STATUS_FULL
        val plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0)
        val source = ChargingSource.fromPlugged(plugged, isCharging)

        val rawTemp = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1)
        val tempCelsius = if (rawTemp > 0) rawTemp / 10f else null

        val rawVoltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1)
        val voltageMv = if (rawVoltage > 0) rawVoltage else null

        val rawHealth = intent.getIntExtra(BatteryManager.EXTRA_HEALTH, BatteryManager.BATTERY_HEALTH_UNKNOWN)
        val health = BatteryHealthStatus.fromHealth(rawHealth)

        val tech = intent.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY)?.takeIf { it.isNotBlank() }

        return BatteryState(
            level = rawLevel,
            scale = if (rawScale > 0) rawScale else 100,
            isCharging = isCharging,
            isFull = isFull,
            source = source,
            plugged = plugged,
            temperatureCelsius = tempCelsius,
            voltageMv = voltageMv,
            health = health,
            technology = tech,
            lastUpdatedMillis = System.currentTimeMillis()
        )
    }
}
