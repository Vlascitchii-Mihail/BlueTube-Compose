package com.appelier.bluetubecompose.screen.video_list

import com.appelier.bluetubecompose.core.core_api.network_observer.NetworkConnectivityObserver
import com.appelier.bluetubecompose.screen_video_list.repository.VideoListRepositoryImpl
import com.appelier.bluetubecompose.screen_video_list.screen.VideoListViewModel
import com.appelier.bluetubecompose.search_video.SearchState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.mock

@RunWith(MockitoJUnitRunner::class)
class VideoListViewModelTest {

    private val repository: VideoListRepositoryImpl = mock<VideoListRepositoryImpl>()
    private val networkConnectivityObserver: NetworkConnectivityObserver = mock()
    private val viewModel = VideoListViewModel(repository, networkConnectivityObserver)

    @Test
    fun `SearchAppBar text updates`() {
        val testText = "TestText"
        viewModel.updateSearchTextState(testText)

        assertEquals(testText, viewModel.searchTextState.value)
    }

    @Test
    fun `SearchAppBar changes state`() {
        assertTrue(viewModel.searchState.value == SearchState.CLOSED)

        viewModel.updateSearchState(SearchState.OPENED)
        assertTrue(viewModel.searchState.value == SearchState.OPENED)

        viewModel.updateSearchState(SearchState.CLOSED)
        assertTrue(viewModel.searchState.value == SearchState.CLOSED)
    }
}