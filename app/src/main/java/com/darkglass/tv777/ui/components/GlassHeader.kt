package com.darkglass.tv777.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.darkglass.tv777.ui.theme.AccentLiveRed
import com.darkglass.tv777.ui.theme.LocalGlassAccent
import com.darkglass.tv777.ui.theme.TextMuted
import com.darkglass.tv777.ui.theme.TextWhite
import com.darkglass.tv777.ui.theme.primaryColor
import com.darkglass.tv777.ui.theme.secondaryColor

@Composable
fun GlassHeader(title: String? = null, onActionClick: (() -> Unit)? = null, actionIcon: Ic = Ic.BOLT, modifier: Modifier = Modifier) {
    val accent = LocalGlassAccent.current
    val pulse by rememberInfiniteTransition("hdr").animateFloat(0.95f, 1.15f,
        infiniteRepeatable(tween(1200, easing = FastOutSlowInEasing), RepeatMode.Reverse), "pulse")
    Box(modifier.fillMaxWidth()
        .background(Brush.verticalGradient(listOf(Color(0xF0070A10), Color(0xB0070A10), Color.Transparent)))
        .padding(horizontal = 20.dp, vertical = 14.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.size(46.dp).clip(RoundedCornerShape(14.dp))
                    .background(Brush.linearGradient(listOf(Color(0x35FFFFFF), accent.primaryColor.copy(alpha = 0.35f), accent.secondaryColor.copy(alpha = 0.25f), Color(0x10FFFFFF))))
                    .border(1.dp, Brush.linearGradient(listOf(Color.White.copy(alpha = 0.6f), accent.primaryColor.copy(alpha = 0.4f), Color.Transparent)), RoundedCornerShape(14.dp)),
                    Alignment.Center) {
                    Text("777", fontWeight = FontWeight.Black, fontSize = 18.sp, letterSpacing = (-0.5).sp, color = TextWhite)
                }
                Spacer(Modifier.width(12.dp))
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(title ?: "777 STREAMING", fontSize = 17.sp, fontWeight = FontWeight.Bold, color = TextWhite, letterSpacing = 0.3.sp)
                        Spacer(Modifier.width(8.dp))
                        Row(Modifier.clip(RoundedCornerShape(8.dp)).background(AccentLiveRed.copy(alpha = 0.18f))
                            .border(0.8.dp, AccentLiveRed.copy(alpha = 0.45f), RoundedCornerShape(8.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp), verticalAlignment = Alignment.CenterVertically) {
                            Box(Modifier.size(6.dp).scale(pulse).clip(CircleShape).background(AccentLiveRed))
                            Spacer(Modifier.width(4.dp))
                            Text("مباشر", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                        }
                    }
                    Text("Dark Glass Live TV Network", fontSize = 11.sp, color = TextMuted)
                }
            }
            if (onActionClick != null) {
                Box(Modifier.size(38.dp).clip(CircleShape).background(Color(0x25FFFFFF))
                    .border(0.8.dp, Color.White.copy(alpha = 0.25f), CircleShape).clickable { onActionClick() }, Alignment.Center) {
                    AppIcon(actionIcon, Modifier.size(18.dp), TextWhite)
                }
            }
        }
    }
}
