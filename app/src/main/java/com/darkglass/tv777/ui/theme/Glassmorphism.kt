package com.darkglass.tv777.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.darkGlass(
    shape: Shape = RoundedCornerShape(18.dp),
    backgroundColor: Color = GlassDarkPanel,
    accentGlow: Color? = null,
    borderWidth: Dp = 1.dp,
    elevation: Dp = 4.dp
): Modifier = composed {
    this.clip(shape).background(backgroundColor, shape).border(borderWidth, GlassStrokeDefault, shape)
}

fun Modifier.floatingGlassDock(accentColor: Color = AccentNeonBlue): Modifier = composed {
    val shape = RoundedCornerShape(20.dp)
    this.clip(shape).background(GlassDockBackground, shape).border(1.dp, GlassStrokeDefault, shape)
}
