package com.appelier.bluetubecompose.core.core_ui.views

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.OrientationEventListener
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
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
import androidx.lifecycle.LifecycleOwner
import com.appelier.bluetubecompose.databinding.FragmentPlayVideoBinding
import com.appelier.bluetubecompose.screen_player.OrientationState
import com.appelier.bluetubecompose.screen_player.YouTubePlayerHandler
import com.appelier.bluetubecompose.utils.VideoPlayerScreenTags.VIDEO_PLAYER
import com.appelier.bluetubecompose.utils.getActivity

@Composable
fun YoutubeVideoPlayer(
    videoId: String,
    lifecycleOwner: LifecycleOwner,
    modifier: Modifier = Modifier,
    popBackStack: () -> Unit,
    getUpdatedPlaybackPosition: (Float) -> Float
) {
    val playerOrientationState = remember { mutableStateOf(OrientationState.PORTRAIT) }
    val localContext = LocalContext.current
    val activity = localContext.getActivity() ?: localContext as Activity
    val binding = remember { FragmentPlayVideoBinding.inflate(LayoutInflater.from(localContext)) }
    val youTubePlayerHandler = remember {
        YouTubePlayerHandler(
            binding,
            playerOrientationState,
            activity = activity,
            currentComposeLifecycle = lifecycleOwner.lifecycle,
            videoId,
            getUpdatedPlaybackPosition
        )
    }
    Log.d(
        "Player test", "Youtube Player recreated"
    )

    youTubePlayerHandler.setScreenAppearanceFlag()

//    fun setupOrientationChange(orientation: Int) {
//        if (playerOrientationState.value == OrientationState.PORTRAIT && OrientationState.PORTRAIT.ordinal != orientation) {
//            youTubePlayerHandler.youTubePlayer?.toggleFullscreen()
//        }
//    }


//    DisposableEffect(Unit) {
//        val orientationEventListener = object : OrientationEventListener(localContext) {
//            override fun onOrientationChanged(orientation: Int) {
////                setupOrientationChange(orientation)
//                youTubePlayerHandler.setScreenAppearanceFlag()
//            }
//        }
//
//        orientationEventListener.enable()
//        onDispose { orientationEventListener.disable() }
//    }

    BackHandler {
        if (playerOrientationState.value == OrientationState.FULL_SCREEN){
            //            youTubePlayerHandler.blueTubeYouTubePlayer?.toggleFullscreen()
//            playerOrientationState.value = OrientationState.PORTRAIT
            youTubePlayerHandler.changeToPortraitOrientation(activity)
            Log.d("Player test", "BackHandler in FULL_SCREEN")

        }
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
        },
//        update = { llPlayerContainer ->
//            when(playerOrientationState.value) {
//                OrientationState.PORTRAIT -> {
////                    val playerHeight = youTubePlayerHandler.playerHeight
////                    llPlayerContainer.layoutParams.height =
////                        if (playerHeight > 0) playerHeight
////                        else WRAP_CONTENT
//                }
//                OrientationState.FULL_SCREEN ->{
////                    youTubePlayerHandler.playerFullScreenView.layoutParams.height =
////                        binding.ytPlayer.width
//                }
//            }
//        }
    )
}
