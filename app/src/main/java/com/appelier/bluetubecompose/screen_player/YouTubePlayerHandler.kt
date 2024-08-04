package com.appelier.bluetubecompose.screen_player

import android.app.Activity
import android.content.pm.ActivityInfo
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.compose.runtime.MutableState
import androidx.lifecycle.Lifecycle
import com.appelier.bluetubecompose.databinding.FragmentPlayVideoBinding
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.FullscreenListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.loadOrCueVideo

class YouTubePlayerHandler(
    private val binding: FragmentPlayVideoBinding,
    private var playerOrientationState: MutableState<OrientationState>,
    private val activity: Activity,
    private val currentComposeLifecycle: Lifecycle,
    private val videoId: String
) {

    private lateinit var youTubePlayer: YouTubePlayer
    private val landscapeSensorOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
    private val portraitSensorOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT
    private lateinit var playerFullScreenView: View
    private var playerHeight: Int = 0

    init {
        setupFullScreenListener()
        setupPlayerWidgets()
    }

    private fun setupPlayerWidgets() {
        binding.ytPlayer.apply {
            currentComposeLifecycle.addObserver(this)
            enableAutomaticInitialization = false
            val fullScreenControl = IFramePlayerOptions.Builder().controls(1).fullscreen(1).build()
            val youTubePlayerListener = getYouTubePlayerListener(videoId, currentComposeLifecycle)
            initialize(youTubePlayerListener, fullScreenControl)
        }
    }

    private fun getYouTubePlayerListener(
        videoId: String,
        currentComposeLifecycle: Lifecycle
    ): AbstractYouTubePlayerListener {
        return object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                this@YouTubePlayerHandler.youTubePlayer = youTubePlayer
                youTubePlayer.loadOrCueVideo(currentComposeLifecycle, videoId, 0F)
            }
        }
    }

    private fun setupFullScreenListener() {
        with(binding) {
            ytPlayer.addFullscreenListener(object: FullscreenListener {
                override fun onEnterFullscreen(fullscreenView: View, exitFullscreen: () -> Unit) {
                    playerFullScreenView = fullscreenView
                    setFullScreenVisibility(fullscreenView)
                    changeToLandscapeOrientation()
                }

                override fun onExitFullscreen() {
                    setPortraitVisibility()
                    changeToPortraitOrientation()
                }
            })
        }
    }

    private fun setFullScreenVisibility(fullscreenView: View) {
        with(binding) {
            playerHeight = ytPlayer.height

            llPlayerContainer.apply {
                removeAllViews()
                addView(fullscreenView)
            }
        }

        playerOrientationState.value = OrientationState.FULL_SCREEN
        setScreenVisibilityFlag()
    }

    private fun setPortraitVisibility() {
        with(binding) {
            llPlayerContainer.apply {

                removeAllViews()
                addView(ytPlayer)
            }
        }

        playerOrientationState.value = OrientationState.PORTRAIT
        setScreenVisibilityFlag()
    }

    private fun setScreenVisibilityFlag() {
        when(playerOrientationState.value) {
            OrientationState.PORTRAIT -> activity.window?.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            OrientationState.FULL_SCREEN -> activity.window?.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }
    }

     private fun changeToLandscapeOrientation() {
         if (activity.requestedOrientation != landscapeSensorOrientation) {
             activity.requestedOrientation = landscapeSensorOrientation
         }
     }

    private fun changeToPortraitOrientation() {
        if (activity.requestedOrientation != portraitSensorOrientation) {
            activity.requestedOrientation = portraitSensorOrientation
        }
    }

    fun togglePlayerOrientation() {
        youTubePlayer.toggleFullscreen()
    }

    fun getPlayerContainerHeight(): Int {
        return if (playerHeight > 0) playerHeight
        else ViewGroup.LayoutParams.WRAP_CONTENT
    }

    fun setFullScreenVideoFitScreenHeight() {
        playerFullScreenView.layoutParams.height = binding.ytPlayer.width
    }
}
