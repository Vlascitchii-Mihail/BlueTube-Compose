package com.vlascitchii.bluetubecompose.navigation

import androidx.navigation3.runtime.NavKey
import com.vlascitchii.presentation_common.model.videos.YoutubeVideoUiModel
import kotlinx.serialization.Serializable

private const val VIDEO = "Video"
private const val PLAYER = "PLayer"
private const val SHORTS = "Shorts"
private const val SETTINGS = "Settings"

@Serializable
sealed class ScreenType(val name: String) : NavKey {

    @Serializable
    data class VideoList(val query: String = ""): ScreenType(VIDEO)
    @Serializable
    data class PlayerScreen(val video: YoutubeVideoUiModel) : ScreenType(PLAYER)
    @Serializable
    data object ShortsScreen: ScreenType(SHORTS)
    @Serializable
    data object SettingsScreen: ScreenType(SETTINGS)
}
