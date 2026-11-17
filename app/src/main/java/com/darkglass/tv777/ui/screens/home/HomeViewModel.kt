package com.darkglass.tv777.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.darkglass.tv777.data.model.Channel
import com.darkglass.tv777.data.model.ChannelCategory
import com.darkglass.tv777.data.repository.ChannelRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class HomeUiState(
    val featuredChannels: List<Channel> = emptyList(),
    val favoriteChannels: List<Channel> = emptyList(),
    val recentChannels: List<Channel> = emptyList(),
    val selectedCategory: ChannelCategory = ChannelCategory.ALL,
    val totalLiveCount: Int = 0,
    val isLoading: Boolean = false
)

class HomeViewModel(private val repo: ChannelRepository) : ViewModel() {
    private val category = MutableStateFlow(ChannelCategory.ALL)

    val uiState: StateFlow<HomeUiState> = combine(repo.allChannelsFlow, category) { channels, cat ->
        HomeUiState(
            channels.filter { it.isFeatured },
            channels.filter { it.isFavorite },
            channels.filter { it.lastWatchedTimestamp > 0 }.sortedByDescending { it.lastWatchedTimestamp },
            cat,
            channels.size
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), HomeUiState(isLoading = true))

    fun selectCategory(cat: ChannelCategory) { category.value = cat }
    fun toggleFavorite(id: String) { viewModelScope.launch { repo.toggleFavorite(id) } }
}
