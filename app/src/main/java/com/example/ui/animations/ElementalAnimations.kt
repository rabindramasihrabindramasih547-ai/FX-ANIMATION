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
import androidx.compose.ui.graphics.Brush
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
fun FireAnimation(
    theme: ChargingTheme,
    batteryPercentage: Int,
    isCharging: Boolean,
    settings: SettingsState,
    modifier: Modifier = Modifier
) {
    val speed = settings.effectiveSpeedMultiplier
    val infiniteTransition = rememberInfiniteTransition(label = "Fire")
    val flameTime by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween((1200 / speed).toInt(), easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "flameTime"
    )

    val sparks = remember {
        List(32) {
            AnimatedParticle(
                x = (Random.nextFloat() - 0.5f) * 160f,
                y = Random.nextFloat() * 180f,
                speed = Random.nextFloat() * 1.5f + 1.0f,
                radius = Random.nextFloat() * 3.5f + 1.5f,
                alpha = Random.nextFloat() * 0.8f + 0.2f
            )
        }
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        val center = Offset(size.width / 2, size.height / 2)
        val baseRadius = size.minDimension * 0.3f

        // Warm radial fire glow
        drawCircle(
            brush = createGlowRadialBrush(center, baseRadius * 1.4f, theme.primaryColor, settings.effectiveGlowIntensity),
            radius = baseRadius * 1.4f,
            center = center
        )

        // Rising flame sparks
        val activeSparks = (sparks.size * settings.effectiveParticleDensity).toInt().coerceIn(6, sparks.size)
        for (i in 0 until activeSparks) {
            val s = sparks[i]
            val progress = (flameTime * s.speed + (i * 0.03f)) % 1f
            val sy = center.y + baseRadius * 0.8f - (progress * 220f)
            val sx = center.x + s.x + sin(progress * PI * 3).toFloat() * 15f
            val sparkColor = if (progress < 0.4f) theme.accentColor else theme.primaryColor

            drawCircle(
                color = sparkColor,
                radius = s.radius * (1f - progress * 0.7f),
                center = Offset(sx, sy),
                alpha = s.alpha * (1f - progress)
            )
        }

        // Fiery Circular Ring
        drawCircle(
            color = theme.secondaryColor.copy(alpha = 0.3f),
            radius = baseRadius,
            center = center,
            style = Stroke(width = 8f)
        )

        drawArc(
            brush = Brush.sweepGradient(
                listOf(theme.primaryColor, theme.secondaryColor, theme.accentColor, theme.primaryColor),
                center = center
            ),
            startAngle = -90f,
            sweepAngle = (batteryPercentage / 100f) * 360f,
            useCenter = false,
            topLeft = Offset(center.x - baseRadius, center.y - baseRadius),
            size = Size(baseRadius * 2, baseRadius * 2),
            style = Stroke(width = 12f, cap = StrokeCap.Round)
        )
    }
}

