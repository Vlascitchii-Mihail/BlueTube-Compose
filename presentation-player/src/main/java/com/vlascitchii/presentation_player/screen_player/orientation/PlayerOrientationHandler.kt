package com.vlascitchii.presentation_player.screen_player.orientation

import android.app.Activity
import android.view.View
import android.view.WindowInsets
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.FullscreenListener
import com.vlascitchii.presentation_common.ui.screen.mvi.CommonMVI
import com.vlascitchii.presentation_player.databinding.FragmentPlayVideoBinding
import com.vlascitchii.presentation_player.screen_player.OrientationState
import com.vlascitchii.presentation_player.screen_player.state.PlayerActionState
import com.vlascitchii.presentation_player.screen_player.state.PlayerNavigationEvent
import com.vlascitchii.presentation_player.screen_player.state.PlayerState
import kotlinx.coroutines.flow.StateFlow

class PlayerOrientationHandler(
    private val binding: FragmentPlayVideoBinding,
    private val activity: Activity,
    private val playerMVI: CommonMVI<PlayerActionState, PlayerNavigationEvent>,
    val playerStateFlow: StateFlow<PlayerState>,
) : OrientationHandler() {

    init {
        setupFullScreenListener()
        setupFullScreenWidget()
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

    private fun hideStatusBar() {
        activity.window.insetsController?.hide(WindowInsets.Type.statusBars())
    }

    private fun showStatusBar() {
        activity.window.insetsController?.show(WindowInsets.Type.statusBars())
    }

    override fun changeToPortraitOrientation() {
        if (activity.requestedOrientation != portraitOrientation) {
            playerMVI.submitAction(PlayerActionState.UpdatePlayerOrientationStateAction(OrientationState.PORTRAIT))

            activity.requestedOrientation = portraitOrientation
            showStatusBar()
        }

        setUnspecifiedOrientationIfAutorotationOFF()
    }

    override fun changeToLandscapeOrientation() {
        if (activity.requestedOrientation != landscapeOrientation) {
            playerMVI.submitAction(PlayerActionState.UpdatePlayerOrientationStateAction(OrientationState.FULL_SCREEN))

            activity.requestedOrientation = landscapeOrientation
            hideStatusBar()
        }
    }

    override fun changeToReverseLandscapeOrientation() {
        if (activity.requestedOrientation != reverseLandscapeOrientation) {
            playerMVI.submitAction(PlayerActionState.UpdatePlayerOrientationStateAction(OrientationState.FULL_SCREEN))

            activity.requestedOrientation = reverseLandscapeOrientation
            hideStatusBar()
        }
    }

    private fun setUnspecifiedOrientationIfAutorotationOFF() {
        if (!getAutoRotationIsEnabledState(activity)) activity.requestedOrientation = unspecifiedOrientation
    }

    override fun outFromFullScreenSetStaticPortraitOrientation() {
        playerMVI.submitAction(PlayerActionState.ApproveOrientationChange(false))

        changeToPortraitOrientation()
    }

    override fun setSensorOrientation() {
        activity.requestedOrientation = unspecifiedOrientation
    }

    override fun rotateWhenScreenAutoRotateSettingIsEnabled(orientation: Int) {
        if (getAutoRotationIsEnabledState(activity) && playerStateFlow.value.isOrientationApproved) {
            setScreenOrientationInAutorotateModeOn(orientation)
        }
        else {
            playerMVI.submitAction(PlayerActionState.ApproveOrientationChange(true))
        }
    }

    override fun rotateWhenAutoRotationSettingIsDisabled(currentSystemOrientation: Int) {
        if (!getAutoRotationIsEnabledState(activity) && playerStateFlow.value.isOrientationApproved) {
            setScreenOrientationInAutorotateModeOff(currentSystemOrientation)
        }
        else playerMVI.submitAction(PlayerActionState.ApproveOrientationChange(true))
    }

    private fun toggleFullScreen() {
        when(playerStateFlow.value.playerOrientationState) {
            OrientationState.PORTRAIT ->  {
                changeToLandscapeOrientation()
                playerMVI.submitAction(PlayerActionState.ApproveOrientationChange(false))
            }
            OrientationState.FULL_SCREEN -> {
                changeToPortraitOrientation()
                playerMVI.submitAction(PlayerActionState.ApproveOrientationChange(false))
            }
        }
    }

    fun setupFullScreenWidget() {
        binding.fullScreenButton.setOnClickListener {
            toggleFullScreen()
        }
    }
}
