package com.appelier.bluetubecompose.core.core_ui.views.video_list_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.appelier.bluetubecompose.R
import com.appelier.bluetubecompose.screen_video_list.model.videos.YoutubeVideo
import com.appelier.bluetubecompose.utils.Core.CHANNEL_PREVIEW_IMG
import com.appelier.bluetubecompose.utils.VideoPlayerScreenTags
import com.appelier.bluetubecompose.utils.formatDate
import com.appelier.bluetubecompose.utils.formatViews
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun VideoDescription(video: YoutubeVideo, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .testTag(VideoPlayerScreenTags.VIDEO_DESCRIPTION)
            .background(MaterialTheme.colorScheme.background)
    ) {
        Text(
            text = video.snippet.title,
            modifier = modifier.padding(vertical = 4.dp),
            maxLines = 2,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = formatViews(video.statistics.viewCount) + stringResource(id = R.string.views) +
                    "\t\t" + formatDate(video.snippet.publishedAt),
            modifier = modifier.padding(vertical = 4.dp),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onBackground
        )

        Row(
            modifier = modifier.padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            GlideImage(
                model = video.snippet.channelImgUrl,
                loading = placeholder(R.drawable.sceleton_thumbnail),
                contentDescription = stringResource(R.string.channel_name) + video.snippet.channelTitle,
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
                color = MaterialTheme.colorScheme.onBackground
            )

            Text(
                text = formatViews(video.statistics.viewCount),
                modifier = modifier.padding(end = 8.dp),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Preview
@Composable
fun VideoDescriptionPreview() {
    VideoDescription(video = YoutubeVideo.DEFAULT_VIDEO)
}