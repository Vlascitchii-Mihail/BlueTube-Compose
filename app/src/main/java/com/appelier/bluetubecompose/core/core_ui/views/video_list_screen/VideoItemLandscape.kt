package com.appelier.bluetubecompose.core.core_ui.views.video_list_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
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
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.appelier.bluetubecompose.R
import com.appelier.bluetubecompose.core.core_ui.theme.BlueTubeComposeTheme
import com.appelier.bluetubecompose.screen_video_list.model.videos.YoutubeVideo
import com.appelier.bluetubecompose.screen_video_list.model.videos.YoutubeVideo.Companion.DEFAULT_VIDEO
import com.appelier.bluetubecompose.utils.Core.CHANNEL_PREVIEW_IMG
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
fun VideoItemLandscape(
    youtubeVideo: YoutubeVideo,
    modifier: Modifier,
    navigateToPlayerScreen: (YoutubeVideo) -> Unit
) {
    ConstraintLayout(modifier = modifier
        .padding(start = 4.dp)
        .fillMaxWidth()
        .height(100.dp)
        .clickable { navigateToPlayerScreen.invoke(youtubeVideo) }
    ) {
        val (videoPreview, videoTitle, channelImg, videoDuration, statisticsFlow) = createRefs()

        GlideImage(
            model = youtubeVideo.snippet.thumbnails.medium.url,
            loading = placeholder(R.drawable.sceleton_android_ompose_thumbnail),
            contentDescription = stringResource(id = R.string.video_preview) + youtubeVideo.snippet.title,
            contentScale = ContentScale.FillHeight,
            modifier = modifier
                .wrapContentHeight()
                .padding(top = 4.dp, bottom = 4.dp)
                .clickable { navigateToPlayerScreen.invoke(youtubeVideo) }
                .clip(MaterialTheme.shapes.extraSmall)
                .testTag(VIDEO_PREVIEW_IMG)
                .constrainAs(videoPreview) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    height = Dimension.fillToConstraints
                }
        )

        Text(
            text = formatVideoDuration(youtubeVideo.contentDetails.duration),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.tertiary,
            modifier = modifier
                .semantics { contentDescription = VIDEO_DURATION }
                .padding(5.dp)
                .clip(MaterialTheme.shapes.small)
                .background(MaterialTheme.colorScheme.secondary)
                .padding(2.dp)
                .constrainAs(videoDuration) {
                    bottom.linkTo(videoPreview.bottom)
                    end.linkTo(videoPreview.end)
                }
        )

        GlideImage(
            model = youtubeVideo.snippet.channelImgUrl,
            loading = placeholder(R.drawable.sceleton),
            contentDescription = stringResource(R.string.channel_name) + youtubeVideo.snippet.channelTitle,
            contentScale = ContentScale.Crop,
            modifier = modifier
                .testTag(CHANNEL_PREVIEW_IMG)
                .padding(8.dp)
                .width(35.dp)
                .height(35.dp)
                .clip(MaterialTheme.shapes.medium)
                .constrainAs(channelImg) {
                    start.linkTo(videoPreview.end)
                    bottom.linkTo(videoPreview.bottom)
                }
        )

        Text(
            text = youtubeVideo.snippet.title,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 2,
            textAlign = TextAlign.Start,
            modifier = modifier
                .testTag(VIDEO_TITLE)
                .clickable { navigateToPlayerScreen.invoke(youtubeVideo) }
                .padding(8.dp)
                .constrainAs(videoTitle) {
                    start.linkTo(videoPreview.end)
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                    bottom.linkTo(videoPreview.baseline)
                    width = Dimension.fillToConstraints
                }
        )

        val channelTitle = youtubeVideo.snippet.channelTitle
        val views = "${youtubeVideo.statistics.viewCount.let { formatViews(it) }} views"
        val publishedAgo = formatDate(youtubeVideo.snippet.publishedAt)

        Text(
            text = "$channelTitle  $views  $publishedAgo",
            style = MaterialTheme.typography.bodySmall,
            fontSize = 10.sp,
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 2,
            textAlign = TextAlign.Start,
            modifier = modifier
                .testTag(VIDEO_STATISTICS)
                .clickable { navigateToPlayerScreen.invoke(youtubeVideo) }
                .padding(end = 8.dp, top = 8.dp, bottom = 8.dp)
                .constrainAs(statisticsFlow) {
                    start.linkTo(channelImg.end)
                    top.linkTo(channelImg.top)
                    bottom.linkTo(channelImg.bottom)
                    width = Dimension.fillToConstraints
                    horizontalBias = 0f
                }
        )
    }
}

@Preview
@Composable
private fun ItemLandscapePreview() {
    BlueTubeComposeTheme {
        VideoItemLandscape(DEFAULT_VIDEO, Modifier, navigateToPlayerScreen = { YoutubeVideo })
    }
}