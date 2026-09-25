package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkSurfaceCard

@Composable
fun GlowingCard(
    modifier: Modifier = Modifier,
    borderColor: Color = DarkBorder,
    glowColor: Color? = null,
    cornerRadius: Dp = 20.dp,
    content: @Composable BoxScope.() -> Unit
) {
    val shape = RoundedCornerShape(cornerRadius)
    val backgroundBrush = Brush.verticalGradient(
        colors = listOf(
            DarkSurfaceCard.copy(alpha = 0.92f),
            DarkSurfaceCard.copy(alpha = 0.75f)
        )
    )

    val border = if (glowColor != null) {
        BorderStroke(1.dp, Brush.linearGradient(listOf(glowColor.copy(alpha = 0.8f), borderColor)))
    } else {
        BorderStroke(1.dp, borderColor)
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(backgroundBrush, shape)
            .border(border, shape)
            .padding(16.dp),
        content = content
    )
}
