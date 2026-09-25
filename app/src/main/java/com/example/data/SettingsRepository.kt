package com.example.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.model.AnimationSpeed
import com.example.model.ChargingTheme
import com.example.model.ComponentSize
import com.example.model.EffectIntensity
import com.example.model.ParticleDensity
import com.example.model.PerformanceProfile
import com.example.model.SettingsState
import com.example.model.SoundStyle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "charge_fx_settings")

class SettingsRepository(private val context: Context) {

    private object PreferencesKeys {
        val SELECTED_THEME_ID = stringPreferencesKey("selected_theme_id")
        val ANIMATION_SPEED = stringPreferencesKey("animation_speed")
        val EFFECT_INTENSITY = stringPreferencesKey("effect_intensity")
        val PARTICLE_DENSITY = stringPreferencesKey("particle_density")
        val GLOW_INTENSITY = floatPreferencesKey("glow_intensity")
        val BATTERY_SIZE = stringPreferencesKey("battery_size")
        val PERCENTAGE_SIZE = stringPreferencesKey("percentage_size")
        val SHOW_CLOCK = booleanPreferencesKey("show_clock")
        val SHOW_CHARGING_TEXT = booleanPreferencesKey("show_charging_text")
        val SHOW_CHARGING_TYPE = booleanPreferencesKey("show_charging_type")
        val SOUND_ENABLED = booleanPreferencesKey("sound_enabled")
        val SOUND_STYLE = stringPreferencesKey("sound_style")
        val VIBRATION_ENABLED = booleanPreferencesKey("vibration_enabled")
        val NOTIFICATION_ENABLED = booleanPreferencesKey("notification_enabled")
        val AUTO_START_ANIMATION = booleanPreferencesKey("auto_start_animation")
        val KEEP_SCREEN_ON = booleanPreferencesKey("keep_screen_on")
        val RANDOM_THEME_MODE = booleanPreferencesKey("random_theme_mode")
        val DAILY_THEME_MODE = booleanPreferencesKey("daily_theme_mode")
        val BATTERY_FULL_ANIMATION = booleanPreferencesKey("battery_full_animation")
        val LOW_BATTERY_ANIMATION = booleanPreferencesKey("low_battery_animation")
        val PERFORMANCE_PROFILE = stringPreferencesKey("performance_profile")
        val REDUCE_MOTION = booleanPreferencesKey("reduce_motion")
        val FAVORITE_THEMES = stringSetPreferencesKey("favorite_themes")
    }

    val settingsFlow: Flow<SettingsState> = context.dataStore.data.map { preferences ->
        val selectedThemeId = preferences[PreferencesKeys.SELECTED_THEME_ID] ?: ChargingTheme.DEFAULT.id
        val speedStr = preferences[PreferencesKeys.ANIMATION_SPEED] ?: AnimationSpeed.NORMAL.name
        val intensityStr = preferences[PreferencesKeys.EFFECT_INTENSITY] ?: EffectIntensity.HIGH.name
        val densityStr = preferences[PreferencesKeys.PARTICLE_DENSITY] ?: ParticleDensity.MEDIUM.name
        val glow = preferences[PreferencesKeys.GLOW_INTENSITY] ?: 0.85f
        val batterySizeStr = preferences[PreferencesKeys.BATTERY_SIZE] ?: ComponentSize.MEDIUM.name
        val percentSizeStr = preferences[PreferencesKeys.PERCENTAGE_SIZE] ?: ComponentSize.MEDIUM.name
        val showClock = preferences[PreferencesKeys.SHOW_CLOCK] ?: true
        val showText = preferences[PreferencesKeys.SHOW_CHARGING_TEXT] ?: true
        val showType = preferences[PreferencesKeys.SHOW_CHARGING_TYPE] ?: true
        val sound = preferences[PreferencesKeys.SOUND_ENABLED] ?: true
        val soundStyleStr = preferences[PreferencesKeys.SOUND_STYLE] ?: SoundStyle.FUTURISTIC.name
        val vibration = preferences[PreferencesKeys.VIBRATION_ENABLED] ?: true
        val notification = preferences[PreferencesKeys.NOTIFICATION_ENABLED] ?: true
        val autoStart = preferences[PreferencesKeys.AUTO_START_ANIMATION] ?: true
        val keepScreen = preferences[PreferencesKeys.KEEP_SCREEN_ON] ?: true
        val randomTheme = preferences[PreferencesKeys.RANDOM_THEME_MODE] ?: false
        val dailyTheme = preferences[PreferencesKeys.DAILY_THEME_MODE] ?: false
        val fullAnim = preferences[PreferencesKeys.BATTERY_FULL_ANIMATION] ?: true
        val lowAnim = preferences[PreferencesKeys.LOW_BATTERY_ANIMATION] ?: true
        val profileStr = preferences[PreferencesKeys.PERFORMANCE_PROFILE] ?: PerformanceProfile.BALANCED.name
        val reduceMotion = preferences[PreferencesKeys.REDUCE_MOTION] ?: false
        val favorites = preferences[PreferencesKeys.FAVORITE_THEMES] ?: setOf(
            ChargingTheme.NEON_RING.id,
            ChargingTheme.ELECTRIC_LIGHTNING.id,
            ChargingTheme.PLASMA_ENERGY.id,
            ChargingTheme.CYBER.id
        )

        SettingsState(
            selectedThemeId = selectedThemeId,
            animationSpeed = runCatching { AnimationSpeed.valueOf(speedStr) }.getOrDefault(AnimationSpeed.NORMAL),
            effectIntensity = runCatching { EffectIntensity.valueOf(intensityStr) }.getOrDefault(EffectIntensity.HIGH),
            particleDensity = runCatching { ParticleDensity.valueOf(densityStr) }.getOrDefault(ParticleDensity.MEDIUM),
            glowIntensity = glow,
            batterySize = runCatching { ComponentSize.valueOf(batterySizeStr) }.getOrDefault(ComponentSize.MEDIUM),
            percentageSize = runCatching { ComponentSize.valueOf(percentSizeStr) }.getOrDefault(ComponentSize.MEDIUM),
            showClock = showClock,
            showChargingText = showText,
            showChargingType = showType,
            soundEnabled = sound,
            soundStyle = runCatching { SoundStyle.valueOf(soundStyleStr) }.getOrDefault(SoundStyle.FUTURISTIC),
            vibrationEnabled = vibration,
            notificationEnabled = notification,
            autoStartAnimation = autoStart,
            keepScreenOn = keepScreen,
            randomThemeMode = randomTheme,
            dailyThemeMode = dailyTheme,
            batteryFullAnimationEnabled = fullAnim,
            lowBatteryAnimationEnabled = lowAnim,
            performanceProfile = runCatching { PerformanceProfile.valueOf(profileStr) }.getOrDefault(PerformanceProfile.BALANCED),
            reduceMotion = reduceMotion,
            favoriteThemeIds = favorites
        )
    }

