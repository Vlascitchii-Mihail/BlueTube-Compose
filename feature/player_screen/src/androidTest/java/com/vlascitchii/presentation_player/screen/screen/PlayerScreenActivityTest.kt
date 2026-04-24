package com.vlascitchii.presentation_player.screen.screen

import android.content.Context
import android.net.ConnectivityManager
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.paging.PagingData
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.vlascitchii.domain.model.videos.YoutubeVideoDomain
import com.vlascitchii.domain.usecase.UseCase
import com.vlascitchii.domain.usecase.VideoPlayerUseCase
import com.vlascitchii.domain.util.VideoResult
import com.vlascitchii.presentation_common.model.util.CommonResultConverter
import com.vlascitchii.presentation_common.model.videos.YoutubeVideoUiModel
import com.vlascitchii.presentation_common.network_observer.NetworkConnectivityAbstraction
import com.vlascitchii.presentation_common.network_observer.NetworkConnectivityObserver
import com.vlascitchii.presentation_common.ui.state_common.UiSingleEvent
import com.vlascitchii.presentation_common.ui.state_common.UiState
import com.vlascitchii.presentation_common.ui.theme.BlueTubeComposeTheme
import com.vlascitchii.presentation_player.screen.OrientationState
import com.vlascitchii.presentation_player.screen.TestActivity
import com.vlascitchii.presentation_player.model.TestVideoUIModel
import com.vlascitchii.presentation_player.screen.state.PlayerActionState
import com.vlascitchii.ui_test.autorotation_system_setting.AUTOROTATION_OFF
import com.vlascitchii.ui_test.autorotation_system_setting.landscapeOrientation
import com.vlascitchii.ui_test.autorotation_system_setting.portraitOrientation
import com.vlascitchii.ui_test.autorotation_system_setting.setAutorotationEnabledValue
import com.vlascitchii.ui_test.autorotation_system_setting.unspecifiedOrientation
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import com.vlascitchii.common_ui.R as CommonR
import com.vlascitchii.player_screen.R as PlayerR

@RunWith(AndroidJUnit4::class)
class PlayerScreenActivityTest {

    @get:Rule
    val composeActivityTestRule: AndroidComposeTestRule<ActivityScenarioRule<TestActivity>, TestActivity> = createAndroidComposeRule(TestActivity::class.java)
    private val videoPlayerUseCase: UseCase<VideoPlayerUseCase.PlayerRequest, VideoPlayerUseCase.PlayerResponse> = mock()
    private val videoPlayerConverter: CommonResultConverter<VideoPlayerUseCase.PlayerResponse, @JvmSuppressWildcards Flow<PagingData<YoutubeVideoUiModel>>> = mock()

    private val context: Context = ApplicationProvider.getApplicationContext<Context>()

    private val networkConnectivityObserver: NetworkConnectivityAbstraction = NetworkConnectivityObserver(
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        )

    private lateinit var videoPlayerViewModel: VideoPlayerViewModel
    private lateinit var mviHandler: PlayerMviHandler

    private val pagingUiData: Flow<PagingData<YoutubeVideoUiModel>> =
        flowOf(PagingData.Companion.from(TestVideoUIModel.TestUIData.TEST_UI_VIDEO_LIST))
    private val pagingData: Flow<PagingData<YoutubeVideoDomain>> =
        flowOf(PagingData.Companion.from(emptyList()))
    
    private val expectedRelatedVideosUseCaseResponse: VideoResult<VideoPlayerUseCase.PlayerResponse> =
        VideoResult.Success(VideoPlayerUseCase.PlayerResponse(pagingData))
    private val successConverterUiState: UiState<Flow<PagingData<YoutubeVideoUiModel>>> =
        UiState.Success(pagingUiData)

    private val fullScreenIntent = PlayerActionState.UpdatePlayerOrientationStateAction(OrientationState.FULL_SCREEN)
    private val portraitIntent = PlayerActionState.UpdatePlayerOrientationStateAction(OrientationState.PORTRAIT)

