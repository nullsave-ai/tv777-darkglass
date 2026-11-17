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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.graphics.Brush
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
import com.darkglass.tv777.ui.theme.BackgroundSurface
import com.darkglass.tv777.ui.theme.BackgroundElevated
import com.darkglass.tv777.ui.theme.GlassStrokeDefault
import com.darkglass.tv777.ui.theme.BackgroundDeep
import com.darkglass.tv777.ui.theme.LocalGlassAccent
import com.darkglass.tv777.ui.theme.TextMuted
import com.darkglass.tv777.ui.theme.TextWhite
import com.darkglass.tv777.ui.theme.darkGlass
import com.darkglass.tv777.ui.theme.primaryColor
import com.darkglass.tv777.ui.theme.secondaryColor

@Composable
fun SettingsScreen(viewModel: SettingsViewModel, modifier: Modifier = Modifier) {
    val state by viewModel.uiState.collectAsState()
    val accent = LocalGlassAccent.current
    var m3u by remember(state.customM3uUrl) { mutableStateOf(state.customM3uUrl) }
    Box(modifier.fillMaxSize().background(BackgroundDeep)) {
        LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(bottom = 110.dp)) {
            item { GlassHeader("إعدادات النظام") }

            item { SectionHeader("الهوية البصرية") }
            item {
                Box(Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 6.dp)
                    .darkGlass(RoundedCornerShape(22.dp), BackgroundSurface, accent.primaryColor).padding(18.dp)) {
                    Column {
                        Text("لون التمييز", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                        Text("اختر لوناً هادئاً لعناصر التمييز في الواجهة", fontSize = 11.sp, color = TextMuted, modifier = Modifier.padding(top = 2.dp, bottom = 14.dp))
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            GlassAccent.entries.forEach { option ->
                                val active = option == state.accent
                                val primary = Color(option.primaryHex)
                                val secondary = Color(option.secondaryHex)
                                Box(Modifier.weight(1f).clip(RoundedCornerShape(16.dp))
                                    .background(if (active) primary.copy(alpha = 0.25f) else BackgroundElevated)
                                    .border(if (active) 1.5.dp else 0.8.dp, if (active) primary else Color.White.copy(alpha = 0.15f), RoundedCornerShape(16.dp))
                                    .clickable { viewModel.setAccent(option) }.padding(vertical = 12.dp, horizontal = 8.dp), Alignment.Center) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Box(Modifier.size(24.dp).clip(CircleShape)
                                            .background(primary)
                                            .border(1.dp, Color.White.copy(alpha = 0.4f), CircleShape))
                                        Spacer(Modifier.height(6.dp))
                                        Text(option.displayName, fontSize = 11.sp, fontWeight = if (active) FontWeight.Bold else FontWeight.Normal, color = if (active) TextWhite else TextMuted)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            item { SectionHeader("إعدادات مشغل البث (Media3)") }
            item {
                Box(Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 6.dp)
                    .darkGlass(RoundedCornerShape(22.dp), BackgroundSurface).padding(18.dp)) {
                    Column {
                        Text("وضع التخزين المؤقت (Buffer Mode)", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                        Text("التحكم بسرعة بدء البث واستقرار الإشارة المباشرة", fontSize = 11.sp, color = TextMuted, modifier = Modifier.padding(top = 2.dp, bottom = 12.dp))
                        BufferStrategy.entries.forEach { strategy ->
                            val active = strategy == state.playerConfig.bufferStrategy
                            Row(Modifier.fillMaxWidth().padding(vertical = 4.dp).clip(RoundedCornerShape(12.dp))
                                .background(if (active) accent.primaryColor.copy(alpha = 0.2f) else Color.Transparent)
                                .clickable { viewModel.setBufferStrategy(strategy) }.padding(horizontal = 12.dp, vertical = 10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                Text(strategy.title, fontSize = 13.sp, fontWeight = if (active) FontWeight.Bold else FontWeight.Normal, color = if (active) TextWhite else TextMuted)
                                if (active) AppIcon(Ic.CHECK, Modifier.size(16.dp), accent.primaryColor, 2.4.dp)
                            }
                        }
                        Spacer(Modifier.height(14.dp))
                        Text("تناسب الشاشة الافتراضي", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                        Row(Modifier.fillMaxWidth().padding(top = 10.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            AspectRatioMode.entries.forEach { mode ->
                                val active = mode == state.playerConfig.aspectRatio
                                Box(Modifier.weight(1f).clip(RoundedCornerShape(12.dp))
                                    .background(if (active) accent.primaryColor.copy(alpha = 0.25f) else BackgroundElevated)
                                    .border(0.8.dp, if (active) accent.primaryColor else Color.White.copy(alpha = 0.1f), RoundedCornerShape(12.dp))
                                    .clickable { viewModel.setAspectRatio(mode) }.padding(vertical = 8.dp), Alignment.Center) {
                                    Text(mode.name, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = if (active) TextWhite else TextMuted)
                                }
                            }
                        }
                        Spacer(Modifier.height(14.dp))
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Column(Modifier.weight(1f)) {
                                Text("تسريع العتاد المادي (Hardware Decoding)", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                                Text("تقليل استهلاك البطارية والحرارة أثناء تشغيل البث 4K/FHD", fontSize = 11.sp, color = TextMuted)
                            }
                            Switch(state.playerConfig.hardwareAcceleration, { viewModel.setHardwareAcceleration(it) }, colors = SwitchDefaults.colors(
                                checkedThumbColor = accent.primaryColor,
                                checkedTrackColor = accent.primaryColor.copy(alpha = 0.4f),
                                uncheckedThumbColor = TextMuted,
                                uncheckedTrackColor = GlassStrokeDefault))
                        }
                    }
                }
            }

            item { SectionHeader("إدارة مصادر القنوات (M3U / IPTV)") }
            item {
                Box(Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 6.dp)
                    .darkGlass(RoundedCornerShape(22.dp), BackgroundSurface).padding(18.dp)) {
                    Column {
                        Text("رابط قائمة البث المخصص (M3U Playlist)", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                        Text("أدخل رابط قائمة قنواتك أو اتركه فارغاً لاستخدام قنوات 777 الرسمية", fontSize = 11.sp, color = TextMuted, modifier = Modifier.padding(top = 2.dp, bottom = 12.dp))
                        Box(Modifier.fillMaxWidth().height(46.dp).darkGlass(RoundedCornerShape(14.dp), BackgroundElevated)
                            .padding(horizontal = 12.dp), Alignment.CenterStart) {
                            if (m3u.isEmpty()) Text("https://example.com/playlist.m3u", fontSize = 12.sp, color = TextMuted)
                            BasicTextField(m3u, { m3u = it }, singleLine = true, cursorBrush = SolidColor(accent.primaryColor),
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

            item { SectionHeader("معلومات التطبيق") }
            item {
                Box(Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 6.dp)
                    .darkGlass(RoundedCornerShape(22.dp), BackgroundSurface).padding(18.dp)) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("777 — STREAMING", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                            Spacer(Modifier.width(8.dp))
                            Text("v1.0.0", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = accent.primaryColor)
                        }
                        Spacer(Modifier.height(6.dp))
                        Text("تطبيق تلفزيوني مباشر خفيف وسريع بواجهة هادئة ونظيفة مبنية بـ Jetpack Compose و Media3.", fontSize = 12.sp, color = TextMuted, lineHeight = 18.sp)
                        Spacer(Modifier.height(10.dp))
                        Text("واجهة بلا إعلانات • لا مكتبات زائدة", fontSize = 11.sp, fontWeight = FontWeight.Medium, color = accent.primaryColor)
                    }
                }
            }
        }
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextWhite, modifier = Modifier.padding(horizontal = 24.dp, vertical = 10.dp))
}
