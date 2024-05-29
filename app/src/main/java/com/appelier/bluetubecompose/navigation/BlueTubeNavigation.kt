package com.appelier.bluetubecompose.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.appelier.bluetubecompose.screen_player.PlayerScreen
import com.appelier.bluetubecompose.screen_settings.SettingsScreen
import com.appelier.bluetubecompose.screen_shorts.screen.ShortsScreen
import com.appelier.bluetubecompose.screen_video_list.screen.VideoListScreen
import com.appelier.bluetubecompose.utils.Tags

@Composable
fun BlueTubeNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.VideoList,
        modifier = Modifier.testTag(Tags.NAVIGATION)
    ) {
        composable<Screen.VideoList> {
            VideoListScreen(navController = navController)
        }
        composable<Screen.PlayerScreen> {
            PlayerScreen(navController = navController)
        }
        composable<Screen.ShortsScreen> { 
            ShortsScreen()
        }
        composable<Screen.SettingsScreen> {
            SettingsScreen()
        }
    }
}