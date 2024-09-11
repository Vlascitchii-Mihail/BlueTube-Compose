package com.appelier.bluetubecompose.screen_player

import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.provider.Settings
import android.view.View
import android.view.WindowManager
import androidx.compose.runtime.MutableState
import com.appelier.bluetubecompose.databinding.FragmentPlayVideoBinding
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.FullscreenListener

const val PORTRAIT_ORIENTATION = 0
const val LANDSCAPE_ORIENTATION = 270
const val LANDSCAPE_REVERSE_ORIENTATION = 90

class OrientationHandler(
    private val binding: FragmentPlayVideoBinding,
    private val activity: Activity,
    private val playerOrientationState: MutableState<OrientationState>,
) {

    private val landscapeSensorOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
    private val landscapeReverseOrientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE
    private val portraitSensorOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT
    private val unspecifiedScreenOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED

    init {
        setupFullScreenListener()
    }

    private fun setupFullScreenListener() {
        binding.ytPlayer.addFullscreenListener(object : FullscreenListener {

            override fun onEnterFullscreen(fullscreenView: View, exitFullscreen: () -> Unit) {
                setFullScreenVisibility(fullscreenView)
                changeToLandscapeOrientation()
            }

            override fun onExitFullscreen() {
                setPortraitVisibility()
                changeToPortraitOrientation()
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

    fun changeToLandscapeOrientation() {
        if (activity.requestedOrientation != landscapeSensorOrientation) {
            activity.requestedOrientation = landscapeSensorOrientation
        }
    }

    fun changeToPortraitOrientation() {
        if (activity.requestedOrientation != portraitSensorOrientation) {
            activity.requestedOrientation = portraitSensorOrientation
        }
    }

    fun initFullScreenWidgetState(player: YouTubePlayer?) {
        if (activity.requestedOrientation == landscapeSensorOrientation ||
            activity.requestedOrientation == landscapeReverseOrientation) {
            player?.toggleFullscreen()
        }
    }

    private fun getAutoRotationEnabledState(context: Context): Boolean {
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
            PORTRAIT_ORIENTATION == orientation && getAutoRotationEnabledState(activity.applicationContext) ||
                    activity.requestedOrientation == portraitSensorOrientation -> {
                setupPortraitAppearance()
            }

            LANDSCAPE_ORIENTATION == orientation && getAutoRotationEnabledState(activity.applicationContext) ||
                    activity.requestedOrientation == landscapeSensorOrientation -> {
                setupLandscapeAppearance()
            }

            LANDSCAPE_REVERSE_ORIENTATION == orientation && getAutoRotationEnabledState(activity.applicationContext) ||
                    activity.requestedOrientation == landscapeReverseOrientation -> {
                setupLandscapeAppearance()
            }
            else -> {}
        }
    }

    private fun setupPortraitAppearance() {
        playerOrientationState.value = OrientationState.PORTRAIT
        changeToAutoRotation()
        binding.ytPlayer.wrapContent()
        activity.window?.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }
    private fun changeToAutoRotation() {
        activity.requestedOrientation = unspecifiedScreenOrientation
    }


    private fun setupLandscapeAppearance() {
        playerOrientationState.value = OrientationState.FULL_SCREEN
        activity.window?.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        binding.ytPlayer.matchParent()
    }
}