    suspend fun updateSelectedTheme(themeId: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.SELECTED_THEME_ID] = themeId
        }
    }

    suspend fun toggleFavorite(themeId: String) {
        context.dataStore.edit { preferences ->
            val current = preferences[PreferencesKeys.FAVORITE_THEMES] ?: emptySet()
            val updated = if (current.contains(themeId)) {
                current - themeId
            } else {
                current + themeId
            }
            preferences[PreferencesKeys.FAVORITE_THEMES] = updated
        }
    }

    suspend fun updateAnimationSpeed(speed: AnimationSpeed) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.ANIMATION_SPEED] = speed.name
        }
    }

    suspend fun updateEffectIntensity(intensity: EffectIntensity) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.EFFECT_INTENSITY] = intensity.name
        }
    }

    suspend fun updateParticleDensity(density: ParticleDensity) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.PARTICLE_DENSITY] = density.name
        }
    }

    suspend fun updateGlowIntensity(glow: Float) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.GLOW_INTENSITY] = glow.coerceIn(0.1f, 1.0f)
        }
    }

    suspend fun updateBatterySize(size: ComponentSize) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.BATTERY_SIZE] = size.name
        }
    }

    suspend fun updatePercentageSize(size: ComponentSize) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.PERCENTAGE_SIZE] = size.name
        }
    }

    suspend fun updateShowClock(show: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.SHOW_CLOCK] = show
        }
    }

    suspend fun updateShowChargingText(show: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.SHOW_CHARGING_TEXT] = show
        }
    }

    suspend fun updateShowChargingType(show: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.SHOW_CHARGING_TYPE] = show
        }
    }

    suspend fun updateSoundEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.SOUND_ENABLED] = enabled
        }
    }

    suspend fun updateSoundStyle(style: SoundStyle) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.SOUND_STYLE] = style.name
        }
    }

    suspend fun updateVibrationEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.VIBRATION_ENABLED] = enabled
        }
    }

    suspend fun updateNotificationEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.NOTIFICATION_ENABLED] = enabled
        }
    }

    suspend fun updateAutoStartAnimation(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.AUTO_START_ANIMATION] = enabled
        }
    }

    suspend fun updateKeepScreenOn(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.KEEP_SCREEN_ON] = enabled
        }
    }

    suspend fun updateRandomThemeMode(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.RANDOM_THEME_MODE] = enabled
            if (enabled) {
                preferences[PreferencesKeys.DAILY_THEME_MODE] = false
            }
        }
    }

    suspend fun updateDailyThemeMode(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.DAILY_THEME_MODE] = enabled
            if (enabled) {
                preferences[PreferencesKeys.RANDOM_THEME_MODE] = false
            }
        }
    }

    suspend fun updateBatteryFullAnimation(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.BATTERY_FULL_ANIMATION] = enabled
        }
    }

    suspend fun updateLowBatteryAnimation(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.LOW_BATTERY_ANIMATION] = enabled
        }
    }

    suspend fun updatePerformanceProfile(profile: PerformanceProfile) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.PERFORMANCE_PROFILE] = profile.name
        }
    }

    suspend fun updateReduceMotion(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.REDUCE_MOTION] = enabled
        }
    }
}
