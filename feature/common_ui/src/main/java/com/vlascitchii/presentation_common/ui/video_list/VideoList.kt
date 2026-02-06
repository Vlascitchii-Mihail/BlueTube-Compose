package com.vlascitchii.presentation_common.ui.video_list

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.vlascitchii.common_ui.R
import com.vlascitchii.presentation_common.model.videos.YoutubeVideoUiModel
import com.vlascitchii.presentation_common.model.videos.YoutubeVideoUiModel.Companion.PREVIEW_VIDEO_LIST
import com.vlascitchii.presentation_common.ui.error.PaginationRetryItem
import com.vlascitchii.presentation_common.ui.screen.CommonScreen
import com.vlascitchii.presentation_common.ui.screen.LocalWindowSizeClass
import com.vlascitchii.presentation_common.ui.screen.PagerContentManager
import com.vlascitchii.presentation_common.ui.screen.mvi.CommonMVI
import com.vlascitchii.presentation_common.ui.screen.previewWindowSizeClass
import com.vlascitchii.presentation_common.ui.theme.BlueTubeComposeTheme
import com.vlascitchii.presentation_common.ui.video_list.state.UiVideoListAction
import com.vlascitchii.presentation_common.ui.video_list.state.VideoListNavigationEvent
import com.vlascitchii.presentation_common.ui.video_list.state.VideoListUIState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

@Composable
fun YouTubeVideoList(
    videoListUIState: VideoListUIState,
    videoListMVI: CommonMVI<UiVideoListAction, VideoListNavigationEvent>,
    modifier: Modifier = Modifier,
    innerPadding: PaddingValues = PaddingValues(),
) {
    CommonScreen(videoListUIState.videoListState) { pagingData: Flow<PagingData<YoutubeVideoUiModel>> ->
        val lazyVideosPagingItem = pagingData.collectAsLazyPagingItems()

        PagerContentManager(
            videoState = lazyVideosPagingItem,
            contentList = {
                ItemsList(
                    lazyVideosPagingItem,
                    modifier,
                    navigateToPlayerScreen = { video: YoutubeVideoUiModel ->
                        videoListMVI.submitSingleNavigationEvent(VideoListNavigationEvent.NavigationPlayerScreenEvent(video))
                    }
                )
            },
            innerPadding = innerPadding,
            modifier = modifier
        )
    }
}

@Composable
fun ItemsList(
    videos: LazyPagingItems<YoutubeVideoUiModel>,
    modifier: Modifier,
    navigateToPlayerScreen: (YoutubeVideoUiModel) -> Unit,
) {
    val videoListDescr = stringResource(R.string.video_list_description)

    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .semantics { contentDescription = videoListDescr }
    ) {
        items(
            count = videos.itemCount,
            key = videos.itemKey(),
            contentType = videos.itemContentType()
        ) { index ->

            when (LocalWindowSizeClass.current.widthSizeClass) {
                WindowWidthSizeClass.Compact ->
                    videos[index]?.let {
                        VideoItem(
                            youtubeVideoUiModel = it,
                            modifier = modifier,
                            navigateToPlayerScreen
                        )
                    }

                WindowWidthSizeClass.Medium ->
                    videos[index]?.let {
                        VideoItemLandscape(
                            youtubeVideoUiModel = it,
                            modifier = modifier,
                            navigateToPlayerScreen
                        )
                    }

                WindowWidthSizeClass.Expanded ->
                    videos[index]?.let {
                        VideoItemLandscape(
                            youtubeVideoUiModel = it,
                            modifier = modifier,
                            navigateToPlayerScreen
                        )
                    }
            }
        }

        item {
            when (videos.loadState.append) {
                is LoadState.Loading -> CircularProgressIndicator(
                    modifier.height(
                        dimensionResource(
                            R.dimen.height_small_40
                        )
                    )
                )

                is LoadState.Error -> PaginationRetryItem(onRetryClick = { videos.retry() })
                is LoadState.NotLoading -> Unit
            }
        }
    }
}

private lateinit var lazyPagingItems: LazyPagingItems<YoutubeVideoUiModel>

@Preview()
@Composable
private fun YouTubeVideoItemsListCompactPreview() {
    BlueTubeComposeTheme {
        val pagingData: Flow<PagingData<YoutubeVideoUiModel>> =
            flowOf(PagingData.from(PREVIEW_VIDEO_LIST))
        lazyPagingItems = pagingData.collectAsLazyPagingItems()

        Surface {
            ItemsList(
                modifier = Modifier,
                videos = lazyPagingItems,
            ) {}
        }
    }
}

@Preview(widthDp = 1200, heightDp = 800)
@Composable
private fun YouTubeVideoItemsListCompactTabletPreview() {
    val tabletSize = DpSize(width = 1200.dp, height = 800.dp)

    CompositionLocalProvider(
        LocalWindowSizeClass provides previewWindowSizeClass(tabletSize)
    ) {
        BlueTubeComposeTheme {
            Surface {
                ItemsList(
                    modifier = Modifier,
                    videos = lazyPagingItems,
                ) {}
            }
        }
    }
}
