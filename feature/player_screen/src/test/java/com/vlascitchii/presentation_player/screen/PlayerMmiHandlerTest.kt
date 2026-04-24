package com.vlascitchii.presentation_player.screen

import com.vlascitchii.common_test.rule.DispatcherTestRule
import com.vlascitchii.presentation_common.ui.state_common.UiSingleEvent
import com.vlascitchii.presentation_player.screen.screen.PlayerMviHandler
import com.vlascitchii.presentation_player.screen.screen.VideoPlayerViewModel
import com.vlascitchii.presentation_player.screen.state.PlayerActionState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

@OptIn(ExperimentalCoroutinesApi::class)
class PlayerMmiHandlerTest {

    @get:Rule
    val dispatcherTestRule = DispatcherTestRule()

    private val viewModelPlayer: VideoPlayerViewModel = mock()
    private val navigationHandler: (UiSingleEvent) -> Unit = { event: UiSingleEvent -> }
    private lateinit var playerMVI: PlayerMviHandler

    @Before
    fun setup() {
        playerMVI =
            PlayerMviHandler(viewModelPlayer, navigationHandler)
    }

    @Test
    fun `submitAction(GetRelatedVideosAction) calls videoPlayerViewModel submitAction`() = runTest {
        val testVideoTitle = "Test title"
        val getRelatedVideosAction = PlayerActionState.GetRelatedVideosAction(testVideoTitle)

        playerMVI.submitAction(getRelatedVideosAction)
        advanceUntilIdle()

        verify(viewModelPlayer).submitAction(getRelatedVideosAction)
    }

    @Test
    fun `submitAction(UpdatePlayStateAction) calls videoPlayerViewModel submitAction`() = runTest {
        val updatePlayStateAction = PlayerActionState.UpdatePlayStateAction(false)

        playerMVI.submitAction(updatePlayStateAction)
        advanceUntilIdle()

        verify(viewModelPlayer).submitAction(updatePlayStateAction)
    }

    @Test
    fun `submitAction(UpdatePlaybackPositionAction) calls videoPlayerViewModel submitAction`() = runTest {
        val playerPosition = 1.7F
        val updatePlaybackPositionAction =
            PlayerActionState.UpdatePlaybackPositionAction(playerPosition)

        playerMVI.submitAction(updatePlaybackPositionAction)
        advanceUntilIdle()

        verify(viewModelPlayer).submitAction(updatePlaybackPositionAction)
    }

    @Test
    fun `submitAction(UpdatePlayerOrientationStateAction) calls videoPlayerViewModel submitAction`() = runTest {
        val orientationState = OrientationState.FULL_SCREEN
        val updatePlayerOrientationStateAction =
            PlayerActionState.UpdatePlayerOrientationStateAction(orientationState)

        playerMVI.submitAction(updatePlayerOrientationStateAction)
        advanceUntilIdle()

        verify(viewModelPlayer).submitAction(updatePlayerOrientationStateAction)
    }
}
