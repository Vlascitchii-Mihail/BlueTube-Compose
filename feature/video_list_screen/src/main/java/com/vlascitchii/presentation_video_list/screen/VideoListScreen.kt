package com.vlascitchii.presentation_video_list.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vlascitchii.presentation_common.network_observer.NetworkConnectivityStatus
import com.vlascitchii.presentation_common.ui.global_snackbar.HandleSnackbar
import com.vlascitchii.presentation_common.ui.global_snackbar.SnackBarController
import com.vlascitchii.presentation_common.ui.global_snackbar.SnackBarEvent
import com.vlascitchii.presentation_common.ui.screen.mvi.CommonMVI
import com.vlascitchii.presentation_common.ui.screen.mvi.PREVIEW_VIDEO_LIST_MVI
import com.vlascitchii.presentation_common.ui.state_common.UiState
import com.vlascitchii.presentation_common.ui.theme.BlueTubeComposeTheme
import com.vlascitchii.presentation_common.ui.video_list.YouTubeVideoList
import com.vlascitchii.presentation_common.ui.video_list.state.UiVideoListAction
import com.vlascitchii.presentation_common.ui.video_list.state.VideoListNavigationEvent
import com.vlascitchii.presentation_common.ui.video_list.state.VideoListUIState
import com.vlascitchii.presentation_video_list.ui.ListScreenAppBar
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.vlascitchii.common_ui.R as RCommon
import com.vlascitchii.video_list_screen.R as RVideoListScreen

const val VIDEO_LIST_BOTTOM_NAV_NAME = "Video list"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoListScreen(
    videoListUIStateFlow: StateFlow<VideoListUIState>,
    videoListMVI: CommonMVI<UiVideoListAction, VideoListNavigationEvent>,
    videoSearchQuery: String,
    modifier: Modifier = Modifier,
    bottomNavigation: @Composable () -> Unit
) {

    val localContext = LocalContext.current
    val scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    videoListUIStateFlow.collectAsStateWithLifecycle().value.let { videoListUIState: VideoListUIState ->

        if (videoListUIState.videoListState == UiState.Loading) videoListMVI.submitAction(
            UiVideoListAction.GetVideo(videoSearchQuery)
        )

        LaunchedEffect(videoListUIState.networkConnectivityStatus) {
            if (videoListUIState.networkConnectivityStatus == NetworkConnectivityStatus.Lost) {
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
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .fillMaxSize()
                .semantics {
                    contentDescription =
                        localContext.getString(RVideoListScreen.string.video_list_screen_description)
                },
            topBar = {
                ListScreenAppBar(
                    videoListUIState = videoListUIState,
                    scrollAppBarBehaviour = scrollBehavior,
                    videoListMVI = videoListMVI,
                    modifier = modifier
                )
            },
            content = { innerPadding ->
                YouTubeVideoList(
                    videoListUIState = videoListUIState,
                    videoListMVI = videoListMVI,
                    innerPadding = innerPadding,
                    modifier = modifier
                )
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
}

@OptIn(ExperimentalMaterial3Api::class)
@PreviewLightDark
@Composable
fun VideoListScreenPreview() {
    BlueTubeComposeTheme {
        Surface {

            VideoListScreen(
                videoListUIStateFlow = MutableStateFlow(VideoListUIState()),
                videoListMVI = PREVIEW_VIDEO_LIST_MVI,
                videoSearchQuery = "",
                modifier = Modifier,
                bottomNavigation = {  }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@PreviewLightDark
@Composable
fun VideoSearchListScreenPreview() {
    BlueTubeComposeTheme {
        Surface {
            VideoListScreen(
                videoListUIStateFlow = MutableStateFlow(VideoListUIState()),
                videoListMVI = PREVIEW_VIDEO_LIST_MVI,
                videoSearchQuery = "",
                modifier = Modifier,
                bottomNavigation = {  }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(device = "spec:parent=pixel_9,orientation=landscape")
@Preview(widthDp = 1200, heightDp = 800)
@Composable
fun VideoSearchListScreenLandscapePreview() {
    BlueTubeComposeTheme {
        Surface {
            VideoListScreen(
                videoListUIStateFlow = MutableStateFlow(VideoListUIState()),
                videoListMVI = PREVIEW_VIDEO_LIST_MVI,
                videoSearchQuery = "",
                modifier = Modifier,
                bottomNavigation = {  }
            )
        }
    }
}
