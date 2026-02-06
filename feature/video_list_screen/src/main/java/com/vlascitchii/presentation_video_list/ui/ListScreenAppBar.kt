package com.vlascitchii.presentation_video_list.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.vlascitchii.presentation_common.ui.screen.mvi.CommonMVI
import com.vlascitchii.video_list_screen.R
import com.vlascitchii.presentation_common.ui.video_list.state.SearchState
import com.vlascitchii.presentation_common.ui.video_list.state.UiVideoListAction
import com.vlascitchii.presentation_common.ui.video_list.state.VideoListNavigationEvent
import com.vlascitchii.presentation_common.ui.video_list.state.VideoListUIState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreenAppBar(
    videoListUIState: VideoListUIState,
    scrollAppBarBehaviour: TopAppBarScrollBehavior,
    videoListMVI: CommonMVI<UiVideoListAction, VideoListNavigationEvent>,
    modifier: Modifier = Modifier,
    searchIcon: ImageVector = Icons.Filled.Search,
    appBarTitle: String = stringResource(id = R.string.appbar_title),
) {

    when (videoListUIState.searchBarState) {
        SearchState.CLOSED -> {
            BlueTubeTopAppBar(
                title = appBarTitle,
                icon = searchIcon,
                scrollBehavior = scrollAppBarBehaviour,
                videoListMVI = videoListMVI,
                modifier
            )
        }

        SearchState.OPENED -> {
            SearchAppBarBlueTube(
                searchText = videoListUIState.searchTextState,
                videoListMVI = videoListMVI,
                scrollAppBarBehaviour = scrollAppBarBehaviour,
                modifier
            )
        }
    }
}
