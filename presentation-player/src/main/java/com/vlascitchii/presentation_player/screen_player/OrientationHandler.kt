package com.vlascitchii.presentation_player.screen_player

import android.app.Activity
import android.content.pm.ActivityInfo
import android.view.View
import android.view.WindowManager
import androidx.paging.PagingData
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.FullscreenListener
import com.vlascitchii.presentation_common.model.videos.YoutubeVideoUiModel
import com.vlascitchii.presentation_common.ui.screen.CommonMVI
import com.vlascitchii.presentation_common.ui.state.UiState
import com.vlascitchii.presentation_player.databinding.FragmentPlayVideoBinding
import com.vlascitchii.presentation_player.screen_player.state.PlayerActionState
import com.vlascitchii.presentation_player.screen_player.state.PlayerNavigationEvent
import com.vlascitchii.presentation_player.screen_player.state.PlayerState
import kotlinx.coroutines.flow.Flow

const val PORTRAIT_ORIENTATION = 0
const val LANDSCAPE_ORIENTATION = 270
const val LANDSCAPE_REVERSE_ORIENTATION = 90

class OrientationHandler(
    private val binding: FragmentPlayVideoBinding,
    private val activity: Activity,
    private val playerMVI: CommonMVI<YoutubeVideoUiModel, UiState<Flow<PagingData<YoutubeVideoUiModel>>>, PlayerActionState, PlayerNavigationEvent>,
    private val playerState: PlayerState,
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

    fun toFullScreenIfIsFullScreenState(player: YouTubePlayer?) {
//        if (playerState.playerOrientationState == OrientationState.FULL_SCREEN) player?.toggleFullscreen()
    }

    fun setOnFullscreenClickListener(player: YouTubePlayer?) {
        binding.fullScreenButton.setOnClickListener {
            playerMVI.submitAction(PlayerActionState.SetFullscreenWidgetState(true))
//            player?.toggleFullscreen()
        }
    }

//    private fun getIsAutoRotationEnabled(context: Context): Boolean {
//        return try {
//            Settings.System.getInt(
//                context.contentResolver,
//                Settings.System.ACCELEROMETER_ROTATION
//            ) == 1
//        } catch (ex: Settings.SettingNotFoundException) {
//            false
//        }
//    }

    fun setScreenOrientationFromFlag(orientation: Int) {
        when {
//            PORTRAIT_ORIENTATION == orientation && getIsAutoRotationEnabled(activity.applicationContext) && !fullscreenWidgetIsClicked
            PORTRAIT_ORIENTATION == orientation && !playerState.fullscreenWidgetIsClicked
                -> {
                changeToPortraitOrientation()
            }

//            LANDSCAPE_ORIENTATION == orientation && getIsAutoRotationEnabled(activity.applicationContext) && !fullscreenWidgetIsClicked
            LANDSCAPE_ORIENTATION == orientation
                -> {
                changeToLandscapeOrientation()
            }

//            LANDSCAPE_REVERSE_ORIENTATION == orientation && getIsAutoRotationEnabled(activity.applicationContext) && !fullscreenWidgetIsClicked
            LANDSCAPE_REVERSE_ORIENTATION == orientation
                -> {
                changeToReverseLandscapeOrientation()
            }
            else -> {}
        }
        playerMVI.submitAction(PlayerActionState.SetFullscreenWidgetState(false))
    }

    fun changeToPortraitOrientation() {
        if (activity.requestedOrientation != portraitOrientation) {
            playerMVI.submitAction(PlayerActionState.UpdatePlayerOrientationStateAction(OrientationState.PORTRAIT))
            activity.requestedOrientation = portraitOrientation
            activity.window?.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }
    }

    fun changeToLandscapeOrientation() {
        if (activity.requestedOrientation != landscapeSensorOrientation) {
            playerMVI.submitAction(PlayerActionState.UpdatePlayerOrientationStateAction(OrientationState.FULL_SCREEN))
            activity.requestedOrientation = landscapeSensorOrientation
            activity.window?.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }
    }

    private fun changeToReverseLandscapeOrientation() {
        if (activity.requestedOrientation != reverseLandscapeOrientation) {
            playerMVI.submitAction(PlayerActionState.UpdatePlayerOrientationStateAction(OrientationState.FULL_SCREEN))
            activity.requestedOrientation = reverseLandscapeOrientation
            activity.window?.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }
    }

    fun setSensorOrientation() {
        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
    }

    fun selectFullScreenWidgetSelectedState(state: Boolean) {
        binding.fullScreenButton.isSelected = state
    }
}
