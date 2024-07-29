package com.appelier.bluetubecompose.navigation

import com.appelier.bluetubecompose.screen_video_list.model.videos.YoutubeVideo
import kotlinx.serialization.Serializable

const val HOME = "Home"
const val PLAYER = "PLayer"
const val SHORTS = "Shorts"
const val SETTINGS = "Settings"

@Serializable
sealed class ScreenType(val name: String) {

    @Serializable
    data object VideoList: ScreenType(HOME)
    @Serializable
    data class PlayerScreen(val video: YoutubeVideo) : ScreenType(PLAYER)
    @Serializable
    data object ShortsScreen: ScreenType(SHORTS)
    @Serializable
    data object SettingsScreen: ScreenType(SETTINGS)
}