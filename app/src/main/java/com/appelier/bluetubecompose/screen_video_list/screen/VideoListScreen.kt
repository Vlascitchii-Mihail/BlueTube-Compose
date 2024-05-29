package com.appelier.bluetubecompose.screen_video_list.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.appelier.bluetubecompose.R
import com.appelier.bluetubecompose.ui.theme.BlueTubeComposeTheme
import com.appelier.bluetubecompose.ui.views.BodyText
import com.appelier.bluetubecompose.ui.views.TubeButton
import com.appelier.bluetubecompose.utils.Tags

@Composable
fun VideoListScreen(navController: NavController) {

    Scaffold(
        modifier = Modifier.testTag(Tags.VIDEO_LIST_SCREEN),
        content = { paddingValue ->
            Column(modifier = Modifier.fillMaxSize().padding(paddingValue)) {
                BodyText(
                    text = stringResource(id = R.string.video_list_screen_descr),
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.size(8.dp))

                TubeButton {}
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_search_24),
                    contentDescription = stringResource(R.string.video_list_screen_descr),
                )
            }
        }
    )
}

@Preview
@Composable
private fun VideoListScreenPreview() {
    BlueTubeComposeTheme {
        VideoListScreen(rememberNavController())
    }
}