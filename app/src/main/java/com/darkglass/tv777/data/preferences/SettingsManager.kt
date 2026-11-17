package com.darkglass.tv777.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.darkglass.tv777.data.model.AspectRatioMode
import com.darkglass.tv777.data.model.BufferStrategy
import com.darkglass.tv777.data.model.GlassAccent
import com.darkglass.tv777.data.model.PlayerConfig
import com.darkglass.tv777.ui.theme.AppBackground
import com.darkglass.tv777.ui.theme.IconStyle
import com.darkglass.tv777.ui.theme.PageTransition
import com.darkglass.tv777.ui.theme.VisualStyle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore("tv777_settings")

class SettingsManager(private val context: Context) {
    private val accentKey = stringPreferencesKey("glass_accent")
    private val aspectKey = stringPreferencesKey("aspect_ratio")
    private val bufferKey = stringPreferencesKey("buffer_strategy")
    private val hwKey = booleanPreferencesKey("hw_accel")
    private val favoritesKey = stringSetPreferencesKey("favorite_channels")
    private val recentKey = stringPreferencesKey("recent_channels_csv")
    private val m3uKey = stringPreferencesKey("custom_m3u_url")
    private val styleKey = stringPreferencesKey("visual_style")
    private val backgroundKey = stringPreferencesKey("app_background")
    private val iconStyleKey = stringPreferencesKey("icon_style")
    private val transitionKey = stringPreferencesKey("page_transition")

    val accentFlow: Flow<GlassAccent> = context.dataStore.data.map { p ->
        GlassAccent.fromName(p[accentKey] ?: GlassAccent.SLATE.name)
    }

    val playerConfigFlow: Flow<PlayerConfig> = context.dataStore.data.map { p ->
        PlayerConfig(
            AspectRatioMode.valueOf(p[aspectKey] ?: AspectRatioMode.FIT.name),
            BufferStrategy.valueOf(p[bufferKey] ?: BufferStrategy.BALANCED.name),
            p[hwKey] ?: true
        )
    }

    val favoritesFlow: Flow<Set<String>> = context.dataStore.data.map { it[favoritesKey] ?: emptySet() }

    val recentIdsFlow: Flow<List<String>> = context.dataStore.data.map { p ->
        (p[recentKey] ?: "").split(",").filter { it.isNotBlank() }
    }

    val customM3uFlow: Flow<String> = context.dataStore.data.map { p -> p[m3uKey] ?: "" }

    val visualStyleFlow: Flow<VisualStyle> = context.dataStore.data.map { p ->
        VisualStyle.fromName(p[styleKey] ?: VisualStyle.CURVED.name)
    }

    val appBackgroundFlow: Flow<AppBackground> = context.dataStore.data.map { p ->
        AppBackground.fromName(p[backgroundKey] ?: AppBackground.CHARCOAL.name)
    }

    val iconStyleFlow: Flow<IconStyle> = context.dataStore.data.map { p ->
        IconStyle.fromName(p[iconStyleKey] ?: IconStyle.OUTLINE.name)
    }

    val pageTransitionFlow: Flow<PageTransition> = context.dataStore.data.map { p ->
        PageTransition.fromName(p[transitionKey] ?: PageTransition.FADE.name)
    }

    suspend fun setAccent(v: GlassAccent) = context.dataStore.edit { it[accentKey] = v.name }
    suspend fun setAspectRatio(v: AspectRatioMode) = context.dataStore.edit { it[aspectKey] = v.name }
    suspend fun setBufferStrategy(v: BufferStrategy) = context.dataStore.edit { it[bufferKey] = v.name }
    suspend fun setHardwareAcceleration(v: Boolean) = context.dataStore.edit { it[hwKey] = v }
    suspend fun setCustomM3u(v: String) = context.dataStore.edit { it[m3uKey] = v }
    suspend fun setVisualStyle(v: VisualStyle) = context.dataStore.edit { it[styleKey] = v.name }
    suspend fun setAppBackground(v: AppBackground) = context.dataStore.edit { it[backgroundKey] = v.name }
    suspend fun setIconStyle(v: IconStyle) = context.dataStore.edit { it[iconStyleKey] = v.name }
    suspend fun setPageTransition(v: PageTransition) = context.dataStore.edit { it[transitionKey] = v.name }

    suspend fun toggleFavorite(id: String) = context.dataStore.edit { p ->
        val set = p[favoritesKey]?.toMutableSet() ?: mutableSetOf()
        if (!set.add(id)) set.remove(id)
        p[favoritesKey] = set
    }

    suspend fun addRecentChannel(id: String) = context.dataStore.edit { p ->
        val list = (p[recentKey] ?: "").split(",").filter { it.isNotBlank() && it != id }.toMutableList()
        list.add(0, id)
        p[recentKey] = list.take(15).joinToString(",")
    }
}
