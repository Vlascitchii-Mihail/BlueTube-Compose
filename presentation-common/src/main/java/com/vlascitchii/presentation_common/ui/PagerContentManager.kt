package com.vlascitchii.presentation_common.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.vlascitchii.presentation_common.R
import com.vlascitchii.presentation_common.entity.videos.YoutubeVideoUiModel
import com.vlascitchii.presentation_common.ui.error.PaginationErrorItem

@Composable
fun PagerContentManager(
    videoState: LazyPagingItems<YoutubeVideoUiModel>,
    contentList: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    val circularProgressIndicatorDescription = stringResource(R.string.circular_progress_indicator)

    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        val refreshLoadState = videoState.loadState.refresh

        when (refreshLoadState) {
            is LoadState.NotLoading -> contentList.invoke()

            is LoadState.Loading -> CircularProgressIndicator(
                Modifier
                    .align(Alignment.Center)
                    .height(48.dp)
                    .semantics { contentDescription = circularProgressIndicatorDescription }
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