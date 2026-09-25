package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ChargingSource
import com.example.model.ChargingTheme
import com.example.model.ComponentSize
import com.example.model.SettingsState
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun BatteryHudCenter(
    percentage: Int,
    isCharging: Boolean,
    source: ChargingSource,
    theme: ChargingTheme,
    settings: SettingsState,
    modifier: Modifier = Modifier,
    isPreview: Boolean = false
) {
    val infiniteTransition = rememberInfiniteTransition(label = "BoltPulse")
    val pulse by infiniteTransition.animateFloat(
        initialValue = 0.9f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    val percentScale = when (settings.percentageSize) {
        ComponentSize.SMALL -> 0.85f
        ComponentSize.MEDIUM -> 1.0f
        ComponentSize.LARGE -> 1.25f
    }

    val currentTime = rememberCurrentTimeFormatted()

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Preview badge if in preview mode
        if (isPreview) {
            Box(
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .background(Color(0xFFFFA000).copy(alpha = 0.2f), RoundedCornerShape(8.dp))
                    .border(1.dp, Color(0xFFFFA000), RoundedCornerShape(8.dp))
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "PREVIEW MODE",
                    color = Color(0xFFFFA000),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }
        }

        // Real-Time Clock
        if (settings.showClock) {
            Text(
                text = currentTime,
                color = TextSecondary,
                fontSize = (18 * percentScale).sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Medium,
                letterSpacing = 2.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
        }

        // Lightning Bolt Icon
        if (isCharging || isPreview) {
            Icon(
                imageVector = Icons.Default.Bolt,
                contentDescription = "Charging Icon",
                tint = theme.accentColor,
                modifier = Modifier
                    .size((36 * percentScale).dp)
                    .scale(pulse)
            )
        }

        // Big Battery Percentage
        Row(
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "$percentage",
                color = TextPrimary,
                fontSize = (54 * percentScale).sp,
                fontWeight = FontWeight.ExtraBold,
                fontFamily = FontFamily.Monospace,
                letterSpacing = (-1).sp
            )
            Text(
                text = "%",
                color = theme.primaryColor,
                fontSize = (26 * percentScale).sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = (8 * percentScale).dp, start = 2.dp)
            )
        }

        // Charging Status Text
        if (settings.showChargingText) {
            val statusText = when {
                percentage >= 100 -> "BATTERY FULL"
                isCharging || isPreview -> "CHARGING"
                else -> "DISCHARGING"
            }
            Text(
                text = statusText,
                color = if (percentage >= 100) NeonGreen else theme.primaryColor,
                fontSize = (13 * percentScale).sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 2.sp
            )
        }

        // Charging Source Badge
        if (settings.showChargingType && (isCharging || isPreview)) {
            val sourceText = if (isPreview) "PREVIEW (USB)" else source.displayName
            Spacer(modifier = Modifier.height(6.dp))
            Box(
                modifier = Modifier
                    .background(Color.White.copy(alpha = 0.08f), RoundedCornerShape(12.dp))
                    .border(1.dp, theme.primaryColor.copy(alpha = 0.35f), RoundedCornerShape(12.dp))
                    .padding(horizontal = 12.dp, vertical = 3.dp)
            ) {
                Text(
                    text = sourceText,
                    color = TextSecondary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun rememberCurrentTimeFormatted(): String {
    val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
    return formatter.format(Date())
}
