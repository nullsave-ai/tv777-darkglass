package com.darkglass.tv777.ui.theme

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.darkglass.tv777.ui.components.AppIcon
import com.darkglass.tv777.ui.components.Ic
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

enum class PatternKind { NONE, DOTS, LINES, GRID, WAVES, RAYS, THREADS, RAIN, STARS, ARC, BUBBLES }

enum class VisualStyle(val displayName: String, val cornerDp: Int, val pattern: PatternKind) {
    CURVED("متقوسة", 22, PatternKind.ARC),
    DUSK("انجراف الشفق", 18, PatternKind.WAVES),
    LEVEL("مستوٍ", 12, PatternKind.NONE),
    CUT_LINES("خطوط", 16, PatternKind.LINES),
    DOTTED("تنقّط", 16, PatternKind.DOTS),
    MESH("شبكة", 14, PatternKind.GRID),
    LIGHT_RAYS("أشعة الضوء", 18, PatternKind.RAYS),
    GLOW("انسكاب الوهج", 20, PatternKind.WAVES),
    STRANDS("خيوط", 20, PatternKind.THREADS),
    DRIZZLE("قطرات", 24, PatternKind.RAIN),
    CONSTELLATION("كوكبة", 16, PatternKind.STARS),
    PEBBLE("حصى", 28, PatternKind.BUBBLES);

    companion object {
        fun fromName(name: String) = entries.firstOrNull { it.name.equals(name, true) } ?: CURVED
    }
}

enum class AppBackground(val displayName: String, val hex: Long) {
    CHARCOAL("فحمي", 0xFF14171C),
    OBSIDIAN("أبسديان", 0xFF0B0D10),
    SLATE("رمادي هادئ", 0xFF1B1F26);

    companion object {
        fun fromName(name: String) = entries.firstOrNull { it.name.equals(name, true) } ?: CHARCOAL
    }
}

enum class IconStyle(val displayName: String) {
    OUTLINE("خطي رفيع"),
    BOLD("خطي سميك"),
    DUO("ثنائي الطبقة");

    companion object {
        fun fromName(name: String) = entries.firstOrNull { it.name.equals(name, true) } ?: OUTLINE
    }
}

enum class PageTransition(val displayName: String) {
    FADE("تلاشٍ ناعم"),
    SLIDE("انزلاق جانبي"),
    ZOOM("تكبير عميق"),
    INSTANT("فوري بدون حركة");

    companion object {
        fun fromName(name: String) = entries.firstOrNull { it.name.equals(name, true) } ?: FADE
    }
}

@Composable
fun AmbientPattern(kind: PatternKind, tint: Color, modifier: Modifier = Modifier) {
    if (kind == PatternKind.NONE) return
    Canvas(modifier) {
        val c = tint.copy(alpha = 0.05f)
        when (kind) {
            PatternKind.DOTS -> for (y in 0..24) for (x in 0..16) drawCircle(c, 2f, Offset(x * size.width / 16f, y * size.height / 24f))
            PatternKind.LINES -> for (i in 0..18) drawLine(c, Offset(0f, i * size.height / 18f), Offset(size.width, i * size.height / 18f + 40f), 1.5f)
            PatternKind.GRID -> {
                for (i in 0..10) drawLine(c, Offset(0f, i * size.height / 10f), Offset(size.width, i * size.height / 10f), 1f)
                for (i in 0..8) drawLine(c, Offset(i * size.width / 8f, 0f), Offset(i * size.width / 8f, size.height), 1f)
            }
            PatternKind.WAVES -> {
                val path = Path()
                for (w in 0..3) {
                    path.moveTo(0f, size.height * (0.2f + w * 0.2f))
                    var x = 0f
                    while (x < size.width) {
                        path.quadraticBezierTo(x + size.width / 8f, size.height * (0.1f + w * 0.2f), x + size.width / 4f, size.height * (0.2f + w * 0.2f))
                        x += size.width / 4f
                    }
                }
                drawPath(path, c, style = Stroke(1.5f))
            }
            PatternKind.RAYS -> for (i in 0..7) {
                val a = i * 0.4f
                drawLine(c, Offset(size.width * 0.5f, size.height * 1.1f), Offset(size.width * 0.5f + size.width * cos(a), size.height * 1.1f - size.height * sin(a) * 1.6f), 2f)
            }
            PatternKind.THREADS -> for (i in 0..9) {
                val x0 = size.width * i / 10f
                drawLine(c, Offset(x0, size.height), Offset(x0 + size.width * 0.15f, 0f), 1f)
            }
            PatternKind.RAIN -> {
                val r = Random(77)
                repeat(40) {
                    val x = r.nextFloat() * size.width
                    val y = r.nextFloat() * size.height
                    drawLine(c, Offset(x, y), Offset(x - 6f, y + 22f), 1f)
                }
            }
            PatternKind.STARS -> {
                val r = Random(19)
                repeat(26) { drawCircle(c, 1.5f + r.nextFloat() * 1.5f, Offset(r.nextFloat() * size.width, r.nextFloat() * size.height)) }
            }
            PatternKind.ARC -> for (i in 0..4) drawArc(c, 130f, 80f, false, Offset(-size.width * 0.2f, size.height * (0.1f + i * 0.16f)), Size(size.width * 1.4f, size.height * 0.5f), style = Stroke(1.5f))
            PatternKind.BUBBLES -> {
                val r = Random(5)
                repeat(14) {
                    val rad = size.width * (0.05f + r.nextFloat() * 0.1f)
                    drawCircle(c, rad, Offset(r.nextFloat() * size.width, r.nextFloat() * size.height), style = Stroke(1f))
                }
            }
            else -> Unit
        }
    }
}

