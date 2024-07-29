package com.appelier.bluetubecompose

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.appelier.bluetubecompose.core.core_ui.theme.BlueTubeComposeTheme
import com.appelier.bluetubecompose.navigation.Navigation

@Composable
fun BlueTubeApp(navController: NavHostController = rememberNavController()) {
    BlueTubeComposeTheme {
        Navigation(navController)
    }
}