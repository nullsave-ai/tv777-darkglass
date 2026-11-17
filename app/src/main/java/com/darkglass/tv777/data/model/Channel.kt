package com.darkglass.tv777.data.model

data class Channel(
    val id: String,
    val name: String,
    val arabicName: String = name,
    val streamUrl: String,
    val category: ChannelCategory,
    val logoText: String = "",
    val resolution: String = "FHD",
    val bitrate: String = "1080p 60fps",
    val isFeatured: Boolean = false,
    val isFavorite: Boolean = false,
    val lastWatchedTimestamp: Long = 0L,
    val accentHue: Long = 0xFF3D7EFE
)
