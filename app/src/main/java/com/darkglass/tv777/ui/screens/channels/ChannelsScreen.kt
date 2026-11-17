package com.darkglass.tv777.ui.screens.channels

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.darkglass.tv777.data.model.Channel
import com.darkglass.tv777.data.model.ChannelCategory
import com.darkglass.tv777.ui.components.AppIcon
import com.darkglass.tv777.ui.components.GlassHeader
import com.darkglass.tv777.ui.components.Ic
import com.darkglass.tv777.ui.components.StandardChannelCard
import com.darkglass.tv777.ui.theme.AccentGold
import com.darkglass.tv777.ui.theme.BackgroundSurface
import com.darkglass.tv777.ui.theme.BackgroundElevated
import com.darkglass.tv777.ui.theme.LocalAppBackground
import com.darkglass.tv777.ui.theme.LocalGlassAccent
import com.darkglass.tv777.ui.theme.TextMuted
import com.darkglass.tv777.ui.theme.TextWhite
import com.darkglass.tv777.ui.theme.darkGlass
import com.darkglass.tv777.ui.theme.primaryColor
import com.darkglass.tv777.ui.theme.secondaryColor

@Composable
fun ChannelsScreen(viewModel: ChannelsViewModel, onChannelClick: (Channel) -> Unit, modifier: Modifier = Modifier) {
    val state by viewModel.uiState.collectAsState()
    val accent = LocalGlassAccent.current
    Box(modifier.fillMaxSize().background(LocalAppBackground.current)) {
        Column(Modifier.fillMaxSize().padding(bottom = 90.dp)) {
            GlassHeader("دليل القنوات المباشرة", { viewModel.refreshChannels() }, Ic.REFRESH)
            AnimatedVisibility(state.statusMessage != null, enter = fadeIn(), exit = fadeOut()) {
                state.statusMessage?.let { msg ->
                    Box(Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 6.dp).clip(RoundedCornerShape(12.dp))
                        .background(accent.primaryColor.copy(alpha = 0.25f))
                        .border(1.dp, accent.primaryColor.copy(alpha = 0.6f), RoundedCornerShape(12.dp))
                        .padding(vertical = 8.dp), Alignment.Center) {
                        Text(msg, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = TextWhite)
                    }
                }
            }
            Row(Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.weight(1f).height(48.dp).darkGlass(RoundedCornerShape(16.dp), BackgroundSurface)
                    .padding(horizontal = 14.dp), Alignment.CenterStart) {
                    if (state.searchQuery.isEmpty()) Text("ابحث عن اسم القناة أو التصنيف…", fontSize = 13.sp, color = TextMuted)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        AppIcon(Ic.SEARCH, Modifier.size(16.dp), accent.primaryColor)
                        Spacer(Modifier.width(8.dp))
                        BasicTextField(state.searchQuery, { viewModel.onSearchQueryChanged(it) }, singleLine = true,
                            cursorBrush = SolidColor(accent.primaryColor),
                            textStyle = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Medium, color = TextWhite),
                            modifier = Modifier.fillMaxWidth())
                    }
                }
                Spacer(Modifier.width(10.dp))
                Box(Modifier.size(48.dp).darkGlass(RoundedCornerShape(16.dp),
                    if (state.showFavoritesOnly) AccentGold.copy(alpha = 0.25f) else BackgroundSurface,
                    if (state.showFavoritesOnly) AccentGold else null).clickable { viewModel.toggleFavoritesFilter() }, Alignment.Center) {
                    AppIcon(Ic.STAR, Modifier.size(20.dp), if (state.showFavoritesOnly) AccentGold else TextMuted)
                }
                Spacer(Modifier.width(8.dp))
                Box(Modifier.size(48.dp).darkGlass(RoundedCornerShape(16.dp), BackgroundSurface)
                    .clickable { viewModel.toggleViewMode() }, Alignment.Center) {
                    AppIcon(if (state.isGridView) Ic.GRID else Ic.LIST, Modifier.size(20.dp), accent.primaryColor)
                }
            }
            LazyRow(Modifier.fillMaxWidth().padding(vertical = 8.dp), contentPadding = PaddingValues(horizontal = 20.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(ChannelCategory.entries, key = { it.id }) { cat ->
                    val active = cat == state.selectedCategory
                    Box(Modifier.clip(RoundedCornerShape(14.dp))
                        .background(if (active) accent.primaryColor.copy(alpha = 0.35f) else BackgroundElevated)
                        .border(1.dp, if (active) accent.primaryColor.copy(alpha = 0.8f) else androidx.compose.ui.graphics.Color.White.copy(alpha = 0.12f), RoundedCornerShape(14.dp))
                        .clickable { viewModel.selectCategory(cat) }.padding(horizontal = 12.dp, vertical = 7.dp), Alignment.Center) {
                        Text(cat.arabicName, fontSize = 12.sp, fontWeight = if (active) FontWeight.Bold else FontWeight.Normal, color = if (active) TextWhite else TextMuted)
                    }
                }
            }
            Row(Modifier.fillMaxWidth().padding(horizontal = 22.dp, vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("${state.channels.size} قناة متاحة", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = TextMuted)
                if (state.isRefreshing) Text("جاري التحديث…", fontSize = 11.sp, color = accent.primaryColor)
            }
            if (state.channels.isEmpty()) {
                Box(Modifier.fillMaxSize().padding(32.dp), Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.darkGlass(RoundedCornerShape(24.dp), BackgroundSurface).padding(28.dp)) {
                        AppIcon(Ic.ANTENNA, Modifier.size(48.dp), accent.primaryColor, 2.dp)
                        Spacer(Modifier.height(12.dp))
                        Text("لم يتم العثور على أي قناة", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                        Spacer(Modifier.height(6.dp))
                        Text("جرب البحث باسم آخر أو اختر تصنيفاً مختلفاً", fontSize = 12.sp, color = TextMuted, textAlign = TextAlign.Center)
                    }
                }
            } else if (state.isGridView) {
                LazyVerticalGrid(GridCells.Fixed(2), contentPadding = PaddingValues(horizontal = 18.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp), verticalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxSize()) {
                    items(state.channels, key = { it.id }) { c ->
                        StandardChannelCard(c, { onChannelClick(c) }, { viewModel.toggleFavorite(c.id) })
                    }
                }
            } else {
                LazyColumn(contentPadding = PaddingValues(horizontal = 18.dp, vertical = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxSize()) {
                    items(state.channels, key = { it.id }) { c ->
                        StandardChannelCard(c, { onChannelClick(c) }, { viewModel.toggleFavorite(c.id) })
                    }
                }
            }
        }
    }
}
