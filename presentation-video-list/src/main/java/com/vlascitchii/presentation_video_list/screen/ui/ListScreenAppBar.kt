package com.vlascitchii.presentation_video_list.screen.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vlascitchii.presentation_video_list.R
import com.vlascitchii.presentation_video_list.util.state.SearchState
import kotlinx.coroutines.flow.StateFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreenAppBar(
    searchViewState: StateFlow<SearchState>,
    searchTextState: StateFlow<String>,
    scrollAppBarBehaviour: TopAppBarScrollBehavior,
    onTextChange: (String) -> Unit,
    onSearchClicked: () -> Unit,
    updateSearchState: (newSearchState: SearchState) -> Unit,
    searchIcon: ImageVector = Icons.Filled.Search,
    appBarTitle: String = stringResource(id = R.string.appbar_title)
) {
    val searchViewToolbarState by searchViewState.collectAsStateWithLifecycle()

    when (searchViewToolbarState) {
        SearchState.CLOSED -> {
            BlueTubeTopAppBar(
                title = appBarTitle,
                icon = searchIcon,
                scrollAppBarBehaviour,
                updateSearchState = updateSearchState
            )
        }

        SearchState.OPENED -> {
            SearchAppBarBlueTube(
                searchText = searchTextState,
                onTextChange = onTextChange,
                updateSearchState = updateSearchState,
                onSearchClicked = onSearchClicked
            )
        }
    }
}