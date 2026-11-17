package com.darkglass.tv777.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.darkglass.tv777.data.model.Channel
import com.darkglass.tv777.ui.theme.AccentGold
import com.darkglass.tv777.ui.theme.AccentLiveRed
import com.darkglass.tv777.ui.theme.AccentNeonBlue
import com.darkglass.tv777.ui.theme.HeroLiveGradient
import com.darkglass.tv777.ui.theme.LocalGlassAccent
import com.darkglass.tv777.ui.theme.TextMuted
import com.darkglass.tv777.ui.theme.TextWhite
import com.darkglass.tv777.ui.theme.darkGlass
import com.darkglass.tv777.ui.theme.primaryColor
import com.darkglass.tv777.ui.theme.secondaryColor

@Composable
fun HeroFeaturedCard(channel: Channel, onClick: () -> Unit, onFavoriteToggle: () -> Unit, modifier: Modifier = Modifier) {
    val accent = LocalGlassAccent.current
    val cc = Color(channel.accentHue)
    Box(modifier.fillMaxWidth().height(200.dp).darkGlass(RoundedCornerShape(26.dp), Color(0x990C1220), cc, elevation = 12.dp).clickable { onClick() }) {
        Box(Modifier.fillMaxSize().background(Brush.linearGradient(listOf(cc.copy(alpha = 0.28f), Color(0x308A3FFC), Color(0xE006080C)), Offset(0f, 0f), Offset(800f, 600f))))
        Box(Modifier.fillMaxSize().background(Brush.verticalGradient(HeroLiveGradient)))
        Row(Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Row(Modifier.clip(RoundedCornerShape(10.dp)).background(Color(0x40000000))
                .border(0.8.dp, AccentLiveRed.copy(alpha = 0.6f), RoundedCornerShape(10.dp))
                .padding(horizontal = 8.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                LiveAudioWave(barCount = 3, color = AccentLiveRed, isPlaying = true)
                Spacer(Modifier.width(6.dp))
                Text("بث حي", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextWhite)
            }
            FavoriteGlassButton(channel.isFavorite, onFavoriteToggle)
        }
        Row(Modifier.fillMaxWidth().align(Alignment.BottomStart).padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Bottom) {
            Column(Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(channel.category.arabicName, fontSize = 11.sp, fontWeight = FontWeight.Medium, color = accent.primaryColor)
                    Spacer(Modifier.width(6.dp))
                    Box(Modifier.clip(RoundedCornerShape(6.dp)).background(Color(0x30FFFFFF)).padding(horizontal = 5.dp, vertical = 2.dp)) {
                        Text(channel.resolution, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }
                Spacer(Modifier.height(4.dp))
                Text(channel.arabicName, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextWhite, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text(channel.bitrate, fontSize = 11.sp, color = TextMuted)
            }
            Box(Modifier.size(46.dp).clip(CircleShape).background(Brush.linearGradient(listOf(accent.primaryColor, accent.secondaryColor)))
                .border(1.dp, Color.White.copy(alpha = 0.5f), CircleShape), Alignment.Center) {
                AppIcon(Ic.PLAY, Modifier.size(22.dp), TextWhite)
            }
        }
    }
}

@Composable
fun StandardChannelCard(channel: Channel, onClick: () -> Unit, onFavoriteToggle: () -> Unit, modifier: Modifier = Modifier) {
    val accent = LocalGlassAccent.current
    val cc = Color(channel.accentHue)
    Box(modifier.fillMaxWidth().height(130.dp).darkGlass(RoundedCornerShape(20.dp), Color(0x800C1220), cc, elevation = 6.dp)
        .clickable { onClick() }.padding(12.dp)) {
        Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
                Box(Modifier.size(38.dp).clip(RoundedCornerShape(12.dp))
                    .background(Brush.linearGradient(listOf(cc.copy(alpha = 0.35f), Color(0x20FFFFFF))))
                    .border(0.8.dp, Brush.linearGradient(listOf(Color.White.copy(alpha = 0.4f), cc.copy(alpha = 0.3f))), RoundedCornerShape(12.dp)), Alignment.Center) {
                    Text(channel.logoText.ifEmpty { "TV" }, fontSize = 12.sp, fontWeight = FontWeight.Black, color = TextWhite)
                }
                FavoriteGlassButton(channel.isFavorite, onFavoriteToggle)
            }
            Column {
                Text(channel.arabicName, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextWhite, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Spacer(Modifier.height(4.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text(channel.category.arabicName, fontSize = 10.sp, color = accent.primaryColor)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        LiveAudioWave(barCount = 2, color = AccentNeonBlue, isPlaying = true)
                        Spacer(Modifier.width(4.dp))
                        Text(channel.resolution, fontSize = 9.sp, fontWeight = FontWeight.SemiBold, color = TextMuted)
                    }
                }
            }
        }
    }
}

@Composable
fun CompactChannelCard(channel: Channel, isSelected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val accent = LocalGlassAccent.current
    val edge = if (isSelected) Brush.horizontalGradient(listOf(accent.primaryColor, accent.secondaryColor))
    else Brush.horizontalGradient(listOf(Color.White.copy(alpha = 0.15f), Color.Transparent))
    Box(modifier.width(140.dp).height(84.dp).clip(RoundedCornerShape(16.dp))
        .background(if (isSelected) Color(0x90162238) else Color(0x600C111C))
        .border(if (isSelected) 1.5.dp else 0.8.dp, edge, RoundedCornerShape(16.dp)).clickable { onClick() }.padding(10.dp)) {
        Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(channel.logoText, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = if (isSelected) accent.primaryColor else TextWhite)
                if (isSelected) Box(Modifier.size(6.dp).clip(CircleShape).background(AccentLiveRed))
            }
            Text(channel.arabicName, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = TextWhite, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(channel.resolution, fontSize = 9.sp, color = TextMuted)
        }
    }
}

@Composable
fun FavoriteGlassButton(isFavorite: Boolean, onToggle: () -> Unit, modifier: Modifier = Modifier) {
    val scale by animateFloatAsState(if (isFavorite) 1.15f else 1f, spring(Spring.DampingRatioMediumBouncy), label = "fav_scale")
    val tint by animateColorAsState(if (isFavorite) AccentGold else Color(0x70FFFFFF), label = "fav_color")
    Box(modifier.size(32.dp).scale(scale).clip(CircleShape).background(Color(0x30101622))
        .border(0.8.dp, Color.White.copy(alpha = 0.2f), CircleShape).clickable { onToggle() }, Alignment.Center) {
        AppIcon(if (isFavorite) Ic.STAR else Ic.STAR_BORDER, Modifier.size(18.dp), tint, 1.6.dp)
    }
}
