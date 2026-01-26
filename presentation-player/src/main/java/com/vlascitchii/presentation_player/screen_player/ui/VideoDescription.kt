package com.vlascitchii.presentation_player.screen_player.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.vlascitchii.presentation_common.R as CommonR
import com.vlascitchii.presentation_player.R as PlayerR
import com.vlascitchii.presentation_common.model.videos.YoutubeVideoUiModel
import com.vlascitchii.presentation_common.ui.theme.BlueTubeComposeTheme
import com.vlascitchii.presentation_common.utils.Core.CHANNEL_PREVIEW_IMG
import com.vlascitchii.presentation_common.utils.VideoPlayerScreenTags
import com.vlascitchii.presentation_common.utils.formatDate
import com.vlascitchii.presentation_common.utils.formatViews

@Composable
fun VideoDescription(video: YoutubeVideoUiModel, modifier: Modifier = Modifier) {

    val videoDescription = stringResource(PlayerR.string.video_content_description)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .semantics { contentDescription = videoDescription }
            .background(MaterialTheme.colorScheme.background)
    ) {
        Text(
            text = video.snippet.title,
            modifier = modifier.padding(vertical = 4.dp),
            maxLines = 2,
            style = MaterialTheme.typography.titleMedium,
        )
        Text(
            text = formatViews(video.statistics.viewCount) + stringResource(id = CommonR.string.views) +
                    "\t\t" + formatDate(video.snippet.publishedAt),
            modifier = modifier.padding(vertical = 4.dp),
            style = MaterialTheme.typography.bodySmall,
        )

        Row(
            modifier = modifier.padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(video.snippet.channelImgUrl)
                    .crossfade(true)
                    .placeholder(CommonR.drawable.sceleton_thumbnail)
                    .error(CommonR.drawable.sceleton_thumbnail)
                    .build(),
                contentDescription =  stringResource(CommonR.string.channel_description) + video.snippet.channelTitle,
                contentScale = ContentScale.Crop,
                modifier = modifier
                    .testTag(CHANNEL_PREVIEW_IMG)
                    .padding(end = 8.dp)
                    .width(50.dp)
                    .height(50.dp)
                    .clip(MaterialTheme.shapes.medium)
            )

            Text(
                text = video.snippet.channelTitle,
                modifier = modifier.padding(end = 8.dp),
                style = MaterialTheme.typography.bodyMedium,
            )

            Text(
                text = formatViews(video.statistics.viewCount),
                modifier = modifier.padding(end = 8.dp),
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

@PreviewLightDark
@Composable
fun VideoDescriptionPreview() {
    BlueTubeComposeTheme {
        Surface {
            VideoDescription(video = YoutubeVideoUiModel.DEFAULT_VIDEO)
        }
    }
}
