package com.example

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.screens.BatteryInfoScreen
import com.example.ui.screens.ChargingScreen
import com.example.ui.screens.CustomizeScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.ThemesGalleryScreen
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.AppScreen
import com.example.viewmodel.ChargeFxViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: ChargeFxViewModel by viewModels()

    private val requestNotificationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { _ ->
            // Notification permission handled gracefully
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Enable show when locked and turn screen on for charging animation
        setupLockscreenFlags()

        // Check intent if launched from power connected receiver or notification
        handleIncomingIntent(intent)

        // Request notification permission if Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestNotificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }

        setContent {
            MyApplicationTheme {
                MainContent(viewModel = viewModel)
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleIncomingIntent(intent)
    }

    private fun handleIncomingIntent(intent: Intent?) {
        if (intent?.getBooleanExtra("EXTRA_START_CHARGING_SCREEN", false) == true) {
            viewModel.openLiveChargingScreen()
        }
    }

    private fun setupLockscreenFlags() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
        } else {
            @Suppress("DEPRECATION")
            window.addFlags(
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
            )
        }
    }

    @Composable
    private fun MainContent(viewModel: ChargeFxViewModel) {
        val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
        val batteryState by viewModel.batteryState.collectAsStateWithLifecycle()
        val settings by viewModel.settings.collectAsStateWithLifecycle()
        val activeTheme by viewModel.activeTheme.collectAsStateWithLifecycle()
        val previewTheme by viewModel.previewTheme.collectAsStateWithLifecycle()
        val previewPercentage by viewModel.previewPercentage.collectAsStateWithLifecycle()

        // Dynamic Keep Screen On management
        DisposableEffect(settings.keepScreenOn, batteryState.isCharging, currentScreen) {
            val shouldKeepOn = settings.keepScreenOn &&
                    (batteryState.isCharging || currentScreen == AppScreen.LIVE_CHARGING)
            if (shouldKeepOn) {
                window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            } else {
                window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            }
            onDispose {
                window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            }
        }

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = DarkBackground
        ) {
            when (currentScreen) {
                AppScreen.DASHBOARD -> {
                    HomeScreen(
                        batteryState = batteryState,
                        activeTheme = activeTheme,
                        settings = settings,
                        onNavigate = { screen -> viewModel.navigateTo(screen) },
                        onOpenPreview = { theme -> viewModel.openPreview(theme) },
                        onOpenLiveCharging = { viewModel.openLiveChargingScreen() },
                        modifier = Modifier.safeDrawingPadding()
                    )
                }

                AppScreen.THEMES_GALLERY -> {
                    ThemesGalleryScreen(
                        currentThemeId = settings.selectedThemeId,
                        settings = settings,
                        onSelectTheme = { theme -> viewModel.selectTheme(theme) },
                        onToggleFavorite = { theme -> viewModel.toggleFavorite(theme) },
                        onOpenPreview = { theme -> viewModel.openPreview(theme) },
                        onBack = { viewModel.navigateTo(AppScreen.DASHBOARD) },
                        modifier = Modifier.safeDrawingPadding()
                    )
                }

                AppScreen.CUSTOMIZE -> {
                    CustomizeScreen(
                        settings = settings,
                        onSpeedChange = { viewModel.setAnimationSpeed(it) },
                        onIntensityChange = { viewModel.setEffectIntensity(it) },
                        onDensityChange = { viewModel.setParticleDensity(it) },
                        onGlowChange = { viewModel.setGlowIntensity(it) },
                        onBatterySizeChange = { viewModel.setBatterySize(it) },
                        onPercentSizeChange = { viewModel.setPercentageSize(it) },
                        onShowClockChange = { viewModel.setShowClock(it) },
                        onShowChargingTextChange = { viewModel.setShowChargingText(it) },
                        onShowChargingTypeChange = { viewModel.setShowChargingType(it) },
                        onSoundEnabledChange = { viewModel.setSoundEnabled(it) },
                        onSoundStyleChange = { viewModel.setSoundStyle(it) },
                        onVibrationEnabledChange = { viewModel.setVibrationEnabled(it) },
                        onNotificationEnabledChange = { viewModel.setNotificationEnabled(it) },
                        onAutoStartChange = { viewModel.setAutoStartAnimation(it) },
                        onKeepScreenOnChange = { viewModel.setKeepScreenOn(it) },
                        onRandomThemeChange = { viewModel.setRandomThemeMode(it) },
                        onDailyThemeChange = { viewModel.setDailyThemeMode(it) },
                        onBatteryFullAnimChange = { viewModel.setBatteryFullAnimation(it) },
                        onLowBatteryAnimChange = { viewModel.setLowBatteryAnimation(it) },
                        onPerformanceProfileChange = { viewModel.setPerformanceProfile(it) },
                        onReduceMotionChange = { viewModel.setReduceMotion(it) },
                        onPlaySampleSound = { viewModel.playSampleSound(it) },
                        onTriggerSampleVibration = { ctx -> viewModel.triggerSampleVibration(ctx) },
                        onBack = { viewModel.navigateTo(AppScreen.DASHBOARD) },
                        modifier = Modifier.safeDrawingPadding()
                    )
                }

                AppScreen.BATTERY_INFO -> {
                    BatteryInfoScreen(
                        batteryState = batteryState,
                        onBack = { viewModel.navigateTo(AppScreen.DASHBOARD) },
                        modifier = Modifier.safeDrawingPadding()
                    )
                }

                AppScreen.LIVE_CHARGING -> {
                    ChargingScreen(
                        theme = activeTheme,
                        batteryState = batteryState,
                        settings = settings,
                        isPreview = false,
                        onClose = { viewModel.closeLiveChargingScreen() }
                    )
                }

                AppScreen.PREVIEW -> {
                    ChargingScreen(
                        theme = previewTheme,
                        batteryState = batteryState,
                        settings = settings,
                        isPreview = true,
                        initialPreviewLevel = previewPercentage,
                        onClose = { viewModel.closePreview() }
                    )
                }
            }
        }
    }
}
