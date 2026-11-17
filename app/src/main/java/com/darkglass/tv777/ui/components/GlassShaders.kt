package com.darkglass.tv777.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.darkglass.tv777.ui.theme.AccentLiveRed
import com.darkglass.tv777.ui.theme.AccentNeonBlue

@Composable
fun LiveAudioWave(modifier: Modifier = Modifier, barCount: Int = 3, color: Color = AccentLiveRed, isPlaying: Boolean = true) {
    val t = rememberInfiniteTransition("audio_bars")
    val a1 by t.animateFloat(0.3f, 1f, infiniteRepeatable(tween(450, easing = FastOutSlowInEasing), RepeatMode.Reverse), "bar1")
    val a2 by t.animateFloat(0.8f, 0.2f, infiniteRepeatable(tween(550, easing = FastOutSlowInEasing), RepeatMode.Reverse), "bar2")
    val a3 by t.animateFloat(0.4f, 0.9f, infiniteRepeatable(tween(380, easing = FastOutSlowInEasing), RepeatMode.Reverse), "bar3")
    Canvas(modifier.size(width = 16.dp, height = 14.dp)) {
        val bw = size.width / (barCount * 2f - 1f)
        val hs = listOf(if (isPlaying) a1 else 0.4f, if (isPlaying) a2 else 0.6f, if (isPlaying) a3 else 0.3f)
        for (i in 0 until barCount) {
            val height = hs[i % hs.size] * size.height
            drawRoundRect(color, Offset(i * bw * 2f, size.height - height), Size(bw, height), CornerRadius(bw / 2f, bw / 2f))
        }
    }
}

@Composable
fun DarkGlassAmbientCanvas(modifier: Modifier = Modifier, primaryAccent: Color = AccentNeonBlue, secondaryAccent: Color = Color(0xFF8A3FFC)) {
    Canvas(modifier) {
        drawCircle(Brush.radialGradient(listOf(primaryAccent.copy(alpha = 0.14f), primaryAccent.copy(alpha = 0.05f), Color.Transparent),
            Offset(size.width * 0.85f, size.height * 0.15f), size.width * 0.65f))
        drawCircle(Brush.radialGradient(listOf(secondaryAccent.copy(alpha = 0.1f), secondaryAccent.copy(alpha = 0.03f), Color.Transparent),
            Offset(size.width * 0.12f, size.height * 0.55f), size.width * 0.6f))
        drawCircle(Brush.radialGradient(listOf(primaryAccent.copy(alpha = 0.08f), Color.Transparent),
            Offset(size.width * 0.5f, size.height * 0.95f), size.width * 0.5f))
    }
}
