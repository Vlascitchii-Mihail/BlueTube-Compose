package com.vlascitchii.bluetubecompose.navigation

import androidx.navigation3.runtime.NavKey
import com.vlascitchii.presentation_common.model.videos.YoutubeVideoUiModel
import com.vlascitchii.presentation_common.ui.bottom_navigation.*
import kotlinx.serialization.Serializable

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
