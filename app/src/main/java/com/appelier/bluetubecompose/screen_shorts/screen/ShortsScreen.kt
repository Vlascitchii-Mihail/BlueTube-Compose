package com.appelier.bluetubecompose.screen_shorts.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import com.appelier.bluetubecompose.R
import com.appelier.bluetubecompose.core.core_ui.views.BodyText
import com.appelier.bluetubecompose.utils.Tags

@Composable
fun ShortsScreen() {

    Scaffold(
        modifier = Modifier.testTag(Tags.SHORTS_SCREEN),
        content = { paddingValue ->
            Column(modifier = Modifier.fillMaxSize().padding(paddingValue)) {
                BodyText(
                    text = stringResource(id = R.string.shorts_screen_descr),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        },
    )
}