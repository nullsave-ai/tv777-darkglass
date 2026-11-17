package com.darkglass.tv777.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import com.darkglass.tv777.data.model.GlassAccent

val LocalGlassAccent = staticCompositionLocalOf { GlassAccent.SLATE }
val LocalAppBackground = staticCompositionLocalOf { Color(0xFF14171C) }
val LocalIconStyle = staticCompositionLocalOf { IconStyle.OUTLINE }

@Composable
fun Tv777Theme(glassAccent: GlassAccent, backgroundColor: Color, iconStyle: IconStyle, content: @Composable () -> Unit) {
    CompositionLocalProvider(
        LocalGlassAccent provides glassAccent,
        LocalAppBackground provides backgroundColor,
        LocalIconStyle provides iconStyle
    ) { content() }
}

val GlassAccent.primaryColor: Color get() = Color(primaryHex)
val GlassAccent.secondaryColor: Color get() = Color(secondaryHex)
