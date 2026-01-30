package com.vlascitchii.presentation_video_list.screen.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vlascitchii.video_list_screen.R
import com.vlascitchii.presentation_video_list.screen.state.SearchState
import kotlinx.coroutines.flow.StateFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreenAppBar(
    searchViewState: StateFlow<SearchState>,
    searchTextState: StateFlow<TextFieldValue>,
    scrollAppBarBehaviour: TopAppBarScrollBehavior,
    onTextChange: (String) -> Unit,
    onSearchClicked: () -> Unit,
    modifier: Modifier = Modifier,
    updateSearchState: (SearchState) -> Unit,
    searchIcon: ImageVector = Icons.Filled.Search,
    appBarTitle: String = stringResource(id = R.string.appbar_title),
) {
    val searchViewToolbarState by searchViewState.collectAsStateWithLifecycle()

    when (searchViewToolbarState) {
        SearchState.CLOSED -> {
            BlueTubeTopAppBar(
                title = appBarTitle,
                icon = searchIcon,
                scrollBehavior = scrollAppBarBehaviour,
                updateSearchState = updateSearchState,
                modifier
            )
        }

        SearchState.OPENED -> {
            SearchAppBarBlueTube(
                searchText = searchTextState,
                onTextChange = onTextChange,
                updateSearchState = updateSearchState,
                onSearchClicked = onSearchClicked,
                scrollAppBarBehaviour = scrollAppBarBehaviour,
                modifier
            )
        }
    }
}