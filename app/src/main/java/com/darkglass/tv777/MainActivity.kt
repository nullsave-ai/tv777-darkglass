package com.darkglass.tv777

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.darkglass.tv777.data.preferences.SettingsManager
import com.darkglass.tv777.data.repository.ChannelRepository
import com.darkglass.tv777.ui.MainScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.statusBarColor = Color.TRANSPARENT
        window.navigationBarColor = Color.TRANSPARENT
        val c = WindowInsetsControllerCompat(window, window.decorView)
        c.isAppearanceLightStatusBars = false
        c.isAppearanceLightNavigationBars = false
        val settings = SettingsManager(applicationContext)
        setContent { MainScreen(settings, ChannelRepository(settings)) }
    }
}
