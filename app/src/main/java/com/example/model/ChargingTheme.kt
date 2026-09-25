package com.example.model

import androidx.compose.ui.graphics.Color

enum class ThemeCategory(val title: String) {
    ENERGY("Energy & Neon"),
    ELEMENTAL("Nature & Elements"),
    SCI_FI("Cyber & Tech"),
    COSMIC("Cosmic & Space"),
    MINIMAL("Minimal & Clock")
}

enum class ChargingTheme(
    val id: String,
    val themeName: String,
    val category: ThemeCategory,
    val description: String,
    val primaryColor: Color,
    val secondaryColor: Color,
    val accentColor: Color
) {
    NEON_RING(
        id = "neon_ring",
        themeName = "Neon Ring",
        category = ThemeCategory.ENERGY,
        description = "Large glowing circular ring with cyan and purple energy rotation",
        primaryColor = Color(0xFF00E5FF),
        secondaryColor = Color(0xFF9D00FF),
        accentColor = Color(0xFF00FF88)
    ),
    ELECTRIC_LIGHTNING(
        id = "electric_lightning",
        themeName = "Electric Lightning",
        category = ThemeCategory.ENERGY,
        description = "Dynamic lightning bolts and electric flashes surging toward battery",
        primaryColor = Color(0xFF00D2FF),
        secondaryColor = Color(0xFF3A7BD5),
        accentColor = Color(0xFFFFFFFF)
    ),
    PLASMA_ENERGY(
        id = "plasma_energy",
        themeName = "Plasma Energy",
        category = ThemeCategory.ENERGY,
        description = "Pulsating plasma sphere with orbiting electric energy arcs",
        primaryColor = Color(0xFFD400FF),
        secondaryColor = Color(0xFF00F0FF),
        accentColor = Color(0xFFFF007F)
    ),
    GALAXY(
        id = "galaxy",
        themeName = "Galaxy",
        category = ThemeCategory.COSMIC,
        description = "Spinning spiral galaxy with deep nebula dust and orbiting stars",
        primaryColor = Color(0xFF7928CA),
        secondaryColor = Color(0xFF4338CA),
        accentColor = Color(0xFFFF4081)
    ),
    FIRE(
        id = "fire",
        themeName = "Fire Flame",
        category = ThemeCategory.ELEMENTAL,
        description = "Blazing flame aura with rising ember sparks and fiery heat waves",
        primaryColor = Color(0xFFFF5722),
        secondaryColor = Color(0xFFFF9800),
        accentColor = Color(0xFFFFEB3B)
    ),
    ICE(
        id = "ice",
        themeName = "Ice Crystal",
        category = ThemeCategory.ELEMENTAL,
        description = "Sub-zero frozen crystal rings with floating frost snowflakes",
        primaryColor = Color(0xFF80D8FF),
        secondaryColor = Color(0xFF00B0FF),
        accentColor = Color(0xFFE0F7FA)
    ),
    WATER_LIQUID(
        id = "water_liquid",
        themeName = "Liquid Wave",
        category = ThemeCategory.ELEMENTAL,
        description = "Organic fluid surge with rhythmic liquid ripples and bubbles",
        primaryColor = Color(0xFF00B4D8),
        secondaryColor = Color(0xFF0077B6),
        accentColor = Color(0xFF90E0EF)
    ),
    OCEAN(
        id = "ocean",
        themeName = "Deep Ocean",
        category = ThemeCategory.ELEMENTAL,
        description = "Bioluminescent deep sea aura with ascending bubble streams",
        primaryColor = Color(0xFF03045E),
        secondaryColor = Color(0xFF0077B6),
        accentColor = Color(0xFF00F5D4)
    ),
    NATURE(
        id = "nature",
        themeName = "Nature Leaf",
        category = ThemeCategory.ELEMENTAL,
        description = "Bio-electric emerald vines with drifting forest pollen particles",
        primaryColor = Color(0xFF00E676),
        secondaryColor = Color(0xFF1B5E20),
        accentColor = Color(0xFF76FF03)
    ),
    FLOWER_ENERGY(
        id = "flower_energy",
        themeName = "Flower Energy",
        category = ThemeCategory.ELEMENTAL,
        description = "Blooming holographic geometric petals radiating bio-charge",
        primaryColor = Color(0xFFFF4081),
        secondaryColor = Color(0xFFE040FB),
        accentColor = Color(0xFFFFD54F)
    ),
    CYBER(
        id = "cyber",
        themeName = "Cyberpunk HUD",
        category = ThemeCategory.SCI_FI,
        description = "Futuristic tactical HUD with pulsing circuit traces and reticles",
        primaryColor = Color(0xFF00F5FF),
        secondaryColor = Color(0xFF7000FF),
        accentColor = Color(0xFFFF0055)
    ),
    MATRIX(
        id = "matrix",
        themeName = "Digital Matrix",
        category = ThemeCategory.SCI_FI,
        description = "Vertical cascading cyber streams with luminous green hex pulses",
        primaryColor = Color(0xFF00FF41),
        secondaryColor = Color(0xFF008F11),
        accentColor = Color(0xFF66FF66)
    ),
    HOLOGRAM(
        id = "hologram",
        themeName = "Hologram 3D",
        category = ThemeCategory.SCI_FI,
        description = "3D holographic wireframe cylinder with scanning laser lines",
        primaryColor = Color(0xFF00FFFF),
        secondaryColor = Color(0xFF0088FF),
        accentColor = Color(0xFFE1F5FE)
    ),
    ENERGY_CORE(
        id = "energy_core",
        themeName = "Energy Core",
        category = ThemeCategory.ENERGY,
        description = "Superheated reactor core releasing high-density shockwave rings",
        primaryColor = Color(0xFFFF3366),
        secondaryColor = Color(0xFFFF6600),
        accentColor = Color(0xFFFFFF00)
    ),
    AURORA(
        id = "aurora",
        themeName = "Aurora Borealis",
        category = ThemeCategory.COSMIC,
        description = "Silky undulating ribbons of northern lights beneath starry skies",
        primaryColor = Color(0xFF00FFA3),
        secondaryColor = Color(0xFF00B8D4),
        accentColor = Color(0xFFD500F9)
    ),
    COSMIC_PORTAL(
        id = "cosmic_portal",
        themeName = "Cosmic Portal",
        category = ThemeCategory.COSMIC,
        description = "Wormhole vortex pulling cosmic stardust into the singularity",
        primaryColor = Color(0xFF6200EA),
        secondaryColor = Color(0xFF304FFE),
        accentColor = Color(0xFF00E5FF)
    ),
    TECH_BATTERY(
        id = "tech_battery",
        themeName = "Tech Battery Cell",
        category = ThemeCategory.SCI_FI,
        description = "Detailed cybernetic cell segments illuminating in sync with power",
        primaryColor = Color(0xFF00E676),
        secondaryColor = Color(0xFF00B0FF),
        accentColor = Color(0xFFFFFFFF)
    ),
    LASER(
        id = "laser",
        themeName = "Laser Sweep",
        category = ThemeCategory.SCI_FI,
        description = "High-energy twin laser beams oscillating across charge telemetry",
        primaryColor = Color(0xFFFF1744),
        secondaryColor = Color(0xFFFF5252),
        accentColor = Color(0xFF00E5FF)
    ),
    PARTICLE_EXPLOSION(
        id = "particle_explosion",
        themeName = "Particle Implosion",
        category = ThemeCategory.ENERGY,
        description = "Dense swarm of luminous sparks converging into a central charge burst",
        primaryColor = Color(0xFFFFD600),
        secondaryColor = Color(0xFFFF6D00),
        accentColor = Color(0xFFFFFFFF)
    ),
    ENERGY_WAVE(
        id = "energy_wave",
        themeName = "Energy Wave",
        category = ThemeCategory.ENERGY,
        description = "Concentric sonar-like waves rippling outwards from the battery core",
        primaryColor = Color(0xFF2979FF),
        secondaryColor = Color(0xFF00E5FF),
        accentColor = Color(0xFFB388FF)
    ),
    SOUND_WAVE(
        id = "sound_wave",
        themeName = "Audio Spectrum",
        category = ThemeCategory.ENERGY,
        description = "Circular rhythmic equalizer bars vibrating with electrical tempo",
        primaryColor = Color(0xFFFF007F),
        secondaryColor = Color(0xFF7928CA),
        accentColor = Color(0xFF00F5FF)
    ),
    DIGITAL_CLOCK(
        id = "digital_clock",
        themeName = "Digital Clock HUD",
        category = ThemeCategory.MINIMAL,
        description = "Bold digital typography surrounded by precision grid telemetry",
        primaryColor = Color(0xFF00E5FF),
        secondaryColor = Color(0xFF37474F),
        accentColor = Color(0xFF76FF03)
    ),
    MINIMAL_GLOW(
        id = "minimal_glow",
        themeName = "Minimal Glow",
        category = ThemeCategory.MINIMAL,
        description = "Pure AMOLED stealth black with an elegant breathing ring",
        primaryColor = Color(0xFFFFFFFF),
        secondaryColor = Color(0xFF616161),
        accentColor = Color(0xFF00E5FF)
    ),
    RAIN(
        id = "rain",
        themeName = "Neon Rain",
        category = ThemeCategory.ELEMENTAL,
        description = "Atmospheric cyber rainfall with soft electric ripples",
        primaryColor = Color(0xFF4FC3F7),
        secondaryColor = Color(0xFF0288D1),
        accentColor = Color(0xFFB3E5FC)
    ),
    STORM(
        id = "storm",
        themeName = "Thunder Storm",
        category = ThemeCategory.ELEMENTAL,
        description = "Heavy stormy ambience with thunderbolts illuminating dark clouds",
        primaryColor = Color(0xFF7C4DFF),
        secondaryColor = Color(0xFF311B92),
        accentColor = Color(0xFFFFEA00)
    ),
    SUN_ENERGY(
        id = "sun_energy",
        themeName = "Solar Flare",
        category = ThemeCategory.COSMIC,
        description = "Radiant sun corona with rotating solar prominences and warm rays",
        primaryColor = Color(0xFFFF9100),
        secondaryColor = Color(0xFFFF3D00),
        accentColor = Color(0xFFFFEA00)
    ),
    MOONLIGHT(
        id = "moonlight",
        themeName = "Moonlight Serenade",
        category = ThemeCategory.COSMIC,
        description = "Serene lunar crescent with gentle twinkling stellar constellation",
        primaryColor = Color(0xFFE0E0E0),
        secondaryColor = Color(0xFF5C6BC0),
        accentColor = Color(0xFF9FA8DA)
    ),
    TECH_GRID(
        id = "tech_grid",
        themeName = "Cyber Grid 3D",
        category = ThemeCategory.SCI_FI,
        description = "Retro-futuristic perspective grid stretching endlessly towards horizon",
        primaryColor = Color(0xFFF50057),
        secondaryColor = Color(0xFF651FFF),
        accentColor = Color(0xFF00E5FF)
    ),
    RAINBOW_ENERGY(
        id = "rainbow_energy",
        themeName = "Chroma Rainbow",
        category = ThemeCategory.ENERGY,
        description = "Continuous fluid chromatic spectrum cycling across energy bands",
        primaryColor = Color(0xFFFF0055),
        secondaryColor = Color(0xFF00E5FF),
        accentColor = Color(0xFFFFFF00)
    ),
    QUANTUM(
        id = "quantum",
        themeName = "Quantum Orbitals",
        category = ThemeCategory.ENERGY,
        description = "Multi-axis Bohr-style atomic electron orbits around quantum core",
        primaryColor = Color(0xFF00F0FF),
        secondaryColor = Color(0xFFFF00E5),
        accentColor = Color(0xFF7000FF)
    );

    companion object {
        val DEFAULT = NEON_RING

        fun fromId(id: String?): ChargingTheme {
            return entries.firstOrNull { it.id.equals(id, ignoreCase = true) } ?: DEFAULT
        }
    }
}
