package com.vlascitchii.presentation_player.screen_player

import androidx.lifecycle.Lifecycle
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import com.vlascitchii.presentation_player.databinding.FragmentPlayVideoBinding

class YouTubePlayerHandler(
    private val binding: FragmentPlayVideoBinding,
    private val currentComposeLifecycle: Lifecycle,
    private val videoId: String,
    private val isVideoPlays: Boolean,
    private val updateVideoIsPlayState: (Boolean) -> Unit,
    private val updatePlaybackPosition: (Float) -> Unit,
    private val getCurrentPlaybackPosition: () -> Float,
    private val orientationHandler: OrientationHandler,
) {

    var player: YouTubePlayer? = null

    init {
        setupPlayerWidgets()
    }

    private fun setupPlayerWidgets() {
        binding.ytPlayer.apply {
            currentComposeLifecycle.addObserver(this)
            enableAutomaticInitialization = false
            val fullScreenControl = IFramePlayerOptions.Builder().controls(1).fullscreen(1).build()
            val youTubePlayerListener = getYouTubePlayerListener()
            initialize(youTubePlayerListener, fullScreenControl)
        }
    }

    private fun getYouTubePlayerListener(): AbstractYouTubePlayerListener {
        return object : AbstractYouTubePlayerListener() {

            override fun onReady(youTubePlayer: YouTubePlayer) {
                player = youTubePlayer

                orientationHandler.initFullScreenWidgetState(player)
                orientationHandler.setOnFullscreenClickListener(player)

                when (isVideoPlays) {
                    true -> youTubePlayer.loadVideo(videoId, getCurrentPlaybackPosition.invoke())
                    false -> youTubePlayer.cueVideo(videoId, getCurrentPlaybackPosition.invoke())
                }
            }

            override fun onStateChange(
                youTubePlayer: YouTubePlayer,
                state: PlayerConstants.PlayerState
            ) {
                super.onStateChange(youTubePlayer, state)
                when (state) {
                    PlayerConstants.PlayerState.PAUSED -> {
                        updateVideoIsPlayState(false)
                    }

                    PlayerConstants.PlayerState.PLAYING -> {
                        updateVideoIsPlayState(true)
                    }

                    else -> {}
                }
            }

            override fun onCurrentSecond(youTubePlayer: YouTubePlayer, second: Float) {
                super.onCurrentSecond(youTubePlayer, second)
                updatePlaybackPosition.invoke(second)
            }
        }
    }
}
