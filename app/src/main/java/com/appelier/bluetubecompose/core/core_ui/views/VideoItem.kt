package com.appelier.bluetubecompose.core.core_ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.appelier.bluetubecompose.R
import com.appelier.bluetubecompose.core.core_ui.theme.BlueTubeComposeTheme
import com.appelier.bluetubecompose.screen_video_list.model.videos.YoutubeVideo
import com.appelier.bluetubecompose.screen_video_list.model.videos.YoutubeVideo.Companion.DEFAULT_VIDEO
import com.appelier.bluetubecompose.utils.VideoListScreenTags.CHANNEL_PREVIEW_IMG
import com.appelier.bluetubecompose.utils.VideoListScreenTags.VIDEO_DURATION
import com.appelier.bluetubecompose.utils.VideoListScreenTags.VIDEO_PREVIEW_IMG
import com.appelier.bluetubecompose.utils.VideoListScreenTags.VIDEO_STATISTICS
import com.appelier.bluetubecompose.utils.VideoListScreenTags.VIDEO_TITLE
import com.appelier.bluetubecompose.utils.formatDate
import com.appelier.bluetubecompose.utils.formatVideoDuration
import com.appelier.bluetubecompose.utils.formatViews
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun VideoItem(
    youtubeVideo: YoutubeVideo,
    defaultModifier: Modifier,
    navigateToPlayerScreen: (YoutubeVideo) -> Unit
) {
    ConstraintLayout(modifier = defaultModifier
        .fillMaxWidth()
        .clickable { navigateToPlayerScreen.invoke(youtubeVideo) }
        .wrapContentHeight()) {

        val (videoPreview, videoTitle, channelImg, videoDuration, statisticsFlow) = createRefs()

        GlideImage(
            model = youtubeVideo.snippet.thumbnails.medium.url,
            loading = placeholder(R.drawable.sceleton_android_ompose_thumbnail),
            contentDescription = stringResource(id = R.string.video_preview) + youtubeVideo.snippet.title,
            contentScale = ContentScale.Crop,
            modifier = defaultModifier
                .fillMaxWidth()
                .wrapContentSize()
                .clickable { navigateToPlayerScreen.invoke(youtubeVideo) }
                .testTag(VIDEO_PREVIEW_IMG)
                .constrainAs(videoPreview) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                }
        )

        Text(
            text = formatVideoDuration(youtubeVideo.contentDetails.duration),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.tertiary,
            modifier = defaultModifier
                .semantics { contentDescription = VIDEO_DURATION }
                .padding(8.dp)
                .clip(MaterialTheme.shapes.small)
                .background(MaterialTheme.colorScheme.secondary)
                .padding(4.dp)
                .constrainAs(videoDuration) {
                    end.linkTo(videoPreview.end)
                    bottom.linkTo(videoPreview.bottom)
                }
        )

        GlideImage(
            model = youtubeVideo.snippet.channelImgUrl,
            loading = placeholder(R.drawable.sceleton),
            contentDescription = stringResource(R.string.channel_name) + youtubeVideo.snippet.channelTitle,
            contentScale = ContentScale.Crop,
            modifier = defaultModifier
                .testTag(CHANNEL_PREVIEW_IMG)
                .padding(8.dp)
                .width(50.dp)
                .height(50.dp)
                .clip(MaterialTheme.shapes.medium)
                .constrainAs(channelImg) {
                    top.linkTo(videoPreview.bottom)
                    start.linkTo(parent.start)
                    bottom.linkTo(statisticsFlow.bottom)
                }
        )

        Text(
            text = youtubeVideo.snippet.title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 2,
            textAlign = TextAlign.Start,
            modifier = defaultModifier
                .testTag(VIDEO_TITLE)
                .clickable { navigateToPlayerScreen.invoke(youtubeVideo) }
                .padding(top = 8.dp, end = 8.dp)
                .constrainAs(videoTitle) {
                    start.linkTo(channelImg.end)
                    top.linkTo(videoPreview.bottom)
                    end.linkTo(parent.end)
                    bottom.linkTo(statisticsFlow.top)
                    width = Dimension.fillToConstraints
                }
        )

        val channelTitle = youtubeVideo.snippet.channelTitle
        val views = "${youtubeVideo.statistics.viewCount.let { formatViews(it) }} views"
        val publishedAgo = formatDate(youtubeVideo.snippet.publishedAt)

        Text(
            text = "$channelTitle  $views  $publishedAgo",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 2,
            textAlign = TextAlign.Start,
            modifier = defaultModifier
                .testTag(VIDEO_STATISTICS)
                .clickable { navigateToPlayerScreen.invoke(youtubeVideo) }
                .padding(end = 8.dp, top = 8.dp, bottom = 8.dp)
                .constrainAs(statisticsFlow) {
                    start.linkTo(channelImg.end)
                    end.linkTo(parent.end)
                    top.linkTo(videoTitle.bottom)
                    bottom.linkTo(parent.bottom)
                    width = Dimension.fillToConstraints
                    horizontalBias = 0f
                }
        )
    }
}

@Preview
@Composable
private fun ItemPreview() {
    BlueTubeComposeTheme {
        VideoItem(DEFAULT_VIDEO, Modifier, navigateToPlayerScreen = { YoutubeVideo })
    }
}