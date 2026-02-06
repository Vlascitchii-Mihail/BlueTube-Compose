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
import androidx.compose.runtime.CompositionLocalProvider
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.vlascitchii.common_ui.R
import com.vlascitchii.presentation_common.model.videos.YoutubeVideoUiModel
import com.vlascitchii.presentation_common.model.videos.YoutubeVideoUiModel.Companion.DEFAULT_VIDEO
import com.vlascitchii.presentation_common.ui.screen.LocalWindowSizeClass
import com.vlascitchii.presentation_common.ui.screen.previewWindowSizeClass
import com.vlascitchii.presentation_common.ui.theme.BlueTubeComposeTheme
import com.vlascitchii.presentation_common.ui.theme.statisticsBodySmall
import com.vlascitchii.presentation_common.utils.VideoListScreenTags.VIDEO_DURATION
import com.vlascitchii.presentation_common.utils.formatDate
import com.vlascitchii.presentation_common.utils.formatVideoDuration
import com.vlascitchii.presentation_common.utils.formatViews

@Composable
fun VideoItemLandscape(
    youtubeVideoUiModel: YoutubeVideoUiModel,
    modifier: Modifier,
    navigateToPlayerScreen: (YoutubeVideoUiModel) -> Unit
    ) {
    val itemLandscapeDescription = stringResource(R.string.video_medium_preview_description)
    ConstraintLayout(modifier = modifier
        .padding(start = dimensionResource(R.dimen.padding_small_extra_4))
        .fillMaxWidth()
        .wrapContentHeight()
        .semantics { contentDescription = itemLandscapeDescription }
        .clickable( onClick = {
            navigateToPlayerScreen.invoke(youtubeVideoUiModel)
        })
    ) {
        val (videoPreview, videoTitle, channelImg, videoDuration, statisticsFlow) = createRefs()
        val videoItemTitleDescription = stringResource(R.string.video_item_title)
        val videoStatisticsDescription = stringResource(R.string.video_statistics)

        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(youtubeVideoUiModel.snippet.thumbnailsUiModel.medium.url)
                .crossfade(true)
                .placeholder(R.drawable.sceleton_thumbnail)
                .error(R.drawable.sceleton)
                .build(),
            contentDescription = stringResource(id = R.string.video_preview_description),
            contentScale = ContentScale.FillWidth,
            modifier = modifier
                .padding(
                    top = dimensionResource(R.dimen.padding_small_extra_4),
                    bottom = dimensionResource(R.dimen.padding_small_extra_4)
                )
                .clip(MaterialTheme.shapes.extraSmall)
                .constrainAs(videoPreview) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    width = Dimension.percent(0.35F)
                }
        )

        Text(
            text = formatVideoDuration(youtubeVideoUiModel.contentDetails.duration),
            style = MaterialTheme.typography.bodySmall,
            color = Color.White,
            modifier = modifier
                .semantics { contentDescription = VIDEO_DURATION }
                .padding(dimensionResource(R.dimen.padding_small_8))
                .clip(MaterialTheme.shapes.small)
                .background(MaterialTheme.colorScheme.outline)
                .padding(dimensionResource(R.dimen.padding_tiny_2))
                .constrainAs(videoDuration) {
                    bottom.linkTo(videoPreview.bottom)
                    end.linkTo(videoPreview.end)
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
                .width(dimensionResource(R.dimen.width_small_extra_35))
                .height(dimensionResource(R.dimen.height_small_extra_35))
                .clip(MaterialTheme.shapes.medium)
                .constrainAs(channelImg) {
                    start.linkTo(videoPreview.end)
                    bottom.linkTo(videoPreview.bottom)
                }
        )

        Text(
            text = youtubeVideoUiModel.snippet.title,
            style = MaterialTheme.typography.titleSmall,
            maxLines = 2,
            textAlign = TextAlign.Start,
            modifier = modifier
                .semantics { contentDescription = videoItemTitleDescription }
                .padding(dimensionResource(R.dimen.padding_small_8))
                .constrainAs(videoTitle) {
                    start.linkTo(videoPreview.end)
                    top.linkTo(videoPreview.top)
                    end.linkTo(parent.end)
                    bottom.linkTo(videoPreview.baseline)
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
                    end = dimensionResource(R.dimen.padding_small_8),
                    top = dimensionResource(R.dimen.padding_small_8),
                    bottom = dimensionResource(R.dimen.padding_small_8)
                )
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

@PreviewLightDark
@Preview(device = "spec:parent=pixel_9,orientation=landscape")
@Composable
private fun ItemLandscapePreview() {
    BlueTubeComposeTheme {
        Surface {
            VideoItemLandscape(youtubeVideoUiModel = DEFAULT_VIDEO, modifier = Modifier,
                navigateToPlayerScreen = {}
            )
        }
    }
}

@Composable
@Preview(widthDp = 1200, heightDp = 800)
fun ItemLandscapePreviewTablet() {
    val tabletSize = DpSize(width = 1200.dp, height = 900.dp)

    CompositionLocalProvider(
        LocalWindowSizeClass provides previewWindowSizeClass(tabletSize)
    ) {
        BlueTubeComposeTheme {
            Surface {
                VideoItemLandscape(youtubeVideoUiModel = DEFAULT_VIDEO, modifier = Modifier,
                    navigateToPlayerScreen = {}
                )
            }
        }
    }
}
