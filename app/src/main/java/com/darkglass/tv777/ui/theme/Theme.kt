package com.darkglass.tv777.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import com.darkglass.tv777.data.model.GlassAccent

val LocalGlassAccent = staticCompositionLocalOf { GlassAccent.SLATE }

@Composable
fun Tv777Theme(glassAccent: GlassAccent = GlassAccent.SLATE, content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalGlassAccent provides glassAccent) { content() }
}

val GlassAccent.primaryColor: Color get() = Color(primaryHex)
val GlassAccent.secondaryColor: Color get() = Color(secondaryHex)
