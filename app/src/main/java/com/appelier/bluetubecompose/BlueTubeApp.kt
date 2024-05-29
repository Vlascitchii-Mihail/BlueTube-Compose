package com.appelier.bluetubecompose

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.appelier.bluetubecompose.navigation.BlueTubeNavigation
import com.appelier.bluetubecompose.core.core_ui.theme.BlueTubeComposeTheme
import com.appelier.bluetubecompose.core.core_ui.views.TubeBottomNavigation

@Composable
fun BlueTubeApp(navController: NavHostController) {
    BlueTubeComposeTheme {
        // A surface container using the 'background' color from the theme
        Scaffold(
            bottomBar = { TubeBottomNavigation(navController) }
        ) { paddingValue ->
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValue),
                color = MaterialTheme.colorScheme.background
            ) {
                BlueTubeNavigation(navController)
            }
        }
    }
}