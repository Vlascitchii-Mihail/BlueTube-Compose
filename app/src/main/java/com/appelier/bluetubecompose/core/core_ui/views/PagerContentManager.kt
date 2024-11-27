package com.appelier.bluetubecompose.core.core_ui.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.appelier.bluetubecompose.R
import com.appelier.bluetubecompose.core.core_ui.views.video_list_screen.PaginationErrorItem
import com.appelier.bluetubecompose.screen_video_list.model.videos.YoutubeVideo
import com.appelier.bluetubecompose.utils.Core

@Composable
fun PagerContentManager(
    videoState: LazyPagingItems<YoutubeVideo>,
    contentList: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        val refreshLoadState = videoState.loadState.refresh

        when (refreshLoadState) {
            is LoadState.NotLoading -> contentList.invoke()

            is LoadState.Loading -> CircularProgressIndicator(
                Modifier
                    .align(Alignment.Center)
                    .height(48.dp)
                    .testTag(Core.CIRCULAR_PROGRESS_INDICATOR)
            )

            is LoadState.Error -> PaginationErrorItem(
                errorText = castLoadState(refreshLoadState, stringResource(id = R.string.error_msg_empty_list)),
                onRetryClick = { videoState.refresh() }
            )
        }
    }
}

fun castLoadState(loadState: LoadState, errorMsg: String): String {
    return if (loadState is LoadState.Error) {
        loadState.error.message ?: errorMsg
    } else errorMsg
}