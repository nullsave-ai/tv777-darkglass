package com.darkglass.tv777.ui.screens.settings

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
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.darkglass.tv777.data.model.AspectRatioMode
import com.darkglass.tv777.data.model.BufferStrategy
import com.darkglass.tv777.data.model.GlassAccent
import com.darkglass.tv777.ui.components.AppIcon
import com.darkglass.tv777.ui.components.GlassHeader
import com.darkglass.tv777.ui.components.Ic
import com.darkglass.tv777.ui.components.StyleIcon
import com.darkglass.tv777.ui.theme.AmbientPattern
import com.darkglass.tv777.ui.theme.AppBackground
import com.darkglass.tv777.ui.theme.BackgroundElevated
import com.darkglass.tv777.ui.theme.BackgroundSurface
import com.darkglass.tv777.ui.theme.GlassStrokeDefault
import com.darkglass.tv777.ui.theme.IconStyle
import com.darkglass.tv777.ui.theme.LocalAppBackground
import com.darkglass.tv777.ui.theme.LocalGlassAccent
import com.darkglass.tv777.ui.theme.PageTransition
import com.darkglass.tv777.ui.theme.VisualStyle
import com.darkglass.tv777.ui.theme.StylePreviewCard
import com.darkglass.tv777.ui.theme.TextMuted
import com.darkglass.tv777.ui.theme.TextWhite
import com.darkglass.tv777.ui.theme.darkGlass
import com.darkglass.tv777.ui.theme.primaryColor

private enum class SettingsTab(val title: String, val icon: Ic) {
    COLORS("الألوان", Ic.GRID),
    STYLE("النمط", Ic.TV),
    BACKGROUND("الخلفية", Ic.HOME),
    MOTION("الحركة", Ic.BOLT),
    MEDIA("الوسائط", Ic.PLAYLIST)
}

@Composable
fun SettingsScreen(viewModel: SettingsViewModel, modifier: Modifier = Modifier) {
    val state by viewModel.uiState.collectAsState()
    val accent = LocalGlassAccent.current
    val appBg = LocalAppBackground.current
    var m3u by remember(state.customM3uUrl) { mutableStateOf(state.customM3uUrl) }
    var tab by remember { mutableStateOf(SettingsTab.COLORS) }
    var showAllStyles by remember { mutableStateOf(false) }
    Box(modifier.fillMaxSize().background(appBg)) {
        LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(bottom = 120.dp)) {
            item { GlassHeader("الإعدادات") }
            item { TabBar(tab) { tab = it } }
            when (tab) {
                SettingsTab.COLORS -> colorsTab(state, viewModel)
                SettingsTab.STYLE -> styleTab(state, viewModel, showAllStyles) { showAllStyles = it }
                SettingsTab.BACKGROUND -> backgroundTab(state, viewModel)
                SettingsTab.MOTION -> motionTab(state, viewModel)
                SettingsTab.MEDIA -> mediaTab(state, viewModel, m3u) { m3u = it }
            }
        }
    }
}

@Composable
private fun TabBar(current: SettingsTab, onSelect: (SettingsTab) -> Unit) {
    Row(Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 10.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        SettingsTab.entries.forEach { t ->
            val active = t == current
            Row(Modifier.weight(1f).clip(RoundedCornerShape(14.dp))
                .background(if (active) BackgroundSurface else Color.Transparent)
                .border(1.dp, if (active) GlassStrokeDefault else Color.Transparent, RoundedCornerShape(14.dp))
                .clickable { onSelect(t) }.padding(vertical = 10.dp), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                StyleIcon(t.icon, if (active) LocalGlassAccent.current.primaryColor else TextMuted, 18.dp)
                Spacer(Modifier.width(5.dp))
                Text(t.title, fontSize = 12.sp, fontWeight = if (active) FontWeight.Bold else FontWeight.Medium,
                    color = if (active) TextWhite else TextMuted, maxLines = 1)
            }
        }
    }
}

