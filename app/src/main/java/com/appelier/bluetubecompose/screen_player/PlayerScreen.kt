package com.appelier.bluetubecompose.screen_player

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.appelier.bluetubecompose.R
import com.appelier.bluetubecompose.ui.views.BodyText

@Composable
fun PlayerScreen(navController: NavController, videoId: String = "") {
    Scaffold(
        content = { paddingValue ->
            Column(modifier = Modifier.fillMaxSize().padding(paddingValue)) {
                BodyText(
                    text = stringResource(id = R.string.player_screen_descr),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        },
    )
}