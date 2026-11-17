package com.darkglass.tv777.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.darkglass.tv777.data.model.AspectRatioMode
import com.darkglass.tv777.data.model.BufferStrategy
import com.darkglass.tv777.data.model.GlassAccent
import com.darkglass.tv777.data.model.PlayerConfig
import com.darkglass.tv777.data.preferences.SettingsManager
import com.darkglass.tv777.data.repository.ChannelRepository
import com.darkglass.tv777.ui.theme.AppBackground
import com.darkglass.tv777.ui.theme.IconStyle
import com.darkglass.tv777.ui.theme.PageTransition
import com.darkglass.tv777.ui.theme.VisualStyle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class SettingsUiState(
    val accent: GlassAccent = GlassAccent.SLATE,
    val playerConfig: PlayerConfig = PlayerConfig(),
    val customM3uUrl: String = "",
    val visualStyle: VisualStyle = VisualStyle.CURVED,
    val background: AppBackground = AppBackground.CHARCOAL,
    val iconStyle: IconStyle = IconStyle.OUTLINE,
    val pageTransition: PageTransition = PageTransition.FADE,
    val isSavingSources: Boolean = false,
    val sourceStatusMessage: String? = null
)

class SettingsViewModel(private val settings: SettingsManager, private val repo: ChannelRepository) : ViewModel() {
    private val saving = MutableStateFlow(false)
    private val status = MutableStateFlow<String?>(null)

    val uiState: StateFlow<SettingsUiState> = combine<Any?, SettingsUiState>(
        settings.accentFlow, settings.playerConfigFlow, settings.customM3uFlow,
        settings.visualStyleFlow, settings.appBackgroundFlow, settings.iconStyleFlow,
        settings.pageTransitionFlow, saving, status
    ) { args ->
        @Suppress("UNCHECKED_CAST")
        SettingsUiState(
            args[0] as GlassAccent,
            args[1] as PlayerConfig,
            args[2] as String,
            args[3] as VisualStyle,
            args[4] as AppBackground,
            args[5] as IconStyle,
            args[6] as PageTransition,
            args[7] as Boolean,
            args[8] as String?
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SettingsUiState())

    fun setAccent(v: GlassAccent) { viewModelScope.launch { settings.setAccent(v) } }
    fun setVisualStyle(v: VisualStyle) { viewModelScope.launch { settings.setVisualStyle(v) } }
    fun setAppBackground(v: AppBackground) { viewModelScope.launch { settings.setAppBackground(v) } }
    fun setIconStyle(v: IconStyle) { viewModelScope.launch { settings.setIconStyle(v) } }
    fun setPageTransition(v: PageTransition) { viewModelScope.launch { settings.setPageTransition(v) } }
    fun setAspectRatio(v: AspectRatioMode) { viewModelScope.launch { settings.setAspectRatio(v) } }
    fun setBufferStrategy(v: BufferStrategy) { viewModelScope.launch { settings.setBufferStrategy(v) } }
    fun setHardwareAcceleration(v: Boolean) { viewModelScope.launch { settings.setHardwareAcceleration(v) } }
    fun updateM3uUrl(v: String) { viewModelScope.launch { settings.setCustomM3u(v) } }

    fun reloadM3uSources(url: String) {
        viewModelScope.launch {
            saving.value = true
            status.value = "جاري تحميل وفحص قائمة البث…"
            settings.setCustomM3u(url)
            val result = repo.loadM3uPlaylist(url)
            saving.value = false
            status.value = if (result.isSuccess) "تم بنجاح تحميل ${result.getOrNull()} قناة"
            else "خطأ في تحميل الرابط: ${result.exceptionOrNull()?.message}"
        }
    }
}