@Composable
private fun colorsTab(state: SettingsUiState, viewModel: SettingsViewModel) {
    item { SectionHeader("لون التمييز") }
    item {
        Row(Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 4.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            GlassAccent.entries.forEach { option ->
                val active = option == state.accent
                val primary = Color(option.primaryHex)
                Box(Modifier.weight(1f).aspectRatio(0.85f).clip(RoundedCornerShape(18.dp))
                    .background(if (active) BackgroundSurface else BackgroundElevated)
                    .border(if (active) 1.5.dp else 1.dp, if (active) primary else GlassStrokeDefault, RoundedCornerShape(18.dp))
                    .clickable { viewModel.setAccent(option) }.padding(12.dp), Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(Modifier.size(30.dp).clip(CircleShape).background(primary).border(1.dp, Color.White.copy(alpha = 0.25f), CircleShape))
                        Spacer(Modifier.height(8.dp))
                        Text(option.displayName, fontSize = 12.sp, fontWeight = if (active) FontWeight.Bold else FontWeight.Normal,
                            color = if (active) TextWhite else TextMuted)
                        if (active) {
                            Spacer(Modifier.height(4.dp))
                            AppIcon(Ic.CHECK, Modifier.size(16.dp), primary, 2.6.dp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun styleTab(state: SettingsUiState, viewModel: SettingsViewModel, showAll: Boolean, onToggleAll: (Boolean) -> Unit) {
    val accent = LocalGlassAccent.current
    item { SectionHeader("نمط الواجهة") }
    item {
        val styles = if (showAll) VisualStyle.entries else VisualStyle.entries.take(6)
        LazyVerticalGrid(columns = GridCells.Fixed(3),
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp).height(if (showAll) 560.dp else 380.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp), verticalArrangement = Arrangement.spacedBy(14.dp),
            userScrollEnabled = false) {
            items(styles) { style ->
                StylePreviewCard(style, style == state.visualStyle, accent.primaryColor) { viewModel.setVisualStyle(style) }
            }
        }
    }
    item {
        Box(Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 12.dp)
            .clip(RoundedCornerShape(14.dp)).background(BackgroundElevated)
            .border(1.dp, GlassStrokeDefault, RoundedCornerShape(14.dp))
            .clickable { onToggleAll(!showAll) }.padding(vertical = 12.dp), Alignment.Center) {
            Text(if (showAll) "عرض الأنماط الشائعة فقط" else "عرض كل الأنماط (${VisualStyle.entries.size})",
                fontSize = 13.sp, fontWeight = FontWeight.Bold, color = accent.primaryColor)
        }
    }
}

@Composable
private fun backgroundTab(state: SettingsUiState, viewModel: SettingsViewModel) {
    val accent = LocalGlassAccent.current
    item { SectionHeader("لون الخلفية والنقشة") }
    item {
        Row(Modifier.fillMaxWidth().padding(horizontal = 20.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            AppBackground.entries.forEach { bg ->
                val active = bg == state.background
                val color = Color(bg.hex)
                Box(Modifier.weight(1f).aspectRatio(1.1f).clip(RoundedCornerShape(18.dp))
                    .background(color)
                    .border(if (active) 1.5.dp else 1.dp, if (active) accent.primaryColor else GlassStrokeDefault, RoundedCornerShape(18.dp))
                    .clickable { viewModel.setAppBackground(bg) }) {
                    AmbientPattern(state.visualStyle.pattern, accent.primaryColor, Modifier.fillMaxSize())
                    if (active) Box(Modifier.align(Alignment.TopEnd).padding(8.dp).clip(CircleShape).background(accent.primaryColor).padding(4.dp)) {
                        AppIcon(Ic.CHECK, Modifier.size(12.dp), color, 2.4.dp)
                    }
                    Text(bg.displayName, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextWhite,
                        modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 10.dp))
                }
            }
        }
    }
    item {
        Box(Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 14.dp)
            .darkGlass(RoundedCornerShape(16.dp), BackgroundSurface).padding(14.dp)) {
            Text("النقشة الخلفية تتبع النمط المختار وتظهر خلف جميع الصفحات بلون التمييز شبه الشفاف.",
                fontSize = 12.sp, color = TextMuted, lineHeight = 18.sp)
        }
    }
}

@Composable
private fun motionTab(state: SettingsUiState, viewModel: SettingsViewModel) {
    item { SectionHeader("انتقال الصفحات") }
    item {
        Column(Modifier.fillMaxWidth().padding(horizontal = 20.dp).darkGlass(RoundedCornerShape(18.dp), BackgroundSurface).padding(10.dp)) {
            PageTransition.entries.forEach { t ->
                val active = t == state.pageTransition
                Row(Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp))
                    .background(if (active) LocalGlassAccent.current.primaryColor.copy(alpha = 0.18f) else Color.Transparent)
                    .clickable { viewModel.setPageTransition(t) }.padding(horizontal = 12.dp, vertical = 11.dp),
                    horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text(t.displayName, fontSize = 13.sp, fontWeight = if (active) FontWeight.Bold else FontWeight.Normal,
                        color = if (active) TextWhite else TextMuted)
                    if (active) AppIcon(Ic.CHECK, Modifier.size(16.dp), LocalGlassAccent.current.primaryColor, 2.4.dp)
                }
            }
        }
    }
    item { SectionHeader("نمط الأيقونات") }
    item {
        Row(Modifier.fillMaxWidth().padding(horizontal = 20.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            IconStyle.entries.forEach { s ->
                val active = s == state.iconStyle
                Box(Modifier.weight(1f).clip(RoundedCornerShape(16.dp))
                    .background(if (active) BackgroundSurface else BackgroundElevated)
                    .border(if (active) 1.5.dp else 1.dp, if (active) LocalGlassAccent.current.primaryColor else GlassStrokeDefault, RoundedCornerShape(16.dp))
                    .clickable { viewModel.setIconStyle(s) }.padding(vertical = 14.dp), Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        IconPreview(s)
                        Spacer(Modifier.height(8.dp))
                        Text(s.displayName, fontSize = 11.sp, fontWeight = if (active) FontWeight.Bold else FontWeight.Normal,
                            color = if (active) TextWhite else TextMuted)
                    }
                }
            }
        }
    }
}

@Composable
private fun IconPreview(s: IconStyle) {
    val accent = LocalGlassAccent.current
    when (s) {
        IconStyle.OUTLINE -> AppIcon(Ic.HOME, Modifier.size(26.dp), accent.primaryColor, 2.dp)
        IconStyle.BOLD -> AppIcon(Ic.HOME, Modifier.size(26.dp), accent.primaryColor, 2.9.dp)
        IconStyle.DUO -> Box(Modifier.size(26.dp)) {
            AppIcon(Ic.HOME, Modifier.padding(top = 2.dp, start = 2.dp).size(26.dp), accent.primaryColor.copy(alpha = 0.35f), 2.dp)
            AppIcon(Ic.HOME, Modifier.size(26.dp), accent.primaryColor, 2.dp)
        }
    }
}

@Composable
private fun mediaTab(state: SettingsUiState, viewModel: SettingsViewModel, m3u: String, onM3uChange: (String) -> Unit) {
    val accent = LocalGlassAccent.current
    item { SectionHeader("المشغل (Media3)") }
    item {
        Column(Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 4.dp)
            .darkGlass(RoundedCornerShape(20.dp), BackgroundSurface).padding(16.dp)) {
            Text("وضع التخزين المؤقت", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextWhite)
            Text("التحكم بسرعة بدء البث واستقرار الإشارة المباشرة", fontSize = 11.sp, color = TextMuted, modifier = Modifier.padding(top = 2.dp, bottom = 10.dp))
            BufferStrategy.entries.forEach { strategy ->
                val active = strategy == state.playerConfig.bufferStrategy
                Row(Modifier.fillMaxWidth().padding(vertical = 3.dp).clip(RoundedCornerShape(12.dp))
                    .background(if (active) accent.primaryColor.copy(alpha = 0.2f) else Color.Transparent)
                    .clickable { viewModel.setBufferStrategy(strategy) }.padding(horizontal = 12.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text(strategy.title, fontSize = 13.sp, fontWeight = if (active) FontWeight.Bold else FontWeight.Normal, color = if (active) TextWhite else TextMuted)
                    if (active) AppIcon(Ic.CHECK, Modifier.size(16.dp), accent.primaryColor, 2.4.dp)
                }
            }
            Spacer(Modifier.height(12.dp))
            Text("ملاءمة الشاشة الافتراضية", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextWhite)
            Row(Modifier.fillMaxWidth().padding(top = 10.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                AspectRatioMode.entries.forEach { mode ->
                    val active = mode == state.playerConfig.aspectRatio
                    Box(Modifier.weight(1f).clip(RoundedCornerShape(12.dp))
                        .background(if (active) accent.primaryColor.copy(alpha = 0.25f) else BackgroundElevated)
                        .border(1.dp, if (active) accent.primaryColor else GlassStrokeDefault, RoundedCornerShape(12.dp))
                        .clickable { viewModel.setAspectRatio(mode) }.padding(vertical = 9.dp), Alignment.Center) {
                        Text(mode.name, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = if (active) TextWhite else TextMuted)
                    }
                }
            }
            Spacer(Modifier.height(12.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text("تسريع العتاد (Hardware Decoding)", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    Text("تقليل استهلاك البطارية والحرارة أثناء البث عالي الجودة", fontSize = 11.sp, color = TextMuted)
                }
                Switch(state.playerConfig.hardwareAcceleration, { viewModel.setHardwareAcceleration(it) }, colors = SwitchDefaults.colors(
                    checkedThumbColor = accent.primaryColor,
                    checkedTrackColor = accent.primaryColor.copy(alpha = 0.4f),
                    uncheckedThumbColor = TextMuted,
                    uncheckedTrackColor = BackgroundElevated))
            }
        }
    }
    item { SectionHeader("مصادر القنوات") }
    item {
        Column(Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 4.dp)
            .darkGlass(RoundedCornerShape(20.dp), BackgroundSurface).padding(16.dp)) {
            Text("رابط قائمة البث المخصص (M3U)", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextWhite)
            Text("أدخل رابط قائمة قنواتك أو اتركه فارغاً لاستخدام القنوات الافتراضية", fontSize = 11.sp, color = TextMuted, modifier = Modifier.padding(top = 2.dp, bottom = 12.dp))
            Box(Modifier.fillMaxWidth().height(46.dp).darkGlass(RoundedCornerShape(14.dp), BackgroundElevated)
                .padding(horizontal = 12.dp), Alignment.CenterStart) {
                if (m3u.isEmpty()) Text("https://example.com/playlist.m3u", fontSize = 12.sp, color = TextMuted)
                BasicTextField(m3u, onM3uChange, singleLine = true, cursorBrush = SolidColor(accent.primaryColor),
                    textStyle = TextStyle(fontSize = 13.sp, color = TextWhite), modifier = Modifier.fillMaxWidth())
            }
            Spacer(Modifier.height(12.dp))
            Box(Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp))
                .background(accent.primaryColor)
                .clickable { viewModel.reloadM3uSources(m3u) }.padding(vertical = 12.dp), Alignment.Center) {
                Text(if (state.isSavingSources) "جاري الفحص والتحديث…" else "حفظ وتحديث القنوات", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextWhite)
            }
            AnimatedVisibility(state.sourceStatusMessage != null, enter = fadeIn(), exit = fadeOut()) {
                state.sourceStatusMessage?.let {
                    Text(it, fontSize = 12.sp, color = accent.primaryColor, modifier = Modifier.padding(top = 8.dp))
                }
            }
        }
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextWhite, modifier = Modifier.padding(horizontal = 24.dp, vertical = 10.dp))
}
