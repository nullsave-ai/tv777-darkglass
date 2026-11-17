package com.darkglass.tv777.data.repository

import com.darkglass.tv777.data.model.Channel
import com.darkglass.tv777.data.model.ChannelCategory
import com.darkglass.tv777.data.preferences.SettingsManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class ChannelRepository(private val settings: SettingsManager) {
    private fun ch(
        id: String, name: String, ar: String, url: String, cat: ChannelCategory, logo: String,
        res: String = "FHD", bit: String = "1080p 60fps", feat: Boolean = false, hue: Long = 0xFF3D7EFE
    ) = Channel(id, name, ar, url, cat, logo, res, bit, feat, false, 0L, hue)

    private val defaults = listOf(
        ch("ch_aljazeera", "Al Jazeera Arabic", "الجزيرة الإخبارية", "https://live-hls-web-aje.getaj.net/AJE/03.m3u8", ChannelCategory.NEWS, "AJA", feat = true),
        ch("ch_alarabiya", "Al Arabiya", "العربية مباشر", "https://live.alarabiya.net/alarabiapublish/alarabiya.smil/playlist.m3u8", ChannelCategory.NEWS, "ARA", bit = "1080p 50fps", feat = true, hue = 0xFF8A3FFC),
        ch("ch_skynews", "Sky News Arabia", "سكاي نيوز عربية", "https://stream.skynewsarabia.com/hls/sna.m3u8", ChannelCategory.NEWS, "SNA", hue = 0xFF00E5FF),
        ch("ch_bbc_arabic", "BBC News Arabic", "بي بي سي عربي", "https://vs-hls-push-ww-live.akamaized.net/x=4/i=urn:bbc:pips:service:bbc_arabic_tv/pc_hd_abr_v2.m3u8", ChannelCategory.NEWS, "BBC", "HD", "720p 50fps", hue = 0xFFFF0055),
        ch("ch_dw_arabic", "DW Arabic", "دويتشه فيله عربية", "https://dwamdstream104.akamaized.net/hls/live/2015530/dwstream104/index.m3u8", ChannelCategory.NEWS, "DW", bit = "1080p 50fps", hue = 0xFF00F0FF),
        ch("ch_france24_ar", "France 24 Arabic", "فرانس 24 عربي", "https://static.france24.com/live/F24_AR_HI_HLS/live_tv.m3u8", ChannelCategory.NEWS, "F24", "HD", "720p 50fps"),
        ch("ch_dubai_sports", "Dubai Sports 1", "دبي الرياضية 1", "https://dmisportshls.akamaized.net/hls/live/2026850/dubaisports/master.m3u8", ChannelCategory.SPORTS, "DSP", "4K", "2160p 60fps", true, 0xFF00E5FF),
        ch("ch_ad_sports", "Abu Dhabi Sports 1", "أبوظبي الرياضية 1", "https://adsportshls.akamaized.net/hls/live/2026851/adsports1/master.m3u8", ChannelCategory.SPORTS, "ADS", feat = true, hue = 0xFF8A3FFC),
        ch("ch_alkass", "Al Kass One HD", "الكأس 1 الرياضية", "https://alkasshls.akamaized.net/hls/live/2026852/alkass1/master.m3u8", ChannelCategory.SPORTS, "KAS", hue = 0xFFFF7700),
        ch("ch_yass_sports", "Yas Sports HD", "ياس الرياضية للفروسية", "https://yassportshls.akamaized.net/hls/live/2026853/yassports/master.m3u8", ChannelCategory.SPORTS, "YAS", bit = "1080p 50fps", hue = 0xFF00FF88),
        ch("ch_aj_doc", "Al Jazeera Doc", "الجزيرة الوثائقية", "https://live-hls-web-ajd.getaj.net/AJD/03.m3u8", ChannelCategory.DOCUMENTARY, "DOC", feat = true),
        ch("ch_natgeo_ar", "National Geo Wild", "عالم الطبيعة والوثائقي", "https://dwamdstream104.akamaized.net/hls/live/2015530/dwstream104/index.m3u8", ChannelCategory.DOCUMENTARY, "GEO", bit = "1080p 50fps", hue = 0xFFFFD700),
        ch("ch_quran_sa", "Saudi Quran Live", "قناة القرآن الكريم مكة", "https://saudiquranhls.akamaized.net/hls/live/2026854/quran/master.m3u8", ChannelCategory.ISLAMIC, "QRN", "4K", "2160p 60fps", true, 0xFF00FF88),
        ch("ch_sunnah_sa", "Saudi Sunnah Live", "قناة السنة النبوية المدينة", "https://saudisunnahhls.akamaized.net/hls/live/2026855/sunnah/master.m3u8", ChannelCategory.ISLAMIC, "SNH", "4K", "2160p 60fps"),
        ch("ch_space_toon", "Spacetoon HD", "سبيستون للأطفال", "https://spacetoonhls.akamaized.net/hls/live/2026856/spacetoon/master.m3u8", ChannelCategory.KIDS, "STN", bit = "1080p 50fps", hue = 0xFFFF007A),
        ch("ch_majid_kids", "Majid Kids TV", "ماجد كيدز", "https://majidkidshls.akamaized.net/hls/live/2026857/majid/master.m3u8", ChannelCategory.KIDS, "MJD", bit = "1080p 50fps", hue = 0xFFFFB800),
        ch("ch_dubai_tv", "Dubai TV Live", "تلفزيون دبي مباشر", "https://dmionetv.akamaized.net/hls/live/2026858/dubaitv/master.m3u8", ChannelCategory.ENTERTAINMENT, "DTV", bit = "1080p 50fps", hue = 0xFF8A3FFC),
        ch("ch_sharjah_tv", "Sharjah TV Live", "قناة الشارقة", "https://sharjahtvhls.akamaized.net/hls/live/2026859/sharjah/master.m3u8", ChannelCategory.ENTERTAINMENT, "SHJ", bit = "1080p 50fps", hue = 0xFF00E5FF)
    )

    private val custom = MutableStateFlow<List<Channel>>(emptyList())

    val allChannelsFlow: Flow<List<Channel>> = combine(custom, settings.favoritesFlow, settings.recentIdsFlow) { list, favs, recents ->
        val base = if (list.isEmpty()) defaults else list
        base.map { c ->
            val idx = recents.indexOf(c.id)
            c.copy(
                isFavorite = favs.contains(c.id),
                lastWatchedTimestamp = if (idx >= 0) System.currentTimeMillis() - idx * 60_000L else 0L
            )
        }
    }

    suspend fun getChannelById(id: String) = (if (custom.value.isEmpty()) defaults else custom.value).firstOrNull { it.id == id }
    suspend fun toggleFavorite(id: String) = settings.toggleFavorite(id)
    suspend fun markWatched(id: String) = settings.addRecentChannel(id)

    suspend fun loadM3uPlaylist(urlStr: String): Result<Int> = withContext(Dispatchers.IO) {
        if (urlStr.isBlank()) {
            custom.value = emptyList()
            return@withContext Result.success(defaults.size)
        }
        try {
            val conn = URL(urlStr).openConnection() as HttpURLConnection
            conn.connectTimeout = 8000
            conn.readTimeout = 8000
            conn.setRequestProperty("User-Agent", "777-DarkGlass/1.0")
            if (conn.responseCode != 200) return@withContext Result.failure(Exception("HTTP ${conn.responseCode}"))
            val reader = BufferedReader(InputStreamReader(conn.inputStream))
            val parsed = mutableListOf<Channel>()
            var title = ""
            var cat = ChannelCategory.NEWS
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                val t = line!!.trim()
                if (t.startsWith("#EXTINF:")) {
                    val group = Regex("group-title=\"([^\"]+)\"").find(t)?.groupValues?.get(1) ?: "Live TV"
                    cat = when {
                        group.contains("sport", true) || group.contains("رياض", true) -> ChannelCategory.SPORTS
                        group.contains("news", true) || group.contains("أخبار", true) -> ChannelCategory.NEWS
                        group.contains("doc", true) || group.contains("وثائق", true) -> ChannelCategory.DOCUMENTARY
                        group.contains("kid", true) || group.contains("أطفال", true) -> ChannelCategory.KIDS
                        group.contains("islam", true) || group.contains("قرآن", true) -> ChannelCategory.ISLAMIC
                        else -> ChannelCategory.ENTERTAINMENT
                    }
                    val parts = t.split(",")
                    title = if (parts.size > 1) parts.last().trim() else "قناة مباشرة"
                } else if (t.startsWith("http://") || t.startsWith("https://")) {
                    parsed.add(ch("custom_${parsed.size}_${title.hashCode()}", title, title, t, cat, title.take(3).uppercase(), feat = parsed.size < 2))
                }
            }
            reader.close()
            conn.disconnect()
            if (parsed.isNotEmpty()) {
                custom.value = parsed
                Result.success(parsed.size)
            } else Result.failure(Exception("لم يتم العثور على قنوات صالحة في الرابط"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
