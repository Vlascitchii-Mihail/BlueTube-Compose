package com.vlascitchii.presentation_video_list.screen_video_list.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.PagingData
import com.vlascitchii.presentation_common.entity.videos.YoutubeVideoUiModel
import com.vlascitchii.presentation_common.entity.videos.YoutubeVideoUiModel.Companion.DEFAULT_VIDEO_LIST
import com.appelier.bluetubecompose.network_observer.ConnectivityStatus
import com.vlascitchii.presentation_common.ui.video_list.YouTubeVideoList
import com.vlascitchii.presentation_common.utils.SnackbarController
import com.vlascitchii.presentation_common.utils.SnackbarEvent
import com.vlascitchii.presentation_video_list.search_video.SearchState
import com.vlascitchii.presentation_video_list.video_list_screen.ListScreenAppBar
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoListScreen(
    connectivityStatus: Flow<com.appelier.bluetubecompose.network_observer.ConnectivityStatus>,
    listScreenAppBar: @Composable (scrollAppBarBehaviour: TopAppBarScrollBehavior) -> Unit,
    videoList: @Composable (padding: PaddingValues) -> Unit
) {
    val networkConnectivityStatus by connectivityStatus.collectAsStateWithLifecycle(
        initialValue = com.appelier.bluetubecompose.network_observer.ConnectivityStatus.Available
    )
    LaunchedEffect(networkConnectivityStatus) {
        if (networkConnectivityStatus == com.appelier.bluetubecompose.network_observer.ConnectivityStatus.Lost) {
            SnackbarController.sendEvent(
                event = SnackbarEvent(
                    message = "Wrong internet connection"
                )
            )
        }
    }

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    Scaffold(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .nestedScroll(scrollBehavior.nestedScrollConnection),
//            .testTag(VIDEO_LIST_SCREEN),
        topBar = {
            listScreenAppBar.invoke(scrollBehavior)
        },
        content = { innerPadding ->
            videoList.invoke(innerPadding)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun VideoListScreenPreview() {
    val videoStateFlow: StateFlow<PagingData<YoutubeVideoUiModel>> = MutableStateFlow(PagingData.from(DEFAULT_VIDEO_LIST ))
    val closedSearchViewState = remember { MutableStateFlow(SearchState.CLOSED) }
    val searchText = remember { MutableStateFlow("Test text") }

    VideoListScreen(
        connectivityStatus = flowOf(com.appelier.bluetubecompose.network_observer.ConnectivityStatus.Available),
        listScreenAppBar = {
            ListScreenAppBar(
                searchViewState = closedSearchViewState,
                searchTextState = searchText,
                scrollAppBarBehaviour = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState()),
                onTextChange = { searchInput: String -> },
                onSearchClicked = {},
                updateSearchState = {}
            )
        },
        videoList = {
            YouTubeVideoList(
                getVideoState = { mutableStateOf(videoStateFlow) },
                navigateToPlayerScreen = {}
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun VideoSearchListScreenPreview() {
    val videoStateFlow: StateFlow<PagingData<YoutubeVideoUiModel>> = MutableStateFlow(PagingData.from(DEFAULT_VIDEO_LIST ))
    val openedSearchViewState = remember { MutableStateFlow(SearchState.OPENED) }
    val searchText = remember { MutableStateFlow("Test text") }

    VideoListScreen(
        connectivityStatus = flowOf(com.appelier.bluetubecompose.network_observer.ConnectivityStatus.Available),
        listScreenAppBar = {
            ListScreenAppBar(
                searchViewState = openedSearchViewState,
                searchTextState = searchText,
                scrollAppBarBehaviour = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState()),
                onTextChange = { searchInput: String -> },
                onSearchClicked = {},
                updateSearchState = {}
            )
        },
        videoList = {
            YouTubeVideoList(
                getVideoState = { mutableStateOf(videoStateFlow) },
                navigateToPlayerScreen = {}
            )
        }
    )
}
