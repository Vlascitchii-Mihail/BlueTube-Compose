package com.appelier.bluetubecompose.screen_player

import android.app.Activity
import android.content.pm.ActivityInfo
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.activity.findViewTreeFullyDrawnReporterOwner
import androidx.compose.runtime.MutableState
import androidx.lifecycle.Lifecycle
import com.appelier.bluetubecompose.databinding.FragmentPlayVideoBinding
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.FullscreenListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.loadOrCueVideo

private const val INITIAL_VIDEO_PLAYBACK_POSITION = 0F
class YouTubePlayerHandler(
    private val binding: FragmentPlayVideoBinding,
    private var playerOrientationState: MutableState<OrientationState>,
    private val activity: Activity,
    private val currentComposeLifecycle: Lifecycle,
    private val videoId: String,
    private val getUpdatedPlaybackPosition: (Float) -> Float
) {

    var blueTubeYouTubePlayer: YouTubePlayer? = null
//    private val landscapeOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    private val landscapeSensorOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
//    private val portraitUserOrientation = ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT
    private val portraitSensorOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT
//    private val sensorOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR
//    lateinit var playerFullScreenView: View
//    var playerHeight: Int = 0

    init {
        setupFullScreenListener()
        setupPlayerWidgets()
    }

    //change function's name
    fun setScreenAppearanceFlag() {
        if (activity.requestedOrientation == landscapeSensorOrientation) {
            activity.window?.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            playerOrientationState.value = OrientationState.FULL_SCREEN
            Log.d("Player test", "setScreenAppearanceFlag in landscape")

        } else {
            activity.window?.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
//            playerOrientationState.value = OrientationState.PORTRAIT
            Log.d("Player test", "setScreenAppearanceFlag in portrait")

        }
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

            override fun onCurrentSecond(youTubePlayer: YouTubePlayer, second: Float) {
                super.onCurrentSecond(youTubePlayer, second)
                getUpdatedPlaybackPosition.invoke(second)
            }

            override fun onReady(youTubePlayer: YouTubePlayer) {
                blueTubeYouTubePlayer = youTubePlayer
                youTubePlayer.loadOrCueVideo(
                    currentComposeLifecycle, videoId, getUpdatedPlaybackPosition.invoke(
                        INITIAL_VIDEO_PLAYBACK_POSITION
                    )
                )
            }
        }
    }

    private fun setupFullScreenListener() {
        with(binding) {
            ytPlayer.addFullscreenListener(object: FullscreenListener {
                override fun onEnterFullscreen(fullscreenView: View, exitFullscreen: () -> Unit) {
                    Log.d("Player test", "onEnterFullscreen()")

//                    playerFullScreenView = fullscreenView
                    setFullScreenVisibility(fullscreenView)
                    changeToLandscapeOrientation(activity)
                }

                override fun onExitFullscreen() {
                    Log.d("Player test", "onExitFullscreen")

                    setPortraitVisibility()
                    changeToPortraitOrientation(activity)
                }
            })
        }
    }

    fun setFullScreenVisibility(fullscreenView: View) {
        with(binding) {
//            playerHeight = ytPlayer.height

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

     fun changeToLandscapeOrientation(activity: Activity) {
         if (activity.requestedOrientation != landscapeSensorOrientation) {
            activity.requestedOrientation = landscapeSensorOrientation
        }
     }

    fun changeToPortraitOrientation(activity: Activity) {
        Log.d("Player test", "changeToPortraitOrientation()")
        if (activity.requestedOrientation != portraitSensorOrientation) {
            activity.requestedOrientation = portraitSensorOrientation
        }
    }
}