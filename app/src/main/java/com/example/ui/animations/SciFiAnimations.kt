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
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.RoundRect
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
fun CyberAnimation(
    theme: ChargingTheme,
    batteryPercentage: Int,
    isCharging: Boolean,
    settings: SettingsState,
    modifier: Modifier = Modifier
) {
    val speed = settings.effectiveSpeedMultiplier
    val infiniteTransition = rememberInfiniteTransition(label = "Cyber")
    val hudRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween((5000 / speed).toInt(), easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "hudRot"
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        val center = Offset(size.width / 2, size.height / 2)
        val baseRadius = size.minDimension * 0.3f

        // Outer HUD Reticle Glow
        drawCircle(
            brush = createGlowRadialBrush(center, baseRadius * 1.35f, theme.primaryColor, settings.effectiveGlowIntensity),
            radius = baseRadius * 1.35f,
            center = center
        )

        // 4 HUD Corner brackets
        val bracketDist = baseRadius * 1.22f
        val bracketLen = 28f
        val angles = listOf(45.0, 135.0, 225.0, 315.0)
        angles.forEach { deg ->
            val rad = Math.toRadians(deg + hudRotation * 0.2)
            val bx = center.x + (bracketDist * cos(rad)).toFloat()
            val by = center.y + (bracketDist * sin(rad)).toFloat()
            drawCircle(
                color = theme.accentColor,
                radius = 3.5f,
                center = Offset(bx, by)
            )
            drawLine(
                color = theme.primaryColor,
                start = Offset(bx - bracketLen / 2, by),
                end = Offset(bx + bracketLen / 2, by),
                strokeWidth = 2f
            )
        }

        // Segmented HUD outer track
        val segmentCount = 24
        for (i in 0 until segmentCount) {
            val ang = Math.toRadians((i * (360.0 / segmentCount) + hudRotation).toDouble())
            val startDist = baseRadius * 1.05f
            val endDist = baseRadius * 1.12f
            drawLine(
                color = theme.primaryColor.copy(alpha = 0.5f),
                start = Offset(center.x + (startDist * cos(ang)).toFloat(), center.y + (startDist * sin(ang)).toFloat()),
                end = Offset(center.x + (endDist * cos(ang)).toFloat(), center.y + (endDist * sin(ang)).toFloat()),
                strokeWidth = 2.5f
            )
        }

        // Inner solid charging ring
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
fun MatrixAnimation(
    theme: ChargingTheme,
    batteryPercentage: Int,
    isCharging: Boolean,
    settings: SettingsState,
    modifier: Modifier = Modifier
) {
    val speed = settings.effectiveSpeedMultiplier
    val infiniteTransition = rememberInfiniteTransition(label = "Matrix")
    val matrixTick by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween((1200 / speed).toInt(), easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "matrixTick"
    )

    val streamColumns = remember {
        List(18) {
            AnimatedParticle(
                x = (it - 9) * 18f,
                y = 0f,
                speed = Random.nextFloat() * 1.2f + 0.6f,
                radius = Random.nextFloat() * 8f + 6f,
                alpha = Random.nextFloat() * 0.7f + 0.3f
            )
        }
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        val center = Offset(size.width / 2, size.height / 2)
        val baseRadius = size.minDimension * 0.3f

        // Matrix phosphorescent green glow
        drawCircle(
            brush = createGlowRadialBrush(center, baseRadius * 1.4f, theme.primaryColor, settings.effectiveGlowIntensity),
            radius = baseRadius * 1.4f,
            center = center
        )

        // Cascading digital particles
        val activeColumns = (streamColumns.size * settings.effectiveParticleDensity).toInt().coerceIn(6, streamColumns.size)
        for (i in 0 until activeColumns) {
            val col = streamColumns[i]
            val progress = (matrixTick * col.speed + i * 0.08f) % 1f
            val py = center.y - 180f + progress * 360f
            val px = center.x + col.x

            // Digital binary block
            drawRect(
                color = theme.accentColor,
                topLeft = Offset(px - 3f, py),
                size = Size(6f, col.radius)
            )
            drawRect(
                color = theme.primaryColor.copy(alpha = 0.4f),
                topLeft = Offset(px - 3f, py - 20f),
                size = Size(6f, 16f)
            )
        }

        // Central digital ring
        drawCircle(
            color = theme.secondaryColor.copy(alpha = 0.4f),
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
fun HologramAnimation(
    theme: ChargingTheme,
    batteryPercentage: Int,
    isCharging: Boolean,
    settings: SettingsState,
    modifier: Modifier = Modifier
) {
    val speed = settings.effectiveSpeedMultiplier
    val infiniteTransition = rememberInfiniteTransition(label = "Hologram")
    val scanY by infiniteTransition.animateFloat(
        initialValue = -1f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween((2000 / speed).toInt(), easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "scanY"
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        val center = Offset(size.width / 2, size.height / 2)
        val baseRadius = size.minDimension * 0.3f

        // Holographic cyan field
        drawCircle(
            brush = createGlowRadialBrush(center, baseRadius * 1.35f, theme.primaryColor, settings.effectiveGlowIntensity),
            radius = baseRadius * 1.35f,
            center = center
        )

        // Hologram horizontal raster scanlines
        val scanlineCount = 14
        for (i in 0 until scanlineCount) {
            val yOffset = (i - scanlineCount / 2) * (baseRadius * 1.6f / scanlineCount)
            val lineY = center.y + yOffset
            val halfW = baseRadius * 0.9f
            drawLine(
                color = theme.primaryColor.copy(alpha = 0.2f),
                start = Offset(center.x - halfW, lineY),
                end = Offset(center.x + halfW, lineY),
                strokeWidth = 1.5f
            )
        }

        // Active laser scanning bar
        val beamY = center.y + scanY * baseRadius * 0.85f
        drawLine(
            color = theme.accentColor,
            start = Offset(center.x - baseRadius * 0.85f, beamY),
            end = Offset(center.x + baseRadius * 0.85f, beamY),
            strokeWidth = 3.5f
        )

        // Holographic Outer Ring
        drawArc(
            color = theme.secondaryColor.copy(alpha = 0.5f),
            startAngle = 0f,
            sweepAngle = 360f,
            useCenter = false,
            topLeft = Offset(center.x - baseRadius, center.y - baseRadius),
            size = Size(baseRadius * 2, baseRadius * 2),
            style = Stroke(width = 4f)
        )

        drawArc(
            color = theme.primaryColor,
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
fun TechBatteryAnimation(
    theme: ChargingTheme,
    batteryPercentage: Int,
    isCharging: Boolean,
    settings: SettingsState,
    modifier: Modifier = Modifier
) {
    val speed = settings.effectiveSpeedMultiplier
    val infiniteTransition = rememberInfiniteTransition(label = "TechBattery")
    val pulse by infiniteTransition.animateFloat(
        initialValue = 0.7f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween((1400 / speed).toInt(), easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        val center = Offset(size.width / 2, size.height / 2)
        val rectWidth = size.minDimension * 0.44f
        val rectHeight = size.minDimension * 0.62f
        val left = center.x - rectWidth / 2
        val top = center.y - rectHeight / 2

        // Glow around battery housing
        drawRoundRect(
            color = theme.primaryColor.copy(alpha = 0.2f * settings.effectiveGlowIntensity),
            topLeft = Offset(left - 12f, top - 12f),
            size = Size(rectWidth + 24f, rectHeight + 24f),
            cornerRadius = CornerRadius(24f, 24f)
        )

        // Outer battery chassis outline
        drawRoundRect(
            color = theme.primaryColor,
            topLeft = Offset(left, top),
            size = Size(rectWidth, rectHeight),
            cornerRadius = CornerRadius(20f, 20f),
            style = Stroke(width = 6f)
        )

        // Positive battery terminal on top
        drawRoundRect(
            color = theme.primaryColor,
            topLeft = Offset(center.x - 24f, top - 14f),
            size = Size(48f, 14f),
            cornerRadius = CornerRadius(6f, 6f)
        )

        // 5 Battery segments filling up proportionally
        val segmentCount = 5
        val segHeight = (rectHeight - 36f) / segmentCount
        val filledSegments = ((batteryPercentage / 100f) * segmentCount).toInt()

        for (s in 0 until segmentCount) {
            val segIndexFromBottom = segmentCount - 1 - s
            val segTop = top + 18f + (segIndexFromBottom * segHeight)
            val isFilled = s < filledSegments
            val isCurrentCharging = s == filledSegments && isCharging

            val segColor = when {
                isFilled -> theme.accentColor
                isCurrentCharging -> theme.primaryColor.copy(alpha = pulse)
                else -> theme.secondaryColor.copy(alpha = 0.2f)
            }

            drawRoundRect(
                color = segColor,
                topLeft = Offset(left + 14f, segTop + 4f),
                size = Size(rectWidth - 28f, segHeight - 8f),
                cornerRadius = CornerRadius(8f, 8f)
            )
        }
    }
}

@Composable
fun LaserAnimation(
    theme: ChargingTheme,
    batteryPercentage: Int,
    isCharging: Boolean,
    settings: SettingsState,
    modifier: Modifier = Modifier
) {
    val speed = settings.effectiveSpeedMultiplier
    val infiniteTransition = rememberInfiniteTransition(label = "Laser")
    val laserSweep by infiniteTransition.animateFloat(
        initialValue = -1f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween((1600 / speed).toInt(), easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "laserSweep"
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        val center = Offset(size.width / 2, size.height / 2)
        val baseRadius = size.minDimension * 0.3f

        // High-energy laser core glow
        drawCircle(
            brush = createGlowRadialBrush(center, baseRadius * 1.35f, theme.primaryColor, settings.effectiveGlowIntensity),
            radius = baseRadius * 1.35f,
            center = center
        )

        // Twin horizontal laser beams
        val yOffset = laserSweep * baseRadius * 0.7f
        val laserY1 = center.y + yOffset
        val laserY2 = center.y - yOffset

        // Red/Crimson High Intensity Beams
        listOf(laserY1, laserY2).forEach { ly ->
            drawLine(
                color = theme.accentColor,
                start = Offset(center.x - baseRadius * 1.25f, ly),
                end = Offset(center.x + baseRadius * 1.25f, ly),
                strokeWidth = 3f,
                cap = StrokeCap.Round
            )
            drawLine(
                color = theme.primaryColor.copy(alpha = 0.5f),
                start = Offset(center.x - baseRadius * 1.25f, ly),
                end = Offset(center.x + baseRadius * 1.25f, ly),
                strokeWidth = 8f,
                cap = StrokeCap.Round
            )
        }

        // Circular containment barrier
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
            style = Stroke(width = 9f, cap = StrokeCap.Round)
        )
    }
}

@Composable
fun TechGridAnimation(
    theme: ChargingTheme,
    batteryPercentage: Int,
    isCharging: Boolean,
    settings: SettingsState,
    modifier: Modifier = Modifier
) {
    val speed = settings.effectiveSpeedMultiplier
    val infiniteTransition = rememberInfiniteTransition(label = "TechGrid")
    val gridOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween((2000 / speed).toInt(), easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "gridOffset"
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        val center = Offset(size.width / 2, size.height / 2)
        val baseRadius = size.minDimension * 0.28f

        // Grid aura
        drawCircle(
            brush = createGlowRadialBrush(center, baseRadius * 1.4f, theme.primaryColor, settings.effectiveGlowIntensity),
            radius = baseRadius * 1.4f,
            center = center
        )

        // 3D Perspective horizontal grid lines below center
        val horizonY = center.y + 40f
        val lineCount = 7
        for (i in 0 until lineCount) {
            val progress = (gridOffset + i.toFloat() / lineCount) % 1f
            val lineY = horizonY + (progress * progress) * (size.height - horizonY)
            val spread = 40f + progress * (size.width * 0.5f)
            drawLine(
                color = theme.secondaryColor.copy(alpha = 0.4f * progress),
                start = Offset(center.x - spread, lineY),
                end = Offset(center.x + spread, lineY),
                strokeWidth = 2f
            )
        }

        // Perspective vanishing lines
        val vLines = 9
        for (v in 0 until vLines) {
            val bottomX = (v.toFloat() / (vLines - 1)) * size.width
            drawLine(
                color = theme.secondaryColor.copy(alpha = 0.25f),
                start = Offset(center.x, horizonY),
                end = Offset(bottomX, size.height),
                strokeWidth = 1.5f
            )
        }

        // Floating Neon Battery Ring
        drawCircle(
            color = theme.primaryColor.copy(alpha = 0.35f),
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
