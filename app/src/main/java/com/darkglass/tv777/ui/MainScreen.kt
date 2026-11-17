package com.darkglass.tv777.ui

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.darkglass.tv777.data.model.Channel
import com.darkglass.tv777.data.model.GlassAccent
import com.darkglass.tv777.data.preferences.SettingsManager
import com.darkglass.tv777.data.repository.ChannelRepository
import com.darkglass.tv777.ui.components.FloatingGlassDock
import com.darkglass.tv777.ui.navigation.AppDestination
import com.darkglass.tv777.ui.navigation.AppDestination.CHANNELS
import com.darkglass.tv777.ui.navigation.AppDestination.HOME
import com.darkglass.tv777.ui.navigation.AppDestination.SETTINGS
import com.darkglass.tv777.ui.screens.channels.ChannelsScreen
import com.darkglass.tv777.ui.screens.channels.ChannelsViewModel
import com.darkglass.tv777.ui.screens.home.HomeScreen
import com.darkglass.tv777.ui.screens.home.HomeViewModel
import com.darkglass.tv777.ui.screens.player.PlayerScreen
import com.darkglass.tv777.ui.screens.player.PlayerViewModel
import com.darkglass.tv777.ui.screens.settings.SettingsScreen
import com.darkglass.tv777.ui.screens.settings.SettingsViewModel
import com.darkglass.tv777.ui.theme.BackgroundVoid
import com.darkglass.tv777.ui.theme.Tv777Theme

@Composable
fun MainScreen(settings: SettingsManager, repo: ChannelRepository) {
    val accent by settings.accentFlow.collectAsState(GlassAccent.NEON_BLUE)
    val context = LocalContext.current
    val home = remember { HomeViewModel(repo) }
    val channels = remember { ChannelsViewModel(repo) }
    val prefs = remember { SettingsViewModel(settings, repo) }
    val player = remember { PlayerViewModel(context, repo, settings) }
    var tab by remember { mutableStateOf(HOME) }
    var active by remember { mutableStateOf<Channel?>(null) }
    Tv777Theme(accent) {
        Box(Modifier.fillMaxSize().background(BackgroundVoid)) {
            if (active != null) {
                PlayerScreen(active!!, player, { active = null })
            } else {
                Crossfade(tab, animationSpec = tween(300), label = "tab") { dest ->
                    when (dest) {
                        HOME -> HomeScreen(home, { active = it })
                        CHANNELS -> ChannelsScreen(channels, { active = it })
                        SETTINGS -> SettingsScreen(prefs)
                    }
                }
                FloatingGlassDock(tab, { tab = it }, Modifier.align(Alignment.BottomCenter))
            }
        }
    }
}
