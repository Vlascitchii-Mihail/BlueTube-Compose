package com.appelier.bluetubecompose.core.core_ui.views

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
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
import com.appelier.bluetubecompose.screen_video_list.model.videos.YoutubeVideo
import java.lang.ClassCastException

@Composable
fun PagerContentSupplier(
    videoState: LazyPagingItems<YoutubeVideo>,
    shortsList: @Composable (videoState: LazyPagingItems<YoutubeVideo>) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        val refreshLoadState = videoState.loadState.refresh
        Log.d("Shorts", "videoState.itemCount = ${videoState.itemCount}")

        when {
            refreshLoadState is LoadState.NotLoading && videoState.itemCount > 0 -> shortsList.invoke(videoState)
            refreshLoadState is LoadState.Loading && videoState.itemCount > 0 ->
                shortsList.invoke(videoState)
            refreshLoadState is LoadState.Loading -> CircularProgressIndicator(
                modifier
                    .align(Alignment.Center)
                    .height(48.dp)
            )
            refreshLoadState is LoadState.Error || videoState.itemCount == 0 ->
                PaginationErrorItem(
                errorText = castLoadState(refreshLoadState, stringResource(id = R.string.error_msg_empty_list) ),
                modifier = modifier,
                onRetryClick = { videoState.refresh() }
            )
        }
    }
}

fun castLoadState(loadState: LoadState, errorMsg: String): String {
    return try {
        (loadState as LoadState.Error).error.message ?: errorMsg
    } catch (ex: ClassCastException) {
        ex.printStackTrace()
        errorMsg
    }
}