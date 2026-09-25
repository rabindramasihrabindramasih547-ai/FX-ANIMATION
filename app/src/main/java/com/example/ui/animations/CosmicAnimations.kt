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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import com.example.model.ChargingTheme
import com.example.model.SettingsState
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

@Composable
fun GalaxyAnimation(
    theme: ChargingTheme,
    batteryPercentage: Int,
    isCharging: Boolean,
    settings: SettingsState,
    modifier: Modifier = Modifier
) {
    val speed = settings.effectiveSpeedMultiplier
    val infiniteTransition = rememberInfiniteTransition(label = "Galaxy")
    val galaxyRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween((7000 / speed).toInt(), easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "galaxyRot"
    )

    val stars = remember {
        List(36) {
            AnimatedParticle(
                x = 0f,
                y = 0f,
                speed = Random.nextFloat() * 1.3f + 0.7f,
                radius = Random.nextFloat() * 3f + 1.2f,
                alpha = Random.nextFloat() * 0.8f + 0.2f,
                angle = Random.nextFloat() * 360f,
                distance = Random.nextFloat() * 0.7f + 0.3f
            )
        }
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        val center = Offset(size.width / 2, size.height / 2)
        val baseRadius = size.minDimension * 0.3f

        // Deep galactic core glow
        drawCircle(
            brush = createGlowRadialBrush(center, baseRadius * 1.5f, theme.primaryColor, settings.effectiveGlowIntensity),
            radius = baseRadius * 1.5f,
            center = center
        )

        // Spiral Arms
        val arms = 2
        for (a in 0 until arms) {
            val armOffset = a * PI
            val path = Path()
            val points = 32
            for (p in 0..points) {
                val t = (p.toFloat() / points)
                val dist = (baseRadius * 0.4f) + (baseRadius * 0.85f * t)
                val theta = t * 3.5 + Math.toRadians(galaxyRotation.toDouble()) + armOffset
                val gx = center.x + (dist * cos(theta)).toFloat()
                val gy = center.y + (dist * sin(theta)).toFloat()
                if (p == 0) path.moveTo(gx, gy) else path.lineTo(gx, gy)
            }
            drawPath(
                path = path,
                color = if (a == 0) theme.secondaryColor else theme.primaryColor,
                alpha = 0.5f,
                style = Stroke(width = 6f, cap = StrokeCap.Round)
            )
        }

        // Orbiting Stardust
        val activeStars = (stars.size * settings.effectiveParticleDensity).toInt().coerceIn(6, stars.size)
        for (i in 0 until activeStars) {
            val st = stars[i]
            val ang = Math.toRadians((st.angle + galaxyRotation * st.speed).toDouble())
            val d = baseRadius * 1.15f * st.distance
            val sx = center.x + (d * cos(ang)).toFloat()
            val sy = center.y + (d * sin(ang)).toFloat()
            drawCircle(
                color = theme.accentColor,
                radius = st.radius,
                center = Offset(sx, sy),
                alpha = st.alpha
            )
        }

        // Central charging ring
        drawArc(
            color = theme.accentColor,
            startAngle = -90f,
            sweepAngle = (batteryPercentage / 100f) * 360f,
            useCenter = false,
            topLeft = Offset(center.x - baseRadius, center.y - baseRadius),
            size = Size(baseRadius * 2, baseRadius * 2),
            style = Stroke(width = 8f, cap = StrokeCap.Round)
        )
    }
}

@Composable
fun AuroraAnimation(
    theme: ChargingTheme,
    batteryPercentage: Int,
    isCharging: Boolean,
    settings: SettingsState,
    modifier: Modifier = Modifier
) {
    val speed = settings.effectiveSpeedMultiplier
    val infiniteTransition = rememberInfiniteTransition(label = "Aurora")
    val wavePhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2 * PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween((3500 / speed).toInt(), easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "auroraPhase"
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        val center = Offset(size.width / 2, size.height / 2)
        val baseRadius = size.minDimension * 0.3f

        // Soft northern lights glow
        drawCircle(
            brush = createGlowRadialBrush(center, baseRadius * 1.4f, theme.primaryColor, settings.effectiveGlowIntensity),
            radius = baseRadius * 1.4f,
            center = center
        )

        // Multiple undulating ribbons of aurora
        val ribbonCount = 3
        for (r in 0 until ribbonCount) {
            val path = Path()
            val steps = 30
            val yBase = center.y - 100f + r * 60f
            for (s in 0..steps) {
                val x = (s.toFloat() / steps) * size.width
                val y = yBase + sin(s * 0.3f + wavePhase + r * 1.2f).toFloat() * 25f
                if (s == 0) path.moveTo(x, y) else path.lineTo(x, y)
            }
            val color = when (r) {
                0 -> theme.primaryColor
                1 -> theme.secondaryColor
                else -> theme.accentColor
            }
            drawPath(
                path = path,
                color = color,
                alpha = 0.45f,
                style = Stroke(width = 12f, cap = StrokeCap.Round)
            )
        }

        // Circular battery tracker
        drawCircle(
            color = theme.secondaryColor.copy(alpha = 0.3f),
            radius = baseRadius,
            center = center,
            style = Stroke(width = 6f)
        )

        drawArc(
            color = theme.accentColor,
            startAngle = -90f,
            sweepAngle = (batteryPercentage / 100f) * 360f,
            useCenter = false,
            topLeft = Offset(center.x - baseRadius, center.y - baseRadius),
            size = Size(baseRadius * 2, baseRadius * 2),
            style = Stroke(width = 10f, cap = StrokeCap.Round)
        )
    }
}

