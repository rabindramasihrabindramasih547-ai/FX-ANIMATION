package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Power
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.BatteryHealthStatus
import com.example.model.BatteryState
import com.example.ui.components.GlowingCard
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextTertiary

@Composable
fun BatteryInfoScreen(
    batteryState: BatteryState,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        // App Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .testTag("battery_info_back_button")
                    .size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = TextPrimary
                )
            }
            Spacer(modifier = Modifier.width(4.dp))
            Column {
                Text(
                    text = "BATTERY TELEMETRY",
                    color = TextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "Live Android Hardware Readings",
                    color = NeonCyan,
                    fontSize = 11.sp
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            // Main Percentage Card
            GlowingCard(
                borderColor = if (batteryState.isCharging) NeonCyan else Color(0xFF22314E),
                glowColor = if (batteryState.isCharging) NeonCyan else null
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "CURRENT CAPACITY",
                        color = TextSecondary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                text = "${batteryState.percentage}",
                                color = TextPrimary,
                                fontSize = 52.sp,
                                fontWeight = FontWeight.Black,
                                fontFamily = FontFamily.Monospace
                            )
                            Text(
                                text = "%",
                                color = NeonCyan,
                                fontSize = 26.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(bottom = 8.dp, start = 2.dp)
                            )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = if (batteryState.isCharging) "CHARGING" else "DISCHARGING",
                                color = if (batteryState.isCharging) NeonGreen else TextSecondary,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = batteryState.source.displayName,
                                color = NeonCyan,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Hardware Telemetry List
            Text(
                text = "SYSTEM HARDWARE SENSORS",
                color = TextSecondary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            // Temperature (if provided by Android)
            if (batteryState.temperatureCelsius != null) {
                TelemetryItem(
                    icon = Icons.Default.Thermostat,
                    title = "Battery Temperature",
                    value = String.format("%.1f °C", batteryState.temperatureCelsius),
                    accentColor = if (batteryState.temperatureCelsius > 42f) Color(0xFFFF5252) else NeonCyan
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            // Voltage (if provided by Android)
            if (batteryState.voltageMv != null) {
                val voltageV = batteryState.voltageMv / 1000f
                TelemetryItem(
                    icon = Icons.Default.Bolt,
                    title = "Voltage",
                    value = String.format("%.2f V (%d mV)", voltageV, batteryState.voltageMv),
                    accentColor = NeonPurple
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            // Health (if known)
            if (batteryState.health != BatteryHealthStatus.UNKNOWN) {
                TelemetryItem(
                    icon = Icons.Default.HealthAndSafety,
                    title = "Battery Health",
                    value = batteryState.health.displayName,
                    accentColor = if (batteryState.health == BatteryHealthStatus.GOOD) NeonGreen else Color(0xFFFF9100)
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            // Technology (if provided by Android)
            if (!batteryState.technology.isNullOrBlank()) {
                TelemetryItem(
                    icon = Icons.Default.Memory,
                    title = "Technology",
                    value = batteryState.technology,
                    accentColor = NeonCyan
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            // Charging Source
            TelemetryItem(
                icon = Icons.Default.Power,
                title = "Power Source Connection",
                value = batteryState.source.displayName,
                accentColor = if (batteryState.isCharging) NeonGreen else TextSecondary
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Note on sensor availability
            Text(
                text = "Note: Battery metrics are read directly from Android's BatteryManager hardware APIs without simulation. Sensor data not supported by the device kernel is automatically hidden.",
                color = TextTertiary,
                fontSize = 11.sp,
                lineHeight = 16.sp
            )

            Spacer(modifier = Modifier.height(36.dp))
        }
    }
}

@Composable
private fun TelemetryItem(
    icon: ImageVector,
    title: String,
    value: String,
    accentColor: Color
) {
    GlowingCard(cornerRadius = 14.dp) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = accentColor,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = title,
                    color = TextPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }
            Text(
                text = value,
                color = accentColor,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
            )
        }
    }
}
