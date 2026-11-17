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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.darkglass.tv777.data.model.Channel
import com.darkglass.tv777.ui.theme.AccentGold
import com.darkglass.tv777.ui.theme.AccentLiveRed
import com.darkglass.tv777.ui.theme.BackgroundElevated
import com.darkglass.tv777.ui.theme.BackgroundSurface
import com.darkglass.tv777.ui.theme.GlassStrokeDefault
import com.darkglass.tv777.ui.theme.LocalGlassAccent
import com.darkglass.tv777.ui.theme.TextMuted
import com.darkglass.tv777.ui.theme.TextWhite
import com.darkglass.tv777.ui.theme.darkGlass
import com.darkglass.tv777.ui.theme.primaryColor

@Composable
fun HeroFeaturedCard(channel: Channel, onClick: () -> Unit, onFavoriteToggle: () -> Unit, modifier: Modifier = Modifier) {
    val accent = LocalGlassAccent.current
    Box(modifier.fillMaxWidth().height(200.dp).darkGlass(RoundedCornerShape(20.dp), BackgroundSurface)
        .clickable { onClick() }) {
        Row(Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Row(Modifier.clip(RoundedCornerShape(8.dp)).background(BackgroundElevated)
                .border(1.dp, GlassStrokeDefault, RoundedCornerShape(8.dp))
                .padding(horizontal = 8.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                LiveAudioWave(barCount = 3, color = AccentLiveRed, isPlaying = true)
                Spacer(Modifier.width(6.dp))
                Text("بث حي", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextWhite)
            }
            FavoriteGlassButton(channel.isFavorite, onFavoriteToggle)
        }
        Row(Modifier.fillMaxWidth().align(Alignment.BottomStart).padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Bottom) {
            Column(Modifier.weight(1f)) {
                Text(channel.category.arabicName, fontSize = 11.sp, fontWeight = FontWeight.Medium, color = accent.primaryColor)
                Spacer(Modifier.height(4.dp))
                Text(channel.arabicName, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextWhite, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text(channel.bitrate, fontSize = 11.sp, color = TextMuted)
            }
            Box(Modifier.size(46.dp).clip(CircleShape).background(accent.primaryColor), Alignment.Center) {
                AppIcon(Ic.PLAY, Modifier.size(22.dp), BackgroundSurface)
            }
        }
    }
}

@Composable
fun StandardChannelCard(channel: Channel, onClick: () -> Unit, onFavoriteToggle: () -> Unit, modifier: Modifier = Modifier) {
    val accent = LocalGlassAccent.current
    Box(modifier.fillMaxWidth().height(130.dp).darkGlass(RoundedCornerShape(18.dp), BackgroundSurface)
        .clickable { onClick() }.padding(12.dp)) {
        Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
                Box(Modifier.size(38.dp).clip(RoundedCornerShape(10.dp)).background(BackgroundElevated), Alignment.Center) {
                    Text(channel.logoText.ifEmpty { "TV" }, fontSize = 12.sp, fontWeight = FontWeight.Black, color = accent.primaryColor)
                }
                FavoriteGlassButton(channel.isFavorite, onFavoriteToggle)
            }
            Column {
                Text(channel.arabicName, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextWhite, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Spacer(Modifier.height(4.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text(channel.category.arabicName, fontSize = 10.sp, color = accent.primaryColor)
                    Text(channel.resolution, fontSize = 9.sp, fontWeight = FontWeight.SemiBold, color = TextMuted)
                }
            }
        }
    }
}

@Composable
fun CompactChannelCard(channel: Channel, isSelected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val accent = LocalGlassAccent.current
    Box(modifier.width(140.dp).height(84.dp).clip(RoundedCornerShape(14.dp))
        .background(if (isSelected) BackgroundElevated else BackgroundSurface)
        .border(if (isSelected) 1.5.dp else 1.dp, if (isSelected) accent.primaryColor else GlassStrokeDefault, RoundedCornerShape(14.dp))
        .clickable { onClick() }.padding(10.dp)) {
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
    val scale by animateFloatAsState(if (isFavorite) 1.1f else 1f, spring(Spring.DampingRatioMediumBouncy), label = "fav_scale")
    val tint by animateColorAsState(if (isFavorite) AccentGold else TextMuted, label = "fav_color")
    Box(modifier.size(32.dp).scale(scale).clip(CircleShape).background(BackgroundElevated)
        .border(1.dp, GlassStrokeDefault, CircleShape).clickable { onToggle() }, Alignment.Center) {
        AppIcon(if (isFavorite) Ic.STAR else Ic.STAR_BORDER, Modifier.size(18.dp), tint, 1.6.dp)
    }
}
