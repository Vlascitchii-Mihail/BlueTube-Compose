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
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.vlascitchii.bluetubecompose.navigation.bottom_navigation.BlueTubeBottomNavigation
import com.vlascitchii.presentation_common.model.videos.YoutubeVideoUiModel
import com.vlascitchii.presentation_common.ui.state.UiSingleEvent
import com.vlascitchii.presentation_common.ui.video_list.YouTubeVideoList
import com.vlascitchii.presentation_player.screen_player.screen.PlayerMVI
import com.vlascitchii.presentation_player.screen_player.screen.PlayerScreen
import com.vlascitchii.presentation_player.screen_player.screen.VideoPlayerViewModel
import com.vlascitchii.presentation_player.screen_player.state.PlayerNavigationEvent
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
fun BlueTubeNavigation(
    modifier: Modifier = Modifier,
    backStack: NavBackStack<NavKey> = rememberNavBackStack(ScreenType.VideoList())
) {

    val navigationHandler: (UiSingleEvent) -> Unit = { navEvent: UiSingleEvent ->
        when (navEvent) {
            is PlayerNavigationEvent -> handlePlayerNavEvent(navEvent, backStack)
        }
    }

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
                    bottomNavigation = { BlueTubeBottomNavigation(backStack = backStack) }
                )
            }
            entry<ScreenType.PlayerScreen> { key: ScreenType.PlayerScreen ->
                val playerScreenViewModel: VideoPlayerViewModel = hiltViewModel()
                val playerMVI = PlayerMVI(
                    videoPlayerViewModel = playerScreenViewModel,
                    navigationHandler = navigationHandler,
                    coroutineScope = playerScreenViewModel.viewModelScope
                )
                val video = key.video

                PlayerScreen(
                    video = video,
                    playerStateFlow = playerScreenViewModel.playerStateFlow,
                    playerMVI = playerMVI,
                    playbackPosition = playerScreenViewModel.videoPlaybackPosition,
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
                    bottomNavigation = { BlueTubeBottomNavigation(backStack = backStack) }
                )
            }
        }
    )
}

private fun handlePlayerNavEvent(singlePlayerEvent: PlayerNavigationEvent, backStack: NavBackStack<NavKey>) {
    when(singlePlayerEvent) {
        is PlayerNavigationEvent.NavigationPlayerScreenEvent -> {
            backStack.removeLastOrNull()
            backStack.add(ScreenType.PlayerScreen(singlePlayerEvent.video))
        }
        is PlayerNavigationEvent.PopBackStackEvent -> backStack.removeLastOrNull()
    }
}
