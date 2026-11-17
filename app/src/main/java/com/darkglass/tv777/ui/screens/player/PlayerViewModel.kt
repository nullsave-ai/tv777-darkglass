package com.darkglass.tv777.ui.screens.player

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.DefaultLoadControl
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.hls.HlsMediaSource
import com.darkglass.tv777.data.model.AspectRatioMode
import com.darkglass.tv777.data.model.BufferStrategy
import com.darkglass.tv777.data.model.Channel
import com.darkglass.tv777.data.preferences.SettingsManager
import com.darkglass.tv777.data.repository.ChannelRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class PlayerUiState(
    val currentChannel: Channel? = null,
    val isPlaying: Boolean = false,
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val volume: Float = 1f,
    val isMuted: Boolean = false,
    val aspectRatio: AspectRatioMode = AspectRatioMode.FIT,
    val areControlsVisible: Boolean = true,
    val isQuickChannelsOpen: Boolean = false,
    val allChannels: List<Channel> = emptyList()
)

class PlayerViewModel(private val context: Context, private val repo: ChannelRepository, private val settings: SettingsManager) : ViewModel() {
    private val _ui = MutableStateFlow(PlayerUiState())
    val uiState: StateFlow<PlayerUiState> = _ui.asStateFlow()
    private var player: ExoPlayer? = null
    private var hideJob: Job? = null

    init {
        viewModelScope.launch {
            val config = settings.playerConfigFlow.first()
            val control = DefaultLoadControl.Builder()
                .setBufferDurationsMs(config.bufferStrategy.minBufferMs, config.bufferStrategy.maxBufferMs,
                    config.bufferStrategy.minBufferMs / 2, config.bufferStrategy.minBufferMs)
                .build()
            player = ExoPlayer.Builder(context).setLoadControl(control).build().apply {
                repeatMode = Player.REPEAT_MODE_OFF
                playWhenReady = true
                addListener(object : Player.Listener {
                    override fun onPlaybackStateChanged(state: Int) {
                        when (state) {
                            Player.STATE_BUFFERING -> _ui.value = _ui.value.copy(isLoading = true, errorMessage = null)
                            Player.STATE_READY -> _ui.value = _ui.value.copy(isLoading = false, isPlaying = playWhenReady, errorMessage = null)
                            Player.STATE_ENDED -> _ui.value = _ui.value.copy(isLoading = false, isPlaying = false)
                            else -> Unit
                        }
                    }
                    override fun onIsPlayingChanged(playing: Boolean) {
                        _ui.value = _ui.value.copy(isPlaying = playing)
                    }
                    override fun onPlayerError(error: PlaybackException) {
                        _ui.value = _ui.value.copy(isLoading = false, isPlaying = false, errorMessage = "فشل تشغيل إشارة البث المباشر. قد يكون السيرفر مشغولاً.")
                    }
                })
            }
            _ui.value = _ui.value.copy(aspectRatio = config.aspectRatio)
        }
        viewModelScope.launch { repo.allChannelsFlow.collect { _ui.value = _ui.value.copy(allChannels = it) } }
    }

    fun getPlayer() = player

    fun playChannel(channel: Channel) {
        _ui.value = _ui.value.copy(currentChannel = channel, isLoading = true, errorMessage = null)
        viewModelScope.launch {
            repo.markWatched(channel.id)
            val factory = DefaultHttpDataSource.Factory().setUserAgent("777-DarkGlass-ExoPlayer/1.0")
                .setConnectTimeoutMs(8000).setReadTimeoutMs(8000).setAllowCrossProtocolRedirects(true)
            val source = HlsMediaSource.Factory(factory).createMediaSource(MediaItem.fromUri(channel.streamUrl))
            player?.apply {
                setMediaSource(source)
                prepare()
                playWhenReady = true
            }
            scheduleHideControls()
        }
    }

    fun togglePlayPause() {
        player?.let { p ->
            if (p.isPlaying) p.pause() else p.play()
            _ui.value = _ui.value.copy(isPlaying = p.isPlaying)
            scheduleHideControls()
        }
    }

    fun retryPlayback() { _ui.value.currentChannel?.let { playChannel(it) } }

    fun toggleControls() {
        val next = !_ui.value.areControlsVisible
        _ui.value = _ui.value.copy(areControlsVisible = next)
        if (next) scheduleHideControls() else hideJob?.cancel()
    }

    fun toggleQuickChannels() {
        _ui.value = _ui.value.copy(isQuickChannelsOpen = !_ui.value.isQuickChannelsOpen)
        scheduleHideControls()
    }

    fun cycleAspectRatio() {
        val values = AspectRatioMode.entries
        _ui.value = _ui.value.copy(aspectRatio = values[(_ui.value.aspectRatio.ordinal + 1) % values.size])
        scheduleHideControls()
    }

    fun setVolume(v: Float) {
        val clamped = v.coerceIn(0f, 1f)
        player?.volume = clamped
        _ui.value = _ui.value.copy(volume = clamped, isMuted = clamped == 0f)
        scheduleHideControls()
    }

    fun toggleMute() {
        if (_ui.value.isMuted) {
            val restore = if (_ui.value.volume <= 0.05f) 0.8f else _ui.value.volume
            player?.volume = restore
            _ui.value = _ui.value.copy(isMuted = false, volume = restore)
        } else {
            player?.volume = 0f
            _ui.value = _ui.value.copy(isMuted = true)
        }
        scheduleHideControls()
    }

    fun playNextChannel() = switchChannel(1)
    fun playPreviousChannel() = switchChannel(-1)

    private fun switchChannel(step: Int) {
        val list = _ui.value.allChannels
        val current = _ui.value.currentChannel ?: return
        val index = list.indexOfFirst { it.id == current.id }
        if (index >= 0 && list.isNotEmpty()) {
            val next = ((index + step) % list.size + list.size) % list.size
            playChannel(list[next])
        }
    }

    private fun scheduleHideControls() {
        hideJob?.cancel()
        hideJob = viewModelScope.launch {
            delay(5000)
            if (_ui.value.isPlaying && !_ui.value.isQuickChannelsOpen) _ui.value = _ui.value.copy(areControlsVisible = false)
        }
    }

    override fun onCleared() {
        super.onCleared()
        player?.release()
        player = null
    }
}
