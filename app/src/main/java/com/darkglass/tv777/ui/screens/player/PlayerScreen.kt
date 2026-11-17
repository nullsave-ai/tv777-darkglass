package com.darkglass.tv777.ui.screens.player

import android.app.Activity
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import com.darkglass.tv777.data.model.AspectRatioMode
import com.darkglass.tv777.data.model.Channel

@Composable
fun PlayerScreen(channel: Channel, viewModel: PlayerViewModel, onBackClick: () -> Unit, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val state by viewModel.uiState.collectAsState()
    val interaction = remember { MutableInteractionSource() }

    LaunchedEffect(channel.id) { viewModel.playChannel(channel) }

    DisposableEffect(Unit) {
        val activity = context as? Activity
        activity?.window?.addFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        onDispose { activity?.window?.clearFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON) }
    }
    BackHandler { onBackClick() }

    val resize = when (state.aspectRatio) {
        AspectRatioMode.FIT -> AspectRatioFrameLayout.RESIZE_MODE_FIT
        AspectRatioMode.FILL -> AspectRatioFrameLayout.RESIZE_MODE_FILL
        AspectRatioMode.ZOOM -> AspectRatioFrameLayout.RESIZE_MODE_ZOOM
    }

    Box(modifier.fillMaxSize().background(Color.Black).clickable(interactionSource = interaction, indication = null) { viewModel.toggleControls() }) {
        AndroidView(Modifier.fillMaxSize(), factory = { ctx ->
            PlayerView(ctx).apply {
                player = viewModel.getPlayer()
                useController = false
                layoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                resizeMode = resize
            }
        }, update = { view ->
            view.player = viewModel.getPlayer()
            view.resizeMode = resize
        })
        PlayerControlsOverlay(state, onBackClick, { viewModel.togglePlayPause() },
            { viewModel.playNextChannel() }, { viewModel.playPreviousChannel() },
            { viewModel.playChannel(it) }, { viewModel.cycleAspectRatio() },
            { viewModel.toggleQuickChannels() }, { viewModel.setVolume(it) },
            { viewModel.toggleMute() }, { viewModel.retryPlayback() })
    }
}
