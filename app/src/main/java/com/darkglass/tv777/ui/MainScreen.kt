package com.darkglass.tv777.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.togetherWith
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
import androidx.compose.ui.graphics.Color
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
import com.darkglass.tv777.ui.theme.AmbientPattern
import com.darkglass.tv777.ui.theme.AppBackground
import com.darkglass.tv777.ui.theme.IconStyle
import com.darkglass.tv777.ui.theme.PageTransition
import com.darkglass.tv777.ui.theme.Tv777Theme
import com.darkglass.tv777.ui.theme.VisualStyle
import com.darkglass.tv777.ui.theme.primaryColor

@Composable
fun MainScreen(settings: SettingsManager, repo: ChannelRepository) {
    val accent by settings.accentFlow.collectAsState(GlassAccent.SLATE)
    val visualStyle by settings.visualStyleFlow.collectAsState(VisualStyle.CURVED)
    val background by settings.appBackgroundFlow.collectAsState(AppBackground.CHARCOAL)
    val iconStyle by settings.iconStyleFlow.collectAsState(IconStyle.OUTLINE)
    val transition by settings.pageTransitionFlow.collectAsState(PageTransition.FADE)
    val context = LocalContext.current
    val home = remember { HomeViewModel(repo) }
    val channels = remember { ChannelsViewModel(repo) }
    val prefs = remember { SettingsViewModel(settings, repo) }
    val player = remember { PlayerViewModel(context, repo, settings) }
    var tab by remember { mutableStateOf(HOME) }
    var active by remember { mutableStateOf<Channel?>(null) }
    val bgColor = Color(background.hex)
    Tv777Theme(accent, bgColor, iconStyle) {
        Box(Modifier.fillMaxSize().background(bgColor)) {
            AmbientPattern(visualStyle.pattern, accent.primaryColor, Modifier.fillMaxSize())
            if (active != null) {
                PlayerScreen(active!!, player, { active = null })
            } else {
                AnimatedContent(tab, transitionSpec = {
                    when (transition) {
                        PageTransition.FADE -> fadeIn(tween(280)) togetherWith fadeOut(tween(280))
                        PageTransition.SLIDE -> (slideInHorizontally(tween(320)) { if (targetState.ordinal >= initialState.ordinal) it / 3 else -it / 3 } + fadeIn(tween(320))) togetherWith fadeOut(tween(200))
                        PageTransition.ZOOM -> (scaleIn(tween(300), initialScale = 0.9f) + fadeIn(tween(300))) togetherWith fadeOut(tween(200))
                        PageTransition.INSTANT -> fadeIn(tween(0)) togetherWith fadeOut(tween(0))
                    }
                }, label = "tabs") { dest ->
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
