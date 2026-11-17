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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
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
fun PlayerControlsOverlay(
    state: PlayerUiState,
    onBackClick: () -> Unit,
    onTogglePlay: () -> Unit,
    onNextChannel: () -> Unit,
    onPrevChannel: () -> Unit,
    onSelectChannel: (Channel) -> Unit,
    onCycleAspectRatio: () -> Unit,
    onCycleSpeed: () -> Unit,
    onToggleFavorite: () -> Unit,
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
                    Box(Modifier.clip(RoundedCornerShape(14.dp)).background(accent.primaryColor)
                        .clickable { onRetry() }.padding(horizontal = 24.dp, vertical = 10.dp)) {
                        Text("إعادة المحاولة", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }
                }
            }
        }
        AnimatedVisibility(state.areControlsVisible, enter = fadeIn(), exit = fadeOut()) {
            Box(Modifier.fillMaxSize()) {
                Column(Modifier.fillMaxWidth().align(Alignment.TopCenter).statusBarsPadding().background(BackgroundSurface).padding(horizontal = 16.dp, vertical = 12.dp)) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Box(Modifier.size(40.dp).darkGlass(CircleShape, BackgroundElevated).clickable { onBackClick() }, Alignment.Center) {
                            AppIcon(Ic.CLOSE, Modifier.size(18.dp), TextWhite)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                LiveAudioWave(barCount = 3, color = AccentLiveRed, isPlaying = state.isPlaying)
                                Spacer(Modifier.width(6.dp))
                                Text(channel?.arabicName ?: "777 Live", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextWhite, maxLines = 1)
                            }
                            Text("${channel?.category?.arabicName ?: "قناة"} • ${channel?.bitrate ?: "FHD"}", fontSize = 10.sp, color = TextMuted)
                        }
                        Box(Modifier.size(40.dp).darkGlass(CircleShape, BackgroundElevated).clickable { onCycleAspectRatio() }, Alignment.Center) {
                            Text(state.aspectRatio.name.first().toString(), fontSize = 13.sp, fontWeight = FontWeight.Black, color = accent.primaryColor)
                        }
                    }
                }
                Column(Modifier.fillMaxWidth().align(Alignment.BottomCenter).padding(horizontal = 14.dp)) {
                    AnimatedVisibility(state.isQuickChannelsOpen,
                        enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                        exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()) {
                        Column(Modifier.padding(bottom = 10.dp).darkGlass(RoundedCornerShape(20.dp), BackgroundSurface).padding(12.dp)) {
                            Text("قائمة القنوات السريعة", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextWhite, modifier = Modifier.padding(bottom = 8.dp))
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp), contentPadding = PaddingValues(horizontal = 2.dp)) {
                                items(state.allChannels, key = { it.id }) { c ->
                                    CompactChannelCard(c, c.id == channel?.id, { onSelectChannel(c) })
                                }
                            }
                        }
                    }
                    Column(Modifier.fillMaxWidth().darkGlass(RoundedCornerShape(24.dp), BackgroundSurface).padding(horizontal = 14.dp, vertical = 10.dp)) {
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp), contentPadding = PaddingValues(bottom = 10.dp)) {
                            item {
                                FeatureChip(if (state.isQuickChannelsOpen) accent.primaryColor.copy(alpha = 0.3f) else BackgroundElevated,
                                    if (state.isQuickChannelsOpen) accent.primaryColor else GlassStrokeDefault, { onToggleQuickChannels() }) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        AppIcon(Ic.PLAYLIST, Modifier.size(15.dp), if (state.isQuickChannelsOpen) accent.primaryColor else TextWhite)
                                        Spacer(Modifier.width(4.dp))
                                        Text("القنوات", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                                    }
                                }
                            }
                            item {
                                FeatureChip(BackgroundElevated, GlassStrokeDefault, { onCycleSpeed() }) {
                                    Text("${state.playbackSpeed}x", fontSize = 12.sp, fontWeight = FontWeight.Black, color = accent.primaryColor)
                                    Spacer(Modifier.width(4.dp))
                                    Text("السرعة", fontSize = 12.sp, color = TextMuted)
                                }
                            }
                            item {
                                FeatureChip(BackgroundElevated, GlassStrokeDefault, { onCycleAspectRatio() }) {
                                    Text(state.aspectRatio.name, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                                }
                            }
                            item {
                                FeatureChip(if (state.isFavorite) AccentGold.copy(alpha = 0.2f) else BackgroundElevated,
                                    if (state.isFavorite) AccentGold else GlassStrokeDefault, { onToggleFavorite() }) {
                                    AppIcon(if (state.isFavorite) Ic.STAR else Ic.STAR_BORDER, Modifier.size(16.dp),
                                        if (state.isFavorite) AccentGold else TextMuted, 1.8.dp)
                                    Spacer(Modifier.width(4.dp))
                                    Text("المفضلة", fontSize = 12.sp, color = if (state.isFavorite) AccentGold else TextMuted)
                                }
                            }
                        }
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                                Box(Modifier.size(28.dp).clickable { onToggleMute() }, Alignment.Center) {
                                    AppIcon(if (state.isMuted) Ic.MUTE else Ic.VOLUME, Modifier.size(20.dp), TextWhite)
                                }
                                Slider(if (state.isMuted) 0f else state.volume, { onVolumeChange(it) },
                                    colors = SliderDefaults.colors(thumbColor = accent.primaryColor,
                                        activeTrackColor = accent.primaryColor, inactiveTrackColor = BackgroundElevated),
                                    modifier = Modifier.weight(1f))
                            }
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                Box(Modifier.size(38.dp).clip(CircleShape).background(BackgroundElevated)
                                    .border(1.dp, GlassStrokeDefault, CircleShape).clickable { onPrevChannel() }, Alignment.Center) {
                                    AppIcon(Ic.PREV, Modifier.size(18.dp), TextWhite)
                                }
                                Box(Modifier.size(52.dp).clip(CircleShape).background(accent.primaryColor)
                                    .border(1.dp, Color.White.copy(alpha = 0.3f), CircleShape).clickable { onTogglePlay() }, Alignment.Center) {
                                    AppIcon(if (state.isPlaying) Ic.PAUSE else Ic.PLAY, Modifier.size(22.dp), BackgroundSurface)
                                }
                                Box(Modifier.size(38.dp).clip(CircleShape).background(BackgroundElevated)
                                    .border(1.dp, GlassStrokeDefault, CircleShape).clickable { onNextChannel() }, Alignment.Center) {
                                    AppIcon(Ic.NEXT, Modifier.size(18.dp), TextWhite)
                                }
                            }
                        }
                    }
                    Spacer(Modifier.navigationBarsPadding().height(12.dp))
                }
            }
        }
    }
}

@Composable
private fun FeatureChip(background: Color, border: Color, onClick: () -> Unit, content: @Composable () -> Unit) {
    Box(Modifier.clip(RoundedCornerShape(14.dp)).background(background)
        .border(1.dp, border, RoundedCornerShape(14.dp))
        .clickable { onClick() }.padding(horizontal = 12.dp, vertical = 8.dp), Alignment.Center) {
        content()
    }
}
