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
    NEON_BLUE("أزرق نيون", 0xFF3D7EFE, 0xFF00E5FF),
    DEEP_PURPLE("بنفسجي فوتوغرافي", 0xFF8A3FFC, 0xFFFF007A),
    CYAN_INDIGO("إنديغو كريستال", 0xFF00E5FF, 0xFF7000FF);

    companion object {
        fun fromName(name: String) = entries.firstOrNull { it.name.equals(name, true) } ?: NEON_BLUE
    }
}

data class PlayerConfig(
    val aspectRatio: AspectRatioMode = AspectRatioMode.FIT,
    val bufferStrategy: BufferStrategy = BufferStrategy.BALANCED,
    val hardwareAcceleration: Boolean = true,
    val autoPlayNext: Boolean = true
)