    private val videoPlayerDescription: String =
        context.getString(PlayerR.string.video_player_description)
    private val videoContentDescription: String =
        context.getString(PlayerR.string.video_content_description)
    private val bottomNavigationDescription: String =
        context.getString(CommonR.string.bottom_navigation_description)
    private val videoItemDescription = context.getString(CommonR.string.video_compact_preview_description)

    private fun initPlayer() {
        composeActivityTestRule.setContent {
            BlueTubeComposeTheme {
                PlayerScreen(
                    video = TestVideoUIModel.TestUIData.TEST_UI_VIDEO_LIST.first(),
                    playerStateFlow = videoPlayerViewModel.playerStateFlow,
                    playerMVI = mviHandler,
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

        videoPlayerViewModel = VideoPlayerViewModel(
            videoPlayerUseCase,
            videoPlayerConverter,
            networkConnectivityObserver,
            Dispatchers.IO
        )

        mviHandler = PlayerMviHandler(
            videoPlayerViewModel,
            navigationHandler = { event: UiSingleEvent -> Unit}
        )

        whenever(videoPlayerUseCase.execute(any<VideoPlayerUseCase.PlayerRequest>()))
            .thenReturn(flowOf(expectedRelatedVideosUseCaseResponse))
        whenever(videoPlayerConverter.convertSuccessVideo(any<Flow<PagingData<YoutubeVideoDomain>>>()))
            .thenReturn(successConverterUiState)
        whenever(videoPlayerConverter.convert(any<VideoResult<VideoPlayerUseCase.PlayerResponse>>()))
            .thenReturn(successConverterUiState)

        initPlayer()
    }

    @Test
    fun playerScreen_updates_when_playerStateFlow_changes() {
        with(composeActivityTestRule) {
            onNodeWithContentDescription(videoPlayerDescription).assertIsDisplayed()
            onNodeWithContentDescription(videoContentDescription).assertIsDisplayed()
            onAllNodesWithContentDescription(videoItemDescription).onFirst().assertIsDisplayed()
            onNodeWithContentDescription(bottomNavigationDescription).assertDoesNotExist()
        }
    }

    @Test
    fun configurationChange_toLandscape_Player_enters_FillScreen() {
        with(composeActivityTestRule) {
            rotateToLandscape()

            mviHandler.submitAction(fullScreenIntent)

            onNodeWithContentDescription(videoPlayerDescription).assertIsDisplayed()
            onNodeWithContentDescription(videoContentDescription).assertDoesNotExist()
            onAllNodesWithContentDescription(videoItemDescription).onFirst().assertIsNotDisplayed()
        }
    }

    @Test
    fun rotationFrom_Landscape_toPortrait_shows_videoDetails_and_relatedVideoList() {
        with(composeActivityTestRule) {
            rotateToLandscape()
            rotateToPortrait()

            mviHandler.submitAction(PlayerActionState.UpdatePlayerOrientationStateAction(OrientationState.PORTRAIT))

            onNodeWithContentDescription(videoPlayerDescription).assertIsDisplayed()
            onNodeWithContentDescription(videoContentDescription).assertExists()
            onNodeWithContentDescription(videoContentDescription).assertIsDisplayed()
            onAllNodesWithContentDescription(videoItemDescription).onFirst().assertIsDisplayed()
            onNodeWithContentDescription(bottomNavigationDescription).assertDoesNotExist()
        }
    }

    @Test
    fun `outFromFullScreenAndLockPortraitOrientation locks orientation`() {
        with(composeActivityTestRule) {
            rotateToLandscape()

            mviHandler.submitAction(fullScreenIntent)
            waitForIdle()
            Espresso.pressBack()
            waitForIdle()

            assertTrue(activity.requestedOrientation == unspecifiedOrientation)
        }
    }
}
