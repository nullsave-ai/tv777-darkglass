package com.darkglass.tv777.ui.theme

import android.graphics.RenderEffect
import android.graphics.RuntimeShader
import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

private const val GLASS_AGSL = """
    uniform shader composable;
    uniform float2 size;
    uniform float4 glowColor;
    half4 main(float2 coord) {
        half4 color = composable.eval(coord);
        float dist = length((coord / size) - float2(0.5, 0.5));
        half4 glow = glowColor * (1.0 - smoothstep(0.0, 0.7, dist)) * 0.12;
        return color + glow;
    }
"""

fun Modifier.darkGlass(
    shape: Shape = RoundedCornerShape(18.dp),
    backgroundColor: Color = GlassDarkPanel,
    accentGlow: Color? = null,
    borderWidth: Dp = 1.dp,
    elevation: Dp = 4.dp
): Modifier = composed {
    val glow = accentGlow ?: AccentNeonBlue
    val edge = Brush.linearGradient(
        listOf(Color.White.copy(alpha = 0.32f), glow.copy(alpha = 0.22f), Color.White.copy(alpha = 0.06f), Color.Transparent),
        Offset(0f, 0f), Offset(400f, 400f)
    )
    val fill = Brush.verticalGradient(listOf(backgroundColor.copy(alpha = 0.78f), backgroundColor.copy(alpha = 0.62f), backgroundColor.copy(alpha = 0.85f)))
    this.shadow(elevation, shape, clip = false, ambientColor = Color.Black, spotColor = glow.copy(alpha = 0.35f))
        .clip(shape)
        .background(fill, shape)
        .border(borderWidth, edge, shape)
        .drawBehind {
            drawLine(
                Brush.horizontalGradient(listOf(Color.White.copy(alpha = 0.25f), glow.copy(alpha = 0.15f), Color.Transparent)),
                Offset(12f, 1.5f), Offset(size.width * 0.65f, 1.5f), 2f
            )
        }
}

fun Modifier.floatingGlassDock(accentColor: Color = AccentNeonBlue): Modifier = composed {
    val shape = RoundedCornerShape(32.dp)
    val edge = Brush.linearGradient(
        listOf(Color.White.copy(alpha = 0.4f), accentColor.copy(alpha = 0.35f), AccentElectricPurple.copy(alpha = 0.25f), Color.White.copy(alpha = 0.08f)),
        Offset(0f, 0f), Offset(1000f, 150f)
    )
    this.shadow(16.dp, shape, clip = false, ambientColor = Color.Black.copy(alpha = 0.8f), spotColor = accentColor.copy(alpha = 0.4f))
        .clip(shape)
        .background(Brush.verticalGradient(listOf(GlassDockBackground.copy(alpha = 0.9f), Color(0xFF090D18).copy(alpha = 0.82f), Color(0xFF05070D).copy(alpha = 0.94f))), shape)
        .border(1.2.dp, edge, shape)
        .drawBehind {
            drawLine(
                Brush.horizontalGradient(listOf(Color.Transparent, Color.White.copy(alpha = 0.45f), accentColor.copy(alpha = 0.35f), Color.Transparent)),
                Offset(size.width * 0.15f, 1.5f), Offset(size.width * 0.85f, 1.5f), 2.5f
            )
        }
}

fun Modifier.ambientBackdropGlow(glowColor: Color = AccentNeonBlue, alpha: Float = 0.18f): Modifier = this.drawBehind {
    drawCircle(
        Brush.radialGradient(listOf(glowColor.copy(alpha = alpha), glowColor.copy(alpha = alpha * 0.4f), Color.Transparent),
            center = Offset(size.width * 0.5f, size.height * 0.2f), radius = size.width * 0.8f)
    )
}

fun Modifier.optionalGlassShader(accentColor: Color): Modifier = composed {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        try {
            val shader = RuntimeShader(GLASS_AGSL)
            shader.setColorUniform("glowColor", android.graphics.Color.valueOf(accentColor.red, accentColor.green, accentColor.blue, accentColor.alpha))
            val effect = RenderEffect.createRuntimeShaderEffect(shader, "composable")
            this.graphicsLayer { renderEffect = effect.asComposeRenderEffect() }
        } catch (_: Throwable) {
            this
        }
    } else this
}