@Composable
fun StylePreviewCard(style: VisualStyle, selected: Boolean, accent: Color, onSelect: () -> Unit, modifier: Modifier = Modifier) {
    Column(modifier) {
        Box(Modifier.fillMaxWidth().aspectRatio(0.78f)
            .clip(RoundedCornerShape(style.cornerDp.dp * 2.4f))
            .background(if (selected) accent.copy(alpha = 0.18f) else Color(0xFF101318))
            .border(BorderStroke(if (selected) 1.5.dp else 1.dp, if (selected) accent else GlassStrokeDefault), RoundedCornerShape(style.cornerDp.dp * 2.4f))
            .clickable { onSelect() }) {
            Canvas(Modifier.fillMaxSize().padding(10.dp)) {
                val w = size.width
                val h = size.height
                val pad = w * 0.14f
                val inner = Size(w - pad * 2f, h - pad * 2f)
                drawRoundRect(Color(0xFF181C23), Offset(pad, pad), inner, CornerRadius(style.cornerDp.dp.toPx() * 0.7f))
                drawPattern(style.pattern, Offset(pad, pad), inner, accent.copy(alpha = 0.3f))
                val pill = Color(0xFFB9C1CC).copy(alpha = 0.85f)
                val cx = pad + inner.width / 2f
                drawRoundRect(pill, Offset(cx - inner.width * 0.28f, pad + inner.height * 0.09f), Size(inner.width * 0.56f, inner.height * 0.05f), CornerRadius(inner.height * 0.03f))
                for (i in 0..2) {
                    val y = pad + inner.height * (0.3f + i * 0.12f)
                    drawRoundRect(pill, Offset(pad + inner.width * 0.12f, y), Size(inner.width * (0.76f - i * 0.08f), inner.height * 0.045f), CornerRadius(inner.height * 0.025f))
                }
                drawRoundRect(pill, Offset(cx - inner.width * 0.22f, pad + inner.height * 0.8f), Size(inner.width * 0.44f, inner.height * 0.06f), CornerRadius(inner.height * 0.035f))
            }
            if (selected) Box(Modifier.align(Alignment.TopEnd).padding(10.dp).clip(CircleShape).background(accent).padding(4.dp)) {
                AppIcon(Ic.CHECK, Modifier.size(14.dp), Color(0xFF101318), 2.6.dp)
            }
        }
        Text(style.displayName, fontSize = 12.sp, fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
            color = if (selected) TextWhite else TextMuted, modifier = Modifier.padding(top = 8.dp).fillMaxWidth(),
            textAlign = TextAlign.Center, maxLines = 1)
    }
}

private fun DrawScope.drawPattern(kind: PatternKind, origin: Offset, area: Size, tint: Color) {
    if (kind == PatternKind.NONE) return
    clipRect(origin.x, origin.y, origin.x + area.width, origin.y + area.height) {
        translate(origin.x, origin.y) {
            when (kind) {
                PatternKind.DOTS -> for (y in 0..8) for (x in 0..6) drawCircle(tint, 1.6f, Offset(area.width * x / 6f, area.height * y / 8f))
                PatternKind.LINES -> for (i in 0..5) drawLine(tint, Offset(0f, area.height * i / 5f), Offset(area.width, area.height * i / 5f), 1f)
                PatternKind.GRID -> {
                    for (i in 0..4) drawLine(tint, Offset(0f, area.height * i / 4f), Offset(area.width, area.height * i / 4f), 0.8f)
                    for (i in 0..4) drawLine(tint, Offset(area.width * i / 4f, 0f), Offset(area.width * i / 4f, area.height), 0.8f)
                }
                PatternKind.WAVES -> {
                    val path = Path()
                    for (w in 0..2) {
                        path.moveTo(0f, area.height * (0.25f + w * 0.25f))
                        var x = 0f
                        while (x < area.width) {
                            path.quadraticBezierTo(x + area.width / 6f, area.height * (0.15f + w * 0.25f), x + area.width / 3f, area.height * (0.25f + w * 0.25f))
                            x += area.width / 3f
                        }
                    }
                    drawPath(path, tint, style = Stroke(1f))
                }
                PatternKind.RAYS -> for (i in 0..5) {
                    val a = i * 0.35f
                    drawLine(tint, Offset(area.width * 0.5f, area.height * 1.05f), Offset(area.width * 0.5f + area.width * cos(a), area.height * 1.05f - area.height * sin(a) * 1.4f), 1.4f)
                }
                PatternKind.THREADS -> for (i in 0..6) {
                    val x0 = area.width * i / 7f
                    drawLine(tint, Offset(x0, area.height), Offset(x0 + area.width * 0.12f, 0f), 0.9f)
                }
                PatternKind.RAIN -> {
                    val r = Random(31)
                    repeat(16) {
                        val x = r.nextFloat() * area.width
                        val y = r.nextFloat() * area.height
                        drawLine(tint, Offset(x, y), Offset(x - 3f, y + 9f), 0.9f)
                    }
                }
                PatternKind.STARS -> {
                    val r = Random(11)
                    repeat(10) { drawCircle(tint, 1f + r.nextFloat(), Offset(r.nextFloat() * area.width, r.nextFloat() * area.height)) }
                }
                PatternKind.ARC -> for (i in 0..3) drawArc(tint, 130f, 80f, false, Offset(-area.width * 0.2f, area.height * (0.05f + i * 0.22f)), Size(area.width * 1.4f, area.height * 0.45f), style = Stroke(1f))
                PatternKind.BUBBLES -> {
                    val r = Random(3)
                    repeat(8) {
                        val rad = area.width * (0.04f + r.nextFloat() * 0.08f)
                        drawCircle(tint, rad, Offset(r.nextFloat() * area.width, r.nextFloat() * area.height), style = Stroke(1f))
                    }
                }
                else -> Unit
            }
        }
    }
}
