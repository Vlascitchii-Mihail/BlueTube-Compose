package com.appelier.bluetubecompose.screen.shorts

import androidx.lifecycle.viewModelScope
import com.appelier.bluetubecompose.screen_shorts.repository.ShortsRepository
import com.appelier.bluetubecompose.screen_shorts.screen.ShortsViewModel
import com.appelier.bluetubecompose.search_video.model.DEFAULT_SHORTS_RESPONSE
import com.appelier.bluetubecompose.utils.VideoType
import kotlinx.coroutines.flow.flow
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.whenever

@RunWith(MockitoJUnitRunner::class)
class ShortsViewModelTest {

    private val repository: ShortsRepository = mock()
    private val viewModel = ShortsViewModel(repository)

    @Before
    fun setup() {
        whenever(
            repository.fetchShorts(VideoType.Shorts, viewModel.viewModelScope)
        ).thenReturn(flow { DEFAULT_SHORTS_RESPONSE })
    }

    @Test
    fun `getShorts() assigns stateFlow to the _shortsVideoState`() {
        assertTrue(viewModel.shortsVideoState == null)
        viewModel.getShorts()
        assertTrue(viewModel.shortsVideoState != null)
    }

}
