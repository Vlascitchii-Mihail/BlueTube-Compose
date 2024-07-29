package com.appelier.bluetubecompose.ui.screen.screen_video_list


import com.appelier.bluetubecompose.screen_video_list.repository.VideoListRepositoryImpl
import com.appelier.bluetubecompose.screen_video_list.screen.VideoListViewModel
import com.appelier.bluetubecompose.search_video.SearchState
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class VideoListViewModelTest {

    @Mock
    private lateinit var repository: VideoListRepositoryImpl
    private lateinit var viewModel: VideoListViewModel
    @Before
    fun setup() {
        viewModel = VideoListViewModel(repository)
    }

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