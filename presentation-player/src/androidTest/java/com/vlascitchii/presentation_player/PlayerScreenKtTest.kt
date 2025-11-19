package com.vlascitchii.presentation_player

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.paging.PagingData
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.vlascitchii.presentation_common.R
import com.vlascitchii.presentation_common.model.videos.YoutubeVideoUiModel
import com.vlascitchii.presentation_common.model.videos.YoutubeVideoUiModel.Companion.PREVIEW_VIDEO_LIST
import com.vlascitchii.presentation_common.ui.bottom_navigation.BlueTubeBottomNavigation
import com.vlascitchii.presentation_common.ui.bottom_navigation.VIDEO_LIST
import com.vlascitchii.presentation_common.ui.screen.CommonMVI
import com.vlascitchii.presentation_common.ui.state.UiState
import com.vlascitchii.presentation_player.screen_player.OrientationState
import com.vlascitchii.presentation_player.screen_player.screen.PlayerScreen
import com.vlascitchii.presentation_player.screen_player.state.PlayerActionState
import com.vlascitchii.presentation_player.screen_player.state.PlayerNavigationEvent
import com.vlascitchii.presentation_player.screen_player.state.PlayerState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import com.vlascitchii.presentation_common.R as CommonR
import com.vlascitchii.presentation_player.R as PlayerR

@RunWith(AndroidJUnit4::class)
class PlayerScreenKtTest {

    @get:Rule
    val composeTestRule: ComposeContentTestRule = createComposeRule()

    private val playerMVI: CommonMVI<YoutubeVideoUiModel,UiState<Flow<PagingData<YoutubeVideoUiModel>>>, PlayerActionState, PlayerNavigationEvent> = mock()
    private lateinit var playerStateFlow: MutableStateFlow<PlayerState>
    private lateinit var commonLoadingScreenDescription : String
    private lateinit var circularProgressIndicatorDescription: String
    private lateinit var videoPlayerDescription: String
    private lateinit var bottomNavigationDescription: String
    private lateinit var videoContentDescription: String
    private lateinit var videoItemLandscapeDescription: String
    private lateinit var videoItemDescription: String
    private lateinit var bottomNavigationHomeScreenTab: String

    private val pagingUiData: Flow<PagingData<YoutubeVideoUiModel>> = flowOf(PagingData.from(PREVIEW_VIDEO_LIST))
    private val successUiState: UiState<Flow<PagingData<YoutubeVideoUiModel>>> = UiState.Success(pagingUiData)

    @Composable
    private fun SetArtefacts() {
        playerStateFlow = MutableStateFlow(PlayerState())
        circularProgressIndicatorDescription = stringResource(CommonR.string.circular_progress_indicator)
        videoPlayerDescription = stringResource(PlayerR.string.video_player_description)
        bottomNavigationDescription = stringResource(CommonR.string.bottom_navigation_description)
        commonLoadingScreenDescription = stringResource(CommonR.string.common_loading_screen_compose_description)
        videoContentDescription = stringResource(PlayerR.string.video_content_description)
        videoItemLandscapeDescription = stringResource(CommonR.string.video_medium_preview_description)
        videoItemDescription = stringResource(CommonR.string.video_compact_preview_description)
        bottomNavigationHomeScreenTab = stringResource(R.string.bottom_navigation_description)
    }

    @Test
    fun playerScree_updates_when_playerStateFlow_changes() {
        with(composeTestRule) {
            setContent {
                SetArtefacts()

                PlayerScreen(
                    video = YoutubeVideoUiModel(),
                    playerStateFlow = playerStateFlow,
                    playerMVI = playerMVI,
                    playbackPosition = 0F,
                    bottomNavigation = { BlueTubeBottomNavigation() }
                )
            }

            onNodeWithContentDescription(commonLoadingScreenDescription).assertIsDisplayed()

            playerStateFlow.value = PlayerState(relatedVideoState = successUiState)

            onNodeWithContentDescription(videoPlayerDescription).assertIsDisplayed()
            onNodeWithContentDescription(videoContentDescription).assertIsDisplayed()
            onNodeWithContentDescription(circularProgressIndicatorDescription).assertIsDisplayed()
            onNodeWithContentDescription(bottomNavigationDescription).assertIsDisplayed()
            onNodeWithContentDescription(VIDEO_LIST).assertIsSelected()
        }
    }

    @Test
    fun only_player_is_visible_on_landscape_view() {

        with(composeTestRule) {
            setContent {
                SetArtefacts()

                PlayerScreen(
                    video = YoutubeVideoUiModel(),
                    playerStateFlow = playerStateFlow,
                    playerMVI = playerMVI,
                    playbackPosition = 0F,
                    bottomNavigation = { BlueTubeBottomNavigation() }
                )
            }

            onNodeWithContentDescription(commonLoadingScreenDescription).assertIsDisplayed()

            playerStateFlow.value = PlayerState(
                relatedVideoState = successUiState,
                playerOrientationState = OrientationState.FULL_SCREEN
            )

            onNodeWithContentDescription(videoPlayerDescription).assertIsDisplayed()
            onNodeWithContentDescription(videoContentDescription).assertDoesNotExist()
            onNodeWithContentDescription(circularProgressIndicatorDescription).assertDoesNotExist()
            onNodeWithContentDescription(bottomNavigationDescription).assertDoesNotExist()
        }
    }
}
