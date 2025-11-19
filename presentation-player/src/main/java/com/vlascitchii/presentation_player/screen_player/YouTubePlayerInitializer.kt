package com.vlascitchii.presentation_player.screen_player

import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.paging.PagingData
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import com.vlascitchii.presentation_common.model.videos.YoutubeVideoUiModel
import com.vlascitchii.presentation_common.ui.screen.CommonMVI
import com.vlascitchii.presentation_common.ui.state.UiState
import com.vlascitchii.presentation_player.databinding.FragmentPlayVideoBinding
import com.vlascitchii.presentation_player.screen_player.state.PlayerActionState
import com.vlascitchii.presentation_player.screen_player.state.PlayerNavigationEvent
import kotlinx.coroutines.flow.Flow

class YouTubePlayerInitializer(
    private val binding: FragmentPlayVideoBinding,
    private val localContext: Context,
    private val currentComposeLifecycle: Lifecycle,
    private val videoId: String,
    private val playerMVI: CommonMVI<YoutubeVideoUiModel, UiState<Flow<PagingData<YoutubeVideoUiModel>>>, PlayerActionState, PlayerNavigationEvent>,
    private val isVideoPlays: Boolean,
    private val currentPlaybackPosition: Float,
    private val orientationHandler: OrientationHandler,
) {

    var player: YouTubePlayer? = null

    init {
        setupPlayerWidgets()
    }

    private fun setupPlayerWidgets() {
        binding.ytPlayer.apply {
            currentComposeLifecycle.addObserver(this)
            currentComposeLifecycle
            enableAutomaticInitialization = false
            val fullScreenControl = IFramePlayerOptions.Builder(localContext).controls(1).fullscreen(1).build()
            val youTubePlayerListener = getYouTubePlayerListener()
            initialize(youTubePlayerListener, fullScreenControl)
        }
    }

    private fun getYouTubePlayerListener(): AbstractYouTubePlayerListener {
        return object : AbstractYouTubePlayerListener() {

            override fun onReady(youTubePlayer: YouTubePlayer) {
                player = youTubePlayer

                orientationHandler.toFullScreenIfIsFullScreenState(player)
//                orientationHandler.setOnFullscreenClickListener(player)

                when (isVideoPlays) {
                    true -> youTubePlayer.loadVideo(videoId, currentPlaybackPosition)
                    false -> youTubePlayer.cueVideo(videoId, currentPlaybackPosition)
                }
            }

            override fun onStateChange(
                youTubePlayer: YouTubePlayer,
                state: PlayerConstants.PlayerState
            ) {
                super.onStateChange(youTubePlayer, state)
                when (state) {
                    PlayerConstants.PlayerState.PAUSED -> {
                        playerMVI.submitAction(PlayerActionState.UpdatePlayStateAction(false))
                    }
                    PlayerConstants.PlayerState.PLAYING -> {
                        playerMVI.submitAction(PlayerActionState.UpdatePlayStateAction(true))
                    }
                    else -> {}
                }
            }

            override fun onCurrentSecond(youTubePlayer: YouTubePlayer, second: Float) {
                super.onCurrentSecond(youTubePlayer, second)
                playerMVI.submitAction(PlayerActionState.UpdatePlaybackPositionAction(second))
            }
        }
    }
}
