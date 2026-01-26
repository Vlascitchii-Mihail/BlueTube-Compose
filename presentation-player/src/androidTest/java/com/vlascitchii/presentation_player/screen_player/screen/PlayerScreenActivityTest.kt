package com.vlascitchii.presentation_player.screen_player.screen

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.paging.PagingData
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.vlascitchii.presentation_common.model.videos.YoutubeVideoUiModel
import com.vlascitchii.presentation_common.model.videos.YoutubeVideoUiModel.Companion.PREVIEW_VIDEO_LIST
import com.vlascitchii.presentation_common.ui.screen.mvi.CommonMVI
import com.vlascitchii.presentation_common.ui.state.UiState
import com.vlascitchii.presentation_common.ui.theme.BlueTubeComposeTheme
import com.vlascitchii.presentation_player.screen_player.OrientationState
import com.vlascitchii.presentation_player.screen_player.TestActivity
import com.vlascitchii.presentation_player.screen_player.state.PlayerActionState
import com.vlascitchii.presentation_player.screen_player.state.PlayerNavigationEvent
import com.vlascitchii.presentation_player.screen_player.state.PlayerState
import com.vlascitchii.ui_test.autorotation_system_setting.AUTOROTATION_OFF
import com.vlascitchii.ui_test.autorotation_system_setting.landscapeOrientation
import com.vlascitchii.ui_test.autorotation_system_setting.portraitOrientation
import com.vlascitchii.ui_test.autorotation_system_setting.setAutorotationEnabledValue
import com.vlascitchii.ui_test.autorotation_system_setting.unspecifiedOrientation
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.spy
import com.vlascitchii.presentation_common.R as CommonR
import com.vlascitchii.presentation_player.R as PlayerR

@RunWith(AndroidJUnit4::class)
class PlayerScreenActivityTest {

    @get:Rule
    val composeActivityTestRule =
        createAndroidComposeRule(TestActivity::class.java)

    private val context = ApplicationProvider.getApplicationContext<Context>()

    private val pagingUiData: Flow<PagingData<YoutubeVideoUiModel>> =
        flowOf(PagingData.Companion.from(PREVIEW_VIDEO_LIST))
    private val successUiState: UiState<Flow<PagingData<YoutubeVideoUiModel>>> =
        UiState.Success(pagingUiData)
    private lateinit var playerStateFlow: MutableStateFlow<PlayerState>

    private val fullScreenIntent = PlayerActionState.UpdatePlayerOrientationStateAction(OrientationState.FULL_SCREEN)
    private val portraitIntent = PlayerActionState.UpdatePlayerOrientationStateAction(OrientationState.PORTRAIT)

    private val playerMVI: CommonMVI<PlayerActionState, PlayerNavigationEvent> = spy(
        object : CommonMVI<PlayerActionState, PlayerNavigationEvent>() {
            override fun handleAction(action: PlayerActionState) {
                when (action) {
                    is PlayerActionState.GetRelatedVideosAction -> {}
                    is PlayerActionState.UpdatePlayStateAction -> {}
                    is PlayerActionState.UpdatePlaybackPositionAction -> {}
                    is PlayerActionState.UpdatePlayerOrientationStateAction -> {
                        playerStateFlow.value = PlayerState(
                            relatedVideoState = successUiState,
                            playerOrientationState = action.orientationState
                        )
                    }

                    is PlayerActionState.ApproveOrientationChange -> {
                        playerStateFlow.value = PlayerState(
                            relatedVideoState = successUiState,
                            isOrientationApproved = action.isOrientationApproved
                        )
                    }
                }
            }

            override fun handleNavigationEvent(singleEvent: PlayerNavigationEvent) {
                println("handleNavigationEvent() waa called")
            }
        }
    )

    private val circularProgressIndicatorDescription: String =
        context.getString(CommonR.string.circular_progress_indicator)
    private val videoPlayerDescription: String =
        context.getString(PlayerR.string.video_player_description)
    private val videoContentDescription: String =
        context.getString(PlayerR.string.video_content_description)
    private val bottomNavigationDescription: String =
        context.getString(CommonR.string.bottom_navigation_description)

    private fun initPlayer() {
        composeActivityTestRule.setContent {
            BlueTubeComposeTheme {
                PlayerScreen(
                    video = PREVIEW_VIDEO_LIST.first(),
                    playerStateFlow = playerStateFlow,
                    playerMVI = playerMVI,
                    playbackPosition = 0F,
                )
            }
        }
    }

    private fun rotateToPortrait() {
        composeActivityTestRule.activity.requestedOrientation = portraitOrientation
        initPlayer()
    }

    private fun rotateToLandscape() {
        composeActivityTestRule.activity.requestedOrientation = landscapeOrientation
        initPlayer()
    }

    @Before
    fun setup() {
        setAutorotationEnabledValue(AUTOROTATION_OFF)

        playerStateFlow = MutableStateFlow(PlayerState())
        initPlayer()
    }

    @Test
    fun playerScreen_updates_when_playerStateFlow_changes() {
        with(composeActivityTestRule) {
            onNodeWithContentDescription(videoPlayerDescription).assertIsDisplayed()
            onNodeWithContentDescription(videoPlayerDescription).assertIsDisplayed()
            onNodeWithContentDescription(videoContentDescription).assertIsDisplayed()
            onNodeWithContentDescription(circularProgressIndicatorDescription).assertIsDisplayed()
            onNodeWithContentDescription(bottomNavigationDescription).assertDoesNotExist()
        }
    }

    @Test
    fun configurationChange_toLandscape_Player_enters_FillScreen() {
        with(composeActivityTestRule) {
            rotateToLandscape()

            playerMVI.submitAction(fullScreenIntent)

            onNodeWithContentDescription(videoPlayerDescription).assertIsDisplayed()
            onNodeWithContentDescription(videoContentDescription).assertDoesNotExist()
            onNodeWithContentDescription(circularProgressIndicatorDescription).assertDoesNotExist()
        }
    }

    @Test
    fun rotationFrom_Landscape_toPortrait_shows_videoDetails_and_relatedVideoList() {
        with(composeActivityTestRule) {
            rotateToLandscape()
            rotateToPortrait()

            playerMVI.submitAction(PlayerActionState.UpdatePlayerOrientationStateAction(OrientationState.PORTRAIT))

            onNodeWithContentDescription(videoPlayerDescription).assertIsDisplayed()
            onNodeWithContentDescription(videoContentDescription).assertExists()
            onNodeWithContentDescription(videoContentDescription).assertIsDisplayed()
            onNodeWithContentDescription(circularProgressIndicatorDescription).assertIsDisplayed()
            onNodeWithContentDescription(circularProgressIndicatorDescription).assertExists()
            onNodeWithContentDescription(bottomNavigationDescription).assertDoesNotExist()
        }
    }

    @Test
    fun `outFromFullScreenAndLockPortraitOrientation locks orientation`() {
        with(composeActivityTestRule) {
            rotateToLandscape()

            playerMVI.submitAction(fullScreenIntent)
            waitForIdle()
            Espresso.pressBack()
            waitForIdle()

            assertTrue(activity.requestedOrientation == unspecifiedOrientation)
        }
    }
}
