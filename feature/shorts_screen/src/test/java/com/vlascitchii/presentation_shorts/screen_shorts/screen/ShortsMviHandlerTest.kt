package com.vlascitchii.presentation_shorts.screen_shorts.screen

import com.vlascitchii.common_test.rule.DispatcherTestRule
import com.vlascitchii.presentation_common.ui.state_common.UiSingleEvent
import com.vlascitchii.presentation_shorts.screen_shorts.state.ShortsAction
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

@OptIn(ExperimentalCoroutinesApi::class)
class ShortsMviHandlerTest {

    @get:Rule
    val dispatcherTestRule = DispatcherTestRule()

    private val shortsViewModel: ShortsViewModel = mock()
    private val navigationHandler: (UiSingleEvent) -> Unit = { event: UiSingleEvent -> }
    private lateinit var shortsMviHandler: ShortsMviHandler

    @Before
    fun setup() {
        shortsMviHandler = ShortsMviHandler(shortsViewModel, navigationHandler)
    }

    @Test
    fun `submitAction(ListenToVideoQueueAction) cals ShortsViewModel submitAction`() = runTest {
        val testAction: ShortsAction.ListenToVideoQueueAction = ShortsAction.ListenToVideoQueueAction

        shortsMviHandler.submitAction(testAction)
        advanceUntilIdle()

        verify(shortsViewModel).submitAction(testAction)
    }

    @Test
    fun `submitAction(FetchShortsAction) calls ShortsViewModel submitAction`() = runTest {
        val testAction = ShortsAction.FetchShortsAction

        shortsMviHandler.submitAction(testAction)
        advanceUntilIdle()

        verify(shortsViewModel).submitAction(testAction)
    }
}
