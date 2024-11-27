package com.appelier.bluetubecompose.core.core_ui.views.shorts_screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.appelier.bluetubecompose.R
import com.appelier.bluetubecompose.core.core_api.network_observer.ConnectivityStatus
import com.appelier.bluetubecompose.screen_shorts.screen.ShortsPlayerHandler
import com.appelier.bluetubecompose.screen_video_list.model.videos.YoutubeVideo
import com.appelier.bluetubecompose.utils.Core.CHANNEL_PREVIEW_IMG
import com.appelier.bluetubecompose.utils.ShortsItemTag.SHORTS_CHANNEL_TITLE
import com.appelier.bluetubecompose.utils.ShortsItemTag.SHORTS_VIDEO_PLAYER
import com.appelier.bluetubecompose.utils.ShortsItemTag.SHORTS_VIDEO_TITLE
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flowOf

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ShortsItem(
    youTubeVideo: YoutubeVideo,
    videoQueue: MutableSharedFlow<YouTubePlayer?> = MutableSharedFlow(),
    modifier: Modifier = Modifier,
    connectivityStatus: ConnectivityStatus
) {
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current.lifecycle
    val shortsPlayerHandler = remember { ShortsPlayerHandler(lifecycleOwner, youTubeVideo.id, videoQueue) }

//    val networkConnectivityStatus by connectivityStatus.collectAsStateWithLifecycle(
//        initialValue = ConnectivityStatus.Available
//    )
    val shortsDescriptionTextColor = if(connectivityStatus is ConnectivityStatus.Available)
        MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.secondary

    ConstraintLayout(
        modifier = modifier
            .fillMaxSize()
    ) {
        val (videoPlayer, channelImg, channelTitle, videoTitle) = createRefs()

        when(connectivityStatus) {
            ConnectivityStatus.Available -> {
                AndroidView(
                    factory = { context ->
                        val youTubePlayerView = YouTubePlayerView(context)
                        shortsPlayerHandler.setupPlayer(youTubePlayerView)
                        youTubePlayerView
                    },
                    modifier = modifier
                        .fillMaxSize()
                        .testTag(SHORTS_VIDEO_PLAYER)
                        .constrainAs(videoPlayer) {
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                        }
                )
            }

            ConnectivityStatus.Lost -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    GlideImage(
                        model = placeholder(R.drawable.sceleton_android_ompose_thumbnail),
                        loading = placeholder(R.drawable.sceleton_android_ompose_thumbnail),
                        contentDescription = stringResource(id = R.string.video_thumbnail_description),
                        modifier = Modifier.fillMaxWidth(),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }

        GlideImage(
            model = youTubeVideo.snippet.channelImgUrl,
            contentDescription = stringResource(id = R.string.channel_name) + youTubeVideo.snippet.channelTitle,
            loading = placeholder(R.drawable.sceleton_android_ompose_thumbnail),
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
            color = shortsDescriptionTextColor,
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
            color = shortsDescriptionTextColor,
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

@Preview
@Composable
fun ShortsNetworkAvailableItemPreview() {
    ShortsItem(YoutubeVideo.DEFAULT_VIDEO, connectivityStatus = ConnectivityStatus.Available)
}

@Preview
@Composable
fun ShortsNetworkUnavailableItemPreview() {
    ShortsItem(YoutubeVideo.DEFAULT_VIDEO, connectivityStatus = ConnectivityStatus.Lost)
}
