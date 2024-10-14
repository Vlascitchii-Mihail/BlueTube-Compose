package com.appelier.bluetubecompose.screen_player

import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.provider.Settings
import android.view.View
import android.view.WindowManager
import com.appelier.bluetubecompose.databinding.FragmentPlayVideoBinding
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.FullscreenListener
import kotlinx.coroutines.flow.StateFlow

const val PORTRAIT_ORIENTATION = 0
const val LANDSCAPE_ORIENTATION = 270
const val LANDSCAPE_REVERSE_ORIENTATION = 90

class OrientationHandler(
    private val binding: FragmentPlayVideoBinding,
    private val activity: Activity,
    private val playerOrientationState: OrientationState,
    private val updatePlayerOrientationState: (OrientationState) -> Unit,
    private val fullscreenWidgetIsClicked: StateFlow<Boolean>,
    private val setFullscreenWidgetIsClicked: (Boolean) -> Unit,
) {

    private val landscapeSensorOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
    private val reverseLandscapeOrientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE
    private val portraitOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

    init {
        setupFullScreenListener()
    }

    private fun setupFullScreenListener() {
        binding.ytPlayer.addFullscreenListener(object : FullscreenListener {

            override fun onEnterFullscreen(fullscreenView: View, exitFullscreen: () -> Unit) {
                setFullScreenVisibility(fullscreenView)
                changeToLandscapeOrientation()
                activity.window?.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            }

            override fun onExitFullscreen() {
                setPortraitVisibility()
                changeToPortraitOrientation()
                activity.window?.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            }
        })
    }

    private fun setFullScreenVisibility(fullscreenView: View) {
        binding.llPlayerContainer.apply {
            removeAllViews()
            addView(fullscreenView)
        }
    }

    private fun setPortraitVisibility() {
        binding.llPlayerContainer.apply {
            removeAllViews()
            addView(binding.ytPlayer)
        }
    }

    fun initFullScreenWidgetState(player: YouTubePlayer?) {
        if (playerOrientationState == OrientationState.FULL_SCREEN) player?.toggleFullscreen()
    }

    fun setOnFullscreenClickListener(player: YouTubePlayer?) {
        binding.fullScreenButton.setOnClickListener {
            setFullscreenWidgetIsClicked.invoke(true)
            player?.toggleFullscreen()
        }
    }

    private fun getIsAutoRotationEnabled(context: Context): Boolean {
        return try {
            Settings.System.getInt(
                context.contentResolver,
                Settings.System.ACCELEROMETER_ROTATION
            ) == 1
        } catch (ex: Settings.SettingNotFoundException) {
            false
        }
    }

    fun setScreenAppearanceOrientationFlag(orientation: Int) {
        when {
            PORTRAIT_ORIENTATION == orientation && getIsAutoRotationEnabled(activity.applicationContext) && !fullscreenWidgetIsClicked.value
            -> {
                changeToPortraitOrientation()
            }

            LANDSCAPE_ORIENTATION == orientation && getIsAutoRotationEnabled(activity.applicationContext) && !fullscreenWidgetIsClicked.value
            -> {
                changeToLandscapeOrientation()
            }

            LANDSCAPE_REVERSE_ORIENTATION == orientation && getIsAutoRotationEnabled(activity.applicationContext) && !fullscreenWidgetIsClicked.value
            -> {
                changeToReverseLandscapeOrientation()
            }
            else -> {}
        }
        setFullscreenWidgetIsClicked.invoke(false)
    }

    fun changeToPortraitOrientation() {
        if (activity.requestedOrientation != portraitOrientation) {
            updatePlayerOrientationState(OrientationState.PORTRAIT)
            activity.requestedOrientation = portraitOrientation
            activity.window?.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }
    }

    fun changeToLandscapeOrientation() {
        if (activity.requestedOrientation != landscapeSensorOrientation) {
            updatePlayerOrientationState(OrientationState.FULL_SCREEN)
            activity.requestedOrientation = landscapeSensorOrientation
            activity.window?.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }
    }

    private fun changeToReverseLandscapeOrientation() {
        if (activity.requestedOrientation != reverseLandscapeOrientation) {
            updatePlayerOrientationState(OrientationState.FULL_SCREEN)
            activity.requestedOrientation = reverseLandscapeOrientation
            activity.window?.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }
    }
}
