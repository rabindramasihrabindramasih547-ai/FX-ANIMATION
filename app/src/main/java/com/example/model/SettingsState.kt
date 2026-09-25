package com.example.model

enum class AnimationSpeed(val multiplier: Float, val label: String) {
    SLOW(0.65f, "Slow"),
    NORMAL(1.0f, "Normal"),
    FAST(1.45f, "Fast")
}

enum class EffectIntensity(val factor: Float, val label: String) {
    LOW(0.6f, "Low"),
    MEDIUM(1.0f, "Medium"),
    HIGH(1.35f, "High"),
    ULTRA(1.75f, "Ultra")
}

enum class ParticleDensity(val countMultiplier: Float, val label: String) {
    LOW(0.5f, "Low"),
    MEDIUM(1.0f, "Medium"),
    HIGH(1.7f, "High")
}

enum class ComponentSize(val scale: Float, val label: String) {
    SMALL(0.85f, "Small"),
    MEDIUM(1.0f, "Medium"),
    LARGE(1.2f, "Large")
}

enum class SoundStyle(val displayName: String) {
    SOFT("Soft Chime"),
    ELECTRIC("Electric Arc"),
    FUTURISTIC("Futuristic Synth"),
    ENERGY("Energy Pulse"),
    MINIMAL("Minimal Click")
}

enum class PerformanceProfile(val displayName: String, val description: String) {
    BATTERY_SAVER("Battery Saver", "Minimal particles, reduced glow, power efficient"),
    BALANCED("Balanced", "Balanced visuals and battery conservation"),
    ULTRA("Ultra Quality", "Maximum particles, rich shaders, high-fidelity")
}

data class SettingsState(
    val selectedThemeId: String = ChargingTheme.DEFAULT.id,
    val animationSpeed: AnimationSpeed = AnimationSpeed.NORMAL,
    val effectIntensity: EffectIntensity = EffectIntensity.HIGH,
    val particleDensity: ParticleDensity = ParticleDensity.MEDIUM,
    val glowIntensity: Float = 0.85f,
    val batterySize: ComponentSize = ComponentSize.MEDIUM,
    val percentageSize: ComponentSize = ComponentSize.MEDIUM,
    val showClock: Boolean = true,
    val showChargingText: Boolean = true,
    val showChargingType: Boolean = true,
    val soundEnabled: Boolean = true,
    val soundStyle: SoundStyle = SoundStyle.FUTURISTIC,
    val vibrationEnabled: Boolean = true,
    val notificationEnabled: Boolean = true,
    val autoStartAnimation: Boolean = true,
    val keepScreenOn: Boolean = true,
    val randomThemeMode: Boolean = false,
    val dailyThemeMode: Boolean = false,
    val batteryFullAnimationEnabled: Boolean = true,
    val lowBatteryAnimationEnabled: Boolean = true,
    val performanceProfile: PerformanceProfile = PerformanceProfile.BALANCED,
    val reduceMotion: Boolean = false,
    val favoriteThemeIds: Set<String> = setOf(
        ChargingTheme.NEON_RING.id,
        ChargingTheme.ELECTRIC_LIGHTNING.id,
        ChargingTheme.PLASMA_ENERGY.id,
        ChargingTheme.CYBER.id
    )
) {
    val effectiveSpeedMultiplier: Float
        get() = if (reduceMotion) 0.5f else animationSpeed.multiplier

    val effectiveParticleDensity: Float
        get() = when {
            reduceMotion -> 0.3f
            performanceProfile == PerformanceProfile.BATTERY_SAVER -> 0.4f
            performanceProfile == PerformanceProfile.BALANCED -> particleDensity.countMultiplier
            else -> particleDensity.countMultiplier * 1.3f
        }

    val effectiveGlowIntensity: Float
        get() = when (performanceProfile) {
            PerformanceProfile.BATTERY_SAVER -> glowIntensity * 0.4f
            PerformanceProfile.BALANCED -> glowIntensity
            PerformanceProfile.ULTRA -> glowIntensity * 1.25f
        }
}
