package com.darkglass.tv777.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.darkglass.tv777.ui.theme.IconStyle
import com.darkglass.tv777.ui.theme.LocalIconStyle

@Composable
fun StyleIcon(name: Ic, tint: Color, size: Dp = 24.dp, modifier: Modifier = Modifier, forceBold: Boolean = false) {
    val style = LocalIconStyle.current
    when {
        forceBold -> AppIcon(name, modifier.size(size), tint, 2.6.dp)
        style == IconStyle.BOLD -> AppIcon(name, modifier.size(size), tint, 2.8.dp)
        style == IconStyle.DUO -> Box(modifier.size(size)) {
            AppIcon(name, Modifier.offset(1.5.dp, 1.5.dp).size(size), tint.copy(alpha = 0.35f), 2.dp)
            AppIcon(name, Modifier.size(size), tint, 2.dp)
        }
        else -> AppIcon(name, modifier.size(size), tint, 2.dp)
    }
}
