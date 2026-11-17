package com.darkglass.tv777.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.darkglass.tv777.ui.navigation.AppDestination
import com.darkglass.tv777.ui.theme.BackgroundSurface
import com.darkglass.tv777.ui.theme.LocalGlassAccent
import com.darkglass.tv777.ui.theme.TextMuted
import com.darkglass.tv777.ui.theme.TextWhite
import com.darkglass.tv777.ui.theme.floatingGlassDock
import com.darkglass.tv777.ui.theme.primaryColor

@Composable
fun FloatingGlassDock(currentDestination: AppDestination, onNavigate: (AppDestination) -> Unit, modifier: Modifier = Modifier) {
    val accent = LocalGlassAccent.current
    Box(modifier.navigationBarsPadding().fillMaxWidth().padding(horizontal = 18.dp).padding(top = 10.dp, bottom = 12.dp), contentAlignment = Alignment.Center) {
        Row(Modifier.fillMaxWidth(0.88f).height(62.dp).floatingGlassDock().padding(horizontal = 8.dp, vertical = 5.dp),
            horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically) {
            AppDestination.entries.forEach { dest ->
                val selected = dest == currentDestination
                val scale by animateFloatAsState(if (selected) 1.05f else 1f, spring(), label = "scale")
                val color by animateColorAsState(if (selected) TextWhite else TextMuted, tween(200), label = "color")
                Box(Modifier.weight(1f).scale(scale).clip(RoundedCornerShape(15.dp))
                    .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null) { onNavigate(dest) }
                    .padding(vertical = 4.dp), Alignment.Center) {
                    if (selected) Box(Modifier.matchParentSize().clip(RoundedCornerShape(13.dp)).background(BackgroundSurface))
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        StyleIcon(dest.icon, if (selected) accent.primaryColor else TextMuted, 24.dp)
                        Text(dest.titleAr, fontSize = 11.sp, color = color, modifier = Modifier.padding(top = 2.dp))
                    }
                }
            }
        }
    }
}
