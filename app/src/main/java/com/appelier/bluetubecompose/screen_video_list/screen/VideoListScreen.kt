package com.appelier.bluetubecompose.screen_video_list.screen

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.appelier.bluetubecompose.R
import com.appelier.bluetubecompose.core.core_ui.views.BlueTubeTopAppBar
import com.appelier.bluetubecompose.core.core_ui.views.PaginationErrorItem
import com.appelier.bluetubecompose.core.core_ui.views.PaginationRetryItem
import com.appelier.bluetubecompose.core.core_ui.views.SearchAppBarBlueTube
import com.appelier.bluetubecompose.core.core_ui.views.VideoPreviewItem
import com.appelier.bluetubecompose.screen_video_list.model.videos.YoutubeVideo
import com.appelier.bluetubecompose.search_video.SearchState
import com.appelier.bluetubecompose.utils.NavigationTags.VIDEO_LIST_SCREEN
import com.appelier.bluetubecompose.utils.VideoListScreenTags.LOAD_STATE
import com.appelier.bluetubecompose.utils.VideoListScreenTags.VIDEO_LIST
import kotlinx.coroutines.flow.StateFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoListScreen(
    navController: NavController,
    searchViewState: SearchState,
    searchTextState: String,
    videos: StateFlow<PagingData<YoutubeVideo>>,
    updateSearchTextState: (String) -> Unit,
    updateSearchState: (SearchState) -> Unit,
    getSearchVideosFlow: (String) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .testTag(VIDEO_LIST_SCREEN),
        topBar = {
            ListScreenAppBar(
                searchViewState = searchViewState,
                searchTextState = searchTextState,
                scrollAppBarBehaviour = scrollBehavior,
                onTextChange = { input -> updateSearchTextState(input) },
                onCloseClicked = { updateSearchState(SearchState.CLOSED) },
                onSearchClicked = { getSearchVideosFlow(searchTextState) },
                onSearchTriggered = { updateSearchState(SearchState.OPENED) }
            )
        }
    )
    { innerPadding ->
        VideoList(videos.collectAsLazyPagingItems(), Modifier, innerPadding)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ListScreenAppBar(
    searchViewState: SearchState,
    searchTextState: String,
    scrollAppBarBehaviour: TopAppBarScrollBehavior,
    onTextChange: (String) -> Unit,
    onCloseClicked: () -> Unit,
    onSearchClicked: (String) -> Unit,
    onSearchTriggered: () -> Unit
) {
    when(searchViewState) {
        SearchState.CLOSED -> {
            BlueTubeTopAppBar(
                title = stringResource(id = R.string.appbar_title),
                icon = Icons.Filled.Search,
                scrollAppBarBehaviour,
                searchAction = onSearchTriggered
            )
        }
        SearchState.OPENED -> {
            SearchAppBarBlueTube(
                searchText = searchTextState,
                onTextChange = onTextChange,
                onCloseClicked = onCloseClicked,
                onSearchClicked = onSearchClicked
            )
        }
    }
}

@Composable
private fun VideoList(
    videos: LazyPagingItems<YoutubeVideo>,
    modifier: Modifier,
    innerPadding: PaddingValues,
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
            refreshLoadState is LoadState.NotLoading -> ItemsList(videos, modifier)
            refreshLoadState is LoadState.Loading && (videos.itemCount > 0) -> ItemsList(videos, modifier)
            refreshLoadState is LoadState.Loading -> CircularProgressIndicator(
                modifier
                    .align(Alignment.Center)
                    .height(48.dp)
                    .testTag(LOAD_STATE))
            videos.loadState.refresh is LoadState.Error -> PaginationErrorItem(
                errorText = (videos.loadState.refresh as LoadState.Error).error.message,
                modifier = modifier,
                onRetryClick = { videos.refresh() }
            )
        }
    }
}

@Composable
private fun ItemsList(videos: LazyPagingItems<YoutubeVideo>, modifier: Modifier) {
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .testTag(VIDEO_LIST)
    ) {
        items(
            count = videos.itemCount,
            key = videos.itemKey(),
            contentType = videos.itemContentType()
        ) { index ->
            videos[index]?.let { VideoPreviewItem(youtubeVideo = it, defaultModifier = modifier) }
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
private fun VideoListPreview() {
    LazyColumn(modifier = Modifier
        .fillMaxSize()
        .border(
            5.dp,
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.secondary
        )) {
        items(YoutubeVideo.DEFAULT_VIDEO_LIST) { video ->
            VideoPreviewItem(
                youtubeVideo = video,
                defaultModifier = Modifier,
            )
        }
    }
}