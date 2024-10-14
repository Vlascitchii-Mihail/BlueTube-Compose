package com.appelier.bluetubecompose.core.core_ui.views

import android.app.Activity
import android.view.LayoutInflater
import android.view.OrientationEventListener
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.appelier.bluetubecompose.databinding.FragmentPlayVideoBinding
import com.appelier.bluetubecompose.screen_player.OrientationHandler
import com.appelier.bluetubecompose.screen_player.OrientationState
import com.appelier.bluetubecompose.screen_player.YouTubePlayerHandler
import com.appelier.bluetubecompose.utils.VideoPlayerScreenTags.VIDEO_PLAYER
import kotlinx.coroutines.flow.StateFlow

@Composable
fun YoutubeVideoPlayer(
    videoId: String,
    modifier: Modifier = Modifier,
    isVideoPlaysFlow: StateFlow<Boolean>,
    updateVideoIsPlayState: (Boolean) -> Unit,
    popBackStack: () -> Unit,
    updatePlaybackPosition: (Float) -> Unit,
    getPlaybackPosition: () -> Float,
    playerOrientationState: StateFlow<OrientationState>,
    updatePlayerOrientationState: (OrientationState) -> Unit,
    fullscreenWidgetIsClicked: StateFlow<Boolean>,
    setFullscreenWidgetIsClicked: (Boolean) -> Unit,
) {

    val isVideoPlays by isVideoPlaysFlow.collectAsStateWithLifecycle()
    val localPlayerOrientationState by playerOrientationState.collectAsStateWithLifecycle()
    val localContext = LocalContext.current
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current.lifecycle
    val binding =  FragmentPlayVideoBinding.inflate(LayoutInflater.from(localContext))

    val orientationHandler = remember {
        OrientationHandler(
            binding,
            localContext as Activity,
            localPlayerOrientationState,
            updatePlayerOrientationState,
            fullscreenWidgetIsClicked,
            setFullscreenWidgetIsClicked
        )
    }

    val youTubePlayerHandler = remember {
        YouTubePlayerHandler(
            binding,
            lifecycleOwner,
            videoId,
            isVideoPlays,
            updateVideoIsPlayState,
            updatePlaybackPosition,
            getPlaybackPosition,
            orientationHandler
        )
    }

    DisposableEffect(Unit) {
        val orientationEventListener = object : OrientationEventListener(localContext) {
            override fun onOrientationChanged(orientation: Int) {
                orientationHandler.setScreenAppearanceOrientationFlag(orientation)
            }
        }

        orientationEventListener.enable()
        onDispose { orientationEventListener.disable() }
    }

    BackHandler {
        if (playerOrientationState.value == OrientationState.FULL_SCREEN)
            orientationHandler.changeToPortraitOrientation()
        else popBackStack.invoke()
    }

    fun getModifier(localPlayerOrientationState: OrientationState): Modifier {
        return when(localPlayerOrientationState) {
            OrientationState.PORTRAIT -> modifier.fillMaxWidth()
            OrientationState.FULL_SCREEN -> modifier.fillMaxSize()
        }
    }

    AndroidView(
        modifier = getModifier(localPlayerOrientationState)
            .testTag(VIDEO_PLAYER),
        factory = { context ->
            binding.llPlayerContainer
        }
    )
}
