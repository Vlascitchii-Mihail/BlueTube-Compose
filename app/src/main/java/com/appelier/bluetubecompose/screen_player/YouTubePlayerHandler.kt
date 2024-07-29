package com.appelier.bluetubecompose.screen_player

import android.app.Activity
import android.content.pm.ActivityInfo
import android.view.View
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

    var youTubePlayer: YouTubePlayer? = null
    private val landscapeOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    private val landscapeSensorOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
    private val portraitUserOrientation = ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT
    private val sensorOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR
    lateinit var playerFullScreenView: View
    var playerHeight: Int = 0

    init {
        setupFullScreenListener()
        setupPlayerWidgets()
    }

    private fun setupPlayerWidgets() {
        binding.ytPlayer.apply {
            currentComposeLifecycle.addObserver(this)
            enableAutomaticInitialization = false
            val fullScreenControl = IFramePlayerOptions.Builder().controls(1).fullscreen(1).build()
            initialize(getYouTubePlayerListener(videoId, currentComposeLifecycle), fullScreenControl)
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
                    changeToLandscapeOrientation(activity)
                }

                override fun onExitFullscreen() {
                    setPortraitVisibility()
                    changeToPortraitOrientation(activity)
                }
            })
        }
    }

    fun setFullScreenVisibility(fullscreenView: View) {
        with(binding) {
            playerHeight = ytPlayer.height

            llPlayerContainer.apply {
                removeAllViews()
                addView(fullscreenView)
            }
        }

        playerOrientationState.value = OrientationState.FULL_SCREEN

        //FLAG_FULLSCREEN - hide status bar
        activity.window?.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

    fun setPortraitVisibility() {
        with(binding) {
            llPlayerContainer.apply {

                removeAllViews()
                addView(ytPlayer)
            }
        }

        playerOrientationState.value = OrientationState.PORTRAIT
        activity.window?.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

     fun changeToLandscapeOrientation(activity: Activity?) {
        if (activity?.requestedOrientation != landscapeOrientation) {
            activity?.requestedOrientation = landscapeSensorOrientation
        }
     }

    fun changeToPortraitOrientation(activity: Activity?) {
        if (activity?.requestedOrientation != portraitUserOrientation) {
            activity?.requestedOrientation = portraitUserOrientation
        }
    }
}