@Composable
fun IceAnimation(
    theme: ChargingTheme,
    batteryPercentage: Int,
    isCharging: Boolean,
    settings: SettingsState,
    modifier: Modifier = Modifier
) {
    val speed = settings.effectiveSpeedMultiplier
    val infiniteTransition = rememberInfiniteTransition(label = "Ice")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween((6000 / speed).toInt(), easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    val snowParticles = remember {
        List(24) {
            AnimatedParticle(
                x = 0f,
                y = 0f,
                speed = Random.nextFloat() * 1.2f + 0.5f,
                radius = Random.nextFloat() * 3f + 1.5f,
                alpha = Random.nextFloat() * 0.7f + 0.3f,
                angle = Random.nextFloat() * 360f,
                distance = Random.nextFloat() * 0.4f + 0.8f
            )
        }
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        val center = Offset(size.width / 2, size.height / 2)
        val baseRadius = size.minDimension * 0.3f

        // Frost crystal glow
        drawCircle(
            brush = createGlowRadialBrush(center, baseRadius * 1.35f, theme.primaryColor, settings.effectiveGlowIntensity),
            radius = baseRadius * 1.35f,
            center = center
        )

        // 6-pointed Ice Crystal Spikes
        val crystalPoints = 6
        for (i in 0 until crystalPoints) {
            val ang = Math.toRadians((i * (360.0 / crystalPoints) + rotation).toDouble())
            val spikeLen = baseRadius * 1.25f
            val endX = center.x + (spikeLen * cos(ang)).toFloat()
            val endY = center.y + (spikeLen * sin(ang)).toFloat()

            drawLine(
                color = theme.accentColor.copy(alpha = 0.6f),
                start = center,
                end = Offset(endX, endY),
                strokeWidth = 3f,
                cap = StrokeCap.Round
            )
        }

        // Floating Snow Particles
        val activeSnow = (snowParticles.size * settings.effectiveParticleDensity).toInt().coerceIn(4, snowParticles.size)
        for (i in 0 until activeSnow) {
            val sp = snowParticles[i]
            val ang = Math.toRadians((sp.angle + rotation * sp.speed).toDouble())
            val dist = baseRadius * sp.distance
            val px = center.x + (dist * cos(ang)).toFloat()
            val py = center.y + (dist * sin(ang)).toFloat()

            drawCircle(
                color = theme.accentColor,
                radius = sp.radius,
                center = Offset(px, py),
                alpha = sp.alpha
            )
        }

        // Hexagonal frost border
        drawCircle(
            color = theme.secondaryColor.copy(alpha = 0.25f),
            radius = baseRadius,
            center = center,
            style = Stroke(width = 6f)
        )

        drawArc(
            color = theme.primaryColor,
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
fun WaterLiquidAnimation(
    theme: ChargingTheme,
    batteryPercentage: Int,
    isCharging: Boolean,
    settings: SettingsState,
    modifier: Modifier = Modifier
) {
    val speed = settings.effectiveSpeedMultiplier
    val infiniteTransition = rememberInfiniteTransition(label = "WaterLiquid")
    val waveOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2 * PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween((2000 / speed).toInt(), easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "waveOffset"
    )

    val bubbles = remember {
        List(20) {
            AnimatedParticle(
                x = (Random.nextFloat() - 0.5f) * 140f,
                y = Random.nextFloat() * 140f,
                speed = Random.nextFloat() * 1.5f + 0.8f,
                radius = Random.nextFloat() * 4f + 2f,
                alpha = Random.nextFloat() * 0.7f + 0.3f
            )
        }
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        val center = Offset(size.width / 2, size.height / 2)
        val baseRadius = size.minDimension * 0.3f

        // Ambient aquatic glow
        drawCircle(
            brush = createGlowRadialBrush(center, baseRadius * 1.3f, theme.primaryColor, settings.effectiveGlowIntensity),
            radius = baseRadius * 1.3f,
            center = center
        )

        // Rising liquid bubbles
        val activeBubbles = (bubbles.size * settings.effectiveParticleDensity).toInt().coerceIn(4, bubbles.size)
        for (i in 0 until activeBubbles) {
            val b = bubbles[i]
            val progress = ((waveOffset / (2 * PI).toFloat()) * b.speed + i * 0.05f) % 1f
            val by = center.y + baseRadius * 0.7f - (progress * baseRadius * 1.4f)
            val bx = center.x + b.x + sin(progress * PI * 4).toFloat() * 10f

            drawCircle(
                color = theme.accentColor,
                radius = b.radius,
                center = Offset(bx, by),
                alpha = b.alpha * (1f - progress * 0.5f),
                style = Stroke(width = 2f)
            )
        }

        // Wavy boundary ring
        val wavePoints = 48
        val path = Path()
        for (i in 0..wavePoints) {
            val ang = (i.toFloat() / wavePoints) * 2 * PI
            val r = baseRadius + sin(ang * 5 + waveOffset).toFloat() * 6f
            val wx = center.x + (r * cos(ang)).toFloat()
            val wy = center.y + (r * sin(ang)).toFloat()
            if (i == 0) path.moveTo(wx, wy) else path.lineTo(wx, wy)
        }
        path.close()

        drawPath(
            path = path,
            color = theme.secondaryColor.copy(alpha = 0.4f),
            style = Stroke(width = 5f)
        )

        // Real battery charge arc
        drawArc(
            color = theme.primaryColor,
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
fun OceanAnimation(
    theme: ChargingTheme,
    batteryPercentage: Int,
    isCharging: Boolean,
    settings: SettingsState,
    modifier: Modifier = Modifier
) {
    val speed = settings.effectiveSpeedMultiplier
    val infiniteTransition = rememberInfiniteTransition(label = "Ocean")
    val wavePhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2 * PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween((2400 / speed).toInt(), easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "wavePhase"
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        val center = Offset(size.width / 2, size.height / 2)
        val baseRadius = size.minDimension * 0.3f

        // Deep ocean bioluminescent pulse
        drawCircle(
            brush = createGlowRadialBrush(center, baseRadius * 1.4f, theme.accentColor, settings.effectiveGlowIntensity),
            radius = baseRadius * 1.4f,
            center = center
        )

        // Triple wave rings
        for (w in 0..2) {
            val r = baseRadius * (0.85f + w * 0.15f)
            val path = Path()
            val steps = 36
            for (s in 0..steps) {
                val theta = (s.toFloat() / steps) * 2 * PI
                val waveH = sin(theta * 4 + wavePhase + w * 1.2f) * 8f
                val wx = center.x + ((r + waveH) * cos(theta)).toFloat()
                val wy = center.y + ((r + waveH) * sin(theta)).toFloat()
                if (s == 0) path.moveTo(wx, wy) else path.lineTo(wx, wy)
            }
            path.close()
            drawPath(
                path = path,
                color = if (w == 0) theme.secondaryColor else theme.primaryColor,
                alpha = 0.5f - w * 0.1f,
                style = Stroke(width = 4f)
            )
        }

        // Central charging arc
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
fun NatureAnimation(
    theme: ChargingTheme,
    batteryPercentage: Int,
    isCharging: Boolean,
    settings: SettingsState,
    modifier: Modifier = Modifier
) {
    val speed = settings.effectiveSpeedMultiplier
    val infiniteTransition = rememberInfiniteTransition(label = "Nature")
    val leafAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween((5500 / speed).toInt(), easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "leafAngle"
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        val center = Offset(size.width / 2, size.height / 2)
        val baseRadius = size.minDimension * 0.3f

        // Natural bio-glow
        drawCircle(
            brush = createGlowRadialBrush(center, baseRadius * 1.3f, theme.primaryColor, settings.effectiveGlowIntensity),
            radius = baseRadius * 1.3f,
            center = center
        )

        // Circling bio leaves
        val leafCount = 8
        for (i in 0 until leafCount) {
            val ang = Math.toRadians((i * (360.0 / leafCount) + leafAngle).toDouble())
            val lx = center.x + (baseRadius * cos(ang)).toFloat()
            val ly = center.y + (baseRadius * sin(ang)).toFloat()

            // Draw leaf petal
            drawCircle(
                color = theme.accentColor,
                radius = 7f,
                center = Offset(lx, ly)
            )
            drawCircle(
                color = theme.primaryColor,
                radius = 12f,
                center = Offset(lx, ly),
                alpha = 0.4f
            )
        }

        // Bio vine ring
        drawCircle(
            color = theme.secondaryColor.copy(alpha = 0.35f),
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
fun FlowerEnergyAnimation(
    theme: ChargingTheme,
    batteryPercentage: Int,
    isCharging: Boolean,
    settings: SettingsState,
    modifier: Modifier = Modifier
) {
    val speed = settings.effectiveSpeedMultiplier
    val infiniteTransition = rememberInfiniteTransition(label = "Flower")
    val bloomRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween((4500 / speed).toInt(), easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "bloom"
    )
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.9f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween((1500 / speed).toInt(), easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        val center = Offset(size.width / 2, size.height / 2)
        val baseRadius = size.minDimension * 0.28f

        // Central flower glow
        drawCircle(
            brush = createGlowRadialBrush(center, baseRadius * 1.4f, theme.primaryColor, settings.effectiveGlowIntensity),
            radius = baseRadius * 1.4f,
            center = center
        )

        // 8 Blooming geometric petals
        val petalCount = 8
        for (i in 0 until petalCount) {
            val ang = Math.toRadians((i * (360.0 / petalCount) + bloomRotation).toDouble())
            val petalRadius = baseRadius * 1.15f * pulseScale
            val px = center.x + (petalRadius * cos(ang)).toFloat()
            val py = center.y + (petalRadius * sin(ang)).toFloat()

            drawCircle(
                color = theme.primaryColor.copy(alpha = 0.35f),
                radius = baseRadius * 0.38f,
                center = Offset(px, py)
            )
            drawCircle(
                color = theme.secondaryColor,
                radius = baseRadius * 0.38f,
                center = Offset(px, py),
                style = Stroke(width = 2.5f)
            )
        }

        // Center ring
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
fun RainAnimation(
    theme: ChargingTheme,
    batteryPercentage: Int,
    isCharging: Boolean,
    settings: SettingsState,
    modifier: Modifier = Modifier
) {
    val speed = settings.effectiveSpeedMultiplier
    val infiniteTransition = rememberInfiniteTransition(label = "Rain")
    val rainTick by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween((900 / speed).toInt(), easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rainTick"
    )

    val rainDrops = remember {
        List(36) {
            AnimatedParticle(
                x = (Random.nextFloat() - 0.5f) * 320f,
                y = Random.nextFloat() * 400f,
                speed = Random.nextFloat() * 1.4f + 0.8f,
                radius = Random.nextFloat() * 12f + 14f,
                alpha = Random.nextFloat() * 0.6f + 0.4f
            )
        }
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        val center = Offset(size.width / 2, size.height / 2)
        val baseRadius = size.minDimension * 0.3f

        // Soft rain mist glow
        drawCircle(
            brush = createGlowRadialBrush(center, baseRadius * 1.3f, theme.primaryColor, settings.effectiveGlowIntensity),
            radius = baseRadius * 1.3f,
            center = center
        )

        // Vertical streaks of rain
        val activeDrops = (rainDrops.size * settings.effectiveParticleDensity).toInt().coerceIn(6, rainDrops.size)
        for (i in 0 until activeDrops) {
            val d = rainDrops[i]
            val progress = (rainTick * d.speed + i * 0.04f) % 1f
            val startY = center.y - 200f + progress * 400f
            val startX = center.x + d.x

            drawLine(
                color = theme.accentColor.copy(alpha = d.alpha * (1f - progress * 0.3f)),
                start = Offset(startX, startY),
                end = Offset(startX, startY + d.radius),
                strokeWidth = 2.5f,
                cap = StrokeCap.Round
            )
        }

        // Energy water ring
        drawCircle(
            color = theme.secondaryColor.copy(alpha = 0.25f),
            radius = baseRadius,
            center = center,
            style = Stroke(width = 6f)
        )

        drawArc(
            color = theme.primaryColor,
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
fun StormAnimation(
    theme: ChargingTheme,
    batteryPercentage: Int,
    isCharging: Boolean,
    settings: SettingsState,
    modifier: Modifier = Modifier
) {
    val speed = settings.effectiveSpeedMultiplier
    val infiniteTransition = rememberInfiniteTransition(label = "Storm")
    val stormPhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 10f,
        animationSpec = infiniteRepeatable(
            animation = tween((700 / speed).toInt(), easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "stormPhase"
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        val center = Offset(size.width / 2, size.height / 2)
        val baseRadius = size.minDimension * 0.3f

        // Storm cloud aura
        val flashAlpha = if (stormPhase.toInt() % 4 == 0) 0.6f else 0.25f
        drawCircle(
            brush = createGlowRadialBrush(center, baseRadius * 1.45f, theme.primaryColor, settings.effectiveGlowIntensity * flashAlpha),
            radius = baseRadius * 1.45f,
            center = center
        )

        // Random lightning strike when flash tick triggers
        if (stormPhase.toInt() % 3 == 0) {
            val path = Path()
            val startY = center.y - baseRadius * 1.3f
            path.moveTo(center.x, startY)
            var cy = startY
            var cx = center.x
            for (step in 1..4) {
                cy += baseRadius * 0.4f
                cx += (if (step % 2 == 0) 24f else -24f)
                path.lineTo(cx, cy)
            }
            drawPath(
                path = path,
                color = theme.accentColor,
                style = Stroke(width = 4f, cap = StrokeCap.Round)
            )
        }

        // Storm circular energy
        drawArc(
            color = theme.secondaryColor,
            startAngle = -90f,
            sweepAngle = (batteryPercentage / 100f) * 360f,
            useCenter = false,
            topLeft = Offset(center.x - baseRadius, center.y - baseRadius),
            size = Size(baseRadius * 2, baseRadius * 2),
            style = Stroke(width = 11f, cap = StrokeCap.Round)
        )
    }
}
