package com.darkglass.tv777.ui.navigation

import com.darkglass.tv777.ui.components.Ic

enum class AppDestination(val route: String, val titleAr: String, val icon: Ic) {
    HOME("home", "الرئيسية", Ic.HOME),
    CHANNELS("channels", "القنوات", Ic.TV),
    SETTINGS("settings", "الإعدادات", Ic.SETTINGS)
}
