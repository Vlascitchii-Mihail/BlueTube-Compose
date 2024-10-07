package com.appelier.bluetubecompose.core.core_ui.views

import android.app.Activity
import android.view.LayoutInflater
import android.view.OrientationEventListener
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.appelier.bluetubecompose.databinding.FragmentPlayVideoBinding
import com.appelier.bluetubecompose.screen_player.OrientationState
import com.appelier.bluetubecompose.screen_player.YouTubePlayerHandler
import com.appelier.bluetubecompose.utils.VideoPlayerScreenTags.VIDEO_PLAYER
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun YoutubeVideoPlayer(
    videoId: String,
    modifier: Modifier = Modifier,
    isVideoPlaysFlow: MutableStateFlow<Boolean>,
    popBackStack: () -> Unit,
    updatePlaybackPosition: (Float) -> Unit,
    getPlaybackPosition: () -> Float,
) {
    val isVideoPlays = isVideoPlaysFlow.collectAsStateWithLifecycle() as MutableState
    val playerOrientationState = remember { mutableStateOf(OrientationState.PORTRAIT) }
    val localContext = LocalContext.current
    val binding = remember { FragmentPlayVideoBinding.inflate(LayoutInflater.from(localContext)) }
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current.lifecycle
    val youTubePlayerHandler = remember {
        YouTubePlayerHandler(
            binding,
            playerOrientationState,
            localContext as Activity,
            lifecycleOwner,
            videoId,
            isVideoPlays,
            updatePlaybackPosition,
            getPlaybackPosition
        )
    }

    DisposableEffect(Unit) {
        val orientationEventListener = object : OrientationEventListener(localContext) {
            override fun onOrientationChanged(orientation: Int) {
                youTubePlayerHandler.setScreenAppearanceOrientationFlag(orientation)
            }
        }

        orientationEventListener.enable()
        onDispose { orientationEventListener.disable() }
    }

    BackHandler {
        if (playerOrientationState.value == OrientationState.FULL_SCREEN)
            youTubePlayerHandler.changeToPortraitOrientation()

        else popBackStack.invoke()
    }

    fun getModifier(playerOrientationState: MutableState<OrientationState>): Modifier {
        return when(playerOrientationState.value) {
            OrientationState.PORTRAIT -> modifier.fillMaxWidth()
            OrientationState.FULL_SCREEN -> modifier.fillMaxSize()
        }
    }

    AndroidView(
        modifier = getModifier(playerOrientationState)
            .testTag(VIDEO_PLAYER),
        factory = { context ->
            binding.llPlayerContainer
        }
    )
}
