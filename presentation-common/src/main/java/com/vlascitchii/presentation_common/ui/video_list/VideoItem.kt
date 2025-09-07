package com.vlascitchii.presentation_common.ui.video_list

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.vlascitchii.presentation_common.R
import com.vlascitchii.presentation_common.entity.videos.YoutubeVideoUiModel
import com.vlascitchii.presentation_common.entity.videos.YoutubeVideoUiModel.Companion.DEFAULT_VIDEO
import com.vlascitchii.presentation_common.ui.theme.BlueTubeComposeTheme
import com.vlascitchii.presentation_common.utils.VideoListScreenTags.VIDEO_DURATION

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun VideoItem(
    youtubeVideoUiModel: YoutubeVideoUiModel,
    modifier: Modifier,
    navigateToPlayerScreen: (YoutubeVideoUiModel) -> Unit
) {
    ConstraintLayout(modifier = modifier
        .fillMaxWidth()
        .clickable (
            onClickLabel = stringResource(R.string.video_compact_preview_description),
            onClick = { navigateToPlayerScreen.invoke(youtubeVideoUiModel) }
        )
        .wrapContentHeight()
    ) {

        val (videoPreview, videoTitle, channelImg, videoDuration, statisticsFlow) = createRefs()

        GlideImage(
            model = youtubeVideoUiModel.snippet.thumbnailsUiModel.medium.url,
            loading = placeholder(R.drawable.sceleton_thumbnail),
            contentDescription = stringResource(id = R.string.video_preview_description),
            contentScale = ContentScale.Crop,
            modifier = modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .clickable { navigateToPlayerScreen.invoke(youtubeVideoUiModel) }
                .constrainAs(videoPreview) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                }
        )

        Text(
            text = com.vlascitchii.presentation_common.utils.formatVideoDuration(youtubeVideoUiModel.contentDetails.duration),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = modifier
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
            model = youtubeVideoUiModel.snippet.channelImgUrl,
            loading = placeholder(R.drawable.sceleton),
            contentDescription = stringResource(R.string.channel_description),
            contentScale = ContentScale.Crop,
            modifier = modifier
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
            text = youtubeVideoUiModel.snippet.title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 2,
            textAlign = TextAlign.Start,
            modifier = modifier
                .clickable(
                    onClickLabel = stringResource(R.string.video_item_title),
                    onClick = { navigateToPlayerScreen.invoke(youtubeVideoUiModel) }
                )
                .padding(top = 8.dp, end = 8.dp)
                .constrainAs(videoTitle) {
                    start.linkTo(channelImg.end)
                    top.linkTo(videoPreview.bottom)
                    end.linkTo(parent.end)
                    bottom.linkTo(statisticsFlow.top)
                    width = Dimension.fillToConstraints
                }
        )

        val channelTitle = youtubeVideoUiModel.snippet.channelTitle
        val views = "${youtubeVideoUiModel.statistics.viewCount.let {
            com.vlascitchii.presentation_common.utils.formatViews(
                it
            )
        }} views"
        val publishedAgo =
            com.vlascitchii.presentation_common.utils.formatDate(youtubeVideoUiModel.snippet.publishedAt)

        Text(
            text = "$channelTitle  $views  $publishedAgo",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 2,
            textAlign = TextAlign.Start,
            modifier = modifier
                .clickable (
                    onClickLabel = stringResource(R.string.video_statistics),
                    onClick = { navigateToPlayerScreen.invoke(youtubeVideoUiModel) }
                )
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
        VideoItem(DEFAULT_VIDEO, Modifier, navigateToPlayerScreen = { YoutubeVideoUiModel })
    }
}