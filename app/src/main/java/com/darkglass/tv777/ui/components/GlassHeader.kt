package com.darkglass.tv777.ui.components

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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.darkglass.tv777.ui.theme.BackgroundSurface
import com.darkglass.tv777.ui.theme.GlassStrokeDefault
import com.darkglass.tv777.ui.theme.LocalAppBackground
import com.darkglass.tv777.ui.theme.LocalGlassAccent
import com.darkglass.tv777.ui.theme.TextMuted
import com.darkglass.tv777.ui.theme.TextWhite
import com.darkglass.tv777.ui.theme.primaryColor

@Composable
fun GlassHeader(title: String? = null, onActionClick: (() -> Unit)? = null, actionIcon: Ic = Ic.BOLT, modifier: Modifier = Modifier) {
    val accent = LocalGlassAccent.current
    val background = LocalAppBackground.current
    Box(modifier.fillMaxWidth().background(background)) {
        Column(Modifier.fillMaxWidth()) {
            Box(Modifier.fillMaxWidth().windowInsetsTopHeight(WindowInsets.statusBars).background(background))
            Row(Modifier.fillMaxWidth().background(background).padding(horizontal = 16.dp).padding(bottom = 12.dp)) {
                Row(Modifier.fillMaxWidth().clip(RoundedCornerShape(18.dp)).background(BackgroundSurface)
                    .border(1.dp, GlassStrokeDefault, RoundedCornerShape(18.dp))
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(Modifier.size(40.dp).clip(RoundedCornerShape(12.dp)).background(accent.primaryColor), Alignment.Center) {
                            Text("777", fontWeight = FontWeight.Black, fontSize = 15.sp, color = BackgroundSurface)
                        }
                        Spacer(Modifier.width(10.dp))
                        Column {
                            Text(title ?: "777 STREAMING", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                            Text("Live TV Network", fontSize = 10.sp, color = TextMuted)
                        }
                    }
                    if (onActionClick != null) {
                        Box(Modifier.size(36.dp).clip(CircleShape).border(1.dp, GlassStrokeDefault, CircleShape)
                            .clickable { onActionClick() }, Alignment.Center) {
                            AppIcon(actionIcon, Modifier.size(18.dp), TextWhite)
                        }
                    }
                }
            }
        }
    }
}
