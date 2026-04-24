package com.vlascitchii.presentation_video_list.screen

import com.vlascitchii.common_test.rule.DispatcherTestRule
import com.vlascitchii.presentation_common.ui.state_common.UiSingleEvent
import com.vlascitchii.presentation_common.ui.video_list.state.SearchState
import com.vlascitchii.presentation_common.ui.video_list.state.UiVideoListAction
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

@OptIn(ExperimentalCoroutinesApi::class)
class VideoListMviHandlerTest {
    @get:Rule
    val dispatcherTestRule = DispatcherTestRule()

    private val videoListViewModel: VideoListViewModel = mock()
    private val navigationHandler: (UiSingleEvent) -> Unit = { event: UiSingleEvent -> }
    private lateinit var videoListMviHandler: VideoListMviHandler

    @Before
    fun setup() {
        videoListMviHandler = VideoListMviHandler(videoListViewModel, navigationHandler)
    }

    @Test
    fun `submitAction(GetVideo) calls VideoListViewModel submitAction`() {
        val testVideoTitle = "Test title"
        val testAction = UiVideoListAction.GetVideo(testVideoTitle)

        videoListMviHandler.submitAction(testAction)

        verify(videoListViewModel).submitAction(testAction)
    }

    @Test
    fun `submitAction(ChangeSearchBarAppearance) calls VideoListViewModel submitAction`() {
        val testSearchbarState = SearchState.OPENED
        val testAction = UiVideoListAction.ChangeSearchBarAppearance(testSearchbarState)

        videoListMviHandler.submitAction(testAction)

        verify(videoListViewModel).submitAction(testAction)
    }

    @Test
    fun `submitAction(TypeInSearchAppBarTextField) calls VideoListViewModel submitAction`() {
        val testTypedText = "Test typed text"
        val testAction = UiVideoListAction.TypeInSearchAppBarTextField(testTypedText)

        videoListMviHandler.submitAction(testAction)

        verify(videoListViewModel).submitAction(testAction)
    }
}
