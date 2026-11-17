package com.darkglass.tv777.ui.screens.player

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.darkglass.tv777.data.model.Channel
import com.darkglass.tv777.ui.components.AppIcon
import com.darkglass.tv777.ui.components.CompactChannelCard
import com.darkglass.tv777.ui.components.Ic
import com.darkglass.tv777.ui.components.LiveAudioWave
import com.darkglass.tv777.ui.theme.AccentLiveRed
import com.darkglass.tv777.ui.theme.BackgroundElevated
import com.darkglass.tv777.ui.theme.GlassStrokeDefault
import com.darkglass.tv777.ui.theme.BackgroundSurface
import com.darkglass.tv777.ui.theme.LocalGlassAccent
import com.darkglass.tv777.ui.theme.TextMuted
import com.darkglass.tv777.ui.theme.TextWhite
import com.darkglass.tv777.ui.theme.darkGlass
import com.darkglass.tv777.ui.theme.primaryColor
import com.darkglass.tv777.ui.theme.secondaryColor

@Composable
fun PlayerControlsOverlay(
    state: PlayerUiState,
    onBackClick: () -> Unit,
    onTogglePlay: () -> Unit,
    onNextChannel: () -> Unit,
    onPrevChannel: () -> Unit,
    onSelectChannel: (Channel) -> Unit,
    onCycleAspectRatio: () -> Unit,
    onToggleQuickChannels: () -> Unit,
    onVolumeChange: (Float) -> Unit,
    onToggleMute: () -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    val accent = LocalGlassAccent.current
    val channel = state.currentChannel
    Box(modifier.fillMaxSize()) {
        if (state.isLoading) {
            Box(Modifier.align(Alignment.Center).darkGlass(RoundedCornerShape(22.dp), BackgroundSurface).padding(24.dp), Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(Modifier.size(44.dp), color = accent.primaryColor, strokeWidth = 3.dp)
                    Spacer(Modifier.height(14.dp))
                    Text("جاري الاتصال بالبث المباشر…", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = TextWhite)
                }
            }
        }
        if (state.errorMessage != null) {
            Box(Modifier.align(Alignment.Center).padding(32.dp).darkGlass(RoundedCornerShape(24.dp), BackgroundSurface, AccentLiveRed).padding(24.dp), Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    AppIcon(Ic.WARNING, Modifier.size(40.dp), AccentLiveRed, 2.4.dp)
                    Spacer(Modifier.height(10.dp))
                    Text("تعذر تشغيل البث المباشر", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    Spacer(Modifier.height(6.dp))
                    Text(state.errorMessage, fontSize = 12.sp, color = TextMuted, textAlign = TextAlign.Center)
                    Spacer(Modifier.height(16.dp))
                    Box(Modifier.clip(RoundedCornerShape(14.dp))
                        .background(accent.primaryColor)
                        .clickable { onRetry() }.padding(horizontal = 24.dp, vertical = 10.dp)) {
                        Text("إعادة المحاولة", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }
            }
        }
        AnimatedVisibility(state.areControlsVisible, enter = fadeIn(), exit = fadeOut()) {
            Box(Modifier.fillMaxSize()) {
                Box(Modifier.fillMaxWidth().align(Alignment.TopCenter)
                    .background(BackgroundSurface)
                    .padding(horizontal = 20.dp, vertical = 18.dp)) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Box(Modifier.size(42.dp).darkGlass(CircleShape, BackgroundElevated).clickable { onBackClick() }, Alignment.Center) {
                            AppIcon(Ic.CLOSE, Modifier.size(20.dp), TextWhite)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                LiveAudioWave(barCount = 3, color = AccentLiveRed, isPlaying = state.isPlaying)
                                Spacer(Modifier.width(6.dp))
                                Text(channel?.arabicName ?: "777 Live", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                            }
                            Text("${channel?.category?.arabicName ?: "قناة"} • ${channel?.bitrate ?: "1080p"}", fontSize = 11.sp, color = TextMuted)
                        }
                        Box(Modifier.clip(RoundedCornerShape(12.dp)).background(BackgroundElevated)
                            .border(1.dp, Color.White.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                            .clickable { onCycleAspectRatio() }.padding(horizontal = 10.dp, vertical = 6.dp), Alignment.Center) {
                            Text(state.aspectRatio.name, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = accent.primaryColor)
                        }
                    }
                }
                Column(Modifier.fillMaxWidth().align(Alignment.BottomCenter)
                    .background(BackgroundSurface)
                    .padding(horizontal = 20.dp, vertical = 20.dp)) {
                    AnimatedVisibility(state.isQuickChannelsOpen,
                        enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                        exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()) {
                        Column(Modifier.padding(bottom = 14.dp)) {
                            Text("قائمة القنوات السريعة", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextWhite, modifier = Modifier.padding(bottom = 8.dp))
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp), contentPadding = PaddingValues(horizontal = 4.dp)) {
                                items(state.allChannels, key = { it.id }) { c ->
                                    CompactChannelCard(c, c.id == channel?.id, { onSelectChannel(c) })
                                }
                            }
                        }
                    }
                    Box(Modifier.fillMaxWidth().darkGlass(RoundedCornerShape(26.dp), BackgroundSurface, accent.primaryColor, elevation = 16.dp)
                        .padding(horizontal = 16.dp, vertical = 12.dp)) {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.width(110.dp)) {
                                Box(Modifier.size(24.dp).clickable { onToggleMute() }, Alignment.Center) {
                                    AppIcon(if (state.isMuted) Ic.MUTE else Ic.VOLUME, Modifier.size(20.dp), TextWhite)
                                }
                                Spacer(Modifier.width(6.dp))
                                Slider(if (state.isMuted) 0f else state.volume, { onVolumeChange(it) },
                                    colors = SliderDefaults.colors(thumbColor = accent.primaryColor,
                                        activeTrackColor = accent.primaryColor, inactiveTrackColor = GlassStrokeDefault),
                                    modifier = Modifier.weight(1f))
                            }
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                                Box(Modifier.size(38.dp).clip(CircleShape).background(GlassStrokeDefault).clickable { onPrevChannel() }, Alignment.Center) {
                                    AppIcon(Ic.PREV, Modifier.size(20.dp), TextWhite)
                                }
                                Box(Modifier.size(54.dp).clip(CircleShape)
                                    .background(accent.primaryColor)
                                    .border(1.2.dp, Color.White.copy(alpha = 0.5f), CircleShape).clickable { onTogglePlay() }, Alignment.Center) {
                                    AppIcon(if (state.isPlaying) Ic.PAUSE else Ic.PLAY, Modifier.size(24.dp), TextWhite)
                                }
                                Box(Modifier.size(38.dp).clip(CircleShape).background(GlassStrokeDefault).clickable { onNextChannel() }, Alignment.Center) {
                                    AppIcon(Ic.NEXT, Modifier.size(20.dp), TextWhite)
                                }
                            }
                            Box(Modifier.clip(RoundedCornerShape(12.dp))
                                .background(if (state.isQuickChannelsOpen) accent.primaryColor.copy(alpha = 0.35f) else GlassStrokeDefault)
                                .border(0.8.dp, if (state.isQuickChannelsOpen) accent.primaryColor else Color.White.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                                .clickable { onToggleQuickChannels() }.padding(horizontal = 10.dp, vertical = 8.dp),
                                Alignment.Center) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    AppIcon(Ic.PLAYLIST, Modifier.size(16.dp), TextWhite)
                                    Spacer(Modifier.width(4.dp))
                                    Text("القنوات", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
