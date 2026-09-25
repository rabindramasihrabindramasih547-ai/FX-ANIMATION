package com.example.ui.animations

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import kotlin.math.cos
import kotlin.math.sin

data class AnimatedParticle(
    var x: Float,
    var y: Float,
    var speed: Float,
    var radius: Float,
    var alpha: Float,
    var angle: Float = 0f,
    var angularSpeed: Float = 0f,
    var distance: Float = 0f
)

fun createGlowRadialBrush(center: Offset, radius: Float, color: Color, intensity: Float = 1.0f): Brush {
    val clampedIntensity = intensity.coerceIn(0.1f, 2.0f)
    return Brush.radialGradient(
        colors = listOf(
            color.copy(alpha = (0.85f * clampedIntensity).coerceIn(0f, 1f)),
            color.copy(alpha = (0.45f * clampedIntensity).coerceIn(0f, 1f)),
            color.copy(alpha = (0.15f * clampedIntensity).coerceIn(0f, 1f)),
            Color.Transparent
        ),
        center = center,
        radius = radius
    )
}

fun DrawScope.drawRadialRays(
    center: Offset,
    rayCount: Int,
    baseRadius: Float,
    rayLength: Float,
    rotationDeg: Float,
    color: Color,
    strokeWidth: Float = 2f
) {
    val rotRad = Math.toRadians(rotationDeg.toDouble())
    for (i in 0 until rayCount) {
        val angle = rotRad + (2 * Math.PI * i / rayCount)
        val startX = center.x + (baseRadius * cos(angle)).toFloat()
        val startY = center.y + (baseRadius * sin(angle)).toFloat()
        val endX = center.x + ((baseRadius + rayLength) * cos(angle)).toFloat()
        val endY = center.y + ((baseRadius + rayLength) * sin(angle)).toFloat()
        drawLine(
            color = color,
            start = Offset(startX, startY),
            end = Offset(endX, endY),
            strokeWidth = strokeWidth
        )
    }
}
