package com.vlascitchii.bluetubecompose.navigation

import androidx.navigation3.runtime.NavKey
import com.vlascitchii.presentation_common.model.videos.YoutubeVideoUiModel
import com.vlascitchii.presentation_player.screen_player.screen.VIDEO_PLAYER_BOTTOM_NAV_NAME
import com.vlascitchii.presentation_shorts.screen_shorts.screen.SHORTS_BOTTOM_NAV_NAME
import com.vlascitchii.presentation_video_list.screen.VIDEO_LIST_BOTTOM_NAV_NAME
import com.vlascitchii.presenttion_settings.screen_settings.SETTINGS_BOTTOM_NAV_NAME
import kotlinx.serialization.Serializable

@Serializable
sealed class ScreenType(val name: String) : NavKey {

    @Serializable
    data class VideoList(val query: String = ""): ScreenType(VIDEO_LIST_BOTTOM_NAV_NAME)
    @Serializable
    data class PlayerScreen(val video: YoutubeVideoUiModel) : ScreenType(VIDEO_PLAYER_BOTTOM_NAV_NAME)
    @Serializable
    data object ShortsScreen: ScreenType(SHORTS_BOTTOM_NAV_NAME)
    @Serializable
    data object SettingsScreen: ScreenType(SETTINGS_BOTTOM_NAV_NAME)
}
