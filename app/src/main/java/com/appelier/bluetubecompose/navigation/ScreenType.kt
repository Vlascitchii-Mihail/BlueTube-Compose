package com.appelier.bluetubecompose.navigation

import com.vlascitchii.presentation_common.entity.videos.YoutubeVideoUiModel
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
    data class PlayerScreen(val video: YoutubeVideoUiModel) : ScreenType(PLAYER)
    @Serializable
    data object ShortsScreen: ScreenType(SHORTS)
    @Serializable
    data object SettingsScreen: ScreenType(SETTINGS)
}