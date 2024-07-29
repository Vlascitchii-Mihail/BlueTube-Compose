package com.appelier.bluetubecompose.core.core_ui.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.appelier.bluetubecompose.screen_video_list.model.videos.YoutubeVideo
import com.appelier.bluetubecompose.screen_video_list.model.videos.YoutubeVideo.Companion.DEFAULT_VIDEO_LIST
import com.appelier.bluetubecompose.utils.VideoListScreenTags
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun YouTubeVideoList(
    videos: LazyPagingItems<YoutubeVideo>,
    modifier: Modifier,
    innerPadding: PaddingValues,
    navigateToPlayerScreen: (YoutubeVideo) -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val nestedScrollConnection = remember {
        object: NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                keyboardController?.hide()
                return Offset.Zero
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .nestedScroll(nestedScrollConnection)
            .padding(innerPadding), contentAlignment = Alignment.Center
    ) {
        val refreshLoadState = videos.loadState.refresh
        when {
            refreshLoadState is LoadState.NotLoading -> ItemsList(videos, modifier, navigateToPlayerScreen)
            refreshLoadState is LoadState.Loading && (videos.itemCount > 0) -> ItemsList(
                videos,
                modifier,
                navigateToPlayerScreen
            )
            refreshLoadState is LoadState.Loading -> CircularProgressIndicator(
                modifier
                    .align(Alignment.Center)
                    .height(48.dp)
            )
            videos.loadState.refresh is LoadState.Error -> PaginationErrorItem(
                errorText = (videos.loadState.refresh as LoadState.Error).error.message,
                modifier = modifier,
                onRetryClick = { videos.refresh() }
            )
        }
    }
}

@Composable
private fun ItemsList(
    videos: LazyPagingItems<YoutubeVideo>,
    modifier: Modifier,
    navigateToPlayerScreen: (YoutubeVideo) -> Unit
) {
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .testTag(VideoListScreenTags.VIDEO_LIST)
    ) {
        items(
            count = videos.itemCount,
            key = videos.itemKey(),
            contentType = videos.itemContentType()
        ) { index ->
            videos[index]?.let { VideoItem(youtubeVideo = it, defaultModifier = modifier, navigateToPlayerScreen) }
        }
        item {
            when(videos.loadState.append) {
                is LoadState.Loading ->  CircularProgressIndicator(modifier.height(48.dp))
                is LoadState.Error -> PaginationRetryItem(onRetryClick = { videos.retry() })
                is LoadState.NotLoading -> Unit
            }
        }
    }
}

@Preview
@Composable
private fun YouTubeVideoListPreview() {
    YouTubeVideoList(
        videos = MutableStateFlow(PagingData.from(DEFAULT_VIDEO_LIST)).collectAsLazyPagingItems(),
        modifier = Modifier,
        PaddingValues(8.dp),
        {}
    )
}