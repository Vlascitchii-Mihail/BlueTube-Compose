package com.vlascitchii.presentation_common

import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.vlascitchii.presentation_player.screen_player.OrientationState
import com.vlascitchii.data_local.enetity.video_list.videos.YoutubeVideoEntity.Companion.DEFAULT_VIDEO
import com.vlascitchii.presentation_common.utils.VideoPlayerScreenTags
//import dagger.hilt.android.testing.HiltAndroidRule
//import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
//@HiltAndroidTest
class VideoPlayerNetworkAccessibilityTest {

    @get:Rule
    val composeAndroidTestRule = createComposeRule()

//    @get:Rule
//    val hiltRule = HiltAndroidRule(this)

    private val defaultVideo = DEFAULT_VIDEO
    private lateinit var videoThumbnailDesc: String

    private fun initVideoPlayer(
        connectivityStatus: com.appelier.bluetubecompose.network_observer.ConnectivityStatus = com.appelier.bluetubecompose.network_observer.ConnectivityStatus.Available
    ) {
        composeAndroidTestRule.setContent {
            videoThumbnailDesc = stringResource(R.string.video_thumbnail_description)

            com.vlascitchii.presentation_player.screen_player.YoutubeVideoPlayer(
                videoId = defaultVideo.id,
                modifier = Modifier,
                isVideoPlaysFlow = MutableStateFlow(true),
                updateVideoIsPlayState = { isPlaying: Boolean -> },
                popBackStack = {},
                updatePlaybackPosition = { position: Float -> },
                getPlaybackPosition = { 1F },
                playerOrientationState = MutableStateFlow(com.vlascitchii.presentation_player.screen_player.OrientationState.PORTRAIT),
                updatePlayerOrientationState = { playerOrientatioState: com.vlascitchii.presentation_player.screen_player.OrientationState -> },
                fullscreenWidgetIsClicked = MutableStateFlow(false),
                setFullscreenWidgetIsClicked = { isClicked: Boolean -> },
                connectivityStatus = connectivityStatus
            )
        }
    }

    @Test
    fun onAccessible_internet_connection_show_videoThumbnail_instead_the_player() {
        initVideoPlayer()
        with(composeAndroidTestRule) {
            onNodeWithTag(com.vlascitchii.presentation_common.utils.VideoPlayerScreenTags.VIDEO_PLAYER).assertIsDisplayed()
            onNodeWithContentDescription(videoThumbnailDesc).assertIsNotDisplayed()
        }
    }

    @Test
    fun onAbsence_internet_connection_show_videoThumbnail_instead_the_player() {
        initVideoPlayer(com.appelier.bluetubecompose.network_observer.ConnectivityStatus.Lost)
        with(composeAndroidTestRule) {
            onNodeWithContentDescription(videoThumbnailDesc).assertIsDisplayed()
            onNodeWithTag(com.vlascitchii.presentation_common.utils.VideoPlayerScreenTags.VIDEO_PLAYER).assertIsNotDisplayed()
        }
    }
}