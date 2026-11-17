package com.darkglass.tv777.data.model

enum class AspectRatioMode(val title: String) {
    FIT("تناسب (Fit)"),
    FILL("ملء (Fill)"),
    ZOOM("تكبير 16:9 (Zoom)")
}

enum class BufferStrategy(val title: String, val minBufferMs: Int, val maxBufferMs: Int) {
    LOW_LATENCY("فائق السرعة (Low Latency)", 1000, 3000),
    BALANCED("متوازن (Balanced)", 2500, 6000),
    STABLE("أقصى استقرار (Stable)", 5000, 15000)
}

enum class GlassAccent(val displayName: String, val primaryHex: Long, val secondaryHex: Long) {
    GRAPHITE("جرافيت", 0xFF8B94A5, 0xFF5E6673),
    SLATE("أزرق هادئ", 0xFF7C93C6, 0xFF9AAAC7),
    SAND("رملي", 0xFFC0A277, 0xFF9C8767);

    companion object {
        fun fromName(name: String) = entries.firstOrNull { it.name.equals(name, true) } ?: SLATE
    }
}

data class PlayerConfig(
    val aspectRatio: AspectRatioMode = AspectRatioMode.FIT,
    val bufferStrategy: BufferStrategy = BufferStrategy.BALANCED,
    val hardwareAcceleration: Boolean = true,
    val autoPlayNext: Boolean = true
)
