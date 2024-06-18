package com.appelier.bluetubecompose.navigation

import kotlinx.serialization.Serializable

private const val HOME = "Home"
private const val PLAYER = "PLayer"
private const val SHORTS = "Shorts"
private const val SETTINGS = "Settings"

@Serializable
sealed class ScreenType(val name: String) {

    @Serializable
    data object VideoList: ScreenType(HOME)
    @Serializable
    data class PlayerScreen(val videoId: String) : ScreenType(PLAYER)
    @Serializable
    data object ShortsScreen: ScreenType(SHORTS)
    @Serializable
    data object SettingsScreen: ScreenType(SETTINGS)
}