package com.vlascitchii.presentation_common.ui.video_list

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.vlascitchii.presentation_common.R
import com.vlascitchii.presentation_common.entity.videos.YoutubeVideoUiModel
import com.vlascitchii.presentation_common.entity.videos.YoutubeVideoUiModel.Companion.DEFAULT_VIDEO_LIST
import com.vlascitchii.presentation_common.ui.PagerContentManager
import com.vlascitchii.presentation_common.ui.error.PaginationRetryItem
import com.vlascitchii.presentation_common.ui.screen.CommonScreen
import com.vlascitchii.presentation_common.ui.state.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf

@Composable
fun YouTubeVideoList(
    modifier: Modifier = Modifier,
    videosFlow: StateFlow<UiState<PagingData<YoutubeVideoUiModel>>>,
    innerPadding: PaddingValues = PaddingValues(0.dp),
    localWindowSizeClass: WindowWidthSizeClass = WindowWidthSizeClass.Compact,
    navigateToPlayerScreen: (YoutubeVideoUiModel) -> Unit,
) {
    videosFlow.collectAsStateWithLifecycle().value.let { uiStateVideoList: UiState<PagingData<YoutubeVideoUiModel>> ->
        CommonScreen(uiStateVideoList) { pagingData: PagingData<YoutubeVideoUiModel> ->
            val lazyVideosPagingItem = flowOf<PagingData<YoutubeVideoUiModel>>(pagingData).collectAsLazyPagingItems()

            val keyboardController = LocalSoftwareKeyboardController.current
            val nestedScrollConnection = remember {
                object : NestedScrollConnection {
                    override fun onPreScroll(
                        available: Offset,
                        source: NestedScrollSource
                    ): Offset {
                        keyboardController?.hide()
                        return Offset.Zero
                    }
                }
            }

            PagerContentManager(
                videoState = lazyVideosPagingItem,
                contentList = {
                    ItemsList(
                        lazyVideosPagingItem,
                        modifier,
                        navigateToPlayerScreen,
                        localWindowSizeClass
                    )
                },
                modifier = modifier
                    .fillMaxSize()
                    .nestedScroll(nestedScrollConnection)
                    .padding(innerPadding)
            )
        }
    }
}

@Composable
fun ItemsList(
    videos: LazyPagingItems<YoutubeVideoUiModel>,
    modifier: Modifier,
    navigateToPlayerScreen: (YoutubeVideoUiModel) -> Unit,
    windowSize: WindowWidthSizeClass
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

            when (windowSize) {
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

                else ->
                    videos[index]?.let {
                        VideoItem(
                            youtubeVideoUiModel = it,
                            modifier = modifier,
                            navigateToPlayerScreen
                        )
                    }
            }
        }

        item {
            when (videos.loadState.append) {
                is LoadState.Loading -> CircularProgressIndicator(modifier.height(48.dp))
                is LoadState.Error -> PaginationRetryItem(onRetryClick = { videos.retry() })
                is LoadState.NotLoading -> Unit
            }
        }
    }
}

@Preview
@Composable
private fun YouTubeVideoListCompactPreview() {
    YouTubeVideoList(
        modifier = Modifier,
        videosFlow = MutableStateFlow(UiState.Success(PagingData.from(DEFAULT_VIDEO_LIST))),
        PaddingValues(8.dp),
        localWindowSizeClass = WindowWidthSizeClass.Compact
    ) {}
}

@Preview
@Composable
private fun YouTubeVideoListMediumPreview() {
    YouTubeVideoList(
        modifier = Modifier,
        videosFlow = MutableStateFlow(UiState.Success(PagingData.from(DEFAULT_VIDEO_LIST))),
        PaddingValues(8.dp),
        localWindowSizeClass = WindowWidthSizeClass.Medium
    ) {}
}

@Preview
@Composable
private fun YouTubeVideoListExpandedPreview() {
    YouTubeVideoList(
        modifier = Modifier,
        videosFlow = MutableStateFlow(UiState.Success(PagingData.from(DEFAULT_VIDEO_LIST))),
        PaddingValues(8.dp),
        localWindowSizeClass = WindowWidthSizeClass.Expanded
    ) {}
}
