package com.appelier.bluetubecompose.core.core_ui.views.video_list_screen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
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
import com.appelier.bluetubecompose.LocalWindowSizeClass
import com.appelier.bluetubecompose.core.core_ui.views.PagerContentManager
import com.appelier.bluetubecompose.screen_video_list.model.videos.YoutubeVideo
import com.appelier.bluetubecompose.screen_video_list.model.videos.YoutubeVideo.Companion.DEFAULT_VIDEO_LIST
import com.appelier.bluetubecompose.utils.VideoListScreenTags
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Composable
fun YouTubeVideoList(
    modifier: Modifier = Modifier,
    getVideoState: () -> State<StateFlow<PagingData<YoutubeVideo>>>,
    innerPadding: PaddingValues = PaddingValues(0.dp),
    navigateToPlayerScreen: (YoutubeVideo) -> Unit,
    ) {
    val videos = getVideoState.invoke().value.collectAsLazyPagingItems()

    val keyboardController = LocalSoftwareKeyboardController.current
    val nestedScrollConnection = remember {
        object: NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                keyboardController?.hide()
                return Offset.Zero
            }
        }
    }

    PagerContentManager(
        videoState = videos,
        contentList = {
            ItemsList(
                videos,
                modifier,
                navigateToPlayerScreen,
                LocalWindowSizeClass.current.widthSizeClass
            )
        },
        modifier = modifier
            .fillMaxSize()
            .nestedScroll(nestedScrollConnection)
            .padding(innerPadding)
    )
}

@Composable
fun ItemsList(
    videos: LazyPagingItems<YoutubeVideo>,
    modifier: Modifier,
    navigateToPlayerScreen: (YoutubeVideo) -> Unit,
    windowSize: WindowWidthSizeClass
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
            when(windowSize) {
                WindowWidthSizeClass.Compact ->
                    videos[index]?.let { VideoItem(youtubeVideo = it, modifier = modifier, navigateToPlayerScreen) }
                else ->
                    videos[index]?.let { VideoItemLandscape(youtubeVideo = it, modifier = modifier, navigateToPlayerScreen) }
            }
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
        modifier = Modifier,
        getVideoState = { mutableStateOf(MutableStateFlow(PagingData.from(DEFAULT_VIDEO_LIST))) },
        PaddingValues(8.dp),
    ) {}
}
