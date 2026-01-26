package com.vlascitchii.presentation_common.ui.screen

import androidx.navigation3.runtime.NavKey
import com.vlascitchii.presentation_common.model.videos.YoutubeVideoUiModel
import kotlinx.serialization.Serializable

const val VIDEO_LIST = "Video list"
const val SHORTS: String = "Shorts"
const val SETTINGS: String = "Settings"

@Serializable
sealed class ScreenType(val name: String) : NavKey {

    @Serializable
    data class VideoList(val query: String = ""): ScreenType(VIDEO_LIST)
    @Serializable
    data class PlayerScreen(val video: YoutubeVideoUiModel) : ScreenType(VIDEO_LIST)
    @Serializable
    data object ShortsScreen: ScreenType(SHORTS)
    @Serializable
    data object SettingsScreen: ScreenType(SETTINGS)
}
