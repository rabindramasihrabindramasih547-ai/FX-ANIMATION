package com.example.ui.screens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AnimationSpeed
import com.example.model.ComponentSize
import com.example.model.EffectIntensity
import com.example.model.ParticleDensity
import com.example.model.PerformanceProfile
import com.example.model.SettingsState
import com.example.model.SoundStyle
import com.example.ui.components.GlowingCard
import com.example.ui.components.NeonButton
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkSurfaceCard
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextTertiary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomizeScreen(
    settings: SettingsState,
    onSpeedChange: (AnimationSpeed) -> Unit,
    onIntensityChange: (EffectIntensity) -> Unit,
    onDensityChange: (ParticleDensity) -> Unit,
    onGlowChange: (Float) -> Unit,
    onBatterySizeChange: (ComponentSize) -> Unit,
    onPercentSizeChange: (ComponentSize) -> Unit,
    onShowClockChange: (Boolean) -> Unit,
    onShowChargingTextChange: (Boolean) -> Unit,
    onShowChargingTypeChange: (Boolean) -> Unit,
    onSoundEnabledChange: (Boolean) -> Unit,
    onSoundStyleChange: (SoundStyle) -> Unit,
    onVibrationEnabledChange: (Boolean) -> Unit,
    onNotificationEnabledChange: (Boolean) -> Unit,
    onAutoStartChange: (Boolean) -> Unit,
    onKeepScreenOnChange: (Boolean) -> Unit,
    onRandomThemeChange: (Boolean) -> Unit,
    onDailyThemeChange: (Boolean) -> Unit,
    onBatteryFullAnimChange: (Boolean) -> Unit,
    onLowBatteryAnimChange: (Boolean) -> Unit,
    onPerformanceProfileChange: (PerformanceProfile) -> Unit,
    onReduceMotionChange: (Boolean) -> Unit,
    onPlaySampleSound: (SoundStyle) -> Unit,
    onTriggerSampleVibration: (Context) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        // App Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .testTag("customize_back_button")
                    .size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = TextPrimary
                )
            }
            Spacer(modifier = Modifier.width(4.dp))
            Column {
                Text(
                    text = "ANIMATION CUSTOMIZER",
                    color = TextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "Fine-tune visuals, speed & sensory feedback",
                    color = NeonCyan,
                    fontSize = 11.sp
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            // Section: Visual & Performance Tuning
            SectionHeader(icon = Icons.Default.Tune, title = "VISUAL & PERFORMANCE")

            GlowingCard {
                Column(modifier = Modifier.fillMaxWidth()) {
                    // Animation Speed
                    Text("Animation Speed", color = TextPrimary, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        AnimationSpeed.entries.forEach { speed ->
                            SelectChip(
                                label = speed.label,
                                isSelected = settings.animationSpeed == speed,
                                onClick = { onSpeedChange(speed) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Effect Intensity
                    Text("Effect Intensity", color = TextPrimary, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        EffectIntensity.entries.forEach { intensity ->
                            SelectChip(
                                label = intensity.label,
                                isSelected = settings.effectIntensity == intensity,
                                onClick = { onIntensityChange(intensity) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Particle Density
                    Text("Particle Density", color = TextPrimary, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        ParticleDensity.entries.forEach { density ->
                            SelectChip(
                                label = density.label,
                                isSelected = settings.particleDensity == density,
                                onClick = { onDensityChange(density) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Glow Intensity Slider
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Glow Intensity", color = TextPrimary, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                        Text("${(settings.glowIntensity * 100).toInt()}%", color = NeonCyan, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                    Slider(
                        value = settings.glowIntensity,
                        onValueChange = onGlowChange,
                        valueRange = 0.2f..1.0f,
                        colors = SliderDefaults.colors(
                            thumbColor = NeonCyan,
                            activeTrackColor = NeonCyan,
                            inactiveTrackColor = DarkBorder
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Performance Profile
                    Text("Performance Profile", color = TextPrimary, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        PerformanceProfile.entries.forEach { profile ->
                            SelectChip(
                                label = profile.displayName,
                                isSelected = settings.performanceProfile == profile,
                                onClick = { onPerformanceProfileChange(profile) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Reduce Motion
                    ToggleRow(
                        title = "Reduce Motion",
                        subtitle = "Minimize particle counts and rapid flashes for accessibility",
                        checked = settings.reduceMotion,
                        onCheckedChange = onReduceMotionChange
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Section: HUD Display & Sizes
            SectionHeader(icon = Icons.Default.Visibility, title = "HUD DISPLAY & SIZES")

            GlowingCard {
                Column(modifier = Modifier.fillMaxWidth()) {
                    // Percentage Size
                    Text("Battery Percentage Size", color = TextPrimary, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        ComponentSize.entries.forEach { size ->
                            SelectChip(
                                label = size.label,
                                isSelected = settings.percentageSize == size,
                                onClick = { onPercentSizeChange(size) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    ToggleRow(
                        title = "Show Clock",
                        subtitle = "Display real-time digital clock in animation center",
                        checked = settings.showClock,
                        onCheckedChange = onShowClockChange
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    ToggleRow(
                        title = "Show Charging Status Text",
                        subtitle = "Display 'CHARGING' or 'BATTERY FULL'",
                        checked = settings.showChargingText,
                        onCheckedChange = onShowChargingTextChange
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    ToggleRow(
                        title = "Show Charging Source",
                        subtitle = "Display detected USB, AC, or Wireless charging type",
                        checked = settings.showChargingType,
                        onCheckedChange = onShowChargingTypeChange
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Section: Audio & Haptic Feedback
            SectionHeader(icon = Icons.AutoMirrored.Filled.VolumeUp, title = "SOUND & VIBRATION")

            GlowingCard {
                Column(modifier = Modifier.fillMaxWidth()) {
                    ToggleRow(
                        title = "Charging Sound",
                        subtitle = "Play original chime when charger connects",
                        checked = settings.soundEnabled,
                        onCheckedChange = onSoundEnabledChange
                    )

                    if (settings.soundEnabled) {
                        Spacer(modifier = Modifier.height(14.dp))
                        Text("Sound Style", color = TextPrimary, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)

                        Column(modifier = Modifier.padding(top = 8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            SoundStyle.entries.forEach { style ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    SelectChip(
                                        label = style.displayName,
                                        isSelected = settings.soundStyle == style,
                                        onClick = { onSoundStyleChange(style) },
                                        modifier = Modifier.weight(1f)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    NeonButton(
                                        onClick = { onPlaySampleSound(style) },
                                        accentColor = NeonCyan,
                                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                                    ) {
                                        Text("Play", fontSize = 11.sp)
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    ToggleRow(
                        title = "Vibration Feedback",
                        subtitle = "Tactile haptic pulse on charger connected",
                        checked = settings.vibrationEnabled,
                        onCheckedChange = onVibrationEnabledChange
                    )

                    if (settings.vibrationEnabled) {
                        Spacer(modifier = Modifier.height(8.dp))
                        NeonButton(
                            onClick = { onTriggerSampleVibration(context) },
                            accentColor = NeonPurple,
                            modifier = Modifier.align(Alignment.End),
                            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Vibration, contentDescription = "Test Haptic", modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Test Haptic", fontSize = 12.sp)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Section: Automation & Special Triggers
            SectionHeader(icon = Icons.Default.ElectricBolt, title = "AUTOMATION & TRIGGERS")

            GlowingCard {
                Column(modifier = Modifier.fillMaxWidth()) {
                    ToggleRow(
                        title = "Auto Start Charging Animation",
                        subtitle = "Automatically launch animation when charger is plugged in",
                        checked = settings.autoStartAnimation,
                        onCheckedChange = onAutoStartChange
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    ToggleRow(
                        title = "Keep Screen On While Charging",
                        subtitle = "Maintain charging animation active while plugged in",
                        checked = settings.keepScreenOn,
                        onCheckedChange = onKeepScreenOnChange
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    ToggleRow(
                        title = "Random Theme Mode",
                        subtitle = "Automatically pick a fresh animation every time charger connects",
                        checked = settings.randomThemeMode,
                        onCheckedChange = onRandomThemeChange
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    ToggleRow(
                        title = "Daily Theme Mode",
                        subtitle = "Automatically selects a new themed animation each day",
                        checked = settings.dailyThemeMode,
                        onCheckedChange = onDailyThemeChange
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    ToggleRow(
                        title = "Battery Full Animation",
                        subtitle = "Golden victory burst when battery reaches 100%",
                        checked = settings.batteryFullAnimationEnabled,
                        onCheckedChange = onBatteryFullAnimChange
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    ToggleRow(
                        title = "Low Battery Indicator",
                        subtitle = "Gentle warning pulse when battery <= 20%",
                        checked = settings.lowBatteryAnimationEnabled,
                        onCheckedChange = onLowBatteryAnimChange
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    ToggleRow(
                        title = "Charging Notification",
                        subtitle = "Ongoing notification with live battery % and source",
                        checked = settings.notificationEnabled,
                        onCheckedChange = onNotificationEnabledChange
                    )
                }
            }

            Spacer(modifier = Modifier.height(36.dp))
        }
    }
}

@Composable
private fun SectionHeader(icon: ImageVector, title: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = icon, contentDescription = title, tint = NeonCyan, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = title, color = TextSecondary, fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
    }
}

@Composable
private fun SelectChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FilterChip(
        selected = isSelected,
        onClick = onClick,
        label = {
            Text(
                text = label,
                fontSize = 12.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                maxLines = 1
            )
        },
        modifier = modifier,
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = NeonCyan.copy(alpha = 0.2f),
            selectedLabelColor = NeonCyan,
            containerColor = DarkSurfaceCard,
            labelColor = TextSecondary
        ),
        border = FilterChipDefaults.filterChipBorder(
            borderColor = if (isSelected) NeonCyan else DarkBorder,
            selectedBorderColor = NeonCyan,
            enabled = true,
            selected = isSelected
        )
    )
}

@Composable
private fun ToggleRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f).padding(end = 12.dp)) {
            Text(text = title, color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
            Text(text = subtitle, color = TextTertiary, fontSize = 11.sp)
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.Black,
                checkedTrackColor = NeonCyan,
                uncheckedThumbColor = TextSecondary,
                uncheckedTrackColor = DarkBorder
            )
        )
    }
}
