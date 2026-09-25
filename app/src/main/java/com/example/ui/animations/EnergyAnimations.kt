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
fun NeonRingAnimation(
    theme: ChargingTheme,
    batteryPercentage: Int,
    isCharging: Boolean,
    settings: SettingsState,
    modifier: Modifier = Modifier
) {
    val speed = settings.effectiveSpeedMultiplier
    val infiniteTransition = rememberInfiniteTransition(label = "NeonRing")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween((3600 / speed).toInt(), easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )
    val pulse by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween((1200 / speed).toInt(), easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    val particles = remember {
        List(24) {
            AnimatedParticle(
                x = 0f,
                y = 0f,
                speed = Random.nextFloat() * 1.5f + 0.8f,
                radius = Random.nextFloat() * 4f + 2f,
                alpha = Random.nextFloat() * 0.7f + 0.3f,
                angle = Random.nextFloat() * 360f,
                angularSpeed = (Random.nextFloat() - 0.5f) * 2f
            )
        }
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        val center = Offset(size.width / 2, size.height / 2)
        val baseRadius = size.minDimension * 0.32f
        val glowIntensity = settings.effectiveGlowIntensity

        // Outer glow
        drawCircle(
            brush = createGlowRadialBrush(center, baseRadius * 1.35f * pulse, theme.primaryColor, glowIntensity),
            radius = baseRadius * 1.35f * pulse,
            center = center
        )

        // Track ring
        drawCircle(
            color = theme.secondaryColor.copy(alpha = 0.2f),
            radius = baseRadius,
            center = center,
            style = Stroke(width = 6f)
        )

        // Progress Arc
        val sweepAngle = (batteryPercentage / 100f) * 360f
        drawArc(
            brush = Brush.sweepGradient(
                listOf(theme.primaryColor, theme.secondaryColor, theme.accentColor, theme.primaryColor),
                center = center
            ),
            startAngle = rotation - 90f,
            sweepAngle = sweepAngle,
            useCenter = false,
            topLeft = Offset(center.x - baseRadius, center.y - baseRadius),
            size = Size(baseRadius * 2, baseRadius * 2),
            style = Stroke(width = 12f * pulse, cap = StrokeCap.Round)
        )

        // Inner glowing ring
        drawCircle(
            color = theme.accentColor.copy(alpha = 0.5f * pulse),
            radius = baseRadius * 0.88f,
            center = center,
            style = Stroke(width = 3f)
        )

        // Energy particles orbiting around ring
        val activeParticleCount = (particles.size * settings.effectiveParticleDensity).toInt().coerceIn(4, particles.size)
        for (i in 0 until activeParticleCount) {
            val p = particles[i]
            val currentAngle = Math.toRadians((p.angle + rotation * p.speed).toDouble())
            val pRadius = baseRadius + (sin(currentAngle * 3) * 12f).toFloat()
            val px = center.x + (pRadius * cos(currentAngle)).toFloat()
            val py = center.y + (pRadius * sin(currentAngle)).toFloat()

            drawCircle(
                color = if (i % 2 == 0) theme.primaryColor else theme.accentColor,
                radius = p.radius,
                center = Offset(px, py),
                alpha = p.alpha * pulse.coerceIn(0.5f, 1f)
            )
        }
    }
}

@Composable
fun ElectricLightningAnimation(
    theme: ChargingTheme,
    batteryPercentage: Int,
    isCharging: Boolean,
    settings: SettingsState,
    modifier: Modifier = Modifier
) {
    val speed = settings.effectiveSpeedMultiplier
    val infiniteTransition = rememberInfiniteTransition(label = "ElectricLightning")
    val flashTick by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 10f,
        animationSpec = infiniteRepeatable(
            animation = tween((500 / speed).toInt(), easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "flashTick"
    )
    val ringRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween((5000 / speed).toInt(), easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "ringRotation"
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        val center = Offset(size.width / 2, size.height / 2)
        val baseRadius = size.minDimension * 0.3f

        // Central Electric Burst Glow
        drawCircle(
            brush = createGlowRadialBrush(center, baseRadius * 1.4f, theme.primaryColor, settings.effectiveGlowIntensity),
            radius = baseRadius * 1.4f,
            center = center
        )

        // Ambient sparks
        drawCircle(
            color = theme.primaryColor.copy(alpha = 0.25f),
            radius = baseRadius,
            center = center,
            style = Stroke(width = 4f)
        )

        // Procedural Lightning bolts converging to center
        val boltCount = if (settings.reduceMotion) 3 else 6
        val seed = flashTick.toInt()
        val rng = Random(seed)

        for (b in 0 until boltCount) {
            val startAngle = (b * (360.0 / boltCount) + ringRotation) * (PI / 180.0)
            val outerRadius = baseRadius * 1.35f
            var currX = center.x + (outerRadius * cos(startAngle)).toFloat()
            var currY = center.y + (outerRadius * sin(startAngle)).toFloat()

            val path = Path()
            path.moveTo(currX, currY)

            val segments = 5
            for (s in 1..segments) {
                val t = s.toFloat() / segments
                val targetRadius = outerRadius - (outerRadius - baseRadius * 0.75f) * t
                val jitterAngle = startAngle + (rng.nextFloat() - 0.5f) * 0.35f
                val nextX = center.x + (targetRadius * cos(jitterAngle)).toFloat()
                val nextY = center.y + (targetRadius * sin(jitterAngle)).toFloat()

                path.lineTo(nextX, nextY)
                currX = nextX
                currY = nextY
            }

            drawPath(
                path = path,
                color = theme.accentColor,
                style = Stroke(width = 3.5f, cap = StrokeCap.Round)
            )
            drawPath(
                path = path,
                color = theme.primaryColor,
                style = Stroke(width = 7f, cap = StrokeCap.Round),
                alpha = 0.5f
            )
        }

        // Percentage ring
        val sweepAngle = (batteryPercentage / 100f) * 360f
        drawArc(
            color = theme.primaryColor,
            startAngle = -90f,
            sweepAngle = sweepAngle,
            useCenter = false,
            topLeft = Offset(center.x - baseRadius, center.y - baseRadius),
            size = Size(baseRadius * 2, baseRadius * 2),
            style = Stroke(width = 8f, cap = StrokeCap.Round)
        )
    }
}

@Composable
fun PlasmaEnergyAnimation(
    theme: ChargingTheme,
    batteryPercentage: Int,
    isCharging: Boolean,
    settings: SettingsState,
    modifier: Modifier = Modifier
) {
    val speed = settings.effectiveSpeedMultiplier
    val infiniteTransition = rememberInfiniteTransition(label = "Plasma")
    val phase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2 * PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween((2800 / speed).toInt(), easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "phase"
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        val center = Offset(size.width / 2, size.height / 2)
        val coreRadius = size.minDimension * 0.28f

        // Plasma orb aura
        drawCircle(
            brush = createGlowRadialBrush(center, coreRadius * 1.5f, theme.secondaryColor, settings.effectiveGlowIntensity),
            radius = coreRadius * 1.5f,
            center = center
        )

        // Multiple undulating plasma arcs
        val arcLayers = 4
        for (layer in 0 until arcLayers) {
            val layerRadius = coreRadius * (0.85f + layer * 0.12f)
            val path = Path()
            val points = 36
            for (i in 0..points) {
                val theta = (i.toFloat() / points) * 2 * PI
                val wave = sin(theta * (3 + layer) + phase + layer * 1.5f) * (8f + layer * 4f)
                val r = layerRadius + wave.toFloat()
                val x = center.x + (r * cos(theta)).toFloat()
                val y = center.y + (r * sin(theta)).toFloat()
                if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
            }
            path.close()

            val color = when (layer % 3) {
                0 -> theme.primaryColor
                1 -> theme.secondaryColor
                else -> theme.accentColor
            }
            drawPath(
                path = path,
                color = color.copy(alpha = 0.75f - layer * 0.15f),
                style = Stroke(width = 4f + layer * 1.5f, cap = StrokeCap.Round)
            )
        }

        // Concentric charging progress
        drawArc(
            color = theme.accentColor,
            startAngle = -90f,
            sweepAngle = (batteryPercentage / 100f) * 360f,
            useCenter = false,
            topLeft = Offset(center.x - coreRadius * 1.25f, center.y - coreRadius * 1.25f),
            size = Size(coreRadius * 2.5f, coreRadius * 2.5f),
            style = Stroke(width = 6f, cap = StrokeCap.Round)
        )
    }
}

@Composable
fun EnergyCoreAnimation(
    theme: ChargingTheme,
    batteryPercentage: Int,
    isCharging: Boolean,
    settings: SettingsState,
    modifier: Modifier = Modifier
) {
    val speed = settings.effectiveSpeedMultiplier
    val infiniteTransition = rememberInfiniteTransition(label = "EnergyCore")
    val pulse by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween((1800 / speed).toInt(), easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulse"
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        val center = Offset(size.width / 2, size.height / 2)
        val maxRadius = size.minDimension * 0.42f
        val coreRadius = size.minDimension * 0.24f

        // Expanding shockwaves
        for (i in 0..2) {
            val waveProgress = (pulse + i * 0.33f) % 1.0f
            val currentRadius = coreRadius + (maxRadius - coreRadius) * waveProgress
            val alpha = (1.0f - waveProgress) * settings.effectiveGlowIntensity
            drawCircle(
                color = theme.primaryColor.copy(alpha = alpha.coerceIn(0f, 0.8f)),
                radius = currentRadius,
                center = center,
                style = Stroke(width = 6f * (1f - waveProgress) + 1f)
            )
        }

        // Central Reactor Core
        drawCircle(
            brush = createGlowRadialBrush(center, coreRadius * 1.2f, theme.secondaryColor, settings.effectiveGlowIntensity),
            radius = coreRadius * 1.2f,
            center = center
        )

        drawCircle(
            color = theme.primaryColor.copy(alpha = 0.4f),
            radius = coreRadius,
            center = center,
            style = Stroke(width = 8f)
        )

        drawArc(
            color = theme.accentColor,
            startAngle = -90f,
            sweepAngle = (batteryPercentage / 100f) * 360f,
            useCenter = false,
            topLeft = Offset(center.x - coreRadius, center.y - coreRadius),
            size = Size(coreRadius * 2, coreRadius * 2),
            style = Stroke(width = 10f, cap = StrokeCap.Round)
        )
    }
}

@Composable
fun ParticleExplosionAnimation(
    theme: ChargingTheme,
    batteryPercentage: Int,
    isCharging: Boolean,
    settings: SettingsState,
    modifier: Modifier = Modifier
) {
    val speed = settings.effectiveSpeedMultiplier
    val infiniteTransition = rememberInfiniteTransition(label = "ParticleExplosion")
    val cycle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween((2000 / speed).toInt(), easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "cycle"
    )

    val particles = remember {
        List(48) {
            AnimatedParticle(
                x = 0f,
                y = 0f,
                speed = Random.nextFloat() * 0.8f + 0.6f,
                radius = Random.nextFloat() * 4.5f + 1.5f,
                alpha = Random.nextFloat() * 0.8f + 0.2f,
                angle = Random.nextFloat() * 360f,
                distance = Random.nextFloat() * 0.6f + 0.4f
            )
        }
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        val center = Offset(size.width / 2, size.height / 2)
        val baseRadius = size.minDimension * 0.3f
        val maxSpread = size.minDimension * 0.48f

        // Luminous core
        drawCircle(
            brush = createGlowRadialBrush(center, baseRadius * 1.3f, theme.primaryColor, settings.effectiveGlowIntensity),
            radius = baseRadius * 1.3f,
            center = center
        )

        // Converging particles toward battery
        val activeCount = (particles.size * settings.effectiveParticleDensity).toInt().coerceIn(8, particles.size)
        for (i in 0 until activeCount) {
            val p = particles[i]
            val progress = (1f - ((cycle * p.speed) % 1f))
            val currentDist = baseRadius * 0.7f + (maxSpread - baseRadius * 0.7f) * progress * p.distance
            val rad = Math.toRadians(p.angle.toDouble())
            val px = center.x + (currentDist * cos(rad)).toFloat()
            val py = center.y + (currentDist * sin(rad)).toFloat()

            drawCircle(
                color = if (i % 2 == 0) theme.primaryColor else theme.accentColor,
                radius = p.radius * (0.5f + 0.5f * (1f - progress)),
                center = Offset(px, py),
                alpha = p.alpha * (0.3f + 0.7f * (1f - progress))
            )
        }

        // Percentage Ring
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
fun EnergyWaveAnimation(
    theme: ChargingTheme,
    batteryPercentage: Int,
    isCharging: Boolean,
    settings: SettingsState,
    modifier: Modifier = Modifier
) {
    val speed = settings.effectiveSpeedMultiplier
    val infiniteTransition = rememberInfiniteTransition(label = "EnergyWave")
    val waveOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween((2200 / speed).toInt(), easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "waveOffset"
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        val center = Offset(size.width / 2, size.height / 2)
        val baseRadius = size.minDimension * 0.28f
        val ringCount = 4

        for (r in 0 until ringCount) {
            val progress = (waveOffset + r.toFloat() / ringCount) % 1f
            val radius = baseRadius * 0.8f + (size.minDimension * 0.22f) * progress
            val alpha = (1f - progress) * settings.effectiveGlowIntensity

            drawCircle(
                color = theme.primaryColor.copy(alpha = alpha.coerceIn(0f, 0.7f)),
                radius = radius,
                center = center,
                style = Stroke(width = 5f * (1f - progress) + 2f)
            )
        }

        // Central battery circle
        drawCircle(
            color = theme.secondaryColor.copy(alpha = 0.3f),
            radius = baseRadius,
            center = center,
            style = Stroke(width = 6f)
        )

        drawArc(
            brush = Brush.sweepGradient(
                listOf(theme.primaryColor, theme.accentColor, theme.primaryColor),
                center = center
            ),
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
fun SoundWaveAnimation(
    theme: ChargingTheme,
    batteryPercentage: Int,
    isCharging: Boolean,
    settings: SettingsState,
    modifier: Modifier = Modifier
) {
    val speed = settings.effectiveSpeedMultiplier
    val infiniteTransition = rememberInfiniteTransition(label = "SoundWave")
    val phase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2 * PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween((1600 / speed).toInt(), easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "phase"
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        val center = Offset(size.width / 2, size.height / 2)
        val baseRadius = size.minDimension * 0.3f
        val barCount = 48

        // Glow
        drawCircle(
            brush = createGlowRadialBrush(center, baseRadius * 1.3f, theme.secondaryColor, settings.effectiveGlowIntensity),
            radius = baseRadius * 1.3f,
            center = center
        )

        // Radial Equalizer bars
        for (i in 0 until barCount) {
            val angle = (i.toFloat() / barCount) * 2 * PI
            val harmonic1 = sin(angle * 4 + phase)
            val harmonic2 = cos(angle * 2 - phase * 1.5f)
            val barHeight = ((harmonic1 + harmonic2).coerceIn(-1.0, 2.0) * 16f + 20f).toFloat()

            val innerX = center.x + (baseRadius * cos(angle)).toFloat()
            val innerY = center.y + (baseRadius * sin(angle)).toFloat()
            val outerX = center.x + ((baseRadius + barHeight) * cos(angle)).toFloat()
            val outerY = center.y + ((baseRadius + barHeight) * sin(angle)).toFloat()

            val color = if (i % 2 == 0) theme.primaryColor else theme.accentColor
            drawLine(
                color = color.copy(alpha = 0.85f),
                start = Offset(innerX, innerY),
                end = Offset(outerX, outerY),
                strokeWidth = 4f,
                cap = StrokeCap.Round
            )
        }

        // Inner progress ring
        drawArc(
            color = theme.accentColor,
            startAngle = -90f,
            sweepAngle = (batteryPercentage / 100f) * 360f,
            useCenter = false,
            topLeft = Offset(center.x - baseRadius * 0.85f, center.y - baseRadius * 0.85f),
            size = Size(baseRadius * 1.7f, baseRadius * 1.7f),
            style = Stroke(width = 6f, cap = StrokeCap.Round)
        )
    }
}

@Composable
fun RainbowEnergyAnimation(
    theme: ChargingTheme,
    batteryPercentage: Int,
    isCharging: Boolean,
    settings: SettingsState,
    modifier: Modifier = Modifier
) {
    val speed = settings.effectiveSpeedMultiplier
    val infiniteTransition = rememberInfiniteTransition(label = "Rainbow")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween((3200 / speed).toInt(), easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    val rainbowColors = listOf(
        Color(0xFFFF0055),
        Color(0xFFFF9900),
        Color(0xFFFFEE00),
        Color(0xFF00FF66),
        Color(0xFF00E5FF),
        Color(0xFF7000FF),
        Color(0xFFFF0055)
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        val center = Offset(size.width / 2, size.height / 2)
        val baseRadius = size.minDimension * 0.32f

        // Multi-chroma glowing sweep
        drawCircle(
            brush = Brush.sweepGradient(rainbowColors, center = center),
            radius = baseRadius * 1.15f,
            center = center,
            alpha = 0.25f * settings.effectiveGlowIntensity
        )

        // Rotating gradient ring
        drawArc(
            brush = Brush.sweepGradient(rainbowColors, center = center),
            startAngle = rotation,
            sweepAngle = 360f,
            useCenter = false,
            topLeft = Offset(center.x - baseRadius, center.y - baseRadius),
            size = Size(baseRadius * 2, baseRadius * 2),
            style = Stroke(width = 12f)
        )

        // Real Battery Fill Arc
        drawArc(
            color = Color.White,
            startAngle = -90f,
            sweepAngle = (batteryPercentage / 100f) * 360f,
            useCenter = false,
            topLeft = Offset(center.x - baseRadius * 0.85f, center.y - baseRadius * 0.85f),
            size = Size(baseRadius * 1.7f, baseRadius * 1.7f),
            style = Stroke(width = 6f, cap = StrokeCap.Round)
        )
    }
}

@Composable
fun QuantumAnimation(
    theme: ChargingTheme,
    batteryPercentage: Int,
    isCharging: Boolean,
    settings: SettingsState,
    modifier: Modifier = Modifier
) {
    val speed = settings.effectiveSpeedMultiplier
    val infiniteTransition = rememberInfiniteTransition(label = "Quantum")
    val orbitAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween((3000 / speed).toInt(), easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "orbit"
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        val center = Offset(size.width / 2, size.height / 2)
        val coreRadius = size.minDimension * 0.26f
        val orbitRadius = size.minDimension * 0.38f

        // Central Quantum Core Glow
        drawCircle(
            brush = createGlowRadialBrush(center, coreRadius * 1.3f, theme.primaryColor, settings.effectiveGlowIntensity),
            radius = coreRadius * 1.3f,
            center = center
        )

        // 3 Elliptical atomic orbits tilted at 0, 60, 120 degrees
        val orbitTilts = listOf(0.0, 60.0, 120.0)
        orbitTilts.forEachIndexed { idx, tiltDeg ->
            val tiltRad = Math.toRadians(tiltDeg)
            val orbitPath = Path()
            val steps = 60
            for (s in 0..steps) {
                val t = (s.toFloat() / steps) * 2 * PI
                val xOrig = orbitRadius * cos(t)
                val yOrig = orbitRadius * 0.45f * sin(t)

                // 2D Rotation by tilt
                val rotX = center.x + (xOrig * cos(tiltRad) - yOrig * sin(tiltRad)).toFloat()
                val rotY = center.y + (xOrig * sin(tiltRad) + yOrig * cos(tiltRad)).toFloat()
                if (s == 0) orbitPath.moveTo(rotX, rotY) else orbitPath.lineTo(rotX, rotY)
            }
            orbitPath.close()

            val color = when (idx) {
                0 -> theme.primaryColor
                1 -> theme.secondaryColor
                else -> theme.accentColor
            }
            drawPath(
                path = orbitPath,
                color = color.copy(alpha = 0.4f),
                style = Stroke(width = 3f)
            )

            // Orbiting electron
            val electronT = Math.toRadians((orbitAngle * (1.2f + idx * 0.3f)).toDouble())
            val exOrig = orbitRadius * cos(electronT)
            val eyOrig = orbitRadius * 0.45f * sin(electronT)
            val ex = center.x + (exOrig * cos(tiltRad) - eyOrig * sin(tiltRad)).toFloat()
            val ey = center.y + (exOrig * sin(tiltRad) + eyOrig * cos(tiltRad)).toFloat()

            drawCircle(
                color = Color.White,
                radius = 6f,
                center = Offset(ex, ey)
            )
            drawCircle(
                color = color,
                radius = 12f,
                center = Offset(ex, ey),
                alpha = 0.6f
            )
        }

        // Inner battery arc
        drawArc(
            color = theme.accentColor,
            startAngle = -90f,
            sweepAngle = (batteryPercentage / 100f) * 360f,
            useCenter = false,
            topLeft = Offset(center.x - coreRadius, center.y - coreRadius),
            size = Size(coreRadius * 2, coreRadius * 2),
            style = Stroke(width = 8f, cap = StrokeCap.Round)
        )
    }
}
