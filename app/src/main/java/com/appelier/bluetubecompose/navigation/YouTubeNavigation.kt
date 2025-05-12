package com.appelier.bluetubecompose.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.appelier.bluetubecompose.LocalWindowSizeClass
import com.appelier.bluetubecompose.R
import com.appelier.bluetubecompose.navigation.bottom_navigation.BlueTubeBottomNavigation
import com.vlascitchii.data_local.enetity.video_list.videos.YoutubeVideoEntity
import com.vlascitchii.presentation_common.entity.videos.YoutubeVideoUiModel
import com.vlascitchii.presentation_common.network_observer.ObserveAsEvents
import com.vlascitchii.presentation_common.utils.SnackbarController
import com.vlascitchii.presentation_player.screen_player.screen.PlayerScreen
import com.vlascitchii.presentation_player.screen_player.screen.VideoPlayerViewModel
import com.vlascitchii.presentation_shorts.screen_shorts.screen.ShortsScreen
import com.vlascitchii.presentation_shorts.screen_shorts.screen.ShortsViewModel
import com.vlascitchii.presentation_video_list.screen_video_list.screen.VideoListScreen
import com.vlascitchii.presentation_video_list.screen_video_list.screen.VideoListViewModel
import com.vlascitchii.presentation_video_list.video_list_screen.ListScreenAppBar
import com.vlascitchii.presentation_common.ui.video_list.YouTubeVideoList
import com.vlascitchii.presenttion_settings.screen_settings.SettingsScreen
import kotlinx.coroutines.launch
import kotlin.reflect.typeOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YouTubeNavigation(
    navController: NavHostController
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    ObserveAsEvents(
        flow = SnackbarController.events,
        key1 = snackbarHostState,
        onEvent = { event ->
            scope.launch {

                snackbarHostState.currentSnackbarData?.dismiss()

                val result = snackbarHostState.showSnackbar(
                    message = event.message,
                    actionLabel = event.action?.name,
                    duration = SnackbarDuration.Long
                )

                if (result == SnackbarResult.ActionPerformed) {
                    event.action?.action?.invoke()
                }
            }
        }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1F)
        ) {
            NavHost(
                navController = navController,
                startDestination = ScreenType.VideoList,
                modifier = Modifier
//                    .testTag(NAVIGATION)
                    .align(Alignment.TopCenter)
            ) {
                composable<ScreenType.VideoList> {
                    val videoListViewModel: VideoListViewModel = hiltViewModel()
                    //check if this function calls many times
                    videoListViewModel.fetchPopularVideos()
                    VideoListScreen(
                        connectivityStatus = videoListViewModel.connectivityObserver,
                        listScreenAppBar = { scrollAppBarBehaviour: TopAppBarScrollBehavior ->
                            ListScreenAppBar(
                                searchViewState = videoListViewModel.searchState,
                                searchTextState = videoListViewModel.searchTextState,
                                scrollAppBarBehaviour = scrollAppBarBehaviour,
                                onTextChange = { searchInput: String ->
                                    videoListViewModel.updateSearchTextState(
                                        searchInput
                                    )
                                },
                                onSearchClicked = { videoListViewModel.setSearchVideosFlow() },
                                updateSearchState = { newSearchState: com.vlascitchii.presentation_video_list.search_video.SearchState ->
                                    videoListViewModel.updateSearchState(
                                        newSearchState
                                    )
                                }
                            )
                        },
                        videoList = { padding: PaddingValues ->
                            val windowSizeClass = LocalWindowSizeClass.current.widthSizeClass
                            YouTubeVideoList(
                                getVideoState = { videoListViewModel.getVideos() },
                                innerPadding = padding,
                                localWindowSizeClass = windowSizeClass,
                                navigateToPlayerScreen = { video: YoutubeVideoUiModel ->
                                    navController.navigate(ScreenType.PlayerScreen(video))
                                }
                            )
                        }
                    )
                }
                composable<ScreenType.PlayerScreen>(
                    typeMap = mapOf(
                        typeOf<YoutubeVideoEntity>() to CustomNavTypeSerializer(
                            YoutubeVideoUiModel::class.java,
                            YoutubeVideoUiModel.serializer()
                        )
                    )
                ) { navBackStackEntry ->
                    val arg = navBackStackEntry.toRoute<ScreenType.PlayerScreen>().video
                    val playerScreenViewModel: VideoPlayerViewModel = hiltViewModel()
                    PlayerScreen(
                        video = arg,
                        relatedVideos = playerScreenViewModel.relatedVideoStateFlow,
                        getRelatedVideos = { query: String ->
                            playerScreenViewModel.getSearchedRelatedVideos(
                                query
                            )
                        },
                        isVideoPlaysFlow = playerScreenViewModel.isVideoPlaysFlow,
                        updateVideoIsPlayState = { isPlaying ->
                            playerScreenViewModel.updateVideoIsPlayState(
                                isPlaying
                            )
                        },
                        navigateToPlayerScreen = { video: YoutubeVideoUiModel ->
                            navController.navigate(ScreenType.PlayerScreen(video)) {
                                launchSingleTop = true
                            }
                        },
                        popBackStack = { navController.popBackStack() },
                        updatePlaybackPosition = { playbackPosition: Float ->
                            playerScreenViewModel.updatePlaybackPosition(
                                playbackPosition
                            )
                        },
                        getPlaybackPosition = { playerScreenViewModel.getCurrentPlaybackPosition() },
                        playerOrientationState = playerScreenViewModel.playerOrientationState,
                        updatePlayerOrientationState = { newPlayerOrientationState ->
                            playerScreenViewModel.updatePlayerOrientationState(
                                newPlayerOrientationState
                            )
                        },
                        fullscreenWidgetIsClicked = playerScreenViewModel.fullscreenWidgetIsClicked,
                        setFullscreenWidgetIsClicked = { isClicked ->
                            playerScreenViewModel.setFullscreenWidgetIsClicked(
                                isClicked
                            )
                        },
                        connectivityStatus = playerScreenViewModel.getCurrentConnectivityState()
                    )
                }
                composable<ScreenType.ShortsScreen> {
                    val shortsViewModel: ShortsViewModel = hiltViewModel()
                    ShortsScreen(
                        shortsStateFlow = shortsViewModel.shortsStateFlow,
                        videoQueue = shortsViewModel.videoQueue,
                        listenToVideoQueue = { shortsViewModel.listenToVideoQueue() },
                        connectivityStatus = shortsViewModel.connectivityObserver,
                    )
                }
                composable<ScreenType.SettingsScreen> {
                    SettingsScreen()
                }
            }

            val snackbarDescription = stringResource(R.string.snackbar_description)
            SnackbarHost(
                modifier = Modifier.align(Alignment.BottomCenter)
                    .semantics { contentDescription = snackbarDescription },
                hostState = snackbarHostState
            )
        }

        BlueTubeBottomNavigation(navController)
    }
}
