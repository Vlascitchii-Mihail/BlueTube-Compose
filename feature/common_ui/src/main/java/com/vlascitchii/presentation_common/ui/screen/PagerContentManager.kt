package com.vlascitchii.presentation_common.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.vlascitchii.common_ui.R
import com.vlascitchii.presentation_common.model.videos.YoutubeVideoUiModel
import com.vlascitchii.presentation_common.ui.error.PaginationErrorItem

@Composable
fun PagerContentManager(
    videoState: LazyPagingItems<YoutubeVideoUiModel>,
    contentList: @Composable () -> Unit,
    innerPadding: PaddingValues = PaddingValues(),
    modifier: Modifier = Modifier
) {
    val circularProgressIndicatorDescription = stringResource(R.string.circular_progress_indicator)

    val keyboardController = LocalSoftwareKeyboardController.current
    val keyboardScrollHider = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                keyboardController?.hide()
                return Offset.Zero
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .nestedScroll(keyboardScrollHider)
            .padding(innerPadding),
        contentAlignment = Alignment.Center
    ) {
        val refreshLoadState = videoState.loadState.refresh

        when (true) {
            (refreshLoadState is LoadState.NotLoading || videoState.itemCount > 0) -> {
                PullToRefresh(
                    scrollableView = {
                        contentList.invoke()
                    },
                    onRefresh = { videoState.refresh() },
                    modifier = modifier
                )
            }
            (refreshLoadState is LoadState.Loading && videoState.itemCount == 0) -> CircularProgressIndicator(
                Modifier
                    .align(Alignment.Center)
                    .height(dimensionResource(R.dimen.height_medium_48))
                    .semantics { contentDescription = circularProgressIndicatorDescription }
            )
            else -> {
                PaginationErrorItem(
                    errorText = castLoadState(refreshLoadState, stringResource(id = R.string.error_msg_empty_list)),
                    onRetryClick = { videoState.refresh() }
                )
            }
        }
    }
}

fun castLoadState(loadState: LoadState, errorMsg: String): String {
    return if (loadState is LoadState.Error) {
        loadState.error.message ?: errorMsg
    } else errorMsg
}
