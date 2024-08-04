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
import androidx.compose.ui.zIndex
import com.appelier.bluetubecompose.databinding.FragmentPlayVideoBinding
import com.appelier.bluetubecompose.screen_player.OrientationState
import com.appelier.bluetubecompose.screen_player.YouTubePlayerHandler
import com.appelier.bluetubecompose.utils.VideoPlayerScreenTags.VIDEO_PLAYER
import com.appelier.bluetubecompose.utils.getActivity

@Composable
fun YoutubeVideoPlayer(
    videoId: String,
    modifier: Modifier = Modifier,
    popBackStack: () -> Unit
) {
    val playerOrientationState = remember { mutableStateOf(OrientationState.PORTRAIT) }
    val localContext = LocalContext.current
    val binding = remember { FragmentPlayVideoBinding.inflate(LayoutInflater.from(localContext)) }
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current.lifecycle
    val youTubePlayerHandler = remember {
        YouTubePlayerHandler(
            binding,
            playerOrientationState,
            localContext.getActivity() ?: localContext as Activity,
            lifecycleOwner,
            videoId
        )
    }

    fun setupOrientationChange(orientation: Int) {
        if (playerOrientationState.value == OrientationState.PORTRAIT && OrientationState.PORTRAIT.ordinal != orientation) {
            youTubePlayerHandler.togglePlayerOrientation()
        }
    }

    DisposableEffect(Unit) {
        val orientationEventListener = object : OrientationEventListener(localContext) {
            override fun onOrientationChanged(orientation: Int) {
                setupOrientationChange(orientation)
            }
        }

        orientationEventListener.enable()
        onDispose { orientationEventListener.disable() }
    }

    BackHandler {
        if (playerOrientationState.value == OrientationState.FULL_SCREEN)
            youTubePlayerHandler.togglePlayerOrientation()

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
            .testTag(VIDEO_PLAYER)
            .zIndex(10F),
        factory = { context ->

            binding.llPlayerContainer
        },
        update = { llPlayerContainer ->
            when (playerOrientationState.value) {
                OrientationState.PORTRAIT -> {
                    llPlayerContainer.layoutParams.height =
                        youTubePlayerHandler.getPlayerContainerHeight()
                }

                OrientationState.FULL_SCREEN -> {
                    youTubePlayerHandler.setFullScreenVideoFitScreenHeight()
                }
            }
        }
    )
}
