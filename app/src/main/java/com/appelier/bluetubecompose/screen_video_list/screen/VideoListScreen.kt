package com.appelier.bluetubecompose.screen_video_list.screen

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.PagingData
import com.appelier.bluetubecompose.R
import com.appelier.bluetubecompose.core.core_ui.views.video_list_screen.BlueTubeTopAppBar
import com.appelier.bluetubecompose.core.core_ui.views.video_list_screen.SearchAppBarBlueTube
import com.appelier.bluetubecompose.core.core_ui.views.video_list_screen.YouTubeVideoList
import com.appelier.bluetubecompose.screen_video_list.model.videos.YoutubeVideo
import com.appelier.bluetubecompose.screen_video_list.model.videos.YoutubeVideo.Companion.DEFAULT_VIDEO_LIST
import com.appelier.bluetubecompose.search_video.SearchState
import com.appelier.bluetubecompose.utils.NavigationTags.VIDEO_LIST_SCREEN
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoListScreen(
    navigateToPlayerScreen: (video: YoutubeVideo) -> Unit,
    searchViewState: StateFlow<SearchState>,
    searchTextState: StateFlow<String>,
    videos: () -> State<StateFlow<PagingData<YoutubeVideo>>>,
    updateSearchTextState: (String) -> Unit,
    updateSearchState: (SearchState) -> Unit,
    setSearchVideosFlow: () -> Unit,
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
                onSearchClicked = { setSearchVideosFlow() },
                onSearchTriggered = { updateSearchState(SearchState.OPENED) }
            )
        }
    )
    { innerPadding ->
        YouTubeVideoList(
            videos,
            Modifier,
            innerPadding,
            navigateToPlayerScreen,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ListScreenAppBar(
    searchViewState: StateFlow<SearchState>,
    searchTextState: StateFlow<String>,
    scrollAppBarBehaviour: TopAppBarScrollBehavior,
    onTextChange: (String) -> Unit,
    onCloseClicked: () -> Unit,
    onSearchClicked: () -> Unit,
    onSearchTriggered: () -> Unit,
) {
    val searchViewToolbarState by searchViewState.collectAsStateWithLifecycle()

    when (searchViewToolbarState) {
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

@Preview
@Composable
fun VideoListScreenPreview() {
    val videos = remember { mutableStateOf(MutableStateFlow(PagingData.from(DEFAULT_VIDEO_LIST))) }
    val searchViewState = remember { MutableStateFlow(SearchState.CLOSED) }
    val searchText = remember { MutableStateFlow("Test text") }
    VideoListScreen(
        navigateToPlayerScreen = {},
        searchViewState = searchViewState,
        searchTextState = searchText,
        videos = { videos },
        updateSearchTextState = {},
        updateSearchState = {},
        {},
    )
}