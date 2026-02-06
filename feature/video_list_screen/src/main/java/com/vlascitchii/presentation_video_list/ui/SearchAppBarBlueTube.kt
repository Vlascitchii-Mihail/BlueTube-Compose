@file:OptIn(ExperimentalMaterial3Api::class)

package com.vlascitchii.presentation_video_list.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.vlascitchii.presentation_common.ui.screen.mvi.CommonMVI
import com.vlascitchii.presentation_common.ui.screen.mvi.PREVIEW_VIDEO_LIST_MVI
import com.vlascitchii.presentation_common.ui.theme.BlueTubeComposeTheme
import com.vlascitchii.presentation_common.ui.video_list.state.SearchState
import com.vlascitchii.presentation_common.ui.video_list.state.UiVideoListAction
import com.vlascitchii.presentation_common.ui.video_list.state.VideoListNavigationEvent
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.vlascitchii.common_ui.R as RCommon
import com.vlascitchii.video_list_screen.R as RPresentationList

private const val SEARCH_INPUT_DELAY = 2000L

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchAppBarBlueTube(
    searchText: TextFieldValue,
    videoListMVI: CommonMVI<UiVideoListAction, VideoListNavigationEvent>,
    scrollAppBarBehaviour: TopAppBarScrollBehavior,
    modifier: Modifier,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }
    val inputScope = rememberCoroutineScope()
    var inputSearchJob by remember { mutableStateOf<Job?>(null) }
    var previousQuery: String = remember { "" }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    fun performSearch(searchInput: String) {
        if (searchInput != "" && searchInput != previousQuery) {
            inputSearchJob?.cancel()
            inputSearchJob = inputScope.launch {
                delay(SEARCH_INPUT_DELAY)
                videoListMVI.submitSingleNavigationEvent(
                    VideoListNavigationEvent.NavigationToParticularSearchedVideoList(searchInput)
                )
                previousQuery = searchInput
            }
        }
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .statusBarsPadding(),
        shadowElevation = dimensionResource(RCommon.dimen.elevation_medium_16),
        tonalElevation = dimensionResource(RCommon.dimen.elevation_medium_16)

    ) {
        TopAppBar(
            navigationIcon = {
                IconButton(
                    modifier = modifier.alpha(0.5f),
                    onClick = { }
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = stringResource(id = RPresentationList.string.appbar_search_icon_descr),
                    )
                }
            },
            title = {
                OutlinedTextField(
                    modifier = modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester),
                    value = searchText,
                    onValueChange = { input ->
                        videoListMVI.submitAction(UiVideoListAction.TypeInSearchAppBarTextField(input.text))
                        performSearch(input.text)
                    },
                    placeholder = {
                        Text(
                            modifier = modifier.alpha(0.5f),
                            text = stringResource(id = RPresentationList.string.search_placeholder),
                        )
                    },
                    textStyle = MaterialTheme.typography.bodyMedium,
                    singleLine = true,
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                if (searchText.text.isNotEmpty()) {
                                    videoListMVI.submitAction(UiVideoListAction.TypeInSearchAppBarTextField(""))
                                }
                                else {
                                    videoListMVI.submitAction(UiVideoListAction.ChangeSearchBarAppearance(SearchState.CLOSED))
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = stringResource(id = RPresentationList.string.appbar_close_icon_descr),
                            )
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Search
                    ),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            performSearch(searchText.text)
                            keyboardController?.hide()
                        }
                    )
                )
            },
            scrollBehavior = scrollAppBarBehaviour
        )
    }
}

@PreviewLightDark
@Composable
fun PreviewSearchAppBarBlueTube() {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    BlueTubeComposeTheme {
        Surface {
            SearchAppBarBlueTube(
                searchText = TextFieldValue(""),
                videoListMVI = PREVIEW_VIDEO_LIST_MVI,
                scrollAppBarBehaviour = scrollBehavior,
                modifier = Modifier,
            )
        }
    }
}

@PreviewLightDark
@Composable
fun PreviewSearchAppBarBlueTubeWithText() {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    BlueTubeComposeTheme {
        Surface {
            SearchAppBarBlueTube(
                searchText = TextFieldValue("Test text"),
                videoListMVI = PREVIEW_VIDEO_LIST_MVI,
                scrollAppBarBehaviour = scrollBehavior,
                modifier = Modifier,
            )
        }
    }
}
