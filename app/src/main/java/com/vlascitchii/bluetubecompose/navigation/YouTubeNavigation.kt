package com.vlascitchii.bluetubecompose.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.vlascitchii.presentation_common.model.videos.YoutubeVideoUiModel
import com.vlascitchii.presentation_common.ui.bottom_navigation.BlueTubeBottomNavigation
import com.vlascitchii.presentation_common.ui.video_list.YouTubeVideoList
import com.vlascitchii.presentation_player.screen_player.screen.PlayerScreen
import com.vlascitchii.presentation_player.screen_player.screen.VideoPlayerViewModel
import com.vlascitchii.presentation_shorts.screen_shorts.screen.ShortsScreen
import com.vlascitchii.presentation_shorts.screen_shorts.screen.ShortsViewModel
import com.vlascitchii.presentation_video_list.screen.VideoListScreen
import com.vlascitchii.presentation_video_list.screen.VideoListViewModel
import com.vlascitchii.presentation_video_list.screen.state.SearchState
import com.vlascitchii.presentation_video_list.screen.state.UiVideoListAction
import com.vlascitchii.presentation_video_list.screen.state.UiVideoListAction.TypeInSearchAppBarTextField
import com.vlascitchii.presentation_video_list.screen.ui.ListScreenAppBar
import com.vlascitchii.presenttion_settings.screen_settings.SettingsScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YouTubeNavigation(
    modifier: Modifier = Modifier
) {
    val backStack: NavBackStack<NavKey> = rememberNavBackStack(ScreenType.VideoList())

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        modifier = modifier.fillMaxSize(),
        entryProvider = entryProvider {
            entry<ScreenType.VideoList> { key: ScreenType.VideoList ->
                val videoListViewModel: VideoListViewModel = hiltViewModel()

                val scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

                VideoListScreen(
                    connectivityStatus = videoListViewModel.connectivityObserver,
                    scrollBehavior = scrollBehavior,
                    listScreenAppBar = {
                        ListScreenAppBar(
                            searchViewState = videoListViewModel.searchBarState,
                            searchTextState = videoListViewModel.searchTextState,
                            scrollAppBarBehaviour = scrollBehavior,
                            onTextChange = { searchInput: String ->
                                videoListViewModel.submitAction(TypeInSearchAppBarTextField(searchInput))
                            },
                            onSearchClicked = {
                                backStack.add(ScreenType.VideoList(videoListViewModel.searchTextState.value.text))
                            },
                            updateSearchState = { newSearchState: SearchState ->
                                videoListViewModel.submitAction(
                                    UiVideoListAction.ChangeSearchBarAppearance(
                                        newSearchState
                                    )
                                )
                            },
                            modifier = modifier
                        )
                    },
                    videoList = { padding: PaddingValues ->
                        YouTubeVideoList(
                            initVideosList = { videoListViewModel.submitAction(UiVideoListAction.GetVideo(key.query) ) },
                            videosFlow = videoListViewModel.uiStateFlow,
                            innerPadding = padding,
                            navigateToPlayerScreen = { video: YoutubeVideoUiModel ->
                                backStack.add(ScreenType.PlayerScreen(video))
                            },
                            modifier = modifier
                        )
                    },
                    modifier = modifier,
                    bottomNavigation = {
                        BlueTubeBottomNavigation(
                            currentDestinationName = getCurrentNavKey(backStack.lastOrNull()).name,
                            navigateToVideo = { backStack.add(ScreenType.VideoList()) },
                            navigateToShorts = { backStack.add(ScreenType.ShortsScreen) },
                            navigateToSettings = { backStack.add(ScreenType.SettingsScreen) }
                        )
                    }
                )
            }
            entry<ScreenType.PlayerScreen> { key: ScreenType.PlayerScreen ->
                val playerScreenViewModel: VideoPlayerViewModel = hiltViewModel()
                val video = key.video

                PlayerScreen(
                    video = video,
                    relatedVideos = playerScreenViewModel.relatedVideoStateFlow,
                    getRelatedVideos = {
                        playerScreenViewModel.getSearchedRelatedVideos(
                            video.snippet.title
                        )
                    },
                    isVideoPlayingFlow = playerScreenViewModel.isVideoPlaysFlow,
                    updateVideoIsPlayState = { isPlaying ->
                        playerScreenViewModel.updateVideoPlayState(
                            isPlaying
                        )
                    },
                    navigateToPlayerScreen = { video: YoutubeVideoUiModel ->
                        backStack.removeLastOrNull()
                        backStack.add(ScreenType.PlayerScreen(video))
                    },
                    popBackStack = { backStack.removeLastOrNull() },
                    playbackPosition = playerScreenViewModel.videoPlaybackPosition,
                    updatePlaybackPosition = { playbackPosition: Float ->
                        playerScreenViewModel.updatePlaybackPosition(
                            playbackPosition
                        )
                    },
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
                    connectivityStatus = playerScreenViewModel.connectivityObserver,
                    bottomNavigation = {
                        BlueTubeBottomNavigation(
                            currentDestinationName = getCurrentNavKey(backStack.lastOrNull()).name,
                            navigateToVideo = { backStack.add(ScreenType.VideoList()) },
                            navigateToShorts = { backStack.add(ScreenType.ShortsScreen) },
                            navigateToSettings = { backStack.add(ScreenType.SettingsScreen) }
                        )
                    },
                    modifier = modifier
                )
            }
            entry<ScreenType.ShortsScreen> { key: ScreenType.ShortsScreen ->
                val shortsViewModel: ShortsViewModel = hiltViewModel()

                ShortsScreen(
                    shortsStateFlow = shortsViewModel.shortsStateFlow,
                    videoQueue = shortsViewModel.videoQueue,
                    listenToVideoQueue = { shortsViewModel.listenToVideoQueue() },
                    networkConnectivityStatus = shortsViewModel.connectivityObserver,
                )
            }
            entry<ScreenType.SettingsScreen> { key: ScreenType.SettingsScreen ->
                SettingsScreen(
                    modifier = modifier,
                    bottomNavigation = {
                        BlueTubeBottomNavigation(
                            currentDestinationName = getCurrentNavKey(backStack.lastOrNull()).name,
                            navigateToVideo = { backStack.add(ScreenType.VideoList()) },
                            navigateToShorts = { backStack.add(ScreenType.ShortsScreen) },
                            navigateToSettings = { backStack.add(ScreenType.SettingsScreen) }
                        )
                    }
                )
            }
        }
    )
}

private fun getCurrentNavKey(lastNavigationKey: NavKey?): ScreenType {
    return when (lastNavigationKey) {
        is ScreenType.VideoList -> lastNavigationKey
        is ScreenType.ShortsScreen -> lastNavigationKey
        is ScreenType.PlayerScreen -> lastNavigationKey
        else -> lastNavigationKey as ScreenType.SettingsScreen
    }
}
