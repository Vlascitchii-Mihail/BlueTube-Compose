package com.vlascitchii.presentation_player.screen_player

import android.app.Activity
import android.view.LayoutInflater
import android.view.OrientationEventListener
import androidx.activity.compose.BackHandler
//import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.vlascitchii.presentation_common.utils.VideoPlayerScreenTags.VIDEO_PLAYER
import com.vlascitchii.presentation_player.R
import kotlinx.coroutines.flow.StateFlow

@OptIn(ExperimentalGlideComposeApi::class, ExperimentalGlideComposeApi::class)
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
    connectivityStatus: com.appelier.bluetubecompose.network_observer.ConnectivityStatus,
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

    remember {
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
            orientationHandler.clickToFullScreenWidget()
        else {
            orientationHandler.setSensorOrientation()
            popBackStack.invoke()
        }
    }

    fun getModifier(localPlayerOrientationState: OrientationState): Modifier {
        return when(localPlayerOrientationState) {
            OrientationState.PORTRAIT -> modifier.fillMaxWidth()
            OrientationState.FULL_SCREEN -> modifier.fillMaxSize()
        }
    }

    when(connectivityStatus) {
        com.appelier.bluetubecompose.network_observer.ConnectivityStatus.Available -> {
            AndroidView(
                modifier = getModifier(localPlayerOrientationState)
                    .background(MaterialTheme.colorScheme.background)
                    .testTag(VIDEO_PLAYER),
                factory = { context ->
                    binding.llPlayerContainer
                }
            )
        }

        com.appelier.bluetubecompose.network_observer.ConnectivityStatus.Lost -> {
            GlideImage(
                model = placeholder(R.drawable.sceleton_thumbnail),
                loading = placeholder(R.drawable.sceleton_thumbnail),
                contentDescription = stringResource(id = R.string.video_thumbnail_description),
                modifier = getModifier(localPlayerOrientationState),
                contentScale = ContentScale.Crop
            )
        }
    }
}
