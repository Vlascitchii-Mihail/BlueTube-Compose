package com.appelier.bluetubecompose.screen_player

import android.app.Activity
import androidx.compose.runtime.MutableState
import androidx.lifecycle.Lifecycle
import com.appelier.bluetubecompose.databinding.FragmentPlayVideoBinding
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions

class YouTubePlayerHandler(
    private val binding: FragmentPlayVideoBinding,
    playerOrientationState: MutableState<OrientationState>,
    activity: Activity,
    private val currentComposeLifecycle: Lifecycle,
    private val videoId: String,
    private var youTubePlayerPlayState:  MutableState<Boolean>,
    private val updatePlaybackPosition:(Float) -> Unit,
    private val getCurrentPlaybackPosition: () -> Float
) {

    var player: YouTubePlayer? = null
    private val orientationHandler = OrientationHandler(binding, activity, playerOrientationState)

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

                when {
                    youTubePlayerPlayState.value -> youTubePlayer.loadVideo(videoId, getCurrentPlaybackPosition.invoke())
                    else -> youTubePlayer.cueVideo(videoId, getCurrentPlaybackPosition.invoke())
                }
            }

            override fun onStateChange(
                youTubePlayer: YouTubePlayer,
                state: PlayerConstants.PlayerState
            ) {
                super.onStateChange(youTubePlayer, state)
                when(state) {
                    PlayerConstants.PlayerState.PAUSED -> {
                        youTubePlayerPlayState.value = false
                    }
                    PlayerConstants.PlayerState.PLAYING -> {
                        youTubePlayerPlayState.value = true
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

    fun changeToPortraitOrientation() {
        orientationHandler.changeToPortraitOrientation()
    }

    fun setScreenAppearanceOrientationFlag(orientation: Int) {
        orientationHandler.setScreenAppearanceOrientationFlag(orientation)
    }
}
