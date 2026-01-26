@file:OptIn(ExperimentalCoroutinesApi::class)

package com.vlascitchii.presentation_player.screen_player.mvi

import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.vlascitchii.common_test.rule.DispatcherTestRule
import com.vlascitchii.presentation_common.model.videos.YoutubeVideoUiModel
import com.vlascitchii.presentation_common.ui.screen.ScreenType
import com.vlascitchii.presentation_player.screen_player.OrientationState
import com.vlascitchii.presentation_player.screen_player.screen.PlayerMVI
import com.vlascitchii.presentation_player.screen_player.screen.VideoPlayerViewModel
import com.vlascitchii.presentation_player.screen_player.state.PlayerActionState
import com.vlascitchii.presentation_player.screen_player.state.PlayerNavigationEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class PlayerMVITest {

    @get:Rule
    val dispatcherTestRule = DispatcherTestRule()

    private val viewModelPlayer: VideoPlayerViewModel = mock()
    private val backStack: NavBackStack<NavKey> = mock()
    private lateinit var playerMVI: PlayerMVI

    @Before
    fun setup() {
        playerMVI =
            PlayerMVI(viewModelPlayer, backStack, CoroutineScope(dispatcherTestRule.testDispatcher))
    }

    @Test
    fun `submitAction(ACTION) calls videoPlayerViewModel getSearchedRelatedVideos`() = runTest {
        val testVideoTitle = "Test title"
        val job = launch {
            val getRelatedVideosAction = PlayerActionState.GetRelatedVideosAction(testVideoTitle)
            playerMVI.submitAction(getRelatedVideosAction)
        }

        advanceUntilIdle()

        verify(viewModelPlayer).getSearchedRelatedVideos(testVideoTitle)
        job.cancel()
    }

    @Test
    fun `submitAction(ACTION) calls videoPlayerViewModel updateVideoPlayState`() = runTest {
        val job = launch {
            val updatePlayStateAction = PlayerActionState.UpdatePlayStateAction(false)
            playerMVI.submitAction(updatePlayStateAction)
        }

        advanceUntilIdle()

        verify(viewModelPlayer).updateVideoPlayState(false)
        job.cancel()
    }

    @Test
    fun `submitAction(ACTION) calls videoPlayerViewModel updatePlaybackPosition`() = runTest {
        val playerPosition = 1.7F
        val job = launch {
            val updatePlaybackPositionAction = PlayerActionState.UpdatePlaybackPositionAction(playerPosition)
            playerMVI.submitAction(updatePlaybackPositionAction)
        }

        advanceUntilIdle()

        verify(viewModelPlayer).updatePlaybackPosition(playerPosition)
        job.cancel()
    }

    @Test
    fun `submitAction(ACTION) calls videoPlayerViewModel updatePlayerOrientationState`() = runTest {
        val orientationState = OrientationState.FULL_SCREEN
        val job = launch {
            val updatePlayerOrientationStateAction = PlayerActionState.UpdatePlayerOrientationStateAction(orientationState)
            playerMVI.submitAction(updatePlayerOrientationStateAction)
        }

        advanceUntilIdle()

        verify(viewModelPlayer).updatePlayerOrientationState(orientationState)
        job.cancel()
    }

    @Test
    fun `submitSingleNavigationEvent(EVENT) calls navigateToPlayerScreen`() = runTest {
        val youTubeVideo: YoutubeVideoUiModel = YoutubeVideoUiModel()
        val job = launch {
            val navigationPlayerScreenEvent = PlayerNavigationEvent.NavigationPlayerScreenEvent(youTubeVideo)
            playerMVI.submitSingleNavigationEvent(navigationPlayerScreenEvent)
        }

        advanceUntilIdle()

        verify(backStack).removeLastOrNull()
        verify(backStack).add(ScreenType.PlayerScreen(youTubeVideo))
        job.cancel()
    }

    @Test
    fun `submitSingleNavigationEvent(EVENT) calls backStack removeLastOrNull`() = runTest {
        val job = launch {
            val popBackStackEvent = PlayerNavigationEvent.PopBackStackEvent
            playerMVI.submitSingleNavigationEvent(popBackStackEvent)
        }

        advanceUntilIdle()

        verify(backStack).removeLastOrNull()
        job.cancel()
    }
}
