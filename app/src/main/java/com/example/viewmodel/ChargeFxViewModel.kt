package com.example.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.BatteryRepository
import com.example.data.SettingsRepository
import com.example.model.AnimationSpeed
import com.example.model.BatteryState
import com.example.model.ChargingTheme
import com.example.model.ComponentSize
import com.example.model.EffectIntensity
import com.example.model.ParticleDensity
import com.example.model.PerformanceProfile
import com.example.model.SettingsState
import com.example.model.SoundStyle
import com.example.util.NotificationHelper
import com.example.util.SoundPlayer
import com.example.util.VibrationHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar

enum class AppScreen {
    DASHBOARD,
    THEMES_GALLERY,
    CUSTOMIZE,
    BATTERY_INFO,
    LIVE_CHARGING,
    PREVIEW
}

class ChargeFxViewModel(application: Application) : AndroidViewModel(application) {

    private val batteryRepository = BatteryRepository(application)
    private val settingsRepository = SettingsRepository(application)

    val batteryState: StateFlow<BatteryState> = batteryRepository.observeBatteryState()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = batteryRepository.getCurrentBatteryState()
        )

    val settings: StateFlow<SettingsState> = settingsRepository.settingsFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = SettingsState()
        )

    private val _currentScreen = MutableStateFlow(AppScreen.DASHBOARD)
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    private val _previewTheme = MutableStateFlow(ChargingTheme.DEFAULT)
    val previewTheme: StateFlow<ChargingTheme> = _previewTheme.asStateFlow()

    private val _previewPercentage = MutableStateFlow(78)
    val previewPercentage: StateFlow<Int> = _previewPercentage.asStateFlow()

    val activeTheme: StateFlow<ChargingTheme> = combine(settings) { (currentSettings) ->
        if (currentSettings.dailyThemeMode) {
            val dayOfYear = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
            val themes = ChargingTheme.entries
            themes[dayOfYear % themes.size]
        } else {
            ChargingTheme.fromId(currentSettings.selectedThemeId)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ChargingTheme.DEFAULT
    )

    init {
        // Observe charging state changes for notification and sound/vibration if app is in foreground
        var wasCharging = batteryState.value.isCharging
        viewModelScope.launch {
            batteryState.collect { state ->
                val currentSettings = settings.value
                if (state.isCharging && !wasCharging) {
                    // Charger just connected while app was open
                    if (currentSettings.soundEnabled) {
                        SoundPlayer.playChargingSound(currentSettings.soundStyle)
                    }
                    if (currentSettings.vibrationEnabled) {
                        VibrationHelper.vibrateChargeConnected(getApplication())
                    }
                    if (currentSettings.autoStartAnimation && _currentScreen.value == AppScreen.DASHBOARD) {
                        _currentScreen.value = AppScreen.LIVE_CHARGING
                    }
                } else if (!state.isCharging && wasCharging) {
                    // Disconnected
                    if (_currentScreen.value == AppScreen.LIVE_CHARGING) {
                        _currentScreen.value = AppScreen.DASHBOARD
                    }
                }
                wasCharging = state.isCharging

                if (currentSettings.notificationEnabled) {
                    NotificationHelper.updateChargingNotification(getApplication(), state)
                }
            }
        }
    }

    fun navigateTo(screen: AppScreen) {
        _currentScreen.value = screen
    }

    fun openPreview(theme: ChargingTheme, sampleLevel: Int = 78) {
        _previewTheme.value = theme
        _previewPercentage.value = sampleLevel
        _currentScreen.value = AppScreen.PREVIEW
    }

    fun closePreview() {
        if (_currentScreen.value == AppScreen.PREVIEW) {
            _currentScreen.value = AppScreen.THEMES_GALLERY
        }
    }

    fun openLiveChargingScreen() {
        _currentScreen.value = AppScreen.LIVE_CHARGING
    }

    fun closeLiveChargingScreen() {
        if (_currentScreen.value == AppScreen.LIVE_CHARGING) {
            _currentScreen.value = AppScreen.DASHBOARD
        }
    }

    fun selectTheme(theme: ChargingTheme) {
        viewModelScope.launch {
            settingsRepository.updateSelectedTheme(theme.id)
        }
    }

    fun toggleFavorite(theme: ChargingTheme) {
        viewModelScope.launch {
            settingsRepository.toggleFavorite(theme.id)
        }
    }

    fun setAnimationSpeed(speed: AnimationSpeed) {
        viewModelScope.launch { settingsRepository.updateAnimationSpeed(speed) }
    }

    fun setEffectIntensity(intensity: EffectIntensity) {
        viewModelScope.launch { settingsRepository.updateEffectIntensity(intensity) }
    }

    fun setParticleDensity(density: ParticleDensity) {
        viewModelScope.launch { settingsRepository.updateParticleDensity(density) }
    }

    fun setGlowIntensity(intensity: Float) {
        viewModelScope.launch { settingsRepository.updateGlowIntensity(intensity) }
    }

    fun setBatterySize(size: ComponentSize) {
        viewModelScope.launch { settingsRepository.updateBatterySize(size) }
    }

    fun setPercentageSize(size: ComponentSize) {
        viewModelScope.launch { settingsRepository.updatePercentageSize(size) }
    }

    fun setShowClock(show: Boolean) {
        viewModelScope.launch { settingsRepository.updateShowClock(show) }
    }

    fun setShowChargingText(show: Boolean) {
        viewModelScope.launch { settingsRepository.updateShowChargingText(show) }
    }

    fun setShowChargingType(show: Boolean) {
        viewModelScope.launch { settingsRepository.updateShowChargingType(show) }
    }

    fun setSoundEnabled(enabled: Boolean) {
        viewModelScope.launch { settingsRepository.updateSoundEnabled(enabled) }
    }

    fun setSoundStyle(style: SoundStyle) {
        viewModelScope.launch { settingsRepository.updateSoundStyle(style) }
    }

    fun setVibrationEnabled(enabled: Boolean) {
        viewModelScope.launch { settingsRepository.updateVibrationEnabled(enabled) }
    }

    fun setNotificationEnabled(enabled: Boolean) {
        viewModelScope.launch { settingsRepository.updateNotificationEnabled(enabled) }
    }

    fun setAutoStartAnimation(enabled: Boolean) {
        viewModelScope.launch { settingsRepository.updateAutoStartAnimation(enabled) }
    }

    fun setKeepScreenOn(enabled: Boolean) {
        viewModelScope.launch { settingsRepository.updateKeepScreenOn(enabled) }
    }

    fun setRandomThemeMode(enabled: Boolean) {
        viewModelScope.launch { settingsRepository.updateRandomThemeMode(enabled) }
    }

    fun setDailyThemeMode(enabled: Boolean) {
        viewModelScope.launch { settingsRepository.updateDailyThemeMode(enabled) }
    }

    fun setBatteryFullAnimation(enabled: Boolean) {
        viewModelScope.launch { settingsRepository.updateBatteryFullAnimation(enabled) }
    }

    fun setLowBatteryAnimation(enabled: Boolean) {
        viewModelScope.launch { settingsRepository.updateLowBatteryAnimation(enabled) }
    }

    fun setPerformanceProfile(profile: PerformanceProfile) {
        viewModelScope.launch { settingsRepository.updatePerformanceProfile(profile) }
    }

    fun setReduceMotion(enabled: Boolean) {
        viewModelScope.launch { settingsRepository.updateReduceMotion(enabled) }
    }

    fun playSampleSound(style: SoundStyle) {
        SoundPlayer.playChargingSound(style)
    }

    fun triggerSampleVibration(context: Context) {
        VibrationHelper.vibrateChargeConnected(context)
    }
}
