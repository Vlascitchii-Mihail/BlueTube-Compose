package com.vlascitchii.bluetubecompose

import androidx.compose.runtime.Composable
import com.vlascitchii.bluetubecompose.navigation.BlueTubeNavigation
import com.vlascitchii.presentation_common.ui.theme.BlueTubeComposeTheme

@Composable
fun BlueTubeApp() {
    BlueTubeComposeTheme {
        BlueTubeNavigation()
    }
}
