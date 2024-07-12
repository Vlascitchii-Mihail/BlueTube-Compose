package com.appelier.bluetubecompose.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.appelier.bluetubecompose.screen_player.PlayerScreen
import com.appelier.bluetubecompose.screen_settings.SettingsScreen
import com.appelier.bluetubecompose.screen_shorts.screen.ShortsScreen
import com.appelier.bluetubecompose.screen_video_list.screen.VideoListScreen
import com.appelier.bluetubecompose.screen_video_list.screen.VideoListViewModel
import com.appelier.bluetubecompose.utils.NavigationTags.NAVIGATION

@Composable
fun Navigation(navController: NavHostController) {

    NavHost(
        navController = navController,
        startDestination = ScreenType.VideoList,
        modifier = Modifier.testTag(NAVIGATION)
    ) {
        composable<ScreenType.VideoList> {
            val videoListViewModel: VideoListViewModel = hiltViewModel()
            VideoListScreen(
                navController = navController,
                videoListViewModel.searchState.value,
                videoListViewModel.searchTextState.value,
                videoListViewModel.videos.value,
                { searchText -> videoListViewModel.updateSearchTextState(searchText) },
                { searchState -> videoListViewModel.updateSearchState(searchState) },
                { searchText -> videoListViewModel.getSearchVideosFlow(searchText) }
            )
        }
        composable<ScreenType.PlayerScreen> {
            PlayerScreen(navController = navController)
        }
        composable<ScreenType.ShortsScreen> {
            ShortsScreen()
        }
        composable<ScreenType.SettingsScreen> {
            SettingsScreen()
        }
    }
}