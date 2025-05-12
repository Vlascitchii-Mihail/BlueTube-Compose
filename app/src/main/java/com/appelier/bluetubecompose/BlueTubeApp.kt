package com.appelier.bluetubecompose

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.appelier.bluetubecompose.navigation.YouTubeNavigation
import com.vlascitchii.presentation_common.ui.theme.BlueTubeComposeTheme

@Composable
fun BlueTubeApp(navController: NavHostController = rememberNavController()) {
    BlueTubeComposeTheme {
        YouTubeNavigation(navController)
    }
}