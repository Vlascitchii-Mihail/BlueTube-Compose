package com.vlascitchii.presentation_common.ui.video_list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.vlascitchii.common_ui.R
import com.vlascitchii.presentation_common.model.videos.YoutubeVideoUiModel
import com.vlascitchii.presentation_common.model.videos.YoutubeVideoUiModel.Companion.DEFAULT_VIDEO
import com.vlascitchii.presentation_common.ui.theme.BlueTubeComposeTheme
import com.vlascitchii.presentation_common.ui.theme.statisticsBodySmall
import com.vlascitchii.presentation_common.utils.VideoListScreenTags.VIDEO_DURATION
import com.vlascitchii.presentation_common.utils.formatDate
import com.vlascitchii.presentation_common.utils.formatVideoDuration
import com.vlascitchii.presentation_common.utils.formatViews

@Composable
fun VideoItem(
    youtubeVideoUiModel: YoutubeVideoUiModel,
    modifier: Modifier,
    navigateToPlayerScreen: (YoutubeVideoUiModel) -> Unit
    ) {
    val videoItemDescription = stringResource(R.string.video_compact_preview_description)
    ConstraintLayout(modifier = modifier
        .fillMaxWidth()
        .semantics {contentDescription = videoItemDescription }
        .clickable(
            onClick = {
                navigateToPlayerScreen.invoke(youtubeVideoUiModel)
            }
        )
        .wrapContentHeight()
    ) {

        val (videoPreview, videoTitle, channelImg, videoDuration, statisticsFlow) = createRefs()
        val videoItemTitleDescription = stringResource(R.string.video_item_title)
        val videoStatisticsDescription = stringResource(R.string.video_statistics)

        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(youtubeVideoUiModel.snippet.thumbnailsUiModel.medium.url)
                .crossfade(true)
                .placeholder(R.drawable.sceleton_thumbnail)
                .error(R.drawable.sceleton_thumbnail)
                .build(),
            contentDescription = stringResource(id = R.string.video_preview_description),
            contentScale = ContentScale.FillWidth,
            modifier = modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .constrainAs(videoPreview) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                }
        )

        Text(
            text = formatVideoDuration(youtubeVideoUiModel.contentDetails.duration),
            style = MaterialTheme.typography.bodyLarge,
            color = Color.White,
            modifier = modifier
                .semantics { contentDescription = VIDEO_DURATION }
                .padding(dimensionResource(R.dimen.padding_small_8))
                .clip(MaterialTheme.shapes.small)
                .background(MaterialTheme.colorScheme.outline)
                .padding(dimensionResource(R.dimen.padding_small_extra_4))
                .constrainAs(videoDuration) {
                    end.linkTo(videoPreview.end)
                    bottom.linkTo(videoPreview.bottom)
                }
        )

        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(youtubeVideoUiModel.snippet.channelImgUrl)
                .crossfade(true)
                .placeholder(R.drawable.sceleton)
                .error(R.drawable.sceleton)
                .build(),
            contentDescription = stringResource(R.string.channel_description),
            contentScale = ContentScale.Crop,
            modifier = modifier
                .padding(dimensionResource(R.dimen.padding_small_8))
                .width(dimensionResource(R.dimen.width_medium_extra_50))
                .height(dimensionResource(R.dimen.height_medium_extra_50))
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
            maxLines = 2,
            textAlign = TextAlign.Start,
            modifier = modifier
                .semantics { contentDescription = videoItemTitleDescription }
                .padding(
                    top = dimensionResource(R.dimen.padding_small_8),
                    end = dimensionResource(R.dimen.padding_small_8)
                )
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
            formatViews(it)
        }} views"

        val publishedAgo = formatDate(youtubeVideoUiModel.snippet.publishedAt)

        Text(
            text = "$channelTitle  $views  $publishedAgo",
            style = MaterialTheme.typography.statisticsBodySmall,
            maxLines = 2,
            modifier = modifier
                .semantics { contentDescription = videoStatisticsDescription }
                .padding(
                    dimensionResource(R.dimen.padding_small_8),
                    top = dimensionResource(R.dimen.padding_small_8),
                    bottom = dimensionResource(R.dimen.padding_small_8)
                )
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

@PreviewLightDark()
@Composable
private fun ItemPreview() {
    BlueTubeComposeTheme {
        Surface {
            VideoItem(youtubeVideoUiModel = DEFAULT_VIDEO, modifier = Modifier, navigateToPlayerScreen = {}
            )
        }
    }
}
