package com.appelier.bluetubecompose.screen.player

import com.appelier.bluetubecompose.screen_player.VideoPlayerViewModel
import com.appelier.bluetubecompose.screen_video_list.repository.VideoListRepository
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner

private const val INITIAL_VIDEO_PLAYBACK_POSITION = 0F
private const val NEW_VIDEO_PLAYBACK_POSITION = 5F


@RunWith(MockitoJUnitRunner::class)
class VideoPlayerViewModelTest {

    private val repository: VideoListRepository = mock()
    private val viewModel = VideoPlayerViewModel(repository)

    @Test
    fun `current playback position updates`() {
        assertEquals(INITIAL_VIDEO_PLAYBACK_POSITION, viewModel.getCurrentPlaybackPosition())
        viewModel.updatePlaybackPosition(NEW_VIDEO_PLAYBACK_POSITION)
        assertEquals(NEW_VIDEO_PLAYBACK_POSITION, viewModel.getCurrentPlaybackPosition())
    }
}
