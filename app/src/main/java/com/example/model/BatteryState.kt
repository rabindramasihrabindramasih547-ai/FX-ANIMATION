package com.example.model

import android.os.BatteryManager

enum class ChargingSource(val displayName: String) {
    USB("USB"),
    AC("AC Wall Charger"),
    WIRELESS("Wireless Charging"),
    DOCK("Dock"),
    UNKNOWN("Charging"),
    NONE("Not Charging");

    companion object {
        fun fromPlugged(plugged: Int, isCharging: Boolean): ChargingSource {
            if (!isCharging) return NONE
            return when (plugged) {
                BatteryManager.BATTERY_PLUGGED_USB -> USB
                BatteryManager.BATTERY_PLUGGED_AC -> AC
                BatteryManager.BATTERY_PLUGGED_WIRELESS -> WIRELESS
                else -> if (isCharging) UNKNOWN else NONE
            }
        }
    }
}

enum class BatteryHealthStatus(val displayName: String) {
    GOOD("Good"),
    OVERHEAT("Overheat"),
    DEAD("Dead"),
    OVER_VOLTAGE("Over Voltage"),
    UNSPECIFIED_FAILURE("Failure"),
    COLD("Cold"),
    UNKNOWN("Unknown");

    companion object {
        fun fromHealth(health: Int): BatteryHealthStatus {
            return when (health) {
                BatteryManager.BATTERY_HEALTH_GOOD -> GOOD
                BatteryManager.BATTERY_HEALTH_OVERHEAT -> OVERHEAT
                BatteryManager.BATTERY_HEALTH_DEAD -> DEAD
                BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE -> OVER_VOLTAGE
                BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE -> UNSPECIFIED_FAILURE
                BatteryManager.BATTERY_HEALTH_COLD -> COLD
                else -> UNKNOWN
            }
        }
    }
}

data class BatteryState(
    val level: Int = 100,
    val scale: Int = 100,
    val isCharging: Boolean = false,
    val isFull: Boolean = false,
    val source: ChargingSource = ChargingSource.NONE,
    val plugged: Int = 0,
    val temperatureCelsius: Float? = null,
    val voltageMv: Int? = null,
    val health: BatteryHealthStatus = BatteryHealthStatus.UNKNOWN,
    val technology: String? = null,
    val lastUpdatedMillis: Long = System.currentTimeMillis()
) {
    val percentage: Int
        get() = if (scale > 0) ((level.toFloat() / scale.toFloat()) * 100).toInt().coerceIn(0, 100) else level.coerceIn(0, 100)

    val isLowBattery: Boolean
        get() = percentage <= 20 && !isCharging

    val isFullCharge: Boolean
        get() = percentage >= 100 || isFull
}
