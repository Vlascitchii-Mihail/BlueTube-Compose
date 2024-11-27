package com.appelier.bluetubecompose.ui.screens.video_player

import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.appelier.bluetubecompose.R
import com.appelier.bluetubecompose.core.core_api.network_observer.ConnectivityStatus
import com.appelier.bluetubecompose.core.core_ui.views.YoutubeVideoPlayer
import com.appelier.bluetubecompose.screen_player.OrientationState
import com.appelier.bluetubecompose.screen_video_list.model.videos.YoutubeVideo.Companion.DEFAULT_VIDEO
import com.appelier.bluetubecompose.utils.VideoPlayerScreenTags
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class VideoPlayerNetworkAccessibilityTest {

    @get:Rule
    val composeAndroidTestRule = createComposeRule()

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    private val defaultVideo = DEFAULT_VIDEO
    private lateinit var videoThumbnailDesc: String

    private fun initVideoPlayer(
        connectivityStatus: ConnectivityStatus = ConnectivityStatus.Available
    ) {
        composeAndroidTestRule.setContent {
            videoThumbnailDesc = stringResource(R.string.video_thumbnail_description)

            YoutubeVideoPlayer(
                videoId = defaultVideo.id,
                modifier = Modifier,
                isVideoPlaysFlow = MutableStateFlow(true),
                updateVideoIsPlayState = { isPlaying: Boolean ->},
                popBackStack = {},
                updatePlaybackPosition = { position: Float -> },
                getPlaybackPosition = { 1F },
                playerOrientationState = MutableStateFlow(OrientationState.PORTRAIT),
                updatePlayerOrientationState = { playerOrientatioState: OrientationState -> },
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
            onNodeWithTag(VideoPlayerScreenTags.VIDEO_PLAYER).assertIsDisplayed()
            onNodeWithContentDescription(videoThumbnailDesc).assertIsNotDisplayed()
        }
    }

    @Test
    fun onAbsence_internet_connection_show_videoThumbnail_instead_the_player() {
        initVideoPlayer(ConnectivityStatus.Lost)
        with(composeAndroidTestRule) {
            onNodeWithContentDescription(videoThumbnailDesc).assertIsDisplayed()
            onNodeWithTag(VideoPlayerScreenTags.VIDEO_PLAYER).assertIsNotDisplayed()
        }
    }
}