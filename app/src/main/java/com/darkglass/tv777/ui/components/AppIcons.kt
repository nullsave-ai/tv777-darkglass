package com.darkglass.tv777.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

enum class Ic {
    PLAY, PAUSE, PREV, NEXT, VOLUME, MUTE, STAR, STAR_BORDER,
    CLOSE, CHECK, REFRESH, SEARCH, GRID, LIST, WARNING, TV,
    ANTENNA, BOLT, HOME, SETTINGS, BACK, PLAYLIST
}

private fun starPath(w: Float, h: Float) = Path().apply {
    val cx = w / 2f
    val cy = h * 0.52f
    val outer = h * 0.45f
    val inner = outer * 0.44f
    for (i in 0 until 10) {
        val r = if (i % 2 == 0) outer else inner
        val a = (-PI / 2 + i * PI / 5).toFloat()
        val x = cx + r * cos(a)
        val y = cy + r * sin(a)
        if (i == 0) moveTo(x, y) else lineTo(x, y)
    }
    close()
}

@Composable
fun AppIcon(name: Ic, modifier: Modifier = Modifier, tint: Color = Color.White, stroke: Dp = 2.dp) {
    Canvas(modifier) {
        val w = size.width
        val h = size.height
        val s = stroke.toPx()
        val line = Stroke(width = s, cap = StrokeCap.Round)
        val speaker = Path().apply {
            moveTo(w * 0.12f, h * 0.38f)
            lineTo(w * 0.3f, h * 0.38f)
            lineTo(w * 0.52f, h * 0.18f)
            lineTo(w * 0.52f, h * 0.82f)
            lineTo(w * 0.3f, h * 0.62f)
            lineTo(w * 0.12f, h * 0.62f)
            close()
        }
        when (name) {
            Ic.PLAY -> drawPath(Path().apply {
                moveTo(w * 0.26f, h * 0.16f)
                lineTo(w * 0.86f, h * 0.5f)
                lineTo(w * 0.26f, h * 0.84f)
                close()
            }, tint)
            Ic.PAUSE -> {
                drawRoundRect(tint, Offset(w * 0.26f, h * 0.18f), Size(w * 0.17f, h * 0.64f), CornerRadius(s / 2, s / 2))
                drawRoundRect(tint, Offset(w * 0.57f, h * 0.18f), Size(w * 0.17f, h * 0.64f), CornerRadius(s / 2, s / 2))
            }
            Ic.PREV -> {
                drawRoundRect(tint, Offset(w * 0.16f, h * 0.2f), Size(w * 0.08f, h * 0.6f), CornerRadius(s / 2, s / 2))
                drawPath(Path().apply {
                    moveTo(w * 0.88f, h * 0.2f)
                    lineTo(w * 0.36f, h * 0.5f)
                    lineTo(w * 0.88f, h * 0.8f)
                    close()
                }, tint)
            }
            Ic.NEXT -> {
                drawRoundRect(tint, Offset(w * 0.76f, h * 0.2f), Size(w * 0.08f, h * 0.6f), CornerRadius(s / 2, s / 2))
                drawPath(Path().apply {
                    moveTo(w * 0.12f, h * 0.2f)
                    lineTo(w * 0.64f, h * 0.5f)
                    lineTo(w * 0.12f, h * 0.8f)
                    close()
                }, tint)
            }
            Ic.VOLUME -> {
                drawPath(speaker, tint)
                drawArc(tint, -50f, 100f, false, Offset(w * 0.62f, h * 0.32f), Size(w * 0.24f, h * 0.36f), style = Stroke(s))
                drawArc(tint, -50f, 100f, false, Offset(w * 0.56f, h * 0.2f), Size(w * 0.4f, h * 0.6f), style = Stroke(s))
            }
            Ic.MUTE -> {
                drawPath(speaker, tint)
                drawLine(tint, Offset(w * 0.64f, h * 0.36f), Offset(w * 0.92f, h * 0.64f), s, StrokeCap.Round)
                drawLine(tint, Offset(w * 0.92f, h * 0.36f), Offset(w * 0.64f, h * 0.64f), s, StrokeCap.Round)
            }
            Ic.STAR -> drawPath(starPath(w, h), tint)
            Ic.STAR_BORDER -> drawPath(starPath(w, h), tint, style = line)
            Ic.CLOSE -> {
                drawLine(tint, Offset(w * 0.26f, h * 0.26f), Offset(w * 0.74f, h * 0.74f), s, StrokeCap.Round)
                drawLine(tint, Offset(w * 0.74f, h * 0.26f), Offset(w * 0.26f, h * 0.74f), s, StrokeCap.Round)
            }
            Ic.CHECK -> drawPath(Path().apply {
                moveTo(w * 0.18f, h * 0.52f)
                lineTo(w * 0.42f, h * 0.76f)
                lineTo(w * 0.84f, h * 0.24f)
            }, tint, style = line)
            Ic.REFRESH -> {
                drawArc(tint, 45f, 290f, false, Offset(w * 0.16f, h * 0.16f), Size(w * 0.68f, h * 0.68f), style = line)
                drawPath(Path().apply {
                    moveTo(w * 0.9f, h * 0.3f)
                    lineTo(w * 0.62f, h * 0.22f)
                    lineTo(w * 0.78f, h * 0.5f)
                    close()
                }, tint)
            }
            Ic.SEARCH -> {
                drawCircle(tint, radius = w * 0.28f, center = Offset(w * 0.42f, h * 0.42f), style = line)
                drawLine(tint, Offset(w * 0.62f, h * 0.62f), Offset(w * 0.88f, h * 0.88f), s, StrokeCap.Round)
            }
            Ic.GRID -> {
                val r = CornerRadius(w * 0.05f, w * 0.05f)
                drawRoundRect(tint, Offset(w * 0.14f, h * 0.14f), Size(w * 0.32f, h * 0.32f), r)
                drawRoundRect(tint, Offset(w * 0.54f, h * 0.14f), Size(w * 0.32f, h * 0.32f), r)
                drawRoundRect(tint, Offset(w * 0.14f, h * 0.54f), Size(w * 0.32f, h * 0.32f), r)
                drawRoundRect(tint, Offset(w * 0.54f, h * 0.54f), Size(w * 0.32f, h * 0.32f), r)
            }
            Ic.LIST -> for (i in 0..2) {
                val y = h * (0.26f + i * 0.24f)
                drawCircle(tint, radius = s * 0.7f, center = Offset(w * 0.18f, y))
                drawLine(tint, Offset(w * 0.32f, y), Offset(w * 0.86f, y), s, StrokeCap.Round)
            }
            Ic.WARNING -> {
                drawPath(Path().apply {
                    moveTo(w * 0.5f, h * 0.14f)
                    lineTo(w * 0.92f, h * 0.84f)
                    lineTo(w * 0.08f, h * 0.84f)
                    close()
                }, tint, style = line)
                drawLine(tint, Offset(w * 0.5f, h * 0.4f), Offset(w * 0.5f, h * 0.62f), s, StrokeCap.Round)
                drawCircle(tint, radius = s * 0.6f, center = Offset(w * 0.5f, h * 0.74f))
            }
            Ic.TV -> {
                drawRoundRect(tint, Offset(w * 0.1f, h * 0.24f), Size(w * 0.8f, h * 0.5f), CornerRadius(w * 0.06f, w * 0.06f), style = line)
                drawLine(tint, Offset(w * 0.35f, h * 0.86f), Offset(w * 0.65f, h * 0.86f), s, StrokeCap.Round)
                drawLine(tint, Offset(w * 0.5f, h * 0.74f), Offset(w * 0.5f, h * 0.86f), s, StrokeCap.Round)
                drawLine(tint, Offset(w * 0.22f, h * 0.24f), Offset(w * 0.36f, h * 0.08f), s, StrokeCap.Round)
                drawLine(tint, Offset(w * 0.78f, h * 0.24f), Offset(w * 0.64f, h * 0.08f), s, StrokeCap.Round)
            }
            Ic.ANTENNA -> {
                drawArc(tint, 200f, 140f, false, Offset(w * 0.12f, h * 0.14f), Size(w * 0.76f, h * 0.6f), style = line)
                drawLine(tint, Offset(w * 0.5f, h * 0.5f), Offset(w * 0.5f, h * 0.78f), s, StrokeCap.Round)
                drawLine(tint, Offset(w * 0.28f, h * 0.86f), Offset(w * 0.72f, h * 0.86f), s, StrokeCap.Round)
                drawCircle(tint, radius = s, center = Offset(w * 0.5f, h * 0.5f))
            }
            Ic.BOLT -> drawPath(Path().apply {
                moveTo(w * 0.56f, h * 0.08f)
                lineTo(w * 0.24f, h * 0.54f)
                lineTo(w * 0.48f, h * 0.54f)
                lineTo(w * 0.4f, h * 0.92f)
                lineTo(w * 0.78f, h * 0.42f)
                lineTo(w * 0.54f, h * 0.42f)
                close()
            }, tint)
            Ic.HOME -> drawPath(Path().apply {
                moveTo(w * 0.14f, h * 0.46f)
                lineTo(w * 0.5f, h * 0.14f)
                lineTo(w * 0.86f, h * 0.46f)
                lineTo(w * 0.86f, h * 0.86f)
                lineTo(w * 0.58f, h * 0.86f)
                lineTo(w * 0.58f, h * 0.58f)
                lineTo(w * 0.42f, h * 0.58f)
                lineTo(w * 0.42f, h * 0.86f)
                lineTo(w * 0.14f, h * 0.86f)
                close()
            }, tint, style = line)
            Ic.SETTINGS -> {
                drawLine(tint, Offset(w * 0.18f, h * 0.3f), Offset(w * 0.82f, h * 0.3f), s, StrokeCap.Round)
                drawCircle(tint, radius = s * 1.6f, center = Offset(w * 0.64f, h * 0.3f))
                drawLine(tint, Offset(w * 0.18f, h * 0.7f), Offset(w * 0.82f, h * 0.7f), s, StrokeCap.Round)
                drawCircle(tint, radius = s * 1.6f, center = Offset(w * 0.36f, h * 0.7f))
            }
            Ic.BACK -> drawPath(Path().apply {
                moveTo(w * 0.54f, h * 0.22f)
                lineTo(w * 0.24f, h * 0.5f)
                lineTo(w * 0.54f, h * 0.78f)
                moveTo(w * 0.24f, h * 0.5f)
                lineTo(w * 0.86f, h * 0.5f)
            }, tint, style = line)
            Ic.PLAYLIST -> {
                drawLine(tint, Offset(w * 0.14f, h * 0.28f), Offset(w * 0.86f, h * 0.28f), s, StrokeCap.Round)
                drawLine(tint, Offset(w * 0.14f, h * 0.5f), Offset(w * 0.86f, h * 0.5f), s, StrokeCap.Round)
                drawPath(Path().apply {
                    moveTo(w * 0.18f, h * 0.62f)
                    lineTo(w * 0.18f, h * 0.9f)
                    lineTo(w * 0.42f, h * 0.76f)
                    close()
                }, tint)
            }
        }
    }
}
