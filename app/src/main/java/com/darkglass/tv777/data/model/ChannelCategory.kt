package com.darkglass.tv777.data.model

enum class ChannelCategory(val id: String, val arabicName: String, val iconName: String) {
    ALL("all", "الكل", "grid"),
    NEWS("news", "أخبار", "newspaper"),
    SPORTS("sports", "رياضة", "trophy"),
    DOCUMENTARY("doc", "وثائقيات", "film"),
    ISLAMIC("islamic", "إسلامية", "moon"),
    KIDS("kids", "أطفال", "smile"),
    ENTERTAINMENT("general", "ترفيه عام", "tv");

    companion object {
        fun fromId(id: String) = entries.firstOrNull { it.id.equals(id, true) } ?: ALL
    }
}
