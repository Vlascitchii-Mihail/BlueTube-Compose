package com.vlascitchii.presentation_video_list.video_list

import com.appelier.bluetubecompose.network_observer.NetworkConnectivityObserver
import com.vlascitchii.presentation_video_list.screen_video_list.repository.VideoListRepositoryImpl
import com.vlascitchii.presentation_video_list.screen_video_list.screen.VideoListViewModel
import com.vlascitchii.presentation_video_list.search_video.SearchState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.mock

@RunWith(MockitoJUnitRunner::class)
class VideoListViewModelTest {

    private val repository: com.vlascitchii.presentation_video_list.screen_video_list.repository.VideoListRepositoryImpl = mock<com.vlascitchii.presentation_video_list.screen_video_list.repository.VideoListRepositoryImpl>()
    private val networkConnectivityObserver: com.appelier.bluetubecompose.network_observer.NetworkConnectivityObserver = mock()
    private val viewModel =
        com.vlascitchii.presentation_video_list.screen_video_list.screen.VideoListViewModel(
            repository,
            networkConnectivityObserver
        )

    @Test
    fun `SearchAppBar text updates`() {
        val testText = "TestText"
        viewModel.updateSearchTextState(testText)

        assertEquals(testText, viewModel.searchTextState.value)
    }

    @Test
    fun `SearchAppBar changes state`() {
        assertTrue(viewModel.searchState.value == com.vlascitchii.presentation_video_list.search_video.SearchState.CLOSED)

        viewModel.updateSearchState(com.vlascitchii.presentation_video_list.search_video.SearchState.OPENED)
        assertTrue(viewModel.searchState.value == com.vlascitchii.presentation_video_list.search_video.SearchState.OPENED)

        viewModel.updateSearchState(com.vlascitchii.presentation_video_list.search_video.SearchState.CLOSED)
        assertTrue(viewModel.searchState.value == com.vlascitchii.presentation_video_list.search_video.SearchState.CLOSED)
    }
}