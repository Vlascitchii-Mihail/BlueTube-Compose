package com.vlascitchii.presentation_video_list.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.PagingData
import com.vlascitchii.common_ui.R as RCommon
import com.vlascitchii.video_list_screen.R as RVideoListScreen
import com.vlascitchii.presentation_common.model.videos.YoutubeVideoUiModel
import com.vlascitchii.presentation_common.model.videos.YoutubeVideoUiModel.Companion.PREVIEW_VIDEO_LIST
import com.vlascitchii.presentation_common.network_observer.NetworkConnectivityStatus
import com.vlascitchii.presentation_common.ui.global_snackbar.HandleSnackbar
import com.vlascitchii.presentation_common.ui.global_snackbar.SnackBarController
import com.vlascitchii.presentation_common.ui.global_snackbar.SnackBarEvent
import com.vlascitchii.presentation_common.ui.state.UiState
import com.vlascitchii.presentation_common.ui.theme.BlueTubeComposeTheme
import com.vlascitchii.presentation_common.ui.video_list.YouTubeVideoList
import com.vlascitchii.presentation_video_list.screen.state.SearchState
import com.vlascitchii.presentation_video_list.screen.ui.ListScreenAppBar
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf

const val VIDEO_LIST_BOTTOM_NAV_NAME = "Video list"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoListScreen(
    connectivityStatus: Flow<NetworkConnectivityStatus>,
    scrollBehavior: TopAppBarScrollBehavior,
    listScreenAppBar: @Composable () -> Unit,
    videoList: @Composable (padding: PaddingValues) -> Unit,
    modifier: Modifier = Modifier,
    bottomNavigation: @Composable () -> Unit
) {
    val networkConnectivityStatus by connectivityStatus.collectAsStateWithLifecycle(
        initialValue = NetworkConnectivityStatus.Available
    )
    val localContext = LocalContext.current
    LaunchedEffect(networkConnectivityStatus) {
        if (networkConnectivityStatus == NetworkConnectivityStatus.Lost) {
            SnackBarController.sendEvent(
                event = SnackBarEvent(
                    message = localContext.getString(RCommon.string.wrong_internet_connection)
                )
            )
        }
    }

    val snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
    HandleSnackbar(snackbarHostState, rememberCoroutineScope())

    Scaffold(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .fillMaxSize()
            .semantics { contentDescription = localContext.getString(RVideoListScreen.string.video_list_screen_description )},
        topBar = {
            listScreenAppBar.invoke()
        },
        content = { innerPadding ->
            videoList.invoke(innerPadding)
        },
        bottomBar = {
            bottomNavigation.invoke()
        },
        snackbarHost = {
            SnackbarHost(
                modifier = Modifier
                    .semantics {
                        contentDescription = localContext.getString(RCommon.string.snackbar_description)
                    },
                hostState = snackbarHostState
            )
        }
    )
}

private val previewVideoStateFlow: StateFlow<UiState<Flow<PagingData<YoutubeVideoUiModel>>>> =
    MutableStateFlow(UiState.Success(flowOf(PagingData.from(PREVIEW_VIDEO_LIST))))

@OptIn(ExperimentalMaterial3Api::class)
@PreviewLightDark
@Composable
fun VideoListScreenPreview() {
    val closedSearchViewState = remember { MutableStateFlow(SearchState.CLOSED) }
    val searchText = remember { MutableStateFlow(TextFieldValue("Test text")) }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    BlueTubeComposeTheme {
        Surface {
            VideoListScreen(
                connectivityStatus = flowOf(NetworkConnectivityStatus.Available),
                scrollBehavior= scrollBehavior,
                listScreenAppBar = {
                    ListScreenAppBar(
                        searchViewState = closedSearchViewState,
                        searchTextState = searchText,
                        scrollAppBarBehaviour = TopAppBarDefaults.enterAlwaysScrollBehavior(
                            rememberTopAppBarState()
                        ),
                        onTextChange = { searchInput: String -> },
                        onSearchClicked = {},
                        updateSearchState = {}
                    )
                },
                videoList = {
                    YouTubeVideoList(
                        initVideosList = {},
                        videosFlow = previewVideoStateFlow,
                        navigateToPlayerScreen = {}
                    )
                },
                bottomNavigation = {}
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@PreviewLightDark
@Composable
fun VideoSearchListScreenPreview() {
    val openedSearchViewState = remember { MutableStateFlow(SearchState.OPENED) }
    val searchText = remember { MutableStateFlow(TextFieldValue("")) }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    BlueTubeComposeTheme {
        Surface {
            VideoListScreen(
                connectivityStatus = flowOf(NetworkConnectivityStatus.Available),
                scrollBehavior= scrollBehavior,
                listScreenAppBar = {
                    ListScreenAppBar(
                        searchViewState = openedSearchViewState,
                        searchTextState = searchText,
                        scrollAppBarBehaviour = TopAppBarDefaults.enterAlwaysScrollBehavior(
                            rememberTopAppBarState()
                        ),
                        onTextChange = { searchInput: String -> },
                        onSearchClicked = {},
                        updateSearchState = {}
                    )
                },
                videoList = {
                    YouTubeVideoList(
                        initVideosList = {},
                        videosFlow = previewVideoStateFlow,
                        navigateToPlayerScreen = {}
                    )
                },
                bottomNavigation = {}
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(device = "spec:parent=pixel_9,orientation=landscape")
@Preview(widthDp = 1200, heightDp = 800)
@Composable
fun VideoSearchListScreenLandscapePreview() {
    val openedSearchViewState = remember { MutableStateFlow(SearchState.OPENED) }
    val searchText = remember { MutableStateFlow(TextFieldValue("")) }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    BlueTubeComposeTheme {
        Surface {
            VideoListScreen(
                connectivityStatus = flowOf(NetworkConnectivityStatus.Available),
                scrollBehavior= scrollBehavior,
                listScreenAppBar = {
                    ListScreenAppBar(
                        searchViewState = openedSearchViewState,
                        searchTextState = searchText,
                        scrollAppBarBehaviour = TopAppBarDefaults.enterAlwaysScrollBehavior(
                            rememberTopAppBarState()
                        ),
                        onTextChange = { searchInput: String -> },
                        onSearchClicked = {},
                        updateSearchState = {}
                    )
                },
                videoList = {
                    YouTubeVideoList(
                        initVideosList = {},
                        videosFlow = previewVideoStateFlow,
                        navigateToPlayerScreen = {}
                    )
                },
                bottomNavigation = {}
            )
        }
    }
}
