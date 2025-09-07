package com.vlascitchii.presentation_shorts.screen_shorts.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import com.vlascitchii.presentation_common.R
import com.vlascitchii.presentation_common.model.videos.YoutubeVideoUiModel
import com.vlascitchii.presentation_common.network_observer.NetworkConnectivityStatus
import com.vlascitchii.presentation_common.ui.theme.BlueTubeComposeTheme
import com.vlascitchii.presentation_common.utils.Core.CHANNEL_PREVIEW_IMG
import com.vlascitchii.presentation_common.utils.ShortsItemTag.SHORTS_CHANNEL_TITLE
import com.vlascitchii.presentation_common.utils.ShortsItemTag.SHORTS_VIDEO_PLAYER
import com.vlascitchii.presentation_common.utils.ShortsItemTag.SHORTS_VIDEO_TITLE
import com.vlascitchii.presentation_shorts.screen_shorts.screen.ShortsPlayerHandler
import kotlinx.coroutines.flow.MutableSharedFlow

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ShortsItem(
    youTubeVideo: YoutubeVideoUiModel,
    videoQueue: MutableSharedFlow<YouTubePlayer?> = MutableSharedFlow(),
    modifier: Modifier = Modifier,
    networkConnectivityStatus: NetworkConnectivityStatus
) {
    val lifecycleOwner = LocalLifecycleOwner.current.lifecycle
    val shortsPlayerHandler = remember { ShortsPlayerHandler(lifecycleOwner, youTubeVideo.id, videoQueue) }

    ConstraintLayout(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        val (videoPlayer, channelImg, channelTitle, videoTitle) = createRefs()

        when(networkConnectivityStatus) {
            NetworkConnectivityStatus.Available -> {
//                AndroidView(
//                    factory = { context ->
//                        val youTubePlayerView = YouTubePlayerView(context)
//                        shortsPlayerHandler.setupPlayer(youTubePlayerView)
//                        youTubePlayerView
//                    },
//                    modifier = modifier
//                        .fillMaxSize()
//                        .testTag(SHORTS_VIDEO_PLAYER)
//                        .constrainAs(videoPlayer) {
//                            start.linkTo(parent.start)
//                            end.linkTo(parent.end)
//                            top.linkTo(parent.top)
//                            bottom.linkTo(parent.bottom)
//                        }
//                )
            }

            NetworkConnectivityStatus.Lost -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    GlideImage(
                        model = placeholder(R.drawable.sceleton_thumbnail),
                        loading = placeholder(R.drawable.sceleton_thumbnail),
                        contentDescription = stringResource(id = R.string.video_thumbnail_description),
                        modifier = Modifier.fillMaxWidth(),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }

        GlideImage(
            model = youTubeVideo.snippet.channelImgUrl,
            contentDescription = stringResource(id = R.string.channel_description) + youTubeVideo.snippet.channelTitle,
            loading = placeholder(R.drawable.sceleton_thumbnail),
            modifier = modifier
                .testTag(CHANNEL_PREVIEW_IMG)
                .padding(8.dp)
                .width(50.dp)
                .height(50.dp)
                .clip(MaterialTheme.shapes.medium)
                .constrainAs(channelImg) {
                    bottom.linkTo(videoTitle.top)
                }
        )

        Text(
            text = youTubeVideo.snippet.channelTitle,
            style = MaterialTheme.typography.bodySmall,
            color = Color.White,
            maxLines = 1,
            textAlign = TextAlign.Start,
            modifier = modifier
                .testTag(SHORTS_VIDEO_TITLE)
                .padding(start = 8.dp)
                .constrainAs(channelTitle) {
                    start.linkTo(channelImg.end)
                    centerVerticallyTo(channelImg)
                }
        )

        Text(
            text = youTubeVideo.snippet.title,
            style = MaterialTheme.typography.bodySmall,
            color = Color.White,
            maxLines = 2,
            textAlign = TextAlign.Start,
            modifier = modifier
                .testTag(SHORTS_CHANNEL_TITLE)
                .padding(start = 8.dp, bottom = 70.dp)
                .constrainAs(videoTitle) {
                    start.linkTo(parent.start)
                    bottom.linkTo(parent.bottom)
                }
        )
    }
}

@PreviewLightDark
@Composable
fun ShortsNetworkAvailableItemPreview() {
    BlueTubeComposeTheme {
        Surface {
            ShortsItem(YoutubeVideoUiModel.DEFAULT_VIDEO, networkConnectivityStatus = NetworkConnectivityStatus.Available)
        }
    }
}

@PreviewLightDark
@Composable
fun ShortsNetworkUnavailableItemPreview() {
    BlueTubeComposeTheme {
        Surface {
            ShortsItem(YoutubeVideoUiModel.DEFAULT_VIDEO, networkConnectivityStatus = NetworkConnectivityStatus.Lost)
        }
    }
}
