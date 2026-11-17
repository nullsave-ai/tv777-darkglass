package com.darkglass.tv777.ui.screens.home

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.darkglass.tv777.data.model.Channel
import com.darkglass.tv777.data.model.ChannelCategory
import com.darkglass.tv777.ui.components.GlassHeader
import com.darkglass.tv777.ui.components.HeroFeaturedCard
import com.darkglass.tv777.ui.components.StandardChannelCard
import com.darkglass.tv777.ui.theme.BackgroundSurface
import com.darkglass.tv777.ui.theme.BackgroundElevated
import com.darkglass.tv777.ui.theme.BackgroundDeep
import com.darkglass.tv777.ui.theme.LocalGlassAccent
import com.darkglass.tv777.ui.theme.TextMuted
import com.darkglass.tv777.ui.theme.TextWhite
import com.darkglass.tv777.ui.theme.darkGlass
import com.darkglass.tv777.ui.theme.primaryColor
import com.darkglass.tv777.ui.theme.secondaryColor

@Composable
fun HomeScreen(viewModel: HomeViewModel, onChannelClick: (Channel) -> Unit, modifier: Modifier = Modifier) {
    val state by viewModel.uiState.collectAsState()
    val accent = LocalGlassAccent.current
    Box(modifier.fillMaxSize().background(BackgroundDeep)) {
        LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(bottom = 110.dp)) {
            item { GlassHeader("777 STREAMING") }
            item { CategoryPillsRow(state.selectedCategory, { viewModel.selectCategory(it) }, Modifier.padding(vertical = 12.dp)) }
            if (state.featuredChannels.isNotEmpty()) {
                item { SectionTitle("القنوات المميزة", "أبرز البثوث المباشرة بأعلى جودة") }
                item {
                    LazyRow(contentPadding = PaddingValues(horizontal = 20.dp), horizontalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.padding(bottom = 20.dp)) {
                        items(state.featuredChannels, key = { it.id }) { c ->
                            HeroFeaturedCard(c, { onChannelClick(c) }, { viewModel.toggleFavorite(c.id) }, Modifier.width(320.dp))
                        }
                    }
                }
            }
            if (state.favoriteChannels.isNotEmpty()) {
                item { SectionTitle("القنوات المفضلة", "قنواتك المختارة للوصول الفوري") }
                item {
                    LazyRow(contentPadding = PaddingValues(horizontal = 20.dp), horizontalArrangement = Arrangement.spacedBy(14.dp), modifier = Modifier.padding(bottom = 20.dp)) {
                        items(state.favoriteChannels, key = { it.id }) { c ->
                            StandardChannelCard(c, { onChannelClick(c) }, { viewModel.toggleFavorite(c.id) }, Modifier.width(170.dp))
                        }
                    }
                }
            }
            if (state.recentChannels.isNotEmpty()) {
                item { SectionTitle("آخر القنوات مشاهدة", "استكمال المشاهدة السريعة") }
                item {
                    LazyRow(contentPadding = PaddingValues(horizontal = 20.dp), horizontalArrangement = Arrangement.spacedBy(14.dp), modifier = Modifier.padding(bottom = 20.dp)) {
                        items(state.recentChannels, key = { it.id }) { c ->
                            StandardChannelCard(c, { onChannelClick(c) }, { viewModel.toggleFavorite(c.id) }, Modifier.width(170.dp))
                        }
                    }
                }
            }
            item {
                Box(Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 8.dp)
                    .darkGlass(RoundedCornerShape(20.dp), BackgroundSurface, accent.primaryColor).padding(16.dp)) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Column {
                            Text("شبكة 777 البثوث المباشرة", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                            Text("${state.totalLiveCount} قناة نشطة بدون انقطاع", fontSize = 12.sp, color = TextMuted)
                        }
                        Box(Modifier.clip(RoundedCornerShape(12.dp)).background(accent.primaryColor.copy(alpha = 0.2f))
                            .border(1.dp, accent.primaryColor.copy(alpha = 0.5f), RoundedCornerShape(12.dp)).padding(horizontal = 10.dp, vertical = 6.dp)) {
                            Text("4K / FHD", fontSize = 11.sp, fontWeight = FontWeight.Black, color = TextWhite)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SectionTitle(title: String, subtitle: String, modifier: Modifier = Modifier) {
    Column(modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 8.dp)) {
        Text(title, fontSize = 17.sp, fontWeight = FontWeight.Bold, color = TextWhite)
        Text(subtitle, fontSize = 11.sp, color = TextMuted)
    }
}

@Composable
private fun CategoryPillsRow(selected: ChannelCategory, onSelect: (ChannelCategory) -> Unit, modifier: Modifier = Modifier) {
    val accent = LocalGlassAccent.current
    LazyRow(modifier.fillMaxWidth(), contentPadding = PaddingValues(horizontal = 20.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        items(ChannelCategory.entries, key = { it.id }) { cat ->
            val active = cat == selected
            Box(Modifier.clip(RoundedCornerShape(16.dp))
                .background(if (active) accent.primaryColor.copy(alpha = 0.35f) else BackgroundElevated)
                .border(1.dp, if (active) accent.primaryColor.copy(alpha = 0.8f) else Color.White.copy(alpha = 0.12f), RoundedCornerShape(16.dp))
                .clickable { onSelect(cat) }.padding(horizontal = 14.dp, vertical = 8.dp), Alignment.Center) {
                Text(cat.arabicName, fontSize = 12.sp, fontWeight = if (active) FontWeight.Bold else FontWeight.Normal, color = if (active) TextWhite else TextMuted)
            }
        }
    }
}
