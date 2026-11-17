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

    suspend fun setAccent(v: GlassAccent) = context.dataStore.edit { it[accentKey] = v.name }
    suspend fun setAspectRatio(v: AspectRatioMode) = context.dataStore.edit { it[aspectKey] = v.name }
    suspend fun setBufferStrategy(v: BufferStrategy) = context.dataStore.edit { it[bufferKey] = v.name }
    suspend fun setHardwareAcceleration(v: Boolean) = context.dataStore.edit { it[hwKey] = v }
    suspend fun setCustomM3u(v: String) = context.dataStore.edit { it[m3uKey] = v }

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
