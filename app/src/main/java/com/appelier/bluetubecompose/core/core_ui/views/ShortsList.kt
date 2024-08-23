package com.appelier.bluetubecompose.core.core_ui.views

import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import com.appelier.bluetubecompose.screen_video_list.model.videos.YoutubeVideo

@Composable
fun ShortsList(
    videos: LazyPagingItems<YoutubeVideo>
) {
    val pagerState = rememberPagerState(pageCount = { videos.itemCount })

    VerticalPager(
        state = pagerState,
        key = videos.itemKey(),
        pageSize = PageSize.Fill,
    ) { index: Int ->
        videos[index]?.let {
                ShortsItem(
                    youTubeVideo = it,
                    index,
                    pagerState.currentPage
                )
            }
        }
}