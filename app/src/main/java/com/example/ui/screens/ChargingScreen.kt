package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.BatteryState
import com.example.model.ChargingSource
import com.example.model.ChargingTheme
import com.example.model.SettingsState
import com.example.ui.animations.ThemeAnimationRenderer
import com.example.ui.components.BatteryHudCenter
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkSurfaceCard
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun ChargingScreen(
    theme: ChargingTheme,
    batteryState: BatteryState,
    settings: SettingsState,
    isPreview: Boolean,
    initialPreviewLevel: Int = 78,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onClose() }

    var previewLevel by remember { mutableIntStateOf(initialPreviewLevel) }
    var showControls by remember { mutableStateOf(true) }

    val displayPercentage = if (isPreview) previewLevel else batteryState.percentage
    val isCurrentlyCharging = if (isPreview) true else batteryState.isCharging
    val displaySource = if (isPreview) ChargingSource.USB else batteryState.source

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                showControls = !showControls
            }
    ) {
        // Full screen animation canvas
        ThemeAnimationRenderer(
            theme = theme,
            batteryPercentage = displayPercentage,
            isCharging = isCurrentlyCharging,
            settings = settings,
            modifier = Modifier.fillMaxSize()
        )

        // Center Battery HUD (Time, Bolt, Percentage, Status, Source)
        BatteryHudCenter(
            percentage = displayPercentage,
            isCharging = isCurrentlyCharging,
            source = displaySource,
            theme = theme,
            settings = settings,
            isPreview = isPreview,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(24.dp)
        )

        // Overlay Navigation Controls
        AnimatedVisibility(
            visible = showControls,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.fillMaxSize()
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                // Top Dismiss Button
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = theme.themeName.uppercase(),
                            color = theme.primaryColor,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 2.sp
                        )
                        if (isPreview) {
                            Text(
                                text = "TAP SCREEN TO HIDE CONTROLS",
                                color = TextSecondary,
                                fontSize = 10.sp
                            )
                        } else {
                            Text(
                                text = "REAL-TIME CHARGE MONITOR",
                                color = TextSecondary,
                                fontSize = 10.sp
                            )
                        }
                    }

                    IconButton(
                        onClick = onClose,
                        modifier = Modifier
                            .testTag("charging_screen_close_button")
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(Color.Black.copy(alpha = 0.6f))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Exit Charging Screen",
                            tint = Color.White
                        )
                    }
                }

                // If in Preview Mode: Sample Level Selector bar at bottom
                if (isPreview) {
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .navigationBarsPadding()
                            .padding(bottom = 24.dp, start = 16.dp, end = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Test at different battery percentages:",
                            color = TextSecondary,
                            fontSize = 11.sp,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            listOf(15, 45, 78, 100).forEach { lvl ->
                                val isSelected = previewLevel == lvl
                                FilterChip(
                                    selected = isSelected,
                                    onClick = { previewLevel = lvl },
                                    label = {
                                        Text(
                                            text = if (lvl == 100) "100% (Full)" else if (lvl == 15) "15% (Low)" else "$lvl%",
                                            fontSize = 12.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                        )
                                    },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = theme.primaryColor.copy(alpha = 0.25f),
                                        selectedLabelColor = theme.primaryColor,
                                        containerColor = DarkSurfaceCard.copy(alpha = 0.8f),
                                        labelColor = TextPrimary
                                    ),
                                    border = FilterChipDefaults.filterChipBorder(
                                        borderColor = if (isSelected) theme.primaryColor else DarkBorder,
                                        selectedBorderColor = theme.primaryColor,
                                        enabled = true,
                                        selected = isSelected
                                    )
                                )
                            }
                        }
                    }
                } else {
                    // Subtle hint for real charging screen
                    Text(
                        text = "Double tap or tap ✕ to exit",
                        color = TextSecondary.copy(alpha = 0.6f),
                        fontSize = 11.sp,
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .navigationBarsPadding()
                            .padding(bottom = 20.dp)
                    )
                }
            }
        }
    }
}
