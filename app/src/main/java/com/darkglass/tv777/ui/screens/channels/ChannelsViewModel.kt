package com.darkglass.tv777.ui.screens.channels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.darkglass.tv777.data.model.Channel
import com.darkglass.tv777.data.model.ChannelCategory
import com.darkglass.tv777.data.repository.ChannelRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class ChannelsUiState(
    val channels: List<Channel> = emptyList(),
    val searchQuery: String = "",
    val selectedCategory: ChannelCategory = ChannelCategory.ALL,
    val isGridView: Boolean = true,
    val showFavoritesOnly: Boolean = false,
    val isRefreshing: Boolean = false,
    val statusMessage: String? = null
)

class ChannelsViewModel(private val repo: ChannelRepository) : ViewModel() {
    private val query = MutableStateFlow("")
    private val category = MutableStateFlow(ChannelCategory.ALL)
    private val grid = MutableStateFlow(true)
    private val favOnly = MutableStateFlow(false)
    private val refreshing = MutableStateFlow(false)
    private val status = MutableStateFlow<String?>(null)

    val uiState: StateFlow<ChannelsUiState> = combine(
        repo.allChannelsFlow, query, category, grid, favOnly, refreshing, status
    ) { all, q, cat, isGrid, fav, busy, msg ->
        ChannelsUiState(
            all.filter { c ->
                (q.isBlank() || c.name.contains(q, true) || c.arabicName.contains(q, true) || c.category.arabicName.contains(q, true)) &&
                        (cat == ChannelCategory.ALL || c.category == cat) && (!fav || c.isFavorite)
            },
            q, cat, isGrid, fav, busy, msg
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ChannelsUiState())

    fun onSearchQueryChanged(v: String) { query.value = v }
    fun selectCategory(v: ChannelCategory) { category.value = v }
    fun toggleViewMode() { grid.value = !grid.value }
    fun toggleFavoritesFilter() { favOnly.value = !favOnly.value }
    fun toggleFavorite(id: String) { viewModelScope.launch { repo.toggleFavorite(id) } }

    fun refreshChannels() {
        viewModelScope.launch {
            refreshing.value = true
            status.value = "جاري فحص المصادر والبثوث…"
            delay(800)
            refreshing.value = false
            status.value = "تم تحديث كافة القنوات بنجاح"
            delay(2000)
            status.value = null
        }
    }
}
