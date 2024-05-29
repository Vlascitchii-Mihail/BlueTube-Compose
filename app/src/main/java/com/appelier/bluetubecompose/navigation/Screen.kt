package com.appelier.bluetubecompose.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen(val name: String) {

    @Serializable
    data object VideoList: Screen("Home")
    @Serializable
    data class PlayerScreen(val videoId: String) : Screen("PLayer")
    @Serializable
    data object ShortsScreen: Screen("Shorts")
    @Serializable
    data object SettingsScreen: Screen("Settings")
}