@Composable
fun CosmicPortalAnimation(
    theme: ChargingTheme,
    batteryPercentage: Int,
    isCharging: Boolean,
    settings: SettingsState,
    modifier: Modifier = Modifier
) {
    val speed = settings.effectiveSpeedMultiplier
    val infiniteTransition = rememberInfiniteTransition(label = "Portal")
    val portalRot by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween((3000 / speed).toInt(), easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "portalRot"
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        val center = Offset(size.width / 2, size.height / 2)
        val baseRadius = size.minDimension * 0.3f

        // Vortex singularity glow
        drawCircle(
            brush = createGlowRadialBrush(center, baseRadius * 1.45f, theme.primaryColor, settings.effectiveGlowIntensity),
            radius = baseRadius * 1.45f,
            center = center
        )

        // Multiple concentric rotating portal rings
        val rings = 4
        for (r in 1..rings) {
            val currentRadius = baseRadius * (0.6f + r * 0.15f)
            val ringSweep = 240f + r * 20f
            val ringStart = portalRot * (if (r % 2 == 0) 1 else -1) * (0.8f + r * 0.2f)

            drawArc(
                color = if (r % 2 == 0) theme.secondaryColor else theme.primaryColor,
                startAngle = ringStart,
                sweepAngle = ringSweep,
                useCenter = false,
                topLeft = Offset(center.x - currentRadius, center.y - currentRadius),
                size = Size(currentRadius * 2, currentRadius * 2),
                style = Stroke(width = 4f + r * 1.5f, cap = StrokeCap.Round)
            )
        }

        // Inner battery arc
        drawArc(
            color = theme.accentColor,
            startAngle = -90f,
            sweepAngle = (batteryPercentage / 100f) * 360f,
            useCenter = false,
            topLeft = Offset(center.x - baseRadius, center.y - baseRadius),
            size = Size(baseRadius * 2, baseRadius * 2),
            style = Stroke(width = 9f, cap = StrokeCap.Round)
        )
    }
}

@Composable
fun SunEnergyAnimation(
    theme: ChargingTheme,
    batteryPercentage: Int,
    isCharging: Boolean,
    settings: SettingsState,
    modifier: Modifier = Modifier
) {
    val speed = settings.effectiveSpeedMultiplier
    val infiniteTransition = rememberInfiniteTransition(label = "Sun")
    val rayRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween((6000 / speed).toInt(), easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rayRot"
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        val center = Offset(size.width / 2, size.height / 2)
        val baseRadius = size.minDimension * 0.28f

        // Solar corona intense radiant glow
        drawCircle(
            brush = createGlowRadialBrush(center, baseRadius * 1.6f, theme.primaryColor, settings.effectiveGlowIntensity),
            radius = baseRadius * 1.6f,
            center = center
        )

        // Solar flare rays
        drawRadialRays(
            center = center,
            rayCount = 18,
            baseRadius = baseRadius * 1.05f,
            rayLength = 32f,
            rotationDeg = rayRotation,
            color = theme.accentColor,
            strokeWidth = 3.5f
        )

        drawRadialRays(
            center = center,
            rayCount = 18,
            baseRadius = baseRadius * 1.08f,
            rayLength = 20f,
            rotationDeg = -rayRotation * 0.7f,
            color = theme.secondaryColor,
            strokeWidth = 2.5f
        )

        // Solar battery arc
        drawArc(
            color = theme.accentColor,
            startAngle = -90f,
            sweepAngle = (batteryPercentage / 100f) * 360f,
            useCenter = false,
            topLeft = Offset(center.x - baseRadius, center.y - baseRadius),
            size = Size(baseRadius * 2, baseRadius * 2),
            style = Stroke(width = 11f, cap = StrokeCap.Round)
        )
    }
}

@Composable
fun MoonlightAnimation(
    theme: ChargingTheme,
    batteryPercentage: Int,
    isCharging: Boolean,
    settings: SettingsState,
    modifier: Modifier = Modifier
) {
    val speed = settings.effectiveSpeedMultiplier
    val infiniteTransition = rememberInfiniteTransition(label = "Moonlight")
    val twinkle by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween((2000 / speed).toInt(), easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "twinkle"
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        val center = Offset(size.width / 2, size.height / 2)
        val baseRadius = size.minDimension * 0.3f

        // Serene lunar halo
        drawCircle(
            brush = createGlowRadialBrush(center, baseRadius * 1.35f, theme.secondaryColor, settings.effectiveGlowIntensity * twinkle),
            radius = baseRadius * 1.35f,
            center = center
        )

        // Crescent moon silhouette aesthetic
        drawCircle(
            color = theme.primaryColor.copy(alpha = 0.3f),
            radius = baseRadius,
            center = center,
            style = Stroke(width = 4f)
        )

        // Elegant lunar progress arc
        drawArc(
            color = theme.accentColor,
            startAngle = -90f,
            sweepAngle = (batteryPercentage / 100f) * 360f,
            useCenter = false,
            topLeft = Offset(center.x - baseRadius, center.y - baseRadius),
            size = Size(baseRadius * 2, baseRadius * 2),
            style = Stroke(width = 8f, cap = StrokeCap.Round)
        )
    }
}
