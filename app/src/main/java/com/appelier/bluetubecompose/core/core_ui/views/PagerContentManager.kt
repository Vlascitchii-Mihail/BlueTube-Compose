package com.appelier.bluetubecompose.core.core_ui.views

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.appelier.bluetubecompose.R
import com.appelier.bluetubecompose.core.core_ui.views.video_list_screen.PaginationErrorItem
import com.appelier.bluetubecompose.screen_video_list.model.videos.YoutubeVideo

@Composable
fun PagerContentManager(
    videoState: LazyPagingItems<YoutubeVideo>,
    contentList: @Composable (videoState: LazyPagingItems<YoutubeVideo>) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        val refreshLoadState = videoState.loadState.refresh
        Log.d("recomposition", "PagerContentManager() called")

        when {
            refreshLoadState is LoadState.NotLoading && videoState.itemCount > 0 -> contentList.invoke(videoState)
            refreshLoadState is LoadState.Loading && videoState.itemCount > 0 ->
                contentList.invoke(videoState)
            refreshLoadState is LoadState.Loading -> CircularProgressIndicator(
                Modifier
                    .align(Alignment.Center)
                    .height(48.dp)
            )
            refreshLoadState is LoadState.Error || videoState.itemCount == 0 ->
                PaginationErrorItem(
                errorText = castLoadState(refreshLoadState, stringResource(id = R.string.error_msg_empty_list) ),
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