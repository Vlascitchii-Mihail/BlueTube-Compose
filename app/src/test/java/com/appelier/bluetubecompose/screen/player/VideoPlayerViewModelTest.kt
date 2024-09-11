package com.appelier.bluetubecompose.screen.player

import androidx.paging.PagingData
import com.appelier.bluetubecompose.screen_player.VideoPlayerViewModel
import com.appelier.bluetubecompose.screen_video_list.model.videos.YoutubeVideoResponse.Companion.DEFAULT_VIDEO_RESPONSE_WITH_CHANNEL_IMG
import com.appelier.bluetubecompose.screen_video_list.repository.VideoListRepository
import kotlinx.coroutines.flow.flow
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever

private const val RELATED_TITLE: String = "Test title"
private const val INITIAL_VIDEO_PLAYBACK_POSITION = 0F
private const val NEW_VIDEO_PLAYBACK_POSITION = 5F


@RunWith(MockitoJUnitRunner::class)
class VideoPlayerViewModelTest {

    private val repository: VideoListRepository = mock()
    private val viewModel = VideoPlayerViewModel(repository)

    @Before
    fun setup() {
        whenever(
            repository.fetchVideos(any(), any())
        ).thenReturn(flow { PagingData.from(DEFAULT_VIDEO_RESPONSE_WITH_CHANNEL_IMG.items) })
    }

    @Test
    fun `getSearchedRelatedVideos() assigns stateFlow to the _relatedVideoStateFlow`() {
        assertTrue(viewModel.relatedVideoStateFlow == null)
        viewModel.getSearchedRelatedVideos(RELATED_TITLE)
        assertFalse(viewModel.relatedVideoStateFlow == null)
    }

    @Test
    fun `current playback position updates`() {
        assertEquals(INITIAL_VIDEO_PLAYBACK_POSITION, viewModel.getCurrentPlaybackPosition())
        viewModel.updatePlaybackPosition(NEW_VIDEO_PLAYBACK_POSITION)
        assertEquals(NEW_VIDEO_PLAYBACK_POSITION, viewModel.getCurrentPlaybackPosition())
    }
}
