package com.example.ui.animations

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import com.example.model.ChargingTheme
import com.example.model.SettingsState

@Composable
fun ThemeAnimationRenderer(
    theme: ChargingTheme,
    batteryPercentage: Int,
    isCharging: Boolean,
    settings: SettingsState,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        // Dispatch to corresponding theme implementation
        when (theme) {
            ChargingTheme.NEON_RING -> NeonRingAnimation(theme, batteryPercentage, isCharging, settings)
            ChargingTheme.ELECTRIC_LIGHTNING -> ElectricLightningAnimation(theme, batteryPercentage, isCharging, settings)
            ChargingTheme.PLASMA_ENERGY -> PlasmaEnergyAnimation(theme, batteryPercentage, isCharging, settings)
            ChargingTheme.GALAXY -> GalaxyAnimation(theme, batteryPercentage, isCharging, settings)
            ChargingTheme.FIRE -> FireAnimation(theme, batteryPercentage, isCharging, settings)
            ChargingTheme.ICE -> IceAnimation(theme, batteryPercentage, isCharging, settings)
            ChargingTheme.WATER_LIQUID -> WaterLiquidAnimation(theme, batteryPercentage, isCharging, settings)
            ChargingTheme.OCEAN -> OceanAnimation(theme, batteryPercentage, isCharging, settings)
            ChargingTheme.NATURE -> NatureAnimation(theme, batteryPercentage, isCharging, settings)
            ChargingTheme.FLOWER_ENERGY -> FlowerEnergyAnimation(theme, batteryPercentage, isCharging, settings)
            ChargingTheme.CYBER -> CyberAnimation(theme, batteryPercentage, isCharging, settings)
            ChargingTheme.MATRIX -> MatrixAnimation(theme, batteryPercentage, isCharging, settings)
            ChargingTheme.HOLOGRAM -> HologramAnimation(theme, batteryPercentage, isCharging, settings)
            ChargingTheme.ENERGY_CORE -> EnergyCoreAnimation(theme, batteryPercentage, isCharging, settings)
            ChargingTheme.AURORA -> AuroraAnimation(theme, batteryPercentage, isCharging, settings)
            ChargingTheme.COSMIC_PORTAL -> CosmicPortalAnimation(theme, batteryPercentage, isCharging, settings)
            ChargingTheme.TECH_BATTERY -> TechBatteryAnimation(theme, batteryPercentage, isCharging, settings)
            ChargingTheme.LASER -> LaserAnimation(theme, batteryPercentage, isCharging, settings)
            ChargingTheme.PARTICLE_EXPLOSION -> ParticleExplosionAnimation(theme, batteryPercentage, isCharging, settings)
            ChargingTheme.ENERGY_WAVE -> EnergyWaveAnimation(theme, batteryPercentage, isCharging, settings)
            ChargingTheme.SOUND_WAVE -> SoundWaveAnimation(theme, batteryPercentage, isCharging, settings)
            ChargingTheme.DIGITAL_CLOCK -> DigitalClockAnimation(theme, batteryPercentage, isCharging, settings)
            ChargingTheme.MINIMAL_GLOW -> MinimalGlowAnimation(theme, batteryPercentage, isCharging, settings)
            ChargingTheme.RAIN -> RainAnimation(theme, batteryPercentage, isCharging, settings)
            ChargingTheme.STORM -> StormAnimation(theme, batteryPercentage, isCharging, settings)
            ChargingTheme.SUN_ENERGY -> SunEnergyAnimation(theme, batteryPercentage, isCharging, settings)
            ChargingTheme.MOONLIGHT -> MoonlightAnimation(theme, batteryPercentage, isCharging, settings)
            ChargingTheme.TECH_GRID -> TechGridAnimation(theme, batteryPercentage, isCharging, settings)
            ChargingTheme.RAINBOW_ENERGY -> RainbowEnergyAnimation(theme, batteryPercentage, isCharging, settings)
            ChargingTheme.QUANTUM -> QuantumAnimation(theme, batteryPercentage, isCharging, settings)
        }

        // Special Battery Full Overlay when 100%
        if (settings.batteryFullAnimationEnabled && batteryPercentage >= 100) {
            BatteryFullBurstOverlay()
        }

        // Low Battery Pulse Overlay if enabled and <= 20% and not charging
        if (settings.lowBatteryAnimationEnabled && batteryPercentage <= 20 && !isCharging) {
            LowBatteryPulseOverlay()
        }
    }
}

@Composable
private fun BatteryFullBurstOverlay() {
    val infiniteTransition = rememberInfiniteTransition(label = "FullBurst")
    val wave by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "wave"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val center = Offset(size.width / 2, size.height / 2)
        val maxRadius = size.minDimension * 0.45f
        val currentRadius = size.minDimension * 0.25f + (maxRadius - size.minDimension * 0.25f) * wave
        val alpha = (1f - wave) * 0.6f

        drawCircle(
            color = Color(0xFFFFD700).copy(alpha = alpha),
            radius = currentRadius,
            center = center,
            style = Stroke(width = 4f * (1f - wave) + 1f)
        )
    }
}

@Composable
private fun LowBatteryPulseOverlay() {
    val infiniteTransition = rememberInfiniteTransition(label = "LowBattery")
    val pulse by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.5f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val center = Offset(size.width / 2, size.height / 2)
        drawCircle(
            color = Color(0xFFFF3333).copy(alpha = pulse),
            radius = size.minDimension * 0.38f,
            center = center,
            style = Stroke(width = 3f)
        )
    }
}
