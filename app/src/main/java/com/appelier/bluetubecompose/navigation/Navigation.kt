package com.appelier.bluetubecompose.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.appelier.bluetubecompose.core.core_database.CustomNavTypeSerializer
import com.appelier.bluetubecompose.core.core_ui.views.BlueTubeBottomNavigation
import com.appelier.bluetubecompose.screen_player.PlayerScreen
import com.appelier.bluetubecompose.screen_player.VideoPlayerViewModel
import com.appelier.bluetubecompose.screen_settings.SettingsScreen
import com.appelier.bluetubecompose.screen_shorts.screen.ShortsScreen
import com.appelier.bluetubecompose.screen_shorts.screen.ShortsViewModel
import com.appelier.bluetubecompose.screen_video_list.model.videos.YoutubeVideo
import com.appelier.bluetubecompose.screen_video_list.screen.VideoListScreen
import com.appelier.bluetubecompose.screen_video_list.screen.VideoListViewModel
import com.appelier.bluetubecompose.utils.NavigationTags.NAVIGATION
import kotlin.reflect.typeOf

@Composable
fun Navigation(
    navController: NavHostController,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        NavHost(
            navController = navController,
            startDestination = ScreenType.VideoList,
            modifier = Modifier
                .testTag(NAVIGATION)
                .weight(1F)
        ) {
            composable<ScreenType.VideoList> {
                val videoListViewModel: VideoListViewModel = hiltViewModel()
                videoListViewModel.getVideosFlow()
                VideoListScreen(
                    navigateToPlayerScreen = { video: YoutubeVideo ->
                        navController.navigate(ScreenType.PlayerScreen(video))
                    },
                    searchViewState = videoListViewModel.searchState.value,
                    searchTextState = videoListViewModel.searchTextState.value,
                    videos = videoListViewModel.videoStateFlow,
                    updateSearchTextState = { searchText ->
                        videoListViewModel.updateSearchTextState(
                            searchText
                        )
                    },
                    updateSearchState = { searchState ->
                        videoListViewModel.updateSearchState(
                            searchState
                        )
                    },
                    getSearchVideosFlow = { searchText ->
                        videoListViewModel.getSearchVideosFlow(searchText)
                    }
                )
            }
            composable<ScreenType.PlayerScreen>(
                typeMap = mapOf(
                    typeOf<YoutubeVideo>() to CustomNavTypeSerializer(
                        YoutubeVideo::class.java,
                        YoutubeVideo.serializer()
                    )
                )
            ) { navBackStackEntry ->
                val arg = navBackStackEntry.toRoute<ScreenType.PlayerScreen>().video
                val playerScreenViewModel: VideoPlayerViewModel = hiltViewModel()
                playerScreenViewModel.getSearchedRelatedVideos(arg.snippet.title)
                PlayerScreen(
                    video = arg,
                    relatedVideos = playerScreenViewModel.relatedVideoStateFlow,
                    navigateToPlayerScreen = { video: YoutubeVideo ->
                        navController.navigate(ScreenType.PlayerScreen(video)) {
                            launchSingleTop = true
                        }
                    },
                    popBackStack = { navController.popBackStack() }
                )
            }
            composable<ScreenType.ShortsScreen> {
                val shortsViewModel: ShortsViewModel = hiltViewModel()
                shortsViewModel.getShorts()
                ShortsScreen(
                    shortsViewModel.shortsVideoState
                )
            }
            composable<ScreenType.SettingsScreen> {
                SettingsScreen()
            }
        }
        BlueTubeBottomNavigation(navController)
    }
}
