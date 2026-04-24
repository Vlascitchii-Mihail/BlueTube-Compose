package com.vlascitchii.bluetubecompose.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.vlascitchii.bluetubecompose.navigation.bottom_navigation.BlueTubeBottomNavigation
import com.vlascitchii.presentation_common.ui.state_common.UiSingleEvent
import com.vlascitchii.presentation_common.ui.video_list.state.VideoListNavigationEvent
import com.vlascitchii.presentation_player.screen.screen.PlayerMviHandler
import com.vlascitchii.presentation_player.screen.screen.PlayerScreen
import com.vlascitchii.presentation_player.screen.screen.VideoPlayerViewModel
import com.vlascitchii.presentation_player.screen.state.PlayerNavigationEvent
import com.vlascitchii.presentation_shorts.screen_shorts.screen.ShortsMviHandler
import com.vlascitchii.presentation_shorts.screen_shorts.screen.ShortsScreen
import com.vlascitchii.presentation_shorts.screen_shorts.screen.ShortsViewModel
import com.vlascitchii.presentation_video_list.screen.VideoListMviHandler
import com.vlascitchii.presentation_video_list.screen.VideoListScreen
import com.vlascitchii.presentation_video_list.screen.VideoListViewModel
import com.vlascitchii.presenttion_settings.screen_settings.SettingsScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BlueTubeNavigation(
    modifier: Modifier = Modifier,
    backStack: NavBackStack<NavKey> = rememberNavBackStack(ScreenType.VideoList())
) {

    val navigationHandler: (UiSingleEvent) -> Unit = { navEvent: UiSingleEvent ->
        when (navEvent) {
            is VideoListNavigationEvent -> handleVideoListNavigationEvent(navEvent, backStack)
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
                val videoListMviHandler = VideoListMviHandler(
                    videoListViewModel =  videoListViewModel,
                    navigationHandler = navigationHandler,
                )

                VideoListScreen(
                    videoListUIStateFlow = videoListViewModel.videoListUIStateFlow,
                    videoListMviHandler = videoListMviHandler,
                    videoSearchQuery = key.query,
                    modifier = modifier,
                    bottomNavigation = { BlueTubeBottomNavigation(backStack = backStack) }
                )
            }
            entry<ScreenType.PlayerScreen> { key: ScreenType.PlayerScreen ->
                val playerScreenViewModel: VideoPlayerViewModel = hiltViewModel()
                val playerMVIHandler = PlayerMviHandler(
                    videoPlayerViewModel = playerScreenViewModel,
                    navigationHandler = navigationHandler,
                )
                val video = key.video

                PlayerScreen(
                    video = video,
                    playerStateFlow = playerScreenViewModel.playerStateFlow,
                    playerMVI = playerMVIHandler,
                    playbackPosition = playerScreenViewModel.videoPlaybackPosition,
                    modifier = modifier
                )
            }
            entry<ScreenType.ShortsScreen> {
                val shortsViewModel: ShortsViewModel = hiltViewModel()
                val shortsMviHandler = ShortsMviHandler(
                    shortsViewModel = shortsViewModel,
                    navigationHandler = navigationHandler
                )

                ShortsScreen(
                    shortsStateFlow = shortsViewModel.shortsUiStateFlow,
                    videoQueue = shortsViewModel.videoQueue,
                    shortsMviHandler = shortsMviHandler,
                )
            }
            entry<ScreenType.SettingsScreen> {
                SettingsScreen(
                    modifier = modifier,
                    bottomNavigation = { BlueTubeBottomNavigation(backStack = backStack) }
                )
            }
        }
    )
}

private fun handleVideoListNavigationEvent(singleVideoListNavigationEvent: VideoListNavigationEvent, backStack: NavBackStack<NavKey>) {
    when (singleVideoListNavigationEvent) {
        is VideoListNavigationEvent.NavigationPlayerScreenEvent -> {
            backStack.add(ScreenType.PlayerScreen(singleVideoListNavigationEvent.video))
        }
        is VideoListNavigationEvent.NavigationToParticularSearchedVideoList -> {
            backStack.add(ScreenType.VideoList(singleVideoListNavigationEvent.searchVideoListQuery))
        }
    }
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
