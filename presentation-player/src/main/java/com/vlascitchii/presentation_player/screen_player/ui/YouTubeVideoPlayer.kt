package com.vlascitchii.presentation_player.screen_player.ui

import android.app.Activity
import android.view.LayoutInflater
import android.view.OrientationEventListener
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.paging.PagingData
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.vlascitchii.presentation_common.model.videos.YoutubeVideoUiModel
import com.vlascitchii.presentation_common.network_observer.NetworkConnectivityStatus
import com.vlascitchii.presentation_common.ui.screen.CommonMVI
import com.vlascitchii.presentation_common.ui.state.UiState
import com.vlascitchii.presentation_player.databinding.FragmentPlayVideoBinding
import com.vlascitchii.presentation_player.screen_player.OrientationHandler
import com.vlascitchii.presentation_player.screen_player.OrientationState
import com.vlascitchii.presentation_player.screen_player.YouTubePlayerInitializer
import com.vlascitchii.presentation_player.screen_player.state.PlayerActionState
import com.vlascitchii.presentation_player.screen_player.state.PlayerNavigationEvent
import com.vlascitchii.presentation_player.screen_player.state.PlayerState
import kotlinx.coroutines.flow.Flow
import com.vlascitchii.presentation_common.R as CommonR
import com.vlascitchii.presentation_player.R as PlayerR

@Composable
fun YoutubeVideoPlayer(
    videoId: String,
    modifier: Modifier = Modifier,
    playerState: PlayerState,
    playerMVI: CommonMVI<YoutubeVideoUiModel, UiState<Flow<PagingData<YoutubeVideoUiModel>>>, PlayerActionState, PlayerNavigationEvent>,
    playbackPosition: Float,
) {

    val localContext = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current.lifecycle
    val binding = remember { FragmentPlayVideoBinding.inflate(LayoutInflater.from(localContext)) }
    val videoPlayerDescription = stringResource(PlayerR.string.video_player_description)

    val orientationHandler = remember {
        OrientationHandler(
            binding = binding,
            activity = localContext as Activity,
            playerMVI = playerMVI,
            playerState = playerState
        )
    }

    remember {
        YouTubePlayerInitializer(
            binding = binding,
            localContext = localContext,
            currentComposeLifecycle = lifecycleOwner,
            videoId = videoId,
            playerMVI = playerMVI,
            isVideoPlays = playerState.isVideoPlaying,
            currentPlaybackPosition = playbackPosition,
            orientationHandler = orientationHandler
        )
    }

    DisposableEffect(Unit) {
        val orientationEventListener = object : OrientationEventListener(localContext) {
            override fun onOrientationChanged(orientation: Int) {
                orientationHandler.setScreenOrientationFromFlag(orientation)
            }
        }

        orientationEventListener.enable()
        onDispose { orientationEventListener.disable() }
    }

    BackHandler {
        if (playerState.playerOrientationState == OrientationState.FULL_SCREEN)
            binding.fullScreenButton.performClick()
        else {
            orientationHandler.setSensorOrientation()
            playerMVI.submitSingleNavigationEvent(PlayerNavigationEvent.PopBackStackEvent)
        }
    }

    fun getModifier(localPlayerOrientationState: OrientationState): Modifier {
        return when (localPlayerOrientationState) {
            OrientationState.PORTRAIT -> modifier.fillMaxWidth()
            OrientationState.FULL_SCREEN -> modifier.fillMaxSize()
        }
    }

    when (playerState.networkConnectivityStatus) {
        NetworkConnectivityStatus.Available -> {
            AndroidView(
                modifier = getModifier(playerState.playerOrientationState)
                    .background(MaterialTheme.colorScheme.background)
                    .semantics { contentDescription = videoPlayerDescription },
                factory = { context ->
                    binding.llPlayerContainer
                }
            )
        }

        NetworkConnectivityStatus.Lost -> {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(CommonR.string.video_thumbnail_description)
                    .crossfade(true)
                    .placeholder(CommonR.drawable.sceleton_thumbnail)
                    .error(CommonR.drawable.sceleton_thumbnail)
                    .build(),
                contentDescription = stringResource(id = CommonR.string.video_thumbnail_description),
                contentScale = ContentScale.Crop,
                modifier = getModifier(playerState.playerOrientationState)
            )
        }
    }
}
