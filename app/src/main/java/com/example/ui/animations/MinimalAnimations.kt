package com.example.ui.animations

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import com.example.model.ChargingTheme
import com.example.model.SettingsState

@Composable
fun DigitalClockAnimation(
    theme: ChargingTheme,
    batteryPercentage: Int,
    isCharging: Boolean,
    settings: SettingsState,
    modifier: Modifier = Modifier
) {
    val speed = settings.effectiveSpeedMultiplier
    val infiniteTransition = rememberInfiniteTransition(label = "DigitalClockHUD")
    val gridPulse by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween((2000 / speed).toInt(), easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "gridPulse"
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        val center = Offset(size.width / 2, size.height / 2)
        val baseRadius = size.minDimension * 0.32f

        // Subtle digital cyan aura
        drawCircle(
            brush = createGlowRadialBrush(center, baseRadius * 1.3f, theme.primaryColor, settings.effectiveGlowIntensity * 0.7f),
            radius = baseRadius * 1.3f,
            center = center
        )

        // Precision tick marks around clock perimeter
        val tickCount = 60
        for (i in 0 until tickCount) {
            val ang = Math.toRadians((i * (360.0 / tickCount) - 90.0).toDouble())
            val isMajor = i % 5 == 0
            val tickLen = if (isMajor) 14f else 6f
            val startDist = baseRadius * 1.02f
            val endDist = startDist + tickLen

            val sx = center.x + (startDist * kotlin.math.cos(ang)).toFloat()
            val sy = center.y + (startDist * kotlin.math.sin(ang)).toFloat()
            val ex = center.x + (endDist * kotlin.math.cos(ang)).toFloat()
            val ey = center.y + (endDist * kotlin.math.sin(ang)).toFloat()

            drawLine(
                color = if (isMajor) theme.accentColor else theme.primaryColor.copy(alpha = 0.4f * gridPulse),
                start = Offset(sx, sy),
                end = Offset(ex, ey),
                strokeWidth = if (isMajor) 3f else 1.5f
            )
        }

        // Clock battery percentage arc
        drawArc(
            color = theme.accentColor,
            startAngle = -90f,
            sweepAngle = (batteryPercentage / 100f) * 360f,
            useCenter = false,
            topLeft = Offset(center.x - baseRadius, center.y - baseRadius),
            size = Size(baseRadius * 2, baseRadius * 2),
            style = Stroke(width = 7f, cap = StrokeCap.Round)
        )
    }
}

@Composable
fun MinimalGlowAnimation(
    theme: ChargingTheme,
    batteryPercentage: Int,
    isCharging: Boolean,
    settings: SettingsState,
    modifier: Modifier = Modifier
) {
    val speed = settings.effectiveSpeedMultiplier
    val infiniteTransition = rememberInfiniteTransition(label = "MinimalGlow")
    val breathing by infiniteTransition.animateFloat(
        initialValue = 0.75f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween((3000 / speed).toInt(), easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "breathing"
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        val center = Offset(size.width / 2, size.height / 2)
        val baseRadius = size.minDimension * 0.28f

        // Ultra-low power soft breathing glow ring
        drawCircle(
            color = theme.primaryColor.copy(alpha = 0.08f * breathing * settings.effectiveGlowIntensity),
            radius = baseRadius * 1.25f,
            center = center
        )

        drawCircle(
            color = Color(0xFF222222),
            radius = baseRadius,
            center = center,
            style = Stroke(width = 4f)
        )

        drawArc(
            color = theme.primaryColor.copy(alpha = breathing),
            startAngle = -90f,
            sweepAngle = (batteryPercentage / 100f) * 360f,
            useCenter = false,
            topLeft = Offset(center.x - baseRadius, center.y - baseRadius),
            size = Size(baseRadius * 2, baseRadius * 2),
            style = Stroke(width = 5f, cap = StrokeCap.Round)
        )
    }
}
